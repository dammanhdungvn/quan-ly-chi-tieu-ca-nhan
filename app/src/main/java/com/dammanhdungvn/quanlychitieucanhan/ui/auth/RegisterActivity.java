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
 * Register Activity - Màn hình đăng ký
 */
public class RegisterActivity extends AppCompatActivity {
    
    private TextInputLayout nameInputLayout;
    private TextInputEditText nameEditText;
    private TextInputLayout emailInputLayout;
    private TextInputEditText emailEditText;
    private TextInputLayout passwordInputLayout;
    private TextInputEditText passwordEditText;
    private TextInputLayout confirmPasswordInputLayout;
    private TextInputEditText confirmPasswordEditText;
    private MaterialButton registerButton;
    private View loginLink;
    
    private AuthManager authManager;
    private ProgressDialog loadingDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        initViews();
        initManagers();
        setupListeners();
    }
    
    /**
     * Khởi tạo views
     */
    private void initViews() {
        nameInputLayout = findViewById(R.id.nameInputLayout);
        nameEditText = findViewById(R.id.nameEditText);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        emailEditText = findViewById(R.id.emailEditText);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);
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
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegister();
            }
        });
        
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Quay lại LoginActivity
            }
        });
    }
    
    /**
     * Xử lý đăng ký
     */
    private void handleRegister() {
        // Clear previous errors
        nameInputLayout.setError(null);
        emailInputLayout.setError(null);
        passwordInputLayout.setError(null);
        confirmPasswordInputLayout.setError(null);
        
        // Get input values
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        
        // Validate
        if (!validateInputs(name, email, password, confirmPassword)) {
            return;
        }
        
        // Show loading
        showLoading();
        
        // Perform register
        authManager.signUpWithEmail(email, password, name, new AuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                hideLoading();
                Toast.makeText(RegisterActivity.this, 
                        R.string.register_success, Toast.LENGTH_SHORT).show();
                navigateToMain();
            }
            
            @Override
            public void onFailure(Exception e) {
                hideLoading();
                String errorMessage = getErrorMessage(e);
                DialogUtils.showErrorDialog(RegisterActivity.this, errorMessage);
            }
        });
    }
    
    /**
     * Validate inputs
     */
    private boolean validateInputs(String name, String email, String password, String confirmPassword) {
        boolean isValid = true;
        
        // Validate name
        String nameError = ValidationUtils.validateDisplayName(name);
        if (nameError != null) {
            nameInputLayout.setError(nameError);
            isValid = false;
        }
        
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
        
        // Validate confirm password
        String confirmPasswordError = ValidationUtils.validateConfirmPassword(password, confirmPassword);
        if (confirmPasswordError != null) {
            confirmPasswordInputLayout.setError(confirmPasswordError);
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
            return getString(R.string.error_register_failed);
        }
        
        String message = e.getMessage();
        
        // Parse Firebase auth error codes
        if (message.contains("email address is already in use") || message.contains("EMAIL_EXISTS")) {
            return "Email đã được sử dụng";
        } else if (message.contains("email address is badly formatted") || message.contains("INVALID_EMAIL")) {
            return "Email không hợp lệ";
        } else if (message.contains("password") && message.contains("weak")) {
            return "Mật khẩu quá yếu";
        } else if (message.contains("network")) {
            return getString(R.string.error_network);
        }
        
        return getString(R.string.error_register_failed);
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
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoading();
    }
}

