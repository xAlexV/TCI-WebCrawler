import crawler.service.MockitoCrawler;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.testng.annotations.Test;
import java.io.IOException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class MockitoCrawlerTest {

    @Mock
    private MockitoCrawler mockitoCrawler = mock(MockitoCrawler.class);

    @Test
    public void mockValidLink() throws IOException{
        assertFalse("Book exists", mockitoCrawler.itemExists("http://i327618.hera.fhict.nl/details.php?id=101",
                "A Design Patterns: Elements of Reusable Object-Oriented Software",
                0));
        when(mockitoCrawler.itemExists("http://i327618.hera.fhict.nl/details.php?id=101",
                "A Design Patterns: Elements of Reusable Object-Oriented Software",
                0)).thenReturn(true);
        assertTrue("Music exists",
                mockitoCrawler.itemExists("http://i327618.hera.fhict.nl/details.php?id=101",
                        "A Design Patterns: Elements of Reusable Object-Oriented Software",
                        0));
    }

    @Test
    public void mockFakeLink() throws IOException{
        assertFalse("Item does not exist", mockitoCrawler.itemExists("fontys.nl", "fntys",0));
        when(mockitoCrawler.itemExists("fontys.nl", "fntys",0)).thenReturn(true);
        assertTrue("Item exists",
                mockitoCrawler.itemExists("fontys.nl", "fntys",0));
    }

    @Test
    public void mockVerification() throws  IOException{
        mockitoCrawler.itemExists("blabla", "blabla name", 1);
        verify(mockitoCrawler).itemExists("blabla", "blabla name", 1);
    }
}
