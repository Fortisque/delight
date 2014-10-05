package com.ieor.delight;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WaiterAdapter extends BaseAdapter {
	private Context context;
	private final ArrayList<WaiterCell> waiters;
 
	public WaiterAdapter(Context context, ArrayList<WaiterCell> waiters) {
		this.context = context;
		this.waiters = waiters;
	}
 
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridView;
		if (convertView == null) {
			gridView = new View(context);
			gridView = inflater.inflate(R.layout.waiter_cell, null);
		} else {
			gridView = (View) convertView;
		}
		TextView textView = (TextView) gridView.findViewById(R.id.textViewWaiterName);
		textView.setText(waiters.get(position).getName());
		ImageView imageView = (ImageView) gridView.findViewById(R.id.imageViewWaiter);
		int imagePath = waiters.get(position).getImage(); //used later with image url
		imageView.setImageResource(imagePath);
		return gridView;
	}
 
	@Override
	public int getCount() {
		return waiters.size();
	}
 
	@Override
	public Object getItem(int position) {
		return waiters.get(position);
	}
 
	@Override
	public long getItemId(int position) {
		return 0;
	}
 
}
