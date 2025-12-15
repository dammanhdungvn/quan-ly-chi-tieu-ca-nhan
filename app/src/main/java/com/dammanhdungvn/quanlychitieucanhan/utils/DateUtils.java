package com.dammanhdungvn.quanlychitieucanhan.utils;

import com.dammanhdungvn.quanlychitieucanhan.config.AppConfig;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class để xử lý các thao tác liên quan đến Date/Time
 */
public class DateUtils {
    
    private static final Locale LOCALE = new Locale(AppConfig.CURRENCY_LOCALE);
    
    /**
     * Private constructor để prevent instantiation
     */
    private DateUtils() {
        throw new AssertionError("Cannot instantiate DateUtils");
    }
    
    // ==================== FORMAT METHODS ====================
    
    /**
     * Format Date thành string theo định dạng dd/MM/yyyy
     */
    public static String formatDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(AppConfig.DATE_FORMAT, LOCALE);
        return sdf.format(date);
    }
    
    /**
     * Format Timestamp thành string theo định dạng dd/MM/yyyy
     */
    public static String formatDate(Timestamp timestamp) {
        if (timestamp == null) return "";
        return formatDate(timestamp.toDate());
    }
    
    /**
     * Format Date thành string theo định dạng dd/MM/yyyy HH:mm
     */
    public static String formatDateTime(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(AppConfig.DATETIME_FORMAT, LOCALE);
        return sdf.format(date);
    }
    
    /**
     * Format Timestamp thành string theo định dạng dd/MM/yyyy HH:mm
     */
    public static String formatDateTime(Timestamp timestamp) {
        if (timestamp == null) return "";
        return formatDateTime(timestamp.toDate());
    }
    
    // ==================== CURRENT DATE/TIME ====================
    
    /**
     * Lấy Timestamp hiện tại
     */
    public static Timestamp getCurrentTimestamp() {
        return Timestamp.now();
    }
    
    /**
     * Lấy Date hiện tại
     */
    public static Date getCurrentDate() {
        return new Date();
    }
    
    /**
     * Lấy tháng hiện tại (1-12)
     */
    public static int getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1; // Calendar.MONTH is 0-based
    }
    
    /**
     * Lấy năm hiện tại
     */
    public static int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }
    
    // ==================== DATE MANIPULATION ====================
    
    /**
     * Lấy tháng từ Date (1-12)
     */
    public static int getMonth(Date date) {
        if (date == null) return 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }
    
    /**
     * Lấy tháng từ Timestamp (1-12)
     */
    public static int getMonth(Timestamp timestamp) {
        if (timestamp == null) return 0;
        return getMonth(timestamp.toDate());
    }
    
    /**
     * Lấy năm từ Date
     */
    public static int getYear(Date date) {
        if (date == null) return 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
    
    /**
     * Lấy năm từ Timestamp
     */
    public static int getYear(Timestamp timestamp) {
        if (timestamp == null) return 0;
        return getYear(timestamp.toDate());
    }
    
    /**
     * Lấy ngày đầu tiên của tháng
     */
    public static Date getStartOfMonth(int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1); // Calendar.MONTH is 0-based
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    /**
     * Lấy ngày cuối cùng của tháng
     */
    public static Date getEndOfMonth(int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1); // Calendar.MONTH is 0-based
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
    
    /**
     * Lấy Timestamp đầu tháng
     */
    public static Timestamp getStartOfMonthTimestamp(int month, int year) {
        return new Timestamp(getStartOfMonth(month, year));
    }
    
    /**
     * Lấy Timestamp cuối tháng
     */
    public static Timestamp getEndOfMonthTimestamp(int month, int year) {
        return new Timestamp(getEndOfMonth(month, year));
    }
    
    // ==================== DATE COMPARISON ====================
    
    /**
     * Kiểm tra xem date có trong tháng/năm chỉ định không
     */
    public static boolean isInMonth(Date date, int month, int year) {
        if (date == null) return false;
        return getMonth(date) == month && getYear(date) == year;
    }
    
    /**
     * Kiểm tra xem timestamp có trong tháng/năm chỉ định không
     */
    public static boolean isInMonth(Timestamp timestamp, int month, int year) {
        if (timestamp == null) return false;
        return isInMonth(timestamp.toDate(), month, year);
    }
    
    /**
     * Kiểm tra xem date có phải hôm nay không
     */
    public static boolean isToday(Date date) {
        if (date == null) return false;
        Calendar calDate = Calendar.getInstance();
        calDate.setTime(date);
        Calendar calToday = Calendar.getInstance();
        return calDate.get(Calendar.YEAR) == calToday.get(Calendar.YEAR) &&
               calDate.get(Calendar.DAY_OF_YEAR) == calToday.get(Calendar.DAY_OF_YEAR);
    }
    
    /**
     * Kiểm tra xem timestamp có phải hôm nay không
     */
    public static boolean isToday(Timestamp timestamp) {
        if (timestamp == null) return false;
        return isToday(timestamp.toDate());
    }
    
    /**
     * Lấy tên tháng bằng tiếng Việt
     */
    public static String getMonthName(int month) {
        String[] monthNames = {
            "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4",
            "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8",
            "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
        };
        if (month < 1 || month > 12) return "";
        return monthNames[month - 1];
    }
    
    /**
     * Convert Date sang Timestamp
     */
    public static Timestamp toTimestamp(Date date) {
        if (date == null) return null;
        return new Timestamp(date);
    }
    
    /**
     * Convert millis sang Timestamp
     */
    public static Timestamp toTimestamp(long millis) {
        return new Timestamp(new Date(millis));
    }
}

