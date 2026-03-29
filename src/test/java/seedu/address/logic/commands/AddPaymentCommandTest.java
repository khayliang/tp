package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PAYMENT_AMOUNT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PAYMENT_DATE;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
 * Contains integration tests (interaction with the Model) and unit tests for AddPaymentCommand.
 */
public class AddPaymentCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        LocalDate paymentDate = LocalDate.parse(VALID_PAYMENT_DATE);
        Optional<Double> amountPaid = Optional.of(VALID_PAYMENT_AMOUNT).map(Double::parseDouble);
        AddPaymentCommand addCommand = new AddPaymentCommand(INDEX_FIRST_PERSON, paymentDate, amountPaid);

        Billing updatedBilling = personToEdit.recordFeesPaidAndAdvanceBilling(paymentDate)
                .updateRate(Double.parseDouble(VALID_PAYMENT_AMOUNT));
        Person editedPerson = new PersonBuilder(personToEdit)
                .withBilling(updatedBilling)
                .build();

        String expectedMessage = String.format(AddPaymentCommand.MESSAGE_ADD_PAYMENT_SUCCESS,
                editedPerson.getBilling().getTuitionFee(),
                Messages.format(editedPerson),
                paymentDate.format(DateTimeFormatter.ISO_LOCAL_DATE));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(addCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        LocalDate paymentDate = LocalDate.parse(VALID_PAYMENT_DATE);
        Optional<Double> amountPaid = Optional.of(VALID_PAYMENT_AMOUNT).map(Double::parseDouble);
        AddPaymentCommand addCommand = new AddPaymentCommand(outOfBoundIndex, paymentDate, amountPaid);

        assertCommandFailure(addCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        LocalDate paymentDate = LocalDate.parse(VALID_PAYMENT_DATE);
        Optional<Double> amountPaid = Optional.of(VALID_PAYMENT_AMOUNT).map(Double::parseDouble);
        AddPaymentCommand standardCommand = new AddPaymentCommand(INDEX_FIRST_PERSON, paymentDate, amountPaid);

        AddPaymentCommand commandWithSameValues = new AddPaymentCommand(INDEX_FIRST_PERSON, paymentDate, amountPaid);
        assertTrue(standardCommand.equals(commandWithSameValues));
        assertTrue(standardCommand.equals(standardCommand));
        assertFalse(standardCommand.equals(null));
        assertFalse(standardCommand.equals(new ClearCommand()));
        assertFalse(standardCommand.equals(new AddPaymentCommand(INDEX_SECOND_PERSON, paymentDate, amountPaid)));

        LocalDate differentPaymentDate = LocalDate.parse("2026-02-01");
        assertFalse(standardCommand.equals(new AddPaymentCommand(
                INDEX_FIRST_PERSON, differentPaymentDate, amountPaid)));

        Optional<Double> differentAmountPaid = Optional.of("20").map(Double::parseDouble);
        assertFalse(standardCommand.equals(new AddPaymentCommand(
                INDEX_FIRST_PERSON, paymentDate, differentAmountPaid)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        LocalDate paymentDate = LocalDate.parse(VALID_PAYMENT_DATE);
        Optional<Double> amountPaid = Optional.of(VALID_PAYMENT_AMOUNT).map(Double::parseDouble);
        AddPaymentCommand addCommand = new AddPaymentCommand(index, paymentDate, amountPaid);
        String expected = AddPaymentCommand.class.getCanonicalName()
                + "{index=" + index
                + ", paymentDate=" + paymentDate
                + ", amount=" + amountPaid.get()
                + "}";
        assertEquals(expected, addCommand.toString());
    }
}
