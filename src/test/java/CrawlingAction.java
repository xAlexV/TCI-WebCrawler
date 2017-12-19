public class CrawlingAction {
    private String id;
    private String strategy;
    private int nrOfPages;
    private int seconds;
    private int searchDepth;

    public CrawlingAction(String id, String strategy, int nrOfPages, int seconds, int searchDepth) {
        this.id = id;
        this.strategy = strategy;
        this.nrOfPages = nrOfPages;
        this.seconds = seconds;
        this.searchDepth = searchDepth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {}

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {}

    public int getNrOfPages() {
        return nrOfPages;
    }

    public void setNrOfPages(int nrOfPages) {}

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {}

    public int getSearchDepth() {
        return searchDepth;
    }

    public void setSearchDepth(int searchDepth) {
        this.searchDepth = searchDepth;
    }
}
