package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class CommandHistoryTest {

    private final CommandHistory commandHistory = new CommandHistory();

    @Test
    public void getPrevious_noRecordedCommands_returnsEmpty() {
        assertFalse(commandHistory.getPrevious("").isPresent());
    }

    @Test
    public void getPrevious_commandsRecorded_returnsCommandsInReverseOrder() {
        commandHistory.record("list");
        commandHistory.record("find Alex");
        commandHistory.record("delete 2");

        assertEquals("delete 2", commandHistory.getPrevious("").orElseThrow());
        assertEquals("find Alex", commandHistory.getPrevious("").orElseThrow());
        assertEquals("list", commandHistory.getPrevious("").orElseThrow());
        assertEquals("list", commandHistory.getPrevious("").orElseThrow());
    }

    @Test
    public void getNext_afterNavigatingPrevious_restoresDraftCommand() {
        commandHistory.record("list");
        commandHistory.record("find Alex");

        assertEquals("find Alex", commandHistory.getPrevious("edit 1 n/Alex Tan").orElseThrow());
        assertEquals("edit 1 n/Alex Tan", commandHistory.getNext().orElseThrow());
        assertFalse(commandHistory.getNext().isPresent());
    }

    @Test
    public void record_afterNavigating_resetsNavigationToLatestCommand() {
        commandHistory.record("list");
        commandHistory.record("find Alex");
        commandHistory.getPrevious("");

        commandHistory.record("clear");

        assertEquals("clear", commandHistory.getPrevious("").orElseThrow());
    }
}
