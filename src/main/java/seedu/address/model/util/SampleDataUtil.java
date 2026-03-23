package seedu.address.model.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.billing.PaymentHistory;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.person.Phone;
import seedu.address.model.subject.Level;
import seedu.address.model.subject.Subject;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new PersonBuilder(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                    new Address("Blk 30 Geylang Street 29, #06-40"), getTagSet("Primary"))
                    .withSubjects(getSubjectSet(new Subject("Mathematics", Level.BASIC),
                            new Subject("English", Level.BASIC)))
                    .withParentName(Optional.of(new Name("Janet Yeoh")))
                    .withParentPhone(Optional.of(new Phone("98765432")))
                    .withParentEmail(Optional.of(new Email("janet@example.com")))
                    .withAppointmentStart(Optional.of(LocalDateTime.of(2026, 3, 21, 15, 30)))
                    .withPayment(new PaymentHistory((LocalDate.of(2026, 3, 15))))
                    .withLastAttendance(Optional.of(LocalDateTime.of(2026, 3, 18, 15, 30)))
                    .build(),
            new PersonBuilder(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                    new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                    getTagSet("Secondary"))
                    .withSubjects(getSubjectSet(new Subject("Physics", Level.STRONG),
                            new Subject("Chemistry", Level.BASIC)))
                    .withParentName(Optional.of(new Name("Ming Yu")))
                    .withParentPhone(Optional.of(new Phone("97654321")))
                    .withParentEmail(Optional.of(new Email("ming@example.com")))
                    .withAppointmentStart(Optional.of(LocalDateTime.of(2026, 3, 22, 16, 0)))
                    .withPayment(new PaymentHistory(LocalDate.of(2026, 3, 10)))
                    .withLastAttendance(Optional.of(LocalDateTime.of(2026, 3, 17, 16, 0)))
                    .build(),
            new PersonBuilder(new Name("Charlotte Oliveiro"), new Phone("93210283"),
                    new Email("charlotte@example.com"), new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                    getTagSet("Secondary"))
                    .withSubjects(getSubjectSet(new Subject("English", Level.STRONG),
                            new Subject("Literature", Level.BASIC)))
                    .withParentName(Optional.of(new Name("Patricia Oliveiro")))
                    .withParentPhone(Optional.of(new Phone("96543210")))
                    .withParentEmail(Optional.of(new Email("patricia@example.com")))
                    .withAppointmentStart(Optional.of(LocalDateTime.of(2026, 3, 23, 14, 30)))
                    .withPayment(new PaymentHistory(LocalDate.of(2026, 3, 5)))
                    .withLastAttendance(Optional.of(LocalDateTime.of(2026, 3, 16, 14, 30)))
                    .build(),
            new PersonBuilder(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                    new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), getTagSet("JC"))
                    .withSubjects(getSubjectSet(new Subject("Mathematics", Level.STRONG),
                            new Subject("Economics", Level.STRONG)))
                    .withParentName(Optional.of(new Name("Mary Li")))
                    .withParentPhone(Optional.of(new Phone("95432109")))
                    .withParentEmail(Optional.of(new Email("mary@example.com")))
                    .withAppointmentStart(Optional.of(LocalDateTime.of(2026, 3, 24, 10, 0)))
                    .withPayment(new PaymentHistory(LocalDate.of(2026, 3, 1)))
                    .withLastAttendance(Optional.of(LocalDateTime.of(2026, 3, 19, 10, 0)))
                    .build(),
            new PersonBuilder(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                    new Address("Blk 47 Tampines Street 20, #17-35"), getTagSet("Primary"))
                    .withSubjects(getSubjectSet(new Subject("Science", Level.BASIC),
                            new Subject("Mathematics", Level.BASIC)))
                    .withParentName(Optional.of(new Name("Zahra Ibrahim")))
                    .withParentPhone(Optional.of(new Phone("94321098")))
                    .withParentEmail(Optional.of(new Email("zahra@example.com")))
                    .withAppointmentStart(Optional.of(LocalDateTime.of(2026, 3, 25, 11, 30)))
                    .withPayment(new PaymentHistory(LocalDate.of(2026, 2, 28)))
                    .withLastAttendance(Optional.of(LocalDateTime.of(2026, 3, 15, 11, 30)))
                    .build(),
            new PersonBuilder(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                    new Address("Blk 45 Aljunied Street 85, #11-31"), getTagSet("JC"))
                    .withSubjects(getSubjectSet(new Subject("Physics", Level.STRONG),
                            new Subject("Mathematics", Level.BASIC)))
                    .withParentName(Optional.of(new Name("Priya Balakrishnan")))
                    .withParentPhone(Optional.of(new Phone("93210987")))
                    .withParentEmail(Optional.of(new Email("priya@example.com")))
                    .withAppointmentStart(Optional.of(LocalDateTime.of(2026, 3, 26, 17, 0)))
                    .withPayment(new PaymentHistory(LocalDate.of(2026, 2, 25)))
                    .withLastAttendance(Optional.of(LocalDateTime.of(2026, 3, 20, 17, 0)))
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
