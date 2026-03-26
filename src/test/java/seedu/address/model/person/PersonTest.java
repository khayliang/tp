package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_JC;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.billing.Billing;
import seedu.address.model.billing.PaymentHistory;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void constructor_directCall_fieldsInitializedCorrectly() {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friend"));

        Person person = new Person(
                ALICE.getName(),
                ALICE.getPhone(),
                ALICE.getEmail(),
                ALICE.getAddress(),
                tags
        );

        // basic fields
        assertEquals(ALICE.getName(), person.getName());
        assertEquals(ALICE.getPhone(), person.getPhone());
        assertEquals(ALICE.getEmail(), person.getEmail());
        assertEquals(ALICE.getAddress(), person.getAddress());

        // tags copied
        assertEquals(tags, person.getTags());

        // academics default
        assertTrue(person.getAcademics().getSubjects().isEmpty());
        assertTrue(person.getAcademics().getNotes().isEmpty());

        // optionals default
        assertTrue(person.getGuardian().isEmpty());
        assertTrue(person.getAppointmentStart().isEmpty());
        assertTrue(person.getAttendance().isEmpty());
        assertTrue(person.getLastAttendance().isEmpty());
    }

    @Test
    public void constructor_nullFields_throwsNullPointerException() {
        Set<Tag> tags = new HashSet<>();

        assertThrows(NullPointerException.class, () -> {
            new Person(
                    null,
                    ALICE.getPhone(),
                    ALICE.getEmail(),
                    ALICE.getAddress(),
                    tags);
        });

        assertThrows(NullPointerException.class, () -> {
            new Person(
                    ALICE.getName(),
                    null,
                    ALICE.getEmail(),
                    ALICE.getAddress(),
                    tags);
        });

        assertThrows(NullPointerException.class, () -> {
            new Person(
                    ALICE.getName(),
                    ALICE.getPhone(),
                    null,
                    ALICE.getAddress(),
                    tags);
        });

        assertThrows(NullPointerException.class, () -> {
            new Person(
                    ALICE.getName(),
                    ALICE.getPhone(),
                    ALICE.getEmail(),
                    null,
                    tags);
        });

        assertThrows(NullPointerException.class, () -> {
            new Person(
                    ALICE.getName(),
                    ALICE.getPhone(),
                    ALICE.getEmail(),
                    ALICE.getAddress(),
                    null);
        });
    }

    @Test
    public void constructor_tagsDefensiveCopy() {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friend"));

        Person person = new Person(
                ALICE.getName(),
                ALICE.getPhone(),
                ALICE.getEmail(),
                ALICE.getAddress(),
                tags
        );

        tags.clear();

        assertFalse(person.getTags().isEmpty());
    }

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().withTags("friend").build();
        assertThrows(UnsupportedOperationException.class, () -> {
            person.getTags().stream().findAny().ifPresent(person.getTags()::remove);
        });
    }

    @Test
    public void getAppointmentStarts_modifySet_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().withAppointmentStart("2026-01-13T08:00:00").build();
        assertThrows(UnsupportedOperationException.class, () -> person.getAppointmentStarts()
                .add(LocalDateTime.parse("2026-01-14T08:00:00")));
    }

    @Test
    public void getAppointmentStart_multipleAppointments_returnsEarliest() {
        Person person = new PersonBuilder(ALICE)
                .withAppointmentStart("2026-01-15T08:00:00", "2026-01-13T08:00:00")
                .build();
        assertEquals(LocalDateTime.parse("2026-01-13T08:00:00"), person.getAppointmentStart().orElseThrow());
    }

    @Test
    public void isSamePerson() {
        assertTrue(ALICE.isSamePerson(ALICE));
        assertFalse(ALICE.isSamePerson(null));

        Person editedAlice = new PersonBuilder(ALICE)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB)
                .withTags(VALID_TAG_JC)
                .build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        Person editedBob = new PersonBuilder(BOB)
                .withName(VALID_NAME_BOB.toLowerCase())
                .build();
        assertFalse(BOB.isSamePerson(editedBob));

        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        assertTrue(ALICE.equals(ALICE));
        assertFalse(ALICE.equals(null));
        assertFalse(ALICE.equals(5));
        assertFalse(ALICE.equals(BOB));

        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_JC).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new PersonBuilder(ALICE).withAppointmentStart("2026-01-13T08:00:00").build();
        assertFalse(ALICE.equals(editedAlice));

        LocalDate differentPaymentDate = LocalDate.parse("2026-01-13");
        PaymentHistory differentPaymentHistory = new PaymentHistory(differentPaymentDate);
        Billing updatedBilling = new Billing(
                ALICE.getBilling().getRecurrence(),
                differentPaymentDate,
                ALICE.getBilling().getTuitionFee(),
                differentPaymentHistory);
        editedAlice = new PersonBuilder(ALICE).withBilling(updatedBilling).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new PersonBuilder(ALICE).addAttendance("2026-01-29T08:00:00").build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName()
                + "{name=" + ALICE.getName()
                + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail()
                + ", address=" + ALICE.getAddress()
                + ", tags=" + ALICE.getTags()
                + ", academics=" + ALICE.getAcademics()
                + ", guardian=" + ALICE.getGuardian().orElse(null)
                + ", appointmentStart=" + ALICE.getAppointmentStart()
                + ", appointmentStarts=" + ALICE.getAppointmentStarts()
                + ", billing=" + ALICE.getBilling()
                + ", attendance=" + ALICE.getAttendance()
                + "}";

        assertEquals(expected, ALICE.toString());
    }
}
