package seedu.address.logic.parser;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;

public class SubcommandDispatcherParserTest {
    private static class DummyCommand extends Command {
        public final String args;

        public DummyCommand(String args) {
            this.args = args;
        }

        @Override
        public CommandResult execute(Model model) {
            return new CommandResult("Dummy " + args);
        }

        @Override
        public boolean equals(Object other) {
            return other == this || (other instanceof DummyCommand
                   && args.equals(((DummyCommand) other).args));
        }
    }

    private SubcommandDispatcherParser<DummyCommand> parser = new SubcommandDispatcherParser<>(
            Map.of("sub", args -> new DummyCommand(args)),
            "Usage: base subcommand"
    );

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, "Usage: base subcommand"));
    }

    @Test
    public void parse_invalidSubcommand_throwsParseException() {
        assertParseFailure(parser, "unknown args",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, "Usage: base subcommand"));
    }

    @Test
    public void parse_validSubcommand_success() {
        assertParseSuccess(parser, "sub my args", new DummyCommand("my args"));
        assertParseSuccess(parser, "sub", new DummyCommand(""));
    }
}
