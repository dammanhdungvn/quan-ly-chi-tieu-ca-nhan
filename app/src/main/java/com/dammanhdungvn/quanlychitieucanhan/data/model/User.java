package com.dammanhdungvn.quanlychitieucanhan.data.model;

import com.google.firebase.Timestamp;

/**
 * Model đại diện cho User trong hệ thống
 * 
 * Mapping với Firestore document structure:
 * users/{userId}
 */
public class User {
    private String userId;
    private String email;
    private String displayName;
    private Timestamp createdAt;
    
    /**
     * Constructor mặc định (required cho Firestore)
     */
    public User() {
    }
    
    /**
     * Constructor đầy đủ
     */
    public User(String userId, String email, String displayName, Timestamp createdAt) {
        this.userId = userId;
        this.email = email;
        this.displayName = displayName;
        this.createdAt = createdAt;
    }
    
    // ==================== GETTERS & SETTERS ====================
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    // ==================== UTILITY METHODS ====================
    
    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", displayName='" + displayName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

