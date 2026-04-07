package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.commons.util.AppClock;
import seedu.address.logic.commands.FindApptCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindPersonCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindPersonCommand() {
        // no leading and trailing whitespaces
        FindPersonCommand expectedFindCommand =
            new FindPersonCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "student Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n student \n \t Alice  \t Bob", expectedFindCommand);
    }

    @Test
    public void parse_caseInsensitiveSubcommand_success() {
        FindPersonCommand expectedFindCommand =
                new FindPersonCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "StUdEnT Alice Bob", expectedFindCommand);
    }

    @Test
    public void parse_validApptArgs_returnsFindApptCommand() {
        FindApptCommand expectedFindCommand = new FindApptCommand(LocalDate.parse("2026-02-13"));
        assertParseSuccess(parser, "appt d/2026-02-13", expectedFindCommand);
    }

    @Test
    public void parse_emptyApptArgs_returnsFindApptCommandWithCurrentDate() {
        FindApptCommand expectedFindCommand = new FindApptCommand(AppClock.today());
        assertParseSuccess(parser, "appt", expectedFindCommand);
    }

}
