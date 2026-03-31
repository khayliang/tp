package seedu.address.logic.commands;

import java.util.Arrays;

import seedu.address.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";
    public static final String HELP_GUIDE = String.join("\n\n",
            "TutorFlow Command Guide",
            section("General Commands",
                    entry("help",
                            "Shows this in-app command reference.",
                            "help",
                            "help"),
                    entry("list",
                            "Shows all students in the address book.",
                            "list",
                            "list"),
                    entry("clear",
                            "Clears all data in the address book.",
                            "clear",
                            "clear"),
                    entry("exit",
                            "Exits the application.",
                            "exit",
                            "exit")),
            section("Add Commands",
                    entry("add",
                            "Command family for adding data via subcommands.",
                            "add SUBCOMMAND PARAMETERS",
                            "add student n/John Doe p/98765432 e/johnd@example.com a/311, Clementi Ave 2, #02-25",
                            "add appt 1 d/2026-01-13T08:00:00 dsc/Weekly algebra practice",
                            "add attd 1 y d/2026-01-29"),
                    entry("add student",
                            "Adds a student to the address book.",
                            "add student n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]...",
                            "add student n/John Doe p/98765432 e/johnd@example.com "
                                    + "a/311, Clementi Ave 2, #02-25 t/friends"),
                    entry("add appt",
                            "Adds an appointment for the selected student.",
                            "add appt INDEX d/DATETIME [r/RECURRENCE] dsc/DESCRIPTION",
                            "add appt 1 d/2026-01-13T08:00:00 dsc/Weekly algebra practice"),
                    entry("add attd",
                            "Records attendance for the selected student's current appointment.",
                            "add attd INDEX [y|n] [d/DATE]",
                            "add attd 1 y d/2026-01-29",
                            "add attd 1 n")),
            section("Edit Commands",
                    entry("edit",
                            "Command family for editing data via subcommands.",
                            "edit SUBCOMMAND PARAMETERS",
                            "edit student 1 n/John Doe"),
                    entry("edit student",
                            "Edits a student's basic details.",
                            "edit student INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS]",
                            "edit student 1 p/91234567 e/johndoe@example.com"),
                    entry("edit tag",
                            "Replaces a student's tags.",
                            "edit tag INDEX [t/TAG]...",
                            "edit tag 1 t/JC t/J1"),
                    entry("edit acad",
                            "Replaces a student's academic subjects and levels.",
                            "edit acad INDEX [s/SUBJECT [l/LEVEL]]...",
                            "edit acad 1 s/Math l/Strong s/Science"),
                    entry("edit parent",
                            "Edits a student's parent details.",
                            "edit parent INDEX [n/PARENT_NAME] [p/PARENT_PHONE] [e/PARENT_EMAIL]",
                            "edit parent 3 n/John Lim p/91234567 e/johnlim@example.com"),
                    entry("edit payment",
                            "Records a tuition payment date for the selected student.",
                            "edit payment INDEX d/DATE",
                            "edit payment 1 d/2026-01-13"),
                    entry("edit billing",
                            "Updates the selected student's tuition fee.",
                            "edit billing INDEX a/AMOUNT",
                            "edit billing 1 a/50")),
            section("Delete Commands",
                    entry("delete",
                            "Command family for deleting data via subcommands.",
                            "delete SUBCOMMAND INDEX",
                            "delete student 1"),
                    entry("delete student",
                            "Deletes the selected student.",
                            "delete student INDEX",
                            "delete student 1")),
            section("Find Commands",
                    entry("find",
                            "Command family for searching via subcommands.",
                            "find SUBCOMMAND PARAMETERS",
                            "find student alice"),
                    entry("find student",
                            "Finds displayed students whose names match any keyword.",
                            "find student KEYWORD [MORE_KEYWORDS]...",
                            "find student alice bob charlie"),
                    entry("find tag",
                            "Finds persons whose tags partially match any given tag keyword.",
                            "find tag t/TAG [t/MORE_TAGS]...",
                            "find tag t/JC t/Sec1"),
                    entry("find payment",
                            "Finds displayed students whose payment due date falls within a given month.",
                            "find payment d/YYYY-MM",
                            "find payment d/2026-03")),
            section("View Commands",
                    entry("view",
                            "Shows the details of the selected student.",
                            "view INDEX",
                            "view 1"),
                    entry("viewappt",
                            "Shows appointments for the week containing the given date.",
                            "viewappt [d/DATE]",
                            "viewappt d/2026-02-13")));

    private static String section(String title, String... entries) {
        return title + "\n" + "=".repeat(title.length()) + "\n\n" + String.join("\n\n", entries);
    }

    private static String entry(String command, String description, String format, String... examples) {
        StringBuilder builder = new StringBuilder();
        builder.append(command).append("\n");
        builder.append("Description: ").append(description).append("\n");
        builder.append("Format: ").append(format);

        if (examples.length > 0) {
            builder.append("\nExamples:");
            Arrays.stream(examples).forEach(example -> builder.append("\n  ").append(example));
        }

        return builder.toString();
    }

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(SHOWING_HELP_MESSAGE, true, false);
    }
}
