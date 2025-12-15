package com.dammanhdungvn.quanlychitieucanhan.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dammanhdungvn.quanlychitieucanhan.R;
import com.dammanhdungvn.quanlychitieucanhan.firebase.AuthManager;
import com.dammanhdungvn.quanlychitieucanhan.ui.auth.LoginActivity;
import com.dammanhdungvn.quanlychitieucanhan.utils.DialogUtils;
import com.google.android.material.button.MaterialButton;

/**
 * ProfileFragment - Hiển thị thông tin user và logout
 * STUB IMPLEMENTATION - TODO: Implement full functionality
 */
public class ProfileFragment extends Fragment {
    
    private TextView displayNameText;
    private TextView emailText;
    private MaterialButton logoutButton;
    
    private AuthManager authManager;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        initManager();
        setupListeners();
        loadUserData();
    }
    
    /**
     * Initialize views
     */
    private void initViews(View view) {
        displayNameText = view.findViewById(R.id.displayNameText);
        emailText = view.findViewById(R.id.emailText);
        logoutButton = view.findViewById(R.id.logoutButton);
    }
    
    /**
     * Initialize manager
     */
    private void initManager() {
        authManager = AuthManager.getInstance();
    }
    
    /**
     * Setup listeners
     */
    private void setupListeners() {
        logoutButton.setOnClickListener(v -> {
            showLogoutConfirmation();
        });
    }
    
    /**
     * Load user data
     */
    private void loadUserData() {
        // TODO: Load full user data from Firestore
        // For now, get from FirebaseUser
        String displayName = authManager.getCurrentUserDisplayName();
        String email = authManager.getCurrentUserEmail();
        
        if (displayName != null) {
            displayNameText.setText(displayName);
        }
        
        if (email != null) {
            emailText.setText(email);
        }
    }
    
    /**
     * Show logout confirmation dialog
     */
    private void showLogoutConfirmation() {
        DialogUtils.showLogoutConfirmDialog(requireContext(), new DialogUtils.OnConfirmListener() {
            @Override
            public void onConfirm() {
                performLogout();
            }
        });
    }
    
    /**
     * Perform logout
     */
    private void performLogout() {
        // TODO: Add loading state
        authManager.signOut();
        
        Toast.makeText(requireContext(), R.string.logout_success, Toast.LENGTH_SHORT).show();
        
        // Navigate to LoginActivity
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}

