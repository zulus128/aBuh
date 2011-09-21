package com.vkassin;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QADetail extends Activity {
	
	private TextView qaTitle;
	private TextView qaDate;
	private LinearLayout qaContent;
	
	// Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qadetail);
        
        RSSItem rssItem = (RSSItem)this.getIntent().getExtras().get("rssitem");
        
        qaTitle = (TextView)this.findViewById(R.id.QATitle);
        qaDate = (TextView)this.findViewById(R.id.QADate);
        qaContent = (LinearLayout)this.findViewById(R.id.QAWebLinearLayout);
        
        qaTitle.setText(rssItem.title);
        qaDate.setText(DateFormat.format("yyyy-MM-dd", rssItem.getPubDate()));

        WebView webview = new WebView(this);
        String content = "<html><head><style type=\"text/css\">body {margin: 0px} img {max-width: 100%;}</style></head>"
        	+ "<body>" + rssItem.fulltext + "</body></html>";
        webview.loadDataWithBaseURL(null, content, "text/html", "utf-8", "about:blank");
        
        qaContent.addView(webview);
        
    }
    
}
