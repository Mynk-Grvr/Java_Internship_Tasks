# Persistent Student Management System (Core Java - Task 2)

A production-style console application in Core Java that implements **file-based data persistence**. Student records (`id`, `name`, `age`) are saved to a plain text file (`students.txt`) as comma-separated values (CSV) and automatically loaded into memory upon program boot.

---

## 📌 Key Objectives & Features

1. **File-Based Persistence**: Permanent data storage using Java File I/O (`FileWriter`, `FileReader`, `BufferedReader`).
2. **Automatic Data Loading**: Reads existing records from `students.txt` on application startup.
3. **Interactive Menu Operations**:
   - **Add Student**: Prompts for ID, Name, and Age, appending the new record directly to `students.txt`.
   - **View Students**: Displays all records loaded in memory.
   - **Search Student**: Locates a student record by ID.
   - **Exit**: Safely terminates the program while preserving all data.
4. **Resilient Exception & Input Handling**: Uses try-with-resources for stream management and handles missing file scenarios cleanly.

---

## 🏗️ Program Architecture & Flow

```
   [ Application Start ]
            │
            ▼
 ┌──────────────────────┐
 │ FileManager.load()   │ ◄── Reads 'students.txt' (if present)
 └──────────┬───────────┘
            │ Populates
            ▼
 ┌──────────────────────┐
 │ List<Student>        │ ◄── In-Memory ArrayList
 └──────────┬───────────┘
            │
            ▼
 ┌──────────────────────┐
 │ Interactive Main     │
 │ Console Menu         │
 └──────────┬───────────┘
            │
  ┌─────────┴──────────┬────────────────────┐
  ▼                    ▼                    ▼
[Add Student]   [View Students]     [Search Student]
  │
  ▼
[FileManager.save()] ──► Appends CSV record to 'students.txt'
```

---

## 📂 File Structure

- **`Student.java`**: Data model class representing a student. Includes `toFileString()` for CSV serialization and `fromFileString(line)` for deserialization.
- **`FileManager.java`**: Utility class handling file reading (`BufferedReader`) and writing (`FileWriter`/`PrintWriter`).
- **`StudentApp.java`**: Entry point class containing the menu loop, input handling, and user choices.
- **`students.txt`**: Persistent text file storing student records in CSV format (`ID,Name,Age`).

---

## 🚀 How to Run

### Prerequisites
- Java Development Kit (JDK 8 or higher).

### Compilation & Execution Steps

1. **Navigate to the Project Directory**:
   ```powershell
   cd task_2-student-management-system
   ```

2. **Compile Java Source Files**:
   ```powershell
   javac Student.java FileManager.java StudentApp.java
   ```

3. **Launch Application**:
   ```powershell
   java StudentApp
   ```

---

## 💻 Console Output & Verification Log

### Initial Run (First-Time Startup & Record Creation)
```text
=================================================
   STUDENT MANAGEMENT SYSTEM (PERSISTENT FILE)   
=================================================
[INFO] Loaded 0 student record(s) from 'students.txt'.

---------------- MAIN MENU ----------------
1. Add Student
2. View Students
3. Search Student
4. Exit
Enter your choice (1-4): 1

--- Add New Student ---
Enter ID: 101
Enter Name: John Doe
Enter Age: 20
[SUCCESS] Student saved successfully.
```

### Subsequent Run (Simulating Application Restart)
```text
=================================================
   STUDENT MANAGEMENT SYSTEM (PERSISTENT FILE)   
=================================================
[INFO] Loaded 2 student record(s) from 'students.txt'.

---------------- MAIN MENU ----------------
1. Add Student
2. View Students
3. Search Student
4. Exit
Enter your choice (1-4): 2

--- Student List ---
---------------------------------------------------
ID: 101, Name: John Doe, Age: 20
ID: 102, Name: Jane Smith, Age: 22
---------------------------------------------------
Total Records: 2
```

### Persistent Data Store (`students.txt`)
```csv
101,John Doe,20
102,Jane Smith,22
103,Bob Johnson,21
```

---

## 📖 Key Concepts Demonstrated

1. **CSV Serialization/Deserialization**: Transforming objects to text lines and parsing text lines back into Java objects.
2. **Try-With-Resources**: Ensuring file readers and writers are cleanly closed to prevent resource leaks.
3. **Data Persistence vs In-Memory Storage**: Understanding state recovery across process lifecycles.
