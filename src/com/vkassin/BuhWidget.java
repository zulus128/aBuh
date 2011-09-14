package com.vkassin;

import org.json.JSONObject;

import com.vkassin.Common.ApiException;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class BuhWidget extends TabActivity {

	private static final String TAG = "aBuh.BuhWidget"; 
	
	public void onCreate(Bundle savedInstanceState) {
		
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);

        Common.prepareUserAgent(this);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, NewsActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("news").setIndicator("Новости",
	                      res.getDrawable(R.drawable.house))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, QAActivity.class);
	    spec = tabHost.newTabSpec("qas").setIndicator("Вопрос-ответ",
	                      res.getDrawable(R.drawable.chat2))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);
	}

    @Override
    public void onStart() {
        super.onStart();

//        Common.prepareUserAgent(this);
    }
}
