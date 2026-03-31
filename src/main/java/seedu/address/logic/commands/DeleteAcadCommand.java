package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.academic.Academics;
import seedu.address.model.academic.Subject;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;

/**
 * Deletes one or more subjects from a person's academics using index.
 */
public class DeleteAcadCommand extends DeleteCommand {

    public static final String SUB_COMMAND_WORD = "acad";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Deletes subject(s) from the student identified by the index.\n"
            + "Parameters: INDEX s/SUBJECT_INDEX [s/SUBJECT_INDEX]...\n"
            + "Example: delete acad 1 s/2 s/4";

    public static final String MESSAGE_DELETE_SUBJECT_SUCCESS = "Deleted Subject(s): %1$s";

    private final List<Index> subjectIndices;

    /**
     * @param index Index of the person
     * @param subjectIndices Indices of subjects to delete
     */
    public DeleteAcadCommand(Index index, List<Index> subjectIndices) {
        super(index);
        requireNonNull(subjectIndices);
        this.subjectIndices = subjectIndices;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToEdit = getTargetPerson(model);
        Academics academics = personToEdit.getAcademics();
        List<Subject> sortedSubjects = academics.getSortedSubjects();

        // Validate indices
        for (Index subjectIndex : subjectIndices) {
            if (subjectIndex.getZeroBased() >= sortedSubjects.size()) {
                throw new CommandException("Invalid subject index: "
                        + subjectIndex.getOneBased());
            }
        }

        // Remove subjects
        Set<Subject> updatedSubjects = new HashSet<>(academics.getSubjects());
        StringBuilder deletedSubjects = new StringBuilder();

        for (Index subjectIndex : subjectIndices) {
            Subject subjectToDelete = sortedSubjects.get(subjectIndex.getZeroBased());
            updatedSubjects.remove(subjectToDelete);
            deletedSubjects.append(subjectToDelete.toString()).append(", ");
        }

        if (deletedSubjects.length() > 0) {
            deletedSubjects.setLength(deletedSubjects.length() - 2);
        }

        Academics updatedAcademics = new Academics(updatedSubjects, academics.getDescription());

        Person editedPerson = new PersonBuilder(personToEdit)
                .withAcademics(updatedAcademics)
                .build();

        model.setPerson(personToEdit, editedPerson);

        return new CommandResult(String.format(
                MESSAGE_DELETE_SUBJECT_SUCCESS, deletedSubjects));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteAcadCommand)) {
            return false;
        }

        DeleteAcadCommand otherCommand = (DeleteAcadCommand) other;
        return index.equals(otherCommand.index)
                && subjectIndices.equals(otherCommand.subjectIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("subjectIndices", subjectIndices)
                .toString();
    }
}
