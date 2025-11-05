package com.blackboard;

import com.blackboard.models.*;
import com.blackboard.services.*;
import com.blackboard.views.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Initialize all services
        AuthenticationService authService = new AuthenticationService();
        CourseService courseService = new CourseService();
        AssignmentService assignmentService = new AssignmentService();
        GradeService gradeService = new GradeService();
        DataPersistenceService persistenceService = new DataPersistenceService(
            authService, courseService, assignmentService, gradeService);
        
        // Load data from files
        System.out.println("Loading data from files...");
        persistenceService.loadAllData();
        System.out.println("Data loaded successfully!");
        
        // Create default admin if no users exist
        if (authService.getAllUsers().isEmpty()) {
            System.out.println("No users found. Creating default admin account...");
            Admin defaultAdmin = new Admin("ADMIN001", "admin123", "System Administrator");
            authService.addUser(defaultAdmin);
            persistenceService.saveAllData(); // Save the default admin
            System.out.println("Default admin created!");
            System.out.println("User ID: ADMIN001");
            System.out.println("Password: admin123");
        }
        
        // Add shutdown hook to save data when program exits (even if crashed)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nSaving data before exit...");
            persistenceService.saveAllData();
        }));
        
        // Main program loop
        boolean running = true;
        
        while (running) {
            System.out.println("\n=== MINI BLACKBOARD SYSTEM ===");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    login(scanner, authService, courseService, assignmentService, 
                          gradeService, persistenceService);
                    break;
                case "2":
                    System.out.println("Saving data...");
                    persistenceService.saveAllData();
                    System.out.println("Data saved. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        
        scanner.close();
    }
    
    private static void login(Scanner scanner, AuthenticationService authService,
                              CourseService courseService, AssignmentService assignmentService,
                              GradeService gradeService, DataPersistenceService persistenceService) {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine().trim();
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();
        
        User user = authService.login(userId, password);
        
        if (user == null) {
            System.out.println("Invalid credentials. Please try again.");
            return;
        }
        
        System.out.println("\nLogin successful! Welcome, " + user.getName() + "!");
        
        // Route to appropriate menu based on role
        String role = user.getRole();
        
        if (role.equals("ADMIN")) {
            AdminMenu adminMenu = new AdminMenu(scanner, authService, courseService);
            adminMenu.displayMenu();
            // Save data after admin logs out
            persistenceService.saveAllData();
        } else if (role.equals("TEACHER")) {
            Teacher teacher = (Teacher) user;
            TeacherMenu teacherMenu = new TeacherMenu(scanner, teacher, authService, 
                                                      courseService, assignmentService, gradeService);
            teacherMenu.displayMenu();
            // Save data after teacher logs out
            persistenceService.saveAllData();
        } else if (role.equals("STUDENT")) {
            Student student = (Student) user;
            StudentMenu studentMenu = new StudentMenu(scanner, student, courseService,
                                                     assignmentService, gradeService);
            studentMenu.displayMenu();
            // Save data after student logs out
            persistenceService.saveAllData();
        }
    }
}

