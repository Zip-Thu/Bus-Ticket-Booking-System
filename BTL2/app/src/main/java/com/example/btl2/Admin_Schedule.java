package com.example.btl2;

import android.os.Bundle;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.TimePickerDialog;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import java.util.Calendar;

public class Admin_Schedule extends AppCompatActivity {
    private TextView textThoiGian;
    private Spinner spinnerXuatPhat;
    private Spinner spinnerDen ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_schedule);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });textThoiGian = findViewById(R.id.textThoiGian);
        textThoiGian.setOnClickListener(v -> showTimePickerDialog());

        spinnerXuatPhat = findViewById(R.id.spinnerXuatPhat);
        spinnerDen = findViewById(R.id.spinnerDen);

        // Tạo một ArrayAdapter sử dụng mảng chuỗi và một spinner layout mặc định
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.places, android.R.layout.simple_spinner_item);

        // Chỉ định layout được sử dụng khi danh sách các tùy chọn hiện ra
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Áp dụng adapter cho spinner
        spinnerXuatPhat.setAdapter(adapter);
        spinnerDen.setAdapter(adapter);
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minuteOfHour) -> {
            String selectedTime = String.format("%02d:%02d", hourOfDay, minuteOfHour);
            textThoiGian.setText(selectedTime);
        }, hour, minute, true);

        timePickerDialog.show();
    }
}