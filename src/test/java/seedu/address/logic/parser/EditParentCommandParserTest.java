package seedu.address.logic.parser;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT_PHONE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditParentCommand;
import seedu.address.logic.commands.EditParentCommand.EditParentDescriptor;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class EditParentCommandParserTest {
    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditParentCommand.MESSAGE_USAGE);
    private static final String VALID_NAME = "John Doe";
    private static final String VALID_PHONE = "98765432";
    private static final String VALID_EMAIL = "johndoe@example.com";

    private EditParentCommandParser parser = new EditParentCommandParser();

    @Test
    public void parse_missingParts_failure() {
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        assertParseFailure(parser, "-5" + " " + PREFIX_PARENT_NAME + VALID_NAME, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "0" + " " + PREFIX_PARENT_NAME + VALID_NAME, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_PARENT_NAME + VALID_NAME
                + " " + PREFIX_PARENT_PHONE + VALID_PHONE + " " + PREFIX_PARENT_EMAIL + VALID_EMAIL;

        Optional<Name> expectedName = Optional.of(new Name(VALID_NAME));
        Optional<Phone> expectedPhone = Optional.of(new Phone(VALID_PHONE));
        Optional<Email> expectedEmail = Optional.of(new Email(VALID_EMAIL));

        EditParentDescriptor expectedDescriptor = new EditParentDescriptor();
        expectedName.ifPresent(expectedDescriptor::setParentName);
        expectedPhone.ifPresent(expectedDescriptor::setParentPhone);
        expectedEmail.ifPresent(expectedDescriptor::setParentEmail);

        EditParentCommand expectedCommand = new EditParentCommand(targetIndex, expectedDescriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_shortPrefixes_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " n/" + VALID_NAME
                + " p/" + VALID_PHONE + " e/" + VALID_EMAIL;

        Optional<Name> expectedName = Optional.of(new Name(VALID_NAME));
        Optional<Phone> expectedPhone = Optional.of(new Phone(VALID_PHONE));
        Optional<Email> expectedEmail = Optional.of(new Email(VALID_EMAIL));

        EditParentDescriptor expectedDescriptor = new EditParentDescriptor();
        expectedName.ifPresent(expectedDescriptor::setParentName);
        expectedPhone.ifPresent(expectedDescriptor::setParentPhone);
        expectedEmail.ifPresent(expectedDescriptor::setParentEmail);

        EditParentCommand expectedCommand = new EditParentCommand(targetIndex, expectedDescriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
