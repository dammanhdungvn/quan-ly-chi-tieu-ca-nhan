package com.dammanhdungvn.quanlychitieucanhan.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dammanhdungvn.quanlychitieucanhan.R;
import com.dammanhdungvn.quanlychitieucanhan.ui.profile.ProfileFragment;
import com.dammanhdungvn.quanlychitieucanhan.ui.transaction.AddEditTransactionActivity;
import com.dammanhdungvn.quanlychitieucanhan.ui.transaction.TransactionListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * MainActivity - Container cho các fragments chính
 * STUB IMPLEMENTATION - TODO: Implement full functionality
 */
public class MainActivity extends AppCompatActivity {
    
    private BottomNavigationView bottomNavigation;
    private FloatingActionButton fabAddTransaction;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        setupListeners();
        
        // Load HomeFragment by default
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }
    
    /**
     * Initialize views
     */
    private void initViews() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
        fabAddTransaction = findViewById(R.id.fabAddTransaction);
    }
    
    /**
     * Setup listeners
     */
    private void setupListeners() {
        // Bottom navigation listener
        bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                
                if (itemId == R.id.navigation_home) {
                    loadFragment(new HomeFragment());
                    return true;
                } else if (itemId == R.id.navigation_transactions) {
                    loadFragment(new TransactionListFragment());
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    loadFragment(new ProfileFragment());
                    return true;
                }
                
                return false;
            }
        });
        
        // FAB listener
        fabAddTransaction.setOnClickListener(v -> {
            // TODO: Implement full functionality
            Intent intent = new Intent(MainActivity.this, AddEditTransactionActivity.class);
            startActivity(intent);
        });
    }
    
    /**
     * Load fragment into container
     */
    private void loadFragment(Fragment fragment) {
        // TODO: Implement full fragment transaction with proper animations
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}

