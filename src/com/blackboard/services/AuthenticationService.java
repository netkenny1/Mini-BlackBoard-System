package com.blackboard.services;

import com.blackboard.models.User;
import com.blackboard.models.Student;
import com.blackboard.models.Teacher;
import java.util.ArrayList;

public class AuthenticationService {
    private ArrayList<User> users;
    
    public AuthenticationService() {
        this.users = new ArrayList<>();
    }
    
    // Login method to validate user credentials
    public User login(String userId, String password) {
        User user = findUserById(userId);
        
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        
        return null; // Invalid credentials
    }
    
    // Finds a user by their userId
    public User findUserById(String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
    
    // Adds a user to the system by data persistence service
    public void addUser(User user) {
        if (user != null && findUserById(user.getUserId()) == null) {
            users.add(user);
        }
    }
    
    // Returns all users in the system
    public ArrayList<User> getAllUsers() {
        return users;
    }
    
    // Updates a user
    public boolean updateUser(User updatedUser) {
        User existingUser = findUserById(updatedUser.getUserId());
        if (existingUser != null) {
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setName(updatedUser.getName());
            
            if (existingUser instanceof Student && updatedUser instanceof Student) {
                Student existingStudent = (Student) existingUser;
                Student updatedStudent = (Student) updatedUser;
                existingStudent.setMajor(updatedStudent.getMajor());
            } else if (existingUser instanceof Teacher && updatedUser instanceof Teacher) {
                Teacher existingTeacher = (Teacher) existingUser;
                Teacher updatedTeacher = (Teacher) updatedUser;
                existingTeacher.setDepartment(updatedTeacher.getDepartment());
            }
            
            return true;
        }
        return false;
    }
    
    // Deletes a user
    public boolean deleteUser(String userId) {
        User user = findUserById(userId);
        if (user != null) {
            users.remove(user);
            return true;
        }
        return false;
    }
    
    public void logout() {
        
    }
}
