import java.util.List;

public class Movie extends Item {
    private String director;
    private String[] writers;
    private String[] stars;

    public Movie(String genre, String format,
                 String year, String director,
                 String[] writers, String[] stars){
        super(genre, format, year);
        this.director = director;
        this.writers = writers;
        this.stars = stars;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String[] getWriters() {
        return writers;
    }

    public void setWriters(String[] writers) {
        this.writers = writers;
    }

    public String[] getStars() {
        return stars;
    }

    public void setStars(String[] stars) {
        this.stars = stars;
    }
}
