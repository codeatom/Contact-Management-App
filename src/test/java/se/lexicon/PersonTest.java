package se.lexicon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    void build_shouldCreatePerson_whenRequiredFieldsAreValid() {
        Person person = new Person.Builder()
                .firstName("Anna")
                .lastName("Andersson")
                .email("anna@example.com")
                .phoneNumber("0701234567")
                .address("Main Street 1")
                .build();

        assertEquals("Anna", person.getFirstName());
        assertEquals("Andersson", person.getLastName());
        assertEquals("anna@example.com", person.getEmail());
        assertEquals("0701234567", person.getPhoneNumber());
        assertEquals("Main Street 1", person.getAddress());
    }

    @Test
    void build_shouldThrowException_whenFirstNameIsMissing() {
        Person.Builder builder = new Person.Builder()
                .lastName("Andersson")
                .email("anna@example.com");

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void build_shouldThrowException_whenLastNameIsBlank() {
        Person.Builder builder = new Person.Builder()
                .firstName("Anna")
                .lastName(" ")
                .email("anna@example.com");

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void build_shouldThrowException_whenEmailIsMissing() {
        Person.Builder builder = new Person.Builder()
                .firstName("Anna")
                .lastName("Andersson");

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void build_shouldAllowOptionalFieldsToBeNull() {
        Person person = new Person.Builder()
                .firstName("Anna")
                .lastName("Andersson")
                .email("anna@example.com")
                .build();

        assertNull(person.getPhoneNumber());
        assertNull(person.getAddress());
    }

    @Test
    void toString_shouldContainContactInformation() {
        Person person = new Person.Builder()
                .firstName("Anna")
                .lastName("Andersson")
                .email("anna@example.com")
                .phoneNumber("0701234567")
                .address("Main Street 1")
                .build();

        String result = person.toString();

        assertTrue(result.contains("Anna Andersson"));
        assertTrue(result.contains("anna@example.com"));
        assertTrue(result.contains("0701234567"));
        assertTrue(result.contains("Main Street 1"));
    }
}
