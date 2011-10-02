package com.vkassin.abuh;

import com.vkassin.abuh.Common.item_type;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FavrDetail extends Activity {
	
	private TextView qaTitle;
	private TextView qaDate;
	private LinearLayout qaContent;
	LinearLayout newsContent;
	
	private RSSItem rssItem;
	
	private static int fontsize = 12;
	private WebView webview, webview1;

	private boolean isNews;

	private void createNews() {
      
		TextView newsTitle = (TextView)this.findViewById(R.id.NewsTitle);
		TextView newsDate = (TextView)this.findViewById(R.id.NewsDate);
		newsContent = (LinearLayout)this.findViewById(R.id.NewsWebLinearLayout);
  		newsTitle.setText(rssItem.title);
  		newsDate.setText(DateFormat.format("yyyy-MM-dd", rssItem.getPubDate()));
  		webview1 = new WebView(this);
        newsContent.addView(webview1);	
	}

	private void createQA() {

		qaTitle = (TextView)this.findViewById(R.id.QATitle);
        qaDate = (TextView)this.findViewById(R.id.QADate);
        qaTitle.setText(rssItem.title);
        qaDate.setText(DateFormat.format("yyyy-MM-dd", rssItem.getPubDate()));
        qaContent = (LinearLayout)this.findViewById(R.id.QAWebLinearLayout1);
        webview = new WebView(this);
        qaContent.addView(webview);
        qaContent = (LinearLayout)this.findViewById(R.id.QAWebLinearLayout2);
        webview1 = new WebView(this);
        qaContent.addView(webview1);
		
	}
	// Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        rssItem = (RSSItem)this.getIntent().getExtras().get("rssitem");
        isNews = (rssItem.type != item_type.IT_QA);
        setContentView(isNews?R.layout.favnewsdetail : R.layout.favqadetail);
        
        if(isNews)
        	createNews();
        else
        	createQA();
        
        refresh();
    }
   
    private void refresh() {
    	
//      String content = "<html><head><style type=\"text/css\">body {margin: 0px} img {max-width: 100%;}</style></head>"
//    	+ "<body>" + rssItem.fulltext + "</body></html>";
    String content;
    
    if(!isNews) {
    content = "<html><head><style type=\"text/css\">body {margin: 0px} img {max-width: 100%;}" +
	"body {font-family: \"helvetica\"; font-size: " + fontsize + ";font-style:oblique;}\n" +
	"</style></head>" + "<body>" + rssItem.description + "</body></html>";
    webview.loadDataWithBaseURL(null, content, "text/html", "utf-8", "about:blank");
    }

    content = "<html><head><style type=\"text/css\">body {margin: 0px} img {max-width: 100%;}" +
	"body {font-family: \"helvetica\"; font-size: " + (fontsize + 4) + ";}\n" +
	"</style></head>" + "<body>" + rssItem.fulltext + "</body></html>";
    webview1.loadDataWithBaseURL(null, content, "text/html", "utf-8", "about:blank");

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
        case R.id.menufavr: { 
        	
			Toast.makeText(this, R.string.alreadyfavr, Toast.LENGTH_SHORT).show();

        	
                            break;
        }
	        case R.id.menushare: Common.sendMail(this, rssItem);
								break;
	        case R.id.menudecr: fontsize = (fontsize > 8)?fontsize - 2:fontsize; refresh();
								break;
	        case R.id.menuincr: fontsize = (fontsize < 30)?fontsize + 2:fontsize; refresh();
								break;
					
			
	    }
	    return true;
	}
	
}
