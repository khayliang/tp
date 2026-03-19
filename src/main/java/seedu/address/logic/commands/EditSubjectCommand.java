package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.subject.Subject;

/**
 * Edits the subjects of an existing person in the address book.
 */
public class EditSubjectCommand extends EditCommand {

    public static final String SUB_COMMAND_WORD = "subject";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the subjects of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing subjects will be overwritten by the input values.\n"
            + "Parameters: person INDEX (must be a positive integer) "
            + "[" + PREFIX_SUBJECT + "SUBJECT [" + PREFIX_LEVEL + "LEVEL]]...\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 "
            + PREFIX_SUBJECT + "Math " + PREFIX_LEVEL + "Strong "
            + PREFIX_SUBJECT + "Science";

    public static final String MESSAGE_EDIT_SUBJECT_SUCCESS = "Edited Subjects for Person: %1$s";

    private final Set<Subject> subjects;

    /**
     * Creates an {@code EditSubjectCommand} to edit the subjects of a person.
     *
     * @param index Index of the person in the filtered person list to edit.
     * @param subjects New set of {@link Subject}s to assign to the person.
     *                 An empty set clears all existing subjects.
     */
    public EditSubjectCommand(Index index, Set<Subject> subjects) {
        super(index);
        requireNonNull(subjects);
        this.subjects = new HashSet<>(subjects);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Person personToEdit = getTargetPerson(model);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withSubjects(subjects)
                .build();

        replacePerson(model, personToEdit, editedPerson);

        return new CommandResult(
                String.format(MESSAGE_EDIT_SUBJECT_SUCCESS, Messages.format(editedPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditSubjectCommand)) {
            return false;
        }

        EditSubjectCommand otherCommand = (EditSubjectCommand) other;
        return index.equals(otherCommand.index)
                && subjects.equals(otherCommand.subjects);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("subjects", subjects)
                .toString();
    }
}
