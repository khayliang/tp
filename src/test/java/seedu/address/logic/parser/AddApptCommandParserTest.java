package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.APPOINTMENT_DESCRIPTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.APPOINTMENT_RECURRENCE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.APPOINTMENT_START_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_APPOINTMENT_DESCRIPTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_APPOINTMENT_RECURRENCE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_APPOINTMENT_START_DESC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_DESCRIPTION;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_RECURRENCE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_START;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddApptCommand;
import seedu.address.model.recurrence.Recurrence;

public class AddApptCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddApptCommand.MESSAGE_USAGE);

    private final AddApptCommandParser parser = new AddApptCommandParser();

    @Test
    public void parse_missingRequiredPrefixes_failure() {
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1" + APPOINTMENT_START_DESC, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1" + APPOINTMENT_DESCRIPTION_DESC, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        assertParseFailure(parser, "-5" + APPOINTMENT_START_DESC + APPOINTMENT_DESCRIPTION_DESC,
                MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "0" + APPOINTMENT_START_DESC + APPOINTMENT_DESCRIPTION_DESC,
                MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1 random" + APPOINTMENT_START_DESC + APPOINTMENT_DESCRIPTION_DESC,
                MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_APPOINTMENT_START_DESC + APPOINTMENT_DESCRIPTION_DESC,
                ParserUtil.MESSAGE_INVALID_DATE_TIME);
        assertParseFailure(parser, "1" + APPOINTMENT_START_DESC + INVALID_APPOINTMENT_RECURRENCE_DESC
                        + APPOINTMENT_DESCRIPTION_DESC,
                ParserUtil.MESSAGE_INVALID_RECURRENCE);
        assertParseFailure(parser, "1" + APPOINTMENT_START_DESC + INVALID_APPOINTMENT_DESCRIPTION_DESC,
                MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + APPOINTMENT_START_DESC
                + APPOINTMENT_RECURRENCE_DESC + APPOINTMENT_DESCRIPTION_DESC;
        AddApptCommand expectedCommand = new AddApptCommand(targetIndex,
                LocalDateTime.parse(VALID_APPOINTMENT_START),
                Recurrence.valueOf(VALID_APPOINTMENT_RECURRENCE),
                VALID_APPOINTMENT_DESCRIPTION);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_optionalRecurrence_defaultsToNone() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + APPOINTMENT_START_DESC + APPOINTMENT_DESCRIPTION_DESC;
        AddApptCommand expectedCommand = new AddApptCommand(targetIndex,
                LocalDateTime.parse(VALID_APPOINTMENT_START),
                Recurrence.NONE,
                VALID_APPOINTMENT_DESCRIPTION);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
