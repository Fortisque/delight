package com.ieor.delight;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ServiceReviewActivity extends Activity implements OnItemClickListener {
	
	GridView gridView;
	TextView text;
	WaiterAdapter adapter;
	ArrayList<WaiterCell> waiters;
	public final static int REVIEW_CODE = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_reviews);
		createCutomActionBarTitle();
		text = (TextView) findViewById(R.id.textViewServer);
		Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Oswald_Regular.otf");
		text.setTypeface(tf);
		gridView = (GridView) findViewById(R.id.gridViewWaiters);
		populateWaiters();
		adapter = new WaiterAdapter(this, waiters);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
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
	
	public void populateWaiters() {
		String[] names = {"Alice", "Nicole", "Kevin", "Jennifer", "Gavin"};
		int[] resources = {R.drawable.head1, R.drawable.head2, R.drawable.head3, R.drawable.head4, R.drawable.head5 };
		waiters = new ArrayList<WaiterCell>();
		for(int i = 0; i < 5; i++){
			WaiterCell waiter = new WaiterCell(i+"", names[i], resources[i]);
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
						sendResultBack();
					}
				}
				break;
			}
		}
	}
	
	public void sendResultBack(){
		Intent resultIntent = new Intent();
		resultIntent.putParcelableArrayListExtra("waiters", waiters);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}

}
