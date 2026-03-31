package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.Objects;
import java.util.Optional;
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
 * Edits the academics of an existing person in the address book.
 */
public class EditAcademicsCommand extends EditCommand {

    public static final String SUB_COMMAND_WORD = "acad";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Edits the academics of the student identified by the index number used in the displayed "
            + "student list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_SUBJECT + "SUBJECT [" + PREFIX_LEVEL + "LEVEL]]... "
            + "[" + PREFIX_DESCRIPTION + "DESCRIPTION]\n"
            + "Use " + PREFIX_SUBJECT + " to set subjects, or " + PREFIX_SUBJECT + " to clear all subjects.\n"
            + "Use " + PREFIX_DESCRIPTION + " to set description, or " + PREFIX_DESCRIPTION + " to clear it.\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 "
            + PREFIX_SUBJECT + "Math " + PREFIX_LEVEL + "Strong "
            + PREFIX_DESCRIPTION + "Good progress";

    public static final String MESSAGE_EDIT_ACADEMICS_SUCCESS =
            "Edited Academics for Person: %1$s";

    private final EditAcademicsDescriptor descriptor;

    /**
     * Creates an {@code EditAcademicsCommand} to edit the academics of a person.
     *
     * @param index Index of the person in the filtered person list to edit.
     * @param descriptor Details of the academics fields to edit. At least one field must be non-null.
     */
    public EditAcademicsCommand(Index index, EditAcademicsDescriptor descriptor) {
        super(index);
        requireNonNull(descriptor);
        this.descriptor = descriptor;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Person personToEdit = getTargetPerson(model);

        Academics current = personToEdit.getAcademics();

        Set<Subject> newSubjects = current.getSubjects();
        if (descriptor.isSubjectsEdited()) {
            newSubjects = descriptor.getSubjects();
        }

        Optional<String> newNote = current.getDescription();
        if (descriptor.isNoteEdited()) {
            String note = descriptor.getNote();
            newNote = (note == null || note.isEmpty())
                    ? Optional.empty()
                    : Optional.of(note);
        }

        Academics updatedAcademics = new Academics(newSubjects, newNote);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withAcademics(updatedAcademics)
                .build();

        replacePerson(model, personToEdit, editedPerson);

        return new CommandResult(
                String.format(MESSAGE_EDIT_ACADEMICS_SUCCESS,
                        Messages.format(editedPerson)),
                editedPerson);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof EditAcademicsCommand
                && index.equals(((EditAcademicsCommand) other).index)
                && descriptor.equals(((EditAcademicsCommand) other).descriptor));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("descriptor", descriptor)
                .toString();
    }

    // ================= Descriptor =================

    /**
     * Stores the details to edit the academics with.
     * Each non-null field value will replace the corresponding field value of the person.
     */
    public static class EditAcademicsDescriptor {

        private Set<Subject> subjects;
        private String note; // nullable → null means "not edited"

        public EditAcademicsDescriptor() {}

        public boolean isSubjectsEdited() {
            return subjects != null;
        }

        public boolean isNoteEdited() {
            return note != null;
        }

        public void setSubjects(Set<Subject> subjects) {
            this.subjects = subjects;
        }

        public Set<Subject> getSubjects() {
            return subjects;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getNote() {
            return note;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof EditAcademicsDescriptor)) {
                return false;
            }

            EditAcademicsDescriptor otherDesc = (EditAcademicsDescriptor) other;
            return Objects.equals(subjects, otherDesc.subjects)
                    && Objects.equals(note, otherDesc.note);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("subjects", subjects)
                    .add("note", note)
                    .toString();
        }
    }
}
