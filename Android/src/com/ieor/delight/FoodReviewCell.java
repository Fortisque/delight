package com.ieor.delight;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class FoodReviewCell implements Parcelable {
	
	private String key;
	private String name;
	private String imageUrl;
	private float rating;
	private String comment;
	
	public FoodReviewCell(String key, String name, String imageUrl){
		this.key = key;
		this.name = name;
		this.imageUrl = imageUrl;
	}
	
	public FoodReviewCell(Parcel in) {
		this.key = in.readString();
		this.name = in.readString();
		this.imageUrl = in.readString();
		this.comment = in.readString();
		this.rating = in.readFloat();
	} 
	
	public String getKey(){
		return key;
	}
	
	public String getName(){
		return name;
	}
	
	public String getImageUrl(){
		return imageUrl;
	}
	
	public float getRating(){
		return rating;
	}
	
	public String getComment(){
		return comment;
	}
	
	public void setRating(float newRating){
		rating = newRating;
	}
	
	public void setComment(String newComment){
		comment = newComment;
	}

	public static final Parcelable.Creator<FoodReviewCell> CREATOR = new Creator<FoodReviewCell>() {
		public FoodReviewCell createFromParcel(Parcel source) {
			FoodReviewCell food = new FoodReviewCell(source);
			return food;
		}

		public FoodReviewCell[] newArray(int size) {
			return new FoodReviewCell[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(key);
		parcel.writeString(name);
		parcel.writeString(imageUrl);
		parcel.writeString(comment);
		parcel.writeFloat(rating);
	}

	@Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("***** Food Details *****\n");
        sb.append("Key="+getKey()+"\n");
        sb.append("Name="+getName()+"\n");
        sb.append("Image="+getImageUrl()+"\n");
        sb.append("Comment="+getComment()+"\n");
        sb.append("Rating="+getRating()+"\n");
        sb.append("*****************************\n");
        return sb.toString();
    }
}
