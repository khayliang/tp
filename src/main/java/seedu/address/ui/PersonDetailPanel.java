package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.person.Person;

/**
 * Panel that displays detailed information for the selected person.
 */
public class PersonDetailPanel extends UiPart<Region> {

    private static final String FXML = "PersonDetailPanel.fxml";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d MMM uuuu, h:mma");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d MMM uuuu");

    @FXML
    private VBox contentContainer;

    @FXML
    private Label emptyStateLabel;

    @FXML
    private Label nameLabel;

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
    private Label lessonStartLabel;

    @FXML
    private Label paymentAmountLabel;

    @FXML
    private Label paymentDueDateLabel;

    @FXML
    private FlowPane paymentHistoryFlowPane;

    @FXML
    private FlowPane attendanceHistoryFlowPane;

    @FXML
    private FlowPane tagsFlowPane;

    @FXML
    private FlowPane subjectsFlowPane;

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
                .map(g -> g.getName().fullName).orElse("-"));
        parentPhoneLabel.setText(person.getGuardian()
                .flatMap(g -> g.getPhone()).map(p -> p.value).orElse("-"));
        parentEmailLabel.setText(person.getGuardian()
                .flatMap(g -> g.getEmail()).map(e -> e.value).orElse("-"));
        lessonStartLabel.setText(formatDateTime(person.getAppointmentStart().orElse(null)));
        paymentAmountLabel.setText(formatAmount(person.getBilling().getTuitionFee()));
        paymentDueDateLabel.setText(formatDate(person.getBilling().getCurrentDueDate()));

        tagsFlowPane.getChildren().clear();
        subjectsFlowPane.getChildren().clear();
        paymentHistoryFlowPane.getChildren().clear();
        attendanceHistoryFlowPane.getChildren().clear();

        if (person.getTags().isEmpty()) {
            Label noTagsLabel = new Label("-");
            noTagsLabel.getStyleClass().add("detail-field-value");
            tagsFlowPane.getChildren().add(noTagsLabel);
        } else {
            person.getTags().stream()
                    .sorted((left, right) -> left.tagName.compareTo(right.tagName))
                    .forEach(tag -> {
                        Label tagLabel = new Label(tag.tagName);
                        tagLabel.getStyleClass().add("detail-tag");
                        tagsFlowPane.getChildren().add(tagLabel);
                    });
        }

        if (person.getAcademics().getSubjects().isEmpty()) {
            Label noSubjectsLabel = new Label("-");
            noSubjectsLabel.getStyleClass().add("detail-field-value");
            subjectsFlowPane.getChildren().add(noSubjectsLabel);
        } else {
            person.getAcademics().getSubjects().stream()
                    .sorted(java.util.Comparator.comparing(seedu.address.model.academic.Subject::getName))
                    .forEach(subject -> {
                        Label subjectLabel = new Label(subject.toString());
                        subjectLabel.getStyleClass().add("detail-subject-tag");
                        subjectsFlowPane.getChildren().add(subjectLabel);
                    });
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

        if (person.getAttendance().isEmpty()) {
            Label noAttendanceLabel = new Label("No attendance history");
            noAttendanceLabel.getStyleClass().add("detail-field-value");
            attendanceHistoryFlowPane.getChildren().add(noAttendanceLabel);
        } else {
            java.util.List<Attendance> attendanceRecords = person.getAttendance().getRecords();
            for (int index = attendanceRecords.size() - 1; index >= 0; index--) {
                Label attendanceLabel = new Label(formatAttendance(attendanceRecords.get(index)));
                attendanceLabel.getStyleClass().add("detail-attendance-date");
                attendanceHistoryFlowPane.getChildren().add(attendanceLabel);
            }
        }

        contentContainer.setManaged(true);
        contentContainer.setVisible(true);
        emptyStateLabel.setManaged(false);
        emptyStateLabel.setVisible(false);
    }

    private void showEmptyState() {
        tagsFlowPane.getChildren().clear();
        subjectsFlowPane.getChildren().clear();
        paymentHistoryFlowPane.getChildren().clear();
        attendanceHistoryFlowPane.getChildren().clear();
        contentContainer.setManaged(false);
        contentContainer.setVisible(false);
        emptyStateLabel.setManaged(true);
        emptyStateLabel.setVisible(true);
    }

    private String formatDateTime(LocalDateTime value) {
        if (value == null) {
            return "-";
        }
        return value.format(DATE_TIME_FORMATTER);
    }

    private String formatDate(LocalDate value) {
        if (value == null) {
            return "-";
        }
        return value.format(DATE_FORMATTER);
    }

    private String formatAmount(double amount) {
        return String.format("$%.2f", amount);
    }

    private String formatAttendance(Attendance attendance) {
        String status = attendance.hasAttended() ? "Present" : "Absent";
        return status + ": " + formatDate(attendance.getRecordedDate());
    }
}
