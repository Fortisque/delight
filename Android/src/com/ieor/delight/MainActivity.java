package com.ieor.delight;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import eu.livotov.zxscan.ZXScanHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener {
	
	Button scanButton;
	Button hiddenButton;
	public final static int QR_SCAN_REQUEST_CODE = 1;
	
	public String fallback = "{\"website\": \"delight-food.appspot.com\", \"business_name\": \"Eureka\", " +
			"\"receipt_key\": \"ag5zfmRlbGlnaHQtZm9vZHIUCxIHUmVjZWlwdBiAgICA2veACQw\", " +
			"\"data\": [{\"picture\": \"http://www.foodrepublic.com/sites/default/files/imagecache/enlarge/recipe/sipsnbites_chickenslider.jpg\", " +
			"\"name\": \"Fried Chicken Sliders\", \"kind_of_food\": \"Signature\", \"cost\": \"12.75\", " +
			"\"key\": \"ag5zfmRlbGlnaHQtZm9vZHIVCxIIRm9vZEl0ZW0YgICAgIDylAoM\"}, {\"picture\": " +
			"\"http://delight-food.appspot.com/static/fresno_fig_burger.png\", \"name\": \"Fresno Fig Burger\", \"kind_of_food\": " +
			"\"Burger\", \"cost\": \"9.75\", \"key\": \"ag5zfmRlbGlnaHQtZm9vZHIVCxIIRm9vZEl0ZW0YgICAgKvz3woM\"}], " +
			"\"business_key\": \"ag5zfmRlbGlnaHQtZm9vZHIVCxIIQnVzaW5lc3MYgICAgJnOggsM\"}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();
        
        scanButton = (Button)findViewById(R.id.buttonScan);
        scanButton.setOnClickListener(this);
        hiddenButton = (Button)findViewById(R.id.buttonHidden);
        hiddenButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.buttonScan){
			ZXScanHelper.scan(this, QR_SCAN_REQUEST_CODE);
//			Intent intent = new Intent(this, RestaurantHomeActivity.class);
//			startActivity(intent);
		}else if(v.getId() == R.id.buttonHidden){
			JSONObject response;
			try {
				response = new JSONObject(fallback);
				System.out.println("response: "+ response.toString());
				String businessKey = response.getString("business_key");
				String businessName = response.getString("business_name");
				String receiptKey = response.getString("receipt_key");
				JSONArray restaurantData = response.getJSONArray("data");
				restaurantData.toString();
				Intent intent = new Intent(this, RestaurantHomeActivity.class);
				intent.putExtra("businessKey", businessKey);
				intent.putExtra("businessName", businessName);
				intent.putExtra("receiptKey", receiptKey);
				intent.putExtra("data", restaurantData.toString());
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == QR_SCAN_REQUEST_CODE) {
			String scannedCode = ZXScanHelper.getScannedCode(data);
			System.out.println(scannedCode);
			if(scannedCode != ""){
				getReceiptDetail(scannedCode);
			}else{
				Toast.makeText(this, "Please Try Again.", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void getReceiptDetail(String receiptId) {
		String getReceiptUrl = "http://delight-food.appspot.com/receipt?json=true&id=" + receiptId;
		System.out.println("entire getReceiptUrl: " + getReceiptUrl);

		JsonObjectRequest req = new JsonObjectRequest(getReceiptUrl, null,
		       new Response.Listener<JSONObject>() {
		           @Override
		           public void onResponse(JSONObject response) {
		               try {
		            	   VolleyLog.v("Response:%n %s", response.toString(4));
		                   System.out.println("response: "+ response.toString());
		                   String businessKey = response.getString("business_key");
		                   String businessName = response.getString("business_name");
		                   String receiptKey = response.getString("receipt_key");
		                   JSONArray restaurantData = response.getJSONArray("data");
		                   //restaurantData.toString();
		                   openRestaurantHomeActivity(businessKey, businessName, receiptKey, restaurantData.toString());
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
	
	public void openRestaurantHomeActivity(String businessKey, String businessName, String receiptKey, String restaurantData){
		Intent intent = new Intent(this, RestaurantHomeActivity.class);
        intent.putExtra("businessKey", businessKey);
        intent.putExtra("businessName", businessName);
        intent.putExtra("receiptKey", receiptKey);
        intent.putExtra("data", restaurantData);
        startActivity(intent);
	}
}
