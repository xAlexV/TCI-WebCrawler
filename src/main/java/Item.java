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

    @Override
    public boolean equals(Object obj){
        Item item = (Item) obj;
        boolean status = false;
        if(this.getGenre().equalsIgnoreCase(item.getGenre())
                && this.getFormat() == item.getFormat()
                && this.getYear() == item.getYear()){
            status = true;
        }
        return status;
    }
}
