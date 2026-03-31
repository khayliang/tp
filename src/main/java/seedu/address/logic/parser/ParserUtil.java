package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.recurrence.Recurrence;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_INVALID_DATE =
            "Date must be in ISO 8601 local date format, e.g. 2026-01-13";
    public static final String MESSAGE_INVALID_DATE_AFTER_TODAY =
            "Date cannot be later than today.";
    public static final String MESSAGE_INVALID_DATE_TIME =
            "Date-time must be in ISO 8601 local format, e.g. 2026-01-13T08:00:00";
    public static final String MESSAGE_INVALID_AMOUNT = "Amount must be a non-negative number.";
    public static final String MESSAGE_INVALID_RECURRENCE =
            "Recurrence must be one of: WEEKLY, BIWEEKLY, MONTHLY, NONE";
    private static final DateTimeFormatter ISO_LOCAL_DATE_FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE.withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter ISO_LOCAL_DATE_TIME_FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME.withResolverStyle(ResolverStyle.STRICT);
    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses {@code oneBasedIndex} into an {@code Index}.
     * Wraps invalid-index errors using the command's usage message.
     */
    public static Index parseIndex(String oneBasedIndex, String commandUsage) throws ParseException {
        requireNonNull(commandUsage);
        try {
            return parseIndex(oneBasedIndex);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, commandUsage), pe);
        }
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String parentName} into a {@code Name}. Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code parentName} is invalid.
     */
    public static Name parseParentName(String parentName) throws ParseException {
        return parseName(parentName);
    }

    /**
     * Parses a {@code String parentPhone} into a {@code Phone}. Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code parentPhone} is invalid.
     */
    public static Phone parseParentPhone(String parentPhone) throws ParseException {
        return parsePhone(parentPhone);
    }

    /**
     * Parses a {@code String parentEmail} into a {@code Email}. Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code parentEmail} is invalid.
     */
    public static Email parseParentEmail(String parentEmail) throws ParseException {
        return parseEmail(parentEmail);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String dateTime} into a {@code LocalDateTime}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code dateTime} is invalid.
     */
    public static LocalDateTime parseIsoDateTime(String dateTime) throws ParseException {
        requireNonNull(dateTime);
        String trimmedDateTime = dateTime.trim();
        try {
            return LocalDateTime.parse(trimmedDateTime, ISO_LOCAL_DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ParseException(MESSAGE_INVALID_DATE_TIME);
        }
    }

    /**
     * Parses a {@code String date} into a {@code LocalDate}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code date} is invalid.
     */
    public static LocalDate parseIsoDate(String date) throws ParseException {
        requireNonNull(date);
        String trimmedDate = date.trim();
        try {
            return LocalDate.parse(trimmedDate, ISO_LOCAL_DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ParseException(MESSAGE_INVALID_DATE);
        }
    }

    /**
     * Parses a {@code String amount} into a non-negative {@code double}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code amount} is invalid.
     */
    public static double parseAmount(String amount) throws ParseException {
        requireNonNull(amount);
        String trimmedAmount = amount.trim();
        try {
            double parsedAmount = Double.parseDouble(trimmedAmount);
            if (!Double.isFinite(parsedAmount) || parsedAmount < 0) {
                throw new ParseException(MESSAGE_INVALID_AMOUNT);
            }
            return parsedAmount;
        } catch (NumberFormatException e) {
            throw new ParseException(MESSAGE_INVALID_AMOUNT);
        }
    }

    /**
     * Parses a {@code String recurrence} into a {@code Recurrence}.
     */
    public static Recurrence parseRecurrence(String recurrence) throws ParseException {
        requireNonNull(recurrence);
        String trimmedRecurrence = recurrence.trim();
        try {
            return Recurrence.valueOf(trimmedRecurrence.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ParseException(MESSAGE_INVALID_RECURRENCE);
        }
    }

    /**
     * Parses a {@code String date} into a {@code LocalDate} and validates that it
     * does not occur after today in the provided {@code clock}'s local date.
     *
     * @throws ParseException if the given {@code date} is invalid or after today.
     */
    public static LocalDate parseIsoDateNotAfterToday(String date, Clock clock) throws ParseException {
        requireNonNull(clock);
        LocalDate parsedDate = parseIsoDate(date);
        LocalDate today = LocalDate.now(clock);
        if (parsedDate.isAfter(today)) {
            throw new ParseException(MESSAGE_INVALID_DATE_AFTER_TODAY);
        }
        return parsedDate;
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }
}
