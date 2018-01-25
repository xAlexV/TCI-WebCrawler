package crawler.service;

import java.io.IOException;

public class MockitoCrawler {

    Crawler crawler = new Crawler();

    public boolean itemExists(String link, String name, int depth) throws IOException{
        if(crawler.findItem(link,name,depth) != null){
        return true;}
        else{return false;}
    }
}