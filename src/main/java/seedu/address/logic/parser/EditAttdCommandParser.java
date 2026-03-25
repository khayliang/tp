package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;

import java.time.LocalDateTime;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.DateTimeUtil;
import seedu.address.logic.commands.EditAttdCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code EditAttdCommand} object.
 */
public class EditAttdCommandParser implements Parser<EditAttdCommand> {

    @Override
    public EditAttdCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_DATE);

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble(), EditAttdCommand.MESSAGE_USAGE);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_DATE);

        Optional<String> attendanceDateTimeInput = argMultimap.getValue(PREFIX_DATE);
        LocalDateTime attendanceDateTime;
        if (attendanceDateTimeInput.isPresent()) {
            attendanceDateTime = ParserUtil.parseIsoDateTime(attendanceDateTimeInput.get());
        } else {
            attendanceDateTime = DateTimeUtil.normalizeToMinute(LocalDateTime.now());
        }

        return new EditAttdCommand(index, attendanceDateTime);
    }
}
