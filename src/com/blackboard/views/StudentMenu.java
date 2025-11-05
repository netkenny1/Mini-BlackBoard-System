package com.blackboard.views;

import com.blackboard.models.*;
import com.blackboard.services.*;
import java.util.ArrayList;
import java.util.Scanner;

public class StudentMenu {
    private Scanner scanner;
    private Student student;
    private CourseService courseService;
    private AssignmentService assignmentService;
    private GradeService gradeService;
    
    public StudentMenu(Scanner scanner, Student student, CourseService courseService,
                      AssignmentService assignmentService, GradeService gradeService) {
        this.scanner = scanner;
        this.student = student;
        this.courseService = courseService;
        this.assignmentService = assignmentService;
        this.gradeService = gradeService;
    }
    
    public void displayMenu() {
        boolean running = true;
        
        while (running) {
            System.out.println("\n=== STUDENT MENU ===");
            System.out.println("Welcome, " + student.getName() + "!");
            System.out.println("1. View My Courses");
            System.out.println("2. View Assignments for Course");
            System.out.println("3. View My Grades");
            System.out.println("4. View Final Grade for Course");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    viewMyCourses();
                    break;
                case "2":
                    viewAssignmentsForCourse();
                    break;
                case "3":
                    viewMyGrades();
                    break;
                case "4":
                    viewFinalGradeForCourse();
                    break;
                case "5":
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
        ArrayList<Course> courses = student.getEnrolledCourses();
        
        if (courses.isEmpty()) {
            System.out.println("You are not enrolled in any courses.");
            return;
        }
        
        for (Course course : courses) {
            System.out.println("ID: " + course.getCourseId() + 
                              ", Name: " + course.getCourseName() + 
                              ", Description: " + course.getDescription());
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
        
        if (!student.getEnrolledCourses().contains(course)) {
            System.out.println("Error: You are not enrolled in this course.");
            return;
        }
        
        ArrayList<Assignment> assignments = assignmentService.getAssignmentsForCourse(courseId);
        
        if (assignments.isEmpty()) {
            System.out.println("No assignments found for this course.");
            return;
        }
        
        System.out.println("\nAssignments and Grades for " + course.getCourseName() + ":");
        for (Assignment assignment : assignments) {
            Grade grade = gradeService.getGradeForStudentAndAssignment(student.getUserId(), assignment.getAssignmentId());
            String gradeInfo = (grade != null) ? 
                "Grade: " + grade.getPoints() + "/" + assignment.getMaxPoints() : 
                "Grade: Not yet graded";
            
            System.out.println("ID: " + assignment.getAssignmentId() + 
                              ", Title: " + assignment.getTitle() + 
                              ", Max Points: " + assignment.getMaxPoints() + 
                              ", Due Date: " + assignment.getDueDate() + 
                              ", " + gradeInfo);
        }
    }
    
    private void viewMyGrades() {
        System.out.println("\n--- My Grades ---");
        ArrayList<Grade> grades = gradeService.getGradesForStudent(student.getUserId());
        
        if (grades.isEmpty()) {
            System.out.println("No grades found.");
            return;
        }
        
        for (Grade grade : grades) {
            Assignment assignment = assignmentService.findAssignmentById(grade.getAssignmentId());
            if (assignment != null) {
                System.out.println("Assignment: " + assignment.getTitle() + 
                                  ", Points: " + grade.getPoints() + "/" + assignment.getMaxPoints());
            }
        }
    }
    
    private void viewFinalGradeForCourse() {
        System.out.println("\n--- View Final Grade for Course ---");
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine().trim();
        
        Course course = courseService.findCourseById(courseId);
        if (course == null) {
            System.out.println("Error: Course not found.");
            return;
        }
        
        if (!student.getEnrolledCourses().contains(course)) {
            System.out.println("Error: You are not enrolled in this course.");
            return;
        }
        
        ArrayList<Assignment> assignments = assignmentService.getAssignmentsForCourse(courseId);
        
        if (assignments.isEmpty()) {
            System.out.println("No assignments found for this course.");
            return;
        }
        
        double finalGrade = gradeService.calculateFinalGradeForCourse(student.getUserId(), courseId, assignments);
        
        System.out.println("\nFinal Grade for " + course.getCourseName() + ":");
        System.out.println(String.format("%.2f%%", finalGrade));
    }
}

