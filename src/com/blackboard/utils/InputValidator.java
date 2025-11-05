package com.blackboard.utils;

public class InputValidator {
    
    // Checks if a string is not empty
    public static boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }
    
    // Validates user ID format (should not be empty)
    public static boolean isValidUserId(String userId) {
        return isNotEmpty(userId);
    }
    
    // Validates password (should be at least 3 characters)
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 3;
    }
    
    // Validates email format (must contain @ and .)
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.contains("@") && email.contains(".");
    }
    
    // Validates if a string can be converted to an integer
    public static boolean isValidInteger(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    // Validates if a string can be converted to a double
    public static boolean isValidDouble(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    // Validates course ID (should not be empty)
    public static boolean isValidCourseId(String courseId) {
        return isNotEmpty(courseId);
    }
    
    // Validates assignment ID (should not be empty)
    public static boolean isValidAssignmentId(String assignmentId) {
        return isNotEmpty(assignmentId);
    }
    
    // Validates grade points (should be between 0 and 100)
    public static boolean isValidGradePoints(double points) {
        return points >= 0 && points <= 100;
    }
    
    // Validates capacity (should be positive)
    public static boolean isValidCapacity(int capacity) {
        return capacity > 0;
    }
}

