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
import seedu.address.model.academic.Academics;
import seedu.address.model.academic.Subject;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;

/**
 * Adds subject(s) to an existing person's academics.
 * Existing subjects will be kept, and new subjects will be added.
 */
public class AddAcademicsCommand extends AddCommand {

    public static final String SUB_COMMAND_WORD = "acad";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Adds subject(s) to the student identified by the index number used in the displayed student list. "
            + "Existing subjects will be kept, and new subjects will be added.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_SUBJECT + "SUBJECT [" + PREFIX_LEVEL + "LEVEL]...\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 "
            + PREFIX_SUBJECT + "Math " + PREFIX_LEVEL + "Strong "
            + PREFIX_SUBJECT + "Science";

    public static final String MESSAGE_ADD_ACADEMICS_SUCCESS =
            "Added Subjects to Person: %1$s";

    private final Index index;
    private final Set<Subject> subjectsToAdd;

    /**
     * @param index of the person in the displayed list
     * @param subjectsToAdd subjects to be added to existing academics
     */
    public AddAcademicsCommand(Index index, Set<Subject> subjectsToAdd) {
        requireNonNull(index);
        requireNonNull(subjectsToAdd);

        this.index = index;
        this.subjectsToAdd = new HashSet<>(subjectsToAdd);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person personToEdit = getTargetPerson(model, index);

        Set<Subject> updatedSubjects = new HashSet<>(
                personToEdit.getAcademics().getSubjects()
        );

        // Upsert: for each new subject, remove existing with same name, then add
        for (Subject newSub : subjectsToAdd) {
            updatedSubjects.removeIf(s -> s.getName().equals(newSub.getName()));
            updatedSubjects.add(newSub);
        }

        Academics updatedAcademics = new Academics(updatedSubjects,
                personToEdit.getAcademics().getDescription());

        Person editedPerson = new PersonBuilder(personToEdit)
                .withAcademics(updatedAcademics)
                .build();

        model.setPerson(personToEdit, editedPerson);

        return new CommandResult(
                String.format(MESSAGE_ADD_ACADEMICS_SUCCESS,
                        Messages.format(editedPerson)),
                editedPerson);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddAcademicsCommand)) {
            return false;
        }

        AddAcademicsCommand otherCommand = (AddAcademicsCommand) other;
        return index.equals(otherCommand.index)
                && subjectsToAdd.equals(otherCommand.subjectsToAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("subjectsToAdd", subjectsToAdd)
                .toString();
    }
}
