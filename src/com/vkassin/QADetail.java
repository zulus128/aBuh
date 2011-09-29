package com.vkassin;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QADetail extends Activity {
	
	private TextView qaTitle;
	private TextView qaDate;
	private LinearLayout qaContent;
	private RSSItem rssItem;
	
	// Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qadetail);
        
        rssItem = (RSSItem)this.getIntent().getExtras().get("rssitem");
        
        qaTitle = (TextView)this.findViewById(R.id.QATitle);
        qaDate = (TextView)this.findViewById(R.id.QADate);
        
        qaTitle.setText(rssItem.title);
        qaDate.setText(DateFormat.format("yyyy-MM-dd", rssItem.getPubDate()));

        qaContent = (LinearLayout)this.findViewById(R.id.QAWebLinearLayout1);
        WebView webview = new WebView(this);
        String content = "<html><head><style type=\"text/css\">body {margin: 0px} img {max-width: 100%;}" +
		"body {font-family: \"helvetica\"; font-size: 12; font-style:oblique;}\n" +
		"</style></head>" + "<body>" + rssItem.description + "</body></html>";
        
        webview.loadDataWithBaseURL(null, content, "text/html", "utf-8", "about:blank");
        qaContent.addView(webview);

        qaContent = (LinearLayout)this.findViewById(R.id.QAWebLinearLayout2);
        WebView webview1 = new WebView(this);
        content = "<html><head><style type=\"text/css\">body {margin: 0px} img {max-width: 100%;}</style></head>"
        	+ "<body>" + rssItem.fulltext + "</body></html>";
        webview1.loadDataWithBaseURL(null, content, "text/html", "utf-8", "about:blank");
        qaContent.addView(webview1);

    }
   
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.newsdetailmenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menufavr: Common.addToFavr(rssItem);
	                            break;
	        case R.id.menuemail: Common.sendMail(this, rssItem);
			break;	                            
	    }
	    return true;
	}
	
}
