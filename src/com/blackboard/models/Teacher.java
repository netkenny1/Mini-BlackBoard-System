package com.blackboard.models;

import java.util.ArrayList;

public class Teacher extends User {

    private String department;                         
    private ArrayList<Course> assignedCourses;       

    public Teacher(String userId, String password, String name, String department) {
 
        super(userId, password, name, "TEACHER");  
        
        this.department = department;
        this.assignedCourses = new ArrayList<>();  
    }
    
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public ArrayList<Course> getAssignedCourses() {
        return assignedCourses;
    }
    
    public void setAssignedCourses(ArrayList<Course> assignedCourses) {
        this.assignedCourses = assignedCourses;
    }
    
    
    public void assignCourse(Course course) {
        if (!assignedCourses.contains(course)) {
            assignedCourses.add(course);
        }
    }
    
    
    @Override
    public String toString() {
        return "Teacher{" +
                "userId='" + getUserId() + '\'' +
                ", name='" + getName() + '\'' +
                ", department='" + department + '\'' +
                ", assignedCourses=" + assignedCourses.size() + " courses" +
                '}';
    }
}