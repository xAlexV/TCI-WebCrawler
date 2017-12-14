import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
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
    public void getHTML_not_a_url() throws IOException{
        crawler.getHTML("wronglink");
    }

    @Test(expected=IOException.class)
    public void getHTML_not_working_link() throws IOException{
        crawler.getHTML("http://hello.world");
    }

    @Test
    public void getHTML_link_with_links_inside() throws IOException{
        List<Document> documents = crawler.getHTML("http://i327618.hera.fhict.nl");
        Assert.assertEquals(documents.size(), 21);
    }

}
