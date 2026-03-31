package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ATTENDANCE_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ATTENDANCE_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ATTENDANCE_DATE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddAttdCommand;

public class AddAttdCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAttdCommand.MESSAGE_USAGE);

    private final AddAttdCommandParser parser = new AddAttdCommandParser();

    @Test
    public void parse_missingParts_failure() {
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "y", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        assertParseFailure(parser, "-1 y", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "0 y", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1 maybe", AddAttdCommandParser.MESSAGE_INVALID_ATTENDANCE_STATUS);
        assertParseFailure(parser, "1 y extra", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidDate_failure() {
        assertParseFailure(parser, "1 y" + INVALID_ATTENDANCE_DATE_DESC, ParserUtil.MESSAGE_INVALID_DATE);
    }

    @Test
    public void parse_dateWithAbsence_failure() {
        assertParseFailure(parser, "1 n" + ATTENDANCE_DATE_DESC,
                AddAttdCommandParser.MESSAGE_DATE_NOT_ALLOWED_FOR_ABSENCE);
    }

    @Test
    public void parse_presentWithoutDate_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        assertParseSuccess(parser, "1 y", new AddAttdCommand(targetIndex, true, Optional.empty()));
    }

    @Test
    public void parse_defaultPresentWithoutDate_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        assertParseSuccess(parser, "1", new AddAttdCommand(targetIndex, true, Optional.empty()));
    }

    @Test
    public void parse_defaultPresentWithDate_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        assertParseSuccess(parser, "1" + ATTENDANCE_DATE_DESC,
                new AddAttdCommand(targetIndex, true, Optional.of(LocalDate.parse(VALID_ATTENDANCE_DATE))));
    }

    @Test
    public void parse_presentWithDate_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        assertParseSuccess(parser, "1 y" + ATTENDANCE_DATE_DESC,
                new AddAttdCommand(targetIndex, true, Optional.of(LocalDate.parse(VALID_ATTENDANCE_DATE))));
    }

    @Test
    public void parse_absentWithoutDate_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        assertParseSuccess(parser, "1 n", new AddAttdCommand(targetIndex, false, Optional.empty()));
    }
}
