package com.dammanhdungvn.quanlychitieucanhan.utils;

import android.text.TextUtils;
import android.util.Patterns;

import com.dammanhdungvn.quanlychitieucanhan.config.AppConfig;

/**
 * Utility class để validate dữ liệu đầu vào
 */
public class ValidationUtils {
    
    /**
     * Private constructor để prevent instantiation
     */
    private ValidationUtils() {
        throw new AssertionError("Cannot instantiate ValidationUtils");
    }
    
    // ==================== EMAIL VALIDATION ====================
    
    /**
     * Kiểm tra email có hợp lệ không
     */
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    
    /**
     * Validate email và trả về error message nếu có
     * @return null nếu valid, error message nếu invalid
     */
    public static String validateEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return "Vui lòng nhập email";
        }
        if (!isValidEmail(email)) {
            return "Email không hợp lệ";
        }
        return null;
    }
    
    // ==================== PASSWORD VALIDATION ====================
    
    /**
     * Kiểm tra password có hợp lệ không
     */
    public static boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= AppConfig.MIN_PASSWORD_LENGTH;
    }
    
    /**
     * Validate password và trả về error message nếu có
     * @return null nếu valid, error message nếu invalid
     */
    public static String validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return "Vui lòng nhập mật khẩu";
        }
        if (password.length() < AppConfig.MIN_PASSWORD_LENGTH) {
            return "Mật khẩu phải có ít nhất " + AppConfig.MIN_PASSWORD_LENGTH + " ký tự";
        }
        return null;
    }
    
    /**
     * Validate confirm password
     * @return null nếu valid, error message nếu invalid
     */
    public static String validateConfirmPassword(String password, String confirmPassword) {
        if (TextUtils.isEmpty(confirmPassword)) {
            return "Vui lòng xác nhận mật khẩu";
        }
        if (!password.equals(confirmPassword)) {
            return "Mật khẩu không khớp";
        }
        return null;
    }
    
    // ==================== NAME VALIDATION ====================
    
    /**
     * Kiểm tra display name có hợp lệ không
     */
    public static boolean isValidDisplayName(String name) {
        return !TextUtils.isEmpty(name) && name.trim().length() > 0;
    }
    
    /**
     * Validate display name và trả về error message nếu có
     * @return null nếu valid, error message nếu invalid
     */
    public static String validateDisplayName(String name) {
        if (TextUtils.isEmpty(name) || name.trim().isEmpty()) {
            return "Vui lòng nhập tên hiển thị";
        }
        return null;
    }
    
    // ==================== AMOUNT VALIDATION ====================
    
    /**
     * Kiểm tra số tiền có hợp lệ không
     */
    public static boolean isValidAmount(double amount) {
        return amount >= AppConfig.MIN_TRANSACTION_AMOUNT && 
               amount <= AppConfig.MAX_TRANSACTION_AMOUNT;
    }
    
    /**
     * Kiểm tra string amount có hợp lệ không
     */
    public static boolean isValidAmount(String amountStr) {
        if (TextUtils.isEmpty(amountStr)) {
            return false;
        }
        try {
            double amount = Double.parseDouble(amountStr);
            return isValidAmount(amount);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate amount và trả về error message nếu có
     * @return null nếu valid, error message nếu invalid
     */
    public static String validateAmount(String amountStr) {
        if (TextUtils.isEmpty(amountStr)) {
            return "Vui lòng nhập số tiền";
        }
        
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            return "Số tiền không hợp lệ";
        }
        
        if (amount <= 0) {
            return "Số tiền phải lớn hơn 0";
        }
        
        if (amount < AppConfig.MIN_TRANSACTION_AMOUNT) {
            return "Số tiền quá nhỏ";
        }
        
        if (amount > AppConfig.MAX_TRANSACTION_AMOUNT) {
            return "Số tiền quá lớn";
        }
        
        return null;
    }
    
    // ==================== NOTE VALIDATION ====================
    
    /**
     * Validate note (ghi chú)
     * @return null nếu valid, error message nếu invalid
     */
    public static String validateNote(String note) {
        if (note != null && note.length() > AppConfig.MAX_NOTE_LENGTH) {
            return "Ghi chú không được vượt quá " + AppConfig.MAX_NOTE_LENGTH + " ký tự";
        }
        return null;
    }
    
    // ==================== CATEGORY VALIDATION ====================
    
    /**
     * Kiểm tra category có được chọn không
     */
    public static boolean isValidCategory(String category) {
        return !TextUtils.isEmpty(category);
    }
    
    /**
     * Validate category và trả về error message nếu có
     * @return null nếu valid, error message nếu invalid
     */
    public static String validateCategory(String category) {
        if (TextUtils.isEmpty(category)) {
            return "Vui lòng chọn danh mục";
        }
        return null;
    }
    
    /**
     * Validate category name (cho custom category)
     * @return null nếu valid, error message nếu invalid
     */
    public static String validateCategoryName(String name) {
        if (TextUtils.isEmpty(name) || name.trim().isEmpty()) {
            return "Vui lòng nhập tên danh mục";
        }
        if (name.length() > AppConfig.MAX_CATEGORY_NAME_LENGTH) {
            return "Tên danh mục không được vượt quá " + AppConfig.MAX_CATEGORY_NAME_LENGTH + " ký tự";
        }
        return null;
    }
    
    // ==================== GENERAL VALIDATION ====================
    
    /**
     * Kiểm tra string có empty không
     */
    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }
    
    /**
     * Kiểm tra string có empty hoặc chỉ chứa whitespace không
     */
    public static boolean isBlank(String str) {
        return TextUtils.isEmpty(str) || str.trim().isEmpty();
    }
}

