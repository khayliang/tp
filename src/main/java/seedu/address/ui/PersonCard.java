package seedu.address.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.academic.Subject;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.person.Person;
import seedu.address.model.session.ScheduledSession;
import seedu.address.model.tag.Tag;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final int MAX_VISIBLE_TAGS = 1;
    private static final int MAX_VISIBLE_SUBJECTS = 2;
    private static final DateTimeFormatter NEXT_SESSION_FORMATTER = DateTimeFormatter.ofPattern("d MMM, h:mma");

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label paymentStatusLabel;
    @FXML
    private FlowPane summaryChips;
    @FXML
    private Label nextSessionLabel;
    @FXML
    private Label attendanceLabel;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ".");
        name.setText(person.getName().fullName);

        setPaymentStatus(person.getBilling().getCurrentDueDate());
        populateSummaryChips(person);
        nextSessionLabel.setText(formatNextSessionSummary(person));
        attendanceLabel.setText(formatOverallAttendanceSummary(person));
    }

    private void setPaymentStatus(LocalDate dueDate) {
        paymentStatusLabel.setText(formatPaymentStatus(dueDate));

        paymentStatusLabel.getStyleClass().removeAll(
                "person-payment-neutral",
                "person-payment-upcoming",
                "person-payment-ok",
                "person-payment-overdue");
        paymentStatusLabel.getStyleClass().add(resolvePaymentStatusStyleClass(dueDate));
    }

    private String resolvePaymentStatusStyleClass(LocalDate dueDate) {
        if (dueDate == null) {
            return "person-payment-neutral";
        }

        LocalDate today = LocalDate.now();
        if (dueDate.isBefore(today)) {
            return "person-payment-overdue";
        }

        long dayDifference = ChronoUnit.DAYS.between(today, dueDate);
        if (dayDifference <= 3) {
            return "person-payment-upcoming";
        }

        return "person-payment-ok";
    }

    private String formatPaymentStatus(LocalDate dueDate) {
        if (dueDate == null) {
            return "No due date";
        }

        LocalDate today = LocalDate.now();
        if (dueDate.isEqual(today)) {
            return "Due today";
        }

        long dayDifference = Math.abs(ChronoUnit.DAYS.between(today, dueDate));
        if (dueDate.isBefore(today)) {
            return "Overdue " + dayDifference + "d";
        }

        return "Due in " + dayDifference + "d";
    }

    private void populateSummaryChips(Person person) {
        summaryChips.getChildren().clear();

        List<Tag> sortedTags = person.getSortedTags();
        int visibleTagCount = Math.min(MAX_VISIBLE_TAGS, sortedTags.size());
        for (int i = 0; i < visibleTagCount; i++) {
            addChip(sortedTags.get(i).tagName, "person-tag-chip");
        }

        int hiddenTagCount = sortedTags.size() - visibleTagCount;
        if (hiddenTagCount > 0) {
            addOverflowChip(hiddenTagCount, "tag");
        }

        List<Subject> sortedSubjects = person.getAcademics().getSortedSubjects();
        int visibleSubjectCount = Math.min(MAX_VISIBLE_SUBJECTS, sortedSubjects.size());
        for (int i = 0; i < visibleSubjectCount; i++) {
            addChip(sortedSubjects.get(i).toString(), "person-subject-chip");
        }

        int hiddenSubjectCount = sortedSubjects.size() - visibleSubjectCount;
        if (hiddenSubjectCount > 0) {
            addOverflowChip(hiddenSubjectCount, "subject");
        }

        if (summaryChips.getChildren().isEmpty()) {
            addChip("No tags or subjects", "person-chip-overflow");
        }
    }

    private void addChip(String text, String styleClass) {
        Label chip = new Label(text);
        chip.getStyleClass().add("person-chip");
        chip.getStyleClass().add(styleClass);
        summaryChips.getChildren().add(chip);
    }

    private void addOverflowChip(int hiddenCount, String noun) {
        String pluralSuffix = hiddenCount == 1 ? "" : "s";
        addChip("+" + hiddenCount + " " + noun + pluralSuffix, "person-chip-overflow");
    }

    private String formatNextSessionSummary(Person person) {
        return person.getNextAppointment()
                .map(this::formatNextSessionSummary)
                .orElse("Next: No upcoming sessions");
    }

    private String formatNextSessionSummary(ScheduledSession session) {
        return "Next: " + session.getDescription() + " (" + session.getNext().format(NEXT_SESSION_FORMATTER) + ")";
    }

    private String formatOverallAttendanceSummary(Person person) {
        List<Attendance> allAttendance = person.getAppointment().getSessions().stream()
                .flatMap(session -> session.getAttendanceHistory().getRecords().stream())
                .toList();

        if (allAttendance.isEmpty()) {
            return "Attendance: No records";
        }

        long presentCount = allAttendance.stream().filter(Attendance::hasAttended).count();
        int presentPercentage = (int) Math.round((presentCount * 100.0) / allAttendance.size());
        return "Attendance: " + presentCount + "/" + allAttendance.size() + " present (" + presentPercentage + "%)";
    }
}
