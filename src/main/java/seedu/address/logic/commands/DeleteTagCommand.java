package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.tag.Tag;

/**
 * Deletes one or more tags from a person identified using its displayed index.
 */
public class DeleteTagCommand extends DeleteCommand {

    public static final String SUB_COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Deletes tag(s) from the student identified by the index number used in the displayed student list.\n"
            + "Parameters: INDEX t/TAG_INDEX [t/TAG_INDEX]...\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 t/2 t/3";

    public static final String MESSAGE_DELETE_TAG_SUCCESS = "Removed tag(s): %1$s.";

    private final List<Index> tagIndices;

    /**
     * Creates a {@code DeleteTagCommand} to delete the specified tags from the person.
     *
     * @param index Index of the person in the filtered person list.
     * @param tagIndices Indices of the tags to delete from the specified person.
     */
    public DeleteTagCommand(Index index, List<Index> tagIndices) {
        super(index);
        requireNonNull(tagIndices);
        this.tagIndices = new ArrayList<>(tagIndices);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToEdit = getTargetPerson(model);
        List<Tag> sortedTags = personToEdit.getSortedTags();

        // validate indices
        for (Index tagIndex : tagIndices) {
            if (tagIndex.getZeroBased() >= sortedTags.size()) {
                throw new CommandException("Invalid tag index: " + tagIndex.getOneBased());
            }
        }

        // remove tags
        Set<Tag> updatedTags = new HashSet<>(personToEdit.getTags());
        Set<Tag> deletedTagSet = new LinkedHashSet<>();

        for (Index tagIndex : tagIndices) {
            Tag tagToDelete = sortedTags.get(tagIndex.getZeroBased());
            if (updatedTags.remove(tagToDelete)) {
                deletedTagSet.add(tagToDelete);
            }
        }

        StringBuilder deletedTags = new StringBuilder();
        for (Tag tag : deletedTagSet) {
            deletedTags.append(tag.tagName).append(", ");
        }

        if (deletedTags.length() > 0) {
            deletedTags.setLength(deletedTags.length() - 2);
        }

        Person editedPerson = new PersonBuilder(personToEdit)
                .withTags(updatedTags)
                .build();

        model.setPerson(personToEdit, editedPerson);

        return new CommandResult(String.format(MESSAGE_DELETE_TAG_SUCCESS, deletedTags));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteTagCommand)) {
            return false;
        }

        DeleteTagCommand otherCommand = (DeleteTagCommand) other;
        return index.equals(otherCommand.index)
                && tagIndices.equals(otherCommand.tagIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("tagIndices", tagIndices)
                .toString();
    }
}
