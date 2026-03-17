package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.tag.Tag;

/**
 * Edits the tags of an existing person in the address book.
 */
public class EditTagCommand extends EditCommand {

    public static final String SUB_COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the tags of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing tags will be overwritten by the input values.\n"
            + "Parameters: " + SUB_COMMAND_WORD + " INDEX (must be a positive integer) "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " " + EditTagCommand.SUB_COMMAND_WORD + " 1 "
            + PREFIX_TAG + "JC " + PREFIX_TAG + "J1";


    public static final String MESSAGE_EDIT_TAG_SUCCESS = "Edited Tags for Person: %1$s";

    private final Set<Tag> tags;

    /**
     * @param index of the person in the filtered person list to edit
     * @param tags new tag set (empty set means clear all tags)
     */
    public EditTagCommand(Index index, Set<Tag> tags) {
        super(index);
        requireNonNull(tags);

        this.tags = new HashSet<>(tags);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Person personToEdit = getTargetPerson(model);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withTags(tags)
                .build();

        replacePersonAndShowAll(model, personToEdit, editedPerson);

        return new CommandResult(
                String.format(MESSAGE_EDIT_TAG_SUCCESS, Messages.format(editedPerson)));
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
                && tags.equals(otherCommand.tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("tags", tags)
                .toString();
    }
}
