public class PrintedBook extends Book {
    private int numberOfPages;

    public PrintedBook(String title, String author, String genre, String isbn, int numberOfPages) {
        super(title, author, genre, isbn);
        this.numberOfPages = numberOfPages;
    }

    @Override
    public String getDetails() {
        return numberOfPages + " pages";
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }
}