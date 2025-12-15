package com.dammanhdungvn.quanlychitieucanhan.data.model;

import com.google.firebase.Timestamp;

/**
 * Model đại diện cho Category (Danh mục)
 * 
 * Category có thể là:
 * - Predefined: Danh mục có sẵn trong app
 * - Custom: Danh mục do user tự tạo
 */
public class Category {
    private String categoryId;
    private String name;                // Tên danh mục
    private String categoryType;        // "income" hoặc "expense"
    private String iconName;            // Tên icon (material icon name)
    private boolean isPredefined;       // true nếu là danh mục có sẵn
    private Timestamp createdAt;        // Thời gian tạo (chỉ cho custom categories)
    
    /**
     * Constructor mặc định (required cho Firestore)
     */
    public Category() {
    }
    
    /**
     * Constructor cho predefined category
     */
    public Category(String categoryId, String name, String categoryType, String iconName) {
        this.categoryId = categoryId;
        this.name = name;
        this.categoryType = categoryType;
        this.iconName = iconName;
        this.isPredefined = true;
        this.createdAt = null;
    }
    
    /**
     * Constructor cho custom category
     */
    public Category(String categoryId, String name, String categoryType, String iconName, Timestamp createdAt) {
        this.categoryId = categoryId;
        this.name = name;
        this.categoryType = categoryType;
        this.iconName = iconName;
        this.isPredefined = false;
        this.createdAt = createdAt;
    }
    
    // ==================== GETTERS & SETTERS ====================
    
    public String getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCategoryType() {
        return categoryType;
    }
    
    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
    
    public String getIconName() {
        return iconName;
    }
    
    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
    
    public boolean isPredefined() {
        return isPredefined;
    }
    
    public void setPredefined(boolean predefined) {
        isPredefined = predefined;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Kiểm tra xem category này có phải cho thu nhập không
     */
    public boolean isIncomeCategory() {
        return "income".equals(categoryType);
    }
    
    /**
     * Kiểm tra xem category này có phải cho chi tiêu không
     */
    public boolean isExpenseCategory() {
        return "expense".equals(categoryType);
    }
    
    @Override
    public String toString() {
        return "Category{" +
                "categoryId='" + categoryId + '\'' +
                ", name='" + name + '\'' +
                ", categoryType='" + categoryType + '\'' +
                ", iconName='" + iconName + '\'' +
                ", isPredefined=" + isPredefined +
                ", createdAt=" + createdAt +
                '}';
    }
}

