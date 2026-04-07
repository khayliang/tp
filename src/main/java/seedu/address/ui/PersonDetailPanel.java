package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.academic.Subject;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.person.Person;
import seedu.address.model.session.ScheduledSession;
import seedu.address.model.tag.Tag;

/**
 * Panel that displays detailed information for the selected person.
 */
public class PersonDetailPanel extends UiPart<Region> {

    private static final String FXML = "PersonDetailPanel.fxml";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d MMM uuuu, h:mma");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d MMM uuuu");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mma");
    private static final int MAX_VISIBLE_ATTENDANCE_RECORDS = 3;
    private static final String NOT_SET_TEXT = "Not set";

    @FXML
    private VBox contentContainer;

    @FXML
    private Label emptyStateLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label nextSessionSummaryLabel;

    @FXML
    private Label attendanceSummaryLabel;

    @FXML
    private Label paymentStatusSummaryLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label parentNameLabel;

    @FXML
    private Label parentPhoneLabel;

    @FXML
    private Label parentEmailLabel;

    @FXML
    private VBox appointmentListContainer;

    @FXML
    private Label paymentAmountLabel;

    @FXML
    private Label paymentDueDateLabel;

    @FXML
    private FlowPane paymentHistoryFlowPane;

    @FXML
    private FlowPane tagsFlowPane;

    @FXML
    private FlowPane subjectsFlowPane;

    @FXML
    private Label academicsNotesLabel;

    /**
     * Creates a {@code PersonDetailPanel}.
     */
    public PersonDetailPanel() {
        super(FXML);
        showEmptyState();
    }

    /**
     * Updates this panel to display the details of {@code person}, or clears the panel when null.
     */
    public void displayPerson(Person person) {
        if (person == null) {
            showEmptyState();
            return;
        }

        requireNonNull(person);

        nameLabel.setText(person.getName().fullName);
        phoneLabel.setText(person.getPhone().value);
        emailLabel.setText(person.getEmail().value);
        addressLabel.setText(person.getAddress().value);
        parentNameLabel.setText(person.getGuardian()
            .map(g -> g.getName().fullName).orElse(NOT_SET_TEXT));
        parentPhoneLabel.setText(person.getGuardian()
            .flatMap(g -> g.getPhone()).map(p -> p.value).orElse(NOT_SET_TEXT));
        parentEmailLabel.setText(person.getGuardian()
            .flatMap(g -> g.getEmail()).map(e -> e.value).orElse(NOT_SET_TEXT));

        nextSessionSummaryLabel.setText(formatNextSessionSummary(person));
        attendanceSummaryLabel.setText(formatOverallAttendanceSummary(person));
        paymentStatusSummaryLabel.setText(formatPaymentStatusSummary(person.getBilling().getCurrentDueDate()));

        paymentAmountLabel.setText(formatAmount(person.getBilling().getTuitionFee()));
        paymentDueDateLabel.setText(formatDate(person.getBilling().getCurrentDueDate()));

        appointmentListContainer.getChildren().clear();
        tagsFlowPane.getChildren().clear();
        subjectsFlowPane.getChildren().clear();
        paymentHistoryFlowPane.getChildren().clear();

        if (person.getTags().isEmpty()) {
            Label noTagsLabel = new Label("No tags");
            noTagsLabel.getStyleClass().add("detail-field-value");
            tagsFlowPane.getChildren().add(noTagsLabel);
        } else {
            List<Tag> sortedTags = person.getSortedTags();

            for (int i = 0; i < sortedTags.size(); i++) {
                Tag tag = sortedTags.get(i);

                Label tagLabel = new Label((i + 1) + ". " + tag.tagName);
                tagLabel.getStyleClass().add("detail-tag");
                tagsFlowPane.getChildren().add(tagLabel);
            }
        }

        // Academics
        if (person.getAcademics().getSubjects().isEmpty()) {
            Label noSubjectsLabel = new Label("No subjects");
            noSubjectsLabel.getStyleClass().add("detail-field-value");
            subjectsFlowPane.getChildren().add(noSubjectsLabel);
        } else {
            List<Subject> sortedSubjects = person.getAcademics().getSortedSubjects();

            for (int i = 0; i < sortedSubjects.size(); i++) {
                Subject subject = sortedSubjects.get(i);

                Label subjectLabel = new Label((i + 1) + ". " + subject.toString());
                subjectLabel.getStyleClass().add("detail-subject-tag");
                subjectsFlowPane.getChildren().add(subjectLabel);
            }
        }

        String description = person.getAcademics().getDescription().orElse("");
        academicsNotesLabel.setText(description.isBlank() ? "No description" : description);

        List<ScheduledSession> sortedSessions = person.getAppointment().getSessions().stream()
                .sorted(Comparator.comparing(ScheduledSession::getNext,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();

        if (sortedSessions.isEmpty()) {
            Label noAppointmentsLabel = new Label("No appointments");
            noAppointmentsLabel.getStyleClass().add("detail-field-value");
            appointmentListContainer.getChildren().add(noAppointmentsLabel);
        } else {
            for (int index = 0; index < sortedSessions.size(); index++) {
                appointmentListContainer.getChildren().add(
                        createAppointmentSection(index + 1, sortedSessions.get(index)));
            }
        }

        // Display payment history
        if (person.getPaymentHistory().getPaidDates().isEmpty()) {
            Label noPaymentsLabel = new Label("No payment history");
            noPaymentsLabel.getStyleClass().add("detail-field-value");
            paymentHistoryFlowPane.getChildren().add(noPaymentsLabel);
        } else {
            person.getPaymentHistory().getPaidDates().stream()
                    .sorted(java.util.Comparator.reverseOrder()) // Most recent first
                    .forEach(date -> {
                        Label paymentLabel = new Label(formatDate(date));
                        paymentLabel.getStyleClass().add("detail-payment-date");
                        paymentHistoryFlowPane.getChildren().add(paymentLabel);
                    });
        }

        contentContainer.setManaged(true);
        contentContainer.setVisible(true);
        emptyStateLabel.setManaged(false);
        emptyStateLabel.setVisible(false);
    }

    private void showEmptyState() {
        appointmentListContainer.getChildren().clear();
        tagsFlowPane.getChildren().clear();
        subjectsFlowPane.getChildren().clear();
        paymentHistoryFlowPane.getChildren().clear();
        nextSessionSummaryLabel.setText("No upcoming sessions");
        attendanceSummaryLabel.setText("No attendance records");
        paymentStatusSummaryLabel.setText("No due date");
        contentContainer.setManaged(false);
        contentContainer.setVisible(false);
        emptyStateLabel.setManaged(true);
        emptyStateLabel.setVisible(true);
    }

    private String formatDateTime(LocalDateTime value) {
        if (value == null) {
            return NOT_SET_TEXT;
        }
        return value.format(DATE_TIME_FORMATTER);
    }

    private String formatDate(LocalDate value) {
        if (value == null) {
            return NOT_SET_TEXT;
        }
        return value.format(DATE_FORMATTER);
    }

    private String formatAmount(double amount) {
        return String.format("$%.2f", amount);
    }

    private String formatAppointmentTitle(int appointmentIndex, ScheduledSession session) {
        return appointmentIndex + ". " + session.getDescription();
    }

    private String formatAppointmentMeta(ScheduledSession session) {
        return "Next: " + formatDateTime(session.getNext());
    }

    private String formatAppointmentRecurrence(ScheduledSession session) {
        return "Recurs: " + formatRecurrenceSchedule(session);
    }

    private String formatAttendanceDate(LocalDateTime recordedAt) {
        return recordedAt.toLocalTime().equals(LocalTime.MIDNIGHT)
                ? formatDate(recordedAt.toLocalDate())
                : formatDateTime(recordedAt);
    }

    private String formatCompactAttendance(Attendance attendance) {
        String statusPrefix = attendance.hasAttended() ? "P" : "A";
        return statusPrefix + " " + formatAttendanceDate(attendance.getRecordedAt());
    }

    private String formatAttendanceSummary(List<Attendance> sortedAttendanceRecords) {
        long presentCount = sortedAttendanceRecords.stream().filter(Attendance::hasAttended).count();
        long absentCount = sortedAttendanceRecords.size() - presentCount;
        int presentPercentage = (int) Math.round((presentCount * 100.0) / sortedAttendanceRecords.size());
        String latestAttendance = formatAttendanceDate(sortedAttendanceRecords.get(0).getRecordedAt());
        return presentCount + " present, " + absentCount + " absent (" + presentPercentage + "%)"
                + " | Latest: " + latestAttendance;
    }

    private String formatNextSessionSummary(Person person) {
        return person.getAppointment().getSessions().stream()
                .map(ScheduledSession::getNext)
                .filter(next -> next != null)
                .min(Comparator.naturalOrder())
                .map(this::formatDateTime)
                .orElse("No upcoming sessions");
    }

    private String formatOverallAttendanceSummary(Person person) {
        List<Attendance> allAttendance = person.getAppointment().getSessions().stream()
                .flatMap(session -> session.getAttendanceHistory().getRecords().stream())
                .toList();

        if (allAttendance.isEmpty()) {
            return "No attendance records";
        }

        long presentCount = allAttendance.stream().filter(Attendance::hasAttended).count();
        int presentPercentage = (int) Math.round((presentCount * 100.0) / allAttendance.size());
        return presentCount + " / " + allAttendance.size() + " present (" + presentPercentage + "%)";
    }

    private String formatPaymentStatusSummary(LocalDate dueDate) {
        if (dueDate == null) {
            return "No due date";
        }

        LocalDate today = LocalDate.now();
        if (dueDate.isEqual(today)) {
            return "Due today";
        }

        long dayDifference = Math.abs(ChronoUnit.DAYS.between(today, dueDate));
        String dayLabel = dayDifference == 1 ? "day" : "days";

        if (dueDate.isBefore(today)) {
            return "Overdue by " + dayDifference + " " + dayLabel;
        }

        return "Due in " + dayDifference + " " + dayLabel;
    }

    private VBox createAppointmentSection(int appointmentIndex, ScheduledSession session) {
        VBox appointmentSection = new VBox(6);
        appointmentSection.getStyleClass().add("detail-appointment-card");

        Label appointmentLabel = new Label(formatAppointmentTitle(appointmentIndex, session));
        appointmentLabel.getStyleClass().add("detail-appointment-title");
        appointmentLabel.setWrapText(true);

        Label scheduleLabel = new Label(formatAppointmentMeta(session));
        scheduleLabel.getStyleClass().add("detail-appointment-meta");
        scheduleLabel.setWrapText(true);

        Label recurrenceLabel = new Label(formatAppointmentRecurrence(session));
        recurrenceLabel.getStyleClass().add("detail-appointment-meta-secondary");
        recurrenceLabel.setWrapText(true);

        FlowPane attendancePane = new FlowPane();
        attendancePane.setHgap(6);
        attendancePane.setVgap(6);
        attendancePane.setPrefWrapLength(260);

        if (session.getAttendanceHistory().isEmpty()) {
            Label attendanceSummary = new Label("0 present, 0 absent");
            attendanceSummary.getStyleClass().add("detail-attendance-summary");

            Label noAttendanceLabel = new Label("No attendance history");
            noAttendanceLabel.getStyleClass().add("detail-field-value");
            attendancePane.getChildren().add(noAttendanceLabel);

            appointmentSection.getChildren().addAll(
                    appointmentLabel,
                    scheduleLabel,
                    recurrenceLabel,
                    attendanceSummary,
                    attendancePane);
        } else {
            List<Attendance> attendanceRecords = session.getAttendanceHistory().getRecords().stream()
                    .sorted(Comparator.comparing(Attendance::getRecordedAt).reversed())
                    .toList();

            Label attendanceSummary = new Label(formatAttendanceSummary(attendanceRecords));
            attendanceSummary.getStyleClass().add("detail-attendance-summary");

            int visibleRecords = Math.min(MAX_VISIBLE_ATTENDANCE_RECORDS, attendanceRecords.size());
            for (int index = 0; index < visibleRecords; index++) {
                Attendance attendance = attendanceRecords.get(index);
                Label attendanceLabel = new Label(formatCompactAttendance(attendance));
                attendanceLabel.getStyleClass().add("detail-attendance-date");
                attendanceLabel.getStyleClass().add(
                        attendance.hasAttended() ? "detail-attendance-present" : "detail-attendance-absent");
                attendancePane.getChildren().add(attendanceLabel);
            }

            int hiddenRecordCount = attendanceRecords.size() - visibleRecords;
            if (hiddenRecordCount > 0) {
                Label hiddenRecordsLabel = new Label("+" + hiddenRecordCount + " more");
                hiddenRecordsLabel.getStyleClass().add("detail-attendance-more");
                attendancePane.getChildren().add(hiddenRecordsLabel);
            }

            appointmentSection.getChildren().addAll(
                    appointmentLabel,
                    scheduleLabel,
                    recurrenceLabel,
                    attendanceSummary,
                    attendancePane);
        }

        return appointmentSection;
    }

    private String formatRecurrenceSchedule(ScheduledSession session) {
        LocalDateTime start = session.getStart();
        String timeText = start.toLocalTime().format(TIME_FORMATTER);
        return switch (session.getRecurrence()) {
        case NONE -> "One-time session";
        case WEEKLY -> "Every " + formatDayOfWeek(start.getDayOfWeek()) + " at " + timeText;
        case BIWEEKLY -> "Every 2 weeks on " + formatDayOfWeek(start.getDayOfWeek()) + " at " + timeText;
        case MONTHLY -> "Every month on day " + start.getDayOfMonth() + " at " + timeText;
        };
    }

    private String formatDayOfWeek(DayOfWeek dayOfWeek) {
        String lower = dayOfWeek.name().toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
