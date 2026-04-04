package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Shared helper for resolving a target person from the currently displayed list by index.
 */
final class IndexedPersonResolver {

    private IndexedPersonResolver() {}

    /**
     * Returns the target person from the currently displayed person list.
     *
     * @throws CommandException if the index is out of bounds
     */
    static Person getTargetPerson(Model model, Index index) throws CommandException {
        requireNonNull(model);
        requireNonNull(index);

        List<Person> displayedPersons = getDisplayedPersonList(model);
        if (index.getZeroBased() >= displayedPersons.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        return displayedPersons.get(index.getZeroBased());
    }

    /**
     * Returns persons in the same order as currently displayed in the UI.
     */
    private static List<Person> getDisplayedPersonList(Model model) {
        return model.getFilteredPersonList();
    }
}
