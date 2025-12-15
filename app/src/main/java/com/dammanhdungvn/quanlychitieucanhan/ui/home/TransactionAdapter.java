package com.dammanhdungvn.quanlychitieucanhan.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dammanhdungvn.quanlychitieucanhan.R;
import com.dammanhdungvn.quanlychitieucanhan.data.model.Transaction;
import com.dammanhdungvn.quanlychitieucanhan.utils.CurrencyUtils;
import com.dammanhdungvn.quanlychitieucanhan.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter cho RecyclerView hiển thị transactions
 * STUB IMPLEMENTATION - TODO: Implement full functionality
 */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    
    private List<Transaction> transactions;
    private OnTransactionClickListener listener;
    
    /**
     * Interface cho click events
     */
    public interface OnTransactionClickListener {
        void onTransactionClick(Transaction transaction);
        void onTransactionLongClick(Transaction transaction);
    }
    
    public TransactionAdapter() {
        this.transactions = new ArrayList<>();
    }
    
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions != null ? transactions : new ArrayList<>();
        notifyDataSetChanged();
    }
    
    public void setOnTransactionClickListener(OnTransactionClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        
        // TODO: Implement full binding logic with proper icons
        holder.categoryText.setText(transaction.getCategory());
        holder.noteText.setText(transaction.getNote());
        holder.dateText.setText(DateUtils.formatDate(transaction.getDate()));
        holder.amountText.setText(CurrencyUtils.formatWithSign(
                transaction.getAmount(), 
                transaction.isIncome()
        ));
        
        // Set color based on type
        int color = transaction.isIncome() ? 
                holder.itemView.getContext().getColor(R.color.income_color) :
                holder.itemView.getContext().getColor(R.color.expense_color);
        holder.amountText.setTextColor(color);
        
        // Click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTransactionClick(transaction);
            }
        });
        
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onTransactionLongClick(transaction);
            }
            return true;
        });
    }
    
    @Override
    public int getItemCount() {
        return transactions.size();
    }
    
    /**
     * ViewHolder class
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryIcon;
        TextView categoryText;
        TextView noteText;
        TextView dateText;
        TextView amountText;
        
        ViewHolder(View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
            categoryText = itemView.findViewById(R.id.categoryText);
            noteText = itemView.findViewById(R.id.noteText);
            dateText = itemView.findViewById(R.id.dateText);
            amountText = itemView.findViewById(R.id.amountText);
        }
    }
}

