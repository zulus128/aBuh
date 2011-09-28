package com.vkassin;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class QAActivity extends ListActivity {
	
	private static final String TAG = "aBuh.QAActivity"; 
	private QAArrayAdapter adapter;
	
	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		setContentView(R.layout.qas);
		 
    	adapter = new QAArrayAdapter(this, R.layout.qaitem, new ArrayList<RSSItem>());
    	setListAdapter(adapter);
		
		ListView listView = getListView();
		listView.setBackgroundColor(Color.WHITE);
		//listView.setFocusable(false);
		
		new getRSS().execute();
		
    	ImageView ban = (ImageView) this.findViewById(R.id.banner);
    	ban.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				//Log.i(TAG, "Click!");
				RSSItem it = Common.bannerItem;
				if(it != null){
					String url = it.clink;
					final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(intent);
				}
			}
		});
    	
    	if(Common.bannerItem != null) {
    		
    		ban.setVisibility(View.VISIBLE);
    		java.io.FileInputStream in;
			try {
				in = openFileInput(Common.BANNER_FNAME);
	    		ban.setImageBitmap(BitmapFactory.decodeStream(in));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	else
    		ban.setVisibility(View.GONE);
	}

	@Override
	protected void onListItemClick(ListView l, View v, final int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		//Log.i(TAG,"click");
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
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.qamenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menuask: {
	        	Intent i = new Intent(this, AskActivity.class);
	        	startActivity(i);
                break;
	        }
	    }
	    return true;
	}
}
