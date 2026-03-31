package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class DeleteTagCommandParserTest {

    private DeleteTagCommandParser parser = new DeleteTagCommandParser();

    @Test
    public void parse_validArgs_success() throws Exception {
        DeleteTagCommand expected = new DeleteTagCommand(
                Index.fromOneBased(1),
                List.of(Index.fromOneBased(2), Index.fromOneBased(3)));

        DeleteTagCommand result = parser.parse("1 t/2 t/3");

        assertEquals(expected, result);
    }

    @Test
    public void parse_singleTag_success() throws Exception {
        DeleteTagCommand expected = new DeleteTagCommand(
                Index.fromOneBased(1),
                List.of(Index.fromOneBased(2)));

        DeleteTagCommand result = parser.parse("1 t/2");

        assertEquals(expected, result);
    }

    @Test
    public void parse_missingTag_throwsParseException() {
        assertThrows(ParseException.class, () ->
                parser.parse("1"));
    }

    @Test
    public void parse_invalidPersonIndex_throwsParseException() {
        assertThrows(ParseException.class, () ->
                parser.parse("a t/2"));
    }

    @Test
    public void parse_invalidTagIndex_throwsParseException() {
        assertThrows(ParseException.class, () ->
                parser.parse("1 t/a"));
    }

    @Test
    public void parse_emptyInput_throwsParseException() {
        assertThrows(ParseException.class, () ->
                parser.parse(""));
    }
}
