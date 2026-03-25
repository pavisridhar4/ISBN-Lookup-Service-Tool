import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;

public class LibrarySystemTest {

    /**
     * Verifies that the system can successfully map an ISBN string to a Book object.
     * Confirms that both the key (ISBN) and the value (Book data) are stored accurately.
     */
    @Test
    public void testAddBookToCollection() {

        HashMap<String, Book> collection = new HashMap<>();
        Book newBook = new Book("9781668208816", "The Long Walk");

        collection.put(newBook.getIsbn(), newBook);

        assertTrue("Collection should contain the ISBN key", collection.containsKey("9781668208816"));
        assertEquals("Title should match", "The Long Walk", collection.get("9781668208816").getTitle());
    }

    /**
     * Tests the behavior of object references within the collection.
     * Verifies that updating a Book object retrieved from the Map correctly
     * updates the data stored in the collection without needing a re-insert.
     */
    @Test
    public void testUpdateExistingBookData() {
        HashMap<String, Book> collection = new HashMap<>();
        Book myBook = new Book("9781668208816", "The Long Walk");
        collection.put("9781668208816", myBook);

        // Action: Retrieve and update the location
        Book retrievedBook = collection.get("9781668208816");
        retrievedBook.setLocation("Chermside Library, Level 2");

        // Verification: The book inside the HashMap should reflect the change
        assertEquals("Chermside Library, Level 2", collection.get("9781668208816").getLocation());
    }
}
