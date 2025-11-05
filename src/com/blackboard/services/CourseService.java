package com.blackboard.services;

import com.blackboard.models.Course;
import com.blackboard.models.Student;
import com.blackboard.models.Teacher;
import java.util.ArrayList;

public class CourseService {
    private ArrayList<Course> courses;
    
    public CourseService() {
        this.courses = new ArrayList<>();
    }
    
    // Creates a new course
    public Course createCourse(String courseId, String courseName, String description, 
                              String teacherId, int capacity) {
        if (findCourseById(courseId) != null) {
            return null; // Course already exists
        }
        
        Course course = new Course(courseId, courseName, description, teacherId, capacity);
        courses.add(course);
        return course;
    }
    
    // Finds a course by its ID
    public Course findCourseById(String courseId) {
        for (Course course : courses) {
            if (course.getCourseId().equals(courseId)) {
                return course;
            }
        }
        return null;
    }
    
    // Returns all courses
    public ArrayList<Course> getAllCourses() {
        return courses;
    }
    
    // Updates a course
    public boolean updateCourse(Course updatedCourseData) {
        // Find the course that already exists in our list
        Course courseInList = findCourseById(updatedCourseData.getCourseId());
        
        // If the course exists in our list, update it with new data
        if (courseInList != null) {
            courseInList.setCourseName(updatedCourseData.getCourseName());
            courseInList.setDescription(updatedCourseData.getDescription());
            courseInList.setTeacherId(updatedCourseData.getTeacherId());
            courseInList.setCapacity(updatedCourseData.getCapacity());
            return true; // Successfully updated
        }
        
        return false; // Course not found, couldn't update
    }
    
    // Deletes a course
    public boolean deleteCourse(String courseId) {
        Course course = findCourseById(courseId);
        if (course != null) {
            courses.remove(course);
            return true;
        }
        return false;
    }
    
    // Assigns a teacher to a course
    public boolean assignTeacherToCourse(String courseId, Teacher teacher) {
        Course course = findCourseById(courseId);
        if (course != null && teacher != null) {
            course.setTeacherId(teacher.getUserId());
            teacher.assignCourse(course);
            return true;
        }
        return false;
    }
    
    // Enrolls a student in a course (checks capacity)
    public boolean enrollStudentInCourse(String courseId, Student student, 
                                        ArrayList<Student> allStudents) {
        Course course = findCourseById(courseId);
        if (course == null || student == null) {
            return false;
        }
        
        // Check if student is already enrolled
        if (student.getEnrolledCourses().contains(course)) {
            return false; // Already enrolled
        }
        
        // Check capacity by counting enrolled students
        int enrolledCount = 0;
        for (Student s : allStudents) {
            if (s.getEnrolledCourses().contains(course)) {
                enrolledCount++;
            }
        }
        
        // Check if course is full
        if (enrolledCount >= course.getCapacity()) {
            return false; // Course is full
        }
        
        // Enroll the student
        student.enrollInCourse(course);
        return true;
    }
    
    // Gets all courses for a specific teacher
    public ArrayList<Course> getCoursesForTeacher(String teacherId) {
        ArrayList<Course> teacherCourses = new ArrayList<>();
        for (Course course : courses) {
            if (course.getTeacherId() != null && course.getTeacherId().equals(teacherId)) {
                teacherCourses.add(course);
            }
        }
        return teacherCourses;
    }
    
    // Gets all courses for a specific student
    public ArrayList<Course> getCoursesForStudent(Student student) {
        if (student != null) {
            return student.getEnrolledCourses();
        }
        return new ArrayList<>();
    }
    
    // Adds a course (used by DataPersistenceService when loading data)
    public void addCourse(Course course) {
        if (course != null && findCourseById(course.getCourseId()) == null) {
            courses.add(course);
        }
    }
}
