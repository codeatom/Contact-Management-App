package se.lexicon;


import java.util.*;

public class ContactApp {

    private final Scanner scanner;
    private final ContactFileHandler fileHandler;

    // Stores contacts using email as the key
    private final Map<String, Person> contacts = new LinkedHashMap<>();

    // Stores email addresses for uniqueness checks
    private final List<String> emails = new ArrayList<>();


    public ContactApp() {
        this(new Scanner(System.in), new ContactFileHandler());
    }

    ContactApp(Scanner scanner, ContactFileHandler fileHandler) {
        this.scanner = scanner;
        this.fileHandler = fileHandler;
    }

    public Map<String, Person> getContacts() {
        return contacts;
    }


    public void run() {
        fileHandler.loadContacts(contacts);
        emails.addAll(contacts.keySet());

        boolean running = true;

        while (running) {
            System.out.println("\n=== CONTACT MANAGEMENT ===");
            System.out.println("1. Create contact");
            System.out.println("2. Read contact");
            System.out.println("3. Read all contacts");
            System.out.println("4. Update contact");
            System.out.println("5. Delete contact");
            System.out.println("6. Search contacts");
            System.out.println("7. Display contacts sorted by name");
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
                case "7" -> displayContactsSortedByName();
                case "0" -> {
                    fileHandler.saveContacts(contacts);
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
        System.out.print("Enter the current email of the contact: ");
        String oldEmail = scanner.nextLine().trim().toLowerCase();

        Person existing = contacts.get(oldEmail);

        if (existing == null) {
            System.out.println("Contact not found.");
            return;
        }

        String existingFirstName = existing.getFirstName();
        String existingLastName = existing.getLastName();
        String existingPhoneNumber = existing.getPhoneNumber();
        String existingAddress = existing.getAddress();
        String existingEmail = existing.getEmail();


        // Ask for the new email first
        System.out.print("Enter the new email ["
                + existingEmail + "]: ");

        String newEmail = scanner.nextLine().trim().toLowerCase();

        // Keep the current email if the user presses Enter
        if (newEmail.isEmpty()) {
            newEmail = oldEmail;
        }

        // Check availability before asking for other fields
        if (!newEmail.equals(oldEmail) && emails.contains(newEmail)) {
            System.out.println(
                    "Error: Email already belongs to another contact.");
            return;
        }

        // Ask for the remaining fields only after email validation
        System.out.println("Press Enter to keep the existing value.");

        String firstName = readOptional(
                "First name [" + existingFirstName + "]: ",
                existing.getFirstName());

        String lastName = readOptional(
                "Last name [" + existingLastName + "]: ",
                existing.getLastName());

        String phoneNumber = readOptional(
                "Phone number [" + existingPhoneNumber + "]: ",
                existing.getPhoneNumber());

        String address = readOptional(
                "Address [" + existingAddress + "]: ",
                existing.getAddress());

        // Because a Person is immutable we build a new Person and remove the old person
        Person updated = new Person.Builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(newEmail)
                .phoneNumber(phoneNumber)
                .address(address)
                .build();

        // Update both collections if the email has changed
        if (!oldEmail.equals(newEmail)) {
            contacts.remove(oldEmail);
            emails.remove(oldEmail);
        }

        contacts.put(newEmail, updated);

        if (!emails.contains(newEmail)) {
            emails.add(newEmail);
        }

        System.out.println("Contact updated successfully.");
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


    private String readOptional(String prompt, String currentValue) {
        System.out.print(prompt);
        String value = scanner.nextLine().trim();

        return value.isEmpty() ? currentValue : value;
    }


    public void displayContactsSortedByName() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts available.");
            return;
        }

        List<Person> sortedContacts = new ArrayList<>(contacts.values());

        Collections.sort(sortedContacts, new PersonComparator());

        System.out.println("\n--- Contacts Sorted by Name ---");

        for (Person person : sortedContacts) {
            System.out.println("--------------------");
            System.out.println(person);
        }
    }


    private static class PersonComparator implements Comparator<Person> {   // anonymous inner class
        @Override
        public int compare(Person p1, Person p2) {
            int result = p1.getFirstName()
                    .compareToIgnoreCase(p2.getFirstName());

            if (result == 0) {
                return p1.getLastName()
                        .compareToIgnoreCase(p2.getLastName());
            }

            return result;
        }
    }
}
