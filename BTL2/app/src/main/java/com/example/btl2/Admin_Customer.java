package com.example.btl2;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Admin_Customer extends Fragment {

    private EditText edtCustomerId, edtName, edtPhone, edtEmail;
    private Button btnAddCustomer, btnUpdateCustomer, btnDeleteCustomer, btnSearchCustomer;
    private DatabaseHelper db;
    private ListView lvCus;

    public static final String COLUMN_CUSTOMER_ID = "CustomerID";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_PHONE_NUMBER = "PhoneNumber";
    public static final String COLUMN_EMAIL = "Email";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_admin_customer, container, false);

        edtCustomerId = view.findViewById(R.id.edtCustomerId);
        edtName = view.findViewById(R.id.edtName);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtEmail = view.findViewById(R.id.edtEmail);
        lvCus = view.findViewById(R.id.lvCus);
        btnAddCustomer = view.findViewById(R.id.btnAddCustomer);
        btnUpdateCustomer = view.findViewById(R.id.btnUpdateCustomer);
        btnDeleteCustomer = view.findViewById(R.id.btnDeleteCustomer);
        btnSearchCustomer = view.findViewById(R.id.btnSearchCustomer);

        db = new DatabaseHelper(getActivity());

        btnAddCustomer.setOnClickListener(v -> addCustomer());
        btnUpdateCustomer.setOnClickListener(v -> updateCustomer());
        btnDeleteCustomer.setOnClickListener(v -> deleteCustomer());
        btnSearchCustomer.setOnClickListener(v -> searchCustomer());
        displayCustomer();
        return view;
    }

    private void addCustomer() {
        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();
        String email = edtEmail.getText().toString();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(getActivity(), "Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {
            db.addCustomer(name, phone, email);
            Toast.makeText(getActivity(), "Thông tin Khách Hàng đã được thêm", Toast.LENGTH_SHORT).show();
            clearFields();
        }
    }

    private void updateCustomer() {
        String id = edtCustomerId.getText().toString();
        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();
        String email = edtEmail.getText().toString();

        if (id.isEmpty() || name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(getActivity(), "Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {
            db.updateCustomer(Integer.parseInt(id), name, phone, email);
            Toast.makeText(getActivity(), "Thông tin khách hàng đã được cập nhập", Toast.LENGTH_SHORT).show();
            clearFields();
        }
    }

    private void deleteCustomer() {
        String id = edtCustomerId.getText().toString();

        if (id.isEmpty()) {
            Toast.makeText(getActivity(), "Hãy nhập mã khách hàng", Toast.LENGTH_SHORT).show();
        } else {
            db.deleteCustomer(Integer.parseInt(id));
            Toast.makeText(getActivity(), "Đã xóa khách hàng", Toast.LENGTH_SHORT).show();
            clearFields();
        }
    }

    private void searchCustomer() {
        String phone = edtPhone.getText().toString();

        if (phone.isEmpty()) {
            Toast.makeText(getActivity(), "Hãy nhập số điện thoại", Toast.LENGTH_SHORT).show();
        } else {
            Cursor cursor = db.getCustomerByPhone(phone);

            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("CustomerID");
                int nameIndex = cursor.getColumnIndex("Name");
                int phoneIndex = cursor.getColumnIndex("PhoneNumber");
                int emailIndex = cursor.getColumnIndex("Email");

                if (idIndex != -1 && nameIndex != -1 && phoneIndex != -1 && emailIndex != -1) {
                    edtCustomerId.setText(cursor.getString(idIndex));
                    edtName.setText(cursor.getString(nameIndex));
                    edtPhone.setText(cursor.getString(phoneIndex));
                    edtEmail.setText(cursor.getString(emailIndex));
                    Toast.makeText(getActivity(), "Khách hàng đã được tìm thấy", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Dữ liệu không đầy đủ", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            } else {
                Toast.makeText(getActivity(), "Khách hàng không tìm thấy", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("Range")
    private void displayCustomer() {
        Cursor cursor = db.getAllCustomer();
        if (cursor != null) {
            // Tạo danh sách các dữ liệu để hiển thị
            List<Map<String, String>> data = new ArrayList<>();
            while (cursor.moveToNext()) {
                Map<String, String> item = new HashMap<>();
                item.put("CustomerID", cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMER_ID)));
                item.put("Name", cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                item.put("PhoneNumber", cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER)));
                item.put("Email", cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                data.add(item);
            }
            cursor.close();

            // Định nghĩa các cột trong ListView
            String[] from = {"CustomerID", "Name", "PhoneNumber", "Email"};
            int[] to = {R.id.tvCustomerID, R.id.tvName, R.id.tvPhoneNumber, R.id.tvEmail};

            // Tạo SimpleAdapter và gán cho ListView
            SimpleAdapter adapter = new SimpleAdapter(requireContext(), data, R.layout.list_item_customer, from, to);
            lvCus.setAdapter(adapter);
        } else {
            Toast.makeText(requireContext(), "Không có dữ liệu chuyến xe bus", Toast.LENGTH_SHORT).show();
        }
    }


    private void clearFields() {
        edtCustomerId.setText("");
        edtName.setText("");
        edtPhone.setText("");
        edtEmail.setText("");
    }
}
