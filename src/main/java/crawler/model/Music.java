package crawler.model;

/**
 * This class inherits from the Item class and represents an item of a Music type.
 */
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

    /**
     * This method compares two objects of a type Item
     * @param obj an object to be compared with
     * @return a boolean (True or False)
     */
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

    public boolean sameArtist(String artist){
        if(artist.equals(getArtist())){
            return true;
        }
        else{return false;}
    }
}

