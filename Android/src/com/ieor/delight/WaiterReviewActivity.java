package com.ieor.delight;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class WaiterReviewActivity extends Activity implements OnClickListener {

	WaiterCell waiter;
	int waiterIndex;
	TextView waiterName;
	ImageView waiterImage;
	RatingBar rating;
	EditText commentBox;
	Button reviewAnotherWaiterButton;
	Button finishButton;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_waiter);
        createCutomActionBarTitle();
        
        Bundle b = getIntent().getExtras(); 
        waiter = b.getParcelable("Waiter");
        waiterIndex = b.getInt("position");
        System.out.println(waiter.toString());
        
        waiterName = (TextView)findViewById(R.id.textViewWaiterName);
        waiterName.setText(waiter.getName());
        waiterImage = (ImageView)findViewById(R.id.imageViewWaiter);
        waiterImage.setImageResource(waiter.getImage());
        rating = (RatingBar)findViewById(R.id.ratingBarWaiter);
        if(waiter.getRating() != 0){
        	rating.setRating(waiter.getRating());
        }
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar,float rating, boolean fromUser) {
				waiter.setRating(rating);
			}
		});
        reviewAnotherWaiterButton = (Button)findViewById(R.id.buttonAnotherWaiter);
        reviewAnotherWaiterButton.setOnClickListener(this);
        finishButton = (Button)findViewById(R.id.buttonFinish);
        finishButton.setOnClickListener(this);
        commentBox = (EditText)findViewById(R.id.editTextComment);
        if(waiter.getComment() != null) {
        	commentBox.setText(waiter.getComment());
        }
        commentBox.addTextChangedListener(new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
    			waiter.setComment(s.toString());
            }
        });
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
	public void onClick(View v) {
		if (v.getId() == R.id.buttonAnotherWaiter) {
			Intent resultIntent = new Intent();
			resultIntent.putExtra("ReviewedWaiter", waiter);
			resultIntent.putExtra("position", waiterIndex);
			resultIntent.putExtra("continue", true);
			setResult(Activity.RESULT_OK, resultIntent);
			finish();
		} else if (v.getId() == R.id.buttonFinish) {
			Intent resultIntent = new Intent();
			resultIntent.putExtra("ReviewedWaiter", waiter);
			resultIntent.putExtra("position", waiterIndex);
			resultIntent.putExtra("continue", false);
			setResult(Activity.RESULT_OK, resultIntent);
			finish();
		}
	}
	
}
