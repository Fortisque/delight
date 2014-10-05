package com.ieor.delight;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

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
	
	public String postUrl = "http://delight-food.appspot.com/batch_reviews";

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_home);
        System.out.println("restaurant home oncreate");
        Bundle b = getIntent().getExtras(); 
       	businessName = b.getString("businessName");
       	businessKey = b.getString("businessKey");
       	receiptKey = b.getString("receiptKey");
       	jsonData = b.getString("data");
       	System.out.println("data: " + jsonData);
       	System.out.println("businessKey: " + businessKey);
       	System.out.println("receiptKey: " + receiptKey);
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
	public void onActivityResult(int requestCode, int resultCode, Intent response) {
		super.onActivityResult(requestCode, resultCode, response);
		switch (requestCode) {
			case (FOOD_REVIEW_CODE): {
				if (resultCode == Activity.RESULT_OK) {
					foods = response.getParcelableArrayListExtra("foods");
					System.out.println("after review");
					
					// Post params to be sent to the server
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("business_key", businessKey);
					params.put("receipt_key", receiptKey);
					JSONArray data = new JSONArray();
					for(FoodReviewCell food : foods){
						System.out.println(food);
						JSONObject foodJson = new JSONObject();
						try {
							foodJson.put("stars", food.getRating());
							foodJson.put("comment", food.getComment());
							foodJson.put("target", food.getKey());
							foodJson.put("kind", "food");
							data.put(foodJson);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					params.put("data", data.toString());
					System.out.println("data for post: " + data.toString());
					System.out.println("entire json param: "+ new JSONObject(params).toString());

					JsonObjectRequest req = new JsonObjectRequest(postUrl, new JSONObject(params),
					       new Response.Listener<JSONObject>() {
					           @Override
					           public void onResponse(JSONObject response) {
					               try {
					                   VolleyLog.v("Response:%n %s", response.toString(4));
					               } catch (JSONException e) {
					                   e.printStackTrace();
					               }
					           }
					       }, new Response.ErrorListener() {
					           @Override
					           public void onErrorResponse(VolleyError error) {
					               VolleyLog.e("Error: ", error.getMessage());
					           }
					       });

					// add the request object to the queue to be executed
					DelightApplication.getInstance().addToRequestQueue(req);
				}
				break;
			}
		}
	}
}
