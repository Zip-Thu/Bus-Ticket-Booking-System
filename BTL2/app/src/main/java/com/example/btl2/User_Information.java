package com.example.btl2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

public class User_Information extends Fragment {
    Button btnGioiThieu;
    Button btnTuyenDuong;
    Button btnVanPhong;
    Button btnQuyChe;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_information, container, false);
        // Initialize buttons
        btnGioiThieu = view.findViewById(R.id.btnGioiThieu);
        btnTuyenDuong = view.findViewById(R.id.btnTuyenDuong);
        btnVanPhong = view.findViewById(R.id.btnVanPhong);
        btnQuyChe = view.findViewById(R.id.btnQuyChe);

        // Set up button click listeners
        btnGioiThieu.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), User_gioithieu.class);
            startActivity(intent);
        });

        btnTuyenDuong.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), User_tuyenduong.class);
            startActivity(intent);
        });

        btnVanPhong.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),User_vanphong.class);
            startActivity(intent);
        });

        btnQuyChe.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Use_quyche.class);
            startActivity(intent);
        });

        return view;
    }
}