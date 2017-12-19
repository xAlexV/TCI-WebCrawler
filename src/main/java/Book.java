import java.util.List;
import java.util.Arrays;

public class Book extends Item {
    private String[] authors;
    private String publisher;
    private String isbn;

    public Book(String genre, String format,
                String year, String[] authors,
                String publisher, String isbn){
        super(genre, format, year);
        this.authors = authors;
        this.publisher = publisher;
        this.isbn = isbn;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public boolean equals(Object obj){
        Book book = (Book) obj;
        boolean status = false;
        if(super.equals(obj)
                && Arrays.equals(this.getAuthors(), book.getAuthors())
                && this.getPublisher().equals(book.getPublisher())
                && this.getIsbn().equals(book.getIsbn())){
            status = true;
        }
        return status;
    }
}
