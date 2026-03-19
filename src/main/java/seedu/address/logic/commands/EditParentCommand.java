package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT_PHONE;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.person.Phone;

/**
 * Edits parent details of an existing person in the address book.
 */
public class EditParentCommand extends EditCommand {

    public static final String SUB_COMMAND_WORD = "parent";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Sets the parent details of the person identified by the index number in the \n"
            + "displayed person list.\n" + "At least one of the optional fields must be provided.\n"
            + "Parameters: INDEX (must be a positive integer) " + "[" + PREFIX_PARENT_NAME + "PARENT_NAME] " + "["
            + PREFIX_PARENT_PHONE + "PARENT_PHONE] " + "[" + PREFIX_PARENT_EMAIL + "PARENT_EMAIL]\n" + "Example: "
            + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 3 " + PREFIX_PARENT_NAME + "John Lim " + PREFIX_PARENT_PHONE
            + "91234567 " + PREFIX_PARENT_EMAIL + "johnlim@example.com";

    public static final String MESSAGE_EDIT_PARENT_SUCCESS = "Edited parent details of Person: %1$s";
    public static final String MESSAGE_NO_FIELD_PROVIDED = "At least one parent field (pn/, pp/, pe/) "
            + "must be provided.";

    private final Optional<Name> parentName;
    private final Optional<Phone> parentPhone;
    private final Optional<Email> parentEmail;

    /**
     * @param index of the person in the filtered person list whose parent details will be set
     * @param parentName the optional parent name to set
     * @param parentPhone the optional parent phone to set
     * @param parentEmail the optional parent email to set
     */
    public EditParentCommand(Index index, Optional<Name> parentName, Optional<Phone> parentPhone,
            Optional<Email> parentEmail) {
        super(index);
        requireNonNull(parentName);
        requireNonNull(parentPhone);
        requireNonNull(parentEmail);

        this.parentName = parentName;
        this.parentPhone = parentPhone;
        this.parentEmail = parentEmail;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Person personToEdit = getTargetPerson(model);
        PersonBuilder builder = new PersonBuilder(personToEdit);
        parentName.ifPresent(pn -> builder.withParentName(Optional.of(pn)));
        parentPhone.ifPresent(pp -> builder.withParentPhone(Optional.of(pp)));
        parentEmail.ifPresent(pe -> builder.withParentEmail(Optional.of(pe)));
        Person editedPerson = builder.build();

        replacePerson(model, personToEdit, editedPerson);
        return new CommandResult(String.format(MESSAGE_EDIT_PARENT_SUCCESS, Messages.format(editedPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditParentCommand)) {
            return false;
        }

        EditParentCommand otherCommand = (EditParentCommand) other;
        return index.equals(otherCommand.index) && parentName.equals(otherCommand.parentName)
                && parentPhone.equals(otherCommand.parentPhone) && parentEmail.equals(otherCommand.parentEmail);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("index", index).add("parentName", parentName.orElse(null))
                .add("parentPhone", parentPhone.orElse(null)).add("parentEmail", parentEmail.orElse(null)).toString();
    }
}
