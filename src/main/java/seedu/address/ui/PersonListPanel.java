package seedu.address.ui;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";

    private boolean showAppointments = false;

    @FXML
    private ListView<Person> personListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Person> personList) {
        super(FXML);
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new PersonListViewCell());
    }

    public void setShowAppointments(boolean showAppointments) {
        this.showAppointments = showAppointments;
        personListView.refresh();
    }

    /**
     * Returns the selected person property from the list view.
     */
    public ReadOnlyObjectProperty<Person> selectedPersonProperty() {
        return personListView.getSelectionModel().selectedItemProperty();
    }

    /**
     * Selects the first person in the list if available.
     */
    public void selectFirstPerson() {
        if (!personListView.getItems().isEmpty()) {
            personListView.getSelectionModel().selectFirst();
        }
    }

    /**
     * Selects a person at the specified {@code index}.
     */
    public void selectIndex(int index) {
        if (!personListView.getItems().isEmpty() && index >= 0 && index < personListView.getItems().size()) {
            personListView.getSelectionModel().select(index);
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(person, getIndex() + 1, showAppointments).getRoot());
            }
        }
    }

}
