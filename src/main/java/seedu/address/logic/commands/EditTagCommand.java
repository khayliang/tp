package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.*;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Edits the tags of an existing person in the address book.
 */
public class EditTagCommand extends EditCommand {

    public static final String SUB_COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the tags of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing tags will be overwritten by the input values.\n"
            + "Parameters: person INDEX (must be a positive integer) "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " " + EditTagCommand.SUB_COMMAND_WORD + " 1 "
            + PREFIX_TAG + "JC " + PREFIX_TAG + "J1";


    public static final String MESSAGE_EDIT_TAG_SUCCESS = "Edited Tags for Person: %1$s";

    private final Index index;
    private final EditTagDescriptor editTagDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editTagDescriptor details to edit the tags with
     */
    public EditTagCommand(Index index, EditTagDescriptor editTagDescriptor) {
        requireNonNull(index);
        requireNonNull(editTagDescriptor);

        this.index = index;
        this.editTagDescriptor = new EditTagDescriptor(editTagDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editTagDescriptor);

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_TAG_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with updated tags.
     */
    private static Person createEditedPerson(Person personToEdit, EditTagDescriptor editTagDescriptor) {
        assert personToEdit != null;

        Set<Tag> updatedTags = editTagDescriptor.getTags().orElse(Collections.emptySet());

        return new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditTagCommand)) {
            return false;
        }

        EditTagCommand otherCommand = (EditTagCommand) other;
        return index.equals(otherCommand.index)
                && editTagDescriptor.equals(otherCommand.editTagDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editTagDescriptor", editTagDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the tag set.
     */
    public static class EditTagDescriptor {

        private Set<Tag> tags;

        public EditTagDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditTagDescriptor(EditTagDescriptor toCopy) {
            setTags(toCopy.tags);
        }

        /**
         * Returns true if tags are edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(tags);
        }

        /**
         * Sets {@code tags}.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof EditTagDescriptor)) {
                return false;
            }

            EditTagDescriptor otherDescriptor = (EditTagDescriptor) other;
            return Objects.equals(tags, otherDescriptor.tags);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("tags", tags)
                    .toString();
        }
    }
}