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

public class Admin_TicketFragment extends Fragment {

    Button btnBus;
    Button btnRoute;
    Button btnBusTrip;
    Button btnTicket;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_admin_ticket_fragment, container, false);

        // Initialize buttons
        btnBus = view.findViewById(R.id.btnBus);
        btnRoute = view.findViewById(R.id.btnRoute);
        btnBusTrip = view.findViewById(R.id.btnBusTrip);
        btnTicket = view.findViewById(R.id.btnTicket);

        // Set up button click listeners
        btnBus.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Admin_Bus.class);
            startActivity(intent);
        });

        btnRoute.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Admin_Route.class);
            startActivity(intent);
        });

        btnBusTrip.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Admin_BusTrip.class);
            startActivity(intent);
        });

        btnTicket.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AdminTicketMain.class);
            startActivity(intent);
        });

        return view;
    }
}
