package com.vkassin;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewsDetail extends Activity {
	
	private TextView newsTitle;
	private TextView newsDate;
	private LinearLayout newsContent;
	
	// Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsdetail);
        
        RSSItem rssItem = (RSSItem)this.getIntent().getExtras().get("rssitem");
        
        newsTitle = (TextView)this.findViewById(R.id.NewsTitle);
        newsDate = (TextView)this.findViewById(R.id.NewsDate);
        newsContent = (LinearLayout)this.findViewById(R.id.NewsWebLinearLayout);
        
        newsTitle.setText(rssItem.title);
        newsDate.setText(DateFormat.format("yyyy-MM-dd", rssItem.getPubDate()));

        WebView webview = new WebView(this);
        String content = "<html><head><style type=\"text/css\">body {margin: 0px} img {max-width: 100%;}</style></head>"
        	+ "<body>" + rssItem.fulltext + "</body></html>";
        webview.loadDataWithBaseURL(null, content, "text/html", "utf-8", "about:blank");
        
        newsContent.addView(webview);
        
    }
    
}