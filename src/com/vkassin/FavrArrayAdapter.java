package com.vkassin;

import java.util.ArrayList;

import com.vkassin.Common.item_type;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FavrArrayAdapter extends ArrayAdapter<RSSItem> {
	private ArrayList<RSSItem> items;
	private Context ctx;
	private int resourceId;
	
	public FavrArrayAdapter(Context context, int resourceId, ArrayList<RSSItem> objects) {
		super(context, resourceId, objects);
		this.items = objects;
		this.ctx = context;
		this.resourceId = resourceId;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
    	LinearLayout layout = new LinearLayout(parent.getContext());
    	
    	LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	layout.addView(vi.inflate(resourceId, null));
    	
    	RSSItem item = getItems().get(position);
    	if (item != null) {
    		TextView title = (TextView) layout.findViewById(R.id.NewsTitleTextView);
    		TextView date = (TextView) layout.findViewById(R.id.NewsDateTextView);
    		TextView rubr = (TextView) layout.findViewById(R.id.NewsRubricTextView);
    		ImageView arrow = (ImageView) layout.findViewById(R.id.arrow);
    		
    		title.setText(item.getShortTitle());
//    		date.setText(DateFormat.format("hh:mm", item.getPubDate()));
    		date.setText("");
    		date.setVisibility(View.GONE);
    		rubr.setText(item.type == item_type.IT_QA?"":item.rubric);
    		if(item.type != item_type.IT_QA) {
    			
    			arrow.setVisibility(View.VISIBLE);
    			rubr.setVisibility(View.VISIBLE);
    		}
    		else {

    			arrow.setVisibility(View.GONE);
    			rubr.setVisibility(View.GONE);
    		}

    	}
    	
    	return layout;
    }

	public void setItems(ArrayList<RSSItem> objects) {
		//this.items.clear();
		//this.items.addAll(objects);
		
		items = objects;
	}
	
	public void addItems(ArrayList<RSSItem> objects) {
		this.items.addAll(objects);
	}

	public ArrayList<RSSItem> getItems() {
		return items;
	}
}
