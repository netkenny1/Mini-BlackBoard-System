package com.blackboard.views;

import com.blackboard.models.*;
import com.blackboard.services.*;
import com.blackboard.utils.InputValidator;
import java.util.ArrayList;
import java.util.Scanner;

public class AdminMenu {
    private Scanner scanner;
    private AuthenticationService authService;
    private CourseService courseService;
    
    public AdminMenu(Scanner scanner, AuthenticationService authService, 
                    CourseService courseService) {
        this.scanner = scanner;
        this.authService = authService;
        this.courseService = courseService;
    }
    
    public void displayMenu() {
        boolean running = true;
        
        while (running) {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. Create Student Account");
            System.out.println("2. Update Student Account");
            System.out.println("3. Delete Student Account");
            System.out.println("4. Create Teacher Account");
            System.out.println("5. Update Teacher Account");
            System.out.println("6. Delete Teacher Account");
            System.out.println("7. Create Course");
            System.out.println("8. Delete Course");
            System.out.println("9. Assign Teacher to Course");
            System.out.println("10. Enroll Student in Course");
            System.out.println("11. View All Students");
            System.out.println("12. View All Teachers");
            System.out.println("13. View All Courses");
            System.out.println("14. Logout");
            System.out.print("Enter your choice: ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    createStudent();
                    break;
                case "2":
                    updateStudent();
                    break;
                case "3":
                    deleteStudent();
                    break;
                case "4":
                    createTeacher();
                    break;
                case "5":
                    updateTeacher();
                    break;
                case "6":
                    deleteTeacher();
                    break;
                case "7":
                    createCourse();
                    break;
                case "8":
                    deleteCourse();
                    break;
                case "9":
                    assignTeacherToCourse();
                    break;
                case "10":
                    enrollStudentInCourse();
                    break;
                case "11":
                    viewAllStudents();
                    break;
                case "12":
                    viewAllTeachers();
                    break;
                case "13":
                    viewAllCourses();
                    break;
                case "14":
                    running = false;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void createStudent() {
        System.out.println("\n--- Create Student Account ---");
        
        // Automatically generate student ID
        String userId = generateStudentId();
        System.out.println("Generated Student ID: " + userId);
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();
        
        if (!InputValidator.isValidPassword(password)) {
            System.out.println("Error: Password must be at least 3 characters.");
            return;
        }
        
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        
        if (!InputValidator.isNotEmpty(name)) {
            System.out.println("Error: Name cannot be empty.");
            return;
        }
        
        System.out.print("Enter Major: ");
        String major = scanner.nextLine().trim();
        
        if (!InputValidator.isNotEmpty(major)) {
            System.out.println("Error: Major cannot be empty.");
            return;
        }
        
        Student student = new Student(userId, password, name, major);
        authService.addUser(student);
        System.out.println("Student account created successfully!");
    }
    
    // Generates the next sequential student ID based on number of students
    private String generateStudentId() {
        // Count all students
        int studentCount = 0;
        int maxNumber = 0;
        
        for (User user : authService.getAllUsers()) {
            if (user instanceof Student) {
                studentCount++;
                String userId = user.getUserId();
                // Check if ID follows STUDENT### pattern
                if (userId.startsWith("STUDENT")) {
                    try {
                        String numberPart = userId.substring(7); // After "STUDENT"
                        int number = Integer.parseInt(numberPart);
                        if (number > maxNumber) {
                            maxNumber = number;
                        }
                    } catch (NumberFormatException e) {
                        // Not a numbered STUDENT ID, ignore
                    }
                }
            }
        }
        
        // Use the next number (max of studentCount or maxNumber found, then add 1)
        int nextNumber = Math.max(studentCount, maxNumber) + 1;
        
        // Format as STUDENT001, STUDENT002, etc. (3 digits)
        return String.format("STUDENT%03d", nextNumber);
    }
    
    private void createTeacher() {
        System.out.println("\n--- Create Teacher Account ---");
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine().trim();
        
        if (!InputValidator.isValidUserId(userId)) {
            System.out.println("Error: User ID cannot be empty.");
            return;
        }
        
        if (authService.findUserById(userId) != null) {
            System.out.println("Error: User ID already exists.");
            return;
        }
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();
        
        if (!InputValidator.isValidPassword(password)) {
            System.out.println("Error: Password must be at least 3 characters.");
            return;
        }
        
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        
        if (!InputValidator.isNotEmpty(name)) {
            System.out.println("Error: Name cannot be empty.");
            return;
        }
        
        System.out.print("Enter Department: ");
        String department = scanner.nextLine().trim();
        
        if (!InputValidator.isNotEmpty(department)) {
            System.out.println("Error: Department cannot be empty.");
            return;
        }
        
        Teacher teacher = new Teacher(userId, password, name, department);
        authService.addUser(teacher);
        System.out.println("Teacher account created successfully!");
    }
    
    private void createCourse() {
        System.out.println("\n--- Create Course ---");
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine().trim();
        
        if (!InputValidator.isValidCourseId(courseId)) {
            System.out.println("Error: Course ID cannot be empty.");
            return;
        }
        
        if (courseService.findCourseById(courseId) != null) {
            System.out.println("Error: Course ID already exists.");
            return;
        }
        
        System.out.print("Enter Course Name: ");
        String courseName = scanner.nextLine().trim();
        
        if (!InputValidator.isNotEmpty(courseName)) {
            System.out.println("Error: Course name cannot be empty.");
            return;
        }
        
        System.out.print("Enter Description: ");
        String description = scanner.nextLine().trim();
        
        System.out.print("Enter Teacher ID: ");
        String teacherId = scanner.nextLine().trim();
        
        User teacher = authService.findUserById(teacherId);
        if (teacher == null || !teacher.getRole().equals("TEACHER")) {
            System.out.println("Error: Teacher not found.");
            return;
        }
        
        System.out.print("Enter Capacity: ");
        String capacityInput = scanner.nextLine().trim();
        
        if (!InputValidator.isValidInteger(capacityInput)) {
            System.out.println("Error: Please enter a valid number.");
            return;
        }
        
        int capacity = Integer.parseInt(capacityInput);
        
        if (!InputValidator.isValidCapacity(capacity)) {
            System.out.println("Error: Capacity must be greater than 0.");
            return;
        }
        
        Course course = courseService.createCourse(courseId, courseName, description, teacherId, capacity);
        if (course != null) {
            System.out.println("Course created successfully!");
        } else {
            System.out.println("Error: Failed to create course.");
        }
    }
    
    private void assignTeacherToCourse() {
        System.out.println("\n--- Assign Teacher to Course ---");
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine().trim();
        
        Course course = courseService.findCourseById(courseId);
        if (course == null) {
            System.out.println("Error: Course not found.");
            return;
        }
        
        System.out.print("Enter Teacher ID: ");
        String teacherId = scanner.nextLine().trim();
        
        User user = authService.findUserById(teacherId);
        if (user == null || !user.getRole().equals("TEACHER")) {
            System.out.println("Error: Teacher not found.");
            return;
        }
        
        Teacher teacher = (Teacher) user;
        
        if (courseService.assignTeacherToCourse(courseId, teacher)) {
            System.out.println("Teacher assigned to course successfully!");
        } else {
            System.out.println("Error: Failed to assign teacher.");
        }
    }
    
    private void enrollStudentInCourse() {
        System.out.println("\n--- Enroll Student in Course ---");
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine().trim();
        
        Course course = courseService.findCourseById(courseId);
        if (course == null) {
            System.out.println("Error: Course not found.");
            return;
        }
        
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();
        
        User user = authService.findUserById(studentId);
        if (user == null || !user.getRole().equals("STUDENT")) {
            System.out.println("Error: Student not found.");
            return;
        }
        
        Student student = (Student) user;
        ArrayList<Student> allStudents = new ArrayList<>();
        for (User u : authService.getAllUsers()) {
            if (u instanceof Student) {
                allStudents.add((Student) u);
            }
        }
        
        if (courseService.enrollStudentInCourse(courseId, student, allStudents)) {
            System.out.println("Student enrolled in course successfully!");
        } else {
            System.out.println("Error: Failed to enroll student. Course may be full or student already enrolled.");
        }
    }
    
    private void viewAllStudents() {
        System.out.println("\n--- All Students ---");
        ArrayList<User> users = authService.getAllUsers();
        boolean found = false;
        
        for (User user : users) {
            if (user instanceof Student) {
                Student student = (Student) user;
                System.out.println("ID: " + student.getUserId() + 
                                  ", Name: " + student.getName() + 
                                  ", Major: " + student.getMajor());
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No students found.");
        }
    }
    
    private void viewAllTeachers() {
        System.out.println("\n--- All Teachers ---");
        ArrayList<User> users = authService.getAllUsers();
        boolean found = false;
        
        for (User user : users) {
            if (user instanceof Teacher) {
                Teacher teacher = (Teacher) user;
                System.out.println("ID: " + teacher.getUserId() + 
                                  ", Name: " + teacher.getName() + 
                                  ", Department: " + teacher.getDepartment());
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No teachers found.");
        }
    }
    
    private void updateStudent() {
        System.out.println("\n--- Update Student Account ---");
        System.out.print("Enter Student ID: ");
        String userId = scanner.nextLine().trim();
        
        User user = authService.findUserById(userId);
        if (user == null || !user.getRole().equals("STUDENT")) {
            System.out.println("Error: Student not found.");
            return;
        }
        
        Student student = (Student) user;
        
        System.out.print("Enter New Password (or press Enter to keep current): ");
        String password = scanner.nextLine().trim();
        if (!password.isEmpty()) {
            if (!InputValidator.isValidPassword(password)) {
                System.out.println("Error: Password must be at least 3 characters.");
                return;
            }
            student.setPassword(password);
        }
        
        System.out.print("Enter New Name (or press Enter to keep current): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) {
            student.setName(name);
        }
        
        System.out.print("Enter New Major (or press Enter to keep current): ");
        String major = scanner.nextLine().trim();
        if (!major.isEmpty()) {
            student.setMajor(major);
        }
        
        if (authService.updateUser(student)) {
            System.out.println("Student account updated successfully!");
        } else {
            System.out.println("Error: Failed to update student account.");
        }
    }
    
    private void deleteStudent() {
        System.out.println("\n--- Delete Student Account ---");
        System.out.print("Enter Student ID: ");
        String userId = scanner.nextLine().trim();
        
        User user = authService.findUserById(userId);
        if (user == null || !user.getRole().equals("STUDENT")) {
            System.out.println("Error: Student not found.");
            return;
        }
        
        System.out.print("Are you sure you want to delete this student? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("yes")) {
            if (authService.deleteUser(userId)) {
                System.out.println("Student account deleted successfully!");
            } else {
                System.out.println("Error: Failed to delete student account.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    private void updateTeacher() {
        System.out.println("\n--- Update Teacher Account ---");
        System.out.print("Enter Teacher ID: ");
        String userId = scanner.nextLine().trim();
        
        User user = authService.findUserById(userId);
        if (user == null || !user.getRole().equals("TEACHER")) {
            System.out.println("Error: Teacher not found.");
            return;
        }
        
        Teacher teacher = (Teacher) user;
        
        System.out.print("Enter New Password (or press Enter to keep current): ");
        String password = scanner.nextLine().trim();
        if (!password.isEmpty()) {
            if (!InputValidator.isValidPassword(password)) {
                System.out.println("Error: Password must be at least 3 characters.");
                return;
            }
            teacher.setPassword(password);
        }
        
        System.out.print("Enter New Name (or press Enter to keep current): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) {
            teacher.setName(name);
        }
        
        System.out.print("Enter New Department (or press Enter to keep current): ");
        String department = scanner.nextLine().trim();
        if (!department.isEmpty()) {
            teacher.setDepartment(department);
        }
        
        if (authService.updateUser(teacher)) {
            System.out.println("Teacher account updated successfully!");
        } else {
            System.out.println("Error: Failed to update teacher account.");
        }
    }
    
    private void deleteTeacher() {
        System.out.println("\n--- Delete Teacher Account ---");
        System.out.print("Enter Teacher ID: ");
        String userId = scanner.nextLine().trim();
        
        User user = authService.findUserById(userId);
        if (user == null || !user.getRole().equals("TEACHER")) {
            System.out.println("Error: Teacher not found.");
            return;
        }
        
        System.out.print("Are you sure you want to delete this teacher? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("yes")) {
            if (authService.deleteUser(userId)) {
                System.out.println("Teacher account deleted successfully!");
            } else {
                System.out.println("Error: Failed to delete teacher account.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    private void deleteCourse() {
        System.out.println("\n--- Delete Course ---");
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine().trim();
        
        Course course = courseService.findCourseById(courseId);
        if (course == null) {
            System.out.println("Error: Course not found.");
            return;
        }
        
        System.out.print("Are you sure you want to delete this course? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("yes")) {
            if (courseService.deleteCourse(courseId)) {
                System.out.println("Course deleted successfully!");
            } else {
                System.out.println("Error: Failed to delete course.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    private void viewAllCourses() {
        System.out.println("\n--- All Courses ---");
        ArrayList<Course> courses = courseService.getAllCourses();
        
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        
        for (Course course : courses) {
            System.out.println("ID: " + course.getCourseId() + 
                              ", Name: " + course.getCourseName() + 
                              ", Teacher: " + course.getTeacherId() + 
                              ", Capacity: " + course.getCapacity());
        }
    }
}

