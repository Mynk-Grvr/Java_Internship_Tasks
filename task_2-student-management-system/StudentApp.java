import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * File-Based Persistent Student Management System Application.
 */
public class StudentApp {
    private static List<Student> students;

    public static void main(String[] args) {
        // Load existing records from students.txt at startup
        students = FileManager.loadStudents();

        Scanner sc = new Scanner(System.in);

        System.out.println("=================================================");
        System.out.println("   STUDENT MANAGEMENT SYSTEM (PERSISTENT FILE)   ");
        System.out.println("=================================================");
        System.out.println("[INFO] Loaded " + students.size() + " student record(s) from 'students.txt'.");

        while (true) {
            System.out.println("\n---------------- MAIN MENU ----------------");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Search Student");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (1-4): ");

            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine(); // Consume newline
            } catch (InputMismatchException e) {
                System.out.println("\n[ERROR] Invalid input! Please enter a number between 1 and 4.");
                sc.nextLine();
                continue;
            }

            switch (choice) {
                case 1 -> addStudent(sc);
                case 2 -> viewStudents();
                case 3 -> searchStudent(sc);
                case 4 -> {
                    System.out.println("\nExiting Student Management System. All data persisted to 'students.txt'. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("\n[ERROR] Invalid option! Choice must be between 1 and 4.");
            }
        }
    }

    /**
     * Option 1: Add a new student and persist to file.
     */
    private static void addStudent(Scanner sc) {
        System.out.println("\n--- Add New Student ---");
        
        int id;
        while (true) {
            System.out.print("Enter ID: ");
            try {
                id = sc.nextInt();
                sc.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("[ERROR] ID must be an integer. Try again.");
                sc.nextLine();
            }
        }

        // Duplicate check
        for (Student s : students) {
            if (s.getId() == id) {
                System.out.println("[ERROR] Student with ID " + id + " already exists. Action canceled.");
                return;
            }
        }

        System.out.print("Enter Name: ");
        String name = sc.nextLine().trim();

        int age;
        while (true) {
            System.out.print("Enter Age: ");
            try {
                age = sc.nextInt();
                sc.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("[ERROR] Age must be an integer. Try again.");
                sc.nextLine();
            }
        }

        if (name.isEmpty()) {
            System.out.println("[ERROR] Student name cannot be empty.");
            return;
        }

        Student student = new Student(id, name, age);
        students.add(student);

        // Save immediately to persistent storage
        FileManager.saveStudent(student);

        System.out.println("[SUCCESS] Student saved successfully.");
    }

    /**
     * Option 2: View all students loaded in memory.
     */
    private static void viewStudents() {
        System.out.println("\n--- Student List ---");
        if (students.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }

        System.out.println("---------------------------------------------------");
        for (Student s : students) {
            System.out.println(s);
        }
        System.out.println("---------------------------------------------------");
        System.out.println("Total Records: " + students.size());
    }

    /**
     * Option 3: Search for student by ID.
     */
    private static void searchStudent(Scanner sc) {
        System.out.println("\n--- Search Student ---");
        if (students.isEmpty()) {
            System.out.println("No records available to search.");
            return;
        }

        System.out.print("Enter ID to search: ");
        int id;
        try {
            id = sc.nextInt();
            sc.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("[ERROR] ID must be an integer.");
            sc.nextLine();
            return;
        }

        for (Student s : students) {
            if (s.getId() == id) {
                System.out.println("Student Found: " + s);
                return;
            }
        }

        System.out.println("Student not found.");
    }
}
