public class Ebook extends Book {
    private String fileFormat;

    public Ebook(String title, String author, String genre, String isbn, String fileFormat) {
        super(title, author, genre, isbn);
        this.fileFormat = fileFormat;
    }

    @Override
    public String getDetails() {
        return fileFormat + " format";
    }

    public String getFileFormat() {
        return fileFormat;
    }
}