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
    private Label paymentDueDateLabel;

    @FXML
    private FlowPane paymentHistoryFlowPane;

    @FXML
    private Label lastAttendanceLabel;

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
        parentNameLabel.setText(person.getParentName().map(parent -> parent.fullName).orElse("-"));
        parentPhoneLabel.setText(person.getParentPhone().map(phone -> phone.value).orElse("-"));
        parentEmailLabel.setText(person.getParentEmail().map(email -> email.value).orElse("-"));
        lessonStartLabel.setText(formatDateTime(person.getAppointmentStart().orElse(null)));
        paymentDueDateLabel.setText(formatDate(person.getBilling().getLastDueDate()));
        lastAttendanceLabel.setText(formatDateTime(person.getLastAttendance().orElse(null)));

        tagsFlowPane.getChildren().clear();
        subjectsFlowPane.getChildren().clear();
        paymentHistoryFlowPane.getChildren().clear();

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

        contentContainer.setManaged(true);
        contentContainer.setVisible(true);
        emptyStateLabel.setManaged(false);
        emptyStateLabel.setVisible(false);
    }

    private void showEmptyState() {
        tagsFlowPane.getChildren().clear();
        subjectsFlowPane.getChildren().clear();
        paymentHistoryFlowPane.getChildren().clear();
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
}
