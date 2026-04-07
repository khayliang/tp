package seedu.address.model.session;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.AppClock;
import seedu.address.model.attendance.AttendanceHistory;
import seedu.address.model.recurrence.Recurrence;

/**
 * Immutable appointment container for a person's scheduled sessions.
 */
public final class Appointment {

    public static final String MESSAGE_DUPLICATE_SESSION_DESCRIPTION =
            "Appointment session descriptions must be unique within the same student.";

    private final List<ScheduledSession> sessions;

    /**
     * Creates an empty appointment container.
     */
    public Appointment() {
        this.sessions = List.of();
    }

    /**
     * Creates an {@code Appointment} from the provided sessions.
     */
    public Appointment(List<ScheduledSession> sessions) {
        requireAllNonNull(sessions);
        List<ScheduledSession> copiedSessions = new ArrayList<>(sessions);
        validateUniqueSessionDescriptions(copiedSessions);
        copiedSessions.sort(Comparator.comparing(ScheduledSession::getNext)
                .thenComparing(ScheduledSession::getStart)
                .thenComparing(ScheduledSession::getDescription));
        this.sessions = List.copyOf(copiedSessions);
    }

    /**
     * Backward-compatible constructor for a single session.
     */
    public Appointment(Recurrence recurrence, LocalDateTime start, LocalDateTime next,
                       AttendanceHistory attendanceHistory, String description) {
        this(List.of(new ScheduledSession(recurrence, start, next, attendanceHistory, description)));
    }

    /**
     * Creates an {@code Appointment} from an ISO-8601 date-time string.
     */
    public static Appointment of(String startDateTime, String description, Recurrence recurrence) {
        LocalDateTime start = LocalDateTime.parse(startDateTime);
        return new Appointment(recurrence, start, start, AttendanceHistory.EMPTY, description);
    }

    public static Appointment defaultAppointment() {
        return new Appointment();
    }

    public List<ScheduledSession> getSessions() {
        return List.copyOf(new ArrayList<>(sessions));
    }

    /**
     * Returns true if a session with the given description already exists.
     */
    public boolean hasSessionWithDescription(String description) {
        String normalizedDescription = normalizeDescription(description);
        return sessions.stream()
                .map(ScheduledSession::getDescription)
                .anyMatch(normalizedDescription::equals);
    }

    /**
     * Returns true if another session, excluding the given index, has the given description.
     */
    public boolean hasSessionWithDescriptionExcludingIndex(String description, int excludedIndex) {
        if (excludedIndex < 0 || excludedIndex >= sessions.size()) {
            throw new IndexOutOfBoundsException("Session index out of range: " + excludedIndex);
        }

        String normalizedDescription = normalizeDescription(description);
        for (int i = 0; i < sessions.size(); i++) {
            if (i == excludedIndex) {
                continue;
            }
            if (sessions.get(i).getDescription().equals(normalizedDescription)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a copy with session appended.
     */
    public Appointment addSession(ScheduledSession session) {
        requireAllNonNull(session);
        List<ScheduledSession> updatedSessions = new ArrayList<>(sessions);
        updatedSessions.add(session);
        return new Appointment(updatedSessions);
    }

    /**
     * Returns a copy with session at {@code index} removed.
     */
    public Appointment removeSession(int index) {
        if (index < 0 || index >= sessions.size()) {
            throw new IndexOutOfBoundsException("Session index out of range: " + index);
        }
        List<ScheduledSession> updatedSessions = new ArrayList<>(sessions);
        updatedSessions.remove(index);
        return new Appointment(updatedSessions);
    }

    /**
     * Returns a copy with the session at {@code index} replaced by {@code updatedSession}.
     */
    public Appointment withSessionAt(int index, ScheduledSession updatedSession) {
        requireAllNonNull(updatedSession);
        if (index < 0 || index >= sessions.size()) {
            throw new IndexOutOfBoundsException("Session index out of range: " + index);
        }
        List<ScheduledSession> updatedSessions = new ArrayList<>(sessions);
        updatedSessions.set(index, updatedSession);
        return new Appointment(updatedSessions);
    }

    /**
     * Returns a copy with all sessions at the given 1-based indices removed.
     */
    public Appointment removeSessions(List<Index> sessionIndices) {
        requireAllNonNull(sessionIndices);
        List<Integer> uniqueZeroBased = sessionIndices.stream()
                .map(Index::getZeroBased)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toList();
        List<ScheduledSession> updatedSessions = new ArrayList<>(sessions);
        for (int zeroBased : uniqueZeroBased) {
            if (zeroBased < 0 || zeroBased >= updatedSessions.size()) {
                throw new IndexOutOfBoundsException("Session index out of range: " + (zeroBased + 1));
            }
            updatedSessions.remove(zeroBased);
        }
        return new Appointment(updatedSessions);
    }

    /**
     * Returns the next upcoming session for this person.
     * If all sessions are in the past, returns the most recent past session.
     */
    public Optional<ScheduledSession> getNextSession() {
        if (sessions.isEmpty()) {
            return Optional.empty();
        }

        LocalDateTime now = AppClock.now();
        return sessions.stream()
                .filter(session -> !session.getNext().isBefore(now))
                .findFirst()
                .or(() -> Optional.of(sessions.get(sessions.size() - 1)));
    }

    private String normalizeDescription(String description) {
        requireAllNonNull(description);
        return description.trim();
    }

    private void validateUniqueSessionDescriptions(List<ScheduledSession> sessions) {
        Set<String> seenDescriptions = new HashSet<>();
        for (ScheduledSession session : sessions) {
            if (!seenDescriptions.add(normalizeDescription(session.getDescription()))) {
                throw new IllegalArgumentException(MESSAGE_DUPLICATE_SESSION_DESCRIPTION);
            }
        }
    }

    private ScheduledSession getSingleSession() {
        if (sessions.size() != 1) {
            throw new IllegalStateException("Single-session accessor used on appointment container");
        }
        return sessions.get(0);
    }

    /**
     * Backward-compatible single-session accessor.
     */
    public Recurrence getRecurrence() {
        return getSingleSession().getRecurrence();
    }

    /**
     * Backward-compatible single-session accessor.
     */
    public LocalDateTime getStart() {
        return getSingleSession().getStart();
    }

    /**
     * Backward-compatible single-session accessor.
     */
    public LocalDateTime getNext() {
        return getSingleSession().getNext();
    }

    /**
     * Backward-compatible single-session accessor.
     */
    public AttendanceHistory getAttendance() {
        return getSingleSession().getAttendanceHistory();
    }

    /**
     * Backward-compatible single-session accessor.
     */
    public String getDescription() {
        return getSingleSession().getDescription();
    }

    /**
     * Backward-compatible single-session mutation.
     */
    public Appointment withAttendance(AttendanceHistory updatedAttendance) {
        ScheduledSession current = getSingleSession();
        return new Appointment(List.of(current.withAttendance(updatedAttendance)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Appointment)) {
            return false;
        }

        Appointment otherAppointment = (Appointment) other;
        return sessions.equals(otherAppointment.sessions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessions);
    }

    @Override
    public String toString() {
        return "Appointment{"
                + "sessions=" + sessions
                + "}";
    }
}
