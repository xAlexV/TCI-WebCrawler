
import org.jsoup.Jsoup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;


@RunWith(value = Parameterized.class)
public class ParameterizedTest {


    private String genre;
    private String format;
    private String year;
    private String[] authors;
    private String publisher;
    private String isbn;
    private String link;


    public ParameterizedTest(String _genre, String _format, String _year, String[] _authors, String _publisher, String _isbn, String _link)
    {
        this.genre = _genre;
        this.format = _format;
        this.year = _year;
        this.authors = _authors;
        this.publisher = _publisher;
        this.isbn = _isbn;
        this.link = _link;
    }

    @Parameters(name = "Book data")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Tech", "Paperback", "1994",
                        new String[] {"Erich Gamma", "Richard Helm", "Ralph Johnson", "John Vlissides"},
                        "Prentice Hall", "978-0201633610", "http://i327618.hera.fhict.nl/details.php?id=101"},
                {"Tech", "Audio", "2011", new String[] {"Robert C. Martin"},
                "Prentice Hall", "007-6092046981", "http://i327618.hera.fhict.nl/details.php?id=104"},
                {"Tech", "Ebook", "2008", new String[] {"Robert C. Martin"}, "Prentice Hall",
                "978-0132350884", "http://i327618.hera.fhict.nl/details.php?id=102"}
        });
    }

    @Test
    public void testBookFound() throws IOException {
        Crawler crawler = new Crawler();
        Book book = new Book(genre, format, year, authors, publisher, isbn);
        String link = crawler.findItem("http://i327618.hera.fhict.nl", book, 0);
        Assert.assertEquals(this.link, link);
    }

}
