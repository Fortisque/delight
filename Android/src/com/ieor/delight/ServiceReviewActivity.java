package com.ieor.delight;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ServiceReviewActivity extends Activity implements OnItemClickListener {
	
	GridView gridView;
	WaiterAdapter adapter;
	ArrayList<WaiterCell> waiters;
	public final static int REVIEW_CODE = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_reviews);
 
		gridView = (GridView) findViewById(R.id.gridViewWaiters);
		populateWaiters();
		adapter = new WaiterAdapter(this, waiters);
		gridView.setAdapter(adapter);
 
		gridView.setOnItemClickListener(this);
	}
	
	public void populateWaiters() {
		String[] names = {"Jessica", "Alice", "Nicole", "Jennifer", "Gavin", "Kevin", "David", "Lucy"};
		waiters = new ArrayList<WaiterCell>();
		for(int i = 0; i < 8; i++){
			WaiterCell waiter = new WaiterCell(i+"", names[i], "imageurl");
			waiters.add(waiter);
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		WaiterCell clickedWaiter = waiters.get(position);
		System.out.println(clickedWaiter.getName() + " clicked");
		Intent intent = new Intent(this, WaiterReviewActivity.class);
		intent.putExtra("Waiter", clickedWaiter);
		intent.putExtra("position", position);
		startActivityForResult(intent, REVIEW_CODE);
	}
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case (REVIEW_CODE): {
				if (resultCode == Activity.RESULT_OK) {
			        WaiterCell waiter = data.getParcelableExtra("ReviewedWaiter");
			        int position = data.getIntExtra("position", -1);
			        if(position != -1){
			        	waiters.set(position, waiter);
			        }
					System.out.println(waiter.toString());
					boolean cont = data.getBooleanExtra("continue", true);
					if(!cont){
						finish();
					}
				}
				break;
			}
		}
	}

}
