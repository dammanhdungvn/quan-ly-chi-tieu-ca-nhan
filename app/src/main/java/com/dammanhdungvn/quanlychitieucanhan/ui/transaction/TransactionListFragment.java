package com.dammanhdungvn.quanlychitieucanhan.ui.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dammanhdungvn.quanlychitieucanhan.R;
import com.dammanhdungvn.quanlychitieucanhan.data.model.Transaction;
import com.dammanhdungvn.quanlychitieucanhan.data.repository.TransactionRepository;
import com.dammanhdungvn.quanlychitieucanhan.ui.home.TransactionAdapter;
import com.google.firebase.firestore.ListenerRegistration;

/**
 * TransactionListFragment - Hiển thị tất cả giao dịch
 * STUB IMPLEMENTATION - TODO: Implement full functionality
 */
public class TransactionListFragment extends Fragment {
    
    private RecyclerView transactionsRecyclerView;
    private View emptyStateLayout;
    
    private TransactionAdapter adapter;
    private TransactionRepository transactionRepository;
    private ListenerRegistration transactionsListener;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction_list, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        initRepository();
        setupRecyclerView();
        loadData();
    }
    
    /**
     * Initialize views
     */
    private void initViews(View view) {
        transactionsRecyclerView = view.findViewById(R.id.transactionsRecyclerView);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
    }
    
    /**
     * Initialize repository
     */
    private void initRepository() {
        transactionRepository = TransactionRepository.getInstance();
    }
    
    /**
     * Setup RecyclerView
     */
    private void setupRecyclerView() {
        adapter = new TransactionAdapter();
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        transactionsRecyclerView.setAdapter(adapter);
        
        adapter.setOnTransactionClickListener(new TransactionAdapter.OnTransactionClickListener() {
            @Override
            public void onTransactionClick(Transaction transaction) {
                // TODO: Implement edit functionality in future
                android.widget.Toast.makeText(requireContext(), 
                    "Chỉnh sửa: " + transaction.getCategory(), 
                    android.widget.Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void onTransactionLongClick(Transaction transaction) {
                showDeleteConfirmDialog(transaction);
            }
        });
    }
    
    /**
     * Load data from repository
     */
    private void loadData() {
        transactionsListener = transactionRepository.getAllTransactions(
            new TransactionRepository.TransactionsListener() {
                @Override
                public void onTransactionsChanged(java.util.List<Transaction> transactions) {
                    if (transactions.isEmpty()) {
                        emptyStateLayout.setVisibility(View.VISIBLE);
                        transactionsRecyclerView.setVisibility(View.GONE);
                    } else {
                        emptyStateLayout.setVisibility(View.GONE);
                        transactionsRecyclerView.setVisibility(View.VISIBLE);
                        adapter.setTransactions(transactions);
                    }
                }
                
                @Override
                public void onError(String errorMessage) {
                    emptyStateLayout.setVisibility(View.VISIBLE);
                    transactionsRecyclerView.setVisibility(View.GONE);
                    android.widget.Toast.makeText(requireContext(), 
                        errorMessage, android.widget.Toast.LENGTH_SHORT).show();
                }
            }
        );
    }
    
    /**
     * Show delete confirmation dialog
     */
    private void showDeleteConfirmDialog(Transaction transaction) {
        com.dammanhdungvn.quanlychitieucanhan.utils.DialogUtils.showDeleteConfirmDialog(
            requireContext(),
            "Bạn có chắc chắn muốn xóa giao dịch này?",
            new com.dammanhdungvn.quanlychitieucanhan.utils.DialogUtils.OnConfirmListener() {
                @Override
                public void onConfirm() {
                    deleteTransaction(transaction);
                }
            }
        );
    }
    
    /**
     * Delete transaction
     */
    private void deleteTransaction(Transaction transaction) {
        transactionRepository.deleteTransaction(
            transaction.getTransactionId(),
            new TransactionRepository.OperationCallback() {
                @Override
                public void onSuccess() {
                    android.widget.Toast.makeText(requireContext(), 
                        R.string.transaction_deleted, android.widget.Toast.LENGTH_SHORT).show();
                }
                
                @Override
                public void onError(String errorMessage) {
                    com.dammanhdungvn.quanlychitieucanhan.utils.DialogUtils.showErrorDialog(
                        requireContext(), errorMessage
                    );
                }
            }
        );
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (transactionsListener != null) {
            transactionsListener.remove();
        }
    }
}

