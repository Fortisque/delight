package com.ieor.delight;

import com.ieor.delight.FoodReviewAdapter.ViewHolder;

public class Row {

	public static final int REVIEW = 0;
	public static final int FINISH = 1;

	public final int type;
	
	public FoodReviewCell review;

	public Row(int type, FoodReviewCell r) {
		this.type = type;
		this.review = r;
	}
}
