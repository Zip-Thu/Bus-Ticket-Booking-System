package com.example.btl2;

import static com.example.btl2.DatabaseHelper.COLUMN_EMAIL;
import static com.example.btl2.DatabaseHelper.COLUMN_EMAIL_FK;
import static com.example.btl2.DatabaseHelper.COLUMN_NAME;
import static com.example.btl2.DatabaseHelper.COLUMN_PASSWORD;
import static com.example.btl2.DatabaseHelper.COLUMN_PERMISSION_FK;
import static com.example.btl2.DatabaseHelper.COLUMN_PHONE_NUMBER;
import static com.example.btl2.DatabaseHelper.TABLE_ACCOUNT;
import static com.example.btl2.DatabaseHelper.TABLE_CUSTOMER;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class User_Signin extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPhone, edtPassword, edtReplacePassword;
    private Button btnRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signin);

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        edtReplacePassword = findViewById(R.id.edtReplacePassword);
        btnRegister = findViewById(R.id.btnRegister);

        dbHelper = new DatabaseHelper(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String replacePassword = edtReplacePassword.getText().toString().trim();

        // Kiểm tra các trường có rỗng không
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || replacePassword.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra xem mật khẩu và mật khẩu xác nhận có khớp không
        if (!password.equals(replacePassword)) {
            Toast.makeText(getApplicationContext(), "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra xem email đã được đăng ký chưa
        if (dbHelper.isEmailExists(email)) {
            Toast.makeText(getApplicationContext(), "Email đã được sử dụng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thêm dữ liệu vào bảng CUSTOMER
        ContentValues customerValues = new ContentValues();
        customerValues.put(COLUMN_NAME, name);
        customerValues.put(COLUMN_PHONE_NUMBER, phone);
        customerValues.put(COLUMN_EMAIL, email);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long customerId = db.insert(TABLE_CUSTOMER, null, customerValues);

        if (customerId == -1) {
            Toast.makeText(getApplicationContext(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thêm dữ liệu vào bảng ACCOUNT
        ContentValues accountValues = new ContentValues();
        accountValues.put(COLUMN_EMAIL_FK, email);
        accountValues.put(COLUMN_PASSWORD, password);
        accountValues.put(COLUMN_PERMISSION_FK, "user"); // Gán mặc định quyền 'user'

        long accountId = db.insert(TABLE_ACCOUNT, null, accountValues);
        if (accountId == -1) {
            Toast.makeText(getApplicationContext(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();

            // Lưu customerId vào SharedPreferences
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong("customerId", customerId);
            editor.apply();
        }

        db.close();
    }
}
