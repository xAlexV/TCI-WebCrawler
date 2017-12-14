import java.util.List;

public class Book extends Item {
    private List<String> authors;
    private String publisher;
    private String isbn;

    public Book(String genre, String format,
                String year, List<String> authors,
                String publisher, String isbn){
        super(genre, format, year);
        this.authors = authors;
        this.publisher = publisher;
        this.isbn = isbn;
    }
}
