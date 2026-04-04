package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

public class IndexedPersonResolverTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void getTargetPerson_personMode_returnsFilteredListIndex() throws Exception {
        Person expected = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Person actual = IndexedPersonResolver.getTargetPerson(model, INDEX_FIRST_PERSON);

        assertEquals(expected, actual);
    }

    @Test
    public void getTargetPerson_filteredList_returnsFilteredListIndex() throws Exception {
        model.updateFilteredPersonList(person -> person.getName().fullName.startsWith("F"));
        Person expected = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Person actual = IndexedPersonResolver.getTargetPerson(model, INDEX_FIRST_PERSON);

        assertEquals(expected, actual);
    }

    @Test
    public void getTargetPerson_indexOutOfBounds_throwsCommandException() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        CommandException commandException = assertThrows(
                CommandException.class, () ->
                        IndexedPersonResolver.getTargetPerson(model, outOfBoundsIndex));

        assertEquals(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, commandException.getMessage());
    }

    @Test
    public void getTargetPerson_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                IndexedPersonResolver.getTargetPerson(null, INDEX_FIRST_PERSON));
    }

    @Test
    public void getTargetPerson_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                IndexedPersonResolver.getTargetPerson(model, null));
    }
}
