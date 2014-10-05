package com.ieor.delight;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FoodReviewActivity extends ListActivity {
	
	Button nextButton;
	ArrayList<FoodReviewCell> foods;
	ArrayList<Row> reviews;
	FoodReviewAdapter adapter;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_reviews);
        createCutomActionBarTitle();
        foods = getIntent().getParcelableArrayListExtra("foods");
        populateReviews();
        adapter = new FoodReviewAdapter(this, R.layout.food_rating_cell, reviews);
		adapter.notifyDataSetChanged();
		setListAdapter(adapter);
    }
	
	private void createCutomActionBarTitle(){
    	ActionBar actionBar = getActionBar();
    	actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    	actionBar.setDisplayShowHomeEnabled(false); 
    	actionBar.setDisplayShowCustomEnabled(true); 
    	actionBar.setDisplayShowTitleEnabled(false);
	    LayoutInflater inflator = LayoutInflater.from(this);
	    View v = inflator.inflate(R.layout.custom_action_bar, null);
	    Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Wisdom_Script.otf");
	    TextView title = (TextView)v.findViewById(R.id.textViewTitle);
	    title.setText("Delight");
	    title.setTypeface(tf);
	    
	    //assign the view to the actionbar
	    LayoutParams layout = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	    actionBar.setCustomView(v, layout);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		System.out.println("onListItemClick " + position);
		if (position == reviews.size() - 1) {
			saveReviews();
			Intent resultIntent = new Intent();
			resultIntent.putParcelableArrayListExtra("foods", foods);
			setResult(Activity.RESULT_OK, resultIntent);
			finish();
		}
	}
	
	public void populateReviews(){
		//use data from qr code
		reviews = new ArrayList<Row>();
		for(FoodReviewCell food : foods){
			Row row = new Row(Row.REVIEW, food);
			reviews.add(row);
		}
		reviews.add(new Row(Row.FINISH, null));
	}
	
	public void saveReviews(){
		//save reviews for post request
		for(int i = 0; i < reviews.size() - 1; i++){
			foods.set(i, reviews.get(i).review);
		}
	}
}
