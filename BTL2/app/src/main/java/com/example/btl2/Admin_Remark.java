package com.example.btl2;


import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Admin_Remark extends Fragment {

    private ListView lvRemarks;
    private TextView tvRemarksTitle;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_admin_remark, container, false);

        lvRemarks = view.findViewById(R.id.lvRemarks);
        tvRemarksTitle = view.findViewById(R.id.tvRemarksTitle);

        dbHelper = new DatabaseHelper(getContext());

        loadRemarks();

        return view;
    }

    @SuppressLint("Range")
    private void loadRemarks() {
        Cursor cursor = dbHelper.getAllRemarks();  // Method to fetch all remarks from the database
        if (cursor != null && cursor.moveToFirst()) {
            List<Map<String, String>> data = new ArrayList<>();
            do {
                Map<String, String> item = new HashMap<>();
                item.put("RemarkID", cursor.getString(cursor.getColumnIndex("RemarkID")));
                item.put("StarNum", cursor.getString(cursor.getColumnIndex("StarNum")));
                item.put("Description", cursor.getString(cursor.getColumnIndex("Description")));
                data.add(item);
            } while (cursor.moveToNext());
            cursor.close();

            String[] from = {"RemarkID", "StarNum", "Description"};
            int[] to = {R.id.tvRemarkID, R.id.tvStarNum, R.id.tvDescription};

            SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, R.layout.list_remark, from, to);
            lvRemarks.setAdapter(adapter);
        } else {
            Toast.makeText(getActivity(), "Không có đánh giá nào", Toast.LENGTH_SHORT).show();
        }
    }
}