package com.ieor.delight;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class FoodReviewAdapter extends ArrayAdapter<Row> {

	static class ViewHolder {
		View base;
		TextView name;
		ImageView image;
		RatingBar rating;
		EditText commentBox;
		Button commentButton;

		public ViewHolder(View base) {
			this.base = base;
		}

		public RatingBar getRatingBar() {
			if (rating == null) {
				rating = (RatingBar) base.findViewById(R.id.ratingBarFood);
			}
			return rating;
		}
	}

	private ArrayList<Row> reviews;
	private Context c;

	public FoodReviewAdapter(Context context, int textViewResourceId,
			ArrayList<Row> r) {
		super(context, textViewResourceId, r);
		this.c = context;
		this.reviews = r;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		System.out.println("getting view for row " + position);
		View v = convertView;
		
		if(position == reviews.size() - 1){
			//case for finish button
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.button_text_view, parent, false);
			TextView view = (TextView) v.findViewById(R.id.textViewSimple);
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/Oswald_Regular.otf");
			view.setTypeface(tf);
			view.setText("DONE!");
			view.setGravity(Gravity.CENTER);
			return v;
		}
		
		Row row = getItem(position);

		LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(R.layout.food_rating_cell, parent, false);
		
		TextView name = (TextView) v.findViewById(R.id.textViewFoodName);
		ImageView image = (ImageView) v.findViewById(R.id.imageViewFood);
		RatingBar rating = (RatingBar) v.findViewById(R.id.ratingBarFood);
		final TextView comment = (TextView) v.findViewById(R.id.textViewComment);
		final Button commentButton = (Button) v.findViewById(R.id.buttonComment);
		final LinearLayout commentbox = (LinearLayout) v.findViewById(R.id.commentBox);
		
		rating.setOnRatingBarChangeListener(null);
		commentButton.setOnClickListener(null);
		
		final FoodReviewCell review = reviews.get(position).review;
		if (review != null) {
			name.setText(review.getName());
			image.setBackgroundResource(review.getImage());
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/Oswald_Regular.otf");
			commentButton.setTypeface(tf);
			commentButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View promptsView = li.inflate(R.layout.comment_dialog, null);
	 
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
					alertDialogBuilder.setView(promptsView);

					final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextComment);
					
					alertDialogBuilder.setTitle("Comment")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											String input = userInput.getText().toString();
											if(input.length() != 0){
												System.out.println("cofirm comment: "+ userInput.getText());
												commentButton.setVisibility(View.GONE);
												commentbox.setVisibility(View.VISIBLE);
												comment.setVisibility(View.VISIBLE);
												comment.setText(userInput.getText());
												review.setComment(userInput.getText().toString());
											}
										}
									})
							.setNegativeButton("Cancel",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
	 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
					alertDialog.show();
				}
			});

			if (review.getComment() != null) {
				commentButton.setVisibility(View.GONE);
				commentbox.setVisibility(View.VISIBLE);
				comment.setVisibility(View.VISIBLE);
				comment.setText(review.getComment());
			}

			if (review.getRating() != 0) {
				System.out.println(review.getName() + " rating: "+ review.getRating());
				rating.setRating(Float.valueOf(review.getRating()));
			} else {
				System.out.println("restore " + review.getName()+ " rating to default");
				rating.setRating(0.0f);
			}
			rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
				public void onRatingChanged(RatingBar ratingBar,float rating, boolean fromUser) {
					System.out.println("rating changed to " + rating + " for " + review.getName());
					review.setRating(rating);
				}
			});
		}
//		} else {
//			if (row.type == row.REVIEW) {
//				System.out.println("found view holder for " + reviews.get(position).review.getName());
//				holder = (ViewHolder) v.getTag();
//				holder.rating.setOnRatingBarChangeListener(null);
//				holder.commentBox.addTextChangedListener(null);
//				holder.commentBox.setOnClickListener(null);
//				holder.commentButton.setOnClickListener(null);
//			} else if (row.type == row.FINISH) {
//				LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				v = vi.inflate(R.layout.button_text_view, parent, false);
//				TextView view = (TextView) v.findViewById(R.id.textViewSimple);
//				view.setText("Finish");
//				view.setGravity(Gravity.CENTER);
//				return view;
//			}
//		}
		
//		final ViewHolder viewHolderFinal = holder;
//		final FoodReviewCell review = reviews.get(position).review;
//		if (review != null) {
//			holder.name.setText(review.getName());
//			holder.commentBox.addTextChangedListener(new CustomTextWatcher(
//					holder.commentBox, review));
//			holder.commentBox.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					System.out.println("requesting focus on " + v.toString());
//					InputMethodManager mgr = (InputMethodManager) c
//							.getSystemService(Context.INPUT_METHOD_SERVICE);
//					v.requestFocus();
//					mgr.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
//				}
//			});
//			holder.commentButton.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					viewHolderFinal.commentButton.setVisibility(View.GONE);
//					viewHolderFinal.commentBox.setVisibility(View.VISIBLE);
//				}
//			});
//
//			if (review.getComment() != "") {
//				holder.commentButton.setVisibility(View.GONE);
//				holder.commentBox.setVisibility(View.VISIBLE);
//				holder.commentBox.setText(review.getComment());
//			}
//
//			if (review.getRating() != -1) {
//				System.out.println(review.getName() + " rating: "+ review.getRating());
//				holder.rating.setRating(Float.valueOf(review.getRating()));
//			} else {
//				System.out.println("restore " + review.getName()+ " rating to default");
//				holder.rating.setRating(0.0f);
//			}
//			holder.rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//				public void onRatingChanged(RatingBar ratingBar,float rating, boolean fromUser) {
//					System.out.println("rating changed to " + rating + " for " + review.getName());
//					review.setRating(rating);
//				}
//			});
//		}

		return v;
	}
}
