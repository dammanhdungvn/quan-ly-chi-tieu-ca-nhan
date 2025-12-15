package com.dammanhdungvn.quanlychitieucanhan.data.datasource;

import com.dammanhdungvn.quanlychitieucanhan.config.AppConfig;
import com.dammanhdungvn.quanlychitieucanhan.data.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Datasource chứa danh sách categories có sẵn trong app
 * 
 * Categories được phân thành 2 loại:
 * - Income categories: Cho giao dịch thu nhập
 * - Expense categories: Cho giao dịch chi tiêu
 */
public class PredefinedCategories {
    
    /**
     * Private constructor để prevent instantiation
     */
    private PredefinedCategories() {
        throw new AssertionError("Cannot instantiate PredefinedCategories");
    }
    
    // ==================== INCOME CATEGORIES ====================
    
    /**
     * Lấy danh sách income categories có sẵn
     */
    public static List<Category> getIncomeCategories() {
        List<Category> categories = new ArrayList<>();
        
        categories.add(new Category(
                "income_salary",
                "Tiền lương",
                AppConfig.TRANSACTION_TYPE_INCOME,
                "payments"
        ));
        
        categories.add(new Category(
                "income_business",
                "Kinh doanh",
                AppConfig.TRANSACTION_TYPE_INCOME,
                "business_center"
        ));
        
        categories.add(new Category(
                "income_investment",
                "Đầu tư",
                AppConfig.TRANSACTION_TYPE_INCOME,
                "trending_up"
        ));
        
        categories.add(new Category(
                "income_gift",
                "Quà tặng",
                AppConfig.TRANSACTION_TYPE_INCOME,
                "card_giftcard"
        ));
        
        categories.add(new Category(
                "income_other",
                "Thu nhập khác",
                AppConfig.TRANSACTION_TYPE_INCOME,
                "attach_money"
        ));
        
        return categories;
    }
    
    // ==================== EXPENSE CATEGORIES ====================
    
    /**
     * Lấy danh sách expense categories có sẵn
     */
    public static List<Category> getExpenseCategories() {
        List<Category> categories = new ArrayList<>();
        
        categories.add(new Category(
                "expense_food",
                "Ăn uống",
                AppConfig.TRANSACTION_TYPE_EXPENSE,
                "restaurant"
        ));
        
        categories.add(new Category(
                "expense_transport",
                "Di chuyển",
                AppConfig.TRANSACTION_TYPE_EXPENSE,
                "directions_car"
        ));
        
        categories.add(new Category(
                "expense_education",
                "Học tập",
                AppConfig.TRANSACTION_TYPE_EXPENSE,
                "school"
        ));
        
        categories.add(new Category(
                "expense_entertainment",
                "Giải trí",
                AppConfig.TRANSACTION_TYPE_EXPENSE,
                "movie"
        ));
        
        categories.add(new Category(
                "expense_health",
                "Sức khỏe",
                AppConfig.TRANSACTION_TYPE_EXPENSE,
                "local_hospital"
        ));
        
        categories.add(new Category(
                "expense_shopping",
                "Mua sắm",
                AppConfig.TRANSACTION_TYPE_EXPENSE,
                "shopping_cart"
        ));
        
        categories.add(new Category(
                "expense_bills",
                "Hóa đơn",
                AppConfig.TRANSACTION_TYPE_EXPENSE,
                "receipt"
        ));
        
        categories.add(new Category(
                "expense_other",
                "Chi tiêu khác",
                AppConfig.TRANSACTION_TYPE_EXPENSE,
                "more_horiz"
        ));
        
        return categories;
    }
    
    // ==================== ALL CATEGORIES ====================
    
    /**
     * Lấy tất cả categories (income + expense)
     */
    public static List<Category> getAllPredefinedCategories() {
        List<Category> categories = new ArrayList<>();
        categories.addAll(getIncomeCategories());
        categories.addAll(getExpenseCategories());
        return categories;
    }
    
    /**
     * Lấy categories theo type
     */
    public static List<Category> getCategoriesByType(String type) {
        if (AppConfig.TRANSACTION_TYPE_INCOME.equals(type)) {
            return getIncomeCategories();
        } else if (AppConfig.TRANSACTION_TYPE_EXPENSE.equals(type)) {
            return getExpenseCategories();
        }
        return new ArrayList<>();
    }
    
    /**
     * Tìm category theo ID
     */
    public static Category getCategoryById(String categoryId) {
        List<Category> allCategories = getAllPredefinedCategories();
        for (Category category : allCategories) {
            if (category.getCategoryId().equals(categoryId)) {
                return category;
            }
        }
        return null;
    }
    
    /**
     * Tìm category theo tên
     */
    public static Category getCategoryByName(String name) {
        List<Category> allCategories = getAllPredefinedCategories();
        for (Category category : allCategories) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }
}

