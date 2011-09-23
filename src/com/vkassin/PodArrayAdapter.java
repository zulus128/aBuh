package com.vkassin;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PodArrayAdapter extends ArrayAdapter<RSSItem> {
	private ArrayList<RSSItem> items;
	private Context ctx;
	private int resourceId;
	private LinearLayout qaContent;
	
	public PodArrayAdapter(Context context, int resourceId, ArrayList<RSSItem> objects) {
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
    		TextView title = (TextView) layout.findViewById(R.id.PodTitleTextView);
//    		TextView date = (TextView) layout.findViewById(R.id.QADateTextView);
    		TextView full = (TextView) layout.findViewById(R.id.PodFullTextView);
    		
    		title.setText(item.getShortTitle());
//    		title.setText(item.title);
    		//date.setText(DateFormat.format("MM:dd", item.getPubDate()));
    		
    		Spanned ss = Html.fromHtml(item.getShortContent());
    		full.setText(ss);

/*            qaContent = (LinearLayout)layout.findViewById(R.id.QAFullLinearLayout);

    		WebView webview = new WebView(this.ctx);
            String content = "<html><head><style type=\"text/css\">body {margin: 0px} img {max-width: 100%;}" +
            		"body {font-family: \"helvetica\"; font-size: 12; font-style:oblique;}\n" +
            		"</style></head>"
            	+ "<body>" + item.getShortContent() + "</body></html>";
            webview.loadDataWithBaseURL(null, content, "text/html", "utf-8", "about:blank");
            webview.setFocusable(false);
            webview.setFocusableInTouchMode(false);
            //webview.setClickable(false);
            qaContent.addView(webview);
  */		
    	}
    	
    	return layout;
    }

	public void setItems(ArrayList<RSSItem> objects) {
		this.items.clear();
		this.items.addAll(objects);
	}
	
	public void addItems(ArrayList<RSSItem> objects) {
		this.items.addAll(objects);
	}

	public ArrayList<RSSItem> getItems() {
		return items;
	}
}
