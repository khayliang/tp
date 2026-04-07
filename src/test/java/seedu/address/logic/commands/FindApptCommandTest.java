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
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.AppointmentInWeekPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.recurrence.Recurrence;
import seedu.address.model.session.Appointment;

/**
 * Contains integration tests (interaction with the Model) for {@code FindApptCommand}.
 */
public class FindApptCommandTest {
    private static final LocalDate TARGET_DATE = LocalDate.parse("2026-02-13");

    private Model model = buildModelWithAppointments();
    private Model expectedModel = buildModelWithAppointments();

    @Test
    public void execute_validDate_success() {
        FindApptCommand command = new FindApptCommand(TARGET_DATE);
        AppointmentInWeekPredicate predicate = new AppointmentInWeekPredicate(TARGET_DATE);
        expectedModel.updateFilteredPersonList(predicate);

        String expectedMessage = String.format(FindApptCommand.MESSAGE_SUCCESS,
                expectedModel.getFilteredPersonList().size(), "2026-02-09", "2026-02-15");
        CommandResult expectedResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, model, expectedResult, expectedModel);
        assertEquals(2, model.getFilteredPersonList().size());
    }

    @Test
    public void equals() {
        FindApptCommand firstCommand = new FindApptCommand(LocalDate.parse("2026-02-13"));
        FindApptCommand secondCommand = new FindApptCommand(LocalDate.parse("2026-03-13"));

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(new FindApptCommand(LocalDate.parse("2026-02-13"))));
        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(secondCommand));
    }

    @Test
    public void toStringMethod() {
        FindApptCommand command = new FindApptCommand(TARGET_DATE);
        String expected = FindApptCommand.class.getCanonicalName() + "{targetDate=" + TARGET_DATE + "}";
        assertEquals(expected, command.toString());
    }

    private static Model buildModelWithAppointments() {
        AddressBook addressBook = new AddressBook();
        Person aliceWithAppt = new PersonBuilder(ALICE)
            .withAppointment(Appointment.of(
                "2026-02-09T08:00:00", "", Recurrence.NONE))
                .build();
        Person bensonWithAppt = new PersonBuilder(BENSON)
            .withAppointment(Appointment.of(
                "2026-02-15T15:30:00", "", Recurrence.NONE)).build();
        Person carlWithAppt = new PersonBuilder(CARL)
            .withAppointment(Appointment.of(
                "2026-02-16T10:00:00", "", Recurrence.NONE)).build();
        addressBook.addPerson(aliceWithAppt);
        addressBook.addPerson(bensonWithAppt);
        addressBook.addPerson(carlWithAppt);
        return new ModelManager(addressBook, new UserPrefs());
    }
}
