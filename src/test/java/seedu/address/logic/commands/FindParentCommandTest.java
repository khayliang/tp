package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.getPersonBuilder;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Email;
import seedu.address.model.person.Guardian;
import seedu.address.model.person.GuardianContainsKeywordsPredicate;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

public class FindParentCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    // ──────────────────────── equals ────────────────────────

    @Test
    public void equals_sameObject_returnsTrue() {
        FindParentCommand cmd = new FindParentCommand(makePredicate(
                List.of("Alice"), Collections.emptyList(), Collections.emptyList()));
        assertTrue(cmd.equals(cmd));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        GuardianContainsKeywordsPredicate pred = makePredicate(
                List.of("Alice"), Collections.emptyList(), Collections.emptyList());
        assertTrue(new FindParentCommand(pred).equals(new FindParentCommand(pred)));
    }

    @Test
    public void equals_differentName_returnsFalse() {
        FindParentCommand a = new FindParentCommand(makePredicate(
                List.of("Alice"), Collections.emptyList(), Collections.emptyList()));
        FindParentCommand b = new FindParentCommand(makePredicate(
                List.of("Bob"), Collections.emptyList(), Collections.emptyList()));
        assertFalse(a.equals(b));
    }

    @Test
    public void equals_differentPhone_returnsFalse() {
        FindParentCommand a = new FindParentCommand(makePredicate(
                Collections.emptyList(), List.of("91234567"), Collections.emptyList()));
        FindParentCommand b = new FindParentCommand(makePredicate(
                Collections.emptyList(), List.of("99999999"), Collections.emptyList()));
        assertFalse(a.equals(b));
    }

    @Test
    public void equals_differentEmail_returnsFalse() {
        FindParentCommand a = new FindParentCommand(makePredicate(
                Collections.emptyList(), Collections.emptyList(), List.of("alice@example.com")));
        FindParentCommand b = new FindParentCommand(makePredicate(
                Collections.emptyList(), Collections.emptyList(), List.of("bob@example.com")));
        assertFalse(a.equals(b));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        FindParentCommand cmd = new FindParentCommand(makePredicate(
                List.of("Alice"), Collections.emptyList(), Collections.emptyList()));
        assertFalse(cmd.equals(1));
    }

    @Test
    public void equals_null_returnsFalse() {
        FindParentCommand cmd = new FindParentCommand(makePredicate(
                List.of("Alice"), Collections.emptyList(), Collections.emptyList()));
        assertFalse(cmd.equals(null));
    }

    // ──────────────────────── execute ────────────────────────

    @Test
    public void execute_nameNotFound_noPersonFound() {
        // "NonExistent" does not match any guardian name in typical persons
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        GuardianContainsKeywordsPredicate predicate = makePredicate(
                List.of("NonExistent"), Collections.emptyList(), Collections.emptyList());
        FindParentCommand command = new FindParentCommand(predicate);
        expectedModel.updateFilteredPersonListWithAnd(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_noGuardian_noPersonFound() {
        // ALICE has no guardian
        Model localModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model localExpectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        GuardianContainsKeywordsPredicate predicate = makePredicate(
                List.of("Alice"), Collections.emptyList(), Collections.emptyList());
        FindParentCommand command = new FindParentCommand(predicate);
        localExpectedModel.updateFilteredPersonListWithAnd(predicate);
        assertCommandSuccess(command, localModel, String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0),
                localExpectedModel);
        assertEquals(Collections.emptyList(), localModel.getFilteredPersonList());
    }

    @Test
    public void execute_matchByGuardianName_onePersonFound() {
        // BENSON has guardian "Susan Meier" (set in TypicalPersons)
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        GuardianContainsKeywordsPredicate predicate = makePredicate(
                List.of("Susan"), Collections.emptyList(), Collections.emptyList());
        FindParentCommand command = new FindParentCommand(predicate);
        expectedModel.updateFilteredPersonListWithAnd(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(BENSON), model.getFilteredPersonList());
    }

    @Test
    public void execute_matchByGuardianNameCaseInsensitive_onePersonFound() {
        // All-upper-case keyword should still match "Susan"
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        GuardianContainsKeywordsPredicate predicate = makePredicate(
                List.of("SUSAN"), Collections.emptyList(), Collections.emptyList());
        FindParentCommand command = new FindParentCommand(predicate);
        expectedModel.updateFilteredPersonListWithAnd(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.singletonList(BENSON), model.getFilteredPersonList());
    }

    @Test
    public void execute_matchByGuardianPhone_onePersonFound() {
        // Add a person with a guardian phone to a fresh model
        Person personWithParentPhone = getPersonBuilder("Test Student")
                .withGuardian(new Guardian(new Name("Test Parent"), new Phone("81234567"),
                        null))
                .build();
        Model localModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model localExpectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        localModel.addPerson(personWithParentPhone);
        localExpectedModel.addPerson(personWithParentPhone);

        GuardianContainsKeywordsPredicate predicate = makePredicate(
                Collections.emptyList(), List.of("81234567"), Collections.emptyList());
        FindParentCommand command = new FindParentCommand(predicate);
        localExpectedModel.updateFilteredPersonListWithAnd(predicate);
        assertCommandSuccess(command, localModel,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1), localExpectedModel);
        assertEquals(Collections.singletonList(personWithParentPhone), localModel.getFilteredPersonList());
    }

    @Test
    public void execute_matchByGuardianPhonePartial_onePersonFound() {
        // Partial phone substring should match
        Person personWithParentPhone = getPersonBuilder("Test Student")
                .withGuardian(new Guardian(new Name("Test Parent"), new Phone("81234567"),
                        null))
                .build();
        Model localModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model localExpectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        localModel.addPerson(personWithParentPhone);
        localExpectedModel.addPerson(personWithParentPhone);

        GuardianContainsKeywordsPredicate predicate = makePredicate(
                Collections.emptyList(), List.of("8123"), Collections.emptyList());
        FindParentCommand command = new FindParentCommand(predicate);
        localExpectedModel.updateFilteredPersonListWithAnd(predicate);
        assertCommandSuccess(command, localModel,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1), localExpectedModel);
        assertEquals(Collections.singletonList(personWithParentPhone), localModel.getFilteredPersonList());
    }

    @Test
    public void execute_matchByGuardianEmail_onePersonFound() {
        Person personWithParentEmail = getPersonBuilder("Test Student")
                .withGuardian(new Guardian(new Name("Test Parent"), null,
                        new Email("parent@example.com")))
                .build();
        Model localModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model localExpectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        localModel.addPerson(personWithParentEmail);
        localExpectedModel.addPerson(personWithParentEmail);

        GuardianContainsKeywordsPredicate predicate = makePredicate(
                Collections.emptyList(), Collections.emptyList(), List.of("parent@example.com"));
        FindParentCommand command = new FindParentCommand(predicate);
        localExpectedModel.updateFilteredPersonListWithAnd(predicate);
        assertCommandSuccess(command, localModel,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1), localExpectedModel);
        assertEquals(Collections.singletonList(personWithParentEmail), localModel.getFilteredPersonList());
    }

    @Test
    public void execute_matchByGuardianEmailCaseInsensitive_onePersonFound() {
        Person personWithParentEmail = getPersonBuilder("Test Student")
                .withGuardian(new Guardian(new Name("Test Parent"), null,
                        new Email("parent@example.com")))
                .build();
        Model localModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model localExpectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        localModel.addPerson(personWithParentEmail);
        localExpectedModel.addPerson(personWithParentEmail);

        GuardianContainsKeywordsPredicate predicate = makePredicate(
                Collections.emptyList(), Collections.emptyList(), List.of("PARENT@EXAMPLE.COM"));
        FindParentCommand command = new FindParentCommand(predicate);
        localExpectedModel.updateFilteredPersonListWithAnd(predicate);
        assertCommandSuccess(command, localModel,
                String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1), localExpectedModel);
        assertEquals(Collections.singletonList(personWithParentEmail), localModel.getFilteredPersonList());
    }

    @Test
    public void execute_nameAndPhoneEitherMatches_orLogic() {
        // Guardian name matches even though phone does not, so the person should still be listed
        GuardianContainsKeywordsPredicate predicate = makePredicate(
                List.of("Susan"), List.of("00000000"), Collections.emptyList());
        FindParentCommand command = new FindParentCommand(predicate);
        expectedModel.updateFilteredPersonListWithAnd(predicate);
        assertCommandSuccess(command, model, String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1), expectedModel);
        assertEquals(Collections.singletonList(BENSON), model.getFilteredPersonList());
    }

    // ──────────────────────── toString ────────────────────────

    @Test
    public void toStringMethod() {
        GuardianContainsKeywordsPredicate predicate = makePredicate(
                Arrays.asList("Alice"), Collections.emptyList(), Collections.emptyList());
        FindParentCommand cmd = new FindParentCommand(predicate);
        String expected = FindParentCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, cmd.toString());
    }

    // ──────────────────────── helpers ────────────────────────

    private GuardianContainsKeywordsPredicate makePredicate(
            List<String> nameKeywords, List<String> phoneKeywords, List<String> emailKeywords) {
        return new GuardianContainsKeywordsPredicate(nameKeywords, phoneKeywords, emailKeywords);
    }
}
