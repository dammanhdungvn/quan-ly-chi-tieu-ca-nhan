package com.dammanhdungvn.quanlychitieucanhan.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.dammanhdungvn.quanlychitieucanhan.R;

/**
 * Utility class để hiển thị các dialogs
 */
public class DialogUtils {
    
    /**
     * Private constructor để prevent instantiation
     */
    private DialogUtils() {
        throw new AssertionError("Cannot instantiate DialogUtils");
    }
    
    // ==================== LOADING DIALOG ====================
    
    /**
     * Tạo và hiển thị loading dialog
     */
    public static ProgressDialog showLoadingDialog(Context context, String message) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }
    
    /**
     * Tạo và hiển thị loading dialog với message mặc định
     */
    public static ProgressDialog showLoadingDialog(Context context) {
        return showLoadingDialog(context, context.getString(R.string.please_wait));
    }
    
    /**
     * Dismiss loading dialog một cách an toàn
     */
    public static void dismissDialog(ProgressDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                // Ignore exception khi activity đã destroyed
            }
        }
    }
    
    // ==================== ALERT DIALOG ====================
    
    /**
     * Hiển thị alert dialog với message
     */
    public static void showAlertDialog(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.confirm, null)
                .show();
    }
    
    /**
     * Hiển thị alert dialog chỉ với message
     */
    public static void showAlertDialog(Context context, String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.confirm, null)
                .show();
    }
    
    /**
     * Hiển thị error dialog
     */
    public static void showErrorDialog(Context context, String message) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.error_unknown)
                .setMessage(message)
                .setPositiveButton(R.string.confirm, null)
                .show();
    }
    
    /**
     * Hiển thị success dialog
     */
    public static void showSuccessDialog(Context context, String message) {
        new AlertDialog.Builder(context)
                .setTitle("Thành công")
                .setMessage(message)
                .setPositiveButton(R.string.confirm, null)
                .show();
    }
    
    // ==================== CONFIRMATION DIALOG ====================
    
    /**
     * Interface cho confirmation callback
     */
    public interface OnConfirmListener {
        void onConfirm();
    }
    
    /**
     * Hiển thị confirmation dialog
     */
    public static void showConfirmDialog(Context context, String title, String message, 
                                        final OnConfirmListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onConfirm();
                        }
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
    
    /**
     * Hiển thị confirmation dialog chỉ với message
     */
    public static void showConfirmDialog(Context context, String message, 
                                        final OnConfirmListener listener) {
        showConfirmDialog(context, context.getString(R.string.confirm), message, listener);
    }
    
    /**
     * Hiển thị delete confirmation dialog
     */
    public static void showDeleteConfirmDialog(Context context, String message, 
                                              final OnConfirmListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.delete_confirm_title)
                .setMessage(message)
                .setPositiveButton(R.string.delete_transaction, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onConfirm();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
    
    /**
     * Hiển thị logout confirmation dialog
     */
    public static void showLogoutConfirmDialog(Context context, final OnConfirmListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.logout_confirm_title)
                .setMessage(R.string.logout_confirm_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onConfirm();
                        }
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
    
    // ==================== INPUT DIALOG ====================
    
    /**
     * Interface cho input callback
     */
    public interface OnInputListener {
        void onInput(String input);
    }
    
    /**
     * Hiển thị input dialog (sẽ implement khi cần)
     * Hiện tại sử dụng DialogFragment cho input phức tạp
     */
    
    // ==================== CHOICE DIALOG ====================
    
    /**
     * Interface cho choice callback
     */
    public interface OnChoiceListener {
        void onChoice(int which);
    }
    
    /**
     * Hiển thị single choice dialog
     */
    public static void showSingleChoiceDialog(Context context, String title, 
                                             String[] items, int checkedItem,
                                             final OnChoiceListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onChoice(which);
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
    
    /**
     * Hiển thị list dialog
     */
    public static void showListDialog(Context context, String title, 
                                     String[] items, 
                                     final OnChoiceListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onChoice(which);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}

