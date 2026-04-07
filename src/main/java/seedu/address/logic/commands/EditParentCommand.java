package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT_PHONE;

import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Email;
import seedu.address.model.person.Guardian;
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
            + ": Sets the parent details of the student identified by the index number in the displayed "
            + "student list. At least one optional field must be provided.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_PARENT_NAME + "PARENT_NAME] "
            + "[" + PREFIX_PARENT_PHONE + "PARENT_PHONE] "
            + "[" + PREFIX_PARENT_EMAIL + "PARENT_EMAIL]\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 3 "
            + PREFIX_PARENT_NAME + "John Lim " + PREFIX_PARENT_PHONE + "91234567 "
            + PREFIX_PARENT_EMAIL + "johnlim@example.com";

    public static final String MESSAGE_EDIT_PARENT_SUCCESS = "Edited parent details of student: %1$s";
    public static final String MESSAGE_MISSING_PARENT_NAME =
            "Parent name must be provided when adding parent phone or email to a student without existing parent "
                    + "details.";

    private final EditParentDescriptor editParentDescriptor;

    /**
     * @param index of the person in the filtered person list whose parent details will be set
     * @param editParentDescriptor details to edit the parent with
     */
    public EditParentCommand(Index index, EditParentDescriptor editParentDescriptor) {
        super(index);
        requireNonNull(editParentDescriptor);
        if (!editParentDescriptor.isAnyFieldEdited()) {
            throw new IllegalArgumentException(MESSAGE_NOT_EDITED);
        }

        this.editParentDescriptor = new EditParentDescriptor(editParentDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Person personToEdit = getTargetPerson(model);
        PersonBuilder builder = new PersonBuilder(personToEdit);

        Optional<Guardian> existingGuardian = personToEdit.getGuardian();
        Name name = editParentDescriptor.getParentName()
                .orElse(existingGuardian.map(Guardian::getName).orElse(null));
        Phone phone = editParentDescriptor.getParentPhone()
                .orElse(existingGuardian.flatMap(Guardian::getPhone).orElse(null));
        Email email = editParentDescriptor.getParentEmail()
                .orElse(existingGuardian.flatMap(Guardian::getEmail).orElse(null));

        if (name == null && (phone != null || email != null)) {
            throw new CommandException(MESSAGE_MISSING_PARENT_NAME);
        }

        if (name != null) {
            builder.withGuardian(new Guardian(name, phone, email));
        }

        Person editedPerson = builder.build();

        replacePerson(model, personToEdit, editedPerson);
        return new CommandResult(
                String.format(MESSAGE_EDIT_PARENT_SUCCESS, Messages.format(editedPerson)),
                editedPerson);
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
                && editParentDescriptor.equals(otherCommand.editParentDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editParentDescriptor", editParentDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the parent with. Each non-empty field value will replace the
     * corresponding parent field value of the person.
     */
    public static class EditParentDescriptor {
        private Optional<Name> parentName;
        private Optional<Phone> parentPhone;
        private Optional<Email> parentEmail;

        /**
         * Creates an empty descriptor with no parent fields set for editing.
         */
        public EditParentDescriptor() {
            parentName = Optional.empty();
            parentPhone = Optional.empty();
            parentEmail = Optional.empty();
        }

        /**
         * Copy constructor.
         */
        public EditParentDescriptor(EditParentDescriptor toCopy) {
            requireNonNull(toCopy);
            parentName = toCopy.parentName;
            parentPhone = toCopy.parentPhone;
            parentEmail = toCopy.parentEmail;
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return parentName.isPresent() || parentPhone.isPresent() || parentEmail.isPresent();
        }

        public void setParentName(Name parentName) {
            this.parentName = Optional.of(requireNonNull(parentName));
        }

        public Optional<Name> getParentName() {
            return parentName;
        }

        public void setParentPhone(Phone parentPhone) {
            this.parentPhone = Optional.of(requireNonNull(parentPhone));
        }

        public Optional<Phone> getParentPhone() {
            return parentPhone;
        }

        public void setParentEmail(Email parentEmail) {
            this.parentEmail = Optional.of(requireNonNull(parentEmail));
        }

        public Optional<Email> getParentEmail() {
            return parentEmail;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof EditParentDescriptor)) {
                return false;
            }

            EditParentDescriptor e = (EditParentDescriptor) other;
            return Objects.equals(parentName, e.parentName)
                    && Objects.equals(parentPhone, e.parentPhone)
                    && Objects.equals(parentEmail, e.parentEmail);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("parentName", parentName.orElse(null))
                    .add("parentPhone", parentPhone.orElse(null))
                    .add("parentEmail", parentEmail.orElse(null))
                    .toString();
        }
    }
}
