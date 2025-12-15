package com.dammanhdungvn.quanlychitieucanhan.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.dammanhdungvn.quanlychitieucanhan.config.AppConfig;
import com.dammanhdungvn.quanlychitieucanhan.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Manager class để quản lý Authentication với Firebase
 * 
 * Sử dụng Singleton pattern
 */
public class AuthManager {
    
    private static final String TAG = "AuthManager";
    
    private static AuthManager instance;
    private final FirebaseAuth auth;
    private final FirebaseFirestore db;
    
    /**
     * Private constructor (Singleton)
     */
    private AuthManager() {
        this.auth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }
    
    /**
     * Lấy instance của AuthManager (Singleton)
     */
    public static synchronized AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }
    
    // ==================== CALLBACK INTERFACES ====================
    
    /**
     * Callback cho authentication operations
     */
    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onFailure(Exception e);
    }
    
    /**
     * Callback cho user data operations
     */
    public interface UserDataCallback {
        void onSuccess(User user);
        void onFailure(Exception e);
    }
    
    // ==================== AUTHENTICATION METHODS ====================
    
    /**
     * Đăng ký user mới với email và password
     */
    public void signUpWithEmail(String email, String password, String displayName, 
                                final AuthCallback callback) {
        if (AppConfig.isLoggingEnabled()) {
            Log.d(TAG, "signUpWithEmail: " + email);
        }
        
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                // Update display name
                                updateDisplayName(firebaseUser, displayName, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> updateTask) {
                                        // Tạo user document trong Firestore
                                        createUserDocument(firebaseUser, displayName, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> createTask) {
                                                if (createTask.isSuccessful()) {
                                                    if (AppConfig.isLoggingEnabled()) {
                                                        Log.d(TAG, "User created successfully");
                                                    }
                                                    callback.onSuccess(firebaseUser);
                                                } else {
                                                    callback.onFailure(createTask.getException());
                                                }
                                            }
                                        });
                                    }
                                });
                            } else {
                                callback.onFailure(new Exception("User is null after sign up"));
                            }
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }
    
    /**
     * Đăng nhập với email và password
     */
    public void signInWithEmail(String email, String password, final AuthCallback callback) {
        if (AppConfig.isLoggingEnabled()) {
            Log.d(TAG, "signInWithEmail: " + email);
        }
        
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                if (AppConfig.isLoggingEnabled()) {
                                    Log.d(TAG, "Sign in successful");
                                }
                                callback.onSuccess(user);
                            } else {
                                callback.onFailure(new Exception("User is null after sign in"));
                            }
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }
    
    /**
     * Đăng xuất
     */
    public void signOut() {
        if (AppConfig.isLoggingEnabled()) {
            Log.d(TAG, "signOut");
        }
        auth.signOut();
    }
    
    /**
     * Lấy current user
     */
    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }
    
    /**
     * Kiểm tra user đã đăng nhập chưa
     */
    public boolean isUserLoggedIn() {
        return auth.getCurrentUser() != null;
    }
    
    /**
     * Lấy user ID hiện tại
     */
    public String getCurrentUserId() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getUid() : null;
    }
    
    // ==================== HELPER METHODS ====================
    
    /**
     * Update display name của user
     */
    private void updateDisplayName(FirebaseUser user, String displayName, 
                                   OnCompleteListener<Void> listener) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();
        
        user.updateProfile(profileUpdates).addOnCompleteListener(listener);
    }
    
    /**
     * Tạo user document trong Firestore sau khi đăng ký
     */
    private void createUserDocument(FirebaseUser firebaseUser, String displayName, 
                                    OnCompleteListener<Void> listener) {
        String userId = firebaseUser.getUid();
        String email = firebaseUser.getEmail();
        
        Map<String, Object> userData = new HashMap<>();
        userData.put(AppConfig.FIELD_USER_ID, userId);
        userData.put(AppConfig.FIELD_EMAIL, email);
        userData.put(AppConfig.FIELD_DISPLAY_NAME, displayName);
        userData.put(AppConfig.FIELD_CREATED_AT, Timestamp.now());
        
        db.collection(AppConfig.USERS_COLLECTION)
                .document(userId)
                .set(userData)
                .addOnCompleteListener(listener);
    }
    
    /**
     * Lấy user data từ Firestore
     */
    public void getUserData(String userId, final UserDataCallback callback) {
        db.collection(AppConfig.USERS_COLLECTION)
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = new User();
                        user.setUserId(documentSnapshot.getString(AppConfig.FIELD_USER_ID));
                        user.setEmail(documentSnapshot.getString(AppConfig.FIELD_EMAIL));
                        user.setDisplayName(documentSnapshot.getString(AppConfig.FIELD_DISPLAY_NAME));
                        user.setCreatedAt(documentSnapshot.getTimestamp(AppConfig.FIELD_CREATED_AT));
                        callback.onSuccess(user);
                    } else {
                        callback.onFailure(new Exception("User document not found"));
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }
    
    /**
     * Lấy email của current user
     */
    public String getCurrentUserEmail() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getEmail() : null;
    }
    
    /**
     * Lấy display name của current user
     */
    public String getCurrentUserDisplayName() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getDisplayName() : null;
    }
}

