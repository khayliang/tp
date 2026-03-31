package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ListDisplayMode;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.person.PersonComparators;
import seedu.address.model.tag.Tag;

/**
 * Adds tag(s) to an existing person in the address book.
 */
public class AddTagCommand extends AddCommand {

    public static final String SUB_COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Adds tag(s) to the student identified by the index number used in the displayed student list. "
            + "Existing tags will be kept, and new tags will be added.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_TAG + "TAG...\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 "
            + PREFIX_TAG + "friends " + PREFIX_TAG + "CS2103";

    public static final String MESSAGE_ADD_TAG_SUCCESS = "Added Tags to Person: %1$s";

    private final Index index;
    private final Set<Tag> tagsToAdd;

    /**
     * @param index of the person in the filtered person list to edit
     * @param tagsToAdd tags to be added to the person's existing tags
     */
    public AddTagCommand(Index index, Set<Tag> tagsToAdd) {
        requireNonNull(index);
        requireNonNull(tagsToAdd);

        this.index = index;
        this.tagsToAdd = new HashSet<>(tagsToAdd);
    }

    private List<Person> getDisplayedPersonList(Model model) {
        if (model.getListDisplayMode() == ListDisplayMode.APPOINTMENT) {
            return model.getFilteredPersonList().stream()
                    .sorted(PersonComparators.APPOINTMENT_ORDER)
                    .toList();
        }
        return model.getFilteredPersonList();
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = getDisplayedPersonList(model);

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        Set<Tag> updatedTags = new HashSet<>(personToEdit.getTags());
        updatedTags.addAll(tagsToAdd);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withTags(updatedTags)
                .build();

        model.setPerson(personToEdit, editedPerson);

        return new CommandResult(
                String.format(MESSAGE_ADD_TAG_SUCCESS, Messages.format(editedPerson)),
                editedPerson);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddTagCommand)) {
            return false;
        }

        AddTagCommand otherCommand = (AddTagCommand) other;
        return index.equals(otherCommand.index)
                && tagsToAdd.equals(otherCommand.tagsToAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("tagsToAdd", tagsToAdd)
                .toString();
    }
}
