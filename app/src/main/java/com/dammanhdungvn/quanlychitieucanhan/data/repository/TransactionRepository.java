package com.dammanhdungvn.quanlychitieucanhan.data.repository;

import com.dammanhdungvn.quanlychitieucanhan.config.AppConfig;
import com.dammanhdungvn.quanlychitieucanhan.data.model.MonthlyStats;
import com.dammanhdungvn.quanlychitieucanhan.data.model.Transaction;
import com.dammanhdungvn.quanlychitieucanhan.firebase.AuthManager;
import com.dammanhdungvn.quanlychitieucanhan.firebase.FirestoreManager;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

/**
 * Repository layer cho Transaction operations
 * 
 * Trung gian giữa UI và Firebase, cung cấp interface clean hơn
 */
public class TransactionRepository {
    
    private static TransactionRepository instance;
    private final FirestoreManager firestoreManager;
    private final AuthManager authManager;
    
    /**
     * Private constructor (Singleton)
     */
    private TransactionRepository() {
        this.firestoreManager = FirestoreManager.getInstance();
        this.authManager = AuthManager.getInstance();
    }
    
    /**
     * Lấy instance của TransactionRepository (Singleton)
     */
    public static synchronized TransactionRepository getInstance() {
        if (instance == null) {
            instance = new TransactionRepository();
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
     * Callback cho việc load monthly stats
     */
    public interface StatsCallback {
        void onSuccess(MonthlyStats stats);
        void onError(String errorMessage);
    }
    
    /**
     * Listener cho realtime updates của transactions
     */
    public interface TransactionsListener {
        void onTransactionsChanged(List<Transaction> transactions);
        void onError(String errorMessage);
    }
    
    // ==================== TRANSACTION CRUD ====================
    
    /**
     * Thêm transaction mới
     */
    public void addTransaction(Transaction transaction, final OperationCallback callback) {
        String userId = authManager.getCurrentUserId();
        if (userId == null) {
            callback.onError("Người dùng chưa đăng nhập");
            return;
        }
        
        firestoreManager.addTransaction(userId, transaction, new FirestoreManager.WriteCallback() {
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
    
    /**
     * Cập nhật transaction
     */
    public void updateTransaction(Transaction transaction, final OperationCallback callback) {
        String userId = authManager.getCurrentUserId();
        if (userId == null) {
            callback.onError("Người dùng chưa đăng nhập");
            return;
        }
        
        firestoreManager.updateTransaction(userId, transaction.getTransactionId(), 
                transaction, new FirestoreManager.WriteCallback() {
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
    
    /**
     * Xóa transaction
     */
    public void deleteTransaction(String transactionId, final OperationCallback callback) {
        String userId = authManager.getCurrentUserId();
        if (userId == null) {
            callback.onError("Người dùng chưa đăng nhập");
            return;
        }
        
        firestoreManager.deleteTransaction(userId, transactionId, new FirestoreManager.WriteCallback() {
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
    
    // ==================== TRANSACTION READ ====================
    
    /**
     * Lấy tất cả transactions với realtime updates
     * @return ListenerRegistration để detach listener sau này
     */
    public ListenerRegistration getAllTransactions(final TransactionsListener listener) {
        String userId = authManager.getCurrentUserId();
        if (userId == null) {
            listener.onError("Người dùng chưa đăng nhập");
            return null;
        }
        
        return firestoreManager.getTransactions(userId, new FirestoreManager.RealtimeListListener<Transaction>() {
            @Override
            public void onDataChange(List<Transaction> data) {
                listener.onTransactionsChanged(data);
            }
            
            @Override
            public void onError(Exception e) {
                listener.onError(getErrorMessage(e));
            }
        });
    }
    
    /**
     * Lấy transactions theo tháng với realtime updates
     * @return ListenerRegistration để detach listener sau này
     */
    public ListenerRegistration getTransactionsByMonth(int month, int year, 
                                                       final TransactionsListener listener) {
        String userId = authManager.getCurrentUserId();
        if (userId == null) {
            listener.onError("Người dùng chưa đăng nhập");
            return null;
        }
        
        return firestoreManager.getTransactionsByMonth(userId, month, year, 
                new FirestoreManager.RealtimeListListener<Transaction>() {
            @Override
            public void onDataChange(List<Transaction> data) {
                listener.onTransactionsChanged(data);
            }
            
            @Override
            public void onError(Exception e) {
                listener.onError(getErrorMessage(e));
            }
        });
    }
    
    /**
     * Lấy recent transactions với realtime updates
     * @return ListenerRegistration để detach listener sau này
     */
    public ListenerRegistration getRecentTransactions(final TransactionsListener listener) {
        String userId = authManager.getCurrentUserId();
        if (userId == null) {
            listener.onError("Người dùng chưa đăng nhập");
            return null;
        }
        
        return firestoreManager.getRecentTransactions(userId, 
                AppConfig.HOME_RECENT_TRANSACTIONS_LIMIT,
                new FirestoreManager.RealtimeListListener<Transaction>() {
            @Override
            public void onDataChange(List<Transaction> data) {
                listener.onTransactionsChanged(data);
            }
            
            @Override
            public void onError(Exception e) {
                listener.onError(getErrorMessage(e));
            }
        });
    }
    
    // ==================== STATISTICS ====================
    
    /**
     * Lấy monthly statistics
     */
    public void getMonthlyStats(int month, int year, final StatsCallback callback) {
        String userId = authManager.getCurrentUserId();
        if (userId == null) {
            callback.onError("Người dùng chưa đăng nhập");
            return;
        }
        
        firestoreManager.getMonthlyStats(userId, month, year, 
                new FirestoreManager.ReadCallback<MonthlyStats>() {
            @Override
            public void onSuccess(MonthlyStats data) {
                callback.onSuccess(data);
            }
            
            @Override
            public void onFailure(Exception e) {
                callback.onError(getErrorMessage(e));
            }
        });
    }
    
    /**
     * Lấy monthly statistics cho tháng hiện tại
     */
    public void getCurrentMonthStats(final StatsCallback callback) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int month = cal.get(java.util.Calendar.MONTH) + 1;
        int year = cal.get(java.util.Calendar.YEAR);
        getMonthlyStats(month, year, callback);
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

