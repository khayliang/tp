package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.APPOINTMENT_DESCRIPTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.APPOINTMENT_START_DESC;
import static seedu.address.logic.commands.CommandTestUtil.ATTENDANCE_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PAYMENT_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddApptCommand;
import seedu.address.logic.commands.AddAttdCommand;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddPaymentCommand;
import seedu.address.logic.commands.AddPersonCommand;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_missingParts_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, NAME_DESC_AMY, expectedMessage);
    }

    @Test
    public void parse_invalidSubcommand_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "unknown" + NAME_DESC_AMY, expectedMessage);
    }

    @Test
    public void parse_validStudentSubcommand_delegatesToPersonParser() throws Exception {
        AddPersonCommandParser addPersonParser = new AddPersonCommandParser();
        String personDetails = NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        String userInput = AddPersonCommand.SUB_COMMAND_WORD + personDetails;

        assertParseSuccess(parser, userInput, addPersonParser.parse(personDetails));
    }

    @Test
    public void parse_caseInsensitiveSubcommand_success() throws Exception {
        AddPersonCommandParser addPersonParser = new AddPersonCommandParser();
        String personDetails = NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
        assertParseSuccess(parser, "StUdEnT" + personDetails, addPersonParser.parse(personDetails));
    }

    @Test
    public void parse_validPaymentSubcommand_delegatesToPaymentParser() throws Exception {
        AddPaymentCommandParser addPaymentParser = new AddPaymentCommandParser();
        String paymentDetails = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_DATE + VALID_PAYMENT_DATE;
        String userInput = AddPaymentCommand.SUB_COMMAND_WORD + " " + paymentDetails;

        assertParseSuccess(parser, userInput, addPaymentParser.parse(paymentDetails));
    }

    @Test
    public void parse_validAppointmentSubcommand_delegatesToAppointmentParser() throws Exception {
        AddApptCommandParser addApptParser = new AddApptCommandParser();
        String appointmentDetails = INDEX_FIRST_PERSON.getOneBased()
                + APPOINTMENT_START_DESC + APPOINTMENT_DESCRIPTION_DESC;
        String userInput = AddApptCommand.SUB_COMMAND_WORD + " " + appointmentDetails;

        assertParseSuccess(parser, userInput, addApptParser.parse(appointmentDetails));
    }

    @Test
    public void parse_validAttendanceSubcommand_delegatesToAttendanceParser() throws Exception {
        AddAttdCommandParser addAttdParser = new AddAttdCommandParser();
        String attendanceDetails = INDEX_FIRST_PERSON.getOneBased() + " y" + ATTENDANCE_DATE_DESC;
        String userInput = AddAttdCommand.SUB_COMMAND_WORD + " " + attendanceDetails;

        assertParseSuccess(parser, userInput, addAttdParser.parse(attendanceDetails));
    }
}
