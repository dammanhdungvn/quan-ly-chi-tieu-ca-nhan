package com.dammanhdungvn.quanlychitieucanhan.config;

import com.dammanhdungvn.quanlychitieucanhan.BuildConfig;

/**
 * Cấu hình trung tâm của ứng dụng
 * 
 * Class này quản lý tất cả các configuration constants:
 * - Tên collections và subcollections trong Firestore
 * - Đường dẫn Storage
 * - Feature flags
 * - Environment settings
 * 
 * Nguyên tắc: KHÔNG BAO GIỜ hard-code strings trong business logic.
 * Tất cả phải thông qua AppConfig.
 */
public class AppConfig {
    
    // ==================== FIRESTORE COLLECTIONS ====================
    
    /**
     * Tên collection chính chứa thông tin users
     */
    public static final String USERS_COLLECTION = "users";
    
    /**
     * Tên subcollection chứa transactions của mỗi user
     */
    public static final String TRANSACTIONS_SUBCOLLECTION = "transactions";
    
    /**
     * Tên subcollection chứa custom categories của mỗi user
     */
    public static final String CUSTOM_CATEGORIES_SUBCOLLECTION = "customCategories";
    
    // ==================== FIREBASE STORAGE PATHS ====================
    
    /**
     * Đường dẫn root cho receipts trong Storage
     * Format: receipts/{userId}/{transactionId}.jpg
     */
    public static final String STORAGE_RECEIPTS_PATH = "receipts";
    
    // ==================== TRANSACTION TYPES ====================
    
    /**
     * Loại giao dịch: Thu nhập
     */
    public static final String TRANSACTION_TYPE_INCOME = "income";
    
    /**
     * Loại giao dịch: Chi tiêu
     */
    public static final String TRANSACTION_TYPE_EXPENSE = "expense";
    
    // ==================== FEATURE FLAGS ====================
    
    /**
     * Bật/tắt tính năng upload ảnh hóa đơn
     * Hiện tại: TẮT (theo yêu cầu plan)
     */
    public static final boolean ENABLE_RECEIPT_UPLOAD = false;
    
    /**
     * Bật/tắt chế độ offline support
     */
    public static final boolean ENABLE_OFFLINE_MODE = true;
    
    /**
     * Số lượng transactions hiển thị trên trang Home
     */
    public static final int HOME_RECENT_TRANSACTIONS_LIMIT = 10;
    
    /**
     * Số lượng transactions tải mỗi lần (pagination)
     */
    public static final int TRANSACTIONS_PAGE_SIZE = 50;
    
    // ==================== VALIDATION RULES ====================
    
    /**
     * Độ dài tối thiểu của mật khẩu
     */
    public static final int MIN_PASSWORD_LENGTH = 6;
    
    /**
     * Độ dài tối đa của ghi chú
     */
    public static final int MAX_NOTE_LENGTH = 500;
    
    /**
     * Độ dài tối đa của tên danh mục
     */
    public static final int MAX_CATEGORY_NAME_LENGTH = 50;
    
    /**
     * Số tiền tối thiểu cho giao dịch (VND)
     */
    public static final double MIN_TRANSACTION_AMOUNT = 0.01;
    
    /**
     * Số tiền tối đa cho giao dịch (VND)
     */
    public static final double MAX_TRANSACTION_AMOUNT = 999999999999.99;
    
    // ==================== ENVIRONMENT ====================
    
    /**
     * Kiểm tra app đang chạy ở chế độ debug hay không
     */
    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }
    
    /**
     * Bật/tắt logging (chỉ bật khi debug)
     */
    public static boolean isLoggingEnabled() {
        return BuildConfig.DEBUG;
    }
    
    // ==================== DATE FORMAT ====================
    
    /**
     * Format ngày hiển thị
     */
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    
    /**
     * Format ngày giờ hiển thị
     */
    public static final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm";
    
    /**
     * Format ngày cho Firestore queries
     */
    public static final String FIRESTORE_DATE_FORMAT = "yyyy-MM-dd";
    
    // ==================== CURRENCY ====================
    
    /**
     * Ký hiệu tiền tệ
     */
    public static final String CURRENCY_SYMBOL = "VNĐ";
    
    /**
     * Locale cho format tiền tệ (Vietnamese)
     */
    public static final String CURRENCY_LOCALE = "vi-VN";
    
    // ==================== FIRESTORE FIELD NAMES ====================
    // Để đảm bảo consistency và tránh typo
    
    // User fields
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_DISPLAY_NAME = "displayName";
    public static final String FIELD_CREATED_AT = "createdAt";
    
    // Transaction fields
    public static final String FIELD_TRANSACTION_ID = "transactionId";
    public static final String FIELD_AMOUNT = "amount";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_CATEGORY = "category";
    public static final String FIELD_NOTE = "note";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_IMAGE_URL = "imageUrl";
    
    // Category fields
    public static final String FIELD_CATEGORY_ID = "categoryId";
    public static final String FIELD_CATEGORY_NAME = "name";
    public static final String FIELD_CATEGORY_TYPE = "categoryType";
    public static final String FIELD_ICON_NAME = "iconName";
    
    // ==================== ERROR CODES ====================
    
    public static final int ERROR_NETWORK = 1001;
    public static final int ERROR_AUTH = 1002;
    public static final int ERROR_FIRESTORE = 1003;
    public static final int ERROR_STORAGE = 1004;
    public static final int ERROR_VALIDATION = 1005;
    public static final int ERROR_UNKNOWN = 9999;
    
    // ==================== CONSTRUCTOR ====================
    
    /**
     * Private constructor để prevent instantiation
     * Đây là utility class, không cần tạo instance
     */
    private AppConfig() {
        throw new AssertionError("Cannot instantiate AppConfig");
    }
}

