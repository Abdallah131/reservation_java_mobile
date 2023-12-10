package com.example.haider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView flightsRecycler;
    private FlightsAdapter flightsAdapter = null;
    private SQLiteHelper sqlliteHelper;
    private ImageView showAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showAddButton = findViewById(R.id.showaddimage);
        showAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddNote();
            }
        });

        sqlliteHelper = new SQLiteHelper(this);
        flightsRecycler = findViewById(R.id.flightsRecycler);
        flightsAdapter = new FlightsAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        flightsRecycler.setLayoutManager(layoutManager);
        flightsRecycler.setAdapter(flightsAdapter);

        getNotes();


    }
    private String formatDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private void getNotes() {
        ArrayList<flightModel> flightsList = sqlliteHelper.getAllFlights();
        if (flightsAdapter != null) {
            flightsAdapter.addUser(flightsList);
        }
    }

    private void showAddNote() {
        DialogPlus dialogBuilder = DialogPlus.newDialog(this)
                .setGravity(Gravity.BOTTOM)
                .setContentHolder(new ViewHolder(R.layout.add_flight))
                .setCancelable(true)
                .create();

        View dialogView = dialogBuilder.getHolderView();
        EditText flightName = dialogView.findViewById(R.id.addflightName);
        EditText flightPassengers = dialogView.findViewById(R.id.addflightPassengers);
        EditText depaturePlace = dialogView.findViewById(R.id.adddeparturePlace);
        EditText ArrivalPlace = dialogView.findViewById(R.id.addarrivalPlace);
        EditText flightPrice = dialogView.findViewById(R.id.addflightPrice);
        Button addflight = dialogView.findViewById(R.id.noteaddflight);
        Button DepatureDate = dialogView.findViewById(R.id.DepatureDate);

        DepatureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                String formattedDate = formatDate(selectedYear, selectedMonth, selectedDay);
                                DepatureDate.setText(formattedDate);
                            }
                        },
                        year,
                        month,
                        dayOfMonth
                );
                datePickerDialog.show();
            }
        });

        addflight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String flightname = flightName.getText().toString();
                String flightpassengersStr = flightPassengers.getText().toString();
                String flightdeparture = depaturePlace.getText().toString();
                String flightarrival = ArrivalPlace.getText().toString();
                String flightpriceStr = flightPrice.getText().toString();
                String departuredate = DepatureDate.getText().toString();
                if (flightname.isEmpty() || flightpassengersStr.isEmpty() || flightarrival.isEmpty() || flightpriceStr.isEmpty() ||  departuredate.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    int flightpassengers = Integer.parseInt(flightpassengersStr);
                    int flightprice = Integer.parseInt(flightpriceStr);
                    flightModel flight = new flightModel();
                    flight.setFlightName(flightname);
                    flight.setFlightPassengers(flightpassengers);
                    flight.setFlightDepature(flightdeparture);
                    flight.setFlightArrival(flightarrival);
                    flight.setFlightPrice(flightprice);
                    flight.setFlightDepature(departuredate);

                    int status = (int) sqlliteHelper.insertFlight(flight);

                    if (status > -1) {
                        Toast.makeText(MainActivity.this, "Flight Added successfully", Toast.LENGTH_SHORT).show();
                        dialogBuilder.dismiss();
                        getNotes();
                    } else {
                        Toast.makeText(MainActivity.this, "Record not saved", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        dialogBuilder.show();
    }

}