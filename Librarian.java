import java.util.*;

public class Librarian extends User {
    public Librarian(String username, String password) {
        super(username, password, "Librarian");
    }

    @Override
    public void showMenu(LibrarySystem system) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nLibrarian Menu:");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Book");
            System.out.println("3. List Available Books");
            System.out.println("4. Logout");
            
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1": addBook(system); break;
                case "2": removeBook(system); break;
                case "3": listAvailableBooks(system); break;
                case "4": return;
                default: System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addBook(LibrarySystem system) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Book Types: 1. Printed Book, 2. Ebook");
        System.out.print("Select book type: ");
        String typeChoice = scanner.nextLine();
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();
        Book book;
        switch (typeChoice) {
            case "1":
                System.out.print("Enter number of pages: ");
                int pages = Integer.parseInt(scanner.nextLine());
                book = new PrintedBook(title, author, genre, isbn, pages);
                break;
            case "2":
                System.out.print("Enter file format: ");
                String format = scanner.nextLine();
                book = new Ebook(title, author, genre, isbn, format);
                break;
            default:
                System.out.println("Invalid book type.");
                return;
        }
        system.getBooks().put(isbn, book);
        system.saveBooksToFile();
        System.out.println("Book added successfully!");
    }

    private void removeBook(LibrarySystem system) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter ISBN of book to remove: ");
        String isbn = scanner.nextLine();
        
        if (!system.getBooks().containsKey(isbn)) {
            System.out.println("Book not found!");
            return;
        }
        
        system.getBooks().remove(isbn);
        system.saveBooksToFile();
        System.out.println("Book removed successfully!");
    }

    private void listAvailableBooks(LibrarySystem system) {
        System.out.println("\nAvailable Books:");
        System.out.printf("%-15s %-20s %-15s %-10s %-15s\n", 
            "ISBN", "Title", "Author", "Type", "Details");
        System.out.println("----------------------------------------------------------------");
        
        for (Book book : system.getBooks().values()) {
            if (book.isAvailable()) {
                String type = book instanceof PrintedBook ? "Printed" : "Ebook";
                System.out.printf("%-15s %-20s %-15s %-10s %-15s\n", 
                    book.getIsbn(), book.getTitle(), book.getAuthor(), 
                    type, book.getDetails());
            }
        }
    }
}