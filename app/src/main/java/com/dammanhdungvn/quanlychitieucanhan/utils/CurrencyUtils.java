package com.dammanhdungvn.quanlychitieucanhan.utils;

import com.dammanhdungvn.quanlychitieucanhan.config.AppConfig;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utility class để format tiền tệ
 */
public class CurrencyUtils {
    
    private static final Locale LOCALE = new Locale(AppConfig.CURRENCY_LOCALE);
    private static final DecimalFormat DECIMAL_FORMAT;
    
    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(LOCALE);
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        
        DECIMAL_FORMAT = new DecimalFormat("#,###", symbols);
    }
    
    /**
     * Private constructor để prevent instantiation
     */
    private CurrencyUtils() {
        throw new AssertionError("Cannot instantiate CurrencyUtils");
    }
    
    // ==================== FORMAT METHODS ====================
    
    /**
     * Format số tiền theo định dạng VND
     * Ví dụ: 1000000 -> "1.000.000 đ"
     */
    public static String format(double amount) {
        String formatted = DECIMAL_FORMAT.format(amount);
        return formatted + " " + AppConfig.CURRENCY_SYMBOL;
    }
    
    /**
     * Format số tiền với dấu +/- dựa trên loại transaction
     * Ví dụ: 
     *   - income: +1.000.000 đ
     *   - expense: -1.000.000 đ
     */
    public static String formatWithSign(double amount, boolean isIncome) {
        String sign = isIncome ? "+" : "-";
        String formatted = DECIMAL_FORMAT.format(Math.abs(amount));
        return sign + formatted + " " + AppConfig.CURRENCY_SYMBOL;
    }
    
    /**
     * Format số tiền với dấu +/- dựa trên sign của amount
     * Ví dụ:
     *   - positive: +1.000.000 đ
     *   - negative: -1.000.000 đ
     */
    public static String formatWithSign(double amount) {
        String sign = amount >= 0 ? "+" : "-";
        String formatted = DECIMAL_FORMAT.format(Math.abs(amount));
        return sign + formatted + " " + AppConfig.CURRENCY_SYMBOL;
    }
    
    /**
     * Format số tiền không có ký hiệu tiền tệ
     * Ví dụ: 1000000 -> "1.000.000"
     */
    public static String formatWithoutSymbol(double amount) {
        return DECIMAL_FORMAT.format(amount);
    }
    
    /**
     * Format số tiền ngắn gọn (K, M, B)
     * Ví dụ: 
     *   - 1000000 -> "1M"
     *   - 50000 -> "50K"
     */
    public static String formatCompact(double amount) {
        if (amount >= 1_000_000_000) {
            return String.format(LOCALE, "%.1fB đ", amount / 1_000_000_000);
        } else if (amount >= 1_000_000) {
            return String.format(LOCALE, "%.1fM đ", amount / 1_000_000);
        } else if (amount >= 1_000) {
            return String.format(LOCALE, "%.1fK đ", amount / 1_000);
        } else {
            return format(amount);
        }
    }
    
    // ==================== PARSE METHODS ====================
    
    /**
     * Parse string thành số tiền
     * Xử lý được cả format có dấu phân cách
     */
    public static double parse(String amountStr) throws NumberFormatException {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            throw new NumberFormatException("Amount string is null or empty");
        }
        
        // Loại bỏ currency symbol và spaces
        String cleaned = amountStr.replace(AppConfig.CURRENCY_SYMBOL, "")
                                  .replace(" ", "")
                                  .trim();
        
        // Loại bỏ dấu phân cách hàng nghìn
        cleaned = cleaned.replace(".", "");
        
        // Thay dấu phẩy thành dấu chấm (decimal separator)
        cleaned = cleaned.replace(",", ".");
        
        return Double.parseDouble(cleaned);
    }
    
    /**
     * Parse string thành số tiền, trả về 0 nếu parse lỗi
     */
    public static double parseSafe(String amountStr) {
        try {
            return parse(amountStr);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Làm tròn số tiền đến 2 chữ số thập phân
     */
    public static double round(double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }
    
    /**
     * Kiểm tra xem amount có phải số dương không
     */
    public static boolean isPositive(double amount) {
        return amount > 0;
    }
    
    /**
     * Kiểm tra xem amount có phải số âm không
     */
    public static boolean isNegative(double amount) {
        return amount < 0;
    }
    
    /**
     * Lấy giá trị tuyệt đối của amount
     */
    public static double abs(double amount) {
        return Math.abs(amount);
    }
    
    /**
     * So sánh 2 số tiền có bằng nhau không (với tolerance cho floating point)
     */
    public static boolean equals(double amount1, double amount2) {
        return Math.abs(amount1 - amount2) < 0.01;
    }
    
    /**
     * Format phần trăm
     * Ví dụ: 0.75 -> "75%"
     */
    public static String formatPercentage(double percentage) {
        return String.format(LOCALE, "%.1f%%", percentage);
    }
}

