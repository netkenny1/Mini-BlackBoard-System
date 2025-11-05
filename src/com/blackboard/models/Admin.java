package com.blackboard.models;

public class Admin extends User {
    
    public Admin(String userId, String password, String name) {
        super(userId, password, name, "ADMIN");
    }
    
    @Override
    public String toString() {
        return "Admin{" +
                "userId='" + getUserId() + '\'' +
                ", name='" + getName() + '\'' +
                '}';
    }
}
