package com.dammanhdungvn.quanlychitieucanhan.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.dammanhdungvn.quanlychitieucanhan.R;
import com.dammanhdungvn.quanlychitieucanhan.firebase.AuthManager;
import com.dammanhdungvn.quanlychitieucanhan.ui.home.MainActivity;

/**
 * Splash Activity - Màn hình khởi động
 * 
 * Kiểm tra trạng thái đăng nhập và điều hướng user
 */
public class SplashActivity extends AppCompatActivity {
    
    private static final long SPLASH_DELAY = 2000; // 2 seconds
    
    private AuthManager authManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        authManager = AuthManager.getInstance();
        
        // Delay để hiển thị splash screen
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                checkAuthenticationAndNavigate();
            }
        }, SPLASH_DELAY);
    }
    
    /**
     * Kiểm tra authentication state và điều hướng
     */
    private void checkAuthenticationAndNavigate() {
        if (authManager.isUserLoggedIn()) {
            // User đã đăng nhập -> vào MainActivity
            navigateToMain();
        } else {
            // User chưa đăng nhập -> vào LoginActivity
            navigateToLogin();
        }
    }
    
    /**
     * Điều hướng đến MainActivity
     */
    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    
    /**
     * Điều hướng đến LoginActivity
     */
    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

