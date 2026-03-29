package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
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
}
