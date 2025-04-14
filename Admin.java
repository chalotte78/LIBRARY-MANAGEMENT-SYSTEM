import java.util.*;

class Admin extends User {
    public Admin(String username, String password) {
        super(username, password, "Admin");
    }

    @Override
    public void showMenu(LibrarySystem system) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add User");
            System.out.println("2. Remove User");
            System.out.println("3. List Users");
            System.out.println("4. Logout");
            
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1": addUser(system); break;
                case "2": removeUser(system); break;
                case "3": listUsers(system); break;
                case "4": return;
                default: System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addUser(LibrarySystem system) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        if (system.getUsers().containsKey(username)) {
            System.out.println("User already exists!");
            return;
        }
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.println("Roles: 1. Admin, 2. Librarian, 3. Reader");
        System.out.print("Select role: ");
        String roleChoice = scanner.nextLine();
        String role;
        switch (roleChoice) {
            case "1": role = "Admin"; break;
            case "2": role = "Librarian"; break;
            case "3": role = "Reader"; break;
            default: System.out.println("Invalid role selection."); return;
        }
        User newUser;
        switch (role) {
            case "Admin": newUser = new Admin(username, password); break;
            case "Librarian": newUser = new Librarian(username, password); break;
            case "Reader": newUser = new Reader(username, password); break;
            default: return;
        } 
        system.getUsers().put(username, newUser);
        system.saveUsersToFile();
        System.out.println("User added successfully!");
    }

    private void removeUser(LibrarySystem system) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username to remove: ");
        String username = scanner.nextLine();
        
        if (!system.getUsers().containsKey(username)) {
            System.out.println("User not found!");
            return;
        }
        
        system.getUsers().remove(username);
        system.saveUsersToFile();
        System.out.println("User removed successfully!");
    }

    private void listUsers(LibrarySystem system) {
        System.out.println("\nList of Users:");
        System.out.printf("%-15s %-10s\n", "Username", "Role");
        System.out.println("---------------------");
        for (User user : system.getUsers().values()) {
            System.out.printf("%-15s %-10s\n", user.getUsername(), user.getRole());
        }
    }
}