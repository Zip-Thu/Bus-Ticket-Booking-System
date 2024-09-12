package com.example.btl2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Use_VeCuaToi extends AppCompatActivity {
    private ListView lvUserTickets;
    private TextView tvResultTitleUserTickets;
    private DatabaseHelper dbHelper;
    private String userEmail;
    private EditText edtTicketID;
    private Button btnCancelTicket,btnRemark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_use_ve_cua_toi);

        lvUserTickets = findViewById(R.id.lvUserTickets);
        tvResultTitleUserTickets = findViewById(R.id.tvResultTitleUserTickets);
        edtTicketID = findViewById(R.id.edtTicketID);
        btnCancelTicket = findViewById(R.id.btnCancelTicket);
        btnRemark = findViewById(R.id.btnRemark);

        dbHelper = new DatabaseHelper(this);

        // Get the email from the intent
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("userEmail");



        if (userEmail != null) {
            // Tạo Intent để chuyển từ User_VeCuaToi sang User_Remark
            Intent intent1 = new Intent(Use_VeCuaToi.this, User_Remark.class);
            // Đặt email vào Intent với khóa là "userEmail"
            intent1.putExtra("userEmail1", userEmail);

            searchTicketsByUserEmail(userEmail);
        } else {
            Toast.makeText(this, "Không tìm thấy email người dùng", Toast.LENGTH_SHORT).show();
        }

        btnRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Use_VeCuaToi.this, User_Remark.class);
                startActivity(intent);
            }
        });

        btnCancelTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ticketIDStr = edtTicketID.getText().toString().trim();

                if (!ticketIDStr.isEmpty()) {
                    int ticketID = Integer.parseInt(ticketIDStr);
                    cancelTicket(ticketID);
                } else {
                    Toast.makeText(Use_VeCuaToi.this, "Vui lòng nhập ID vé", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @SuppressLint("Range")
    private void searchTicketsByUserEmail(String email) {
        Cursor cursor = dbHelper.getTicketsByUserEmail(email);
        if (cursor != null && cursor.moveToFirst()) {
            List<Map<String, String>> data = new ArrayList<>();
            do {
                Map<String, String> item = new HashMap<>();
                item.put("TicketID", cursor.getString(cursor.getColumnIndex("TicketID")));
                item.put("SeatNum", cursor.getString(cursor.getColumnIndex("SeatNum")));
                item.put("Price", cursor.getString(cursor.getColumnIndex("Price")));
                item.put("Total", cursor.getString(cursor.getColumnIndex("Total")));
                item.put("BusTripID", cursor.getString(cursor.getColumnIndex("BusTripID")));
                data.add(item);
            } while (cursor.moveToNext());
            cursor.close();

            String[] from = {"TicketID", "SeatNum", "Price", "Total", "BusTripID"};
            int[] to = {R.id.tvTicketID1, R.id.tvSeatNum1, R.id.tvPrice1, R.id.tvTotal1, R.id.tvBusTripID1};

            SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.ticket_item_by_customer, from, to);
            lvUserTickets.setAdapter(adapter);
            tvResultTitleUserTickets.setVisibility(View.VISIBLE);
        } else {
            tvResultTitleUserTickets.setVisibility(View.GONE);
            Toast.makeText(this, "Không tìm thấy vé cho email này", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelTicket(int ticketID) {
        boolean isCancelled = dbHelper.cancelTicketByID(ticketID);

        if (isCancelled) {
            Toast.makeText(this, "Vé đã được hủy thành công", Toast.LENGTH_SHORT).show();
            searchTicketsByUserEmail(userEmail);  // Refresh the ticket list
        } else {
            Toast.makeText(this, "Hủy vé thất bại hoặc vé không thể hủy trong thời gian hiện tại", Toast.LENGTH_SHORT).show();
        }
    }
}