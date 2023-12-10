package com.example.haider;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {
    // Database info
    private static final String DB_NAME = "Flights";
    private static final int DB_VERSION = 1;
    // Table info
    private static final String TABLE_NAME = "FlightsList";
    // Columns info
    private static final String flightId = "flightId";
    private static final String flightName = "flightName";
    private static final String flightPassengers = "flightPassengers";
    private static final String flightDeparture = "flightDeparture";
    private static final String flightArrival = "flightArrival";
    private static final String flightPrice = "flightPrice";
    private static final String flightDepartureDate = "flightDepartureDate";



    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Query for creating the table
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                flightId + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                flightName + " TEXT," +
                flightPassengers + " INT," +
                flightDeparture + " TEXT," +
                flightArrival + " TEXT," +
                flightPrice + " INT," +
                flightDepartureDate + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If the table is upgraded, drop it and recreate it with new upgrades (info)
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public long insertFlight(flightModel flight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(flightName, flight.getFlightName());
        values.put(flightPassengers, flight.getFlightPassengers());
        values.put(flightDeparture, flight.getFlightDepature());
        values.put(flightArrival, flight.getFlightArrival());
        values.put(flightPrice, flight.getFlightPrice());
        values.put(flightDepartureDate, flight.getDepartueDate());

        long inserted = db.insert(TABLE_NAME, null, values);
        db.close();
        return inserted;
    }

    public boolean deleteFlight(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int success = db.delete(TABLE_NAME, flightId + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return success != -1;
    }

    @SuppressLint("Range")
    public ArrayList<flightModel> getAllFlights() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<flightModel> flightsList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor;

        try {
            cursor = db.rawQuery(query, null);
        } catch (Exception e) {
            e.printStackTrace();
            db.execSQL(query);
            return new ArrayList<>();
        }

        if (cursor.moveToFirst()) {
            do {
                flightModel flight = new flightModel();
                flight.setFlightId(cursor.getInt(cursor.getColumnIndex(flightId)));
                flight.setFlightName(cursor.getString(cursor.getColumnIndex(flightName)));
                flight.setFlightPassengers(cursor.getInt(cursor.getColumnIndex(flightPassengers)));
                flight.setFlightDepature(cursor.getString(cursor.getColumnIndex(flightDeparture)));
                flight.setFlightArrival(cursor.getString(cursor.getColumnIndex(flightArrival)));
                flight.setFlightPrice(cursor.getInt(cursor.getColumnIndex(flightPrice)));
                flight.setDepartueDate(cursor.getString(cursor.getColumnIndex(flightDepartureDate)));

                flightsList.add(flight);
            } while (cursor.moveToNext());
        }

        return flightsList;
    }
}
