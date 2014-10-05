package com.ieor.delight;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RestaurantHomeActivity extends Activity implements OnClickListener {
	
	Button reviewFoodButton;
	Button reviewServiceButton;
	TextView businessTitle;
	
	String businessName;
	String businessKey;
	String receiptKey;
	String jsonData;
	
	ArrayList<FoodReviewCell> foods;
	
	public final static int FOOD_REVIEW_CODE = 1;
	
	public String postUrl = "http://delight-food.appspot.com/batch_review";

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_home);
        Bundle b = getIntent().getExtras(); 
       	businessName = b.getString("businessName");
       	businessKey = b.getString("businessKey");
       	receiptKey = b.getString("receiptKey");
       	jsonData = b.getString("data");
       	System.out.println("data: " + jsonData);
       	sortData(jsonData);
       	
        businessTitle = (TextView)findViewById(R.id.textViewRestaurantName);
        businessTitle.setText(businessName);
        reviewFoodButton = (Button)findViewById(R.id.buttonFoodReview);
        reviewFoodButton.setOnClickListener(this);
        reviewServiceButton = (Button)findViewById(R.id.buttonServiceReview);
        reviewServiceButton.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonFoodReview) {
			Intent intent = new Intent(this, FoodReviewActivity.class);
			intent.putParcelableArrayListExtra("foods", foods);
			startActivityForResult(intent, FOOD_REVIEW_CODE);
		} else if (v.getId() == R.id.buttonServiceReview) {
			Intent intent = new Intent(this, ServiceReviewActivity.class);
			startActivity(intent);
		}
	}

	public void sortData(String data){
		foods = new ArrayList<FoodReviewCell>();
		try {
			JSONArray array = new JSONArray(data);
			for (int i = 0; i < array.length(); i++) {
				JSONObject foodJson = array.getJSONObject(i);
				String key = foodJson.getString("key");
				String name = foodJson.getString("name");
				String picture = foodJson.getString("picture");
				FoodReviewCell food = new FoodReviewCell(key, name, picture);
				foods.add(food);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case (FOOD_REVIEW_CODE): {
				if (resultCode == Activity.RESULT_OK) {
					foods = data.getParcelableArrayListExtra("foods");
					System.out.println("after review");
					for(FoodReviewCell food: foods){
						System.out.println(food);
					}
				}
				break;
			}
		}
	}
}
