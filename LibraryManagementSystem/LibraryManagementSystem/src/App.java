import java.sql.*;
import java.util.Scanner;

public class App {
    static final String DB_URL = "jdbc:mysql://localhost:3306/library_db";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "Pavan@2002"; // <-- REPLACE THIS

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✅ Connected to the database!");

            // Login
            System.out.print("👤 Enter username: ");
            String username = scanner.nextLine();

            System.out.print("🔑 Enter password: ");
            String password = scanner.nextLine();

            boolean isAdmin = false;

            if (username.equals("admin") && password.equals("admin123")) {
                isAdmin = true;
                System.out.println("✅ Admin login successful.");
            } else {
                System.out.println("✅ User login successful.");
            }

            while (true) {
                System.out.println("\n📚 Library Menu:");
                System.out.println("1. View all books");
                if (isAdmin) {
                    System.out.println("2. Add a new book");
                    System.out.println("3. Exit");
                } else {
                    System.out.println("2. Exit");
                }

                System.out.print("Choose an option: ");
                int choice = Integer.parseInt(scanner.nextLine());

                if (choice == 1) {
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM books");

                    System.out.println("\n📖 Book List:");
                    while (rs.next()) {
                        System.out.println("ID: " + rs.getInt("id") +
                                           " | Title: " + rs.getString("title") +
                                           " | Author: " + rs.getString("author") +
                                           " | Available: " + rs.getBoolean("available"));
                    }
                    rs.close();
                    stmt.close();

                } else if (choice == 2 && isAdmin) {
                    System.out.print("Enter Book ID (unique): ");
                    int id = Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter book title: ");
                    String title = scanner.nextLine();

                    System.out.print("Enter author name: ");
                    String author = scanner.nextLine();

                    System.out.print("Is book available? (true/false): ");
                    boolean available = Boolean.parseBoolean(scanner.nextLine());

                    String sql = "INSERT INTO books (id, title, author, available) VALUES (?, ?, ?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, id);
                    pstmt.setString(2, title);
                    pstmt.setString(3, author);
                    pstmt.setBoolean(4, available);

                    int rows = pstmt.executeUpdate();
                    if (rows > 0) {
                        System.out.println("✅ Book added successfully!");
                    }

                    pstmt.close();

                } else if ((isAdmin && choice == 3) || (!isAdmin && choice == 2)) {
                    System.out.println("👋 Exiting...");
                    break;
                } else {
                    System.out.println("❌ Invalid option. Try again.");
                }
            }

            conn.close();
            scanner.close();

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}
