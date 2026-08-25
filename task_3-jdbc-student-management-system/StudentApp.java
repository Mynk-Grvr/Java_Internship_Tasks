import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Database-Driven Student Management System (Task 3 - JDBC).
 */
public class StudentApp {
    private static final StudentDAO studentDAO = new StudentDAO();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=================================================");
        System.out.println("  STUDENT MANAGEMENT SYSTEM (JDBC - DATABASE)    ");
        System.out.println("=================================================");

        while (true) {
            System.out.println("\n---------------- MAIN MENU ----------------");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student by ID");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");
            System.out.print("Enter your choice (1-6): ");

            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine(); // Clear newline buffer
            } catch (InputMismatchException e) {
                System.out.println("\n[ERROR] Invalid input! Please enter a number between 1 and 6.");
                sc.nextLine();
                continue;
            }

            switch (choice) {
                case 1 -> addStudent(sc);
                case 2 -> viewStudents();
                case 3 -> searchStudent(sc);
                case 4 -> updateStudent(sc);
                case 5 -> deleteStudent(sc);
                case 6 -> {
                    System.out.println("\nExiting Database Student Management System. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("\n[ERROR] Invalid option! Choice must be between 1 and 6.");
            }
        }
    }

    private static void addStudent(Scanner sc) {
        System.out.println("\n--- Add New Student ---");
        int id = readInt(sc, "Enter ID: ");

        // Check if ID already exists
        if (studentDAO.getStudentById(id) != null) {
            System.out.println("[ERROR] Student with ID " + id + " already exists in database.");
            return;
        }

        System.out.print("Enter Name: ");
        String name = sc.nextLine().trim();
        int age = readInt(sc, "Enter Age: ");

        if (name.isEmpty()) {
            System.out.println("[ERROR] Student name cannot be empty.");
            return;
        }

        boolean success = studentDAO.addStudent(new Student(id, name, age));
        if (success) {
            System.out.println("[SUCCESS] Student added successfully to database.");
        } else {
            System.out.println("[ERROR] Failed to insert student record.");
        }
    }

    private static void viewStudents() {
        System.out.println("\n--- All Student Records ---");
        List<Student> students = studentDAO.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No student records found in database.");
            return;
        }

        System.out.println("-------------------------------------------------------");
        for (Student s : students) {
            System.out.println(s);
        }
        System.out.println("-------------------------------------------------------");
        System.out.println("Total Database Records: " + students.size());
    }

    private static void searchStudent(Scanner sc) {
        System.out.println("\n--- Search Student ---");
        int id = readInt(sc, "Enter Student ID to Search: ");
        Student s = studentDAO.getStudentById(id);

        if (s != null) {
            System.out.println("[FOUND] Record Details: " + s);
        } else {
            System.out.println("[ERROR] Student with ID " + id + " not found in database.");
        }
    }

    private static void updateStudent(Scanner sc) {
        System.out.println("\n--- Update Student ---");
        int id = readInt(sc, "Enter Student ID to Update: ");
        Student existing = studentDAO.getStudentById(id);

        if (existing == null) {
            System.out.println("[ERROR] Student with ID " + id + " not found.");
            return;
        }

        System.out.println("Current Details: " + existing);
        System.out.print("Enter New Name: ");
        String newName = sc.nextLine().trim();
        int newAge = readInt(sc, "Enter New Age: ");

        if (newName.isEmpty()) {
            System.out.println("[ERROR] Name cannot be empty.");
            return;
        }

        boolean updated = studentDAO.updateStudent(new Student(id, newName, newAge));
        if (updated) {
            System.out.println("[SUCCESS] Student record updated successfully.");
        } else {
            System.out.println("[ERROR] Failed to update record.");
        }
    }

    private static void deleteStudent(Scanner sc) {
        System.out.println("\n--- Delete Student ---");
        int id = readInt(sc, "Enter Student ID to Delete: ");
        Student existing = studentDAO.getStudentById(id);

        if (existing == null) {
            System.out.println("[ERROR] Student with ID " + id + " not found.");
            return;
        }

        System.out.print("Are you sure you want to delete student " + existing.getName() + "? (y/n): ");
        String confirm = sc.nextLine().trim().toLowerCase();
        if (confirm.equals("y") || confirm.equals("yes")) {
            boolean deleted = studentDAO.deleteStudent(id);
            if (deleted) {
                System.out.println("[SUCCESS] Student record deleted from database.");
            } else {
                System.out.println("[ERROR] Failed to delete student record.");
            }
        } else {
            System.out.println("[INFO] Deletion canceled.");
        }
    }

    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = sc.nextInt();
                sc.nextLine();
                return val;
            } catch (InputMismatchException e) {
                System.out.println("[ERROR] Input must be an integer. Try again.");
                sc.nextLine();
            }
        }
    }
}
