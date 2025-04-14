import java.util.*;
abstract class Book {
    protected String title;
    protected String author;
    protected String genre;
    protected String isbn;
    protected boolean available;
    protected Date dueDate;

    public Book(String title, String author, String genre, String isbn) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isbn = isbn;
        this.available = true;
        this.dueDate = null;
    }

    public abstract String getDetails();

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public String getIsbn() { return isbn; }
    public boolean isAvailable() { return available; }
    public Date getDueDate() { return dueDate; }
    
    public void setAvailable(boolean available) { this.available = available; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
}