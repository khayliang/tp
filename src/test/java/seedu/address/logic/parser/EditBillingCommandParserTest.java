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

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditBillingCommand;

public class EditBillingCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditBillingCommand.MESSAGE_USAGE);
    private static final String VALID_AMOUNT = "25";
    private static final String VALID_PAST_DATE = "2026-03-20";
    private static final String VALID_AMOUNT_DESC = " " + PREFIX_AMOUNT + VALID_AMOUNT;
    private static final String VALID_DATE_DESC = " " + PREFIX_DATE + VALID_PAST_DATE;
    private static final String INVALID_0_AMOUNT_DESC = " " + PREFIX_AMOUNT + "0";
    private static final String INVALID_AMOUNT_DESC = " " + PREFIX_AMOUNT + "-10";

    private final EditBillingCommandParser parser = new EditBillingCommandParser();

    @Test
    public void parse_missingParts_failure() {
        assertParseFailure(parser, "1", seedu.address.logic.commands.EditCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        assertParseFailure(parser, "-5" + VALID_AMOUNT_DESC, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "0" + VALID_AMOUNT_DESC, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_AMOUNT_DESC, ParserUtil.MESSAGE_INVALID_AMOUNT);
        assertParseFailure(parser, "1 a/notANumber", ParserUtil.MESSAGE_INVALID_AMOUNT);
        assertParseFailure(parser, "1 d/invalidDate", ParserUtil.MESSAGE_INVALID_DATE);
    }

    @Test
    public void parse_amountSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + VALID_AMOUNT_DESC;
        EditBillingCommand expectedCommand = new EditBillingCommand(targetIndex, Double.parseDouble(VALID_AMOUNT));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_amount0_failure() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + INVALID_0_AMOUNT_DESC;
        assertParseFailure(parser, userInput, ParserUtil.MESSAGE_INVALID_AMOUNT);
    }

    @Test
    public void parse_dateSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + VALID_DATE_DESC;
        EditBillingCommand expectedCommand = new EditBillingCommand(targetIndex, LocalDate.parse(VALID_PAST_DATE));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + VALID_AMOUNT_DESC + VALID_DATE_DESC;
        EditBillingCommand expectedCommand = new EditBillingCommand(targetIndex,
                Optional.of(25.0),
                Optional.of(LocalDate.parse(VALID_PAST_DATE)));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_duplicateAmountPrefix_failure() {
        assertParseFailure(parser, "1 a/20 a/30", Messages.getErrorMessageForDuplicatePrefixes(PREFIX_AMOUNT));
    }

    @Test
    public void parse_duplicateDatePrefix_failure() {
        assertParseFailure(parser, "1 d/2026-03-20 d/2026-03-21",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE));
    }
}
