public class Music extends Item {
    private String artist;

    public Music(String genre, String format,
                 String year, String artist){
        super(genre, format, year);
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
