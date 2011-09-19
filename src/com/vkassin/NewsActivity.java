package com.vkassin;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.http.util.ByteArrayBuffer;

//import com.vkassin.Common.ApiException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.ProgressBar;
//import android.widget.Toast;

import com.vkassin.NewsArrayAdapter;

public class NewsActivity extends Activity {

	private static final String TAG = "aBuh.NewsActivity"; 
	private ListView list;
	private NewsArrayAdapter adapter;
	//private URL mainpicURL;
	private RSSItem topitem;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //refresh();
        
        setContentView(R.layout.news);
        
        list = (ListView)this.findViewById(R.id.NewsList);
        
    	adapter = new NewsArrayAdapter(this, R.layout.newsitem, new ArrayList<RSSItem>());
    	list.setAdapter(adapter);
    	
    	list.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				Intent i = new Intent(NewsActivity.this, NewsDetail.class);
				RSSItem it = adapter.getItems().get(arg2);
				i.putExtra("rssitem", it);
				startActivity(i);
				//Log.i(TAG, "row: "+arg2+" arg3: "+arg3);
			}
		});
    	
    	new getRSS().execute();
    	new getMainNews().execute();
    }
    
    public boolean onTouchEvent(MotionEvent event) {
    	
    	Log.i(TAG, "tap");
		Intent i = new Intent(NewsActivity.this, NewsDetail.class);
		i.putExtra("rssitem", topitem);
		startActivity(i);
    	return false;
    }
/*	private void refresh() {
		
		String cont = "no content!";
		try {
			cont = Common.getUrlContent(Common.MENU_URL);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.i(TAG, cont);
	}
*/
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
    
    private class getMainNews extends AsyncTask<Context, Integer, ArrayList<RSSItem>> {

    	@Override
		protected ArrayList<RSSItem> doInBackground(Context... params) {
			ArrayList<RSSItem> rssItems = Common.getMainNews();
            return rssItems;
		}
    	
        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(final ArrayList<RSSItem> result) {

        	if(result.size() > 0) {
        	
        		topitem = result.get(0);
            	if (topitem != null) {
            		
            		TextView title = (TextView) NewsActivity.this.findViewById(R.id.TopNewsTitleTextView);
            		TextView rubr = (TextView) NewsActivity.this.findViewById(R.id.TopNewsRubricTextView);
            		
            		title.setText(topitem.getShortTitle());
            		rubr.setText(topitem.rubric);
            		
            	}
        		//mainpicURL = item.imageUrl;
        		new getMainPic().execute();
        	}
        }
    }

    private class getMainPic extends AsyncTask<Context, Integer, Bitmap> {

    	@Override
		protected Bitmap doInBackground(Context... params) {
 //   		FileInputStream fIn = null;
    		Bitmap img = null;
    		boolean fileNotFound = true;//false;
    		
/*    		try {
    			fIn = Start.this.openFileInput("racelogo.png");
    			img = BitmapFactory.decodeStream(fIn);
				fIn.close();
    		} catch (FileNotFoundException e) {
    			fileNotFound = true;
    		} catch (IOException e) {
    			Log.e("KSSS", "Race logo: IOException");
    		}
  */  		
    		if(fileNotFound) {
	    			
    			FileOutputStream fOut = null;
	                
    			try {
    				
    				URLConnection ucon = topitem.imageUrl.openConnection();
					ucon.setUseCaches(true);
					InputStream is = ucon.getInputStream();
	                BufferedInputStream bis = new BufferedInputStream(is, is.available());
	                    
	                    ByteArrayBuffer baf = new ByteArrayBuffer(50);
	                    int current = 0;
	                    while ((current = bis.read()) != -1) {
	                            baf.append((byte) current);
	                    }
	                    
	                    img = BitmapFactory.decodeByteArray(baf.toByteArray(), 0, baf.length());
	
	                    fOut = NewsActivity.this.openFileOutput("mainpic.png", Context.MODE_PRIVATE);
	                    fOut.write(baf.toByteArray());
	                    fOut.flush();
	                    fOut.close();
	                    is.close();
	                    bis.close();
	                    baf.clear();
	                } catch (FileNotFoundException e) {
	                } catch (IOException e) {
					}
    		}
			
            return img;
		}
    	
        protected void onProgressUpdate(Integer... progress) {
            
        }

        protected void onPostExecute(final Bitmap result) {
        	
        	ImageView mainpic = (ImageView) NewsActivity.this.findViewById(R.id.mainpic);
        	
        	if(result != null)
        		mainpic.setImageBitmap(result);
        	//else
        		//mainpic.setImageResource(R.drawable.logo_fallback);
        }
    }
}
