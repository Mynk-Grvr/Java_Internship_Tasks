import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling file-based persistence operations for Student records.
 * Uses text file (students.txt) with CSV formatting.
 */
public class FileManager {
    private static final String FILE_NAME = "students.txt";

    /**
     * Appends a single student record to the file.
     */
    public static void saveStudent(Student student) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            pw.println(student.toFileString());
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to save student data to file: " + e.getMessage());
        }
    }

    /**
     * Reads all student records from the file and reconstructs Student objects.
     */
    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            // File does not exist yet (first-time run); return empty list
            return students;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Student s = Student.fromFileString(line);
                if (s != null) {
                    students.add(s);
                }
            }
        } catch (FileNotFoundException e) {
            // Handled gracefully
        } catch (IOException e) {
            System.err.println("[ERROR] Error reading student data from file: " + e.getMessage());
        }

        return students;
    }
}
