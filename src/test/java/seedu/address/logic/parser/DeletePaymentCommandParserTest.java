package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeletePaymentCommand;

public class DeletePaymentCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePaymentCommand.MESSAGE_USAGE);
    private static final String VALID_DATE = "2026-01-13";
    private static final String VALID_DATE_DESC = " " + PREFIX_DATE + VALID_DATE;
    private static final String INVALID_DATE_DESC = " " + PREFIX_DATE + "2026-13-40";

    private final DeletePaymentCommandParser parser = new DeletePaymentCommandParser();

    @Test
    public void parse_missingDate_failure() {
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        assertParseFailure(parser, "-1" + VALID_DATE_DESC, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "0" + VALID_DATE_DESC, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1 abc" + VALID_DATE_DESC, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidDate_failure() {
        assertParseFailure(parser, "1" + INVALID_DATE_DESC, ParserUtil.MESSAGE_INVALID_DATE);
    }

    @Test
    public void parse_futureDate_failure() {
        String futureDate = LocalDate.now().plusDays(1).toString();
        assertParseFailure(parser, "1 " + PREFIX_DATE + futureDate, ParserUtil.MESSAGE_DATE_AFTER_TODAY);
    }

    @Test
    public void parse_validArgs_success() {
        DeletePaymentCommand expectedCommand =
                new DeletePaymentCommand(INDEX_FIRST_PERSON, LocalDate.parse(VALID_DATE));
        assertParseSuccess(parser, INDEX_FIRST_PERSON.getOneBased() + VALID_DATE_DESC, expectedCommand);
    }
}
