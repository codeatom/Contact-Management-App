package se.lexicon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ContactFileHandlerTest {

    @TempDir
    Path tempDir;

    private Person createPerson() {
        return new Person.Builder()
                .firstName("Anna")
                .lastName("Andersson")
                .email("anna@example.com")
                .phoneNumber("0701234567")
                .address("Main Street 1")
                .build();
    }

    @Test
    void saveContacts_shouldWriteContactsToFile() throws IOException {
        Path file = tempDir.resolve("contacts.txt");
        ContactFileHandler handler = new ContactFileHandler(file);

        Map<String, Person> contacts = new HashMap<>();
        contacts.put("anna@example.com", createPerson());

        handler.saveContacts(contacts);

        assertTrue(Files.exists(file));

        List<String> lines = Files.readAllLines(
                file, StandardCharsets.UTF_8);

        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("anna@example.com"));
        assertTrue(lines.get(0).contains("Anna"));
    }

    @Test
    void saveContacts_shouldHandleUnwritableDestination() {
        // A directory cannot be used as a regular output file.
        Path directory = tempDir.resolve("directory");
        assertDoesNotThrow(() -> Files.createDirectory(directory));

        ContactFileHandler handler = new ContactFileHandler(directory);

        Map<String, Person> contacts = new HashMap<>();
        contacts.put("anna@example.com", createPerson());

        // The handler catches IOException internally.
        assertDoesNotThrow(() -> handler.saveContacts(contacts));
        assertTrue(Files.isDirectory(directory));
    }

    @Test
    void loadContacts_shouldLoadValidContacts() throws IOException {
        Path file = tempDir.resolve("contacts.txt");

        Files.writeString(
                file,
                "Anna;Andersson;ANNA@EXAMPLE.COM;0701234567;Main Street 1",
                StandardCharsets.UTF_8
        );

        ContactFileHandler handler = new ContactFileHandler(file);
        Map<String, Person> contacts = new HashMap<>();

        handler.loadContacts(contacts);

        assertEquals(1, contacts.size());
        assertTrue(contacts.containsKey("anna@example.com"));

        Person person = contacts.get("anna@example.com");
        assertEquals("Anna", person.getFirstName());
        assertEquals("Andersson", person.getLastName());
        assertEquals("anna@example.com", person.getEmail());
    }

    @Test
    void loadContacts_shouldHandleMissingFile() {
        Path file = tempDir.resolve("missing.txt");
        ContactFileHandler handler = new ContactFileHandler(file);

        Map<String, Person> contacts = new HashMap<>();

        assertDoesNotThrow(() -> handler.loadContacts(contacts));
        assertTrue(contacts.isEmpty());
    }

    @Test
    void loadContacts_shouldSkipInvalidRecords() throws IOException {
        Path file = tempDir.resolve("contacts.txt");

        Files.write(
                file,
                List.of(
                        "Anna;Andersson;anna@example.com;0701234567;Street 1",
                        "Too;Few;Fields",
                        ";Andersson;invalid@example.com;070;Street 2"
                ),
                StandardCharsets.UTF_8
        );

        ContactFileHandler handler = new ContactFileHandler(file);
        Map<String, Person> contacts = new HashMap<>();

        handler.loadContacts(contacts);

        assertEquals(1, contacts.size());
        assertTrue(contacts.containsKey("anna@example.com"));
        assertFalse(contacts.containsKey("invalid@example.com"));
    }

    @Test
    void loadContacts_shouldIgnoreDuplicateEmailsIgnoringCase()
            throws IOException {
        Path file = tempDir.resolve("contacts.txt");

        Files.write(
                file,
                List.of(
                        "Anna;Andersson;anna@example.com;0701111111;Street 1",
                        "Ann;Other;ANNA@EXAMPLE.COM;0702222222;Street 2"
                ),
                StandardCharsets.UTF_8
        );

        ContactFileHandler handler = new ContactFileHandler(file);
        Map<String, Person> contacts = new HashMap<>();

        handler.loadContacts(contacts);

        assertEquals(1, contacts.size());

        // The first record is retained.
        assertEquals(
                "Anna",
                contacts.get("anna@example.com").getFirstName()
        );
    }

    @Test
    void saveAndLoadContacts_shouldPreserveEscapedCharacters()
            throws IOException {
        Path file = tempDir.resolve("contacts.txt");
        ContactFileHandler handler = new ContactFileHandler(file);

        Person person = new Person.Builder()
                .firstName("Anna;Marie")
                .lastName("Andersson")
                .email("anna@example.com")
                .phoneNumber("0701234567")
                .address("Street 1\\A\nApartment 2")
                .build();

        Map<String, Person> original = new HashMap<>();
        original.put("anna@example.com", person);

        handler.saveContacts(original);

        Map<String, Person> loaded = new HashMap<>();
        handler.loadContacts(loaded);

        assertEquals(1, loaded.size());
        assertEquals(
                "Anna;Marie",
                loaded.get("anna@example.com").getFirstName()
        );
        assertEquals(
                "Street 1\\A\nApartment 2",
                loaded.get("anna@example.com").getAddress()
        );
    }
}