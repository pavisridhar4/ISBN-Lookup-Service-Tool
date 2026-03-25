import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An implementation of the {@link ISBNLookupService} that retrieves book information
 * from the Open Library Web API.
 * <p>
 */
public class LibraryLookupService implements ISBNLookupService {

    /** The base URL for the Open Library API request. */
    private static final String BASE_URL = "https://openlibrary.org/api/books?bibkeys=ISBN:%s&jscmd=details&format=json";

    /**
     * Finds the title of a book given its ISBN by querying an external web service.
     * <p>
     * If the ISBN is null, empty, or the book cannot be found in the external database,
     * the method returns "Unknown" rather than throwing an exception to ensure
     * system stability during UI operations.
     *
     * @param isbn the ISBN string of the book to look up.
     * @return the title of the book as a String, or "Unknown" if not found or invalid.
     * @throws IOException if an error occurs during the network connection or
     * reading the API response.
     */
    @Override
    public String lookup(String isbn) throws IOException {
        if (isbn == null || isbn.isEmpty()) {
            return "Unknown";
        }

        URL url = URI.create(String.format(BASE_URL, isbn)).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String content = br.readLine();

            // Check if the stream returned no data
            if (content == null) {
                return "Unknown";
            }

            // Define regex pattern to extract the title
            Pattern pattern = Pattern.compile("\"title\": \"(.*?)\"");
            Matcher matcher = pattern.matcher(content);

            if (matcher.find()) {
                // Return the first captured group (the actual title)
                return matcher.group(1);
            }
        }
        // Default fallback if the title pattern was not matched
        return "Unknown";
    }
}
