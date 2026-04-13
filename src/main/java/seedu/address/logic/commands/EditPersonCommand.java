package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.person.Phone;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditPersonCommand extends EditCommand {

    public static final String SUB_COMMAND_WORD = "student";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Edits the details of the student identified "
            + "by the index number used in the displayed student list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "Example: " + COMMAND_WORD + " " + EditPersonCommand.SUB_COMMAND_WORD + " 1 "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Updated student details: %1$s.";
    public static final String MESSAGE_DUPLICATE_PERSON =
            "A student with the same name and email already exists in TutorFlow.";

    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditPersonCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        super(index);
        requireNonNull(editPersonDescriptor);
        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new IllegalArgumentException(MESSAGE_NOT_EDITED);
        }

        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Person personToEdit = getTargetPerson(model);
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        replacePerson(model, personToEdit, editedPerson);
        return new CommandResult(
                String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)),
                editedPerson);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        PersonBuilder builder = new PersonBuilder(personToEdit);
        editPersonDescriptor.getName().ifPresent(builder::withName);
        editPersonDescriptor.getPhone().ifPresent(builder::withPhone);
        editPersonDescriptor.getEmail().ifPresent(builder::withEmail);
        editPersonDescriptor.getAddress().ifPresent(builder::withAddress);
        return builder.build();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditPersonCommand)) {
            return false;
        }

        EditPersonCommand otherEditCommand = (EditPersonCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(index, editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Optional<Name> name;
        private Optional<Phone> phone;
        private Optional<Email> email;
        private Optional<Address> address;

        /**
         * Creates an empty descriptor with no fields set for editing.
         */
        public EditPersonDescriptor() {
            name = Optional.empty();
            phone = Optional.empty();
            email = Optional.empty();
            address = Optional.empty();
        }

        /**
         * Copy constructor.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            requireNonNull(toCopy);
            name = toCopy.name;
            phone = toCopy.phone;
            email = toCopy.email;
            address = toCopy.address;
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return name.isPresent() || phone.isPresent() || email.isPresent() || address.isPresent();
        }

        public void setName(Name name) {
            this.name = Optional.of(requireNonNull(name));
        }

        public Optional<Name> getName() {
            return name;
        }

        public void setPhone(Phone phone) {
            this.phone = Optional.of(requireNonNull(phone));
        }

        public Optional<Phone> getPhone() {
            return phone;
        }

        public void setEmail(Email email) {
            this.email = Optional.of(requireNonNull(email));
        }

        public Optional<Email> getEmail() {
            return email;
        }

        public void setAddress(Address address) {
            this.address = Optional.of(requireNonNull(address));
        }

        public Optional<Address> getAddress() {
            return address;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(address, otherEditPersonDescriptor.address);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(name, phone, email, address);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name.orElse(null))
                    .add("phone", phone.orElse(null))
                    .add("email", email.orElse(null))
                    .add("address", address.orElse(null))
                    .toString();
        }
    }
}
