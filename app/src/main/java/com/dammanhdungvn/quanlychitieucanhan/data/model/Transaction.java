package com.dammanhdungvn.quanlychitieucanhan.data.model;

import com.google.firebase.Timestamp;

/**
 * Model đại diện cho Transaction (Giao dịch)
 * 
 * Mapping với Firestore document structure:
 * users/{userId}/transactions/{transactionId}
 */
public class Transaction {
    private String transactionId;
    private double amount;              // Số tiền
    private String type;                // "income" hoặc "expense"
    private String category;            // Danh mục (Ăn uống, Di chuyển, etc.)
    private String note;                // Ghi chú
    private Timestamp date;             // Ngày giao dịch
    private String imageUrl;            // URL ảnh hóa đơn (nullable)
    private Timestamp createdAt;        // Thời gian tạo
    
    /**
     * Constructor mặc định (required cho Firestore)
     */
    public Transaction() {
    }
    
    /**
     * Constructor đầy đủ
     */
    public Transaction(String transactionId, double amount, String type, String category, 
                      String note, Timestamp date, String imageUrl, Timestamp createdAt) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.note = note;
        this.date = date;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }
    
    /**
     * Constructor không có imageUrl (cho trường hợp không upload ảnh)
     */
    public Transaction(String transactionId, double amount, String type, String category, 
                      String note, Timestamp date, Timestamp createdAt) {
        this(transactionId, amount, type, category, note, date, null, createdAt);
    }
    
    // ==================== GETTERS & SETTERS ====================
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getNote() {
        return note;
    }
    
    public void setNote(String note) {
        this.note = note;
    }
    
    public Timestamp getDate() {
        return date;
    }
    
    public void setDate(Timestamp date) {
        this.date = date;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Kiểm tra xem transaction này có phải thu nhập không
     */
    public boolean isIncome() {
        return "income".equals(type);
    }
    
    /**
     * Kiểm tra xem transaction này có phải chi tiêu không
     */
    public boolean isExpense() {
        return "expense".equals(type);
    }
    
    /**
     * Lấy số tiền với dấu (+ cho income, - cho expense)
     */
    public double getSignedAmount() {
        return isIncome() ? amount : -amount;
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", category='" + category + '\'' +
                ", note='" + note + '\'' +
                ", date=" + date +
                ", imageUrl='" + imageUrl + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

