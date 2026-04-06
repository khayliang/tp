package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.YearMonth;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.FindBillingCommand;
import seedu.address.model.billing.PaymentDueMonthPredicate;

public class FindBillingCommandParserTest {

    private FindBillingCommandParser parser = new FindBillingCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindBillingCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noPrefix_throwsParseException() {
        assertParseFailure(parser, "2026-03",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindBillingCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_prefixOnly_throwsParseException() {
        assertParseFailure(parser, "d/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindBillingCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validMonth_success() {
        FindBillingCommand expected = new FindBillingCommand(
                new PaymentDueMonthPredicate(YearMonth.of(2026, 3)));

        assertParseSuccess(parser, "d/2026-03", expected);
    }

    @Test
    public void parse_invalidMonth_throwsParseException() {
        // invalid format
        assertParseFailure(parser, "d/2026-13",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindBillingCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_preamblePresent_throwsParseException() {
        assertParseFailure(parser, "random d/2026-03",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindBillingCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicateDatePrefix_throwsParseException() {
        assertParseFailure(parser, "d/2026-03 d/2026-04",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE));
    }
}
