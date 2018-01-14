package crawler.model;

import java.util.Arrays;
import java.util.List;


/**
 * This class inherits from the Item class and represents an item of a Movie type.
 */
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

    /**
     * This method compares two objects of a type Item
     * @param obj an object to be compared with
     * @return a boolean (True or False)
     */
    @Override
    public boolean equals(Object obj){
        Movie movie = (Movie) obj;
        boolean status = false;
        if(super.equals(obj)
                && Arrays.equals(this.getWriters(), movie.getWriters())
                && Arrays.equals(this.getStars(), movie.getStars())
                && this.getDirector().equals(movie.getDirector())){
            status = true;
        }
        return status;
    }
}

