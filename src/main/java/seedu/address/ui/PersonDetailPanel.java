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
    private Label lessonStartLabel;

    @FXML
    private Label paymentDateLabel;

    @FXML
    private FlowPane tagsFlowPane;

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
        lessonStartLabel.setText(formatDateTime(person.getAppointmentStart().orElse(null)));
        paymentDateLabel.setText(formatDate(person.getPaymentDate().orElse(null)));

        tagsFlowPane.getChildren().clear();
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

        contentContainer.setManaged(true);
        contentContainer.setVisible(true);
        emptyStateLabel.setManaged(false);
        emptyStateLabel.setVisible(false);
    }

    private void showEmptyState() {
        tagsFlowPane.getChildren().clear();
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
