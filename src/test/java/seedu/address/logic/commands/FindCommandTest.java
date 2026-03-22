package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FindCommandTest {

    @Test
    public void verifyConstants() {
        assertEquals("find", FindCommand.COMMAND_WORD);
        assertEquals(FindCommand.COMMAND_WORD + ": Finds entries by subcommand.\n"
                + "Parameters: SUBCOMMAND [ARGS]\n"
                + "Example: " + FindCommand.COMMAND_WORD + " person alice", FindCommand.MESSAGE_USAGE);
    }
}
