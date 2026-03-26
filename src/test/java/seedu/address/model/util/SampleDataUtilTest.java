package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;

public class SampleDataUtilTest {

    @Test
    public void getSamplePersons_returnsNonEmptyArray() {
        Person[] samplePersons = SampleDataUtil.getSamplePersons();
        assertTrue(samplePersons.length > 0);
        for (Person person : samplePersons) {
            assertTrue(person.getGuardian().isPresent());
        }
    }

    @Test
    public void getSampleAddressBook_returnsNonEmptyAddressBook() {
        ReadOnlyAddressBook sampleAb = SampleDataUtil.getSampleAddressBook();
        assertEquals(SampleDataUtil.getSamplePersons().length, sampleAb.getPersonList().size());
    }
}
