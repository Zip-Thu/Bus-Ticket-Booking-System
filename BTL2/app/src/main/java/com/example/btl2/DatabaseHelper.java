package com.example.btl2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "busBooking.db";
    private static final int DATABASE_VERSION = 12;

    // Table and column names
    public static final String TABLE_CUSTOMER = "Customer";
    public static final String TABLE_ROUTE = "Route";
    public static final String TABLE_BUS = "Bus";
    public static final String TABLE_BUSTRIP = "BusTrip";
    public static final String TABLE_TICKET = "Ticket";
    public static final String TABLE_ACCOUNT = "Account";
    private static final String TABLE_REMARK = "Remark";

    public static final String COLUMN_CUSTOMER_ID = "CustomerID";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_PHONE_NUMBER = "PhoneNumber";
    public static final String COLUMN_EMAIL = "Email";

    public static final String COLUMN_ROUTE_ID = "RouteID";
    public static final String COLUMN_START_LOCATION = "StartLocation";
    public static final String COLUMN_END_LOCATION = "EndLocation";

    public static final String COLUMN_BUS_ID = "BusID";
    public static final String COLUMN_LICENSE_PLATE = "LicensePlate";
    public static final String COLUMN_SEAT_COUNT = "SeatCount";
    public static final String COLUMN_DRIVER_NAME = "DriverName";
    public static final String COLUMN_STATUS = "Status";

    public static final String COLUMN_BUSTRIP_ID = "BusTripID";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_TIME = "Time";
    public static final String COLUMN_ROUTE_ID_FK = "RouteID";
    public static final String COLUMN_BUS_ID_FK = "BusID";

    public static final String COLUMN_TICKET_ID = "TicketID";
    public static final String COLUMN_SEAT_NUM = "SeatNum";
    public static final String COLUMN_PRICE = "Price";
    public static final String COLUMN_TOTAL = "Total";
    public static final String COLUMN_CUSTOMER_ID_FK = "CustomerID";
    public static final String COLUMN_BUSTRIP_ID_FK = "BusTripID";

    public static final String COLUMN_REMARK_ID = "RemarkID";
    public static final String COLUMN_STAR_NUM = "StarNum";
    public static final String COLUMN_DESCRIPTION = "Description";
    //public static final String COLUMN_TICKET_ID_FK = "TicketID";
    public static final String COLUMN_CUSTOMER_ID_RM_FK = "CustomerID";

    public static final String COLUMN_ACCOUNT_ID = "AccountID";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_EMAIL_FK = "Email";
    public static final String COLUMN_PERMISSION_FK = "PermissionID";



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUSTRIP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMARK);


        // Recreate tables
        onCreate(db);
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // KHÁCH HÀNG
        db.execSQL("CREATE TABLE " + TABLE_CUSTOMER + " (" +
                COLUMN_CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PHONE_NUMBER + " TEXT, " +
                COLUMN_EMAIL + " TEXT)");

        // TUYẾN ĐƯỜNG
        db.execSQL("CREATE TABLE " + TABLE_ROUTE + " (" +
                COLUMN_ROUTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_START_LOCATION + " TEXT, " +
                COLUMN_END_LOCATION + " TEXT)");

        // XE
        db.execSQL("CREATE TABLE " + TABLE_BUS + " (" +
                COLUMN_BUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LICENSE_PLATE + " TEXT, " +
                COLUMN_SEAT_COUNT + " INTEGER, " +
                COLUMN_DRIVER_NAME + " TEXT, " +
                COLUMN_STATUS + " TEXT)");

        // BUSTRIP
        db.execSQL("CREATE TABLE " + TABLE_BUSTRIP + " (" +
                COLUMN_BUSTRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_SEAT_COUNT + " INTEGER, " +
                COLUMN_ROUTE_ID_FK + " INTEGER, " +
                COLUMN_BUS_ID_FK + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_ROUTE_ID_FK + ") REFERENCES " + TABLE_ROUTE + "(" + COLUMN_ROUTE_ID + ") ON DELETE CASCADE," +
                "FOREIGN KEY(" + COLUMN_BUS_ID_FK + ") REFERENCES " + TABLE_BUS + "(" + COLUMN_BUS_ID + ") ON DELETE CASCADE" +
                ")");

        // VÉ
        db.execSQL("CREATE TABLE " + TABLE_TICKET + " (" +
                COLUMN_TICKET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SEAT_NUM + " INTEGER, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_TOTAL + " REAL," +
                COLUMN_CUSTOMER_ID_FK + " INTEGER, " +
                COLUMN_BUSTRIP_ID_FK + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_CUSTOMER_ID_FK + ") REFERENCES " + TABLE_CUSTOMER + "(" + COLUMN_CUSTOMER_ID + ") ON DELETE CASCADE," +
                "FOREIGN KEY(" + COLUMN_BUSTRIP_ID_FK + ") REFERENCES " + TABLE_BUSTRIP + "(" + COLUMN_BUSTRIP_ID + ") ON DELETE CASCADE" +
                ")");

        // ĐÁNH GIÁ
        db.execSQL ("CREATE TABLE " + TABLE_REMARK + " (" +
                COLUMN_REMARK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STAR_NUM + " INTEGER, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_CUSTOMER_ID_FK + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_CUSTOMER_ID_RM_FK + ") REFERENCES " + TABLE_CUSTOMER + "(" + COLUMN_CUSTOMER_ID + ") ON DELETE CASCADE" +
                ")");



        // TÀI KHOẢN
        db.execSQL("CREATE TABLE " + TABLE_ACCOUNT + " (" +
                COLUMN_ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_EMAIL_FK + " TEXT, " +
                COLUMN_PHONE_NUMBER + " TEXT, " +
                COLUMN_PERMISSION_FK + " TEXT)");

        ContentValues values = new ContentValues();

        values.put(COLUMN_EMAIL_FK, "diepanhthu@gmail.com");
        values.put(COLUMN_PASSWORD, "user123");
        values.put(COLUMN_PERMISSION_FK, "user");
        db.insert(TABLE_ACCOUNT, null, values);

        values.put(COLUMN_EMAIL_FK, "lyminhtam@gmail.com");
        values.put(COLUMN_PASSWORD, "admin123");
        values.put(COLUMN_PERMISSION_FK, "admin");
        db.insert(TABLE_ACCOUNT, null, values);

        values.put(COLUMN_EMAIL_FK, "diepanhthu123@gmail.com");
        values.put(COLUMN_PASSWORD, "admin123");
        values.put(COLUMN_PERMISSION_FK, "admin");
        db.insert(TABLE_ACCOUNT, null, values);


    }


    public Customer getCustomerByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Customer customer = null;

        String query = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE_NUMBER));
            String emailAddress = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));

            customer = new Customer(id, name, phone, emailAddress);
            cursor.close();
        }

        return customer;
    }

    //GET KHÁCH HÀNG
    public Cursor getAllCustomer() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CUSTOMER;
        return db.rawQuery(query, null);
    }



    //CHECK ĐIỀU KIỆN ĐĂNG NHẬP
    public boolean checkUserCredentials(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ACCOUNT + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        boolean valid = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return valid;
    }
    public boolean updateSeatCount(int busTripId, int newSeatCount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SEAT_COUNT, newSeatCount); // Cập nhật cột seatCount
        int result = db.update(TABLE_BUSTRIP, contentValues, COLUMN_BUSTRIP_ID + " = ?", new String[]{String.valueOf(busTripId)});
        db.close();
        return result > 0; // Trả về true nếu cập nhật thành công
    }

    public Customer getCustomerById(long customerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Customer customer = null;

        try {
            // Thay đổi `_id` thành `COLUMN_CUSTOMER_ID`
            cursor = db.query(TABLE_CUSTOMER, null, COLUMN_CUSTOMER_ID + " = ?", new String[]{String.valueOf(customerId)}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Lấy các chỉ số cột
                int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
                int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE_NUMBER);

                // Kiểm tra chỉ số cột
                if (nameIndex != -1 && emailIndex != -1 && phoneIndex != -1) {
                    String name = cursor.getString(nameIndex);
                    String email = cursor.getString(emailIndex);
                    String phone = cursor.getString(phoneIndex);

                    customer = new Customer(customerId, name, email, phone);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return customer;
    }

    public Cursor getTicketsByUserEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TICKET + " WHERE " + COLUMN_CUSTOMER_ID_FK + " = (SELECT " + COLUMN_CUSTOMER_ID + " FROM " + TABLE_CUSTOMER + " WHERE " + COLUMN_EMAIL + " = ?)";
        return db.rawQuery(query, new String[]{email});
    }

    public boolean cancelTicketByID(int ticketID) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + COLUMN_BUSTRIP_ID_FK + ", " + COLUMN_SEAT_NUM +
                " FROM " + TABLE_TICKET + " WHERE " + COLUMN_TICKET_ID + " = ?", new String[]{String.valueOf(ticketID)});

        if (cursor != null && cursor.moveToFirst()) {
            int busTripIDIndex = cursor.getColumnIndex(COLUMN_BUSTRIP_ID_FK);
            int seatNumIndex = cursor.getColumnIndex(COLUMN_SEAT_NUM);

            if (busTripIDIndex != -1 && seatNumIndex != -1) {
                int busTripID = cursor.getInt(busTripIDIndex);
                int seatNum = cursor.getInt(seatNumIndex);
                cursor.close();

                // Tăng lại SeatCount trong bảng BUSTRIP
                db.execSQL("UPDATE " + TABLE_BUSTRIP + " SET " + COLUMN_SEAT_COUNT + " = " + COLUMN_SEAT_COUNT + " + ? " +
                        "WHERE " + COLUMN_BUSTRIP_ID + " = ?", new Object[]{seatNum, busTripID});

                return db.delete(TABLE_TICKET, COLUMN_TICKET_ID + " = ?", new String[]{String.valueOf(ticketID)}) > 0;
            } else {

                cursor.close();
                return false;
            }
        }

        return false;
    }



    // THÊM XE
    public long addBus(String licensePlate, int seatCount, String driverName, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LICENSE_PLATE, licensePlate);
        values.put(COLUMN_SEAT_COUNT, seatCount);
        values.put(COLUMN_DRIVER_NAME, driverName);
        values.put(COLUMN_STATUS, status);

        long result = db.insert(TABLE_BUS, null, values);
        if (result != -1) {
            System.out.println("Bus added successfully.");
        } else {
            System.out.println("Failed to add bus.");
        }
        db.close();
        return result;
    }

    // SỬA XE
    public int updateBus(int busId, String licensePlate, int seatCount, String driverName, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LICENSE_PLATE, licensePlate);
        values.put(COLUMN_SEAT_COUNT, seatCount);
        values.put(COLUMN_DRIVER_NAME, driverName);
        values.put(COLUMN_STATUS, status);

        int rowsAffected = db.update(TABLE_BUS, values, COLUMN_BUS_ID + " = ?", new String[]{String.valueOf(busId)});
        if (rowsAffected > 0) {
            System.out.println("Bus updated successfully.");
        } else {
            System.out.println("Failed to update bus.");
        }
        db.close();
        return rowsAffected;
    }

    // XÓA XE
    public int deleteBus(int busId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_BUS, COLUMN_BUS_ID + " = ?", new String[]{String.valueOf(busId)});
        if (rowsAffected > 0) {
            System.out.println("Bus deleted successfully.");
        } else {
            System.out.println("Failed to delete bus.");
        }
        db.close();
        return rowsAffected;
    }

    public Cursor getAllBuses() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BUS;
        return db.rawQuery(query, null);
    }


    //GET BUS THEO BIỂN SỐ XE
    public int getBusID(String licensePlate) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT BusID FROM Bus WHERE LicensePlate = ?", new String[]{licensePlate});
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        cursor.close();
        return -1;
    }

    // TÌM KIẾM XE THEO BIỂN SỐ
    @SuppressLint("Range")
    public List<String> getAllBusPlates() {
        List<String> busPlates = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_LICENSE_PLATE + " FROM " + TABLE_BUS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                busPlates.add(cursor.getString(cursor.getColumnIndex(COLUMN_LICENSE_PLATE)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return busPlates;
    }

    // TÌM KIẾM XE THEO BIỂN SỐ
    public Cursor findBusByLicensePlate(String licensePlate) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BUS + " WHERE " + COLUMN_LICENSE_PLATE + " = ?", new String[]{licensePlate});
    }


    // GET SEATCOUNT THEO BIỂN SỐ
    @SuppressLint("Range")
    public int getSeatCountByBusPlate(String busPlate) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_SEAT_COUNT + " FROM " + TABLE_BUS + " WHERE " + COLUMN_LICENSE_PLATE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{busPlate});

        int seatCount = 0;
        if (cursor.moveToFirst()) {
            seatCount = cursor.getInt(cursor.getColumnIndex(COLUMN_SEAT_COUNT));
        }

        cursor.close();
        db.close();
        return seatCount;
    }

    //GET BIỂN SỐ BY ID
    @SuppressLint("Range")
    public String getBusPlateByID(int busID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String busPlate = null;

        String query = "SELECT " + COLUMN_LICENSE_PLATE + " FROM " + TABLE_BUS +
                " WHERE " + COLUMN_BUS_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(busID)});

        if (cursor.moveToFirst()) {
            busPlate = cursor.getString(cursor.getColumnIndex(COLUMN_LICENSE_PLATE));
        }

        cursor.close();
        db.close();

        return busPlate;
    }

    // THÊM TUYẾN ĐƯỜNG
    public long addRoute(String startLocation, String endLocation) {

        if (isRouteExists(startLocation, endLocation)) {
            return -1; // Trả về -1 nếu tuyến đường đã tồn tại
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_START_LOCATION, startLocation);
        values.put(COLUMN_END_LOCATION, endLocation);

        long result = db.insert(TABLE_ROUTE, null, values);
        if (result != -1) {
            System.out.println("Route added successfully.");
        } else {
            System.out.println("Failed to add route.");
        }
        db.close();
        return result;
    }

    // KIỂM TRA SỰ TỒN TẠI CỦA TUYẾN ĐƯỜNG
    public boolean isRouteExists(String startLocation, String endLocation) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ROUTE, new String[]{COLUMN_ROUTE_ID},
                COLUMN_START_LOCATION + " = ? AND " + COLUMN_END_LOCATION + " = ?",
                new String[]{startLocation, endLocation}, null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // LẤY ĐIỂM ĐẾN TƯƠNG THÍCH VỚI ĐIỂM KHỞI HÀNH
    @SuppressLint("Range")
    public List<String> getCompatibleEndLocations(String startLocation) {
        List<String> endLocations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_END_LOCATION + " FROM " + TABLE_ROUTE + " WHERE " + COLUMN_START_LOCATION + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{startLocation});

        if (cursor.moveToFirst()) {
            do {
                endLocations.add(cursor.getString(cursor.getColumnIndex(COLUMN_END_LOCATION)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return endLocations;
    }

    //GET ROUTE
    public int getRouteID(String startLocation, String endLocation) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT RouteID FROM Route WHERE StartLocation = ? AND EndLocation = ?", new String[]{startLocation, endLocation});
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        cursor.close();
        return -1;
    }


    //LẤY ĐIỂM BẮT ĐẦU VÀ KẾT THÚC THEO ID
    @SuppressLint("Range")
    public String[] getRouteLocationsByID(int routeID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] locations = new String[2]; // [0] = StartLocation, [1] = EndLocation

        String query = "SELECT " + COLUMN_START_LOCATION + ", " + COLUMN_END_LOCATION + " FROM " + TABLE_ROUTE +
                " WHERE " + COLUMN_ROUTE_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(routeID)});

        if (cursor.moveToFirst()) {
            locations[0] = cursor.getString(cursor.getColumnIndex(COLUMN_START_LOCATION));
            locations[1] = cursor.getString(cursor.getColumnIndex(COLUMN_END_LOCATION));
        }

        cursor.close();
        db.close();

        return locations;
    }

    public int getRouteIDbyBusTripID(int busTripID_fk) {
        SQLiteDatabase db = this.getReadableDatabase();
        int routeID = -1;  // Khởi tạo với giá trị mặc định là -1 (nếu không tìm thấy)

        String query = "SELECT " + COLUMN_ROUTE_ID_FK +
                " FROM " + TABLE_BUSTRIP +
                " WHERE " + COLUMN_BUSTRIP_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(busTripID_fk)});

        if (cursor != null && cursor.moveToFirst()) {
            routeID = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ROUTE_ID_FK));
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();  // Đóng database sau khi hoàn thành việc với cursor

        return routeID;
    }



    // THÊM BUSTRIP
    public long addBusTrip(String date, String time, int seatCount, String startLocation, String endLocation, int busID) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Lấy routeID từ bảng Route
        int routeID = getRouteID(startLocation, endLocation);

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_SEAT_COUNT, seatCount);
        values.put(COLUMN_ROUTE_ID_FK, routeID);  // Lưu routeID
        values.put(COLUMN_BUS_ID_FK, busID);

        long result = db.insert(TABLE_BUSTRIP, null, values);
        db.close();
        return result;
    }

    // SỬA BUSTRIP
    public int updateBusTrip(int busTripID, String date, String time, String busPlate, int seatCount, String startLocation, String endLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int busID = getBusID(busPlate);
        int routeID = getRouteID(startLocation, endLocation);

        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_BUS_ID_FK, busID);
        values.put(COLUMN_SEAT_COUNT, seatCount);
        values.put(COLUMN_ROUTE_ID_FK, routeID);

        int rowsAffected = db.update(TABLE_BUSTRIP, values, COLUMN_BUSTRIP_ID + " = ?", new String[]{String.valueOf(busTripID)});
        db.close();
        return rowsAffected;
    }

    // XÓA BUSTRIP
    public int deleteBusTrip(int busTripId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_BUSTRIP, COLUMN_BUSTRIP_ID + " = ?", new String[]{String.valueOf(busTripId)});
        if (rowsAffected > 0) {
            System.out.println("Bus trip deleted successfully.");
        } else {
            System.out.println("Failed to delete bus trip.");
        }
        db.close();
        return rowsAffected;
    }

    //CÁC PHƯƠNG THỨC XỬ LÍ BUSTRIP
    //GET ALL BUSTRIP
    public Cursor getAllBusTrips() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BUSTRIP;
        return db.rawQuery(query, null);
    }
    //TÌM BUSTRIP BY ID
    public Cursor findBusTripByID(int busTripID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BUSTRIP + " WHERE " + COLUMN_BUSTRIP_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(busTripID)});
        return cursor;
    }



    // GET TOÀN BỘ ROUTE BẰNG VIỆC CHỌN STARTLOCATION VÀ ENDLOCATION
    public ArrayList<String> getAllStartLocations() {
        ArrayList<String> startLocations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT startLocation FROM Route", null);
        if (cursor.moveToFirst()) {
            do {
                startLocations.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return startLocations;
    }

    public ArrayList<String> getAllEndLocations() {
        ArrayList<String> endLocations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT endLocation FROM Route", null);
        if (cursor.moveToFirst()) {
            do {
                endLocations.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return endLocations;
    }


    //GET TOÀN BỘ BUS DỰA TRÊN BIỂN SỐ XE
    public ArrayList<String> getAllBusLicensePlates() {
        ArrayList<String> busList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT LicensePlate FROM Bus", null);
        if (cursor.moveToFirst()) {
            do {
                busList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return busList;
    }

    // SEATCOUNT CỦA BUSTRIP BẰNG VỚI SEATCOUNT CỦA BUS
    public int getSeatCountByLicensePlate(String licensePlate) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SeatCount FROM Bus WHERE LicensePlate = ?", new String[]{licensePlate});
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        cursor.close();
        return -1;
    }

    //THỐNG KÊ TICKET
    public Cursor getTicketsByUser(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Ticket WHERE CustomerID IN (SELECT CustomerID FROM Customer WHERE Name = ?)", new String[]{userName});
    }


    public Cursor getTicketsByMonth(String month) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Ticket WHERE strftime('%m', Date) = ?", new String[]{month});
    }

    public Cursor getTicketsByBusTrip(String busTripID) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Ticket WHERE BusTripID = ?", new String[]{busTripID});
    }
    public boolean insertTicket(int seatNum, double price, double total, int customerId, int busTripId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SEAT_NUM, seatNum);
        contentValues.put(COLUMN_PRICE, price);
        contentValues.put(COLUMN_TOTAL, total);
        contentValues.put(COLUMN_CUSTOMER_ID_FK, customerId);
        contentValues.put(COLUMN_BUSTRIP_ID_FK, busTripId);

        long result = db.insert(TABLE_TICKET, null, contentValues);
        db.close();
        return result != -1; // Trả về true nếu chèn thành công
    }
    public Cursor getTicketDetails(int customerId, int busTripId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TICKET +
                " WHERE " + COLUMN_CUSTOMER_ID_FK + " = ?" +
                " AND " + COLUMN_BUSTRIP_ID_FK + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(customerId), String.valueOf(busTripId)});
    }

    public Cursor getTicketsByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn lấy vé theo ngày từ bảng BusTrip và liên kết với bảng Ticket
        String query = "SELECT t.*, b." + COLUMN_DATE +
                " FROM " + TABLE_TICKET + " t" +
                " JOIN " + TABLE_BUSTRIP + " b ON t." + COLUMN_BUSTRIP_ID_FK + " = b." + COLUMN_BUSTRIP_ID +
                " WHERE b." + COLUMN_DATE + " = ?";

        return db.rawQuery(query, new String[]{date});
    }

    public Cursor getTicketByID(int ticketID) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_TICKET,
                null, // Lấy tất cả các cột
                COLUMN_TICKET_ID + "=?",
                new String[]{String.valueOf(ticketID)},
                null,
                null,
                null);
    }


    public String getDateFromBusTripID(int busTripID_fk) {
        SQLiteDatabase db = this.getReadableDatabase();
        String date = null;  // Khởi tạo với giá trị mặc định là null

        String query = "SELECT " + COLUMN_DATE +
                " FROM " + TABLE_BUSTRIP +
                " WHERE " + COLUMN_BUSTRIP_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(busTripID_fk)});

        if (cursor != null && cursor.moveToFirst()) {
            date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();  // Đóng database sau khi hoàn thành việc với cursor

        return date;
    }

    public Cursor getTicketsByCustomerID(int customerID) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TICKET + " WHERE " + COLUMN_CUSTOMER_ID_FK + " = ?", new String[]{String.valueOf(customerID)});
    }



    //TÌM TICKET BY ID
    public Cursor findTicketDetailsByID(int ticketID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT *" + " FROM " + TABLE_TICKET + " WHERE " + COLUMN_TICKET_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(ticketID)});
        if (cursor != null) {
            cursor.moveToFirst(); // Di chuyển con trỏ đến bản ghi đầu tiên
        }
        return cursor; // Trả về con trỏ để sử dụng sau
    }




    // THÊM KHÁCH HÀNG
    public void addCustomer(String name, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE_NUMBER, phone);
        values.put(COLUMN_EMAIL, email);

        long result = db.insert(TABLE_CUSTOMER, null, values);
        if (result != -1) {
            System.out.println("Customer added successfully.");
        } else {
            System.out.println("Failed to add customer.");
        }
        db.close();
    }

    // SỬA KHÁCH HÀNG
    public void updateCustomer(int customerId, String name, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE_NUMBER, phone);
        values.put(COLUMN_EMAIL, email);

        int rowsAffected = db.update(TABLE_CUSTOMER, values, COLUMN_CUSTOMER_ID + " = ?", new String[]{String.valueOf(customerId)});
        if (rowsAffected > 0) {
            System.out.println("Customer updated successfully.");
        } else {
            System.out.println("Failed to update customer.");
        }
        db.close();
    }

    // XÓA KHÁCH HÀNG
    public void deleteCustomer(int customerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_CUSTOMER, COLUMN_CUSTOMER_ID + " = ?", new String[]{String.valueOf(customerId)});
        if (rowsAffected > 0) {
            System.out.println("Customer deleted successfully.");
        } else {
            System.out.println("Failed to delete customer.");
        }
        db.close();
    }

    //GET KHÁCH HÀNG
    public Cursor getCustomer(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_CUSTOMER, null, COLUMN_CUSTOMER_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
    }

    public Cursor getCustomerByPhone(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_CUSTOMER, null, COLUMN_PHONE_NUMBER + " = ?", new String[]{phone}, null, null, null);
    }

    //THÊM ĐÁNH GIÁ
    public long addRemark(int starNum, String description, int customerID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STAR_NUM, starNum);
        values.put(COLUMN_DESCRIPTION, description);
        //values.put(COLUMN_TICKET_ID_FK, ticketID);
        values.put(COLUMN_CUSTOMER_ID_FK, customerID);
        return db.insert(TABLE_REMARK, null, values);
    }

    public Cursor getAllRemarks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_REMARK, null);
    }


    // XÓA ĐÁNH GIÁ
    public void deleteRemark(int remarkId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REMARK, COLUMN_REMARK_ID + " = ?", new String[]{String.valueOf(remarkId)});
        db.close();
    }

    @SuppressLint("Range")
    public int getCustomerIDByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        int customerID = -1;

        // Truy vấn để lấy customerID từ bảng Customer dựa trên email
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_CUSTOMER_ID + " FROM " + TABLE_CUSTOMER + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                customerID = cursor.getInt(cursor.getColumnIndex(COLUMN_CUSTOMER_ID));
            }
            cursor.close();
        }

        return customerID;
    }




    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ACCOUNT, new String[]{COLUMN_EMAIL_FK},
                COLUMN_EMAIL_FK + "=?", new String[]{email},
                null, null, null);

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
    public Cursor getAllRoutes() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ROUTE;
        return db.rawQuery(query, null);
    }
    public List<BusTrip> findBusTrips(String startLocation, String endLocation, String date, int seatCount) {
        List<BusTrip> trips = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn để tìm routeIds phù hợp với startLocation và endLocation
        Cursor routeCursor = db.rawQuery("SELECT " + COLUMN_ROUTE_ID +
                        " FROM " + TABLE_ROUTE +
                        " WHERE " + COLUMN_START_LOCATION + " = ? AND " +
                        COLUMN_END_LOCATION + " = ?",
                new String[]{startLocation, endLocation});

        // Kiểm tra và lấy chỉ số cột
        int routeIdIndex = routeCursor.getColumnIndex(COLUMN_ROUTE_ID);
        if (routeIdIndex == -1) {
            throw new RuntimeException("Column " + COLUMN_ROUTE_ID + " not found in Route table");
        }

        if (routeCursor.moveToFirst()) {
            do {
                int routeId = routeCursor.getInt(routeIdIndex);

                // Truy vấn các chuyến xe tương ứng với routeId
                Cursor tripCursor = db.rawQuery("SELECT * FROM " + TABLE_BUSTRIP +
                                " WHERE " + COLUMN_ROUTE_ID_FK + " = ? AND " +
                                COLUMN_DATE + " = ? AND " +
                                COLUMN_SEAT_COUNT + " >= ?",
                        new String[]{String.valueOf(routeId), date, String.valueOf(seatCount)});

                // Kiểm tra chỉ số cột trong Cursor chuyến xe
                int idIndex = tripCursor.getColumnIndex(COLUMN_BUSTRIP_ID);
                int dateIndex = tripCursor.getColumnIndex(COLUMN_DATE);
                int timeIndex = tripCursor.getColumnIndex(COLUMN_TIME);
                int seatCountIndex = tripCursor.getColumnIndex(COLUMN_SEAT_COUNT);

                if (idIndex == -1 || dateIndex == -1 || timeIndex == -1 || seatCountIndex == -1) {
                    throw new RuntimeException("One or more columns not found in BusTrip table");
                }

                if (tripCursor.moveToFirst()) {
                    do {
                        BusTrip trip = new BusTrip(
                                tripCursor.getInt(idIndex),
                                tripCursor.getString(dateIndex),
                                tripCursor.getString(timeIndex),
                                tripCursor.getInt(seatCountIndex)
                        );
                        trips.add(trip);
                    } while (tripCursor.moveToNext());
                }
                tripCursor.close();
            } while (routeCursor.moveToNext());
        }
        routeCursor.close();
        return trips;
    }









    /*// THÊM TÀI KHOẢN
    public void addAccount(String password, String email, int permissionId, int customerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL_FK, email);
        values.put(COLUMN_PERMISSION_ID_FK, permissionId);
        values.put(COLUMN_CUSTOMER_ID_ACC_FK, customerId);

        db.insert(TABLE_ACCOUNT, null, values);
        db.close();
    }

    // SỬA TÀI KHOẢN
    public void updateAccount(int accountId, String password, String email, int permissionId, int customerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL_FK, email);
        values.put(COLUMN_PERMISSION_ID_FK, permissionId);
        values.put(COLUMN_CUSTOMER_ID_ACC_FK, customerId);

        db.update(TABLE_ACCOUNT, values, COLUMN_ACCOUNT_ID + " = ?", new String[]{String.valueOf(accountId)});
        db.close();
    }

    // XÓA TÀI KHOẢN
    public void deleteAccount(int accountId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACCOUNT, COLUMN_ACCOUNT_ID + " = ?", new String[]{String.valueOf(accountId)});
        db.close();
    }
     */


}
