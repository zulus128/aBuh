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
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.vkassin.abuh.RSSItem;

public class NewsDetail extends Activity {
	
	private static final String TAG = "aBuh.NewsDetail"; 
	private GestureDetector gestureDetector;
	private static int fontsize = 16;
	private float downXValue;
	private float downYValue;
	private static ViewFlipper vf;
	private static ArrayList<View> views;
	private static int curr;
	
	@Override
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
       
        newsTitle.setText(item.title);
        newsDate.setText(DateFormat.format("yyyy-MM-dd", item.getPubDate()));
        
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

        vf = (ViewFlipper)this.findViewById(R.id.details);
        
        for(View v:views)
        	vf.addView(v);
        
        curr = 0;
        
        for(int i = 0; i < Common.news.size(); i++) {
        
            RSSItem it = Common.news.get(i);
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
        if(curr >= Common.news.size())
        	curr = 0;
        
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
           if(curr < 0)
              	curr = Common.news.size() - 1;

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
  if ((diff > SWIPE_MIN_DISTANCE)
  && (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)) {
     NewsDetail.this.onLeftSwipe();

  // Right swipe
  } else if ((diff < -SWIPE_MIN_DISTANCE)
  && (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)) {
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
