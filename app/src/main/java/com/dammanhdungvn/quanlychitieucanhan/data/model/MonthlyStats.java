package com.dammanhdungvn.quanlychitieucanhan.data.model;

/**
 * Model đại diện cho thống kê tháng
 * 
 * Không lưu vào Firestore - chỉ dùng để hiển thị UI
 * Data được tính toán từ transactions
 */
public class MonthlyStats {
    private double totalIncome;     // Tổng thu trong tháng
    private double totalExpense;    // Tổng chi trong tháng
    private double balance;         // Số dư (income - expense)
    private int month;              // Tháng (1-12)
    private int year;               // Năm
    private int incomeCount;        // Số lượng giao dịch thu
    private int expenseCount;       // Số lượng giao dịch chi
    
    /**
     * Constructor mặc định
     */
    public MonthlyStats() {
        this.totalIncome = 0;
        this.totalExpense = 0;
        this.balance = 0;
        this.incomeCount = 0;
        this.expenseCount = 0;
    }
    
    /**
     * Constructor với tháng và năm
     */
    public MonthlyStats(int month, int year) {
        this();
        this.month = month;
        this.year = year;
    }
    
    /**
     * Constructor đầy đủ
     */
    public MonthlyStats(double totalIncome, double totalExpense, int month, int year) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.balance = totalIncome - totalExpense;
        this.month = month;
        this.year = year;
        this.incomeCount = 0;
        this.expenseCount = 0;
    }
    
    // ==================== GETTERS & SETTERS ====================
    
    public double getTotalIncome() {
        return totalIncome;
    }
    
    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
        updateBalance();
    }
    
    public double getTotalExpense() {
        return totalExpense;
    }
    
    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
        updateBalance();
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public int getMonth() {
        return month;
    }
    
    public void setMonth(int month) {
        this.month = month;
    }
    
    public int getYear() {
        return year;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public int getIncomeCount() {
        return incomeCount;
    }
    
    public void setIncomeCount(int incomeCount) {
        this.incomeCount = incomeCount;
    }
    
    public int getExpenseCount() {
        return expenseCount;
    }
    
    public void setExpenseCount(int expenseCount) {
        this.expenseCount = expenseCount;
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Cập nhật số dư dựa trên total income và expense
     */
    private void updateBalance() {
        this.balance = this.totalIncome - this.totalExpense;
    }
    
    /**
     * Thêm một transaction vào stats
     */
    public void addTransaction(Transaction transaction) {
        if (transaction.isIncome()) {
            this.totalIncome += transaction.getAmount();
            this.incomeCount++;
        } else if (transaction.isExpense()) {
            this.totalExpense += transaction.getAmount();
            this.expenseCount++;
        }
        updateBalance();
    }
    
    /**
     * Tổng số giao dịch
     */
    public int getTotalTransactionCount() {
        return incomeCount + expenseCount;
    }
    
    /**
     * Kiểm tra xem có giao dịch nào không
     */
    public boolean hasTransactions() {
        return getTotalTransactionCount() > 0;
    }
    
    /**
     * Kiểm tra xem balance có dương không (thu > chi)
     */
    public boolean hasPositiveBalance() {
        return balance > 0;
    }
    
    /**
     * Kiểm tra xem balance có âm không (chi > thu)
     */
    public boolean hasNegativeBalance() {
        return balance < 0;
    }
    
    /**
     * Lấy phần trăm chi tiêu so với thu nhập
     */
    public double getExpensePercentage() {
        if (totalIncome == 0) {
            return 0;
        }
        return (totalExpense / totalIncome) * 100;
    }
    
    @Override
    public String toString() {
        return "MonthlyStats{" +
                "totalIncome=" + totalIncome +
                ", totalExpense=" + totalExpense +
                ", balance=" + balance +
                ", month=" + month +
                ", year=" + year +
                ", incomeCount=" + incomeCount +
                ", expenseCount=" + expenseCount +
                '}';
    }
}

