//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrawlerTest {
    Crawler crawler;

    @Before
    public void initializeCrawler(){
        crawler = new Crawler();
    }

    @Test(expected=IllegalArgumentException.class)
    public void getAllDocuments_not_a_url() throws IOException{
        crawler.getAllDocuments("wronglink");
    }

    @Test(expected=IOException.class)
    public void getAllDocuments_not_working_link() throws IOException{
        crawler.getAllDocuments("http://hello.world");
    }

    //Number of links that should be found
    @Test
    public void getAllDocuments_link_with_links_inside() throws IOException{
        List<Document> documents = crawler.getAllDocuments("http://i327618.hera.fhict.nl");
        Assert.assertEquals(documents.size(), 21);
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
        List<Document> documents = crawler.getAllDocuments("http://i327618.hera.fhict.nl");
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
                              new String[]{"Erich Gamma", "Richard Helm", "Ralph Johnson", "John Vlissides"},
                             "Prentice Hall", "978-0201633610");
        String link = crawler.findItem("http://i327618.hera.fhict.nl", book);
        Assert.assertEquals("http://i327618.hera.fhict.nl/details.php?id=101", link);
    }

    @Test
    public void findMovie() throws IOException{
        Movie movie = new Movie("Comedy", "Blu-ray", "1999",
                "Mike Judge", new String[]{"William Goldman"},
                new String[]{"Ron Livingston", "Jennifer Aniston", "David Herman", "Ajay Naidu", "Diedrich Bader", "Stephen Root"});
        String link = crawler.findItem("http://i327618.hera.fhict.nl", movie);
        Assert.assertEquals("http://i327618.hera.fhict.nl/details.php?id=202", link);
    }
}
