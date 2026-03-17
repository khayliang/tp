package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT_NAME;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.ParentName;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;

/**
 * Edits the parent name of an existing person in the address book.
 */
public class EditParentCommand extends EditCommand {

    public static final String SUB_COMMAND_WORD = "parent";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Sets the parent name of the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_PARENT_NAME + "PARENT_NAME\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 3 " + PREFIX_PARENT_NAME + "John Lim";

    public static final String MESSAGE_EDIT_PARENT_SUCCESS = "Edited parent name of Person: %1$s";

    private final ParentName parentName;

    /**
     * @param index of the person in the filtered person list whose parent name will be set
     * @param parentName the parent name to set
     */
    public EditParentCommand(Index index, ParentName parentName) {
        super(index);
        requireNonNull(parentName);

        this.parentName = parentName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Person personToEdit = getTargetPerson(model);
        Person editedPerson = new PersonBuilder(personToEdit)
                .withParentName(Optional.of(parentName))
                .build();

        replacePersonAndShowAll(model, personToEdit, editedPerson);
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
        return index.equals(otherCommand.index)
                && parentName.equals(otherCommand.parentName);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("parentName", parentName)
                .toString();
    }
}
