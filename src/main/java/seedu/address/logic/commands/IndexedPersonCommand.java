package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ListDisplayMode;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonComparators;

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
     * Returns the target person from the currently displayed person list.
     *
     * @throws CommandException if the index is out of bounds
     */
    protected Person getTargetPerson(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = getDisplayedPersonList(model);

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        return lastShownList.get(index.getZeroBased());
    }

    /**
     * Returns persons in the same order as currently displayed in the UI.
     */
    private List<Person> getDisplayedPersonList(Model model) {
        if (model.getListDisplayMode() == ListDisplayMode.APPOINTMENT) {
            return model.getFilteredPersonList().stream()
                    .sorted(PersonComparators.APPOINTMENT_ORDER)
                    .toList();
        }
        return model.getFilteredPersonList();
    }

    /**
     * Replaces {@code personToEdit} with {@code editedPerson} while preserving the current filter.
     */
    protected void replacePerson(Model model, Person personToEdit, Person editedPerson) {
        requireNonNull(model);
        requireNonNull(personToEdit);
        requireNonNull(editedPerson);
        model.setPerson(personToEdit, editedPerson);
    }
}
