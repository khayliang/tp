package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private Predicate<Person> currentFilterPredicate;
    private final Set<Person> preservedPersons;
    private ListDisplayMode listDisplayMode;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        currentFilterPredicate = PREDICATE_SHOW_ALL_PERSONS;
        preservedPersons = new HashSet<>();
        listDisplayMode = ListDisplayMode.PERSON;
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
        preservedPersons.clear();
        applyEffectiveFilterPredicate();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
        preservedPersons.remove(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        preservedPersons.remove(target);
        if (hasActiveFilter()) {
            preservedPersons.add(editedPerson);
        }
        addressBook.setPerson(target, editedPerson);
        applyEffectiveFilterPredicate();
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        preservedPersons.clear();
        currentFilterPredicate = predicate;
        applyEffectiveFilterPredicate();
    }

    @Override
    public void updateFilteredPersonListWithAnd(Predicate<Person> predicate) {
        requireNonNull(predicate);

        Predicate<? super Person> previousDisplayedPredicate = filteredPersons.getPredicate();

        // If there is no previous filter, allow everyone; otherwise, keep the current displayed set.
        Predicate<Person> displayedPredicate = person -> previousDisplayedPredicate == null
                || previousDisplayedPredicate.test(person);

        // Edited persons are preserved only until the next find command.
        preservedPersons.clear();

        if (currentFilterPredicate == PREDICATE_SHOW_ALL_PERSONS) {
            currentFilterPredicate = predicate;
        } else {
            currentFilterPredicate = displayedPredicate.and(predicate);
        }
        applyEffectiveFilterPredicate();
    }

    @Override
    public ListDisplayMode getListDisplayMode() {
        return listDisplayMode;
    }

    @Override
    public void setListDisplayMode(ListDisplayMode listDisplayMode) {
        requireNonNull(listDisplayMode);
        this.listDisplayMode = listDisplayMode;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons)
                && listDisplayMode == otherModelManager.listDisplayMode;
    }

    private boolean hasActiveFilter() {
        return currentFilterPredicate != PREDICATE_SHOW_ALL_PERSONS;
    }

    private void applyEffectiveFilterPredicate() {
        if (preservedPersons.isEmpty()) {
            filteredPersons.setPredicate(currentFilterPredicate);
            return;
        }

        Set<Person> pinnedSnapshot = new HashSet<>(preservedPersons);
        Predicate<Person> basePredicateSnapshot = currentFilterPredicate;
        Predicate<Person> effectivePredicate = person -> pinnedSnapshot.contains(person)
                || basePredicateSnapshot.test(person);
        filteredPersons.setPredicate(effectivePredicate);
    }

}
