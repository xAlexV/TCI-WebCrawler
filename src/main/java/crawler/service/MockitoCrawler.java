package crawler.service;

import java.io.IOException;


//we added this class to be able to have a mock and a system under testing
//this class has one method that calls a method on a crawler
public class MockitoCrawler {

    Crawler crawler = new Crawler();

    public boolean itemExists(String link, String name, int depth) throws IOException{
       return crawler.itemExists(link, name, depth);
    }
}