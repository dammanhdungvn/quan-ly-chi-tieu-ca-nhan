package com.dammanhdungvn.quanlychitieucanhan.ui.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dammanhdungvn.quanlychitieucanhan.R;
import com.dammanhdungvn.quanlychitieucanhan.firebase.AuthManager;
import com.dammanhdungvn.quanlychitieucanhan.ui.home.MainActivity;
import com.dammanhdungvn.quanlychitieucanhan.utils.DialogUtils;
import com.dammanhdungvn.quanlychitieucanhan.utils.ValidationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

/**
 * Login Activity - Màn hình đăng nhập
 */
public class LoginActivity extends AppCompatActivity {
    
    private TextInputLayout emailInputLayout;
    private TextInputEditText emailEditText;
    private TextInputLayout passwordInputLayout;
    private TextInputEditText passwordEditText;
    private MaterialButton loginButton;
    private View registerLink;
    
    private AuthManager authManager;
    private ProgressDialog loadingDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        initViews();
        initManagers();
        setupListeners();
    }
    
    /**
     * Khởi tạo views
     */
    private void initViews() {
        emailInputLayout = findViewById(R.id.emailInputLayout);
        emailEditText = findViewById(R.id.emailEditText);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);
    }
    
    /**
     * Khởi tạo managers
     */
    private void initManagers() {
        authManager = AuthManager.getInstance();
    }
    
    /**
     * Setup listeners cho các views
     */
    private void setupListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
        
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegister();
            }
        });
    }
    
    /**
     * Xử lý đăng nhập
     */
    private void handleLogin() {
        // Clear previous errors
        emailInputLayout.setError(null);
        passwordInputLayout.setError(null);
        
        // Get input values
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        
        // Validate
        if (!validateInputs(email, password)) {
            return;
        }
        
        // Show loading
        showLoading();
        
        // Perform login
        authManager.signInWithEmail(email, password, new AuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                hideLoading();
                Toast.makeText(LoginActivity.this, 
                        R.string.login_success, Toast.LENGTH_SHORT).show();
                navigateToMain();
            }
            
            @Override
            public void onFailure(Exception e) {
                hideLoading();
                String errorMessage = getErrorMessage(e);
                DialogUtils.showErrorDialog(LoginActivity.this, errorMessage);
            }
        });
    }
    
    /**
     * Validate inputs
     */
    private boolean validateInputs(String email, String password) {
        boolean isValid = true;
        
        // Validate email
        String emailError = ValidationUtils.validateEmail(email);
        if (emailError != null) {
            emailInputLayout.setError(emailError);
            isValid = false;
        }
        
        // Validate password
        String passwordError = ValidationUtils.validatePassword(password);
        if (passwordError != null) {
            passwordInputLayout.setError(passwordError);
            isValid = false;
        }
        
        return isValid;
    }
    
    /**
     * Hiển thị loading dialog
     */
    private void showLoading() {
        loadingDialog = DialogUtils.showLoadingDialog(this, getString(R.string.please_wait));
    }
    
    /**
     * Ẩn loading dialog
     */
    private void hideLoading() {
        DialogUtils.dismissDialog(loadingDialog);
    }
    
    /**
     * Parse error message từ Exception
     */
    private String getErrorMessage(Exception e) {
        if (e == null || e.getMessage() == null) {
            return getString(R.string.error_login_failed);
        }
        
        String message = e.getMessage();
        
        // Parse Firebase auth error codes
        if (message.contains("password is invalid") || message.contains("INVALID_PASSWORD")) {
            return "Mật khẩu không đúng";
        } else if (message.contains("no user record") || message.contains("USER_NOT_FOUND")) {
            return "Email không tồn tại";
        } else if (message.contains("network")) {
            return getString(R.string.error_network);
        } else if (message.contains("too many requests")) {
            return "Quá nhiều lần thử. Vui lòng thử lại sau";
        }
        
        return getString(R.string.error_login_failed);
    }
    
    /**
     * Điều hướng đến MainActivity
     */
    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        // Clear back stack
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    /**
     * Điều hướng đến RegisterActivity
     */
    private void navigateToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoading();
    }
}

