# Mini Blackboard System

A console-based Learning Management System (LMS) built with Java, implementing Object-Oriented Programming principles including Inheritance, Encapsulation, Abstraction, and Polymorphism.

## Project Description

The Mini Blackboard System is a simplified learning management system that manages relationships between students, teachers, and courses. The application supports a full academic workflow through a text-based console interface, allowing administrators to manage users and courses, teachers to create assignments and grade students, and students to view their courses, assignments, and grades.

## Features

### General Features
- **Data Persistence**: All data is saved to `.txt` files and automatically loaded on startup
- **Login System**: Secure login mechanism with role-based access control

### Administrator Features
- Create, update, and delete Student accounts
- Create, update, and delete Teacher accounts
- Create and define new Courses
- Assign Teachers to specific courses
- Enroll Students in courses
- View all students, teachers, and courses

### Teacher Features
- View all courses assigned to them
- View list of students enrolled in each course
- Create new Assignments for courses
- Enter and update Grades for students on assignments
- View assignments for courses

### Student Features
- View list of enrolled courses
- View all assignments for each course with their grades
- View final calculated grade for each course
- View grades across all courses

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- A terminal/command prompt

## Installation & Setup

1. **Extract the project files** to your desired directory

2. **Navigate to the project directory**:
   ```bash
   cd "Mini Blackboard System"
   ```

3. **Compile the project**:
   ```bash
   javac -d bin -sourcepath src src/com/blackboard/*.java src/com/blackboard/**/*.java
   ```

4. **Run the application**:
   ```bash
   java -cp bin com.blackboard.Main
   ```

## Default Admin Credentials

**User ID:** `ADMIN001`  
**Password:** `admin123`

**Note:** The default admin account is automatically created on first run if no users exist.

## Project Structure

```
Mini Blackboard System/
├── src/
│   └── com/
│       └── blackboard/
│           ├── Main.java                    # Entry point
│           ├── models/                      # Data models (POJOs)
│           │   ├── User.java                # Abstract base class
│           │   ├── Student.java
│           │   ├── Teacher.java
│           │   ├── Admin.java
│           │   ├── Course.java
│           │   ├── Assignment.java
│           │   └── Grade.java
│           ├── services/                     # Business logic layer
│           │   ├── AuthenticationService.java
│           │   ├── CourseService.java
│           │   ├── AssignmentService.java
│           │   ├── GradeService.java
│           │   └── DataPersistenceService.java
│           ├── views/                        # User interface layer
│           │   ├── AdminMenu.java
│           │   ├── TeacherMenu.java
│           │   └── StudentMenu.java
│           └── utils/                        # Utility classes
│               └── InputValidator.java
├── data/                                      # Data persistence files
│   ├── users.txt                            # User accounts
│   ├── courses.txt                          # Course information
│   ├── assignments.txt                     # Assignment details
│   ├── grades.txt                          # Student grades
│   └── enrollments.txt                     # Student-course relationships
└── bin/                                      # Compiled .class files
```

## Usage Instructions

### Starting the Application

1. Run the compiled program using:
   ```bash
   java -cp bin com.blackboard.Main
   ```

2. You will see the main menu:
   ```
   === MINI BLACKBOARD SYSTEM ===
   1. Login
   2. Exit
   ```

### Login as Administrator

1. Select option `1` to login
2. Enter User ID: `ADMIN001`
3. Enter Password: `admin123`
4. You will be taken to the Admin Menu

### Creating a Student Account

1. From Admin Menu, select option `1` (Create Student Account)
2. The system will automatically generate a Student ID (e.g., STUDENT001, STUDENT002, etc.)
3. Enter the password, name, and major
4. The student account will be created automatically

### Creating a Teacher Account

1. From Admin Menu, select option `4` (Create Teacher Account)
2. Enter User ID, password, name, and department
3. The teacher account will be created

### Creating a Course

1. From Admin Menu, select option `7` (Create Course)
2. Enter Course ID, Course Name, Description, Teacher ID, and Capacity
3. The course will be created and assigned to the specified teacher

### Enrolling a Student in a Course

1. From Admin Menu, select option `10` (Enroll Student in Course)
2. Enter the Course ID and Student ID
3. The system will check course capacity before enrollment

### Teacher Workflow

1. Login as a teacher
2. View assigned courses (Option 1)
3. Create assignments for courses (Option 3)
4. Enter/update grades for students (Option 4)
5. View students enrolled in courses (Option 2)

### Student Workflow

1. Login as a student
2. View enrolled courses (Option 1)
3. View assignments with grades for a course (Option 2)
4. View all grades (Option 3)
5. View final grade for a course (Option 4)

## Data Persistence

All data is automatically saved to `.txt` files in the `data/` directory:

- **users.txt**: Stores all user accounts (Students, Teachers, Admins)
- **courses.txt**: Stores course information
- **assignments.txt**: Stores assignment details
- **grades.txt**: Stores student grades
- **enrollments.txt**: Stores student-course enrollment relationships

**Important:** Data is automatically:
- Loaded when the application starts
- Saved when you logout or exit the application
- Saved automatically on program termination (including crashes)

## OOP Design Principles

This project demonstrates:

1. **Inheritance**: `User` abstract class with `Student`, `Teacher`, and `Admin` subclasses
2. **Encapsulation**: Private fields with public getters/setters in all model classes
3. **Abstraction**: Abstract `User` class defining common behavior
4. **Polymorphism**: Role-based menu routing and service methods handling different user types

## Notes

- Student IDs are automatically generated in the format: STUDENT001, STUDENT002, etc.
- Course capacity is enforced - students cannot enroll if the course is full
- Grades are calculated as percentages based on points earned vs. total points possible
- All input validation is performed to ensure data integrity

## Troubleshooting

If you encounter issues:

1. **Compilation errors**: Ensure you're using JDK 8 or higher
2. **File not found errors**: Make sure the `data/` directory exists in the project root
3. **Login fails**: Verify you're using the correct credentials (case-sensitive)
4. **Data not persisting**: Check that the `data/` directory has write permissions

## Author

Created as an individual project for Computer Programming 2 course.

## License

This project is for educational purposes only.

