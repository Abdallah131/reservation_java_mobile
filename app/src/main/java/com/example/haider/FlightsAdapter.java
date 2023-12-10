package com.example.haider;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class FlightsAdapter extends RecyclerView.Adapter<FlightsAdapter.NotesViewHolder> {
    private SQLiteHelper sqlliteHelper;
    private ArrayList<flightModel> flightsList = new ArrayList<>();

    public void addUser(ArrayList<flightModel> noteItem) {
        this.flightsList = noteItem;
        notifyDataSetChanged();
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View noteItem = layoutInflater.inflate(R.layout.flight_item, parent, false);
        return new NotesViewHolder(noteItem);
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, @SuppressLint("RecyclerView") int position) {
        flightModel flight = flightsList.get(position);
        holder.bind(flight);
        sqlliteHelper = new SQLiteHelper(holder.itemView.getContext());

        holder.delbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status = sqlliteHelper.deleteFlight(flight.getFlightId());
                if (status) {
                    sqlliteHelper.deleteFlight(flight.getFlightId());
                    Toast.makeText(holder.itemView.getContext(), "Flight Deleted", Toast.LENGTH_SHORT).show();
                    flightsList.remove(flight);
                    notifyItemRemoved(position);
                    sqlliteHelper.getAllFlights();
                } else {
                    Toast.makeText(holder.itemView.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return flightsList.size();
    }

    class NotesViewHolder extends RecyclerView.ViewHolder {
        TextView flightid;
        TextView flightname;
        TextView flightpassengers;
        TextView flightdepartue;
        TextView flightarrival;
        TextView flightprice;
        TextView flightdeparturedate;
        ImageView delbutton;

        public NotesViewHolder(View itemView) {
            super(itemView);

            flightid = itemView.findViewById(R.id.flightid);
            flightname = itemView.findViewById(R.id.flightname);
            flightpassengers = itemView.findViewById(R.id.numberofpassengers);
            flightdepartue = itemView.findViewById(R.id.depatureplace);
            flightarrival = itemView.findViewById(R.id.arrivalplace);
            flightprice = itemView.findViewById(R.id.flightprice);
            flightdeparturedate = itemView.findViewById(R.id.depaturedate);
            delbutton = itemView.findViewById(R.id.flightDelbutton);
        }

        public void bind(flightModel flight) {
            flightid.setText(String.valueOf(flight.getFlightId()));
            flightname.setText(flight.getFlightName());
            flightpassengers.setText(String.valueOf(flight.getFlightPassengers()));
            flightdepartue.setText(flight.getFlightDepature());
            flightarrival.setText(flight.getFlightArrival());
            flightprice.setText(String.valueOf(flight.getFlightPrice()));
            flightdeparturedate.setText(String.valueOf(flight.getDepartueDate()));
        }


    }
}
