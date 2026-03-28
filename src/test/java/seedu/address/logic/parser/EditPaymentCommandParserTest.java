package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditPaymentCommand;

public class EditPaymentCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditPaymentCommand.MESSAGE_USAGE);
    private static final String VALID_DATE = "2026-01-13";
    private static final String TODAY_DATE = "2026-03-28";
    private static final String FUTURE_DATE = "2026-03-29";
    private static final String VALID_DATE_DESC = " " + PREFIX_DATE + VALID_DATE;
    private static final String TODAY_DATE_DESC = " " + PREFIX_DATE + TODAY_DATE;
    private static final String FUTURE_DATE_DESC = " " + PREFIX_DATE + FUTURE_DATE;
    private static final String INVALID_DATE_DESC = " " + PREFIX_DATE + "2026-13-40";
    private static final Clock FIXED_CLOCK = Clock.fixed(
            Instant.parse("2026-03-28T12:00:00Z"),
            ZoneId.of("Asia/Singapore"));

    private final EditPaymentCommandParser parser = new EditPaymentCommandParser(FIXED_CLOCK);

    @Test
    public void parse_missingParts_failure() {
        assertParseFailure(parser, "1", seedu.address.logic.commands.EditCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        assertParseFailure(parser, "-5" + VALID_DATE_DESC, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "0" + VALID_DATE_DESC, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_DATE_DESC, ParserUtil.MESSAGE_INVALID_DATE);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + VALID_DATE_DESC;
        EditPaymentCommand expectedCommand = new EditPaymentCommand(targetIndex,
            LocalDate.parse(VALID_DATE), java.util.Optional.empty());
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_paymentDateIsToday_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + TODAY_DATE_DESC;
        EditPaymentCommand expectedCommand = new EditPaymentCommand(targetIndex,
                LocalDate.parse(TODAY_DATE), java.util.Optional.empty());
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_futurePaymentDate_failure() {
        assertParseFailure(parser, "1" + FUTURE_DATE_DESC, ParserUtil.MESSAGE_DATE_AFTER_TODAY);
    }
}
