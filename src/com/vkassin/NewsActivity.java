package com.vkassin;

import com.vkassin.Common.ApiException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class NewsActivity extends Activity {

	private static final String TAG = "aBuh.NewsActivity"; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView textview = new TextView(this);
        textview.setText("This is the News tab");
        setContentView(textview);
        
        refresh();
    }
    
	private void refresh() {
		
		String cont = "no content!";
		try {
			cont = Common.getUrlContent(Common.MENU_URL);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.i(TAG, cont);
	}

}
