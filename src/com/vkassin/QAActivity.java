package com.vkassin;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class QAActivity extends ListActivity implements OnItemClickListener {
	
	String[] listItems = {"exploring", "android", 
                          "list", "activities"};
	
	private static final String TAG = "aBuh.QAActivity"; 
	private QAArrayAdapter adapter;
	
	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);

		setContentView(R.layout.qas);
		 
    	adapter = new QAArrayAdapter(this, R.layout.qaitem, new ArrayList<RSSItem>());
		
    	setListAdapter(adapter);
		
		//setListAdapter(new ArrayAdapter(this, R.layout.qaitem, listItems));
		
		ListView listView = getListView();
		listView.setBackgroundColor(Color.WHITE);
		//listView.setFocusable(false);
		
		new getRSS().execute();
	}

    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

		Log.i(TAG,"click1");

    }

	@Override
	protected void onListItemClick(ListView l, View v, final int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Log.i(TAG,"click");
		Intent i = new Intent(QAActivity.this, QADetail.class);
		RSSItem it = adapter.getItems().get(position);
		i.putExtra("rssitem", it);
		startActivity(i);

	}
    
    private class getRSS extends AsyncTask<Context, Integer, ArrayList<RSSItem>> {

    	@Override
		protected ArrayList<RSSItem> doInBackground(Context... params) {
			ArrayList<RSSItem> rssItems = Common.getQAs();
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
