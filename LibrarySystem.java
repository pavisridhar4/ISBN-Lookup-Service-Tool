import java.util.*;
import java.io.*;
import java.time.LocalDateTime;

/**
 * The main entry point and controller for the Library Management System.
 * This class manages the collection of books, handles the user interface via a console menu,
 * and manages data persistence to the local file system.
 */
public class LibrarySystem {
    /** Internal storage of books, mapped by their unique ISBN string. */
    private static final Map<String, Book> collection = new HashMap<>();

    /** Service used to retrieve book titles from an external API based on ISBN. */
    private static final ISBNLookupService lookupService = new LibraryLookupService();

    /**
     * Main execution loop for the Library System.
     * Initializes the collection from disk and manages the user interaction menu.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        // to load any existing data from the file system
        loadCollection("library.txt");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Library Management System Initialized.");

        boolean running = true;
        while (running) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Scan ISBN");
            System.out.println("2. Update Book Location");
            System.out.println("3. View Collection");
            System.out.println("4. Search Collection");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter ISBN to scan: ");
                    String isbn = scanner.nextLine();
                    handleScan(isbn);
                    break;
                case "2":
                    System.out.print("Enter ISBN to update location: ");
                    String targetIsbn = scanner.nextLine();
                    updateLocation(targetIsbn, scanner);
                    break;
                case "3":
                    displayCollection();
                    break;
                case "4":
                    System.out.print("Enter search keyword: ");
                    String query = scanner.nextLine();
                    searchCollection(query);
                    break;
                case "5":
                    saveCollection("library.txt");
                    running = false;
                    System.out.println("Data saved. Closing system...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    /**
     * Processes a book scan. If the book exists, its history is updated.
     * If the book is new, its title is fetched via the lookup service.
     *
     * @param isbn the unique identifier of the book being scanned.
     */
    private static void handleScan(String isbn) {
        try {
            if (collection.containsKey(isbn)) {
                collection.get(isbn).recordScan();
                System.out.println("Scan recorded for: " + collection.get(isbn).getTitle());
            }
            else {
                String title = lookupService.lookup(isbn);
                Book newBook = new Book(isbn, title);
                collection.put(isbn, newBook);
                System.out.println("New book added to collection: " + title);
            }
        } catch (Exception e) {
            System.err.println("Error during scan: " + e.getMessage());
        }
    }

    /**
     * Updates the physical storage location of a book in the collection.
     *
     * @param isbn    the ISBN of the book to update.
     * @param scanner the scanner instance used to read the new location from user input.
     */
    private static void updateLocation(String isbn, Scanner scanner) {
        if (collection.containsKey(isbn)) {
            System.out.print("Enter new physical location: ");
            String location = scanner.nextLine();
            // Requirement: Set and update the location via a scanned ISBN
            collection.get(isbn).setLocation(location);
            System.out.println("Location updated.");
        } else {
            System.err.println("Error: Book not found.");
        }
    }

    /**
     * Displays all books currently held in the library collection.
     */
    private static void displayCollection() {
        if (collection.isEmpty()) {
            System.out.println("The collection is currently empty.");
        } else {
            for (Book b : collection.values()) {
                System.out.println(b);
            }
        }
    }

    /**
     * Searches for books whose titles contain the specified query string.
     * The search is case-insensitive.
     *
     * @param query the search keyword.
     */
    private static void searchCollection(String query) {
        int count = 0;
        String lowerQuery = query.toLowerCase();
        for (Book b : collection.values()) {
            if (b.getTitle().toLowerCase().contains(lowerQuery)){
                System.out.println(b);
                count++;
            }
        }
        System.out.println("Found " + count + " results.");
    }

    /**
     * Saves the current library collection to a text file for persistent storage.
     * Uses a pipe-delimited format for book data and comma-separated timestamps for history.
     *
     * @param filename the name of the file to save data to.
     */
    public static void saveCollection(String filename) {

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Book book : collection.values()) {
                // We use a delimiter (like |) to separate fields for easy parsing later
                writer.print(book.getIsbn() + "|");
                writer.print(book.getTitle() + "|");
                writer.print(book.getLocation() + "|");

                // For the scan history, we can join the timestamps with commas
                List<String> scans = new ArrayList<>();
                for (LocalDateTime dt : book.getScanHistory()) {
                    scans.add(dt.toString());
                }
                writer.println(String.join(",", scans));
            }
            System.out.println("Collection saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving collection: " + e.getMessage());
        }
    }

    /**
     * Loads the library collection from a text file during system initialization.
     *
     * @param filename the name of the file to read data from.
     */
    public static void loadCollection(String filename) {
        File file = new File(filename);
        if (!file.exists()) return;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");

                if (parts.length >= 3) {
                    String isbn = parts[0];
                    String title = parts[1];
                    String location = parts[2];

                    Book book = new Book(isbn, title);
                    book.setLocation(location);

                    if (parts.length > 3) {
                        String[] scanDates = parts[3].split(",");
                        for (String dateStr : scanDates) {
                            LocalDateTime dt = LocalDateTime.parse(dateStr);
                            book.getScanHistory().add(dt);
                        }
                    }
                    collection.put(isbn, book);
                }
            }
            System.out.println("Collection loaded successfully.");
        }
        catch(FileNotFoundException ignored) {
            // No action needed if file doesn't exist
        }
    }
}

