package com.blackboard.models;
import java.util.ArrayList;


public class Student extends User {
    private String major;                              
    private ArrayList<Course> enrolledCourses;       
    
    public Student(String userId, String password, String name, String major) {
        super(userId, password, name, "STUDENT");  
        
        this.major = major;
        this.enrolledCourses = new ArrayList<>();  
    }

    public String getMajor() {
        return major;
    }
    
    public void setMajor(String major) {
        this.major = major;
    }
    
    public ArrayList<Course> getEnrolledCourses() {
        return enrolledCourses;
    }
    
    public void setEnrolledCourses(ArrayList<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public void enrollInCourse(Course course) {
        enrolledCourses.add(course);
    }

    @Override
    public String toString() {
        return "Student{" +
                "userId='" + getUserId() + '\'' +
                ", name='" + getName() + '\'' +
                ", major='" + major + '\'' +
                ", enrolledCourses=" + enrolledCourses.size() + " courses" +
                '}';
    }
}





