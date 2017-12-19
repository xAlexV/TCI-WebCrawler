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

    @Override
    public boolean equals(Object obj){
        Music music = (Music) obj;
        boolean status = false;
        if(super.equals(obj)
                && this.getArtist().equals(music.getArtist())){
            status = true;
        }
        return status;
    }
}
