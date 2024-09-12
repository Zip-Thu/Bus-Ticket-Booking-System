package com.example.btl2;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminTicketByDate extends AppCompatActivity {

    private EditText edtDate;
    private Button btnSearchDate;
    private ListView lvTicketsDate;
    private TextView tvResultTitleDate;

    private DatabaseHelper dbHelper;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_ticket_by_date);
        edtDate = findViewById(R.id.edtDate);
        btnSearchDate = findViewById(R.id.btnSearchDate);
        lvTicketsDate = findViewById(R.id.lvTicketsDate);
        tvResultTitleDate = findViewById(R.id.tvResultTitleDate);

        dbHelper = new DatabaseHelper(this);
        calendar = Calendar.getInstance();

        // Hiển thị DatePickerDialog khi người dùng nhấn vào EditText
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btnSearchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = edtDate.getText().toString();
                if (!date.isEmpty()) {
                    searchTicketsByDate(date);
                } else {
                    Toast.makeText(AdminTicketByDate.this, "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Hàm hiển thị DatePickerDialog
    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AdminTicketByDate.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Định dạng ngày và hiển thị lên EditText
                        String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        edtDate.setText(selectedDate);
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    @SuppressLint("Range")
    private void searchTicketsByDate(String date) {
        Cursor cursor = dbHelper.getTicketsByDate(date);
        if (cursor != null && cursor.getCount() > 0) {
            List<Map<String, String>> data = new ArrayList<>();
            while (cursor.moveToNext()) {
                Map<String, String> item = new HashMap<>();
                item.put("TicketID", cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TICKET_ID)));
                item.put("SeatNum", cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_SEAT_NUM)));
                item.put("Price", cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE)));
                item.put("Total", cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TOTAL)));
                item.put("CustomerID", cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CUSTOMER_ID_FK)));
                item.put("BusTripID", cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_BUSTRIP_ID_FK)));
                data.add(item);
            }
            cursor.close();

            // Định nghĩa các cột trong ListView
            String[] from = {"TicketID", "SeatNum", "Price", "Total", "CustomerID", "BusTripID"};
            int[] to = {R.id.tvTicketID, R.id.tvSeatNum, R.id.tvPrice, R.id.tvTotal, R.id.tvCustomerID, R.id.tvBusTripID};

            // Tạo SimpleAdapter và gán cho ListView
            SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.ticket_item_by_date, from, to);
            lvTicketsDate.setAdapter(adapter);
            tvResultTitleDate.setVisibility(View.VISIBLE);
        } else {
            tvResultTitleDate.setVisibility(View.GONE);
            Toast.makeText(this, "Không tìm thấy vé cho ngày này", Toast.LENGTH_SHORT).show();
        }
    }
}