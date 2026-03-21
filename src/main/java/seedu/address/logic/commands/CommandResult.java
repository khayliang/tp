package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;

    /** The application should view a person. */
    private final Index viewIndex;

    /** The application should view a person dynamically via lookup. */
    private final Person viewPerson;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this(feedbackToUser, showHelp, exit, null, null);
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * {@code showHelp}, {@code exit}, {@code viewIndex} and {@code viewPerson}.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit, Index viewIndex, Person viewPerson) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.viewIndex = viewIndex;
        this.viewPerson = viewPerson;
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser} and {@code viewIndex},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser, Index viewIndex) {
        this(feedbackToUser, false, false, viewIndex, null);
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser} and {@code viewPerson},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser, Person viewPerson) {
        this(feedbackToUser, false, false, null, viewPerson);
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false, null, null);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    public Index getViewIndex() {
        return viewIndex;
    }

    public Person getViewPerson() {
        return viewPerson;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit
                && Objects.equals(viewIndex, otherCommandResult.viewIndex)
                && Objects.equals(viewPerson, otherCommandResult.viewPerson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit, viewIndex, viewPerson);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .add("viewIndex", viewIndex)
                .add("viewPerson", viewPerson)
                .toString();
    }

}
