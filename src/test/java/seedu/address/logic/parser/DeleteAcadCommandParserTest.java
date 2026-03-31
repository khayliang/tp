package seedu.address.logic.parser;

import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteAcadCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class DeleteAcadCommandParserTest {

    private DeleteAcadCommandParser parser = new DeleteAcadCommandParser();

    @Test
    public void parse_validArgs_success() throws Exception {
        DeleteAcadCommand expected =
                new DeleteAcadCommand(Index.fromOneBased(1),
                        List.of(Index.fromOneBased(1), Index.fromOneBased(2)));

        DeleteAcadCommand result = parser.parse("1 s/1 s/2");

        // compare using equals
        org.junit.jupiter.api.Assertions.assertEquals(expected, result);
    }

    @Test
    public void parse_singleSubject_success() throws Exception {
        DeleteAcadCommand expected =
                new DeleteAcadCommand(Index.fromOneBased(2),
                        List.of(Index.fromOneBased(3)));

        DeleteAcadCommand result = parser.parse("2 s/3");

        org.junit.jupiter.api.Assertions.assertEquals(expected, result);
    }

    @Test
    public void parse_missingPersonIndex_throwsParseException() {
        assertThrows(ParseException.class, () ->
                parser.parse("s/1"));
    }

    @Test
    public void parse_missingSubjectPrefix_throwsParseException() {
        assertThrows(ParseException.class, () ->
                parser.parse("1"));
    }

    @Test
    public void parse_invalidPersonIndex_throwsParseException() {
        assertThrows(ParseException.class, () ->
                parser.parse("a s/1"));
    }

    @Test
    public void parse_invalidSubjectIndex_throwsParseException() {
        assertThrows(ParseException.class, () ->
                parser.parse("1 s/a"));
    }

    @Test
    public void parse_noArguments_throwsParseException() {
        assertThrows(ParseException.class, () ->
                parser.parse(""));
    }

    @Test
    public void parse_multipleInvalidSubjectIndices_throwsParseException() {
        assertThrows(ParseException.class, () ->
                parser.parse("1 s/1 s/a s/3"));
    }
}
