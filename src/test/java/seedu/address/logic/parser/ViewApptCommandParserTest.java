package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ViewApptCommand;

public class ViewApptCommandParserTest {
    private final ViewApptCommandParser parser = new ViewApptCommandParser();

    @Test
    public void parse_emptyArgs_returnsCommandWithCurrentDate() {
        assertParseSuccess(parser, "   ", new ViewApptCommand(LocalDate.now()));
    }

    @Test
    public void parse_validDate_success() {
        assertParseSuccess(parser, " d/2026-02-13", new ViewApptCommand(LocalDate.parse("2026-02-13")));
    }

    @Test
    public void parse_invalidDate_failure() {
        assertParseFailure(parser, " d/not-a-date", ParserUtil.MESSAGE_INVALID_DATE);
    }

    @Test
    public void parse_preamble_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewApptCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " randomText d/2026-02-13", expectedMessage);
    }
}
