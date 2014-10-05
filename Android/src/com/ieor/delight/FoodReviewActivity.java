package com.ieor.delight;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class FoodReviewActivity extends ListActivity implements OnClickListener {
	
	Button nextButton;
	ArrayList<Row> reviews;
	FoodReviewAdapter adapter;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_reviews);
        populateReviews();
        adapter = new FoodReviewAdapter(this, R.layout.food_rating_cell, reviews);
		adapter.notifyDataSetChanged();
		setListAdapter(adapter);
    }

	@Override
	public void onClick(View v) {
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		System.out.println("onListItemClick " + position);
		if (position == reviews.size() - 1) {
			Intent intent = new Intent(this, RestaurantHomeActivity.class);
			startActivity(intent);
			saveReviews();
			finish();
		}
	}
	
	public void populateReviews(){
		//make get request
		//mock data for now
		reviews = new ArrayList<Row>();
		for(int i = 1; i <= 12; i++){
			FoodReviewCell food  = new FoodReviewCell(i, "Food " + i, "image" + i);
			Row row = new Row(Row.REVIEW, food);
			reviews.add(row);
		}
		reviews.add(new Row(Row.FINISH, null));
	}
	
	public void saveReviews(){
		//save reviews for post request
	}
}
