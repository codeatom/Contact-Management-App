package se.lexicon;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ContactFileHandler {

    //private static final Path FILE_PATH = Path.of("contacts.txt");

    private final Path filePath;

    public ContactFileHandler() {
        this(Path.of("contacts.txt"));
    }

    public ContactFileHandler(Path filePath) {
        this.filePath = filePath;
    }

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
            Files.write(filePath, lines, StandardCharsets.UTF_8);
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

        return value
                .replace("\\", "\\\\")
                .replace(";", "\\;")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }


    // Import contacts from a text file
    public void loadContacts(Map<String, Person> contacts) {
        if (!Files.exists(filePath)) {
            System.out.println("No saved contacts found. Starting fresh.");
            return;
        }

        try (Stream<String> lines =
                     Files.lines(filePath, StandardCharsets.UTF_8)) {

            lines.forEach(line -> {
                try {
                    List<String> fields = parseLine(line);

                    //The number of fields in Person.java is 5
                    if (fields.size() != 5) {
                        System.out.println("Skipping invalid contact line.");
                        return;
                    }

                    String firstName = fields.get(0);
                    String lastName = fields.get(1);
                    String email = fields.get(2).toLowerCase();
                    String phoneNumber = fields.get(3);
                    String address = fields.get(4);

                    if (contacts.containsKey(email)) {
                        return;
                    }

                    Person person = new Person.Builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .email(email)
                            .phoneNumber(phoneNumber)
                            .address(address)
                            .build();

                    contacts.put(email, person);

                } catch (IllegalStateException e) {
                    System.out.println("Skipping invalid contact: "
                            + e.getMessage());
                }
            });

            System.out.println("Contacts loaded: " + contacts.size());

        } catch (IOException e) {
            System.out.println("Error loading contacts: " + e.getMessage());
        }
    }


    // Split a line while preserving escaped delimiters
    private List<String> parseLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean escaped = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (escaped) {
                switch (ch) {
                    case 'n' -> field.append('\n');
                    case 'r' -> field.append('\r');
                    default -> field.append(ch);
                }
                escaped = false;
            } else if (ch == '\\') {
                escaped = true;
            } else if (ch == ';') {
                fields.add(field.toString());
                field.setLength(0);
            } else {
                field.append(ch);
            }
        }

        if (escaped) {
            field.append('\\');
        }

        fields.add(field.toString());
        return fields;
    }


/*  // Import contacts from a text file
    public void loadContacts(Map<String, Person> contacts) {
        if (!Files.exists(FILE_PATH)) {
            System.out.println("No saved contacts found. Starting fresh.");
            return;
        }

        try (Stream<String> lines =
                     Files.lines(FILE_PATH, StandardCharsets.UTF_8)) {

            Iterator<String> iterator = lines.iterator();

            while (iterator.hasNext()) {
                String line = iterator.next();

                try {
                    List<String> fields = parseLine(line);

                    if (fields.size() != 5) {
                        System.out.println("Skipping invalid contact line.");
                        continue;
                    }

                    String firstName = fields.get(0);
                    String lastName = fields.get(1);
                    String email = fields.get(2).toLowerCase();
                    String phoneNumber = fields.get(3);
                    String address = fields.get(4);

                    if (contacts.containsKey(email)) {
                        continue;
                    }

                    Person person = new Person.Builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .email(email)
                            .phoneNumber(phoneNumber)
                            .address(address)
                            .build();

                    contacts.put(email, person);

                } catch (IllegalStateException e) {
                    System.out.println("Skipping invalid contact: "
                            + e.getMessage());
                }
            }

            System.out.println("Contacts loaded: " + contacts.size());

        } catch (IOException e) {
            System.out.println("Error loading contacts: " + e.getMessage());
        }
    }*/

}
