package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.academic.Academics;
import seedu.address.model.academic.Subject;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code DeleteAcadCommand}.
 */
public class DeleteAcadCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_deleteSingleSubject_success() {
        Person personToEdit = model.getFilteredPersonList().get(0);

        List<Subject> sortedSubjects = personToEdit.getAcademics().getSortedSubjects();
        Index subjectIndex = Index.fromZeroBased(0);

        Subject subjectToDelete = sortedSubjects.get(subjectIndex.getZeroBased());

        DeleteAcadCommand command =
                new DeleteAcadCommand(Index.fromZeroBased(0), List.of(subjectIndex));

        // expected person
        Academics oldAcademics = personToEdit.getAcademics();
        Academics expectedAcademics = new Academics(
                oldAcademics.getSubjects().stream()
                        .filter(s -> !s.equals(subjectToDelete))
                        .collect(java.util.stream.Collectors.toSet()),
                oldAcademics.getDescription());

        Person expectedPerson = new PersonBuilder(personToEdit)
                .withAcademics(expectedAcademics)
                .build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, expectedPerson);

        String expectedMessage = String.format(
                DeleteAcadCommand.MESSAGE_DELETE_SUBJECT_SUCCESS,
                subjectToDelete);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteMultipleSubjects_success() {
        // get existing person
        Person personToEdit = model.getFilteredPersonList().get(0);

        // create subjects (Level can be null)
        Subject math = new Subject("Math", null);
        Subject physics = new Subject("Physics", null);

        Academics academics = new Academics(
                Set.of(math, physics),
                personToEdit.getAcademics().getDescription()
        );

        // update person with subjects
        Person updatedPerson = new PersonBuilder(personToEdit)
                .withAcademics(academics)
                .build();

        model.setPerson(personToEdit, updatedPerson);

        Person target = model.getFilteredPersonList().get(0);
        List<Subject> sortedSubjects = target.getAcademics().getSortedSubjects();

        Index index1 = Index.fromZeroBased(0);
        Index index2 = Index.fromZeroBased(1);

        DeleteAcadCommand command =
                new DeleteAcadCommand(Index.fromZeroBased(0), List.of(index1, index2));

        // expected result: empty subjects
        Academics expectedAcademics = new Academics(
                Set.of(),
                target.getAcademics().getDescription()
        );

        Person expectedPerson = new PersonBuilder(target)
                .withAcademics(expectedAcademics)
                .build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(target, expectedPerson);

        String expectedMessage = String.format(
                DeleteAcadCommand.MESSAGE_DELETE_SUBJECT_SUCCESS,
                sortedSubjects.get(0) + ", " + sortedSubjects.get(1));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_messageFormat_noTrailingComma() throws Exception {
        Person personToEdit = model.getFilteredPersonList().get(0);

        Subject math = new Subject("Math", null);
        Subject physics = new Subject("Physics", null);

        Academics academics = new Academics(
                Set.of(math, physics),
                personToEdit.getAcademics().getDescription()
        );

        Person updatedPerson = new PersonBuilder(personToEdit)
                .withAcademics(academics)
                .build();

        model.setPerson(personToEdit, updatedPerson);

        DeleteAcadCommand command = new DeleteAcadCommand(
                Index.fromZeroBased(0),
                List.of(Index.fromZeroBased(0), Index.fromZeroBased(1)));

        CommandResult result = command.execute(model);

        org.junit.jupiter.api.Assertions.assertFalse(
                result.getFeedbackToUser().endsWith(", "));
    }

    @Test
    public void execute_invalidSubjectIndex_throwsCommandException() {
        Person personToEdit = model.getFilteredPersonList().get(0);

        Subject math = new Subject("Math", null);

        Academics academics = new Academics(
                Set.of(math),
                personToEdit.getAcademics().getDescription()
        );

        Person updatedPerson = new PersonBuilder(personToEdit)
                .withAcademics(academics)
                .build();

        model.setPerson(personToEdit, updatedPerson);

        DeleteAcadCommand command = new DeleteAcadCommand(
                Index.fromZeroBased(0),
                List.of(Index.fromZeroBased(1)));

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_subjectIndexEqualToSize_throwsCommandException() {
        Person personToEdit = model.getFilteredPersonList().get(0);

        Subject math = new Subject("Math", null);

        Academics academics = new Academics(
                Set.of(math),
                personToEdit.getAcademics().getDescription()
        );

        Person updatedPerson = new PersonBuilder(personToEdit)
                .withAcademics(academics)
                .build();

        model.setPerson(personToEdit, updatedPerson);

        DeleteAcadCommand command = new DeleteAcadCommand(
                Index.fromZeroBased(0),
                List.of(Index.fromZeroBased(1)));

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void equals() {
        Index index = Index.fromOneBased(1);
        List<Index> subjectIndices = List.of(Index.fromOneBased(1));

        DeleteAcadCommand command1 = new DeleteAcadCommand(index, subjectIndices);
        DeleteAcadCommand command2 = new DeleteAcadCommand(index, subjectIndices);

        // same object
        assertEquals(command1, command1);

        // same values
        assertEquals(command1, command2);

        // null
        assertEquals(false, command1.equals(null));

        // different type
        assertEquals(false, command1.equals(new ClearCommand()));

        // different index
        assertEquals(false,
                command1.equals(new DeleteAcadCommand(Index.fromOneBased(2), subjectIndices)));

        // different subject indices
        assertEquals(false,
                command1.equals(new DeleteAcadCommand(index,
                        List.of(Index.fromOneBased(2)))));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        List<Index> subjectIndices = List.of(Index.fromOneBased(2), Index.fromOneBased(3));

        DeleteAcadCommand command = new DeleteAcadCommand(index, subjectIndices);

        String expected = DeleteAcadCommand.class.getCanonicalName()
                + "{index=" + index + ", subjectIndices=" + subjectIndices + "}";

        org.junit.jupiter.api.Assertions.assertEquals(expected, command.toString());
    }
}
