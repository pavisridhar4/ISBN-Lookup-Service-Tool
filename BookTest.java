import org.junit.Test;
import static org.junit.Assert.*;



public class BookTest {

    /**
     * Verifies that the scan history list grows by exactly one
     * each time recordScan is called.
     */
    @Test
    public void testRecordScanIncrementsHistory() {
        Book book = new Book("9781454945970", "My Little Garden");
        int initialSize = book.getScanHistory().size();
        book.recordScan();
        assertEquals(initialSize + 1, book.getScanHistory().size());
    }

    /**
     * Ensures the location string is correctly updated and persists.
     */
    @Test
    public void testSetLocationUpdatesCorrectly() {
        Book book = new Book("9781454945970", "My Little Garden");
        book.setLocation("Garden City Library, Level 4");
        assertEquals("Garden City Library, Level 4", book.getLocation());
    }

    /**
     * Confirms the title provided at construction is correctly returned.
     */
    @Test
    public void testGetTitleReturnsCorrectValue() {
        Book book = new Book("9780340670729", "Famous Five"); //
        assertEquals("Famous Five", book.getTitle());
    }

    /**
     * Validates that the scan history can handle high-frequency events
     * and maintains an accurate count over time.
     */
    @Test
    public void testMultipleScansRecorded() {
        Book book = new Book("9780345461391", "The Long Walk"); //

        // Perform multiple scans
        book.recordScan();
        book.recordScan();
        book.recordScan();

        assertEquals(4, book.getScanHistory().size());
    }
}
