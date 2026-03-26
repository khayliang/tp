package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.academic.Academics;
import seedu.address.model.academic.Subject;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;

/**
 * Edits the academics of an existing person in the address book.
 */
public class EditAcademicsCommand extends EditCommand {

    public static final String SUB_COMMAND_WORD = "acad";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Edits the academics of the student identified by the index number used in the displayed "
            + "student list. Existing academics will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_SUBJECT + "SUBJECT [" + PREFIX_LEVEL + "LEVEL]]...\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 "
            + PREFIX_SUBJECT + "Math " + PREFIX_LEVEL + "Strong "
            + PREFIX_SUBJECT + "Science";

    public static final String MESSAGE_EDIT_ACADEMICS_SUCCESS = "Edited Academics for Person: %1$s";

    private final Academics academics;

    /**
     * Creates an {@code EditAcademicsCommand} to edit the academics of a person.
     *
     * @param index Index of the person in the filtered person list to edit.
     * @param academics New set of {@link Subject}s to assign to the person.
     *                 An empty set clears all existing academics.
     */
    public EditAcademicsCommand(Index index, Academics academics) {
        super(index);
        requireNonNull(academics);
        this.academics = academics;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Person personToEdit = getTargetPerson(model);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withAcademics(academics)
                .build();

        replacePerson(model, personToEdit, editedPerson);

        return new CommandResult(
                String.format(MESSAGE_EDIT_ACADEMICS_SUCCESS, Messages.format(editedPerson)),
                editedPerson);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditAcademicsCommand)) {
            return false;
        }

        EditAcademicsCommand otherCommand = (EditAcademicsCommand) other;
        return index.equals(otherCommand.index)
                && academics.equals(otherCommand.academics);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("academics", academics)
                .toString();
    }
}
