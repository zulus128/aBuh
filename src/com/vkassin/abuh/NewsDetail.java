package com.vkassin.abuh;

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

public class NewsDetail extends Activity {
	
	private TextView newsTitle;
	private TextView newsDate;
	private LinearLayout newsContent;
	private RSSItem rssItem;
	
	private static int fontsize = 16;
	private WebView webview;
	
	// Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsdetail);
        
        rssItem = (RSSItem)this.getIntent().getExtras().get("rssitem");
        
        newsTitle = (TextView)this.findViewById(R.id.NewsTitle);
        newsDate = (TextView)this.findViewById(R.id.NewsDate);
       
        
        newsContent = (LinearLayout)this.findViewById(R.id.NewsWebLinearLayout);
        
        newsTitle.setText(rssItem.title);
        newsDate.setText(DateFormat.format("yyyy-MM-dd", rssItem.getPubDate()));

        webview = new WebView(this);
        refresh();
        newsContent.addView(webview);
        
    }
 
    private void refresh() {
    	
//      String content = "<html><head><style type=\"text/css\">body {margin: 0px} img {max-width: 100%;}</style></head>"
//    	+ "<body>" + rssItem.fulltext + "</body></html>";
    String content = "<html><head><style type=\"text/css\">body {margin: 0px} img {max-width: 100%;}" +
	"body {font-family: \"helvetica\"; font-size: " + fontsize + ";}\n" +
	"</style></head>" + "<body>" + rssItem.fulltext + "</body></html>";
    webview.loadDataWithBaseURL(null, content, "text/html", "utf-8", "about:blank");

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
	        	
	        	AlertDialog ad = new AlertDialog.Builder(this)
    					//.setTitle("Внимание")
	        			.setMessage(R.string.menu_favr)
	        			.setCancelable(false)
	        			.setPositiveButton(R.string.dialog_add, new DialogInterface.OnClickListener() {
	        	           public void onClick(DialogInterface dialog, int id) {
	        	        	   Common.addToFavr(rssItem); 
	        	        	   //MyActivity.this.finish();
	        	           }
	        			})
	        	       	.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
	        	           public void onClick(DialogInterface dialog, int id) {
	        	                dialog.cancel();
	        	           }
	        	       	})
	        	       	.create();
	        	ad.show();
	        	
	                            break;
	        }
	        case R.id.menushare: Common.sendMail(this, rssItem);
//	        case R.id.menushare: Common.sendMail(this);
	        					break;
	        case R.id.menudecr: fontsize = (fontsize > 8)?fontsize - 2:fontsize; refresh();
	        					break;
	        case R.id.menuincr: fontsize = (fontsize < 30)?fontsize + 2:fontsize; refresh();
								break;
	    }
	    return true;
	}
}
