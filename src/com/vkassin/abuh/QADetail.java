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

public class QADetail extends Activity {
	
	private TextView qaTitle;
	private TextView qaDate;
	private LinearLayout qaContent;
	private RSSItem rssItem;
	
	private static int fontsize = 12;
	private WebView webview, webview1;

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
        webview = new WebView(this);
//        String content = "<html><head><style type=\"text/css\">body {margin: 0px} img {max-width: 100%;}" +
//		"body {font-family: \"helvetica\"; font-size: 12; font-style:oblique;}\n" +
//		"</style></head>" + "<body>" + rssItem.description + "</body></html>";
//        
//        webview.loadDataWithBaseURL(null, content, "text/html", "utf-8", "about:blank");
        qaContent.addView(webview);

        qaContent = (LinearLayout)this.findViewById(R.id.QAWebLinearLayout2);
        webview1 = new WebView(this);
//        content = "<html><head><style type=\"text/css\">body {margin: 0px} img {max-width: 100%;}</style></head>"
//        	+ "<body>" + rssItem.fulltext + "</body></html>";
//        webview1.loadDataWithBaseURL(null, content, "text/html", "utf-8", "about:blank");
        qaContent.addView(webview1);

        refresh();
    }
   
    private void refresh() {
    	
//      String content = "<html><head><style type=\"text/css\">body {margin: 0px} img {max-width: 100%;}</style></head>"
//    	+ "<body>" + rssItem.fulltext + "</body></html>";
    String content = "<html><head><style type=\"text/css\">body {margin: 0px} img {max-width: 100%;}" +
	"body {font-family: \"helvetica\"; font-size: " + fontsize + ";font-style:oblique;}\n" +
	"</style></head>" + "<body>" + rssItem.description + "</body></html>";
    webview.loadDataWithBaseURL(null, content, "text/html", "utf-8", "about:blank");

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
								break;
	        case R.id.menudecr: fontsize = (fontsize > 8)?fontsize - 2:fontsize; refresh();
								break;
	        case R.id.menuincr: fontsize = (fontsize < 30)?fontsize + 2:fontsize; refresh();
								break;
					
			
	    }
	    return true;
	}
	
}
