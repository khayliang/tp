package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Base command for operations that target a person from the filtered list by index.
 */
public abstract class IndexedPersonCommand extends Command {

    protected final Index index;

    protected IndexedPersonCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    /**
     * Returns the target person from the currently filtered person list.
     *
     * @throws CommandException if the index is out of bounds
     */
    protected Person getTargetPerson(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        return lastShownList.get(index.getZeroBased());
    }

    /**
     * Replaces {@code personToEdit} with {@code editedPerson} and refreshes the filtered list.
     */
    protected void replacePersonAndShowAll(Model model, Person personToEdit, Person editedPerson) {
        requireNonNull(model);
        requireNonNull(personToEdit);
        requireNonNull(editedPerson);
        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }
}
