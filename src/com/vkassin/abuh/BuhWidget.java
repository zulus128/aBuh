package com.vkassin.abuh;

import org.json.JSONObject;

//import com.vkassin.abuh.Common.ApiException;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class BuhWidget extends TabActivity {

	private static final String TAG = "aBuh.BuhWidget"; 
	private static final float textsize = 9.5f;
	
	public void onCreate(Bundle savedInstanceState) {
		
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);

	    Common.app_ctx = getApplicationContext();
	    
 //       Common.prepareUserAgent(this);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Reusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, NewsActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("tnews").setIndicator("Новости",
	                      res.getDrawable(R.drawable.house))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, QAActivity.class);
	    spec = tabHost.newTabSpec("tqas").setIndicator("Вопрос-ответ",
	                      res.getDrawable(R.drawable.chat2))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, FavrActivity.class);
	    spec = tabHost.newTabSpec("tfavr").setIndicator("Избранное",
	                      res.getDrawable(R.drawable.star))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, PodActivity.class);
	    spec = tabHost.newTabSpec("tpod").setIndicator("Подкасты",
	                      res.getDrawable(R.drawable.note))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, SettActivity.class);
	    spec = tabHost.newTabSpec("tsett").setIndicator("Настройки",
	                      res.getDrawable(R.drawable.gear))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	  
	    LinearLayout ll = (LinearLayout) tabHost.getChildAt(0);
	    TabWidget tw = (TabWidget) ll.getChildAt(0);
	    RelativeLayout rllf = (RelativeLayout) tw.getChildAt(0);
//	    rllf.setBackgroundColor(0xFF0000FF);
	    TextView lf = (TextView) rllf.getChildAt(1);
	    lf.setTextSize(textsize);
	    lf.setPadding(0, 0, 0, 2);
	    rllf = (RelativeLayout) tw.getChildAt(1);
//	    rllf.setBackgroundColor(0xFF0000FF);
	    lf = (TextView) rllf.getChildAt(1);
	    lf.setTextSize(textsize);
//	    lf.setLines(2);
//	    lf.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
	    lf.setPadding(0, 0, 0, 2);
	    rllf = (RelativeLayout) tw.getChildAt(2);
//	    rllf.setBackgroundColor(0xFF0000FF);
	    lf = (TextView) rllf.getChildAt(1);
	    lf.setTextSize(textsize);
	    lf.setPadding(0, 0, 0, 2);
	    rllf = (RelativeLayout) tw.getChildAt(3);
//	    rllf.setBackgroundColor(0xFF0000FF);
	    lf = (TextView) rllf.getChildAt(1);
	    lf.setTextSize(textsize);
	    lf.setPadding(0, 0, 0, 2);

	    rllf = (RelativeLayout) tw.getChildAt(4);
//	    rllf.setBackgroundColor(0xFF0000FF);
	    lf = (TextView) rllf.getChildAt(1);
	    lf.setTextSize(textsize);
	    lf.setPadding(0, 0, 0, 2);
	    
	    tabHost.setCurrentTab(0);
	}

    @Override
    public void onStart() {
        super.onStart();

//        Common.prepareUserAgent(this);
    }
}
