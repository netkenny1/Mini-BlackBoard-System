package com.blackboard.services;

import com.blackboard.models.*;
import java.io.*;

public class DataPersistenceService {
    private static final String DATA_DIR = "data/";
    private static final String USERS_FILE = DATA_DIR + "users.txt";
    private static final String COURSES_FILE = DATA_DIR + "courses.txt";
    private static final String ASSIGNMENTS_FILE = DATA_DIR + "assignments.txt";
    private static final String GRADES_FILE = DATA_DIR + "grades.txt";
    private static final String ENROLLMENTS_FILE = DATA_DIR + "enrollments.txt";
    
    private AuthenticationService authService;
    private CourseService courseService;
    private AssignmentService assignmentService;
    private GradeService gradeService;
    
    public DataPersistenceService(AuthenticationService authService, 
                                 CourseService courseService,
                                 AssignmentService assignmentService,
                                 GradeService gradeService) {
        this.authService = authService;
        this.courseService = courseService;
        this.assignmentService = assignmentService;
        this.gradeService = gradeService;
    }
    
    // Loads all data from files
    public void loadAllData() {
        loadUsers();
        loadCourses();
        loadEnrollments(); // Must load after users and courses
        loadAssignments();
        loadGrades();
    }
    
    // Saves all data to files
    public void saveAllData() {
        saveUsers();
        saveCourses();
        saveEnrollments(); // Save enrollment relationships
        saveAssignments();
        saveGrades();
    }
    
    // Loads users from file
    private void loadUsers() {
        try {
            File file = new File(USERS_FILE);
            if (!file.exists()) {
                return; // File doesn't exist yet, nothing to load
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        String userId = parts[0];
                        String password = parts[1];
                        String name = parts[2];
                        String role = parts[3];
                        
                        User user = null;
                        if (role.equals("STUDENT") && parts.length >= 5) {
                            String major = parts[4];
                            user = new Student(userId, password, name, major);
                        } else if (role.equals("TEACHER") && parts.length >= 5) {
                            String department = parts[4];
                            user = new Teacher(userId, password, name, department);
                        } else if (role.equals("ADMIN")) {
                            user = new Admin(userId, password, name);
                        }
                        
                        if (user != null) {
                            authService.addUser(user);
                        }
                    }
                }
                line = reader.readLine();
            }
            
            reader.close();
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }
    
    // Saves users to file
    private void saveUsers() {
        try {
            File dir = new File(DATA_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE));
            
            for (User user : authService.getAllUsers()) {
                String line = user.getUserId() + "," + user.getPassword() + "," + 
                             user.getName() + "," + user.getRole();
                
                if (user instanceof Student) {
                    Student student = (Student) user;
                    line += "," + student.getMajor();
                } else if (user instanceof Teacher) {
                    Teacher teacher = (Teacher) user;
                    line += "," + teacher.getDepartment();
                }
                
                writer.println(line);
            }
            
            writer.close();
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
    
    // Loads courses from file
    private void loadCourses() {
        try {
            File file = new File(COURSES_FILE);
            if (!file.exists()) {
                return;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            
            while (line != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        String courseId = parts[0];
                        String courseName = parts[1];
                        String description = parts[2];
                        String teacherId = parts[3];
                        int capacity = Integer.parseInt(parts[4]);
                        
                        Course course = new Course(courseId, courseName, description, teacherId, capacity);
                        courseService.addCourse(course);
                    }
                }
                line = reader.readLine();
            }
            
            reader.close();
        } catch (IOException e) {
            System.err.println("Error loading courses: " + e.getMessage());
        }
    }
    
    // Saves courses to file
    private void saveCourses() {
        try {
            File dir = new File(DATA_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            PrintWriter writer = new PrintWriter(new FileWriter(COURSES_FILE));
            
            for (Course course : courseService.getAllCourses()) {
                String line = course.getCourseId() + "," + course.getCourseName() + "," + 
                             course.getDescription() + "," + course.getTeacherId() + "," + 
                             course.getCapacity();
                writer.println(line);
            }
            
            writer.close();
        } catch (IOException e) {
            System.err.println("Error saving courses: " + e.getMessage());
        }
    }
    
    // Loads enrollment relationships from file
    private void loadEnrollments() {
        try {
            File file = new File(ENROLLMENTS_FILE);
            if (!file.exists()) {
                return;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            
            while (line != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        String studentId = parts[0];
                        String courseId = parts[1];
                        
                        User user = authService.findUserById(studentId);
                        if (user instanceof Student) {
                            Student student = (Student) user;
                            Course course = courseService.findCourseById(courseId);
                            if (course != null && !student.getEnrolledCourses().contains(course)) {
                                student.enrollInCourse(course);
                            }
                        }
                    }
                }
                line = reader.readLine();
            }
            
            reader.close();
        } catch (IOException e) {
            System.err.println("Error loading enrollments: " + e.getMessage());
        }
    }
    
    // Saves enrollment relationships to file
    private void saveEnrollments() {
        try {
            File dir = new File(DATA_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            PrintWriter writer = new PrintWriter(new FileWriter(ENROLLMENTS_FILE));
            
            for (User user : authService.getAllUsers()) {
                if (user instanceof Student) {
                    Student student = (Student) user;
                    for (Course course : student.getEnrolledCourses()) {
                        writer.println(student.getUserId() + "," + course.getCourseId());
                    }
                }
            }
            
            writer.close();
        } catch (IOException e) {
            System.err.println("Error saving enrollments: " + e.getMessage());
        }
    }
    
    // Loads assignments from file
    private void loadAssignments() {
        try {
            File file = new File(ASSIGNMENTS_FILE);
            if (!file.exists()) {
                return;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            
            while (line != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 6) {
                        String assignmentId = parts[0];
                        String courseId = parts[1];
                        String title = parts[2];
                        String description = parts[3];
                        String dueDate = parts[4];
                        double maxPoints = Double.parseDouble(parts[5]);
                        
                        Assignment assignment = new Assignment(assignmentId, courseId, title, 
                                                             description, dueDate, maxPoints);
                        assignmentService.addAssignment(assignment);
                    }
                }
                line = reader.readLine();
            }
            
            reader.close();
        } catch (IOException e) {
            System.err.println("Error loading assignments: " + e.getMessage());
        }
    }
    
    // Saves assignments to file
    private void saveAssignments() {
        try {
            File dir = new File(DATA_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            PrintWriter writer = new PrintWriter(new FileWriter(ASSIGNMENTS_FILE));
            
            for (Assignment assignment : assignmentService.getAllAssignments()) {
                String line = assignment.getAssignmentId() + "," + assignment.getCourseId() + "," + 
                             assignment.getTitle() + "," + assignment.getDescription() + "," + 
                             assignment.getDueDate() + "," + assignment.getMaxPoints();
                writer.println(line);
            }
            
            writer.close();
        } catch (IOException e) {
            System.err.println("Error saving assignments: " + e.getMessage());
        }
    }
    
    // Loads grades from file
    private void loadGrades() {
        try {
            File file = new File(GRADES_FILE);
            if (!file.exists()) {
                return;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            
            while (line != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        String gradeId = parts[0];
                        String studentId = parts[1];
                        String assignmentId = parts[2];
                        double points = Double.parseDouble(parts[3]);
                        
                        Grade grade = new Grade(gradeId, studentId, assignmentId, points);
                        gradeService.addGrade(grade);
                    }
                }
                line = reader.readLine();
            }
            
            reader.close();
        } catch (IOException e) {
            System.err.println("Error loading grades: " + e.getMessage());
        }
    }
    
    // Saves grades to file
    private void saveGrades() {
        try {
            File dir = new File(DATA_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            PrintWriter writer = new PrintWriter(new FileWriter(GRADES_FILE));
            
            for (Grade grade : gradeService.getAllGrades()) {
                String line = grade.getGradeId() + "," + grade.getStudentId() + "," + 
                             grade.getAssignmentId() + "," + grade.getPoints();
                writer.println(line);
            }
            
            writer.close();
        } catch (IOException e) {
            System.err.println("Error saving grades: " + e.getMessage());
        }
    }
}

