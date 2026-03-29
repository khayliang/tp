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

import java.time.LocalDate;
import java.util.Optional;

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

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredListAmountOnly_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Optional<Double> tuitionFee = Optional.of(Double.parseDouble(VALID_PAYMENT_AMOUNT));
        EditBillingCommand editCommand = new EditBillingCommand(
                INDEX_FIRST_PERSON, tuitionFee, Optional.empty());

        Billing updatedBilling = personToEdit.getBilling().updateRate(tuitionFee.get());
        Person editedPerson = new PersonBuilder(personToEdit)
                .withBilling(updatedBilling)
                .build();

        String expectedMessage = String.format(EditBillingCommand.MESSAGE_EDIT_TUITION_FEE_SUCCESS,
                Messages.format(editedPerson), updatedBilling.getTuitionFee());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(editCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_validIndexUnfilteredListDateOnly_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Optional<LocalDate> paymentDueDate = Optional.of(LocalDate.of(2026, 3, 1));
        EditBillingCommand editCommand = new EditBillingCommand(
                INDEX_FIRST_PERSON, Optional.empty(), paymentDueDate);

        Billing updatedBilling = personToEdit.getBilling().updatePaymentDueDate(paymentDueDate.get());
        Person editedPerson = new PersonBuilder(personToEdit)
                .withBilling(updatedBilling)
                .build();

        String expectedMessage = String.format(EditBillingCommand.MESSAGE_EDIT_PAYMENT_DUE_SUCCESS,
                Messages.format(editedPerson), updatedBilling.getCurrentDueDate());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(editCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_validIndexUnfilteredListAmountAndDate_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        double tuitionFee = Double.parseDouble(VALID_PAYMENT_AMOUNT);
        LocalDate paymentDueDate = LocalDate.of(2026, 3, 1);
        EditBillingCommand editCommand = new EditBillingCommand(INDEX_FIRST_PERSON,
                Optional.of(tuitionFee), Optional.of(paymentDueDate));

        Billing updatedBilling = personToEdit.getBilling()
                .updateRate(tuitionFee)
                .updatePaymentDueDate(paymentDueDate);
        Person editedPerson = new PersonBuilder(personToEdit)
                .withBilling(updatedBilling)
                .build();

        String expectedMessage = String.format(EditBillingCommand.MESSAGE_EDIT_BILLING_SUCCESS,
                Messages.format(editedPerson),
                updatedBilling.getTuitionFee(),
                updatedBilling.getCurrentDueDate());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(editCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Optional<Double> tuitionFee = Optional.of(Double.parseDouble(VALID_PAYMENT_AMOUNT));
        EditBillingCommand editCommand = new EditBillingCommand(
                outOfBoundIndex, tuitionFee, Optional.empty());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Optional<Double> tuitionFee = Optional.of(Double.parseDouble(VALID_PAYMENT_AMOUNT));
        Optional<LocalDate> paymentDueDate = Optional.of(LocalDate.of(2026, 3, 1));
        EditBillingCommand standardCommand = new EditBillingCommand(
                INDEX_FIRST_PERSON, tuitionFee, Optional.empty());

        EditBillingCommand commandWithSameValues = new EditBillingCommand(
                INDEX_FIRST_PERSON, tuitionFee, Optional.empty());
        assertTrue(standardCommand.equals(commandWithSameValues));

        assertTrue(standardCommand.equals(standardCommand));

        assertFalse(standardCommand.equals(null));

        assertFalse(standardCommand.equals(new ClearCommand()));

        assertFalse(standardCommand.equals(new EditBillingCommand(
                INDEX_SECOND_PERSON, tuitionFee, Optional.empty())));

        assertFalse(standardCommand.equals(new EditBillingCommand(
                INDEX_FIRST_PERSON, Optional.empty(), paymentDueDate)));

        assertFalse(standardCommand.equals(new EditBillingCommand(INDEX_FIRST_PERSON,
                tuitionFee, paymentDueDate)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        Optional<Double> tuitionFee = Optional.of(Double.parseDouble(VALID_PAYMENT_AMOUNT));
        EditBillingCommand editCommand = new EditBillingCommand(
                index, tuitionFee, Optional.empty());
        String expected = EditBillingCommand.class.getCanonicalName()
                + "{index=" + index
                + ", tuitionFee=" + tuitionFee
                + ", paymentDue=" + Optional.empty()
                + "}";
        assertEquals(expected, editCommand.toString());
    }
}
