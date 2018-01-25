import crawler.service.Crawler;
import crawler.service.MockitoCrawler;
import org.mockito.Mock;
import org.testng.annotations.Test;
import java.io.IOException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertTrue;

public class MockitoCrawlerTest {

    MockitoCrawler crawlerMockito;

    @Mock
    private Crawler crawler = mock(Crawler.class);

    @Test
    public void mockValidLink() throws IOException{
        //the crawlerMockito is our system under testing and the crawler is a mock
        crawlerMockito = new MockitoCrawler();

        //using a matcher on a mock
        when(crawler.itemExists("http://i327618.hera.fhict.nl/details.php?id=101",
                "A Design Patterns: Elements of Reusable Object-Oriented Software",
                0)).thenReturn(true);

        //calling the method on the system under testing
        assertTrue("Music exists",
                crawlerMockito.itemExists("http://i327618.hera.fhict.nl/details.php?id=101",
                        "A Design Patterns: Elements of Reusable Object-Oriented Software",
                        0));
    }
}
