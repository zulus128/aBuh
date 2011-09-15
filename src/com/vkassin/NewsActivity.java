package com.vkassin;

import java.util.ArrayList;

import com.vkassin.Common.ApiException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class NewsActivity extends Activity {

	private static final String TAG = "aBuh.NewsActivity"; 
	private ListView list;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        refresh();
        
        setContentView(R.layout.news);
        
        list = (ListView)this.findViewById(R.id.NewsList);
        
        String[] names = new String[] { "Linux", "Windows7", "Eclipse", "Suse",
				"Ubuntu", "Solaris", "Android", "iPhone" };
		// Use your own layout and point the adapter to the UI elements which
		// contains the label
    	adapter = new NewsArrayAdapter(this, R.layout.newsitem, new ArrayList<RSSItem>(), this);
    	list.setAdapter(adapter);
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
