package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PAYMENT_DATE;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.billing.Billing;
import seedu.address.model.billing.PaymentHistory;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.recurrence.Recurrence;

/**
 * Contains integration tests (interaction with the Model) and unit tests for AddPaymentCommand.
 */
public class AddPaymentCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        LocalDate paymentDate = LocalDate.parse(VALID_PAYMENT_DATE);
        AddPaymentCommand addCommand = new AddPaymentCommand(INDEX_FIRST_PERSON, paymentDate);

        Billing updatedBilling = personToEdit.recordFeesPaidAndAdvanceBilling(paymentDate);
        Person editedPerson = new PersonBuilder(personToEdit)
                .withBilling(updatedBilling)
                .build();

        String expectedMessage = buildExpectedSuccessMessage(personToEdit, editedPerson, paymentDate);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(addCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        LocalDate paymentDate = LocalDate.parse(VALID_PAYMENT_DATE);
        AddPaymentCommand addCommand = new AddPaymentCommand(outOfBoundIndex, paymentDate);

        assertCommandFailure(addCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicatePaymentDate_failureAndBillingUnchanged() {
        LocalDate paymentDate = LocalDate.parse(VALID_PAYMENT_DATE);

        Person initialPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        AddPaymentCommand firstAddCommand = new AddPaymentCommand(INDEX_FIRST_PERSON, paymentDate);

        Billing billingAfterFirstPayment = initialPerson.recordFeesPaidAndAdvanceBilling(paymentDate);
        Person personAfterFirstPayment = new PersonBuilder(initialPerson)
            .withBilling(billingAfterFirstPayment)
            .build();

        String firstSuccessMessage = buildExpectedSuccessMessage(initialPerson, personAfterFirstPayment, paymentDate);

        Model expectedModelAfterFirstPayment = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModelAfterFirstPayment.setPerson(initialPerson, personAfterFirstPayment);

        CommandResult expectedCommandResult = new CommandResult(firstSuccessMessage, personAfterFirstPayment);
        assertCommandSuccess(firstAddCommand, model, expectedCommandResult, expectedModelAfterFirstPayment);

        AddPaymentCommand duplicateAddCommand = new AddPaymentCommand(INDEX_FIRST_PERSON, paymentDate);
        String expectedFailureMessage = String.format(AddPaymentCommand.MESSAGE_PAYMENT_DATE_IS_PRESENT,
            paymentDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
            Messages.format(personAfterFirstPayment));

        Billing billingBeforeDuplicateCommand =
            model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()).getBilling();
        LocalDate dueDateBeforeDuplicateCommand = billingBeforeDuplicateCommand.getCurrentDueDate();

        assertCommandFailure(duplicateAddCommand, model, expectedFailureMessage);

        Person personAfterDuplicateFailure = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertEquals(billingBeforeDuplicateCommand, personAfterDuplicateFailure.getBilling());
        assertEquals(dueDateBeforeDuplicateCommand, personAfterDuplicateFailure.getBilling().getCurrentDueDate());
    }

    @Test
    public void execute_backfilledPaymentDate_doesNotAdvanceDueDate() throws Exception {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Billing initialBilling = new Billing(Recurrence.MONTHLY, LocalDate.parse("2026-04-01"), 25.0,
            new PaymentHistory(LocalDate.parse("2026-03-15")));
        Person updatedPerson = new PersonBuilder(personToEdit).withBilling(initialBilling).build();
        model.setPerson(personToEdit, updatedPerson);

        AddPaymentCommand addCommand = new AddPaymentCommand(INDEX_FIRST_PERSON, LocalDate.parse("2026-03-01"));
        CommandResult commandResult = addCommand.execute(model);

        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Billing billingAfterCommand = editedPerson.getBilling();
        assertEquals(LocalDate.parse("2026-04-01"), billingAfterCommand.getCurrentDueDate());
        assertTrue(billingAfterCommand.getPaymentHistory().hasPaidOn(LocalDate.parse("2026-03-01")));
        assertEquals(buildExpectedSuccessMessage(updatedPerson, editedPerson, LocalDate.parse("2026-03-01")),
                commandResult.getFeedbackToUser());
    }

    @Test
    public void execute_laterPaymentDate_advancesDueDate() throws Exception {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Billing initialBilling = new Billing(Recurrence.MONTHLY, LocalDate.parse("2026-04-01"), 25.0,
            new PaymentHistory(LocalDate.parse("2026-03-15")));
        Person updatedPerson = new PersonBuilder(personToEdit).withBilling(initialBilling).build();
        model.setPerson(personToEdit, updatedPerson);

        AddPaymentCommand addCommand = new AddPaymentCommand(INDEX_FIRST_PERSON, LocalDate.parse("2026-03-20"));
        CommandResult commandResult = addCommand.execute(model);

        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Billing billingAfterCommand = editedPerson.getBilling();
        assertEquals(LocalDate.parse("2026-05-01"), billingAfterCommand.getCurrentDueDate());
        assertTrue(billingAfterCommand.getPaymentHistory().hasPaidOn(LocalDate.parse("2026-03-20")));
        assertEquals(buildExpectedSuccessMessage(updatedPerson, editedPerson, LocalDate.parse("2026-03-20")),
                commandResult.getFeedbackToUser());
    }

    private String buildExpectedSuccessMessage(Person originalPerson, Person editedPerson, LocalDate paymentDate) {
        String paymentFeedback = String.format(AddPaymentCommand.MESSAGE_ADD_PAYMENT_SUCCESS,
                editedPerson.getBilling().getTuitionFee(),
                Messages.format(editedPerson),
                paymentDate.format(DateTimeFormatter.ISO_LOCAL_DATE));

        LocalDate previousDueDate = originalPerson.getBilling().getCurrentDueDate();
        LocalDate updatedDueDate = editedPerson.getBilling().getCurrentDueDate();
        String dueDateFeedback;
        if (updatedDueDate.isAfter(previousDueDate)) {
            dueDateFeedback = String.format(AddPaymentCommand.MESSAGE_PAYMENT_DUE_DATE_ADVANCED,
                previousDueDate,
                updatedDueDate);
        } else {
            dueDateFeedback = String.format(AddPaymentCommand.MESSAGE_PAYMENT_DUE_DATE_UNCHANGED,
                updatedDueDate);
        }

        return paymentFeedback + " " + dueDateFeedback;
    }

    @Test
    public void equals() {
        LocalDate paymentDate = LocalDate.parse(VALID_PAYMENT_DATE);
        AddPaymentCommand standardCommand = new AddPaymentCommand(INDEX_FIRST_PERSON, paymentDate);

        // same values -> returns true
        AddPaymentCommand commandWithSameValues = new AddPaymentCommand(INDEX_FIRST_PERSON, paymentDate);
        assertTrue(standardCommand.equals(commandWithSameValues));
        assertTrue(standardCommand.equals(standardCommand));
        assertFalse(standardCommand.equals(null));
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new AddPaymentCommand(INDEX_SECOND_PERSON, paymentDate)));

        LocalDate differentPaymentDate = LocalDate.parse("2026-02-01");
        assertFalse(standardCommand.equals(new AddPaymentCommand(
                INDEX_FIRST_PERSON, differentPaymentDate)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        LocalDate paymentDate = LocalDate.parse(VALID_PAYMENT_DATE);
        AddPaymentCommand editCommand = new AddPaymentCommand(index, paymentDate);
        String expected = AddPaymentCommand.class.getCanonicalName()
                + "{index=" + index
                + ", paymentDate=" + paymentDate + "}";
        assertEquals(expected, editCommand.toString());
    }
}
