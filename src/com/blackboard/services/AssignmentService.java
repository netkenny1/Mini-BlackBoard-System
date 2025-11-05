package com.blackboard.services;

import com.blackboard.models.Assignment;
import com.blackboard.models.Course;
import com.blackboard.models.Student;
import java.util.ArrayList;

public class AssignmentService {
    private ArrayList<Assignment> assignments;
    
    public AssignmentService() {
        this.assignments = new ArrayList<>();
    }
    
    // Creates a new assignment
    public Assignment createAssignment(String assignmentId, String courseId, String title,
                                       String description, String dueDate, double maxPoints) {
        if (findAssignmentById(assignmentId) != null) {
            return null; // Assignment already exists
        }
        
        Assignment assignment = new Assignment(assignmentId, courseId, title, description, dueDate, maxPoints);
        assignments.add(assignment);
        return assignment;
    }
    
    // Finds an assignment by its ID
    public Assignment findAssignmentById(String assignmentId) {
        for (Assignment assignment : assignments) {
            if (assignment.getAssignmentId().equals(assignmentId)) {
                return assignment;
            }
        }
        return null;
    }
    
    // Returns all assignments
    public ArrayList<Assignment> getAllAssignments() {
        return assignments;
    }
    
    // Updates an assignment
    public boolean updateAssignment(Assignment updatedAssignmentData) {
        // Find the assignment that already exists in our list
        Assignment assignmentInList = findAssignmentById(updatedAssignmentData.getAssignmentId());
        
        // If the assignment exists in our list, update it with new data
        if (assignmentInList != null) {
            assignmentInList.setCourseId(updatedAssignmentData.getCourseId());
            assignmentInList.setTitle(updatedAssignmentData.getTitle());
            assignmentInList.setDescription(updatedAssignmentData.getDescription());
            assignmentInList.setDueDate(updatedAssignmentData.getDueDate());
            assignmentInList.setMaxPoints(updatedAssignmentData.getMaxPoints());
            return true; // Successfully updated
        }
        
        return false; // Assignment not found, couldn't update
    }
    
    // Deletes an assignment
    public boolean deleteAssignment(String assignmentId) {
        Assignment assignment = findAssignmentById(assignmentId);
        if (assignment != null) {
            assignments.remove(assignment);
            return true;
        }
        return false;
    }
    
    // Gets all assignments for a specific course
    public ArrayList<Assignment> getAssignmentsForCourse(String courseId) {
        ArrayList<Assignment> courseAssignments = new ArrayList<>();
        for (Assignment assignment : assignments) {
            if (assignment.getCourseId().equals(courseId)) {
                courseAssignments.add(assignment);
            }
        }
        return courseAssignments;
    }
    
    // Gets all assignments for a student (from all courses they're enrolled in)
    public ArrayList<Assignment> getAssignmentsForStudent(Student student) {
        ArrayList<Assignment> studentAssignments = new ArrayList<>();
        
        if (student == null) {
            return studentAssignments;
        }
        
        // Loop through all courses the student is enrolled in
        for (Course course : student.getEnrolledCourses()) {
            // Get all assignments for this course
            ArrayList<Assignment> courseAssignments = getAssignmentsForCourse(course.getCourseId());
            // Add them to the student's assignments list
            studentAssignments.addAll(courseAssignments);
        }
        
        return studentAssignments;
    }
    
    // Adds an assignment (used by DataPersistenceService when loading data)
    public void addAssignment(Assignment assignment) {
        if (assignment != null && findAssignmentById(assignment.getAssignmentId()) == null) {
            assignments.add(assignment);
        }
    }
}
