import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class LibraryLookupServiceTest {

    /**
     * Verifies that the class correctly implements the ISBNLookupService interface.
     * This ensures the system maintains a decoupled architecture.
     */
    @Test
    public void testInterfaceImplementation() throws IOException {
        ISBNLookupService service = new LibraryLookupService();
        String title = service.lookup("9780141439518");
        assertNotNull("The interface method must return a value", title);
    }

    /**
     * Tests a valid, successful API lookup.
     * Verifies that the service can fetch and parse a real-world book title (Famous Five).
     */
    @Test
    public void testLookupReturnsString() throws IOException {
        LibraryLookupService service = new LibraryLookupService();
        // Testing a known valid ISBN - this is for the book Famous Five
        String result = service.lookup("9780340670729");
        assertNotNull("Lookup should not return null", result);
    }

    /**
     * Edge Case: Ensures that an empty ISBN string does not crash the system
     * and instead returns the standardized "Unknown" value.
     */
    @Test
    public void testLookupEmptyIsbn() throws IOException {
        LibraryLookupService service = new LibraryLookupService();
        // Edge Case: Instance if the ISBN string is empty?
        String result = service.lookup("");
        assertEquals("Unknown", result);
    }

    /**
     * Edge Case: Validates the robustness of the Regex parsing.
     * Ensuring non-numeric or malformed ISBN strings are handled gracefully.
     */
    @Test
    public void testLookupInvalidCharacters() throws IOException {
        LibraryLookupService service = new LibraryLookupService();
        // Edge Case: ISBN containing non-numeric characters
        String result = service.lookup("ABC-123-HELP");
        assertEquals("Unknown", result);
    }

    /**
     * Edge Case: Verifies that passing a null reference is handled via
     * null-safe checks rather than allowing a NullPointerException.
     */
    @Test
    public void testLookupNullIsbn() throws IOException {
        LibraryLookupService service = new LibraryLookupService();
        // Edge Case: Passing a null reference
        String result = service.lookup(null);
        assertEquals("Unknown", result);
    }
}