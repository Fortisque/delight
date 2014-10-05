package com.ieor.delight;

public class FoodReviewCell {
	
	private int id;
	private String name;
	private String imageUrl;
	private float rating;
	private String comment;
	
	public FoodReviewCell(int id, String name, String imageUrl){
		this.id = id;
		this.name = name;
		this.imageUrl = imageUrl;
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
}
