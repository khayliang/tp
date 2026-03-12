package seedu.address.model.person;

/**
 * Represents a Person's parent name in the address book.
 * Guarantees: immutable; is valid as declared in {@link Name#isValidName(String)}
 */
public class ParentName extends Name {

    /**
     * Constructs a {@code ParentName}.
     *
     * @param name A valid parent name.
     */
    public ParentName(String name) {
        super(name);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ParentName)) {
            return false;
        }

        ParentName otherParentName = (ParentName) other;
        return fullName.equals(otherParentName.fullName);
    }

}
