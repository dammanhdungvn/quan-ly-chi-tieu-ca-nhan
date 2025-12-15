package com.dammanhdungvn.quanlychitieucanhan.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.dammanhdungvn.quanlychitieucanhan.config.AppConfig;
import com.dammanhdungvn.quanlychitieucanhan.data.model.Category;
import com.dammanhdungvn.quanlychitieucanhan.data.model.MonthlyStats;
import com.dammanhdungvn.quanlychitieucanhan.data.model.Transaction;
import com.dammanhdungvn.quanlychitieucanhan.utils.DateUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager class để quản lý Firestore operations
 * 
 * Sử dụng Singleton pattern
 */
public class FirestoreManager {
    
    private static final String TAG = "FirestoreManager";
    
    private static FirestoreManager instance;
    private final FirebaseFirestore db;
    
    /**
     * Private constructor (Singleton)
     */
    private FirestoreManager() {
        this.db = FirebaseFirestore.getInstance();
        
        // Enable offline persistence
        if (AppConfig.ENABLE_OFFLINE_MODE) {
            // Offline persistence is enabled by default in Android
            if (AppConfig.isLoggingEnabled()) {
                Log.d(TAG, "Offline persistence enabled");
            }
        }
    }
    
    /**
     * Lấy instance của FirestoreManager (Singleton)
     */
    public static synchronized FirestoreManager getInstance() {
        if (instance == null) {
            instance = new FirestoreManager();
        }
        return instance;
    }
    
    // ==================== CALLBACK INTERFACES ====================
    
    /**
     * Callback cho write operations
     */
    public interface WriteCallback {
        void onSuccess();
        void onFailure(Exception e);
    }
    
    /**
     * Callback cho read operations (single)
     */
    public interface ReadCallback<T> {
        void onSuccess(T data);
        void onFailure(Exception e);
    }
    
    /**
     * Callback cho read operations (list)
     */
    public interface ListReadCallback<T> {
        void onSuccess(List<T> data);
        void onFailure(Exception e);
    }
    
    /**
     * Listener cho realtime updates (list)
     */
    public interface RealtimeListListener<T> {
        void onDataChange(List<T> data);
        void onError(Exception e);
    }
    
    // ==================== TRANSACTION CRUD ====================
    
    /**
     * Thêm transaction mới
     */
    public void addTransaction(String userId, Transaction transaction, final WriteCallback callback) {
        if (AppConfig.isLoggingEnabled()) {
            Log.d(TAG, "addTransaction for user: " + userId);
        }
        
        // Tạo transaction ID nếu chưa có
        if (transaction.getTransactionId() == null) {
            String transactionId = getTransactionsCollection(userId).document().getId();
            transaction.setTransactionId(transactionId);
        }
        
        // Set created timestamp
        if (transaction.getCreatedAt() == null) {
            transaction.setCreatedAt(DateUtils.getCurrentTimestamp());
        }
        
        Map<String, Object> data = transactionToMap(transaction);
        
        getTransactionsCollection(userId)
                .document(transaction.getTransactionId())
                .set(data)
                .addOnSuccessListener(aVoid -> {
                    if (AppConfig.isLoggingEnabled()) {
                        Log.d(TAG, "Transaction added successfully");
                    }
                    callback.onSuccess();
                })
                .addOnFailureListener(callback::onFailure);
    }
    
    /**
     * Cập nhật transaction
     */
    public void updateTransaction(String userId, String transactionId, Transaction transaction, 
                                  final WriteCallback callback) {
        if (AppConfig.isLoggingEnabled()) {
            Log.d(TAG, "updateTransaction: " + transactionId);
        }
        
        Map<String, Object> data = transactionToMap(transaction);
        
        getTransactionsCollection(userId)
                .document(transactionId)
                .update(data)
                .addOnSuccessListener(aVoid -> {
                    if (AppConfig.isLoggingEnabled()) {
                        Log.d(TAG, "Transaction updated successfully");
                    }
                    callback.onSuccess();
                })
                .addOnFailureListener(callback::onFailure);
    }
    
    /**
     * Xóa transaction
     */
    public void deleteTransaction(String userId, String transactionId, final WriteCallback callback) {
        if (AppConfig.isLoggingEnabled()) {
            Log.d(TAG, "deleteTransaction: " + transactionId);
        }
        
        getTransactionsCollection(userId)
                .document(transactionId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    if (AppConfig.isLoggingEnabled()) {
                        Log.d(TAG, "Transaction deleted successfully");
                    }
                    callback.onSuccess();
                })
                .addOnFailureListener(callback::onFailure);
    }
    
    /**
     * Lấy tất cả transactions với realtime listener
     */
    public ListenerRegistration getTransactions(String userId, 
                                                final RealtimeListListener<Transaction> listener) {
        if (AppConfig.isLoggingEnabled()) {
            Log.d(TAG, "getTransactions with realtime listener");
        }
        
        return getTransactionsCollection(userId)
                .orderBy(AppConfig.FIELD_DATE, Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) {
                        listener.onError(error);
                        return;
                    }
                    
                    if (snapshots != null) {
                        List<Transaction> transactions = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : snapshots) {
                            Transaction transaction = documentToTransaction(doc);
                            if (transaction != null) {
                                transactions.add(transaction);
                            }
                        }
                        listener.onDataChange(transactions);
                    }
                });
    }
    
    /**
     * Lấy transactions theo tháng/năm với realtime listener
     */
    public ListenerRegistration getTransactionsByMonth(String userId, int month, int year,
                                                       final RealtimeListListener<Transaction> listener) {
        if (AppConfig.isLoggingEnabled()) {
            Log.d(TAG, "getTransactionsByMonth: " + month + "/" + year);
        }
        
        Date startDate = DateUtils.getStartOfMonth(month, year);
        Date endDate = DateUtils.getEndOfMonth(month, year);
        
        return getTransactionsCollection(userId)
                .whereGreaterThanOrEqualTo(AppConfig.FIELD_DATE, new Timestamp(startDate))
                .whereLessThanOrEqualTo(AppConfig.FIELD_DATE, new Timestamp(endDate))
                .orderBy(AppConfig.FIELD_DATE, Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) {
                        listener.onError(error);
                        return;
                    }
                    
                    if (snapshots != null) {
                        List<Transaction> transactions = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : snapshots) {
                            Transaction transaction = documentToTransaction(doc);
                            if (transaction != null) {
                                transactions.add(transaction);
                            }
                        }
                        listener.onDataChange(transactions);
                    }
                });
    }
    
    /**
     * Lấy recent transactions (limit số lượng)
     */
    public ListenerRegistration getRecentTransactions(String userId, int limit,
                                                      final RealtimeListListener<Transaction> listener) {
        if (AppConfig.isLoggingEnabled()) {
            Log.d(TAG, "getRecentTransactions: limit=" + limit);
        }
        
        return getTransactionsCollection(userId)
                .orderBy(AppConfig.FIELD_DATE, Query.Direction.DESCENDING)
                .limit(limit)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) {
                        listener.onError(error);
                        return;
                    }
                    
                    if (snapshots != null) {
                        List<Transaction> transactions = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : snapshots) {
                            Transaction transaction = documentToTransaction(doc);
                            if (transaction != null) {
                                transactions.add(transaction);
                            }
                        }
                        listener.onDataChange(transactions);
                    }
                });
    }
    
    // ==================== STATISTICS ====================
    
    /**
     * Tính monthly stats từ transactions
     */
    public void getMonthlyStats(String userId, int month, int year, 
                               final ReadCallback<MonthlyStats> callback) {
        if (AppConfig.isLoggingEnabled()) {
            Log.d(TAG, "getMonthlyStats: " + month + "/" + year);
        }
        
        Date startDate = DateUtils.getStartOfMonth(month, year);
        Date endDate = DateUtils.getEndOfMonth(month, year);
        
        getTransactionsCollection(userId)
                .whereGreaterThanOrEqualTo(AppConfig.FIELD_DATE, new Timestamp(startDate))
                .whereLessThanOrEqualTo(AppConfig.FIELD_DATE, new Timestamp(endDate))
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    MonthlyStats stats = new MonthlyStats(month, year);
                    
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Transaction transaction = documentToTransaction(doc);
                        if (transaction != null) {
                            stats.addTransaction(transaction);
                        }
                    }
                    
                    if (AppConfig.isLoggingEnabled()) {
                        Log.d(TAG, "Monthly stats calculated: " + stats.toString());
                    }
                    
                    callback.onSuccess(stats);
                })
                .addOnFailureListener(callback::onFailure);
    }
    
    // ==================== CUSTOM CATEGORIES ====================
    
    /**
     * Thêm custom category
     */
    public void addCustomCategory(String userId, Category category, final WriteCallback callback) {
        if (AppConfig.isLoggingEnabled()) {
            Log.d(TAG, "addCustomCategory for user: " + userId);
        }
        
        // Tạo category ID nếu chưa có
        if (category.getCategoryId() == null) {
            String categoryId = getCustomCategoriesCollection(userId).document().getId();
            category.setCategoryId(categoryId);
        }
        
        // Set created timestamp
        if (category.getCreatedAt() == null) {
            category.setCreatedAt(DateUtils.getCurrentTimestamp());
        }
        
        Map<String, Object> data = categoryToMap(category);
        
        getCustomCategoriesCollection(userId)
                .document(category.getCategoryId())
                .set(data)
                .addOnSuccessListener(aVoid -> {
                    if (AppConfig.isLoggingEnabled()) {
                        Log.d(TAG, "Custom category added successfully");
                    }
                    callback.onSuccess();
                })
                .addOnFailureListener(callback::onFailure);
    }
    
    /**
     * Lấy custom categories của user
     */
    public void getCustomCategories(String userId, final ListReadCallback<Category> callback) {
        if (AppConfig.isLoggingEnabled()) {
            Log.d(TAG, "getCustomCategories for user: " + userId);
        }
        
        getCustomCategoriesCollection(userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Category> categories = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Category category = documentToCategory(doc);
                        if (category != null) {
                            categories.add(category);
                        }
                    }
                    callback.onSuccess(categories);
                })
                .addOnFailureListener(callback::onFailure);
    }
    
    // ==================== HELPER METHODS ====================
    
    /**
     * Lấy reference đến transactions collection của user
     */
    private CollectionReference getTransactionsCollection(String userId) {
        return db.collection(AppConfig.USERS_COLLECTION)
                .document(userId)
                .collection(AppConfig.TRANSACTIONS_SUBCOLLECTION);
    }
    
    /**
     * Lấy reference đến custom categories collection của user
     */
    private CollectionReference getCustomCategoriesCollection(String userId) {
        return db.collection(AppConfig.USERS_COLLECTION)
                .document(userId)
                .collection(AppConfig.CUSTOM_CATEGORIES_SUBCOLLECTION);
    }
    
    /**
     * Convert Transaction object sang Map (để lưu vào Firestore)
     */
    private Map<String, Object> transactionToMap(Transaction transaction) {
        Map<String, Object> map = new HashMap<>();
        map.put(AppConfig.FIELD_TRANSACTION_ID, transaction.getTransactionId());
        map.put(AppConfig.FIELD_AMOUNT, transaction.getAmount());
        map.put(AppConfig.FIELD_TYPE, transaction.getType());
        map.put(AppConfig.FIELD_CATEGORY, transaction.getCategory());
        map.put(AppConfig.FIELD_NOTE, transaction.getNote());
        map.put(AppConfig.FIELD_DATE, transaction.getDate());
        map.put(AppConfig.FIELD_IMAGE_URL, transaction.getImageUrl());
        map.put(AppConfig.FIELD_CREATED_AT, transaction.getCreatedAt());
        return map;
    }
    
    /**
     * Convert Firestore document sang Transaction object
     */
    private Transaction documentToTransaction(DocumentSnapshot doc) {
        try {
            Transaction transaction = new Transaction();
            transaction.setTransactionId(doc.getString(AppConfig.FIELD_TRANSACTION_ID));
            
            // Handle amount as Number to support both Long and Double
            Number amount = (Number) doc.get(AppConfig.FIELD_AMOUNT);
            transaction.setAmount(amount != null ? amount.doubleValue() : 0.0);
            
            transaction.setType(doc.getString(AppConfig.FIELD_TYPE));
            transaction.setCategory(doc.getString(AppConfig.FIELD_CATEGORY));
            transaction.setNote(doc.getString(AppConfig.FIELD_NOTE));
            transaction.setDate(doc.getTimestamp(AppConfig.FIELD_DATE));
            transaction.setImageUrl(doc.getString(AppConfig.FIELD_IMAGE_URL));
            transaction.setCreatedAt(doc.getTimestamp(AppConfig.FIELD_CREATED_AT));
            return transaction;
        } catch (Exception e) {
            if (AppConfig.isLoggingEnabled()) {
                Log.e(TAG, "Error converting document to transaction", e);
            }
            return null;
        }
    }
    
    /**
     * Convert Category object sang Map (để lưu vào Firestore)
     */
    private Map<String, Object> categoryToMap(Category category) {
        Map<String, Object> map = new HashMap<>();
        map.put(AppConfig.FIELD_CATEGORY_ID, category.getCategoryId());
        map.put(AppConfig.FIELD_CATEGORY_NAME, category.getName());
        map.put(AppConfig.FIELD_CATEGORY_TYPE, category.getCategoryType());
        map.put(AppConfig.FIELD_ICON_NAME, category.getIconName());
        map.put("isPredefined", category.isPredefined());
        map.put(AppConfig.FIELD_CREATED_AT, category.getCreatedAt());
        return map;
    }
    
    /**
     * Convert Firestore document sang Category object
     */
    private Category documentToCategory(DocumentSnapshot doc) {
        try {
            Category category = new Category();
            category.setCategoryId(doc.getString(AppConfig.FIELD_CATEGORY_ID));
            category.setName(doc.getString(AppConfig.FIELD_CATEGORY_NAME));
            category.setCategoryType(doc.getString(AppConfig.FIELD_CATEGORY_TYPE));
            category.setIconName(doc.getString(AppConfig.FIELD_ICON_NAME));
            
            Boolean isPredefined = doc.getBoolean("isPredefined");
            category.setPredefined(isPredefined != null && isPredefined);
            
            category.setCreatedAt(doc.getTimestamp(AppConfig.FIELD_CREATED_AT));
            return category;
        } catch (Exception e) {
            if (AppConfig.isLoggingEnabled()) {
                Log.e(TAG, "Error converting document to category", e);
            }
            return null;
        }
    }
}

