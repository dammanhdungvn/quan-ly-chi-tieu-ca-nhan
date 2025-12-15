package com.dammanhdungvn.quanlychitieucanhan.data.repository;

import com.dammanhdungvn.quanlychitieucanhan.config.AppConfig;
import com.dammanhdungvn.quanlychitieucanhan.data.datasource.PredefinedCategories;
import com.dammanhdungvn.quanlychitieucanhan.data.model.Category;
import com.dammanhdungvn.quanlychitieucanhan.firebase.AuthManager;
import com.dammanhdungvn.quanlychitieucanhan.firebase.FirestoreManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository layer cho Category operations
 * 
 * Quản lý cả predefined categories và custom categories
 */
public class CategoryRepository {
    
    private static CategoryRepository instance;
    private final FirestoreManager firestoreManager;
    private final AuthManager authManager;
    
    /**
     * Private constructor (Singleton)
     */
    private CategoryRepository() {
        this.firestoreManager = FirestoreManager.getInstance();
        this.authManager = AuthManager.getInstance();
    }
    
    /**
     * Lấy instance của CategoryRepository (Singleton)
     */
    public static synchronized CategoryRepository getInstance() {
        if (instance == null) {
            instance = new CategoryRepository();
        }
        return instance;
    }
    
    // ==================== CALLBACK INTERFACES ====================
    
    /**
     * Callback cho các thao tác thành công/thất bại
     */
    public interface OperationCallback {
        void onSuccess();
        void onError(String errorMessage);
    }
    
    /**
     * Callback cho việc load categories
     */
    public interface CategoriesCallback {
        void onSuccess(List<Category> categories);
        void onError(String errorMessage);
    }
    
    // ==================== CATEGORY OPERATIONS ====================
    
    /**
     * Lấy tất cả categories (predefined + custom) theo type
     */
    public void getCategoriesByType(String type, final CategoriesCallback callback) {
        // Lấy predefined categories
        List<Category> predefinedCategories = PredefinedCategories.getCategoriesByType(type);
        
        // Lấy custom categories
        String userId = authManager.getCurrentUserId();
        if (userId == null) {
            // Nếu chưa đăng nhập, chỉ trả về predefined
            callback.onSuccess(predefinedCategories);
            return;
        }
        
        firestoreManager.getCustomCategories(userId, new FirestoreManager.ListReadCallback<Category>() {
            @Override
            public void onSuccess(List<Category> customCategories) {
                // Merge predefined và custom categories
                List<Category> allCategories = new ArrayList<>(predefinedCategories);
                
                // Filter custom categories theo type
                for (Category category : customCategories) {
                    if (type.equals(category.getCategoryType())) {
                        allCategories.add(category);
                    }
                }
                
                callback.onSuccess(allCategories);
            }
            
            @Override
            public void onFailure(Exception e) {
                // Nếu load custom categories thất bại, vẫn trả về predefined
                callback.onSuccess(predefinedCategories);
            }
        });
    }
    
    /**
     * Lấy tất cả income categories
     */
    public void getIncomeCategories(final CategoriesCallback callback) {
        getCategoriesByType(AppConfig.TRANSACTION_TYPE_INCOME, callback);
    }
    
    /**
     * Lấy tất cả expense categories
     */
    public void getExpenseCategories(final CategoriesCallback callback) {
        getCategoriesByType(AppConfig.TRANSACTION_TYPE_EXPENSE, callback);
    }
    
    /**
     * Lấy tất cả categories (cả income và expense)
     */
    public void getAllCategories(final CategoriesCallback callback) {
        // Lấy tất cả predefined categories
        List<Category> predefinedCategories = PredefinedCategories.getAllPredefinedCategories();
        
        // Lấy custom categories
        String userId = authManager.getCurrentUserId();
        if (userId == null) {
            // Nếu chưa đăng nhập, chỉ trả về predefined
            callback.onSuccess(predefinedCategories);
            return;
        }
        
        firestoreManager.getCustomCategories(userId, new FirestoreManager.ListReadCallback<Category>() {
            @Override
            public void onSuccess(List<Category> customCategories) {
                // Merge predefined và custom categories
                List<Category> allCategories = new ArrayList<>(predefinedCategories);
                allCategories.addAll(customCategories);
                callback.onSuccess(allCategories);
            }
            
            @Override
            public void onFailure(Exception e) {
                // Nếu load custom categories thất bại, vẫn trả về predefined
                callback.onSuccess(predefinedCategories);
            }
        });
    }
    
    /**
     * Thêm custom category
     */
    public void addCustomCategory(Category category, final OperationCallback callback) {
        String userId = authManager.getCurrentUserId();
        if (userId == null) {
            callback.onError("Người dùng chưa đăng nhập");
            return;
        }
        
        // Kiểm tra category name đã tồn tại chưa
        getCategoriesByType(category.getCategoryType(), new CategoriesCallback() {
            @Override
            public void onSuccess(List<Category> categories) {
                // Check duplicate name
                for (Category existingCategory : categories) {
                    if (existingCategory.getName().equalsIgnoreCase(category.getName())) {
                        callback.onError("Danh mục này đã tồn tại");
                        return;
                    }
                }
                
                // Add category to Firestore
                firestoreManager.addCustomCategory(userId, category, new FirestoreManager.WriteCallback() {
                    @Override
                    public void onSuccess() {
                        callback.onSuccess();
                    }
                    
                    @Override
                    public void onFailure(Exception e) {
                        callback.onError(getErrorMessage(e));
                    }
                });
            }
            
            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }
    
    /**
     * Tìm category theo name (search cả predefined và custom)
     */
    public void findCategoryByName(String name, final CategoriesCallback callback) {
        getAllCategories(new CategoriesCallback() {
            @Override
            public void onSuccess(List<Category> categories) {
                List<Category> matchedCategories = new ArrayList<>();
                String searchTerm = name.toLowerCase();
                
                for (Category category : categories) {
                    if (category.getName().toLowerCase().contains(searchTerm)) {
                        matchedCategories.add(category);
                    }
                }
                
                callback.onSuccess(matchedCategories);
            }
            
            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }
    
    /**
     * Lấy category names (chỉ lấy tên) theo type
     */
    public void getCategoryNames(String type, final CategoryNamesCallback callback) {
        getCategoriesByType(type, new CategoriesCallback() {
            @Override
            public void onSuccess(List<Category> categories) {
                List<String> names = new ArrayList<>();
                for (Category category : categories) {
                    names.add(category.getName());
                }
                callback.onSuccess(names);
            }
            
            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }
    
    /**
     * Callback cho category names
     */
    public interface CategoryNamesCallback {
        void onSuccess(List<String> categoryNames);
        void onError(String errorMessage);
    }
    
    // ==================== HELPER METHODS ====================
    
    /**
     * Convert Exception sang error message dễ hiểu
     */
    private String getErrorMessage(Exception e) {
        if (e == null) {
            return "Đã xảy ra lỗi không xác định";
        }
        
        String message = e.getMessage();
        if (message == null || message.isEmpty()) {
            return "Đã xảy ra lỗi không xác định";
        }
        
        // Parse Firebase error messages
        if (message.contains("network")) {
            return "Lỗi kết nối mạng. Vui lòng kiểm tra internet";
        } else if (message.contains("permission")) {
            return "Không có quyền truy cập dữ liệu";
        } else if (message.contains("not found")) {
            return "Không tìm thấy dữ liệu";
        }
        
        return message;
    }
}

