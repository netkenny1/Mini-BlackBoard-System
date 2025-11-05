package com.blackboard.services;

import com.blackboard.models.Grade;
import com.blackboard.models.Assignment;
import java.util.ArrayList;

public class GradeService {
    private ArrayList<Grade> grades;
    
    public GradeService() {
        this.grades = new ArrayList<>();
    }
    
    // Creates a new grade
    public Grade createGrade(String gradeId, String studentId, String assignmentId, double points) {
        if (findGradeById(gradeId) != null) {
            return null; // Grade already exists
        }
        
        Grade grade = new Grade(gradeId, studentId, assignmentId, points);
        grades.add(grade);
        return grade;
    }
    
    // Finds a grade by its ID
    public Grade findGradeById(String gradeId) {
        for (Grade grade : grades) {
            if (grade.getGradeId().equals(gradeId)) {
                return grade;
            }
        }
        return null;
    }
    
    // Returns all grades
    public ArrayList<Grade> getAllGrades() {
        return grades;
    }
    
    // Updates a grade
    public boolean updateGrade(Grade updatedGradeData) {
        // Find the grade that already exists in our list
        Grade gradeInList = findGradeById(updatedGradeData.getGradeId());
        
        // If the grade exists in our list, update it with new data
        if (gradeInList != null) {
            gradeInList.setStudentId(updatedGradeData.getStudentId());
            gradeInList.setAssignmentId(updatedGradeData.getAssignmentId());
            gradeInList.setPoints(updatedGradeData.getPoints());
            return true; // Successfully updated
        }
        
        return false; // Grade not found, couldn't update
    }
    
    // Deletes a grade
    public boolean deleteGrade(String gradeId) {
        Grade grade = findGradeById(gradeId);
        if (grade != null) {
            grades.remove(grade);
            return true;
        }
        return false;
    }
    
    // Gets all grades for a specific student
    public ArrayList<Grade> getGradesForStudent(String studentId) {
        ArrayList<Grade> studentGrades = new ArrayList<>();
        for (Grade grade : grades) {
            if (grade.getStudentId().equals(studentId)) {
                studentGrades.add(grade);
            }
        }
        return studentGrades;
    }
    
    // Gets all grades for a specific assignment
    public ArrayList<Grade> getGradesForAssignment(String assignmentId) {
        ArrayList<Grade> assignmentGrades = new ArrayList<>();
        for (Grade grade : grades) {
            if (grade.getAssignmentId().equals(assignmentId)) {
                assignmentGrades.add(grade);
            }
        }
        return assignmentGrades;
    }
    
    // Gets a specific grade for a student and assignment
    public Grade getGradeForStudentAndAssignment(String studentId, String assignmentId) {
        for (Grade grade : grades) {
            if (grade.getStudentId().equals(studentId) && grade.getAssignmentId().equals(assignmentId)) {
                return grade;
            }
        }
        return null;
    }
    
    // Calculates final grade for a student in a course
    // Returns the percentage (points earned / total points possible)
    public double calculateFinalGradeForCourse(String studentId, String courseId, 
                                               ArrayList<Assignment> courseAssignments) {
        if (courseAssignments == null || courseAssignments.isEmpty()) {
            return 0.0;
        }
        
        double totalPointsEarned = 0.0;
        double totalPointsPossible = 0.0;
        
        // Loop through all assignments in the course
        for (Assignment assignment : courseAssignments) {
            totalPointsPossible += assignment.getMaxPoints();
            
            // Find grade for this student and assignment
            Grade grade = getGradeForStudentAndAssignment(studentId, assignment.getAssignmentId());
            if (grade != null) {
                totalPointsEarned += grade.getPoints();
            }
        }
        
        // Calculate percentage
        if (totalPointsPossible == 0) {
            return 0.0;
        }
        
        return (totalPointsEarned / totalPointsPossible) * 100.0;
    }
    
    // Adds a grade (used by DataPersistenceService when loading data)
    public void addGrade(Grade grade) {
        if (grade != null && findGradeById(grade.getGradeId()) == null) {
            grades.add(grade);
        }
    }
}

