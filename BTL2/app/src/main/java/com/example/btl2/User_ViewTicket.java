package com.example.btl2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class User_ViewTicket extends AppCompatActivity {
    private ListView lvBusList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_bus_view);

        lvBusList = findViewById(R.id.lvBusList); // Đảm bảo bạn đã khai báo ListView trong layout
        dbHelper = new DatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int customerId = (int) prefs.getLong("customerId", -1);

        Intent intent = getIntent();
        ArrayList<BusTrip> matchingTrips = intent.getParcelableArrayListExtra("MatchingTrips");
        int seatCount = intent.getIntExtra("seatNum", 1); // Nhận seatCount từ User_Ticket

        if (matchingTrips != null && !matchingTrips.isEmpty()) {
            displayBusTrips(matchingTrips, customerId, seatCount);
        } else {
            Toast.makeText(this, "Không tìm thấy chuyến xe phù hợp", Toast.LENGTH_SHORT).show();
        }
    }


    private void displayBusTrips(List<BusTrip> trips, int customerId, int seatCount) {
        // Tạo BusTripAdapter với seatCount
        BusTripAdapter adapter = new BusTripAdapter(this, trips, customerId, seatCount);
        lvBusList.setAdapter(adapter);
    }
}
