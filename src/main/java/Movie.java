import java.util.List;

public class Movie extends Item {
    private String director;
    private List<String> writers;
    private List<String> stars;

    public Movie(String genre, String format,
                 String year, String director,
                 List<String> writers, List<String> stars){
        super(genre, format, year);
        this.director = director;
        this.writers = writers;
        this.stars = stars;
    }
}
