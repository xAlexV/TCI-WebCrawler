import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Crawler {
    private List<String> links;

    public Crawler(){
        links = new ArrayList<>();
    }

    public List<Document> getHTML(String link) throws IOException, IllegalArgumentException {
        List<Document> documents = new ArrayList<>();
        if(!links.contains(link)) {
            // check if list is empty
            if(links.size() > 0) {
                // check if the host of the link is the same as the original host given by the user
                final URL url = new URL(link);
                final URL urlOriginal = new URL(links.get(0));
                final String originalHost = urlOriginal.getHost();
                final String urlHost = url.getHost();
                if (!url.getHost().equals(urlOriginal.getHost())){
                    return new ArrayList<>();
                }
            }
            links.add(link);
            Document document = Jsoup.connect(link).get();
            documents.add(document);
            // Get links that were found on the page
            Elements links_found = document.select("a[href]");
            // Recursively call the method to get all data
            for(Element link_found : links_found) {
                documents.addAll(this.getHTML(link_found.attr("abs:href")));
            }
        }
        return documents;
    }


}
