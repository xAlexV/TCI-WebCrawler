import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

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

    @Test
    public void getAllDocuments_link_with_links_inside() throws IOException{
        List<Document> documents = crawler.getAllDocuments("http://i327618.hera.fhict.nl");
        Assert.assertEquals(documents.size(), 21);
    }

    @Test
    public void getBooks_book_from_link() throws IOException{
        Document document = Jsoup.connect("http://i327618.hera.fhict.nl/details.php?id=101").get();
        crawler.getBooks(document);
    }
}
