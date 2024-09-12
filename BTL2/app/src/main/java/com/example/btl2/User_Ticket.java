package com.example.btl2;

import static android.R.layout.simple_list_item_1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class User_Ticket extends Fragment {
    private Button btnSelectDate;
    private Button btnDecrease, btnIncrease;
    private Button btnFindTicket;
    private DatabaseHelper dbHelper;
    private TextView tvSeatCount, tvNgayDi;
    private Spinner spinnerStartLocation, spinnerEndLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_ticket, container, false);

        dbHelper = new DatabaseHelper(getContext()); // Khởi tạo DatabaseHelper

        btnSelectDate = view.findViewById(R.id.btnSelectDate);
        btnDecrease = view.findViewById(R.id.btnDecrease);
        btnIncrease = view.findViewById(R.id.btnIncrease);
        tvSeatCount = view.findViewById(R.id.tvSeatCount);
        tvNgayDi = view.findViewById(R.id.tvDayGo);
        btnFindTicket = view.findViewById(R.id.btn_findTicket);
        spinnerStartLocation = view.findViewById(R.id.spnFrom);
        spinnerEndLocation = view.findViewById(R.id.spnTo);

        // Thiết lập spinner
        setupLocationSpinners();
        setCurrentDate();

        btnFindTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String startLocation = spinnerStartLocation.getSelectedItem().toString();
                    String endLocation = spinnerEndLocation.getSelectedItem().toString();
                    String selectedDate = tvNgayDi.getText().toString().replace("Ngày đi: ", "");
                    int seatCount = Integer.parseInt(tvSeatCount.getText().toString());

                    // Gọi phương thức để tìm chuyến xe phù hợp từ cơ sở dữ liệu
                    List<BusTrip> matchingTrips = dbHelper.findBusTrips(startLocation, endLocation, selectedDate, seatCount);

                    if (!matchingTrips.isEmpty()) {
                        Intent intent = new Intent(getActivity(), User_ViewTicket.class);
                        intent.putParcelableArrayListExtra("MatchingTrips", new ArrayList<>(matchingTrips));
                        intent.putExtra("seatNum", seatCount);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "Không tìm thấy chuyến xe phù hợp", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("User_Ticket", "Error finding tickets", e);
                    Toast.makeText(getContext(), "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(tvSeatCount.getText().toString());
                if (count > 1) {
                    count--;
                    tvSeatCount.setText(String.valueOf(count));
                }
            }
        });

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(tvSeatCount.getText().toString());
                count++;
                tvSeatCount.setText(String.valueOf(count));
            }
        });

        return view;
    }

    private void setupLocationSpinners() {
        // Lấy danh sách các địa điểm xuất phát từ database
        List<String> startLocations = dbHelper.getAllStartLocations();

        // Tạo ArrayAdapter và gán dữ liệu
        ArrayAdapter<String> adapterStart = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, startLocations);
        adapterStart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStartLocation.setAdapter(adapterStart);

        // Sự kiện chọn địa điểm xuất phát
        spinnerStartLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStart = parent.getItemAtPosition(position).toString();

                // Lấy các địa điểm kết thúc tương thích với địa điểm xuất phát
                List<String> compatibleEndLocations = dbHelper.getCompatibleEndLocations(selectedStart);

                // Tạo ArrayAdapter và gán dữ liệu
                ArrayAdapter<String> adapterEnd = new ArrayAdapter<>(getContext(), simple_list_item_1, compatibleEndLocations);
                adapterEnd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerEndLocation.setAdapter(adapterEnd);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không có gì được chọn
            }
        });
    }

    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(calendar.getTime());
        tvNgayDi.setText("Ngày đi: " + currentDate);
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, (month + 1), year);
                tvNgayDi.setText("Ngày đi: " + selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }
}

