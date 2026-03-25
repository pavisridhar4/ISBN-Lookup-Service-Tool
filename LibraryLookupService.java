import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a book in the library collection.
 * Maintains the book's identity, physical location, and scan history.
 */
public class Book {
    private final String isbn;
    private final String title;
    private String location;
    private final List<LocalDateTime> scanHistory;

    /**
     * Constructs a new Book instance and records the initial scan timestamp.
     *
     * @param isbn  the unique 13-digit identifier for the book; must not be null.
     * @param title the descriptive name of the book; must not be null.
     * @throws IllegalArgumentException if either isbn or title is null.
     */

    public Book(String isbn, String title) {
        if (isbn == null || title == null) {
            throw new IllegalArgumentException("ISBN and Title cannot be null");
        }

        this.isbn = isbn;
        this.title = title;
        this.location = "Default";
        this.scanHistory = new ArrayList<>();
        recordScan();

    }

    /**
     * Records a new scan event by adding the current system time to the history.
     * */
    public void recordScan() {
        this.scanHistory.add(LocalDateTime.now());
    }

    /**
     * Updates the physical storage location of the book.
     *
     * @param location the new library or level identifier for the book.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the book's unique ISBN string.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @return the title of the book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the current physical location of the book.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Retrieves the history of all recorded scans for this book.
     *
     * @return a list of LocalDateTime objects representing each scan event.
     */
    public List<LocalDateTime> getScanHistory() {
        return this.scanHistory;
    }

    /**
     * Returns a string representation of the book's current status.
     *
     * @return a formatted string containing ISBN, title, location, and scan count.
     */
    @Override
    public String toString() {
        return String.format("ISBN: %s | Title: %s | Location: %s | Scans: %d",
                isbn, title, location, scanHistory.size());
    }

}
