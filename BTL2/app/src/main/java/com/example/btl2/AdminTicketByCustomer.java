package com.example.btl2;

import android.annotation.SuppressLint;
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

public class AdminTicketByCustomer extends AppCompatActivity {
    private EditText edtCustomerID;
    private Button btnSearch;
    private ListView lvTickets;
    private TextView tvResultTitle;

    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_ticket_by_customer);
        edtCustomerID = findViewById(R.id.edtCustomerID);
        btnSearch = findViewById(R.id.btnSearch);
        lvTickets = findViewById(R.id.lvTickets);
        tvResultTitle = findViewById(R.id.tvResultTitle);

        dbHelper = new DatabaseHelper(this);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customerID = edtCustomerID.getText().toString();
                if (!customerID.isEmpty()) {
                    searchTicketsByCustomerID(Integer.parseInt(customerID));
                } else {
                    Toast.makeText(AdminTicketByCustomer.this, "Vui lòng nhập mã khách hàng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("Range")
    private void searchTicketsByCustomerID(int customerID) {
        Cursor cursor = dbHelper.getTicketsByCustomerID(customerID);
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
            int[] to = {R.id.tvTicketID1, R.id.tvSeatNum1, R.id.tvPrice1, R.id.tvTotal11, R.id.tvBusTripID1};

            SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.ticket_item_by_customer, from, to);
            lvTickets.setAdapter(adapter);
            tvResultTitle.setVisibility(View.VISIBLE);
        } else {
            tvResultTitle.setVisibility(View.GONE);
            Toast.makeText(this, "Không tìm thấy vé cho mã khách hàng này", Toast.LENGTH_SHORT).show();
        }
    }
}