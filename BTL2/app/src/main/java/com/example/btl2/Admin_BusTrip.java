package com.example.btl2;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Admin_BusTrip extends AppCompatActivity {
    private EditText edtBusTripID, edtDate, edtTime, edtSeatCount;
    private Spinner spinnerBus, spinnerStartLocation, spinnerEndLocation;
    private Button btnAddBusTrip, btnUpdateBusTrip, btnDeleteBusTrip, btnFindBusTrip;
    private ListView lvBusTrips;
    private DatabaseHelper dbHelper;
    private static final String COLUMN_BUS_ID_FK = "BusID";
    private static final String COLUMN_ROUTE_ID_FK = "RouteID";
    private static final String COLUMN_DATE = "Date";
    private static final String COLUMN_TIME = "Time";
    public static final String COLUMN_BUSTRIP_ID = "BusTripID";
    private static final String COLUMN_SEAT_COUNT = "SeatCount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_bus_trip);

        // Khởi tạo các view
        edtBusTripID = findViewById(R.id.edtBusTripID);
        edtDate = findViewById(R.id.edtDate);
        edtTime = findViewById(R.id.edtTime);
        edtSeatCount = findViewById(R.id.edtSeatCount);
        spinnerBus = findViewById(R.id.spinnerBus);
        spinnerStartLocation = findViewById(R.id.spinnerStartLocation);
        spinnerEndLocation = findViewById(R.id.spinnerEndLocation);
        btnAddBusTrip = findViewById(R.id.btnAddBusTrip);
        btnUpdateBusTrip = findViewById(R.id.btnUpdateBusTrip);
        btnDeleteBusTrip = findViewById(R.id.btnDeleteBusTrip);
        btnFindBusTrip = findViewById(R.id.btnFindBusTrip);
        lvBusTrips = findViewById(R.id.lvBusTrips);

        // Khởi tạo database helper
        dbHelper = new DatabaseHelper(this);

        // Thiết lập Spinner dữ liệu
        setupBusSpinner();
        setupLocationSpinners();

        // Thiết lập sự kiện cho các button
        btnAddBusTrip.setOnClickListener(view -> addBusTrip());
        btnUpdateBusTrip.setOnClickListener(view -> updateBusTrip());
        btnDeleteBusTrip.setOnClickListener(view -> deleteBusTrip());
        btnFindBusTrip.setOnClickListener(view -> findBusTrip());

        // Thiết lập DatePicker và TimePicker
        edtDate.setOnClickListener(view -> showDatePickerDialog());
        edtTime.setOnClickListener(view -> showTimePickerDialog());

        // Hiển thị danh sách các chuyến xe bus hiện có
        displayBusTrips();

    }
    private void setupBusSpinner() {
        List<String> busPlates = dbHelper.getAllBusPlates(); // Lấy tất cả biển số xe từ DB
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, busPlates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBus.setAdapter(adapter);

        spinnerBus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPlate = parent.getItemAtPosition(position).toString();
                int seatCount = dbHelper.getSeatCountByBusPlate(selectedPlate); // Lấy số ghế từ biển số
                edtSeatCount.setText(String.valueOf(seatCount));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không có gì được chọn
            }
        });
    }
    private void setupLocationSpinners() {
        List<String> startLocations = dbHelper.getAllStartLocations();
        ArrayAdapter<String> adapterStart = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, startLocations);
        adapterStart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStartLocation.setAdapter(adapterStart);

        spinnerStartLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStart = parent.getItemAtPosition(position).toString();
                List<String> compatibleEndLocations = dbHelper.getCompatibleEndLocations(selectedStart);
                ArrayAdapter<String> adapterEnd = new ArrayAdapter<>(Admin_BusTrip.this, android.R.layout.simple_spinner_item, compatibleEndLocations);
                adapterEnd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerEndLocation.setAdapter(adapterEnd);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không có gì được chọn
            }
        });
    }
    @SuppressLint("Range")
    private void displayBusTrips() {
        Cursor cursor = dbHelper.getAllBusTrips();
        if (cursor != null) {
            // Tạo danh sách các dữ liệu để hiển thị
            List<Map<String, String>> data = new ArrayList<>();
            while (cursor.moveToNext()) {
                Map<String, String> item = new HashMap<>();
                item.put("BusTripID", cursor.getString(cursor.getColumnIndex(COLUMN_BUSTRIP_ID)));
                item.put("Date", cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                item.put("Time", cursor.getString(cursor.getColumnIndex(COLUMN_TIME)));
                item.put("SeatCount", cursor.getString(cursor.getColumnIndex(COLUMN_SEAT_COUNT)));
                item.put("BusID", cursor.getString(cursor.getColumnIndex(COLUMN_BUS_ID_FK)));
                item.put("RouteID", cursor.getString(cursor.getColumnIndex(COLUMN_ROUTE_ID_FK)));
                data.add(item);
            }
            cursor.close();

            // Định nghĩa các cột trong ListView
            String[] from = {"BusTripID", "Date", "Time", "SeatCount", "BusID", "RouteID"};
            int[] to = {R.id.tvBusTripID1, R.id.tvDate, R.id.tvTime, R.id.tvSeatCount, R.id.tvBusID, R.id.tvRouteID};

            // Tạo SimpleAdapter và gán cho ListView
            SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.list_item_bus_trip, from, to);
            lvBusTrips.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Không có dữ liệu chuyến xe bus", Toast.LENGTH_SHORT).show();
        }
    }


    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year1, month1, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String date = sdf.format(selectedDate.getTime());
            edtDate.setText(date);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
            edtTime.setText(time);
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void addBusTrip() {
        String date = edtDate.getText().toString();
        String time = edtTime.getText().toString();
        String busPlate = spinnerBus.getSelectedItem().toString();
        int seatCount = Integer.parseInt(edtSeatCount.getText().toString());
        String startLocation = spinnerStartLocation.getSelectedItem().toString();
        String endLocation = spinnerEndLocation.getSelectedItem().toString();
        int busID = dbHelper.getBusID(busPlate);

        long result = dbHelper.addBusTrip(date, time, seatCount, startLocation, endLocation, busID);

        if (result != -1) {
            Toast.makeText(this, "Thêm chuyến xe bus thành công", Toast.LENGTH_SHORT).show();
            displayBusTrips(); // Hiển thị lại danh sách
            clearFields();
        } else {
            Toast.makeText(this, "Thêm chuyến xe bus thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateBusTrip() {
        int busTripID = Integer.parseInt(edtBusTripID.getText().toString());
        String date = edtDate.getText().toString();
        String time = edtTime.getText().toString();
        String busPlate = spinnerBus.getSelectedItem().toString();
        int seatCount = Integer.parseInt(edtSeatCount.getText().toString());
        String startLocation = spinnerStartLocation.getSelectedItem().toString();
        String endLocation = spinnerEndLocation.getSelectedItem().toString();

        int rowsAffected = dbHelper.updateBusTrip(busTripID, date, time, busPlate, seatCount, startLocation, endLocation);

        if (rowsAffected > 0) {
            Toast.makeText(this, "Sửa chuyến xe bus thành công", Toast.LENGTH_SHORT).show();
            displayBusTrips(); // Hiển thị lại danh sách
            clearFields();
        } else {
            Toast.makeText(this, "Sửa chuyến xe bus thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("Range")
    private void findBusTrip() {
        int busTripID = Integer.parseInt(edtBusTripID.getText().toString());
        Cursor cursor = dbHelper.findBusTripByID(busTripID);

        if (cursor != null && cursor.moveToFirst()) {
            int busID = cursor.getInt(cursor.getColumnIndex(COLUMN_BUS_ID_FK));
            int routeID = cursor.getInt(cursor.getColumnIndex(COLUMN_ROUTE_ID_FK));

            String busPlate = dbHelper.getBusPlateByID(busID);
            String[] routeLocations = dbHelper.getRouteLocationsByID(routeID);

            // Hiển thị thông tin lên UI
            edtBusTripID.setText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BUSTRIP_ID)));
            edtDate.setText(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            edtTime.setText(cursor.getString(cursor.getColumnIndex(COLUMN_TIME)));
            spinnerBus.setSelection(((ArrayAdapter) spinnerBus.getAdapter()).getPosition(busPlate));
            edtSeatCount.setText(cursor.getString(cursor.getColumnIndex(COLUMN_SEAT_COUNT)));
            spinnerStartLocation.setSelection(((ArrayAdapter) spinnerStartLocation.getAdapter()).getPosition(routeLocations[0]));
            spinnerEndLocation.setSelection(((ArrayAdapter) spinnerEndLocation.getAdapter()).getPosition(routeLocations[1]));
        } else {
            Toast.makeText(this, "Không tìm thấy chuyến xe bus", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) cursor.close();
    }


    private void deleteBusTrip() {
        int busTripID = Integer.parseInt(edtBusTripID.getText().toString());

        int rowsAffected = dbHelper.deleteBusTrip(busTripID);

        if (rowsAffected > 0) {
            Toast.makeText(this, "Xóa chuyến xe bus thành công", Toast.LENGTH_SHORT).show();
            displayBusTrips(); // Hiển thị lại danh sách
        } else {
            Toast.makeText(this, "Xóa chuyến xe bus thất bại", Toast.LENGTH_SHORT).show();
        }
    }
    private void clearFields() {
        edtBusTripID.setText("");
        edtDate.setText("");
        edtTime.setText("");
        edtSeatCount.setText("");
    }

    private void onDateSet(DatePicker view, int year1, int month1, int dayOfMonth) {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year1, month1, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String date = sdf.format(selectedDate.getTime());
        edtDate.setText(date);
    }
}