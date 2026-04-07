package seedu.address.model.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.util.AppClock;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.academic.Academics;
import seedu.address.model.academic.Level;
import seedu.address.model.academic.Subject;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.attendance.AttendanceHistory;
import seedu.address.model.billing.Billing;
import seedu.address.model.billing.PaymentHistory;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Guardian;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.person.Phone;
import seedu.address.model.recurrence.Recurrence;
import seedu.address.model.session.Appointment;
import seedu.address.model.session.ScheduledSession;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        LocalDate today = AppClock.today();

        return new Person[] {
            new PersonBuilder(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                    new Address("Blk 30 Geylang Street 29, #06-40"), getTagSet("Primary"))
                    .withAcademics(new Academics(getSubjectSet(
                            new Subject("Mathematics", Level.BASIC),
                            new Subject("English", Level.BASIC))))
                    .withGuardian(new Guardian(
                            new Name("Janet Yeoh"), new Phone("98765432"),
                            new Email("janet@example.com")))
                    .withAppointment(getAppointment(
                            new ScheduledSession(Recurrence.WEEKLY,
                                    relativeDateTime(today, -49, 15, 30),
                                    relativeDateTime(today, 4, 15, 30),
                                    getAttendanceHistory(
                                            attendance(true, today, -31),
                                            attendance(true, today, -24),
                                            attendance(false, today, -17),
                                            attendance(true, today, -10),
                                            attendance(true, today, -3)),
                                    "Math and English Core Lesson"),
                            new ScheduledSession(Recurrence.MONTHLY,
                                    relativeDateTime(today, -90, 17, 0),
                                    relativeDateTime(today, 18, 17, 0),
                                    getAttendanceHistory(
                                            attendance(true, today, -90),
                                            attendance(true, today, -60),
                                            attendance(true, today, -30)),
                                    "Parent Progress Review")))
                    .withBilling(new Billing(Recurrence.MONTHLY, relativeDate(today, 13), 45.0,
                            getPaymentHistory(
                                    relativeDate(today, -82),
                                    relativeDate(today, -53),
                                    relativeDate(today, -24))))
                    .build(),
            new PersonBuilder(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                    new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                    getTagSet("Secondary"))
                    .withAcademics(new Academics(getSubjectSet(
                            new Subject("Physics", Level.STRONG),
                            new Subject("Chemistry", Level.BASIC))))
                    .withGuardian(new Guardian(
                            new Name("Ming Yu"), new Phone("97654321"),
                            new Email("ming@example.com")))
                    .withAppointment(getAppointment(
                            new ScheduledSession(Recurrence.BIWEEKLY,
                                    relativeDateTime(today, -70, 16, 0),
                                    relativeDateTime(today, 12, 16, 0),
                                    getAttendanceHistory(
                                            attendance(true, today, -56),
                                            attendance(true, today, -42),
                                            attendance(false, today, -28),
                                            attendance(true, today, -14)),
                                    "Physics and Chemistry Main Session"),
                            new ScheduledSession(Recurrence.MONTHLY,
                                    relativeDateTime(today, -84, 10, 30),
                                    relativeDateTime(today, 6, 10, 30),
                                    getAttendanceHistory(
                                            attendance(true, today, -56),
                                            attendance(true, today, -28)),
                                    "Practical Lab Revision")))
                    .withBilling(new Billing(Recurrence.MONTHLY, relativeDate(today, 3), 55.0,
                            getPaymentHistory(
                                    relativeDate(today, -88),
                                    relativeDate(today, -58),
                                    relativeDate(today, -28))))
                    .build(),
            new PersonBuilder(new Name("Charlotte Oliveiro"), new Phone("93210283"),
                    new Email("charlotte@example.com"), new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                    getTagSet("Secondary"))
                    .withAcademics(new Academics(getSubjectSet(
                            new Subject("English", Level.STRONG),
                            new Subject("Literature", Level.BASIC))))
                    .withGuardian(new Guardian(
                            new Name("Patricia Oliveiro"), new Phone("96543210"),
                            new Email("patricia@example.com")))
                    .withAppointment(getAppointment(
                            new ScheduledSession(Recurrence.WEEKLY,
                                    relativeDateTime(today, -63, 14, 30),
                                    relativeDateTime(today, 7, 14, 30),
                                    getAttendanceHistory(
                                            attendance(true, today, -49),
                                            attendance(false, today, -42),
                                            attendance(true, today, -28),
                                            attendance(true, today, -14)),
                                    "English and Literature Weekly Session"),
                            new ScheduledSession(Recurrence.NONE,
                                    relativeDateTime(today, -5, 16, 0),
                                    relativeDateTime(today, -5, 16, 0),
                                    getAttendanceHistory(
                                            attendance(true, today, -5)),
                                    "Essay Consult")))
                    .withBilling(new Billing(Recurrence.MONTHLY, relativeDate(today, 28), 48.0,
                            getPaymentHistory(
                                    relativeDate(today, -75),
                                    relativeDate(today, -45),
                                    relativeDate(today, -15))))
                    .build(),
            new PersonBuilder(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                    new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), getTagSet("JC"))
                    .withAcademics(new Academics(getSubjectSet(
                            new Subject("Mathematics", Level.STRONG),
                            new Subject("Economics", Level.STRONG))))
                    .withGuardian(new Guardian(
                            new Name("Mary Li"), new Phone("95432109"),
                            new Email("mary@example.com")))
                    .withAppointment(getAppointment(
                            new ScheduledSession(Recurrence.WEEKLY,
                                    relativeDateTime(today, -70, 10, 0),
                                    relativeDateTime(today, 5, 10, 0),
                                    getAttendanceHistory(
                                            attendance(true, today, -49),
                                            attendance(true, today, -35),
                                            attendance(false, today, -21),
                                            attendance(true, today, -7)),
                                    "Mathematics and Economics Core Session"),
                            new ScheduledSession(Recurrence.BIWEEKLY,
                                    relativeDateTime(today, -56, 19, 30),
                                    relativeDateTime(today, 5, 19, 30),
                                    getAttendanceHistory(
                                            attendance(true, today, -42),
                                            attendance(true, today, -28),
                                            attendance(true, today, -14)),
                                    "Exam Strategy Session")))
                    .withBilling(new Billing(Recurrence.MONTHLY, relativeDate(today, 25), 65.0,
                            getPaymentHistory(
                                    relativeDate(today, -90),
                                    relativeDate(today, -60),
                                    relativeDate(today, -30))))
                    .build(),
            new PersonBuilder(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                    new Address("Blk 47 Tampines Street 20, #17-35"), getTagSet("Primary"))
                    .withAcademics(new Academics(getSubjectSet(
                            new Subject("Science", Level.BASIC),
                            new Subject("Mathematics", Level.BASIC))))
                    .withGuardian(new Guardian(
                            new Name("Zahra Ibrahim"), new Phone("94321098"),
                            new Email("zahra@example.com")))
                    .withAppointment(getAppointment(
                            new ScheduledSession(Recurrence.WEEKLY,
                                    relativeDateTime(today, -77, 11, 30),
                                    relativeDateTime(today, 2, 11, 30),
                                    getAttendanceHistory(
                                            attendance(true, today, -56),
                                            attendance(true, today, -49),
                                            attendance(false, today, -42),
                                            attendance(true, today, -35),
                                            attendance(true, today, -28),
                                            attendance(true, today, -21)),
                                    "Science and Mathematics Weekly Session")))
                    .withBilling(new Billing(Recurrence.MONTHLY, relativeDate(today, 20), 40.0,
                            getPaymentHistory(
                                    relativeDate(today, -84),
                                    relativeDate(today, -56),
                                    relativeDate(today, -27))))
                    .build(),
            new PersonBuilder(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                    new Address("Blk 45 Aljunied Street 85, #11-31"), getTagSet("JC"))
                    .withAcademics(new Academics(getSubjectSet(
                            new Subject("Physics", Level.STRONG),
                            new Subject("Mathematics", Level.BASIC))))
                    .withGuardian(new Guardian(
                            new Name("Priya Balakrishnan"), new Phone("93210987"),
                            new Email("priya@example.com")))
                    .withAppointment(getAppointment(
                            new ScheduledSession(Recurrence.WEEKLY,
                                    relativeDateTime(today, -63, 17, 0),
                                    relativeDateTime(today, 3, 17, 0),
                                    getAttendanceHistory(
                                            attendance(true, today, -49),
                                            attendance(false, today, -42),
                                            attendance(true, today, -35),
                                            attendance(true, today, -28),
                                            attendance(true, today, -14)),
                                    "Physics and Mathematics Main Session"),
                            new ScheduledSession(Recurrence.MONTHLY,
                                    relativeDateTime(today, -90, 19, 30),
                                    relativeDateTime(today, 10, 19, 30),
                                    getAttendanceHistory(
                                            attendance(true, today, -60),
                                            attendance(true, today, -30)),
                                    "Mock Paper Review")))
                    .withBilling(new Billing(Recurrence.MONTHLY, relativeDate(today, 24), 60.0,
                            getPaymentHistory(
                                    relativeDate(today, -89),
                                    relativeDate(today, -59),
                                    relativeDate(today, -29))))
                    .build()
        };
    }

    private static Attendance attendance(boolean hasAttended, LocalDate today, int daysOffset) {
        return new Attendance(hasAttended, relativeDate(today, daysOffset));
    }

    private static LocalDate relativeDate(LocalDate today, int daysOffset) {
        return today.plusDays(daysOffset);
    }

    private static LocalDateTime relativeDateTime(LocalDate today, int daysOffset, int hour, int minute) {
        return relativeDate(today, daysOffset).atTime(hour, minute);
    }

    private static Appointment getAppointment(ScheduledSession... sessions) {
        Appointment appointment = Appointment.defaultAppointment();
        for (ScheduledSession session : sessions) {
            appointment = appointment.addSession(session);
        }
        return appointment;
    }

    private static AttendanceHistory getAttendanceHistory(Attendance... attendances) {
        return new AttendanceHistory(attendances);
    }

    private static PaymentHistory getPaymentHistory(LocalDate... paymentDates) {
        return new PaymentHistory(paymentDates);
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
