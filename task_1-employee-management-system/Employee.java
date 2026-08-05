/**
 * Employee Data Model Class
 * Represents an individual employee record with ID, Name, and Department.
 */
public class Employee {
    private int id;
    private String name;
    private String department;

    // Constructor to initialize Employee attributes
    public Employee(int id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return String.format("ID: %-6d | Name: %-20s | Department: %-15s", id, name, department);
    }
}
