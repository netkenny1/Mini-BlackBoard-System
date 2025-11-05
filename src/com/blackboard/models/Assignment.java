package com.blackboard.models;

public class Assignment {
    private String assignmentId;
    private String courseId;
    private String title;
    private String description;
    private String dueDate;
    private double maxPoints;
    
    public Assignment(String assignmentId, String courseId, String title, 
                     String description, String dueDate, double maxPoints) {
        this.assignmentId = assignmentId;
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.maxPoints = maxPoints;
    }
    
    
    public String getAssignmentId() {
        return assignmentId;
    }
    
    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }
    
    public String getCourseId() {
        return courseId;
    }
    
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
    
    public double getMaxPoints() {
        return maxPoints;
    }
    
    public void setMaxPoints(double maxPoints) {
        this.maxPoints = maxPoints;
    }
    
    @Override
    public String toString() {
        return "Assignment{" +
                "assignmentId='" + assignmentId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", title='" + title + '\'' +
                ", maxPoints=" + maxPoints +
                ", dueDate='" + dueDate + '\'' +
                '}';
    }
}
