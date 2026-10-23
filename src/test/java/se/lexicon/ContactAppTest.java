package se.lexicon;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ContactAppTest {

    private ContactApp createApp(String input) {
        return new ContactApp(
                new Scanner(input),
                new ContactFileHandler()
        );
    }

    /* returns appropriate input for scanner */
    private String contactInput(String firstName, String lastName, String email) {
        return firstName + "\n"
                + lastName + "\n"
                + email + "\n"
                + "0701234567\n"
                + "Main Street 1\n";
    }

    private Map<String, Person> getContacts(ContactApp app) {
        return app.getContacts();
    }



    // CREATE

    @Test
    void createShouldCreateContactSuccessfully() {
        ContactApp app = createApp(
                contactInput("John", "Doe", "john@example.com")
        );

        app.create();

        Map<String, Person> contacts = getContacts(app);

        assertEquals(1, contacts.size());
        assertTrue(contacts.containsKey("john@example.com"));
        assertEquals(
                "John",
                contacts.get("john@example.com").getFirstName()
        );
        assertEquals(
                "Doe",
                contacts.get("john@example.com").getLastName()
        );
    }

    @Test
    void createShouldRejectDuplicateEmail() {
        ContactApp app = createApp(
                contactInput("John", "Doe", "john@example.com")
                        + contactInput(
                        "Jane", "Smith", "JOHN@example.com"
                )
        );

        app.create();
        app.create();

        Map<String, Person> contacts = getContacts(app);

        assertEquals(1, contacts.size());
        assertTrue(contacts.containsKey("john@example.com"));
        assertFalse(contacts.containsKey("jane@example.com"));
    }



    // READ

    @Test
    void readShouldHandleExistingContact() {
        ContactApp app = createApp(
                contactInput("John", "Doe", "john@example.com")
                        + "john@example.com\n"
        );

        app.create();

        assertDoesNotThrow(app::read);
        assertEquals(1, getContacts(app).size());
    }

    @Test
    void readShouldHandleContactNotFound() {
        ContactApp app = createApp("missing@example.com\n");

        assertDoesNotThrow(app::read);
        assertTrue(getContacts(app).isEmpty());
    }



    // READ ALL

    @Test
    void readAllShouldHandleMultipleContacts() {
        ContactApp app = createApp(
                contactInput("John", "Doe", "john@example.com")
                        + contactInput(
                        "Jane", "Smith", "jane@example.com"
                )
        );

        app.create();
        app.create();

        assertDoesNotThrow(app::readAll);
        assertEquals(2, getContacts(app).size());
    }

    @Test
    void readAllShouldHandleEmptyCollection() {
        ContactApp app = createApp("");

        assertDoesNotThrow(app::readAll);
        assertTrue(getContacts(app).isEmpty());
    }



    // UPDATE

    @Test
    void updateShouldUpdateContactSuccessfully() {
        ContactApp app = createApp(
                contactInput("John", "Doe", "john@example.com")
                        + "john@example.com\n"
                        + "john.new@example.com\n"
                        + "Johnny\n"
                        + "\n"
                        + "\n"
                        + "\n"
        );

        app.create();
        app.update();

        Map<String, Person> contacts = getContacts(app);

        assertEquals(1, contacts.size());
        assertFalse(contacts.containsKey("john@example.com"));
        assertTrue(contacts.containsKey("john.new@example.com"));

        Person updated = contacts.get("john.new@example.com");

        assertEquals("Johnny", updated.getFirstName());
        assertEquals("Doe", updated.getLastName());
        assertEquals("john.new@example.com", updated.getEmail());
    }

    @Test
    void updateShouldHandleContactNotFound() {
        ContactApp app = createApp("missing@example.com\n");

        assertDoesNotThrow(app::update);
        assertTrue(getContacts(app).isEmpty());
    }

    @Test
    void updateShouldRejectEmailBelongingToAnotherContact() {
        ContactApp app = createApp(
                contactInput("John", "Doe", "john@example.com")
                        + contactInput(
                        "Jane", "Smith", "jane@example.com"
                )
                        + "john@example.com\n"
                        + "jane@example.com\n"
        );

        app.create();
        app.create();
        app.update();

        Map<String, Person> contacts = getContacts(app);

        assertEquals(2, contacts.size());
        assertTrue(contacts.containsKey("john@example.com"));
        assertTrue(contacts.containsKey("jane@example.com"));

        assertEquals(
                "John",
                contacts.get("john@example.com").getFirstName()
        );
    }



    // DELETE

    @Test
    void deleteShouldDeleteExistingContact() {
        ContactApp app = createApp(
                contactInput("John", "Doe", "john@example.com")
                        + "john@example.com\n"
        );

        app.create();
        app.delete();

        assertTrue(getContacts(app).isEmpty());
    }

    @Test
    void deleteShouldHandleContactNotFound() {
        ContactApp app = createApp("missing@example.com\n");

        assertDoesNotThrow(app::delete);
        assertTrue(getContacts(app).isEmpty());
    }



    // SEARCH

    @Test
    void searchShouldHandleExistingContact() {
        ContactApp app = createApp(
                contactInput("Jonathan", "Doe", "john@example.com")
                        + "jon\n"
        );

        app.create();

        assertDoesNotThrow(app::search);
        assertEquals(1, getContacts(app).size());
    }

    @Test
    void searchShouldHandleNoMatchingContacts() {
        ContactApp app = createApp(
                contactInput("John", "Doe", "john@example.com")
                        + "Unknown\n"
        );

        app.create();

        assertDoesNotThrow(app::search);
        assertEquals(1, getContacts(app).size());
    }



    // SORT BY NAME

    @Test
    void displayContactsSortedByNameShouldHandleMultipleContacts() {
        ContactApp app = createApp(
                contactInput("Zoe", "Brown", "zoe@example.com")
                        + contactInput(
                        "Amy", "Smith", "amy@example.com"
                )
        );

        app.create();
        app.create();

        assertDoesNotThrow(app::displayContactsSortedByName);
        assertEquals(2, getContacts(app).size());
    }

    @Test
    void displayContactsSortedByNameShouldHandleEmptyCollection() {
        ContactApp app = createApp("");

        assertDoesNotThrow(app::displayContactsSortedByName);
        assertTrue(getContacts(app).isEmpty());
    }
}