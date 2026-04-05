package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Stores previously entered commands and supports bash-style navigation.
 */
class CommandHistory {

    private final List<String> history = new ArrayList<>();
    private int historyIndex;
    private String draftCommand = "";

    /**
     * Records a command and resets navigation to the newest position.
     */
    void record(String command) {
        requireNonNull(command);
        history.add(command);
        historyIndex = history.size();
        draftCommand = "";
    }

    /**
     * Returns the previous command if one exists.
     */
    Optional<String> getPrevious(String currentInput) {
        requireNonNull(currentInput);

        if (history.isEmpty()) {
            return Optional.empty();
        }

        if (historyIndex == history.size()) {
            draftCommand = currentInput;
        }

        if (historyIndex > 0) {
            historyIndex--;
        }

        return Optional.of(history.get(historyIndex));
    }

    /**
     * Returns the next command or the preserved draft if one exists.
     */
    Optional<String> getNext() {
        if (history.isEmpty() || historyIndex == history.size()) {
            return Optional.empty();
        }

        historyIndex++;

        if (historyIndex == history.size()) {
            return Optional.of(draftCommand);
        }

        return Optional.of(history.get(historyIndex));
    }
}
