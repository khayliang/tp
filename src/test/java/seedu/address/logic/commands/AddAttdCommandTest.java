package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ATTENDANCE_DATE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ATTENDANCE_DATE_TIME;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.AppClock;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.attendance.AttendanceRecords;
import seedu.address.model.person.Person;
import seedu.address.model.recurrence.Recurrence;
import seedu.address.model.session.Appointment;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests and unit tests for AddAttdCommand.
 */
public class AddAttdCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_presentWithoutDate_usesCurrentAppointmentDate() {
        Person personToEdit = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment("2026-01-13T08:00:00", "Algebra", Recurrence.NONE)
                .build();
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), personToEdit);

        AddAttdCommand addCommand = new AddAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                true, Optional.empty());

        Person editedPerson = new PersonBuilder(personToEdit)
                .withAppointment(new Appointment(Recurrence.NONE,
                        LocalDateTime.parse("2026-01-13T08:00:00"),
                        LocalDateTime.parse("2026-01-13T08:00:00"),
                        personToEdit.getNextAppointment().orElseThrow().getAttendance()
                                .addAttendance(new Attendance(true, LocalDate.parse("2026-01-13"))),
                        "Algebra"))
                .build();

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        String expectedMessage = String.format(AddAttdCommand.MESSAGE_ADD_ATTD_SUCCESS,
                Messages.format(editedPerson), "present", LocalDate.parse("2026-01-13"))
                + " " + AddAttdCommand.MESSAGE_NON_RECURRING_NEXT_UNCHANGED;

        assertCommandSuccess(addCommand, model,
                new CommandResult(expectedMessage, editedPerson),
                expectedModel);
    }

    @Test
    public void execute_presentWithDate_usesProvidedDate() throws Exception {
        Person personToEdit = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment("2026-01-13T08:00:00", "Algebra", Recurrence.NONE)
                .build();
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), personToEdit);

        AddAttdCommand addCommand = new AddAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, true,
                Optional.of(LocalDateTime.parse(VALID_ATTENDANCE_DATE + "T00:00:00")));

        addCommand.execute(model);

        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Attendance recordedAttendance = editedPerson.getNextAppointment().orElseThrow()
                .getAttendance().getLastRecord().orElseThrow();
        assertTrue(recordedAttendance.hasAttended());
        assertEquals(LocalDateTime.parse(VALID_ATTENDANCE_DATE + "T00:00:00"), recordedAttendance.getRecordedAt());
    }

    @Test
    public void execute_presentWithDateTime_usesProvidedDateTime() throws Exception {
        Person personToEdit = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment("2026-01-13T08:00:00", "Algebra", Recurrence.NONE)
                .build();
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), personToEdit);

        AddAttdCommand addCommand = new AddAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, true,
                Optional.of(LocalDateTime.parse("2026-01-29T08:30:00")));

        addCommand.execute(model);

        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Attendance recordedAttendance = editedPerson.getNextAppointment().orElseThrow()
                .getAttendance().getLastRecord().orElseThrow();
        assertTrue(recordedAttendance.hasAttended());
        assertEquals(LocalDateTime.parse("2026-01-29T08:30:00"), recordedAttendance.getRecordedAt());
    }

    @Test
    public void execute_absentWithoutDate_usesCurrentAppointmentDate() throws Exception {
        Person personToEdit = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment("2026-01-13T08:00:00", "Algebra", Recurrence.NONE)
                .build();
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), personToEdit);

        AddAttdCommand addCommand = new AddAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                false, Optional.empty());
        addCommand.execute(model);

        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Attendance recordedAttendance = editedPerson.getNextAppointment().orElseThrow()
                .getAttendance().getLastRecord().orElseThrow();
        assertFalse(recordedAttendance.hasAttended());
        assertEquals(LocalDateTime.parse("2026-01-13T00:00:00"), recordedAttendance.getRecordedAt());
    }

    @Test
    public void execute_absentWithDate_usesProvidedDate() throws Exception {
        Person personToEdit = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment("2026-01-13T08:00:00", "Algebra", Recurrence.NONE)
                .build();
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), personToEdit);

        AddAttdCommand addCommand = new AddAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                false, Optional.of(LocalDateTime.parse(VALID_ATTENDANCE_DATE + "T00:00:00")));
        addCommand.execute(model);

        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Attendance recordedAttendance = editedPerson.getNextAppointment().orElseThrow()
                .getAttendance().getLastRecord().orElseThrow();
        assertFalse(recordedAttendance.hasAttended());
        assertEquals(LocalDateTime.parse(VALID_ATTENDANCE_DATE + "T00:00:00"), recordedAttendance.getRecordedAt());
    }

    @Test
    public void execute_nonRecurringAppointmentWithExistingAttendance_failure() {
        Person personToEdit = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment("2026-01-13T08:00:00", "Algebra", Recurrence.NONE)
                .addAttendance("2026-01-13T08:00:00")
                .build();
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), personToEdit);

        AddAttdCommand addCommand = new AddAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                true, Optional.empty());

        assertCommandFailure(addCommand, model, AddAttdCommand.MESSAGE_NON_RECURRING_ATTENDANCE_ALREADY_RECORDED);
    }

    @Test
    public void execute_recurringAppointmentWithExistingAttendance_success() throws Exception {
        Person personToEdit = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment("2026-01-13T08:00:00", "Algebra", Recurrence.WEEKLY)
                .addAttendance("2026-01-13T08:00:00")
                .build();
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), personToEdit);

        AddAttdCommand addCommand = new AddAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, true,
                Optional.of(LocalDateTime.parse(VALID_ATTENDANCE_DATE + "T00:00:00")));

        addCommand.execute(model);

        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertEquals(2, editedPerson.getNextAppointment().orElseThrow().getAttendance().getRecords().size());
        assertEquals(LocalDateTime.parse(VALID_ATTENDANCE_DATE + "T00:00:00"), editedPerson.getNextAppointment()
                .orElseThrow().getAttendance().getLastRecord().orElseThrow().getRecordedAt());
    }

    @Test
    public void execute_recurringAppointmentWithSameDateAttendance_failure() {
        Person personToEdit = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment("2026-01-13T08:00:00", "Algebra", Recurrence.WEEKLY)
                .addAttendance("2026-01-13T08:00:00")
                .build();
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), personToEdit);

        AddAttdCommand addCommand = new AddAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, true,
                Optional.of(LocalDateTime.parse("2026-01-13T00:00:00")));

        assertCommandFailure(addCommand, model,
                String.format(AddAttdCommand.MESSAGE_RECURRING_ATTENDANCE_ALREADY_RECORDED_ON_DATE,
                        LocalDate.parse("2026-01-13")));
    }

    @Test
    public void execute_recurringAppointmentWithBackfilledAttendance_doesNotAdvanceNext() throws Exception {
        Person personToEdit = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment("2026-01-20T08:00:00", "Algebra", Recurrence.WEEKLY)
                .addAttendance("2026-01-13T08:00:00")
                .build();
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), personToEdit);

        AddAttdCommand addCommand = new AddAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, true,
                Optional.of(LocalDateTime.parse("2026-01-06T00:00:00")));

        addCommand.execute(model);

        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertEquals(LocalDateTime.parse("2026-01-20T08:00:00"),
                editedPerson.getNextAppointment().orElseThrow().getNext());
        assertEquals(2, editedPerson.getNextAppointment().orElseThrow().getAttendance().getRecords().size());
    }

    @Test
    public void execute_futureAppointmentDate_failure() {
        LocalDate futureDate = AppClock.today().plusDays(1);
        LocalDateTime futureDateTime = futureDate.atTime(8, 0);
        Person personToEdit = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment(new Appointment(Recurrence.NONE, futureDateTime, futureDateTime,
                        AttendanceRecords.EMPTY, "Algebra"))
                .build();
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), personToEdit);

        AddAttdCommand addCommand = new AddAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                true, Optional.empty());

        assertCommandFailure(addCommand, model, AddAttdCommand.MESSAGE_FUTURE_ATTENDANCE_NOT_ALLOWED);
    }

    @Test
    public void execute_futureOverrideDate_failure() {
        LocalDate futureDate = AppClock.today().plusDays(1);
        Person personToEdit = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment("2026-01-13T08:00:00", "Algebra", Recurrence.WEEKLY)
                .build();
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), personToEdit);

        AddAttdCommand addCommand = new AddAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                true, Optional.of(futureDate.atStartOfDay()));

        assertCommandFailure(addCommand, model, AddAttdCommand.MESSAGE_FUTURE_ATTENDANCE_NOT_ALLOWED);
    }

    @Test
    public void execute_invalidAppointmentIndex_failure() {
        Person personToEdit = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment("2026-01-13T08:00:00", "Algebra", Recurrence.NONE)
                .build();
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), personToEdit);

        AddAttdCommand addCommand = new AddAttdCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON,
                true, Optional.empty());
        assertCommandFailure(addCommand, model, AddAttdCommand.MESSAGE_INVALID_APPOINTMENT_INDEX);
    }

    @Test
    public void execute_invalidIndex_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        AddAttdCommand addCommand = new AddAttdCommand(outOfBoundIndex, INDEX_FIRST_PERSON,
                true, Optional.empty());
        assertCommandFailure(addCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_specificAppointmentIndex_updatesSelectedAppointment() throws Exception {
        Appointment firstAppointment = new Appointment(Recurrence.NONE,
                LocalDateTime.parse("2026-01-13T08:00:00"),
                LocalDateTime.parse("2026-01-13T08:00:00"),
                AttendanceRecords.EMPTY,
                "Algebra");
        Appointment secondAppointment = new Appointment(Recurrence.NONE,
                LocalDateTime.parse("2026-01-20T08:00:00"),
                LocalDateTime.parse("2026-01-20T08:00:00"),
                AttendanceRecords.EMPTY,
                "Physics");
        Person personToEdit = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withAppointment(firstAppointment)
                .addAppointment(secondAppointment)
                .build();
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), personToEdit);

        AddAttdCommand addCommand = new AddAttdCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON,
                true, Optional.empty());

        addCommand.execute(model);

        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertTrue(editedPerson.getAppointments().get(0).getAttendance().isEmpty());
        assertEquals(LocalDateTime.parse("2026-01-20T00:00:00"),
                editedPerson.getAppointments().get(1).getAttendance().getLastRecord().orElseThrow()
                        .getRecordedAt());
    }

    @Test
    public void equals() {
        AddAttdCommand standardCommand = new AddAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                true, Optional.empty());
        AddAttdCommand sameValues = new AddAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                true, Optional.empty());

        assertTrue(standardCommand.equals(sameValues));
        assertTrue(standardCommand.equals(standardCommand));
        assertFalse(standardCommand.equals(null));
        assertFalse(standardCommand.equals(new ClearCommand()));
        assertFalse(standardCommand.equals(new AddAttdCommand(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON,
                true, Optional.empty())));
        assertFalse(standardCommand.equals(new AddAttdCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON,
                true, Optional.empty())));
        assertFalse(standardCommand.equals(
                new AddAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, false,
                        Optional.of(LocalDateTime.parse(VALID_ATTENDANCE_DATE + "T00:00:00")))));
        assertFalse(standardCommand.equals(
                new AddAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, true,
                        Optional.of(LocalDateTime.parse("2026-01-29T08:30:00")))));
    }

    @Test
    public void toStringMethod() {
        Index personIndex = Index.fromOneBased(1);
        Index appointmentIndex = Index.fromOneBased(2);
        AddAttdCommand addCommand = new AddAttdCommand(personIndex, appointmentIndex, true,
                Optional.of(LocalDateTime.parse("2026-01-29T08:30:00")));
        String expected = AddAttdCommand.class.getCanonicalName()
                + "{personIndex=" + personIndex + ", sessionIndex=" + appointmentIndex
                + ", hasAttended=true, recordedAt=" + VALID_ATTENDANCE_DATE_TIME + "}";
        assertEquals(expected, addCommand.toString());
    }
}
