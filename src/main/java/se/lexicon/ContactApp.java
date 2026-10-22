package se.lexicon;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ContactApp {

    private final Scanner scanner = new Scanner(System.in);

    // Stores contacts using email as the key
    private final Map<String, Person> contacts = new LinkedHashMap<>();

    // Stores email addresses for uniqueness checks
    private final List<String> emails = new ArrayList<>();

    public void run() {
        boolean running = true;

        while (running) {
            System.out.println("\n=== CONTACT MANAGEMENT ===");
            System.out.println("1. Create contact");
            System.out.println("2. Read contact");
            System.out.println("3. Read all contacts");
            System.out.println("4. Update contact");
            System.out.println("5. Delete contact");
            System.out.println("6. Search contacts");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> create();
                case "2" -> read();
                case "3" -> readAll();
                case "4" -> update();
                case "5" -> delete();
                case "6" -> search();
                case "0" -> {
                    running = false;
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid option.");
            }
        }

        scanner.close();
    }


    public void create() {
        System.out.println("\n--- Create Contact ---");

        String firstName = readRequired("First name: ");
        String lastName = readRequired("Last name: ");
        String email = readRequired("Email: ").toLowerCase();

        // Check email uniqueness before creating the Person
        if (emails.contains(email)) {
            System.out.println("Error: Email already exists.");
            return;
        }

        String phoneNumber = readRequired("Phone number: ");
        String address = readRequired("Address: ");

        Person person = new Person.Builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .address(address)
                .build();

        contacts.put(email, person);
        emails.add(email);

        System.out.println("Contact created successfully.");
    }


    public void read() {
        System.out.print("Enter email to find: ");
        String email = scanner.nextLine().trim().toLowerCase();

        Person person = contacts.get(email);

        if (person == null) {
            System.out.println("Contact not found.");
        } else {
            System.out.println("\n" + person);
        }
    }


    public void readAll() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts available.");
            return;
        }

        System.out.println("\n--- All Contacts ---");

        for (Person person : contacts.values()) {
            System.out.println("--------------------");
            System.out.println(person);
        }
    }


    public void update() {

    }


    public void delete() {
        System.out.print("Enter email of the contact to delete: ");
        String email = scanner.nextLine().trim().toLowerCase();

        Person removed = contacts.remove(email);

        if (removed == null) {
            System.out.println("Contact not found.");
        } else {
            emails.remove(email);
            System.out.println("Contact deleted successfully.");
        }
    }


    public void search() {
        System.out.print("Enter a first or last name to search: ");
        String query = scanner.nextLine().trim().toLowerCase();

        boolean found = false;

        for (Person person : contacts.values()) {
            String firstName = person.getFirstName().toLowerCase();
            String lastName = person.getLastName().toLowerCase();

            if (firstName.contains(query) || lastName.contains(query)) {
                System.out.println("--------------------");
                System.out.println(person);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching contacts found.");
        }
    }


    private String readRequired(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("This field cannot be empty.");
        }
    }

}
