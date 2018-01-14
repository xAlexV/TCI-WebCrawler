//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import crawler.model.*;
import crawler.service.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.*;

@RunWith(Parameterized.class)
public class CrawlerTest {

    private String genre;
    private String format;
    private String year;
    private String[] authors;
    private String publisher;
    private String isbn;
    private String name;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    public CrawlerTest(String _genre, String _format, String _year, String[] _authors, String _publisher, String _isbn, String _name)
    {
        this.name = _name;
        this.genre = _genre;
        this.format = _format;
        this.year = _year;
        this.authors = _authors;
        this.publisher = _publisher;
        this.isbn = _isbn;
    }

    @Parameterized.Parameters(name = "Book data")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Tech", "Paperback", "1994",
                        new String[] {"Erich Gamma", "Richard Helm", "Ralph Johnson", "John Vlissides"},
                        "Prentice Hall", "978-0201633610", "A Design Patterns: Elements of Reusable Object-Oriented Software"},
                {"Tech", "Audio", "2011", new String[] {"Robert C. Martin"},
                        "Prentice Hall", "007-6092046981", "The Clean Coder: A Code of Conduct for Professional Programmers"},
                {"Tech", "Ebook", "2008", new String[] {"Robert C. Martin"}, "Prentice Hall",
                        "978-0132350884", "Clean Code: A Handbook of Agile Software Craftsmanship"}
        });
    }

    @Test
    public void testBookFound() throws IOException {
        Crawler crawler = new Crawler();
        Book book = new Book(genre, format, year, authors, publisher, isbn);
        Book expectedBook = (Book)crawler.findItem("http://i327618.hera.fhict.nl", this.name, 0);
        Assert.assertEquals(book, expectedBook);
    }

    @InjectMocks
    Crawler crawler = new Crawler();

    @Test(expected=IllegalArgumentException.class)
    public void getAllDocuments_not_a_url() throws IOException{
        crawler.getAllDocuments("wrong link", 0);
    }

    @Test(expected=IOException.class)
    public void getAllDocuments_not_working_link() throws IOException{
        crawler.getAllDocuments("http://hello.world",0);
    }

    //Number of links that should be found
    @Test
    public void getAllDocuments_link_with_links_inside() throws IOException{
        List<Document> documents = crawler.getAllDocuments("http://i327618.hera.fhict.nl", 0);
        Assert.assertEquals(21, documents.size());
    }

    //Expected output for a book
    @Test
    public void documentToMap_book_from_link() throws IOException{
        Document document = Jsoup.connect("http://i327618.hera.fhict.nl/details.php?id=101").get();
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Category", "Books");
        expectedMap.put("Format", "Paperback");
        expectedMap.put("Year", "1994");
        expectedMap.put("ISBN", "978-0201633610");
        expectedMap.put("Authors", "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides");
        expectedMap.put("Genre", "Tech");
        expectedMap.put("Publisher", "Prentice Hall");
        Assert.assertEquals(crawler.documentToMap(document), expectedMap);
    }

    @Test
    public void getAllDocumentsFromWebsite() throws IOException {
        List<Document> documents = crawler.getAllDocuments("http://i327618.hera.fhict.nl", 0);
        List<Map<String, String>> maps = new ArrayList<>();
        for(Document doc : documents){
            Map<String, String> mapFromDocument = crawler.documentToMap(doc);
            if(mapFromDocument != null) {
                maps.add(mapFromDocument);
            }
        }
        for(Map<String, String> map : maps){
            System.out.println(map);
        }
    }

    @Test
    public void convertMapWithBookToObject(){
        HashMap<String, String> mapToConvert = new HashMap<>();
        mapToConvert.put("Category", "Books");
        mapToConvert.put("Format", "Paperback");
        mapToConvert.put("Year", "1994");
        mapToConvert.put("ISBN", "978-0201633610");
        mapToConvert.put("Authors", "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides");
        mapToConvert.put("Genre", "Tech");
        mapToConvert.put("Publisher", "Prentice Hall");
        Book expectedResult = new Book("Tech", "Paperback", "1994",
                new String[]{"Erich Gamma", "Richard Helm", "Ralph Johnson", "John Vlissides"},
                "Prentice Hall", "978-0201633610");
        List<Item> expectedList = new ArrayList<>();
        expectedList.add(expectedResult);
        List<Map<String, String>> maps = new ArrayList<>();
        maps.add(mapToConvert);
        List<Item> convertedItem = crawler.mapToItems(maps);
        Assert.assertArrayEquals(expectedList.toArray(), convertedItem.toArray());
    }

    @Test
    public void findBook() throws IOException{
        Book book = new Book("Tech", "Paperback", "1994",
                new String[]{"Erich Gamma", "Richard Helm", "Ralph Johnson", "John Vlissides"},"Prentice Hall",
                "978-0201633610");
        Book foundBook = (Book)crawler.findItem("http://i327618.hera.fhict.nl",
                                       "A Design Patterns: Elements of Reusable Object-Oriented Software",
                                       0);
        Assert.assertEquals(book, foundBook);
    }

    @Test
    public void findMovie() throws IOException{
        Movie movie = new Movie("Comedy", "Blu-ray", "1999",
                "Mike Judge", new String[]{"William Goldman"},
                new String[]{"Ron Livingston", "Jennifer Aniston", "David Herman", "Ajay Naidu", "Diedrich Bader", "Stephen Root"});
        Movie foundMovie = (Movie)crawler.findItem("http://i327618.hera.fhict.nl", "Office Space", 0);
        Assert.assertEquals(movie, foundMovie);
    }

    @Test
    public void findMusic() throws IOException{
        Music music = new Music("Rock", "Vinyl",
                "2015","Elvis Presley");
        Music foundMusic = (Music)crawler.findItem("http://i327618.hera.fhict.nl", "Elvis Forever", 0);
        Assert.assertEquals(music, foundMusic);
    }

    @Test
    public void findMusicFail() throws IOException{
        Music music = (Music)crawler.findItem("http://i327618.hera.fhict.nl", "Ricky Martin Forever", 0);
        Assert.assertNotEquals(null, music);
    }

    @Test
    public void createAction() throws IOException{
        int size = crawler.crawlingActions.size();
        crawler.createActionFindItem("http://i327618.hera.fhict.nl", "Elvis Forever");
        Assert.assertEquals(size + 1, crawler.crawlingActions.size());
    }
}
