public class CrawlingAction {
    private int id;
    private String strategy;
    private int nrOfPages;
    private int seconds;
    private int searchDepth;

    public CrawlingAction(int id, String strategy, int nrOfPages, int seconds, int searchDepth) {
        this.id = id;
        this.strategy = strategy;
        this.nrOfPages = nrOfPages;
        this.seconds = seconds;
        this.searchDepth = searchDepth;
    }

    public int getId() {
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
