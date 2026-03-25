package seedu.address.model.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.academic.Academics;
import seedu.address.model.academic.Level;
import seedu.address.model.academic.Subject;
import seedu.address.model.billing.Billing;
import seedu.address.model.billing.PaymentHistory;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.person.Phone;
import seedu.address.model.recurrence.Recurrence;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new PersonBuilder(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                    new Address("Blk 30 Geylang Street 29, #06-40"), getTagSet("Primary"))
                    .withAcademics(new Academics(getSubjectSet(
                            new Subject("Mathematics", Level.BASIC),
                            new Subject("English", Level.BASIC))))
                    .withParentName(new Name("Janet Yeoh"))
                    .withParentPhone(new Phone("98765432"))
                    .withParentEmail(new Email("janet@example.com"))
                    .withAppointmentStart(LocalDateTime.of(2026, 3, 21, 15, 30))
                    .withBilling(new Billing(Recurrence.MONTHLY, (LocalDate.of(2026, 3, 15)), 25,
                                new PaymentHistory(LocalDate.of(2026, 3, 15))))
                    .addAttendance(LocalDateTime.of(2026, 3, 18, 15, 30))
                    .build(),
            new PersonBuilder(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                    new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                    getTagSet("Secondary"))
                    .withAcademics(new Academics(getSubjectSet(
                            new Subject("Physics", Level.STRONG),
                            new Subject("Chemistry", Level.BASIC))))
                    .withParentName(new Name("Ming Yu"))
                    .withParentPhone(new Phone("97654321"))
                    .withParentEmail(new Email("ming@example.com"))
                    .withAppointmentStart(LocalDateTime.of(2026, 3, 22, 16, 0))
                    .withBilling(new Billing(Recurrence.MONTHLY, (LocalDate.of(2026, 3, 10)), 25,
                                new PaymentHistory(LocalDate.of(2026, 3, 10))))
                    .addAttendance(LocalDateTime.of(2026, 3, 17, 16, 0))
                    .build(),
            new PersonBuilder(new Name("Charlotte Oliveiro"), new Phone("93210283"),
                    new Email("charlotte@example.com"), new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                    getTagSet("Secondary"))
                    .withAcademics(new Academics(getSubjectSet(
                            new Subject("English", Level.STRONG),
                            new Subject("Literature", Level.BASIC))))
                    .withParentName(new Name("Patricia Oliveiro"))
                    .withParentPhone(new Phone("96543210"))
                    .withParentEmail(new Email("patricia@example.com"))
                    .withAppointmentStart(LocalDateTime.of(2026, 3, 23, 14, 30))
                    .withBilling(new Billing(Recurrence.MONTHLY, (LocalDate.of(2026, 3, 5)), 25,
                                new PaymentHistory(LocalDate.of(2026, 3, 5))))
                    .addAttendance(LocalDateTime.of(2026, 3, 16, 14, 30))
                    .build(),
            new PersonBuilder(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                    new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), getTagSet("JC"))
                    .withAcademics(new Academics(getSubjectSet(
                            new Subject("Mathematics", Level.STRONG),
                            new Subject("Economics", Level.STRONG))))
                    .withParentName(new Name("Mary Li"))
                    .withParentPhone(new Phone("95432109"))
                    .withParentEmail(new Email("mary@example.com"))
                    .withAppointmentStart(LocalDateTime.of(2026, 3, 24, 10, 0))
                    .withBilling(new Billing(Recurrence.MONTHLY, (LocalDate.of(2026, 3, 1)), 25,
                                new PaymentHistory(LocalDate.of(2026, 3, 1))))
                    .addAttendance(LocalDateTime.of(2026, 3, 19, 10, 0))
                    .build(),
            new PersonBuilder(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                    new Address("Blk 47 Tampines Street 20, #17-35"), getTagSet("Primary"))
                    .withAcademics(new Academics(getSubjectSet(
                            new Subject("Science", Level.BASIC),
                            new Subject("Mathematics", Level.BASIC))))
                    .withParentName(new Name("Zahra Ibrahim"))
                    .withParentPhone(new Phone("94321098"))
                    .withParentEmail(new Email("zahra@example.com"))
                    .withAppointmentStart(LocalDateTime.of(2026, 3, 25, 11, 30))
                    .withBilling(new Billing(Recurrence.MONTHLY, (LocalDate.of(2026, 2, 28)), 25,
                                new PaymentHistory(LocalDate.of(2026, 2, 27))))
                    .addAttendance(LocalDateTime.of(2026, 3, 15, 11, 30))
                    .build(),
            new PersonBuilder(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                    new Address("Blk 45 Aljunied Street 85, #11-31"), getTagSet("JC"))
                    .withAcademics(new Academics(getSubjectSet(
                            new Subject("Physics", Level.STRONG),
                            new Subject("Mathematics", Level.BASIC))))
                    .withParentName(new Name("Priya Balakrishnan"))
                    .withParentPhone(new Phone("93210987"))
                    .withParentEmail(new Email("priya@example.com"))
                    .withAppointmentStart(LocalDateTime.of(2026, 3, 26, 17, 0))
                    .withBilling(new Billing(Recurrence.MONTHLY, (LocalDate.of(2026, 2, 25)), 25,
                                new PaymentHistory(LocalDate.of(2026, 2, 24))))
                                        .addAttendance(LocalDateTime.of(2026, 3, 20, 17, 0))
                    .build()
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a subject set containing the subjects given.
     */
    public static Set<Subject> getSubjectSet(Subject... subjects) {
        return new HashSet<>(Arrays.asList(subjects));
    }

}
