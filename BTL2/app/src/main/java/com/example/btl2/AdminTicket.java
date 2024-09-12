package com.example.btl2;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminTicket extends AppCompatActivity {

    private EditText edtTicketID;
    private Button btnSearch;
    private ListView lvTickets;
    private TextView tvResultTitle;

    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_ticket);
        // Ánh xạ các thành phần trong giao diện
        edtTicketID = findViewById(R.id.edtTicketID);
        btnSearch = findViewById(R.id.btnSearch);
        lvTickets = findViewById(R.id.lvTickets);
        tvResultTitle = findViewById(R.id.tvResultTitle);

        dbHelper = new DatabaseHelper(this);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ticketIDStr = edtTicketID.getText().toString();
                if (!ticketIDStr.isEmpty()) {
                    searchTicketByID(Integer.parseInt(ticketIDStr));
                } else {
                    Toast.makeText(AdminTicket.this, "Vui lòng nhập mã vé", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("Range")
    private void searchTicketByID(int ticketID) {
        Cursor cursor = dbHelper.getTicketByID(ticketID);
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
            int[] to = {R.id.tvTicketID, R.id.tvSeatNum, R.id.tvPrice, R.id.tvTotal, R.id.tvBusTripID};

            SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.ticket_item_by_id, from, to);
            lvTickets.setAdapter(adapter);
            tvResultTitle.setVisibility(View.VISIBLE);
        } else {
            tvResultTitle.setVisibility(View.GONE);
            Toast.makeText(this, "Không tìm thấy vé với mã vé này", Toast.LENGTH_SHORT).show();
        }
    }
}