package seedu.address.model.util;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Utility class for common string operations.
 */
public class StringUtil {

    /**
     * Converts the given input string to title case.
     * <p>
     * Each word will have its first character capitalized and the remaining characters in lowercase.
     * Words are assumed to be separated by one or more whitespace characters.
     * Leading and trailing whitespace will be removed, and multiple spaces will be reduced to a single space.
     *
     * @param input The input string to be converted.
     * @return The input string in title case, with normalized spacing.
     * @throws NullPointerException if {@code input} is null.
     */
    public static String toTitleCase(String input) {
        String trimmed = input.trim();

        if (trimmed.isEmpty()) {
            return trimmed;
        }

        String[] words = trimmed.split("\\s+");

        return Arrays.stream(words)
                .filter(word -> !word.isEmpty())
                .map(word -> word.substring(0, 1).toUpperCase()
                        + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
