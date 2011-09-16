package com.vkassin;

import java.util.ArrayList;
import com.vkassin.Common.ApiException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



import com.vkassin.NewsArrayAdapter;

public class NewsActivity extends Activity {

	private static final String TAG = "aBuh.NewsActivity"; 
	private ListView list;
	private NewsArrayAdapter adapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //refresh();
        
        setContentView(R.layout.news);
      
        list = (ListView)this.findViewById(R.id.NewsList);
        
    	adapter = new NewsArrayAdapter(this, R.layout.newsitem, new ArrayList<RSSItem>());
    	list.setAdapter(adapter);
    	/*ArrayList<RSSItem> r = new ArrayList<RSSItem>();
    	for(int i = 0; i < 15; i++)
    		r.add(new RSSItem());
    	adapter.addItems(r);
    	*/
    	list.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
		//		Toast toast = new Toast(this);
		//		toast.setText("You clicked");
		//		toast.show();
			}
		});
    	
    	new getRSS().execute();
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

    private class getRSS extends AsyncTask<Context, Integer, ArrayList<RSSItem>> {

    	@Override
		protected ArrayList<RSSItem> doInBackground(Context... params) {
			ArrayList<RSSItem> rssItems = Common.getNews();
            return rssItems;
		}
    	
        protected void onProgressUpdate(Integer... progress) {
  //          ProgressBar mProgress = (ProgressBar)Start.this.findViewById(R.id.MoreNewsProgressBar);
  //          mProgress.setProgress(progress[0]);
        }

        protected void onPostExecute(final ArrayList<RSSItem> result) {

        	adapter.addItems(result);
			adapter.notifyDataSetChanged();
        }
    }
}
