import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Student entity.
 * Handles database operations using JDBC PreparedStatements and ResultSets.
 */
public class StudentDAO {

    /**
     * Adds a new student record to the database.
     */
    public boolean addStudent(Student student) {
        String query = "INSERT INTO students (id, name, age) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, student.getId());
                ps.setString(2, student.getName());
                ps.setInt(3, student.getAge());
                int rows = ps.executeUpdate();
                return rows > 0;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Database error in addStudent: " + e.getMessage());
            return false;
        }
    }

    /**
     * Fetches all student records from the database.
     */
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String query = "SELECT id, name, age FROM students ORDER BY id ASC";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return list;
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(query)) {
                while (rs.next()) {
                    list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Database error in getAllStudents: " + e.getMessage());
        }
        return list;
    }

    /**
     * Searches for a student by ID.
     */
    public Student getStudentById(int id) {
        String query = "SELECT id, name, age FROM students WHERE id = ?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return null;
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Student(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("age")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Database error in getStudentById: " + e.getMessage());
        }
        return null;
    }

    /**
     * Updates an existing student's name and age by ID.
     */
    public boolean updateStudent(Student student) {
        String query = "UPDATE students SET name = ?, age = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, student.getName());
                ps.setInt(2, student.getAge());
                ps.setInt(3, student.getId());
                int rows = ps.executeUpdate();
                return rows > 0;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Database error in updateStudent: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a student record by ID.
     */
    public boolean deleteStudent(int id) {
        String query = "DELETE FROM students WHERE id = ?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return false;
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, id);
                int rows = ps.executeUpdate();
                return rows > 0;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Database error in deleteStudent: " + e.getMessage());
            return false;
        }
    }
}
