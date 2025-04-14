import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class LibrarySystem {
    private Map<String, User> users;
    private Map<String, Book> books;
    private static final String USERS_FILE = "users.txt";
    private static final String BOOKS_FILE = "books.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";

    public LibrarySystem() {
        users = new HashMap<>();
        books = new HashMap<>();
        loadUsersFromFile();
        loadBooksFromFile();
        
        if (users.isEmpty()) {
            User admin = new Admin("admin", "admin123");
            users.put("admin", admin);
            saveUsersToFile();
        }
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\nLibrary Management System");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();
            
            if (choice.equals("1")) {
                login(scanner);
            } else if (choice.equals("2")) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void login(Scanner scanner) {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        if (users.containsKey(username)) {
            User user = users.get(username);
            if (user.getPassword().equals(password)) {
                System.out.println("\nWelcome, " + username + " (" + user.getRole() + ")");
                user.showMenu(this);
            } else {
                System.out.println("Invalid password!");
            }
        } else {
            System.out.println("User not found!");
        }
    }

    private void loadUsersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0];
                    String password = parts[1];
                    String role = parts[2];
                    
                    User user;
                    switch (role) {
                        case "Admin": user = new Admin(username, password); break;
                        case "Librarian": user = new Librarian(username, password); break;
                        case "Reader": user = new Reader(username, password); break;
                        default: continue;
                    }
                    users.put(username, user);
                }
            }
        } catch (IOException e) {
        }
    }

    public void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User user : users.values()) {
                writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getRole());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    private void loadBooksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String type = parts[0];
                    String title = parts[1];
                    String author = parts[2];
                    String genre = parts[3];
                    String isbn = parts[4];
                    boolean available = Boolean.parseBoolean(parts[5]);
                    Date dueDate = parts[6].equals("null") ? null : sdf.parse(parts[6]);
                    
                    Book book;
                    if (type.equals("Printed")) {
                        int pages = Integer.parseInt(parts[7]);
                        book = new PrintedBook(title, author, genre, isbn, pages);
                    } else {
                        String format = parts[7];
                        book = new Ebook(title, author, genre, isbn, format);
                    }
                    
                    book.setAvailable(available);
                    book.setDueDate(dueDate);
                    books.put(isbn, book);
                }
            }
        } catch (IOException e) {
        } catch (Exception e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }

    public void saveBooksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            for (Book book : books.values()) {
                String type = book instanceof PrintedBook ? "Printed" : "Ebook";
                String dueDateStr = book.getDueDate() == null ? "null" : sdf.format(book.getDueDate());
                
                if (book instanceof PrintedBook) {
                    PrintedBook pb = (PrintedBook) book;
                    writer.write(String.join(",",
                        type, book.getTitle(), book.getAuthor(), book.getGenre(), book.getIsbn(),
                        String.valueOf(book.isAvailable()), dueDateStr,
                        String.valueOf(pb.getNumberOfPages())
                    ));
                } else {
                    Ebook eb = (Ebook) book;
                    writer.write(String.join(",",
                        type, book.getTitle(), book.getAuthor(), book.getGenre(), book.getIsbn(),
                        String.valueOf(book.isAvailable()), dueDateStr,
                        eb.getFileFormat()
                    ));
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    public void saveTransaction(String transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE, true))) {
            writer.write(transaction);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }

    public Map<String, User> getUsers() { 
        return users; 
    }
    public Map<String, Book> getBooks() { 
        return books;
    }
}