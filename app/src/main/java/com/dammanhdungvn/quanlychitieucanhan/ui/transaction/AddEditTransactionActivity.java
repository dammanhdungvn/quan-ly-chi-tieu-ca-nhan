package com.dammanhdungvn.quanlychitieucanhan.ui.transaction;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dammanhdungvn.quanlychitieucanhan.R;
import com.dammanhdungvn.quanlychitieucanhan.config.AppConfig;
import com.dammanhdungvn.quanlychitieucanhan.data.repository.CategoryRepository;
import com.dammanhdungvn.quanlychitieucanhan.data.repository.TransactionRepository;
import com.dammanhdungvn.quanlychitieucanhan.utils.DateUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * AddEditTransactionActivity - Thêm/sửa giao dịch
 * STUB IMPLEMENTATION - TODO: Implement full functionality
 */
public class AddEditTransactionActivity extends AppCompatActivity {
    
    private TextView titleText;
    private TextInputLayout amountInputLayout;
    private TextInputEditText amountEditText;
    private RadioGroup typeRadioGroup;
    private Spinner categorySpinner;
    private TextInputLayout dateInputLayout;
    private TextInputEditText dateEditText;
    private TextInputLayout noteInputLayout;
    private TextInputEditText noteEditText;
    private MaterialButton saveButton;
    
    private TransactionRepository transactionRepository;
    private CategoryRepository categoryRepository;
    
    private Date selectedDate;
    private String currentTransactionType = AppConfig.TRANSACTION_TYPE_EXPENSE;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_transaction);
        
        initViews();
        initRepositories();
        setupListeners();
        loadCategories();
        
        // Set default date to today
        selectedDate = DateUtils.getCurrentDate();
        dateEditText.setText(DateUtils.formatDate(selectedDate));
    }
    
    /**
     * Initialize views
     */
    private void initViews() {
        titleText = findViewById(R.id.titleText);
        amountInputLayout = findViewById(R.id.amountInputLayout);
        amountEditText = findViewById(R.id.amountEditText);
        typeRadioGroup = findViewById(R.id.typeRadioGroup);
        categorySpinner = findViewById(R.id.categorySpinner);
        dateInputLayout = findViewById(R.id.dateInputLayout);
        dateEditText = findViewById(R.id.dateEditText);
        noteInputLayout = findViewById(R.id.noteInputLayout);
        noteEditText = findViewById(R.id.noteEditText);
        saveButton = findViewById(R.id.saveButton);
    }
    
    /**
     * Initialize repositories
     */
    private void initRepositories() {
        transactionRepository = TransactionRepository.getInstance();
        categoryRepository = CategoryRepository.getInstance();
    }
    
    /**
     * Setup listeners
     */
    private void setupListeners() {
        // Type radio group listener
        typeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.incomeRadioButton) {
                currentTransactionType = AppConfig.TRANSACTION_TYPE_INCOME;
            } else {
                currentTransactionType = AppConfig.TRANSACTION_TYPE_EXPENSE;
            }
            loadCategories();
        });
        
        // Date picker listener
        dateEditText.setOnClickListener(v -> showDatePicker());
        
        // Save button listener
        saveButton.setOnClickListener(v -> saveTransaction());
    }
    
    /**
     * Save transaction to Firestore
     */
    private void saveTransaction() {
        // Clear previous errors
        amountInputLayout.setError(null);
        noteInputLayout.setError(null);
        
        // Get input values
        String amountStr = amountEditText.getText().toString().trim();
        String note = noteEditText.getText().toString().trim();
        String category = (String) categorySpinner.getSelectedItem();
        
        // Validate inputs
        if (!validateInputs(amountStr, category, note)) {
            return;
        }
        
        // Parse amount
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            amountInputLayout.setError("Số tiền không hợp lệ");
            return;
        }
        
        // Create Transaction object
        com.dammanhdungvn.quanlychitieucanhan.data.model.Transaction transaction = 
            new com.dammanhdungvn.quanlychitieucanhan.data.model.Transaction();
        transaction.setAmount(amount);
        transaction.setType(currentTransactionType);
        transaction.setCategory(category);
        transaction.setNote(note);
        transaction.setDate(com.dammanhdungvn.quanlychitieucanhan.utils.DateUtils.toTimestamp(selectedDate));
        transaction.setCreatedAt(com.dammanhdungvn.quanlychitieucanhan.utils.DateUtils.getCurrentTimestamp());
        
        // Show loading
        android.app.ProgressDialog loadingDialog = 
            com.dammanhdungvn.quanlychitieucanhan.utils.DialogUtils.showLoadingDialog(
                this, "Đang lưu giao dịch..."
            );
        
        // Save to Firestore
        transactionRepository.addTransaction(transaction, 
            new com.dammanhdungvn.quanlychitieucanhan.data.repository.TransactionRepository.OperationCallback() {
            @Override
            public void onSuccess() {
                com.dammanhdungvn.quanlychitieucanhan.utils.DialogUtils.dismissDialog(loadingDialog);
                android.widget.Toast.makeText(AddEditTransactionActivity.this, 
                    R.string.transaction_added, android.widget.Toast.LENGTH_SHORT).show();
                finish();
            }
            
            @Override
            public void onError(String errorMessage) {
                com.dammanhdungvn.quanlychitieucanhan.utils.DialogUtils.dismissDialog(loadingDialog);
                com.dammanhdungvn.quanlychitieucanhan.utils.DialogUtils.showErrorDialog(
                    AddEditTransactionActivity.this, errorMessage
                );
            }
        });
    }
    
    /**
     * Validate inputs
     */
    private boolean validateInputs(String amountStr, String category, String note) {
        boolean isValid = true;
        
        // Validate amount
        String amountError = com.dammanhdungvn.quanlychitieucanhan.utils.ValidationUtils.validateAmount(amountStr);
        if (amountError != null) {
            amountInputLayout.setError(amountError);
            isValid = false;
        }
        
        // Validate category
        if (category == null || category.isEmpty()) {
            android.widget.Toast.makeText(this, R.string.error_empty_category, 
                android.widget.Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        
        // Validate note (optional but check length if provided)
        String noteError = com.dammanhdungvn.quanlychitieucanhan.utils.ValidationUtils.validateNote(note);
        if (noteError != null) {
            noteInputLayout.setError(noteError);
            isValid = false;
        }
        
        return isValid;
    }
    
    /**
     * Load categories based on transaction type
     */
    private void loadCategories() {
        categoryRepository.getCategoryNames(currentTransactionType, 
            new CategoryRepository.CategoryNamesCallback() {
            @Override
            public void onSuccess(List<String> categoryNames) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        AddEditTransactionActivity.this,
                        android.R.layout.simple_spinner_item,
                        categoryNames
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(adapter);
            }
            
            @Override
            public void onError(String errorMessage) {
                // If loading fails, use predefined categories as fallback
                List<String> categories = new ArrayList<>();
                if (AppConfig.TRANSACTION_TYPE_INCOME.equals(currentTransactionType)) {
                    categories.add("Tiền lương");
                    categories.add("Kinh doanh");
                    categories.add("Đầu tư");
                    categories.add("Quà tặng");
                    categories.add("Thu nhập khác");
                } else {
                    categories.add("Ăn uống");
                    categories.add("Di chuyển");
                    categories.add("Học tập");
                    categories.add("Giải trí");
                    categories.add("Sức khỏe");
                    categories.add("Mua sắm");
                    categories.add("Hóa đơn");
                    categories.add("Chi tiêu khác");
                }
                
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        AddEditTransactionActivity.this,
                        android.R.layout.simple_spinner_item,
                        categories
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(adapter);
            }
        });
    }
    
    /**
     * Show date picker dialog
     */
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        if (selectedDate != null) {
            calendar.setTime(selectedDate);
        }
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(year, month, dayOfMonth);
                    selectedDate = selected.getTime();
                    dateEditText.setText(DateUtils.formatDate(selectedDate));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        
        datePickerDialog.show();
    }
}

