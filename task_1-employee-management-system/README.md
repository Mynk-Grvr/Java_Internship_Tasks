# Employee Management System (Core Java - Task 1)

A console-based **Employee Management System (EMS)** application written in Core Java. This project demonstrates core Object-Oriented Programming (OOP) concepts, data structures (`ArrayList`), input handling using `Scanner`, loop controls, conditional structures, and clean method organization to implement basic CRUD (Create, Read, Update) operations.

---

## 📌 Features

1. **Add New Employee**: Create employee records with `ID`, `Name`, and `Department`. Prevents duplicate IDs and empty entries.
2. **View All Employees**: List all stored employees in a clean format alongside total record count.
3. **Search Employee by ID**: Instantly locate an employee record by ID.
4. **Update Employee Department**: Modify the department for an existing employee record.
5. **Robust Console Menu**: Loops continuously until the user chooses to exit, handling invalid inputs gracefully without crashing.

---

## 🏗️ Project Architecture & Data Model

### 1. `Employee.java` (Data Model)
Encapsulates individual employee data fields:
- `id` (`int`) – Unique employee identifier
- `name` (`String`) – Employee full name
- `department` (`String`) – Assigned department

Includes constructor, getters, setters, and an overridden `toString()` method.

### 2. `EmployeeApp.java` (Main Application Logic)
Manages application execution flow:
- `employees`: Dynamic `ArrayList<Employee>` for runtime storage.
- `sc`: `Scanner` for capturing user input.
- `addEmployee()`: Accepts user input and appends a new `Employee` object.
- `viewEmployees()`: Iterates through `ArrayList` to print records.
- `searchEmployee()`: Performs linear search by ID.
- `updateDepartment()`: Finds employee by ID and updates department name.

---

## 🚀 How to Run the Project

### Prerequisites
- **JDK 17** or higher installed (`javac` and `java` commands available in System PATH).

### Execution Steps

1. **Navigate to the Project Directory**:
   ```powershell
   cd C:\Users\Mynk_Grvr\.gemini\antigravity\scratch\employee-management-system
   ```

2. **Compile the Java Files**:
   ```powershell
   javac Employee.java EmployeeApp.java
   ```

3. **Run the Application**:
   ```powershell
   java EmployeeApp
   ```

---

## 💻 Sample Program Output

```text
=================================================
   WELCOME TO EMPLOYEE MANAGEMENT SYSTEM (EMS)  
=================================================

---------------- MAIN MENU ----------------
1. Add Employee
2. View Employees
3. Search Employee
4. Update Department
5. Exit
Enter your choice (1-5): 1

--- Add New Employee ---
ID: 101
Name: Alice Smith
Department: Engineering
[SUCCESS] Employee added successfully.

---------------- MAIN MENU ----------------
1. Add Employee
2. View Employees
3. Search Employee
4. Update Department
5. Exit
Enter your choice (1-5): 2

--- Employee List ---
---------------------------------------------------------------
101 | Alice Smith | Engineering
---------------------------------------------------------------
Total Employees: 1

---------------- MAIN MENU ----------------
1. Add Employee
2. View Employees
3. Search Employee
4. Update Department
5. Exit
Enter your choice (1-5): 4

--- Update Department ---
Employee ID: 101
New Department: Cloud Operations
Department updated.

---------------- MAIN MENU ----------------
1. Add Employee
2. View Employees
3. Search Employee
4. Update Department
5. Exit
Enter your choice (1-5): 5

Exiting Employee Management System. Goodbye!
```

---

## 📖 Key Concepts Covered

- **Encapsulation**: Private class fields accessed via public getter and setter methods.
- **Dynamic Collections**: `ArrayList` dynamically resizes to manage records without fixed-array constraints.
- **Input Validation & Exception Handling**: Scanner buffer cleanup and `InputMismatchException` catching.
- **Method Modularization**: Clean breakdown of CRUD actions into static helper methods.
