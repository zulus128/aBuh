package com.vkassin.abuh;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.util.ByteArrayBuffer;

//import com.vkassin.abuh.Common.ApiException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
//import android.widget.ProgressBar;
//import android.widget.Toast;

import com.vkassin.abuh.NewsArrayAdapter;
import com.vkassin.abuh.Common.item_type;

public class NewsActivity extends Activity {

	private static final String TAG = "aBuh.NewsActivity"; 
	private ListView list;
	private NewsArrayAdapter adapter;
//	private RSSItem topitem;
	private ProgressBar pb;
	private TextView refreshText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.news);
        
        list = (ListView)this.findViewById(R.id.NewsList);
        pb = (ProgressBar)findViewById(R.id.ProgressBar01);
        refreshText = (TextView)findViewById(R.id.TopTextRefr);
        
    	adapter = new NewsArrayAdapter(this, R.layout.newsitem, new ArrayList<RSSItem>());
    	list.setAdapter(adapter);
    	
    	list.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				if(Common.topnews == null)
					return;
				Common.news = adapter.getItems();
				Common.curnews = adapter.getItems().get(arg2);
				Intent i = new Intent(NewsActivity.this, NewsDetail.class);
//				RSSItem it = adapter.getItems().get(arg2);
//				i.putExtra("rssitem", it);
//				i.putExtra("topitem", topitem);
//				i.putExtra("itemlist", adapter.getItems());
				NewsDetail.prepare();
				startActivity(i);
				//Log.i(TAG, "row: "+arg2+" arg3: "+arg3);
			}
		});
    	
    	ImageView ban = (ImageView) NewsActivity.this.findViewById(R.id.banner);
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
    	
    	refresh();
    	
    	Common.loadFavr();
    }
    
    private void refresh() {
    	
		pb.setVisibility(View.VISIBLE);

    	new getRSS().execute();
    	new getMainNews().execute();
    	new getBanner().execute();
    	
    	NewsDetail.resetViewsList();

    	QADetail.resetViewsList();

    }
    
    public boolean onTouchEvent(MotionEvent event) {
    	
		if(Common.topnews == null)
			return true;

//    	Log.i(TAG, "tap");
		Common.news = adapter.getItems();

		Intent i = new Intent(NewsActivity.this, NewsDetail.class);
//		i.putExtra("rssitem", topitem);
//		i.putExtra("topitem", topitem);
//		i.putExtra("itemlist", adapter.getItems());
		Common.curnews = Common.topnews;
		NewsDetail.prepare();
		startActivity(i);
    	return false;
    }

    private class getRSS extends AsyncTask<Context, Integer, ArrayList<RSSItem>> {

    	@Override
		protected ArrayList<RSSItem> doInBackground(Context... params) {
    					
    		ArrayList<RSSItem> rssItems = Common.getNews();
            return rssItems;
		}
    	
        protected void onProgressUpdate(Integer... progress) {
        //    ProgressBar mProgress = (ProgressBar)NewsActivity.this.findViewById(R.id.progressBar1);
        //    mProgress.setProgress(progress[0]);
        }

        protected void onPostExecute(final ArrayList<RSSItem> result) {

    		pb.setVisibility(View.GONE);
    		
    		String sformat = "Обновлено MM.dd в HH:mm";
    		
    		Calendar cal = Calendar.getInstance();
    		SimpleDateFormat sdf = new SimpleDateFormat(sformat);
    		refreshText.setText(sdf.format(cal.getTime()));
        	adapter.setItems(result);
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
        	
        		Common.topnews = result.get(0);
            	if (Common.topnews != null) {
            		
            		TextView title = (TextView) NewsActivity.this.findViewById(R.id.TopNewsTitleTextView);
            		TextView rubr = (TextView) NewsActivity.this.findViewById(R.id.TopNewsRubricTextView);
            		ImageView imrubr = (ImageView) NewsActivity.this.findViewById(R.id.top_arrow);
            		
            		imrubr.setVisibility(View.VISIBLE);
            		title.setText(Common.topnews.getShortTitle());
            		rubr.setText(Common.topnews.rubric);
            		
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
    				
    				URLConnection ucon = Common.topnews.imageUrl.openConnection();
					ucon.setUseCaches(true);
					InputStream is = ucon.getInputStream();
	                BufferedInputStream bis = new BufferedInputStream(is, is.available());
	                    
	                    ByteArrayBuffer baf = new ByteArrayBuffer(50);
	                    int current = 0;
	                    while ((current = bis.read()) != -1) {
	                            baf.append((byte) current);
	                    }
	                    
	                    img = BitmapFactory.decodeByteArray(baf.toByteArray(), 0, baf.length());
	
	                    fOut = NewsActivity.this.openFileOutput(Common.MAINPIC_FNAME, Context.MODE_PRIVATE);
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
			
    		Common.mainpic = img;
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
    
    private class getBanner extends AsyncTask<Context, Integer, Bitmap> {

    	@Override
		protected Bitmap doInBackground(Context... params) {
    		Bitmap img = null;
    		boolean fileNotFound = true;//false;
    		
    		if(fileNotFound) {
	    			
    			FileOutputStream fOut = null;
	                
    			try {
    				RSSHandler handler = new RSSHandler(item_type.IT_BANNER);
    				String errorMsg = Common.generalWebServiceCall(Common.BANNER_URL, handler);
    				
    				if(errorMsg.length() > 0)
    					Log.e(TAG, errorMsg);
    				else {
    					
    					ArrayList<RSSItem> result = handler.getParsedData();
    					
    					if(result.size() > 0) {
    			        	
    		        		Common.bannerItem = result.get(0);
    		            	if (Common.bannerItem != null) {
    		            		
    		            		URLConnection ucon = new URL(Common.bannerItem.bigb).openConnection();
    		            		ucon.setUseCaches(true);
    		            		InputStream is = ucon.getInputStream();
    		            		BufferedInputStream bis = new BufferedInputStream(is, is.available());
	                    
    		            		ByteArrayBuffer baf = new ByteArrayBuffer(50);
    		            		int current = 0;
    		            		while ((current = bis.read()) != -1) {
    		            			baf.append((byte) current);
    		            		}
	                    
    		            		img = BitmapFactory.decodeByteArray(baf.toByteArray(), 0, baf.length());
	
    		            		fOut = NewsActivity.this.openFileOutput(Common.BANNER_FNAME, Context.MODE_PRIVATE);
    		            		fOut.write(baf.toByteArray());
    		            		fOut.flush();
    		            		fOut.close();
    		            		is.close();
    		            		bis.close();
    		            		baf.clear();
    		            	}
    					}
    				}
	                } catch (FileNotFoundException e) {
	                } catch (IOException e) {
					}
    		}
			
            return img;
		}
    	
        protected void onProgressUpdate(Integer... progress) {
            
        }

        protected void onPostExecute(final Bitmap result) {
        	
        	ImageView ban = (ImageView) NewsActivity.this.findViewById(R.id.banner);
        	
        	if(result != null) {
        		
        		//ban.setVisibility(View.GONE);
        		ban.setVisibility(View.VISIBLE);
        		ban.setImageBitmap(result);
        	}
        	else
        		ban.setVisibility(View.GONE);
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.newsmenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
	        case R.id.menurefr: refresh();
	                            break;
	    }
	    return true;
	}
	
	
}
