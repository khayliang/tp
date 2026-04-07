package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_GROUP1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_JC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import seedu.address.model.AddressBook;
import seedu.address.model.academic.Academics;
import seedu.address.model.academic.Level;
import seedu.address.model.academic.Subject;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Guardian;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    public static final Person ALICE = getPersonBuilder("Alice Pauline", "94351253",
            "alice@example.com", "123, Jurong West Ave 6, #08-111")
            .withTags(Set.of(new Tag("friends")))
            .withAcademics(new Academics(Set.of(
                    new Subject("Math", Level.STRONG)
            )))
            .build();
    public static final Person BENSON = getPersonBuilder("Benson Meier", "98765432",
            "johnd@example.com", "311, Clementi Ave 2, #02-25")
            .withTags(Set.of(new Tag("owesMoney"), new Tag("friends")))
            .withAcademics(new Academics(Set.of(
                    new Subject("Math", Level.STRONG),
                    new Subject("Science", Level.BASIC)
            )))
            .withGuardian(new Guardian(new Name("Susan Meier"), null, null))
            .build();
    public static final Person CARL = getPersonBuilder("Carl Kurz", "95352563",
            "heinz@example.com", "wall street").build();
    public static final Person DANIEL = getPersonBuilder("Daniel Meier", "87652533",
            "cornelia@example.com", "10th street")
            .withTags(Set.of(new Tag("friends"))).build();
    public static final Person ELLE = getPersonBuilder("Elle Meyer", "94822240",
            "werner@example.com", "michegan ave").build();
    public static final Person FIONA = getPersonBuilder("Fiona Kunz", "94824270",
            "lydia@example.com", "little tokyo").build();
    public static final Person GEORGE = getPersonBuilder("George Best", "94824420",
            "anna@example.com", "4th street").build();

    // Manually added
    public static final Person HOON = getPersonBuilder("Hoon Meier", "98482424",
            "stefan@example.com", "little india").build();
    public static final Person IDA = getPersonBuilder("Ida Mueller", "98482131",
            "hans@example.com", "chicago ave").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = getPersonBuilder(VALID_NAME_AMY, VALID_PHONE_AMY,
            VALID_EMAIL_AMY, VALID_ADDRESS_AMY)
            .withTags(Set.of(new Tag(VALID_TAG_JC))).build();
    public static final Person BOB = getPersonBuilder(VALID_NAME_BOB, VALID_PHONE_BOB,
            VALID_EMAIL_BOB, VALID_ADDRESS_BOB)
            .withTags(Set.of(new Tag(VALID_TAG_GROUP1), new Tag(VALID_TAG_JC))).build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    public static PersonBuilder getPersonBuilder() {
        return getPersonBuilder(DEFAULT_NAME, DEFAULT_PHONE, DEFAULT_EMAIL, DEFAULT_ADDRESS);
    }

    public static PersonBuilder getPersonBuilder(String name) {
        return getPersonBuilder(name, DEFAULT_PHONE, DEFAULT_EMAIL, DEFAULT_ADDRESS);
    }

    public static PersonBuilder getPersonBuilder(String name, String phone, String email, String address) {
        return new PersonBuilder(
                new Name(name),
                new Phone(phone),
                new Email(email),
                new Address(address),
                Set.of());
    }

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
