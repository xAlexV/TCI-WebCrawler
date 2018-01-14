package crawler.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import crawler.model.CrawlingAction;
import crawler.model.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * The crawler program implements and REST API service that provides
 * 3 functionalities:
 * crawl the whole website
 * find a specific item on the website
 * show data about a specific crawling action
 * <p></p>
 * @author Alexandru Vinerean
 * @author Daria Mikhailovskaia
 * @author Dmytro Bunin
 * @since 2017-12-05
 */
@Path("crawler")
public class Crawler {
    private List<String> links;
    public List<CrawlingAction> crawlingActions;
    public int pagesChecked = 0;
    public int depth = 0;

    public Crawler(){
        links = new ArrayList<>();
        crawlingActions = new ArrayList<>();
    }

    /**
     * This is the method to crawl the whole website. This method finds all the items that the website contains
     * @param link the link to the website
     * @param depth the depth where the search starts (usually 0)
     * @return a list of Document objects
     * @throws IOException
     * @throws IllegalArgumentException
     */
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

    /**
     * This is a GET method that uses the createActionFindAllDocuments function and returns the result in a json format
     * @return a list of Document objects
     * @throws IOException
     */
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Item> crawlWebsite() throws IOException {
        return createActionFindAllDocuments("http://i327618.hera.fhict.nl/");
    }

    /**
     * This is a GET method that uses the createActionFindItem method and returns the result in a json format
     * @param name the name of the searched item
     * @return an object of an Item type
     * @throws IOException
     */
    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Item getSpecificItem(@PathParam("name") String name) throws IOException {
        return createActionFindItem("http://i327618.hera.fhict.nl/", name);
    }


    /**
     * This is a GET method that uses the createCrawlingAction method and returns the result in a json format
     * @param id an id of the searched CrawlingAction
     * @return an object of type CrawlingAction
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CrawlingAction getCrawlingInfo(@PathParam("id") int id)
    {
        return getCrawlerAction(id);
    }


    /**
     * This method puts a Document object into a Map object
     * @param document a Document object
     * @return a Map object
     */
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

    /**
     * This method creates Items from Maps
     * @param maps a List of Map objects
     * @return a list of Item objects
     */
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

    /**
     * This method checks if the given link is valid
     * @param link a link to the website
     * @return a boolean (True or False)
     * @throws IllegalArgumentException
     * @throws IOException
     */
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

    /**
     * This method crawls the website in order to find a specific item.
     * @param link a link to the website
     * @param name the name of the searched item
     * @param depth the depth where the search starts (usually 0)
     * @return an object of a type Item
     * @throws IOException
     */
    public Item findItem(String link, String name, int depth) throws IOException{
        this.pagesChecked++;
        if(depth == this.depth){
            this.depth++;
        }
        if(this.checkLink(link)){
            Document document = Jsoup.connect(link).get();
            Elements elements = document.select("h1");
            Elements links_found = document.select("a[href]");
            if(elements.size() < 2){
                // there is no object here, so look further

                for(Element link_found : links_found) {
                    return this.findItem(link_found.attr("abs:href"), name, depth + 1);
                }
            }
            else {
                // there is object here so check if it's the
                // same the object we are looking for
                Boolean nameIsFound = false;
                for(Element e : elements){
                    if(e.children().size() >= 0)
                        for(Element child : e.children()){
                            System.out.println((child));
                            if(child.childNodes().size()>=0){
                                Node text = child.childNodes().get(0);
                                if(text.toString().equals(name)){
                                    nameIsFound = true;
                                }
                            }
                        }
                }
                if(nameIsFound) {
                    List<Map<String, String>> maps = new ArrayList<>();
                    maps.add(this.documentToMap(document));
                    return this.mapToItems(maps).get(0);
                }
                for(Element link_found : links_found) {
                    return this.findItem(link_found.attr("abs:href"), name, depth + 1);
                }
            }
        }
        return null;
    }

    /**
     * This method creates an action to find an item
     * @param link a link to the website
     * @param name a name of the searched item
     * @return an object of a type Item
     * @throws IOException
     */
    public Item createActionFindItem(String link, String name) throws IOException {
        this.depth = 0;
        this.pagesChecked = 0;
        long startTime = System.currentTimeMillis();
        Item Item = this.findItem(link, name, 0);
        long endTime = System.currentTimeMillis();
        CrawlingAction crawlingAction = new CrawlingAction(this.crawlingActions.size(), "DFS",
                this.pagesChecked, (int) ((endTime - startTime) / 1000),
                this.depth);
        this.crawlingActions.add(crawlingAction);
        return Item;
    }


    /**
     * This method creates an action to crawl the whole website
     * @param link a link to the website
     * @return a list of Item objects
     * @throws IOException
     */
    public List<Item> createActionFindAllDocuments(String link) throws IOException {
        this.depth = 0;
        this.pagesChecked = 0;
        long startTime = System.currentTimeMillis();
        List<Document> documents = this.getAllDocuments(link, 0);
        long endTime = System.currentTimeMillis();
        CrawlingAction crawlingAction = new CrawlingAction(this.crawlingActions.size(), "DFS",
                this.pagesChecked, (int) ((endTime - startTime) / 1000),
                this.depth);
        this.crawlingActions.add(crawlingAction);
        List<Map<String, String>> maps = new ArrayList<>();
        for(Document d : documents){
            maps.add(this.documentToMap(d));
        }
        List<Item> items = this.mapToItems(maps);
        return items; // Serialize this to json
    }

    /**
     * This method finds a specific CrawlingAction in the list of CrawlingAction objects based on its id
     * @param ID an id of the searched CrawlingAction
     * @return an object of type CrawlingAction
     */
    public CrawlingAction getCrawlerAction(int ID){
        for(CrawlingAction ca : this.crawlingActions){
            if(ca.getId() == ID){
                return ca;
            }
        }
        return null;
    }
}

