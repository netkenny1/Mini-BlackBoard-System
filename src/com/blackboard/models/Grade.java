package com.blackboard.models;

public class Grade {
    private String gradeId;
    private String studentId;
    private String assignmentId;
    private double points;
    
    public Grade(String gradeId, String studentId, String assignmentId, double points) {
        this.gradeId = gradeId;
        this.studentId = studentId;
        this.assignmentId = assignmentId;
        this.points = points;
    }
    
    public String getGradeId() {
        return gradeId;
    }
    
    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getAssignmentId() {
        return assignmentId;
    }
    
    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }
    
    public double getPoints() {
        return points;
    }
    
    public void setPoints(double points) {
        this.points = points;
    }
    
    @Override
    public String toString() {
        return "Grade{" +
                "gradeId='" + gradeId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", assignmentId='" + assignmentId + '\'' +
                ", points=" + points +
                '}';
    }
}

