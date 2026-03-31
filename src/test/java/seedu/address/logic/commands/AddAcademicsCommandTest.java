package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.ListDisplayMode;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.academic.Academics;
import seedu.address.model.academic.Subject;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;

/**
 * Contains integration tests and unit tests for AddAcademicsCommand.
 */
public class AddAcademicsCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Subject> subjectsToAdd = new HashSet<>();
        subjectsToAdd.add(new Subject("Math", null));

        AddAcademicsCommand command = new AddAcademicsCommand(INDEX_FIRST_PERSON, subjectsToAdd);

        Set<Subject> expectedSubjects = new HashSet<>(personToEdit.getAcademics().getSubjects());
        expectedSubjects.addAll(subjectsToAdd);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withAcademics(new Academics(expectedSubjects))
                .build();

        String expectedMessage = String.format(AddAcademicsCommand.MESSAGE_ADD_ACADEMICS_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(command, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_duplicateSubject_noChangeButSuccess() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Subject existing = personToEdit.getAcademics().getSubjects().iterator().next();
        Set<Subject> subjectsToAdd = new HashSet<>();
        subjectsToAdd.add(existing);

        AddAcademicsCommand command = new AddAcademicsCommand(INDEX_FIRST_PERSON, subjectsToAdd);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withAcademics(personToEdit.getAcademics()) // no change
                .build();

        String expectedMessage = String.format(AddAcademicsCommand.MESSAGE_ADD_ACADEMICS_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(command, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_multipleSubjects_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Subject> subjectsToAdd = new HashSet<>();
        subjectsToAdd.add(new Subject("Physics", null));
        subjectsToAdd.add(new Subject("Chemistry", null));

        AddAcademicsCommand command = new AddAcademicsCommand(INDEX_FIRST_PERSON, subjectsToAdd);

        Set<Subject> expectedSubjects = new HashSet<>(personToEdit.getAcademics().getSubjects());
        expectedSubjects.addAll(subjectsToAdd);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withAcademics(new Academics(expectedSubjects))
                .build();

        String expectedMessage = String.format(AddAcademicsCommand.MESSAGE_ADD_ACADEMICS_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(command, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        Set<Subject> subjectsToAdd = new HashSet<>();
        subjectsToAdd.add(new Subject("Math", null));

        AddAcademicsCommand command = new AddAcademicsCommand(outOfBoundIndex, subjectsToAdd);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_SECOND_PERSON);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Subject> subjectsToAdd = new HashSet<>();
        subjectsToAdd.add(new Subject("FilteredSub", null));

        AddAcademicsCommand command = new AddAcademicsCommand(INDEX_FIRST_PERSON, subjectsToAdd);

        Set<Subject> expectedSubjects = new HashSet<>(personToEdit.getAcademics().getSubjects());
        expectedSubjects.addAll(subjectsToAdd);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withAcademics(new Academics(expectedSubjects))
                .build();

        String expectedMessage = String.format(AddAcademicsCommand.MESSAGE_ADD_ACADEMICS_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        showPersonAtIndex(expectedModel, INDEX_SECOND_PERSON);

        Person expectedPersonToEdit =
                expectedModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        expectedModel.setPerson(expectedPersonToEdit, editedPerson);

        CommandResult expectedResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(command, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_validIndexDifferentDisplayMode_success() {
        model.setListDisplayMode(ListDisplayMode.APPOINTMENT);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Subject> subjectsToAdd = new HashSet<>();
        subjectsToAdd.add(new Subject("ApptSub", null));

        AddAcademicsCommand command = new AddAcademicsCommand(INDEX_FIRST_PERSON, subjectsToAdd);

        Set<Subject> expectedSubjects = new HashSet<>(personToEdit.getAcademics().getSubjects());
        expectedSubjects.addAll(subjectsToAdd);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withAcademics(new Academics(expectedSubjects))
                .build();

        String expectedMessage = String.format(AddAcademicsCommand.MESSAGE_ADD_ACADEMICS_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setListDisplayMode(ListDisplayMode.APPOINTMENT);
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(command, model, expectedResult, expectedModel);
    }

    @Test
    public void equals() {
        Set<Subject> subjects = new HashSet<>();
        subjects.add(new Subject("Math", null));

        AddAcademicsCommand standardCommand = new AddAcademicsCommand(INDEX_FIRST_PERSON, subjects);

        AddAcademicsCommand sameCommand = new AddAcademicsCommand(INDEX_FIRST_PERSON, subjects);
        AddAcademicsCommand differentIndex = new AddAcademicsCommand(INDEX_SECOND_PERSON, subjects);

        Set<Subject> differentSubjects = new HashSet<>();
        differentSubjects.add(new Subject("Different", null));
        AddAcademicsCommand differentCommand =
                new AddAcademicsCommand(INDEX_FIRST_PERSON, differentSubjects);

        assertTrue(standardCommand.equals(sameCommand));
        assertTrue(standardCommand.equals(standardCommand));
        assertFalse(standardCommand.equals(null));
        assertFalse(standardCommand.equals(new ClearCommand()));
        assertFalse(standardCommand.equals(differentIndex));
        assertFalse(standardCommand.equals(differentCommand));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);

        Set<Subject> subjects = new HashSet<>();
        subjects.add(new Subject("Math", null));

        AddAcademicsCommand command = new AddAcademicsCommand(index, subjects);

        String expected = AddAcademicsCommand.class.getCanonicalName()
                + "{index=" + index + ", subjectsToAdd=" + subjects + "}";

        assertEquals(expected, command.toString());
    }
}
