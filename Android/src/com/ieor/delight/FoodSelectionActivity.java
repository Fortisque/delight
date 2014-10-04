package com.ieor.delight;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FoodSelectionActivity extends ListActivity implements OnClickListener {
	
	Button rateButton;
	ArrayList<FoodReviewCell> events = new ArrayList<FoodReviewCell>();
	FoodReviewAdapter adapter;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_selection);
        rateButton = (Button)findViewById(R.id.buttonRate);
        rateButton.setOnClickListener(this);
        adapter = new FoodReviewAdapter(this, R.layout.food_rating_cell, events);
		adapter.notifyDataSetChanged();
		setListAdapter(adapter);
    }

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonRate) {
			Intent intent = new Intent(this, FoodSelectionActivity.class);
			startActivity(intent);
		}
	}
}
