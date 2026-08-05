import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Console-based Employee Management System Application.
 * Mimics basic CRUD operations (Create, Read, Update) using Java Collections.
 */
public class EmployeeApp {
    static ArrayList<Employee> employees = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   WELCOME TO EMPLOYEE MANAGEMENT SYSTEM (EMS)  ");
        System.out.println("=================================================");

        while (true) {
            System.out.println("\n---------------- MAIN MENU ----------------");
            System.out.println("1. Add Employee");
            System.out.println("2. View Employees");
            System.out.println("3. Search Employee");
            System.out.println("4. Update Department");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");

            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine(); // Clear newline buffer
            } catch (InputMismatchException e) {
                System.out.println("\n[ERROR] Invalid input! Please enter a number between 1 and 5.");
                sc.nextLine(); // Clear bad input buffer
                continue;
            }

            switch (choice) {
                case 1 -> addEmployee();
                case 2 -> viewEmployees();
                case 3 -> searchEmployee();
                case 4 -> updateDepartment();
                case 5 -> {
                    System.out.println("\nExiting Employee Management System. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("\n[ERROR] Invalid option! Choice must be between 1 and 5.");
            }
        }
    }

    /**
     * Option 1: Add a new employee record.
     */
    static void addEmployee() {
        System.out.println("\n--- Add New Employee ---");
        int id;
        while (true) {
            System.out.print("ID: ");
            try {
                id = sc.nextInt();
                sc.nextLine(); // Consume newline
                break;
            } catch (InputMismatchException e) {
                System.out.println("[ERROR] ID must be an integer. Try again.");
                sc.nextLine();
            }
        }

        // Check if ID already exists
        for (Employee e : employees) {
            if (e.getId() == id) {
                System.out.println("[ERROR] Employee with ID " + id + " already exists. Operation aborted.");
                return;
            }
        }

        System.out.print("Name: ");
        String name = sc.nextLine().trim();

        System.out.print("Department: ");
        String dept = sc.nextLine().trim();

        if (name.isEmpty() || dept.isEmpty()) {
            System.out.println("[ERROR] Name and Department cannot be empty. Employee not added.");
            return;
        }

        employees.add(new Employee(id, name, dept));
        System.out.println("[SUCCESS] Employee added successfully.");
    }

    /**
     * Option 2: Display all employee records.
     */
    static void viewEmployees() {
        System.out.println("\n--- Employee List ---");
        if (employees.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        System.out.println("---------------------------------------------------------------");
        for (Employee e : employees) {
            System.out.println(e.getId() + " | " + e.getName() + " | " + e.getDepartment());
        }
        System.out.println("---------------------------------------------------------------");
        System.out.println("Total Employees: " + employees.size());
    }

    /**
     * Option 3: Search for an employee by ID.
     */
    static void searchEmployee() {
        System.out.println("\n--- Search Employee ---");
        if (employees.isEmpty()) {
            System.out.println("No records in system to search.");
            return;
        }

        System.out.print("Enter ID: ");
        int id;
        try {
            id = sc.nextInt();
            sc.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("[ERROR] ID must be an integer.");
            sc.nextLine();
            return;
        }

        for (Employee e : employees) {
            if (e.getId() == id) {
                System.out.println("Found: " + e.getName() + " (" + e.getDepartment() + ")");
                return;
            }
        }

        System.out.println("Employee not found.");
    }

    /**
     * Option 4: Update an employee's department by ID.
     */
    static void updateDepartment() {
        System.out.println("\n--- Update Department ---");
        if (employees.isEmpty()) {
            System.out.println("No records in system to update.");
            return;
        }

        System.out.print("Employee ID: ");
        int id;
        try {
            id = sc.nextInt();
            sc.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("[ERROR] ID must be an integer.");
            sc.nextLine();
            return;
        }

        for (Employee e : employees) {
            if (e.getId() == id) {
                System.out.print("New Department: ");
                String newDept = sc.nextLine().trim();
                if (newDept.isEmpty()) {
                    System.out.println("[ERROR] Department name cannot be empty.");
                    return;
                }
                e.setDepartment(newDept);
                System.out.println("Department updated.");
                return;
            }
        }

        System.out.println("Employee not found.");
    }
}
