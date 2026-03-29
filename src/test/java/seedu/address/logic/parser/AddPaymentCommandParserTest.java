package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddPaymentCommand;

public class AddPaymentCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPaymentCommand.MESSAGE_USAGE);
    private static final String VALID_DATE = "2026-01-13";
    private static final String VALID_DATE_DESC = " " + PREFIX_DATE + VALID_DATE;
    private static final String VALID_AMOUNT_DESC = " " + PREFIX_AMOUNT + "25";

    private final AddPaymentCommandParser parser = new AddPaymentCommandParser();

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
        assertParseFailure(parser, "1 " + PREFIX_DATE + "2026-13-40", ParserUtil.MESSAGE_INVALID_DATE);
    }

    @Test
    public void parse_invalidAmount_failure() {
        assertParseFailure(parser, "1" + VALID_DATE_DESC + " " + PREFIX_AMOUNT + "abc", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_validArgsWithAmount_success() {
        AddPaymentCommand expectedCommand = new AddPaymentCommand(
                INDEX_FIRST_PERSON, LocalDate.parse(VALID_DATE), Optional.of(25.0));
        assertParseSuccess(parser, INDEX_FIRST_PERSON.getOneBased() + VALID_DATE_DESC + VALID_AMOUNT_DESC,
                expectedCommand);
    }

    @Test
    public void parse_validArgsWithoutAmount_success() {
        AddPaymentCommand expectedCommand = new AddPaymentCommand(
                INDEX_FIRST_PERSON, LocalDate.parse(VALID_DATE), Optional.empty());
        assertParseSuccess(parser, INDEX_FIRST_PERSON.getOneBased() + VALID_DATE_DESC, expectedCommand);
    }
}
