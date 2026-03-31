package seedu.address.logic.parser;

import java.util.HashMap;
import java.util.Map;

import seedu.address.logic.commands.EditAcademicsCommand;
import seedu.address.logic.commands.EditBillingCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditParentCommand;
import seedu.address.logic.commands.EditPersonCommand;
import seedu.address.logic.commands.EditTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    private final SubcommandDispatcherParser<EditCommand> dispatcher;

    /**
     * Creates an EditCommandParser and registers available sub-command parsers.
     */
    public EditCommandParser() {
        Map<String, Parser<? extends EditCommand>> parsers = new HashMap<>();
        parsers.put(EditPersonCommand.SUB_COMMAND_WORD, new EditPersonCommandParser());
        parsers.put(EditTagCommand.SUB_COMMAND_WORD, new EditTagCommandParser());
        parsers.put(EditAcademicsCommand.SUB_COMMAND_WORD, new EditAcademicsCommandParser());
        parsers.put(EditParentCommand.SUB_COMMAND_WORD, new EditParentCommandParser());
<<<<<<< HEAD
        parsers.put(EditApptCommand.SUB_COMMAND_WORD, new EditApptCommandParser());
=======
        parsers.put(EditPaymentCommand.SUB_COMMAND_WORD, new EditPaymentCommandParser());
>>>>>>> master
        parsers.put(EditBillingCommand.SUB_COMMAND_WORD, new EditBillingCommandParser());
        this.dispatcher = new SubcommandDispatcherParser<>(parsers, EditCommand.MESSAGE_USAGE);
    }

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public EditCommand parse(String args) throws ParseException {
        return dispatcher.parse(args);
    }
}
