package com.example.btl2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class BusTripAdapter extends BaseAdapter {
    private Context context;
    private List<BusTrip> busTrips;
    private DatabaseHelper dbHelper;
    private int customerId; // ID của khách hàng
    private int seatCount; // Số ghế đặt
    private static final int PRICE_PER_SEAT = 250000;

    public BusTripAdapter(Context context, List<BusTrip> busTrips, int customerId, int seatCount) {
        this.context = context;
        this.busTrips = busTrips;
        this.customerId = customerId;
        this.seatCount = seatCount;
        dbHelper = new DatabaseHelper(context); // Khởi tạo DatabaseHelper
    }

    @Override
    public int getCount() {
        return busTrips.size();
    }

    @Override
    public Object getItem(int position) {
        return busTrips.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_item_bus, parent, false);
        }

        // Khai báo các thành phần trong item
        TextView tvDate = convertView.findViewById(R.id.tvUserDate);
        TextView tvTime = convertView.findViewById(R.id.tvUserTime);
        TextView tvSeatCount = convertView.findViewById(R.id.tvUserSeatCount);
        TextView tvTotal = convertView.findViewById(R.id.tvTotal11);
        Button btnBook = convertView.findViewById(R.id.btnBook);

        // Lấy dữ liệu BusTrip
        final BusTrip busTrip = busTrips.get(position);
        tvDate.setText(busTrip.getDate());
        tvTime.setText(busTrip.getTime());
        tvSeatCount.setText(String.valueOf(busTrip.getSeatCount()));
        int totalPrice = seatCount * PRICE_PER_SEAT;
        tvTotal.setText(String.valueOf(totalPrice) + " VND");

        // Xử lý sự kiện khi bấm nút "Đặt"
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chèn dữ liệu vào bảng Ticket
                boolean isInserted = dbHelper.insertTicket(seatCount, PRICE_PER_SEAT, totalPrice, customerId, busTrip.getId());

                if (isInserted) {
                    // Giảm số ghế còn lại của chuyến xe
                    int newSeatCount = busTrip.getSeatCount() - seatCount; // Số ghế mới sau khi đặt
                    boolean isUpdated = dbHelper.updateSeatCount(busTrip.getId(), newSeatCount); // Gọi phương thức đã tồn tại

                    if (isUpdated) {
                        // Cập nhật giao diện với số ghế mới
                        busTrip.setSeatCount(newSeatCount);
                        notifyDataSetChanged(); // Cập nhật lại ListView
                        Toast.makeText(context, "Đặt vé thành công cho chuyến: " + busTrip.getDate() + " lúc " + busTrip.getTime(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Cập nhật số ghế thất bại!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Đặt vé thất bại cho chuyến: " + busTrip.getDate() + " lúc " + busTrip.getTime(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }
}
