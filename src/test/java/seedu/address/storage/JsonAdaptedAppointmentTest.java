package seedu.address.storage;

import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.session.Appointment;

public class JsonAdaptedAppointmentTest {

    @Test
    public void toModelType_duplicateSessionDescriptions_throwsIllegalValueException() {
        JsonAdaptedScheduledSession firstSession = new JsonAdaptedScheduledSession(
                "2026-01-01T10:00:00", null, "NONE", "Algebra", null);
        JsonAdaptedScheduledSession secondSession = new JsonAdaptedScheduledSession(
                "2026-01-08T10:00:00", null, "NONE", " Algebra ", null);
        JsonAdaptedAppointment appointment = new JsonAdaptedAppointment(List.of(firstSession, secondSession));

        assertThrows(IllegalValueException.class, Appointment.MESSAGE_DUPLICATE_SESSION_DESCRIPTION,
                appointment::toModelType);
    }
}
