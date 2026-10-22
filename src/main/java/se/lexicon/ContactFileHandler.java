package se.lexicon;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ContactFileHandler {

    private static final Path FILE_PATH = Path.of("contacts.txt");

    // Export all contacts to a text file
    public void saveContacts(Map<String, Person> contacts) {
        List<String> lines = new ArrayList<>();

        for (Person person : contacts.values()) {
            String line = String.join(";",
                    escape(person.getFirstName()),
                    escape(person.getLastName()),
                    escape(person.getEmail()),
                    escape(person.getPhoneNumber()),
                    escape(person.getAddress())
            );

            lines.add(line);
        }

        try {
            Files.write(FILE_PATH, lines, StandardCharsets.UTF_8);
            System.out.println("Contacts saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving contacts: " + e.getMessage());
        }
    }


    // Escape special characters before saving
    private String escape(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("\\", "\\\\")
                .replace(";", "\\;")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
    
}
