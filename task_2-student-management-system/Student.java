/**
 * Student Data Model Class
 * Represents an individual student record.
 * Supports CSV data serialization for file storage.
 */
public class Student {
    private int id;
    private String name;
    private int age;

    public Student(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    /**
     * Converts Student object to CSV string format for file storage.
     */
    public String toFileString() {
        return id + "," + name + "," + age;
    }

    /**
     * Reconstructs a Student object from a CSV file line.
     */
    public static Student fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split(",");
        if (parts.length < 3) {
            return null;
        }
        try {
            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            int age = Integer.parseInt(parts[2].trim());
            return new Student(id, name, age);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Age: " + age;
    }
}
