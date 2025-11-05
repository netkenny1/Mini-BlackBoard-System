package com.blackboard.views;

import com.blackboard.models.*;
import com.blackboard.services.*;
import com.blackboard.utils.InputValidator;
import java.util.ArrayList;
import java.util.Scanner;

public class TeacherMenu {
    private Scanner scanner;
    private Teacher teacher;
    private AuthenticationService authService;
    private CourseService courseService;
    private AssignmentService assignmentService;
    private GradeService gradeService;
    
    public TeacherMenu(Scanner scanner, Teacher teacher, AuthenticationService authService,
                      CourseService courseService, AssignmentService assignmentService, 
                      GradeService gradeService) {
        this.scanner = scanner;
        this.teacher = teacher;
        this.authService = authService;
        this.courseService = courseService;
        this.assignmentService = assignmentService;
        this.gradeService = gradeService;
    }
    
    public void displayMenu() {
        boolean running = true;
        
        while (running) {
            System.out.println("\n=== TEACHER MENU ===");
            System.out.println("Welcome, " + teacher.getName() + "!");
            System.out.println("1. View My Courses");
            System.out.println("2. View Students in Course");
            System.out.println("3. Create Assignment");
            System.out.println("4. Enter/Update Grade");
            System.out.println("5. View Assignments for Course");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    viewMyCourses();
                    break;
                case "2":
                    viewStudentsInCourse();
                    break;
                case "3":
                    createAssignment();
                    break;
                case "4":
                    enterGrade();
                    break;
                case "5":
                    viewAssignmentsForCourse();
                    break;
                case "6":
                    running = false;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void viewMyCourses() {
        System.out.println("\n--- My Courses ---");
        ArrayList<Course> courses = courseService.getCoursesForTeacher(teacher.getUserId());
        
        if (courses.isEmpty()) {
            System.out.println("No courses assigned.");
            return;
        }
        
        for (Course course : courses) {
            System.out.println("ID: " + course.getCourseId() + 
                              ", Name: " + course.getCourseName() + 
                              ", Capacity: " + course.getCapacity());
        }
    }
    
    private void viewStudentsInCourse() {
        System.out.println("\n--- View Students in Course ---");
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine().trim();
        
        Course course = courseService.findCourseById(courseId);
        if (course == null) {
            System.out.println("Error: Course not found.");
            return;
        }
        
        if (!course.getTeacherId().equals(teacher.getUserId())) {
            System.out.println("Error: You are not assigned to this course.");
            return;
        }
        
        System.out.println("\nStudents enrolled in " + course.getCourseName() + ":");
        ArrayList<User> users = authService.getAllUsers();
        boolean found = false;
        
        for (User user : users) {
            if (user instanceof Student) {
                Student student = (Student) user;
                if (student.getEnrolledCourses().contains(course)) {
                    System.out.println("ID: " + student.getUserId() + 
                                      ", Name: " + student.getName() + 
                                      ", Major: " + student.getMajor());
                    found = true;
                }
            }
        }
        
        if (!found) {
            System.out.println("No students enrolled in this course.");
        }
    }
    
    private void createAssignment() {
        System.out.println("\n--- Create Assignment ---");
        System.out.print("Enter Assignment ID: ");
        String assignmentId = scanner.nextLine().trim();
        
        if (!InputValidator.isValidAssignmentId(assignmentId)) {
            System.out.println("Error: Assignment ID cannot be empty.");
            return;
        }
        
        if (assignmentService.findAssignmentById(assignmentId) != null) {
            System.out.println("Error: Assignment ID already exists.");
            return;
        }
        
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine().trim();
        
        Course course = courseService.findCourseById(courseId);
        if (course == null) {
            System.out.println("Error: Course not found.");
            return;
        }
        
        if (!course.getTeacherId().equals(teacher.getUserId())) {
            System.out.println("Error: You are not assigned to this course.");
            return;
        }
        
        System.out.print("Enter Title: ");
        String title = scanner.nextLine().trim();
        
        if (!InputValidator.isNotEmpty(title)) {
            System.out.println("Error: Title cannot be empty.");
            return;
        }
        
        System.out.print("Enter Description: ");
        String description = scanner.nextLine().trim();
        
        System.out.print("Enter Due Date (e.g., 2024-12-15): ");
        String dueDate = scanner.nextLine().trim();
        
        if (!InputValidator.isNotEmpty(dueDate)) {
            System.out.println("Error: Due date cannot be empty.");
            return;
        }
        
        System.out.print("Enter Max Points: ");
        String maxPointsInput = scanner.nextLine().trim();
        
        if (!InputValidator.isValidDouble(maxPointsInput)) {
            System.out.println("Error: Please enter a valid number.");
            return;
        }
        
        double maxPoints = Double.parseDouble(maxPointsInput);
        
        Assignment assignment = assignmentService.createAssignment(assignmentId, courseId, title, 
                                                                   description, dueDate, maxPoints);
        if (assignment != null) {
            System.out.println("Assignment created successfully!");
        } else {
            System.out.println("Error: Failed to create assignment.");
        }
    }
    
    private void enterGrade() {
        System.out.println("\n--- Enter/Update Grade ---");
        System.out.print("Enter Assignment ID: ");
        String assignmentId = scanner.nextLine().trim();
        
        Assignment assignment = assignmentService.findAssignmentById(assignmentId);
        if (assignment == null) {
            System.out.println("Error: Assignment not found.");
            return;
        }
        
        Course course = courseService.findCourseById(assignment.getCourseId());
        if (course == null || !course.getTeacherId().equals(teacher.getUserId())) {
            System.out.println("Error: You are not assigned to this course.");
            return;
        }
        
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();
        
        User user = authService.findUserById(studentId);
        if (user == null || !user.getRole().equals("STUDENT")) {
            System.out.println("Error: Student not found.");
            return;
        }
        
        System.out.print("Enter Points Earned: ");
        String pointsInput = scanner.nextLine().trim();
        
        if (!InputValidator.isValidDouble(pointsInput)) {
            System.out.println("Error: Please enter a valid number.");
            return;
        }
        
        double points = Double.parseDouble(pointsInput);
        
        if (points < 0 || points > assignment.getMaxPoints()) {
            System.out.println("Error: Points must be between 0 and " + assignment.getMaxPoints() + ".");
            return;
        }
        
        Grade existingGrade = gradeService.getGradeForStudentAndAssignment(studentId, assignmentId);
        
        if (existingGrade != null) {
            existingGrade.setPoints(points);
            gradeService.updateGrade(existingGrade);
            System.out.println("Grade updated successfully!");
        } else {
            String gradeId = "G" + System.currentTimeMillis();
            Grade grade = gradeService.createGrade(gradeId, studentId, assignmentId, points);
            if (grade != null) {
                System.out.println("Grade entered successfully!");
            } else {
                System.out.println("Error: Failed to create grade.");
            }
        }
    }
    
    private void viewAssignmentsForCourse() {
        System.out.println("\n--- View Assignments for Course ---");
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine().trim();
        
        Course course = courseService.findCourseById(courseId);
        if (course == null) {
            System.out.println("Error: Course not found.");
            return;
        }
        
        if (!course.getTeacherId().equals(teacher.getUserId())) {
            System.out.println("Error: You are not assigned to this course.");
            return;
        }
        
        ArrayList<Assignment> assignments = assignmentService.getAssignmentsForCourse(courseId);
        
        if (assignments.isEmpty()) {
            System.out.println("No assignments found for this course.");
            return;
        }
        
        System.out.println("\nAssignments for " + course.getCourseName() + ":");
        for (Assignment assignment : assignments) {
            System.out.println("ID: " + assignment.getAssignmentId() + 
                              ", Title: " + assignment.getTitle() + 
                              ", Max Points: " + assignment.getMaxPoints() + 
                              ", Due Date: " + assignment.getDueDate());
        }
    }
}

