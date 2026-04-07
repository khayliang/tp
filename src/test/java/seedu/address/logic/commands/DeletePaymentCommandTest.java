package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeletePaymentCommand}.
 */
public class DeletePaymentCommandTest {

    private static final LocalDate PAYMENT_DATE_TO_DELETE = LocalDate.of(2026, 4, 1);
    private static final LocalDate OTHER_PAYMENT_DATE = LocalDate.of(2026, 3, 1);

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexAndDate_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personWithPaymentHistory = withPaymentHistory(personToEdit, OTHER_PAYMENT_DATE, PAYMENT_DATE_TO_DELETE);
        model.setPerson(personToEdit, personWithPaymentHistory);

        DeletePaymentCommand deleteCommand = new DeletePaymentCommand(INDEX_FIRST_PERSON, PAYMENT_DATE_TO_DELETE);

        Billing updatedBilling = personWithPaymentHistory.getBilling().deleteRecordedPayment(PAYMENT_DATE_TO_DELETE);
        Person editedPerson = new PersonBuilder(personWithPaymentHistory)
                .withBilling(updatedBilling)
                .build();

        String formattedDate = PAYMENT_DATE_TO_DELETE.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String deleteFeedback = String.format(DeletePaymentCommand.MESSAGE_DELETE_PAYMENT_SUCCESS,
                formattedDate, Messages.format(editedPerson));
        String dueDateFeedback;
        if (updatedBilling.getCurrentDueDate().isBefore(personWithPaymentHistory.getBilling().getCurrentDueDate())) {
            dueDateFeedback = String.format(DeletePaymentCommand.MESSAGE_PAYMENT_DUE_DATE_ROLLED_BACK,
                    personWithPaymentHistory.getBilling().getCurrentDueDate(),
                    updatedBilling.getCurrentDueDate());
        } else {
            dueDateFeedback = String.format(DeletePaymentCommand.MESSAGE_PAYMENT_DUE_DATE_UNCHANGED,
                    updatedBilling.getCurrentDueDate());
        }
        String expectedMessage = deleteFeedback + " " + dueDateFeedback;

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personWithPaymentHistory, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(deleteCommand, model, expectedCommandResult, expectedModel);

        assertFalse(editedPerson.getPaymentHistory().hasPaidOn(PAYMENT_DATE_TO_DELETE));
        assertTrue(editedPerson.getPaymentHistory().hasPaidOn(OTHER_PAYMENT_DATE));
    }

    @Test
    public void execute_paymentDateMissing_failure() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personWithPaymentHistory = withPaymentHistory(personToEdit, OTHER_PAYMENT_DATE);
        model.setPerson(personToEdit, personWithPaymentHistory);

        DeletePaymentCommand deleteCommand = new DeletePaymentCommand(INDEX_FIRST_PERSON, PAYMENT_DATE_TO_DELETE);
        String expectedMessage = String.format(DeletePaymentCommand.MESSAGE_PAYMENT_DATE_NOT_FOUND,
                PAYMENT_DATE_TO_DELETE.format(
                        DateTimeFormatter.ISO_LOCAL_DATE),
                        Messages.format(personWithPaymentHistory));

        assertCommandFailure(deleteCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidIndex_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeletePaymentCommand deleteCommand = new DeletePaymentCommand(outOfBoundIndex, PAYMENT_DATE_TO_DELETE);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeletePaymentCommand command = new DeletePaymentCommand(INDEX_FIRST_PERSON, PAYMENT_DATE_TO_DELETE);

        assertTrue(command.equals(command));
        assertTrue(command.equals(new DeletePaymentCommand(INDEX_FIRST_PERSON, PAYMENT_DATE_TO_DELETE)));
        assertFalse(command.equals((Object) null));
        assertFalse(command.equals((Object) new ClearCommand()));
        assertFalse(command.equals(new DeletePaymentCommand(INDEX_SECOND_PERSON, PAYMENT_DATE_TO_DELETE)));
        assertFalse(command.equals(new DeletePaymentCommand(INDEX_FIRST_PERSON, OTHER_PAYMENT_DATE)));
    }

    @Test
    public void toStringMethod() {
        DeletePaymentCommand deleteCommand = new DeletePaymentCommand(INDEX_FIRST_PERSON, PAYMENT_DATE_TO_DELETE);
        String expected = DeletePaymentCommand.class.getCanonicalName()
                + "{index=" + INDEX_FIRST_PERSON
                + ", paymentDate=" + PAYMENT_DATE_TO_DELETE
                + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    private Person withPaymentHistory(Person base, LocalDate... paidDates) {
        Billing updatedBilling = new Billing(
                base.getBilling().getRecurrence(),
                base.getBilling().getCurrentDueDate(),
                base.getBilling().getTuitionFee(),
                new PaymentHistory(paidDates));
        return new PersonBuilder(base)
                .withBilling(updatedBilling)
                .build();
    }
}
