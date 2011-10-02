package com.vkassin.abuh;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class NewsDetail extends Activity {
	
	private static final String TAG = "aBuh.NewsDetail"; 
	
//	private TextView newsTitle;
//	private TextView newsDate;
//	private LinearLayout newsContent;
//	private RSSItem rssItem;
	
	private static int fontsize = 16;
//	private WebView webview;

	private float downXValue;
	private float downYValue;
//    ViewFlipper vf;
//	LayoutInflater vi;

	public boolean dispatchTouchEvent(MotionEvent event) {
	
//		Log.i(TAG, "dispatchTouchEvent");
//        return true;
//	}
//	public boolean onTouch(View v, MotionEvent event) {
	    
//		Log.i(TAG, "onTouch");
		// Get the action that was done on this touch event
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                // store the X value when the user's finger was pressed down
                downXValue = event.getX();
                downXValue = event.getY();
//                Log.i(TAG, "ACTION_DOWN");
                break;
            }

            case MotionEvent.ACTION_UP:
            {
                // Get the X value when the user released his/her finger
                float currentX = event.getX();  
                float currentY = event.getY();   
                
//                Log.i(TAG, "ACTION_UP");

//                if(Math.abs(currentY - downYValue) > 140)
//                	return true;
                
                // going backwards: pushing stuff to the right
                if (downXValue < currentX)
                {
//                    // Get a reference to the ViewFlipper
                     ViewFlipper vf = (ViewFlipper) findViewById(R.id.details);
//                     // Set the animation
//                      vf.setAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
                      vf.setInAnimation(this, R.anim.slide_in_left);
                      vf.setOutAnimation(this, R.anim.slide_out_right);
//                      // Flip!
                      vf.showPrevious();
                }

                // going forwards: pushing stuff to the left
                if (downXValue > currentX)
                {
//                    // Get a reference to the ViewFlipper
                    ViewFlipper vf = (ViewFlipper) findViewById(R.id.details);
//                     // Set the animation
//                     vf.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right));
                    vf.setInAnimation(this, R.anim.slide_in_right);
                    vf.setOutAnimation(this, R.anim.slide_out_left);
//                      // Flip!
                     vf.showNext();
                }
                break;
            }
        }

        // if you return false, these actions will not be recorded
        return true;
	}
    
	private void flipadd(RSSItem item) {
		
		ViewFlipper vf = (ViewFlipper)this.findViewById(R.id.details);
		LayoutInflater vi = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout flipdetail = (LinearLayout)vi.inflate(R.layout.flipdetail, null);
        
        TextView newsTitle = (TextView)flipdetail.findViewById(R.id.NewsTitle);
        TextView newsDate = (TextView)flipdetail.findViewById(R.id.NewsDate);
       
        LinearLayout newsContent = (LinearLayout)flipdetail.findViewById(R.id.NewsWebLinearLayout);
        
        newsTitle.setText(item.title);
        newsDate.setText(DateFormat.format("yyyy-MM-dd", item.getPubDate()));

        WebView webview = new WebView(this);
       
        String content = "<html><head><style type=\"text/css\">body {margin: 0px} img {max-width: 100%;}" +
    	"body {font-family: \"helvetica\"; font-size: " + fontsize + ";}\n" +
    	"</style></head>" + "<body>" + item.fulltext + "</body></html>";
        webview.loadDataWithBaseURL(null, content, "text/html", "utf-8", "about:blank");       
        
        newsContent.addView(webview);
        
        vf.addView(flipdetail);	
	}
	
	// Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsdetail);
    	
    	//RSSItem topItem = (RSSItem)this.getIntent().getExtras().get("topitem");
        flipadd(Common.topnews);
        
        //ArrayList<RSSItem> list = (ArrayList<RSSItem>)this.getIntent().getExtras().get("itemlist");
        for(RSSItem i : Common.news) {
        
            flipadd(i);

        }
        
        ViewFlipper vf = (ViewFlipper)this.findViewById(R.id.details);
        vf.showNext();
     //   refresh();
        
    }
 
 
      
    private void refresh() {
    	ViewFlipper vf = (ViewFlipper)this.findViewById(R.id.details);
    	vf.getCurrentView();


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
//	        	        	   Common.addToFavr(rssItem); 
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
	        case R.id.menushare: //Common.sendMail(this, rssItem);
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
