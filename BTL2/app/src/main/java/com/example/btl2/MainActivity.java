package com.example.btl2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button btnSignin, btnLogin, btnAdminLogin;
    EditText edtEmailLogin, edtPasswordLogin;
    private DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        btnSignin = findViewById(R.id.btnSignin);

        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnAdminLogin = findViewById(R.id.btnAdminLogin);
        dbHelper = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> {
            String email = edtEmailLogin.getText().toString().trim();
            String password = edtPasswordLogin.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            } else {
                loginUser();
            }
        });
        btnAdminLogin.setOnClickListener(v -> {
            String email = edtEmailLogin.getText().toString().trim();
            String password = edtPasswordLogin.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            } else {
                loginAdmin();
            }
        });
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, User_Signin.class);
                startActivity(intent);
            }

        });
    }

    public void loginAdmin() {
        String email = edtEmailLogin.getText().toString().trim();
        String password = edtPasswordLogin.getText().toString().trim();

        Log.d("DEBUG", "Email: " + email + ", Password: " + password);

        if (dbHelper.checkUserCredentials(email, password)) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.query(DatabaseHelper.TABLE_ACCOUNT,
                    new String[]{DatabaseHelper.COLUMN_PERMISSION_FK},
                    DatabaseHelper.COLUMN_EMAIL + " = ?",
                    new String[]{email},
                    null, null, null);

            if (cursor.moveToFirst()) {
                int permissionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PERMISSION_FK);
                if (permissionIndex != -1) {
                    String permission = cursor.getString(permissionIndex);

                    if ("admin".equals(permission)) {
                        Intent intent = new Intent(MainActivity.this, Admin_Fragment.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(this, "không tồn tại", Toast.LENGTH_SHORT).show();                    }
                } else {
                    // Thông báo lỗi nếu cột không tồn tại
                    Toast.makeText(this, "Cột quyền không tồn tại trong kết quả truy vấn", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Người dùng không tồn tại", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
            db.close();
        } else {
            Toast.makeText(this, "Thông tin đăng nhập không chính xác", Toast.LENGTH_SHORT).show();
        }
    }

    public void loginUser() {
        String email = edtEmailLogin.getText().toString().trim();
        String password = edtPasswordLogin.getText().toString().trim();

        Log.d("DEBUG", "Email: " + email + ", Password: " + password);

        if (dbHelper.checkUserCredentials(email, password)) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.query(DatabaseHelper.TABLE_ACCOUNT,
                    new String[]{DatabaseHelper.COLUMN_PERMISSION_FK},
                    DatabaseHelper.COLUMN_EMAIL + " = ?",
                    new String[]{email},
                    null, null, null);

            if (cursor.moveToFirst()) {
                int permissionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PERMISSION_FK);
                if (permissionIndex != -1) {
                    String permission = cursor.getString(permissionIndex);

                    if ("user".equals(permission)) {
                        Intent intent = new Intent(MainActivity.this, User_Fragment.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(this, "không tồn tại", Toast.LENGTH_SHORT).show();                    }
                } else {
                    // Thông báo lỗi nếu cột không tồn tại
                    Toast.makeText(this, "Cột quyền không tồn tại trong kết quả truy vấn", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Người dùng không tồn tại", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
            db.close();
        } else {
            Toast.makeText(this, "Thông tin đăng nhập không chính xác", Toast.LENGTH_SHORT).show();
        }
    }
}