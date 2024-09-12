package com.example.btl2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminTicketMain extends AppCompatActivity {
    private Button btnMaVe, btnMaKH, btnNgay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_ticket_main);

        // Khởi tạo các button
        btnMaVe = findViewById(R.id.btnMaVe);
        btnMaKH = findViewById(R.id.btnMaKH);
        btnNgay = findViewById(R.id.btnNgay);

        // Xử lý sự kiện click cho btnMaVe
        btnMaVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminTicketMain.this, AdminTicket.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện click cho btnMaKH
        btnMaKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminTicketMain.this, AdminTicketByCustomer.class);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện click cho btnNgay
        btnNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminTicketMain.this, AdminTicketByDate.class);
                startActivity(intent);
            }
        });
    }
}