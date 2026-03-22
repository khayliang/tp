package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class IndexedPersonCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    private static class DummyIndexedPersonCommand extends IndexedPersonCommand {
        DummyIndexedPersonCommand(Index index) {
            super(index);
        }

        @Override
        public CommandResult execute(Model model) throws CommandException {
            Person targetPerson = getTargetPerson(model);
            Person editedPerson = new PersonBuilder(targetPerson).withName("New Name").build();
            replacePerson(model, targetPerson, editedPerson);
            return new CommandResult("Dummy success");
        }
    }

    @Test
    public void execute_validIndex_success() throws Exception {
        DummyIndexedPersonCommand dummyCommand = new DummyIndexedPersonCommand(INDEX_FIRST_PERSON);
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personToEdit).withName("New Name").build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        dummyCommand.execute(model);
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DummyIndexedPersonCommand dummyCommand = new DummyIndexedPersonCommand(outOfBoundIndex);

        assertThrows(CommandException.class, () -> dummyCommand.execute(model));
    }
}
