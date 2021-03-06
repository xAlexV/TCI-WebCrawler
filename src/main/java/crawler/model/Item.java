package crawler.model;

/**
 * This is a data class. An instance of this class contains information about an item
 * that can be found on the website
 */
public class Item {
    private String genre;
    private String format;
    private String year;

    public Item(String genre, String format, String year){
        this.genre = genre;
        this.format = format;
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    /**
     * This method compares two objects of a type Item
     * @param obj an object to be compared with
     * @return a boolean (True or False)
     */
    @Override
    public boolean equals(Object obj){
        Item item = (Item) obj;
        boolean status = false;
        if(this.getGenre().equalsIgnoreCase(item.getGenre())
                && this.getFormat().equals(item.getFormat())
                && this.getYear().equals(item.getYear())){
            status = true;
        }
        return status;
    }

    @Override
    public int hashCode(){
        return 0;
    }
}

