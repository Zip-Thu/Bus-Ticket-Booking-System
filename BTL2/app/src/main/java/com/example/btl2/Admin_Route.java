package com.example.btl2;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Admin_Route extends AppCompatActivity {

    private static final String COLUMN_ROUTE_ID = "RouteID";
    private static final String COLUMN_START_LOCATION = "StartLocation";
    private static final String COLUMN_END_LOCATION = "EndLocation";

    private EditText edtRouteID, edtStartLocation, edtEndLocation;
    private Button btnAddRoute;
    private DatabaseHelper db;
    private ListView lvRoutes;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_route);

        db = new DatabaseHelper(this);

        edtStartLocation = findViewById(R.id.edtStartLocation);
        edtEndLocation = findViewById(R.id.edtEndLocation);
        lvRoutes = findViewById(R.id.lvRoutes);
        btnAddRoute = findViewById(R.id.btnAddRoute);
        displayBusTrips();
        btnAddRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startLocation = edtStartLocation.getText().toString().trim();
                String endLocation = edtEndLocation.getText().toString().trim();

                if (startLocation.isEmpty() || endLocation.isEmpty()) {
                    Toast.makeText(Admin_Route.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                long routeID = db.addRoute(startLocation, endLocation);
                if (routeID == -1) {
                    Toast.makeText(Admin_Route.this, "Error adding route", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Admin_Route.this, "Route added successfully", Toast.LENGTH_SHORT).show();
                    clearFields();
                }
            }
        });
    }

    @SuppressLint("Range")
    private void displayBusTrips() {
        Cursor cursor = db.getAllRoutes();
        if (cursor != null) {
            // Tạo danh sách các dữ liệu để hiển thị
            List<Map<String, String>> data = new ArrayList<>();
            while (cursor.moveToNext()) {
                Map<String, String> item = new HashMap<>();
                item.put("RouteID", cursor.getString(cursor.getColumnIndex(COLUMN_ROUTE_ID)));
                item.put("StartLocation", cursor.getString(cursor.getColumnIndex(COLUMN_START_LOCATION)));
                item.put("EndLocation", cursor.getString(cursor.getColumnIndex(COLUMN_END_LOCATION)));
                data.add(item);
            }
            cursor.close();

            // Định nghĩa các cột trong ListView
            String[] from = {"RouteID", "StartLocation", "EndLocation"};
            int[] to = {R.id.tvRouteID, R.id.tvStartLocation, R.id.tvEndLocation};

            // Tạo SimpleAdapter và gán cho ListView
            SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.list_item_route, from, to);
            lvRoutes.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Không có dữ liệu chuyến xe bus", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        edtRouteID.setText("");
        edtStartLocation.setText("");
        edtEndLocation.setText("");
    }
}