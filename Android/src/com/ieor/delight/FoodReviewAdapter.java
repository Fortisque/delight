package com.ieor.delight;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class FoodReviewAdapter extends ArrayAdapter<FoodReviewCell> {

	private ArrayList<FoodReviewCell> reviews;

    public FoodReviewAdapter(Context context, int textViewResourceId, ArrayList<FoodReviewCell> r) {
            super(context, textViewResourceId, r);
            this.reviews = r;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.food_rating_cell, null);
            }
            FoodReviewCell review = reviews.get(position);
            if (review != null) {
//            	 TextView eventName = (TextView) v.findViewById(R.id.tvEventName);
//                    TextView day = (TextView) v.findViewById(R.id.tvDayOfTheWeek);
//                    TextView date = (TextView) v.findViewById(R.id.tvDate);
//                    TextView time = (TextView) v.findViewById(R.id.tvTime);
//                    TextView location = (TextView) v.findViewById(R.id.tvLocation);
//                    if (eventName != null) {
//                    	eventName.setText("");
//                    }
//                    if(day != null){
//                        day.setText("");
//                    }
//                    if(date != null){
//                        date.setText(e.getDate());
//                    }
//                    if(time != null){
//                    	time.setText("Time: " + e.getTime());
//                    }
//                    if(location != null){
//                        location.setText("Location: " + e.getLocation());
//                    }      
            }
            return v;
    }
}

