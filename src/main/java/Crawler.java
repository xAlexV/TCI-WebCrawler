import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Crawler {
    private List<String> links;

    public Crawler(){
        links = new ArrayList<>();
    }

    public List<Document> getAllDocuments(String link) throws IOException, IllegalArgumentException {
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
                documents.addAll(this.getAllDocuments(link_found.attr("abs:href")));
            }
        }
        return documents;
    }

    public Map<String, String> documentToMap(Document document){
        Elements table = document.select("tbody");
        Elements cells = table.select("tr");
        Map<String, String> map = new HashMap<>();
        for(Element cell : cells){
            Elements row = cell.children();
            String key = row.first().childNode(0).toString();
            String value = row.last().childNode(0).toString();
            map.put(key, value);
        }
        if(map.containsKey("Category")) {
            return map;
        }
        return null;
    }

    public List<Item> mapToItems(List<Map<String, String>> maps){
        List<Item> items = new ArrayList<>();
        for(Map<String, String> map : maps){
            if(map.get("Category") == "Books"){
                String[] authors = map.get("Authors").split(", ");
                items.add(new Book(map.get("Genre"), map.get("Format"),
                                    map.get("Year"), authors,
                                    map.get("Publisher"), map.get("ISBN")));
            }
            if(map.get("Category") == "Music"){
                items.add(new Music(map.get("Genre"), map.get("Format"),
                                    map.get("Year"), map.get("Artist")));
            }
            if(map.get("Category") == "Movies"){
                String[] stars = map.get("Stars").split(", ");
                String[] writers = map.get("Writers").split(",");
                items.add(new Movie(map.get("Genre"), map.get("Format"),
                        map.get("Year"), map.get("Director"), writers, stars));
            }
        }
        return items;
    }

    public boolean checkLink(String link) throws IllegalArgumentException, IOException{
        if(!this.links.contains(link)){
            if(links.size() > 0) {
                // check if the host of the link is the same as the original host given by the user
                final URL url = new URL(link);
                final URL urlOriginal = new URL(links.get(0));
                final String originalHost = urlOriginal.getHost();
                final String urlHost = url.getHost();
                if (!url.getHost().equals(urlOriginal.getHost())){
                    return false;
                }
            }
            this.links.add(link);
            return true;
        }
        return false;
    }

    public String findItem(String link, Item item) throws IOException{
        String returnLink = "";
        if(this.checkLink(link)){
            Document document = Jsoup.connect(link).get();
            List<Map<String, String>> maps = new ArrayList<>();
            Map<String, String> map = this.documentToMap(document);
            if(map == null){
                // there is no object here, so look further
            }
            else {
                // there is object here so check if it's the
                // same the object we are looking for
            }
        }
        return returnLink;
    }
}
