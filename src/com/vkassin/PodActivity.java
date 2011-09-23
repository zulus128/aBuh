package com.vkassin;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PodActivity extends ListActivity {

	private static final String TAG = "aBuh.PodActivity"; 
	
	private PodArrayAdapter adapter;
    //     String[] listItems = {"exploring", "android", 
    //                           "list", "activities"};

         public void onCreate(Bundle icicle) {

		super.onCreate(icicle);

		setContentView(R.layout.pods);

	//	setListAdapter(new ArrayAdapter(this, 
//                android.R.layout.simple_list_item_1, listItems));
		 
    	adapter = new PodArrayAdapter(this, R.layout.poditem, new ArrayList<RSSItem>());
		this.setListAdapter(adapter);
				
		ListView listView = getListView();
		listView.setBackgroundColor(Color.WHITE);
		
		new getRSS().execute();
	}

	@Override
	protected void onListItemClick(ListView l, View v, final int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Log.i(TAG,"click");

		RSSItem it = adapter.getItems().get(position);
		String url = it.mplink;
		final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(intent);
	}
    
    private class getRSS extends AsyncTask<Context, Integer, ArrayList<RSSItem>> {

    	@Override
		protected ArrayList<RSSItem> doInBackground(Context... params) {
			ArrayList<RSSItem> rssItems = Common.getPods();
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
