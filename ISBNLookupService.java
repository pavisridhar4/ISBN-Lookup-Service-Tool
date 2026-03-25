import java.io.*;

public interface ISBNLookupService {
    /**
     * Finds the title of a book given its ISBN.
     * @param isbn The ISBN string.
     * @return The book title.
     * @throws IOException If a network or file error occurs.
     */
    String lookup(String isbn) throws IOException;
}
