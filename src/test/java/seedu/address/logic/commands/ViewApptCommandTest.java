package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.ListDisplayMode;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.AppointmentInWeekPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code ViewApptCommand}.
 */
public class ViewApptCommandTest {
    private static final LocalDate TARGET_DATE = LocalDate.parse("2026-02-13");

    private Model model = buildModelWithAppointments();
    private Model expectedModel = buildModelWithAppointments();

    @Test
    public void execute_validDate_success() {
        ViewApptCommand command = new ViewApptCommand(TARGET_DATE);
        AppointmentInWeekPredicate predicate = new AppointmentInWeekPredicate(TARGET_DATE);
        expectedModel.updateFilteredPersonList(predicate);
        expectedModel.setListDisplayMode(ListDisplayMode.APPOINTMENT);

        String expectedMessage = String.format(ViewApptCommand.MESSAGE_SUCCESS,
                expectedModel.getFilteredPersonList().size(), "2026-02-09", "2026-02-15");
        CommandResult expectedResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, model, expectedResult, expectedModel);
        assertEquals(2, model.getFilteredPersonList().size());
    }

    @Test
    public void equals() {
        ViewApptCommand firstCommand = new ViewApptCommand(LocalDate.parse("2026-02-13"));
        ViewApptCommand secondCommand = new ViewApptCommand(LocalDate.parse("2026-03-13"));

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(new ViewApptCommand(LocalDate.parse("2026-02-13"))));
        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(secondCommand));
    }

    @Test
    public void toStringMethod() {
        ViewApptCommand command = new ViewApptCommand(TARGET_DATE);
        String expected = ViewApptCommand.class.getCanonicalName() + "{targetDate=" + TARGET_DATE + "}";
        assertEquals(expected, command.toString());
    }

    private static Model buildModelWithAppointments() {
        AddressBook addressBook = new AddressBook();
        Person aliceWithAppt = new PersonBuilder(ALICE).withAppointmentStart("2026-02-09T08:00:00").build();
        Person bensonWithAppt = new PersonBuilder(BENSON).withAppointmentStart("2026-02-15T15:30:00").build();
        Person carlWithAppt = new PersonBuilder(CARL).withAppointmentStart("2026-02-16T10:00:00").build();
        addressBook.addPerson(aliceWithAppt);
        addressBook.addPerson(bensonWithAppt);
        addressBook.addPerson(carlWithAppt);
        return new ModelManager(addressBook, new UserPrefs());
    }
}
