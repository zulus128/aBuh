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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.vkassin.abuh.RSSItem;

public class NewsDetail extends Activity {
	
	private static final String TAG = "aBuh.NewsDetail"; 
	private GestureDetector gestureDetector;
	
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

	
	private static ViewFlipper vf;
	private static ArrayList<View> views;
	private static int curr;
	
/*	public boolean dispatchTouchEvent(MotionEvent event) {
	
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
                
                Log.i(TAG, "ACTION_UP " + Math.abs(currentY - downYValue));

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
        return false;
	}
  */
	@Override
//	public boolean onTouchEvent(MotionEvent event) {
	public boolean dispatchTouchEvent(MotionEvent event) {
		super.dispatchTouchEvent(event);
		
		if (gestureDetector.onTouchEvent(event))
			return true;
		else
			return false;
	}
	
	private static void flipadd(RSSItem item) {
		
		LayoutInflater vi = (LayoutInflater)Common.app_ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout flipdetail = (LinearLayout)vi.inflate(R.layout.flipdetail, null);
        
        TextView newsTitle = (TextView)flipdetail.findViewById(R.id.NewsTitle);
        TextView newsDate = (TextView)flipdetail.findViewById(R.id.NewsDate);
       
//        LinearLayout newsContent = (LinearLayout)flipdetail.findViewById(R.id.NewsWebLinearLayout);
        
        newsTitle.setText(item.title);
        newsDate.setText(DateFormat.format("yyyy-MM-dd", item.getPubDate()));

//        WebView webview = new WebView(this);
       
//        WebView webview = (WebView)flipdetail.findViewById(R.id.webView1);
        
//        String content = "<html><head><style type=\"text/css\">body {margin: 0px} img {max-width: 100%;}" +
//    	"body {font-family: \"helvetica\"; font-size: " + fontsize + ";}\n" +
//    	"</style></head>" + "<body>" + item.fulltext + "</body></html>";
//        webview.loadDataWithBaseURL(null, content, "text/html", "utf-8", "about:blank");       
        
//        newsContent.addView(webview);
        
        views.add(flipdetail);
	}
	
	public static void resetViewsList() {
	
		views = null;
	}
	
	public static void prepare() {
		
		if (views == null)
			views = new ArrayList<View>();
		else
			return;
//		views.clear();
		
		flipadd(Common.topnews);
		
        for(RSSItem it:Common.news)
        	flipadd(it);

	}
	
	private RSSItem getCurrentItem() {
		
		if(curr == 0)
			return Common.topnews;
		else
			return Common.news.get(curr - 1);
	}
	
	// Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsdetail);
    	
        if(vf != null)
        	vf.removeAllViews();
//        else
        	vf = (ViewFlipper)this.findViewById(R.id.details);
        
//        vf.removeAllViews();
        
//        flipadd(Common.topnews);
        
        for(View v:views)
        	vf.addView(v);
        
//        TextView newsTitle = (TextView)this.findViewById(R.id.NewsTitle);
//        TextView newsDate = (TextView)this.findViewById(R.id.NewsDate);
//       
//        LinearLayout newsContent = (LinearLayout)this.findViewById(R.id.NewsWebLinearLayout);
//        
//        newsTitle.setText(Common.topnews.title);
//        newsDate.setText(DateFormat.format("yyyy-MM-dd", Common.topnews.getPubDate()));
//
//        WebView webview = new WebView(this);
//       
//        String content = "<html><head><style type=\"text/css\">body {margin: 0px} img {max-width: 100%;}" +
//    	"body {font-family: \"helvetica\"; font-size: " + fontsize + ";}\n" +
//    	"</style></head>" + "<body>" + Common.topnews.fulltext + "</body></html>";
//        webview.loadDataWithBaseURL(null, content, "text/html", "utf-8", "about:blank");       
//        
//        newsContent.addView(webview);
        
        //ArrayList<RSSItem> list = (ArrayList<RSSItem>)this.getIntent().getExtras().get("itemlist");
        curr = 0;
        
//        for(RSSItem i : Common.news) {
        for(int i = 0; i < Common.news.size(); i++) {
        
            RSSItem it = Common.news.get(i);
//        	flipadd(it);
        
            if(Common.curnews.title.equals(it.title))
            	curr = i + 1;

        }

        vf.setDisplayedChild(curr);
        refresh();

        gestureDetector = new GestureDetector(
                new SwipeGestureDetector());
        
    }
 
    private void onLeftSwipe() {
    	
        // Get a reference to the ViewFlipper
        ViewFlipper vf = (ViewFlipper) findViewById(R.id.details);
//         // Set the animation
//         vf.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right));
        vf.setInAnimation(this, R.anim.slide_in_right);
        vf.setOutAnimation(this, R.anim.slide_out_left);
//          // Flip!
        curr++;
        LinearLayout fd = (LinearLayout)views.get(curr);
        ScrollView sv = (ScrollView)fd.findViewById(R.id.ScrollView01);
        sv.fullScroll(View.FOCUS_UP);
         vf.showNext();
         refresh();
      }

      private void onRightSwipe() {
    	  
    	   // Get a reference to the ViewFlipper
          ViewFlipper vf = (ViewFlipper) findViewById(R.id.details);
//          // Set the animation
//           vf.setAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
           vf.setInAnimation(this, R.anim.slide_in_left);
           vf.setOutAnimation(this, R.anim.slide_out_right);
//           // Flip!
           curr--;
           LinearLayout fd = (LinearLayout)views.get(curr);
           ScrollView sv = (ScrollView)fd.findViewById(R.id.ScrollView01);
           sv.fullScroll(View.FOCUS_UP);
           vf.showPrevious();
           refresh();
      }
      
    private class SwipeGestureDetector 
    extends SimpleOnGestureListener {
// Swipe properties, you can change it to make the swipe 
// longer or shorter and speed
private static final int SWIPE_MIN_DISTANCE = 60;
private static final int SWIPE_MAX_OFF_PATH = 250;
private static final int SWIPE_THRESHOLD_VELOCITY = 200;

@Override
public boolean onFling(MotionEvent e1, MotionEvent e2,
                   float velocityX, float velocityY) {
try {
  float diffAbs = Math.abs(e1.getY() - e2.getY());
  float diff = e1.getX() - e2.getX();

//  Log.e(TAG, "diffpath = "+diffAbs);
  
  if (diffAbs > SWIPE_MAX_OFF_PATH)
    return false;
  
//  Log.e(TAG, "diff = "+diff);
//  Log.e(TAG, "vel = "+velocityX);
  
  // Left swipe
  if (diff > SWIPE_MIN_DISTANCE
  && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
     NewsDetail.this.onLeftSwipe();

  // Right swipe
  } else if (diff < -SWIPE_MIN_DISTANCE
  && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	  NewsDetail.this.onRightSwipe();
  }
} catch (Exception e) {
  Log.e(TAG, "Error on gestures");
}
return false;
}
}
      
    private void refresh() {
       
    	LinearLayout fd = (LinearLayout)views.get(curr);
    	
        WebView webview = (WebView)fd.findViewById(R.id.webView1);
        
        String content = "<html><head><style type=\"text/css\">body {margin: 0px} img {max-width: 100%;}" +
    	"body {font-family: \"helvetica\"; font-size: " + fontsize + ";}\n" +
    	"</style></head>" + "<body>" + this.getCurrentItem().fulltext + "</body></html>";
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
	        	        	   Common.addToFavr(getCurrentItem()); 
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
	        case R.id.menushare: Common.sendMail(this, getCurrentItem());
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
