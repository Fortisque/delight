package com.ieor.delight;

import java.util.ArrayList;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
			view.setText("Finish");
			view.setGravity(Gravity.CENTER);
			return view;
		}
		
		Row row = getItem(position);

			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.food_rating_cell, parent, false);
//				System.out.println("creating new view holder for "
//						+ reviews.get(position).review.getName());
//				LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				v = vi.inflate(R.layout.food_rating_cell, parent, false);
//				holder = new ViewHolder(v);
//				holder.name = (TextView) v.findViewById(R.id.textViewFoodName);
//				holder.image = (ImageView) v.findViewById(R.id.imageViewFood);
//				holder.rating = (RatingBar) v.findViewById(R.id.ratingBarFood);
//				holder.commentBox = (EditText) v.findViewById(R.id.editTextComment);
//				holder.commentButton = (Button) v.findViewById(R.id.buttonComment);
//				v.setTag(holder);
		
		TextView name = (TextView) v.findViewById(R.id.textViewFoodName);
		ImageView image = (ImageView) v.findViewById(R.id.imageViewFood);
		RatingBar rating = (RatingBar) v.findViewById(R.id.ratingBarFood);
		final EditText commentBox = (EditText) v.findViewById(R.id.editTextComment);
		final Button commentButton = (Button) v.findViewById(R.id.buttonComment);
		
		rating.setOnRatingBarChangeListener(null);
		commentBox.addTextChangedListener(null);
		commentBox.setOnClickListener(null);
		commentButton.setOnClickListener(null);
		
		final FoodReviewCell review = reviews.get(position).review;
		if (review != null) {
			name.setText(review.getName());
			commentBox.addTextChangedListener(new CustomTextWatcher(commentBox, review));
			commentBox.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					System.out.println("requesting focus on " + v.toString());
					InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
					v.requestFocus();
					mgr.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
				}
			});
			commentButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					commentButton.setVisibility(View.GONE);
					commentBox.setVisibility(View.VISIBLE);
				}
			});

			if (review.getComment() != "") {
				commentButton.setVisibility(View.GONE);
				commentBox.setVisibility(View.VISIBLE);
				commentBox.setText(review.getComment());
			}

			if (review.getRating() != -1) {
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

	private class CustomTextWatcher implements TextWatcher {

		private View view;
		private FoodReviewCell review;

		private CustomTextWatcher(View view, FoodReviewCell review) {
			this.view = view;
			this.review = review;
		}

		public void beforeTextChanged(CharSequence charSequence, int i, int i1,
				int i2) {
		}

		public void onTextChanged(CharSequence charSequence, int i, int i1,
				int i2) {
		}

		public void afterTextChanged(Editable editable) {
			String text = editable.toString();
			review.setComment(text);
		}
	}
}
