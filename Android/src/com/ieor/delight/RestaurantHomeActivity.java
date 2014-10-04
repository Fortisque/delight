package com.ieor.delight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RestaurantHomeActivity extends Activity implements OnClickListener {
	
	Button reviewFoodButton;
	Button reviewServiceButton;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_home);
        reviewFoodButton = (Button)findViewById(R.id.buttonFoodReview);
        reviewFoodButton.setOnClickListener(this);
        reviewServiceButton = (Button)findViewById(R.id.buttonServiceReview);
        reviewServiceButton.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonFoodReview) {
			Intent intent = new Intent(this, FoodReviewActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.buttonServiceReview) {
			Intent intent = new Intent(this, FoodReviewActivity.class);
			startActivity(intent);
		}
	}
}
