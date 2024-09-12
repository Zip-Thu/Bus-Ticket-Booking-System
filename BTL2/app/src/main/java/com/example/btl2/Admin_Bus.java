package com.example.btl2;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Admin_Bus extends AppCompatActivity {

    private EditText edtBusID, edtLicensePlate, edtSeatCount, edtDriverName, edtStatus;
    private Button btnAddBus, btnUpdateBus, btnDeleteBus, btnFindBus;
    private DatabaseHelper db;
    private SimpleCursorAdapter adapter;
    private ListView lvBus;
    private static final String COLUMN_BUS_ID = "BusID";
    private static final String COLUMN_LICENSE_PLATE = "LicensePlate";
    private static final String COLUMN_SEAT_COUNT = "SeatCount";
    private static final String COLUMN_DRIVER_NAME = "DriverName";
    private static final String COLUMN_STATUS = "Status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bus);

        db = new DatabaseHelper(this);


        edtBusID = findViewById(R.id.edtBusID);
        edtLicensePlate = findViewById(R.id.edtLicensePlate);
        edtSeatCount = findViewById(R.id.edtSeatCount);
        edtDriverName = findViewById(R.id.edtDriverName);
        edtStatus = findViewById(R.id.edtStatus);
        lvBus = findViewById(R.id.lvBus);
        btnAddBus = findViewById(R.id.btnAddBus);
        btnUpdateBus = findViewById(R.id.btnUpdateBus);
        btnDeleteBus = findViewById(R.id.btnDeleteBus);
        btnFindBus = findViewById(R.id.btnFindBus);
        displayBus();
        btnAddBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String licensePlate = edtLicensePlate.getText().toString().trim();
                String seatCount = edtSeatCount.getText().toString().trim();
                String driverName = edtDriverName.getText().toString().trim();
                String status = edtStatus.getText().toString().trim();

                if (licensePlate.isEmpty() || seatCount.isEmpty() || driverName.isEmpty() || status.isEmpty()) {
                    Toast.makeText(Admin_Bus.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                long busID = db.addBus(licensePlate, Integer.parseInt(seatCount), driverName, status);
                if (busID == -1) {
                    Toast.makeText(Admin_Bus.this, "Error adding bus", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Admin_Bus.this, "Bus added successfully", Toast.LENGTH_SHORT).show();
                    clearFields();
                }
            }
        });

        btnUpdateBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String busID = edtBusID.getText().toString().trim();
                String licensePlate = edtLicensePlate.getText().toString().trim();
                String seatCount = edtSeatCount.getText().toString().trim();
                String driverName = edtDriverName.getText().toString().trim();
                String status = edtStatus.getText().toString().trim();

                if (busID.isEmpty() || licensePlate.isEmpty() || seatCount.isEmpty() || driverName.isEmpty() || status.isEmpty()) {
                    Toast.makeText(Admin_Bus.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                int rowsAffected = db.updateBus(Integer.parseInt(busID), licensePlate, Integer.parseInt(seatCount), driverName, status);
                if (rowsAffected == 0) {
                    Toast.makeText(Admin_Bus.this, "Error updating bus", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Admin_Bus.this, "Bus updated successfully", Toast.LENGTH_SHORT).show();
                    clearFields();
                }
            }
        });

        btnDeleteBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String busID = edtBusID.getText().toString().trim();

                if (busID.isEmpty()) {
                    Toast.makeText(Admin_Bus.this, "Bus ID is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                int rowsAffected = db.deleteBus(Integer.parseInt(busID));
                if (rowsAffected == 0) {
                    Toast.makeText(Admin_Bus.this, "Error deleting bus", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Admin_Bus.this, "Bus deleted successfully", Toast.LENGTH_SHORT).show();
                    clearFields();
                }
            }
        });

        btnFindBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String licensePlate = edtLicensePlate.getText().toString().trim();

                if (licensePlate.isEmpty()) {
                    Toast.makeText(Admin_Bus.this, "License plate is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor = db.findBusByLicensePlate(licensePlate);
                if (cursor.moveToFirst()) {
                    edtBusID.setText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BUS_ID)));
                    edtLicensePlate.setText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LICENSE_PLATE)));
                    edtSeatCount.setText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SEAT_COUNT)));
                    edtDriverName.setText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DRIVER_NAME)));
                    edtStatus.setText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                } else {
                    Toast.makeText(Admin_Bus.this, "Bus not found", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            }
        });

    }

    @SuppressLint("Range")
    private void displayBus() {
        Cursor cursor = db.getAllBuses();
        if (cursor != null) {
            // Tạo danh sách các dữ liệu để hiển thị
            List<Map<String, String>> data = new ArrayList<>();
            while (cursor.moveToNext()) {
                Map<String, String> item = new HashMap<>();
                item.put("BusID", cursor.getString(cursor.getColumnIndex(COLUMN_BUS_ID)));
                item.put("LicensePlate", cursor.getString(cursor.getColumnIndex(COLUMN_LICENSE_PLATE)));
                item.put("SeatCount", cursor.getString(cursor.getColumnIndex(COLUMN_SEAT_COUNT)));
                item.put("DriverName", cursor.getString(cursor.getColumnIndex(COLUMN_DRIVER_NAME)));
                item.put("Status", cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
                data.add(item);
            }
            cursor.close();

            // Định nghĩa các cột trong ListView
            String[] from = {"BusID", "LicensePlate", "SeatCount", "DriverName", "Status"};
            int[] to = {R.id.tvBusID, R.id.tvLicensePlate, R.id.tvSeatCount, R.id.tvDriverName, R.id.tvStatus};

            // Tạo SimpleAdapter và gán cho ListView
            SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.list_item_bus, from, to);
            lvBus.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Không có dữ liệu chuyến xe bus", Toast.LENGTH_SHORT).show();
        }
    }


    private void clearFields() {
        edtBusID.setText("");
        edtLicensePlate.setText("");
        edtSeatCount.setText("");
        edtDriverName.setText("");
        edtStatus.setText("");
    }

}