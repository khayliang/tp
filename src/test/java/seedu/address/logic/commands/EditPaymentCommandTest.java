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
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditApptCommand.
 */
public class EditPaymentCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        LocalDate paymentDate = LocalDate.parse(VALID_PAYMENT_DATE);
        EditPaymentCommand editCommand = new EditPaymentCommand(INDEX_FIRST_PERSON, paymentDate);

        PaymentHistory updatedPayment = personToEdit.recordPaymentDate(paymentDate);
        Billing updatedBilling = personToEdit.advancePaymentDueDate();

        Person editedPerson = new PersonBuilder(personToEdit)
                .withPayment(updatedPayment)
                .withBilling(updatedBilling)
                .build();

        String expectedMessage = String.format(EditPaymentCommand.MESSAGE_EDIT_PAYMENT_SUCCESS,
                Messages.format(editedPerson), paymentDate.format(DateTimeFormatter.ISO_LOCAL_DATE));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(editCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        LocalDate paymentDate = LocalDate.parse(VALID_PAYMENT_DATE);
        EditPaymentCommand editCommand = new EditPaymentCommand(outOfBoundIndex, paymentDate);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        LocalDate paymentDate = LocalDate.parse(VALID_PAYMENT_DATE);
        EditPaymentCommand standardCommand = new EditPaymentCommand(INDEX_FIRST_PERSON, paymentDate);

        // same values -> returns true
        EditPaymentCommand commandWithSameValues = new EditPaymentCommand(INDEX_FIRST_PERSON, paymentDate);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different type -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditPaymentCommand(INDEX_SECOND_PERSON, paymentDate)));

        // different appointment start -> returns false
        LocalDate differentPaymentDate = LocalDate.parse("2026-02-01");
        assertFalse(standardCommand.equals(new EditPaymentCommand(INDEX_FIRST_PERSON, differentPaymentDate)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        LocalDate paymentDate = LocalDate.parse(VALID_PAYMENT_DATE);
        EditPaymentCommand editCommand = new EditPaymentCommand(index, paymentDate);
        String expected = EditPaymentCommand.class.getCanonicalName()
                + "{index=" + index + ", paymentDate=" + paymentDate + "}";
        assertEquals(expected, editCommand.toString());
    }
}
