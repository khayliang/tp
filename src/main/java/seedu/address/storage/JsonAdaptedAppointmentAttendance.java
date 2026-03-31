package seedu.address.storage;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.attendance.Attendance;

/**
 * Jackson-friendly version of appointment {@link Attendance}.
 */
class JsonAdaptedAppointmentAttendance {

    private static final String ATTENDANCE_DATE_MESSAGE_CONSTRAINTS =
            "Appointment attendance date must be in ISO 8601 local date format, e.g. 2026-01-29";

    private final boolean hasAttended;
    private final String recordedDate;

    @JsonCreator
    public JsonAdaptedAppointmentAttendance(@JsonProperty("hasAttended") boolean hasAttended,
                                            @JsonProperty("recordedDate") String recordedDate) {
        this.hasAttended = hasAttended;
        this.recordedDate = recordedDate;
    }

    public JsonAdaptedAppointmentAttendance(Attendance source) {
        this.hasAttended = source.hasAttended();
        this.recordedDate = source.getRecordedDate().toString();
    }

    public Attendance toModelType() throws IllegalValueException {
        if (recordedDate == null) {
            throw new IllegalValueException(String.format(
                    JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT, "appointment attendance date"));
        }

        try {
            return new Attendance(hasAttended, LocalDate.parse(recordedDate));
        } catch (DateTimeParseException e) {
            throw new IllegalValueException(ATTENDANCE_DATE_MESSAGE_CONSTRAINTS);
        }
    }
}
