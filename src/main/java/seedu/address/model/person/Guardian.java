package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a Guardian (parent/caretaker) of a Person in the address book. A Guardian is a contact belonging to a
 * student, with a compulsory name and optional phone and email fields. This is a value object — immutable once created.
 */
public class Guardian {

    private final Name name;
    private final Optional<Phone> phone;
    private final Optional<Email> email;

    /**
     * Constructs a {@code Guardian}. Name must not be null. Phone and email are optional.
     */
    public Guardian(Name name, Phone phone, Email email) {
        requireNonNull(name);
        this.name = name;
        this.phone = Optional.ofNullable(phone);
        this.email = Optional.ofNullable(email);
    }

    public Name getName() {
        return name;
    }

    public Optional<Phone> getPhone() {
        return phone;
    }

    public Optional<Email> getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Guardian)) {
            return false;
        }

        Guardian o = (Guardian) other;
        return Objects.equals(name, o.name) && Objects.equals(phone, o.phone) && Objects.equals(email, o.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone.orElse(null))
                .add("email", email.orElse(null))
                .toString();
    }
}
