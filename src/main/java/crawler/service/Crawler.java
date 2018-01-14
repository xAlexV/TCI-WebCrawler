package crawler.service;

import crawler.model.CrawlingAction;
import crawler.model.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("crawler")
@Singleton
public class Crawler {
    private List<String> links;
    public List<CrawlingAction> crawlingActions;
    public int pagesChecked = 0;
    public int depth = 0;

    public Crawler(){
        links = new ArrayList<>();
        crawlingActions = new ArrayList<>();
    }

    public List<Document> getAllDocuments(String link, int depth) throws IOException, IllegalArgumentException {
        this.pagesChecked++;
        if(this.depth == depth){
            depth++;
        }
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
                documents.addAll(this.getAllDocuments(link_found.attr("abs:href"), depth + 1));
            }
        }
        return documents;
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Document> crawlWebsite() throws IOException {
        List<Document> documents = getAllDocuments("http://i327618.hera.fhict.nl/", 0);
        return documents;
    }

    @GET
    @Path("hi")
    @Produces(MediaType.TEXT_PLAIN)
    public String trythis() {
        return "hi!";
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
            if(map.get("Category").equals("Books")){
                String[] authors = map.get("Authors").split(", ");
                items.add(new Book(map.get("Genre"), map.get("Format"),
                        map.get("Year"), authors,
                        map.get("Publisher"), map.get("ISBN")));
            }
            if(map.get("Category").equals("Music")){
                items.add(new Music(map.get("Genre"), map.get("Format"),
                        map.get("Year"), map.get("Artist")));
            }
            if(map.get("Category").equals("Movies")){
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

    public String findItem(String link, Item item, int depth) throws IOException{
        this.pagesChecked++;
        if(depth == this.depth){
            this.depth++;
        }
        String returnLink = "";
        if(this.checkLink(link)){
            Document document = Jsoup.connect(link).get();
            Map<String, String> map = this.documentToMap(document);
            if(map == null){
                // there is no object here, so look further
                Elements links_found = document.select("a[href]");
                for(Element link_found : links_found) {
                    returnLink += this.findItem(link_found.attr("abs:href"), item, depth + 1);
                    if(!returnLink.equals("")){
                        return returnLink;
                    }
                }
            }
            else {
                // there is object here so check if it's the
                // same the object we are looking for
                List<Map<String, String>> maps = new ArrayList<>();
                maps.add(map);
                List<Item> foundItems = this.mapToItems(maps);
                if(item.getClass() == foundItems.get(0).getClass()) {
                    if (item.equals(foundItems.get(0))) {
                        return link;
                    }
                }
                Elements links_found = document.select("a[href]");
                for(Element link_found : links_found) {
                    returnLink += this.findItem(link_found.attr("abs:href"), item, depth + 1);
                    if (!returnLink.equals("")) {
                        return returnLink;
                    }
                }
            }
        }
        return returnLink;
    }

    public CrawlingAction createActionFindItem(String link, Item item) throws IOException {
        this.depth = 0;
        this.pagesChecked = 0;
        long startTime = System.currentTimeMillis();
        //String foundLink = this.findItem(link, item, 0);
        long endTime = System.currentTimeMillis();
        CrawlingAction crawlingAction = new CrawlingAction(this.crawlingActions.size(), "DFS",
                this.pagesChecked, (int) ((endTime - startTime) / 1000),
                this.depth);
        this.crawlingActions.add(crawlingAction);
        return crawlingAction;
    }
}

