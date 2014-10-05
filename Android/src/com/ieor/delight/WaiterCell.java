package com.ieor.delight;

import android.os.Parcel;
import android.os.Parcelable;

public class WaiterCell implements Parcelable {

	private String key;
	private String name;
	private String imageUrl; //use resource path for mock
	private String comment;
	private float rating;
	
	public WaiterCell(String key, String name, String image){
		this.key = key;
		this.name = name;
		this.imageUrl = image;
	}
	
	public WaiterCell(Parcel in) {
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
	
	public String getImage(){
		return imageUrl;
	}
	
	public String getComment(){
		return comment;
	}
	
	public float getRating(){
		return rating;
	}
	
	public void setRating(float newRating){
		rating = newRating;
	}
	
	public void setComment(String newComment){
		comment = newComment;
	}

	public static final Parcelable.Creator<WaiterCell> CREATOR = new Creator<WaiterCell>() {
		public WaiterCell createFromParcel(Parcel source) {
			WaiterCell waiter = new WaiterCell(source);
			return waiter;
		}

		public WaiterCell[] newArray(int size) {
			return new WaiterCell[size];
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
        sb.append("***** Waiter Details *****\n");
        sb.append("Key="+getKey()+"\n");
        sb.append("Name="+getName()+"\n");
        sb.append("Image="+getImage()+"\n");
        sb.append("Comment="+getComment()+"\n");
        sb.append("Rating="+getRating()+"\n");
        sb.append("*****************************\n");
        return sb.toString();
    }
	
	
}
