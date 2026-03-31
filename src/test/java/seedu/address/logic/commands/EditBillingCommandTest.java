package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PAYMENT_AMOUNT;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.billing.Billing;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditBillingCommand.
 */
public class EditBillingCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        double tuitionFee = Double.parseDouble(VALID_PAYMENT_AMOUNT);
        EditBillingCommand editCommand = new EditBillingCommand(INDEX_FIRST_PERSON, tuitionFee);

        Billing updatedBilling = personToEdit.getBilling().updateRate(tuitionFee);
        Person editedPerson = new PersonBuilder(personToEdit)
                .withBilling(updatedBilling)
                .build();

        String expectedMessage = String.format(EditBillingCommand.MESSAGE_EDIT_BILLING_SUCCESS,
                Messages.format(editedPerson), tuitionFee);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(editCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        double tuitionFee = Double.parseDouble(VALID_PAYMENT_AMOUNT);
        EditBillingCommand editCommand = new EditBillingCommand(outOfBoundIndex, tuitionFee);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        double tuitionFee = Double.parseDouble(VALID_PAYMENT_AMOUNT);
        EditBillingCommand standardCommand = new EditBillingCommand(INDEX_FIRST_PERSON, tuitionFee);

        EditBillingCommand commandWithSameValues = new EditBillingCommand(INDEX_FIRST_PERSON, tuitionFee);
        assertTrue(standardCommand.equals(commandWithSameValues));

        assertTrue(standardCommand.equals(standardCommand));

        assertFalse(standardCommand.equals(null));

        assertFalse(standardCommand.equals(new ClearCommand()));

        assertFalse(standardCommand.equals(new EditBillingCommand(INDEX_SECOND_PERSON, tuitionFee)));

        assertFalse(standardCommand.equals(new EditBillingCommand(INDEX_FIRST_PERSON, 20.0)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        double tuitionFee = Double.parseDouble(VALID_PAYMENT_AMOUNT);
        EditBillingCommand editCommand = new EditBillingCommand(index, tuitionFee);
        String expected = EditBillingCommand.class.getCanonicalName()
                + "{index=" + index
                + ", tuitionFee=" + tuitionFee
                + "}";
        assertEquals(expected, editCommand.toString());
    }
}
