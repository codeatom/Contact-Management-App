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

    }


    public void read() {

    }


    public void readAll() {

    }


    public void update() {

    }


    public void delete() {

    }


    public void search() {

    }

}
