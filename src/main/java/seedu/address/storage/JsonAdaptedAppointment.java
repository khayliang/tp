package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.session.Appointment;
import seedu.address.model.session.ScheduledSession;

/**
 * Jackson-friendly version of {@link Appointment}.
 */
class JsonAdaptedAppointment {

    private final List<JsonAdaptedScheduledSession> sessions;

    @JsonCreator
    public JsonAdaptedAppointment(@JsonProperty("sessions") List<JsonAdaptedScheduledSession> sessions) {
        this.sessions = sessions;
    }

    public JsonAdaptedAppointment(Appointment source) {
        this.sessions = source.getSessions().stream()
                .map(JsonAdaptedScheduledSession::new)
                .toList();
    }

    public Appointment toModelType() throws IllegalValueException {
        List<ScheduledSession> modelSessions = new ArrayList<>();
        if (sessions != null) {
            for (JsonAdaptedScheduledSession session : sessions) {
                modelSessions.add(session.toModelType());
            }
        }
        try {
            return new Appointment(modelSessions);
        } catch (IllegalArgumentException err) {
            throw new IllegalValueException(err.getMessage());
        }
    }
}
