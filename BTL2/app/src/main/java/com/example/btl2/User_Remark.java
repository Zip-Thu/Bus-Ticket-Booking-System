package com.example.btl2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class User_Remark extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText edtDescription;
    private Button btnSubmitRemark;
    private DatabaseHelper dbHelper;
    private int customerID;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_remark);
        // Khởi tạo các thành phần giao diện
        ratingBar = findViewById(R.id.ratingBar);
        edtDescription = findViewById(R.id.edtDescription);
        btnSubmitRemark = findViewById(R.id.btnSubmitRemark);

        dbHelper = new DatabaseHelper(this);

        /*// Nhận Intent từ User_VeCuaToi
        Intent intent = getIntent();

        // Lấy email từ Intent với khóa "userEmail"
        userEmail = intent.getStringExtra("userEmail1");

        if (userEmail!= null) {
            customerID = dbHelper.getCustomerIDByEmail(userEmail);
        } else {
            Toast.makeText(this, "Không tìm thấy email người dùng", Toast.LENGTH_SHORT).show();
        }

        if (customerID == -1) {
            Toast.makeText(this, "Không tìm thấy khách hàng với email này!", Toast.LENGTH_SHORT).show();
            finish(); // Đóng Activity nếu không tìm thấy customerID
        }*/

        btnSubmitRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float starNum = ratingBar.getRating();
                String description = edtDescription.getText().toString();

                if (starNum > 0 && !description.isEmpty()) {
                    dbHelper.addRemark((int) starNum, description, 0);
                    Toast.makeText(User_Remark.this, "Cảm ơn bạn đã đánh giá!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(User_Remark.this, "Vui lòng hoàn thành tất cả các trường!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}