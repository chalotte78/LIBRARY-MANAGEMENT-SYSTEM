import java.text.SimpleDateFormat;
import java.util.*;


public class Reader extends User {
    public Reader(String username, String password) {
        super(username, password, "Reader");
    }

    @Override
    public void showMenu(LibrarySystem system) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nReader Menu:");
            System.out.println("1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("3. View All Books");
            System.out.println("4. Logout");
            
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1": borrowBook(system); break;
                case "2": returnBook(system); break;
                case "3": viewAllBooks(system); break;
                case "4": return;
                default: System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void borrowBook(LibrarySystem system) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter ISBN of book to borrow: ");
        String isbn = scanner.nextLine();
        if (!system.getBooks().containsKey(isbn)) {
            System.out.println("Book not found!");
            return;
        }
        Book book = system.getBooks().get(isbn);
        if (book instanceof Ebook) {
            System.out.println("Ebooks cannot be borrowed!");
            return;
        }
        if (!book.isAvailable()) {
            System.out.println("Book is already borrowed!");
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 14);
        Date dueDate = calendar.getTime();
        book.setAvailable(false);
        book.setDueDate(dueDate);
        String transaction = username + "," + isbn + ",borrow," + new Date().getTime();
        system.saveTransaction(transaction);
        system.saveBooksToFile();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("Book borrowed successfully! Due date: " + sdf.format(dueDate));
    }

    private void returnBook(LibrarySystem system) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter ISBN of book to return: ");
        String isbn = scanner.nextLine();
        
        if (!system.getBooks().containsKey(isbn)) {
            System.out.println("Book not found!");
            return;
        }
        
        Book book = system.getBooks().get(isbn);
        if (book.isAvailable()) {
            System.out.println("This book is not currently borrowed!");
            return;
        }
        
        book.setAvailable(true);
        book.setDueDate(null);
        
        String transaction = username + "," + isbn + ",return," + new Date().getTime();
        system.saveTransaction(transaction);
        system.saveBooksToFile();
        
        System.out.println("Book returned successfully!");
    }

    private void viewAllBooks(LibrarySystem system) {
        System.out.println("\nAll Books:");
        System.out.printf("%-15s %-20s %-15s %-10s %-15s %-10s\n", 
            "ISBN", "Title", "Author", "Type", "Details", "Available");
        System.out.println("---------------------------------------------------------------------------");
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Book book : system.getBooks().values()) {
            String type = book instanceof PrintedBook ? "Printed" : "Ebook";
            String available = book.isAvailable() ? "Yes" : "No (" + sdf.format(book.getDueDate()) + ")";
            System.out.printf("%-15s %-20s %-15s %-10s %-15s %-10s\n", 
                book.getIsbn(), book.getTitle(), book.getAuthor(), 
                type, book.getDetails(), available);
        }
    }
}