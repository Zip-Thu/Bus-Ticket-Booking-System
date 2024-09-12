package com.example.btl2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

public class User_Account extends Fragment {

    private TextView tvEmailAccount, tvNameAccount, tvPhoneAccount;
    private Button btnLogout, btnMyTicket;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_user_account, container, false);

        // Initialize views
        tvEmailAccount = view.findViewById(R.id.tvEmailAccount);
        tvNameAccount = view.findViewById(R.id.tvNameAccount);
        tvPhoneAccount = view.findViewById(R.id.tvPhoneAccount);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnMyTicket = view.findViewById(R.id.btnMyTicket);
        String userEmail = tvEmailAccount.getText().toString();

        databaseHelper = new DatabaseHelper(getActivity());

        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
        long customerId = prefs.getLong("customerId", -1);

        if (customerId != -1) {
            // Load customer data using the customerId
            loadCustomerData(customerId);
        } else {
            Toast.makeText(getContext(), "Lỗi lấy dữ liệu người dùng", Toast.LENGTH_SHORT).show();
        }

        // Set up logout button
        btnLogout.setOnClickListener(v -> showExitConfirmationDialog());

        // Set up my tickets button
        btnMyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = tvEmailAccount.getText().toString();
                if (!userEmail.isEmpty()) {
                    Intent intent = new Intent(getActivity(), Use_VeCuaToi.class);
                    intent.putExtra("userEmail", userEmail);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy email người dùng", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    private void loadCustomerData(long customerId) {
        // Fetch customer data from the database using customerId
        Customer customer = databaseHelper.getCustomerById(customerId);

        if (customer != null) {
            Log.d("User_Account", "Customer Data: " + customer.getName() + ", " + customer.getPhone() + ", " + customer.getEmail());
            // Display customer data in the TextViews
            tvEmailAccount.setText(customer.getEmail());
            tvNameAccount.setText(customer.getName());
            tvPhoneAccount.setText(customer.getPhone());
        } else {
            Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
        }
    }


    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Xác nhận thoát");
        builder.setMessage("Bạn có chắc chắn muốn thoát chương trình không?");
        builder.setPositiveButton("Có", (dialog, which) -> {
            Intent logoutIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(logoutIntent);
            requireActivity().finish(); // Close the current activity
        });
        builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
        builder.setCancelable(false);
        builder.show();
    }

    public static User_Account newInstance(String email) {
        User_Account fragment = new User_Account();
        Bundle args = new Bundle();
        args.putString("email", email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
