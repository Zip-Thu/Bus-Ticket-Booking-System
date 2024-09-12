package com.example.btl2;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class User_InfoTicket extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private TextView tvStartDate, tvStartLocation, tvEndLocation, tvQuantity, tvTotal;
    private int customerId;
    private int busTripId; // Giả sử bạn đã có busTripId từ Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_ticket);

        dbHelper = new DatabaseHelper(this);

        // Khởi tạo các TextView
        tvStartDate = findViewById(R.id.tvStartDate);
        tvStartLocation = findViewById(R.id.tvStartLocation);
        tvEndLocation = findViewById(R.id.tvEndLocation);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvTotal = findViewById(R.id.tvTotal11);

        // Lấy customerId và busTripId từ Intent
        customerId = getIntent().getIntExtra("CustomerId", -1);
        busTripId = getIntent().getIntExtra("BusTripId", -1);

        // Tải dữ liệu vé và hiển thị
        loadTicketData();
    }

    private void loadTicketData() {
        Cursor cursor = dbHelper.getTicketDetails(customerId, busTripId);
        if (cursor != null && cursor.moveToFirst()) {
            // Đọc dữ liệu từ Cursor và kiểm tra index
            int dateIndex = cursor.getColumnIndex("Date");
            int startLocationIndex = cursor.getColumnIndex("StartLocation");
            int endLocationIndex = cursor.getColumnIndex("EndLocation");
            int quantityIndex = cursor.getColumnIndex("SeatCount");
            int totalIndex = cursor.getColumnIndex("Total");

            // Kiểm tra xem các index có hợp lệ không
            if (dateIndex == -1 || startLocationIndex == -1 || endLocationIndex == -1 || quantityIndex == -1 || totalIndex == -1) {
                Toast.makeText(this, "Cột dữ liệu không hợp lệ trong cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
                cursor.close();
                return;
            }

            // Đọc dữ liệu từ Cursor
            String date = cursor.getString(dateIndex);
            String startLocation = cursor.getString(startLocationIndex);
            String endLocation = cursor.getString(endLocationIndex);
            int quantity = cursor.getInt(quantityIndex);
            double total = cursor.getDouble(totalIndex);

            // Hiển thị dữ liệu lên giao diện
            tvStartDate.setText(date);
            tvStartLocation.setText(startLocation);
            tvEndLocation.setText(endLocation);
            tvQuantity.setText(String.valueOf(quantity));
            tvTotal.setText(String.valueOf(total));

            cursor.close();
        } else {
            Toast.makeText(this, "Không có dữ liệu vé", Toast.LENGTH_SHORT).show();
        }
    }
}
