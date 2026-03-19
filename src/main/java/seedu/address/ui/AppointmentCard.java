package seedu.address.ui;

import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * An UI component that displays appointment-centric information for a {@code Person}.
 */
public class AppointmentCard extends UiPart<Region> {

    private static final String FXML = "AppointmentListCard.fxml";

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label appointmentStart;
    @FXML
    private Label studentName;
    @FXML
    private Label parentName;
    @FXML
    private Label phone;
    @FXML
    private Label email;

    /**
     * Creates a {@code AppointmentCard} with the given {@code Person} and index to display.
     */
    public AppointmentCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;

        id.setText(displayedIndex + ". ");
        String appointmentStartValue = person.getAppointmentStart()
                .map(value -> value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .orElse("-");
        appointmentStart.setText("Start: " + appointmentStartValue);
        studentName.setText("Student: " + person.getName().fullName);
        phone.setText("Phone: " + person.getPhone().value);
        email.setText("Email: " + person.getEmail().value);
        person.getParentName().ifPresentOrElse(
                value -> parentName.setText("Parent: " + value.fullName), () -> {
                    parentName.setVisible(false);
                    parentName.setManaged(false);
                });
    }
}
