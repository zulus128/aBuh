package com.vkassin;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StreamCorruptedException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.vkassin.Common.item_type;

public class Common {

	private static final String TAG = "aBuh.Common"; 
	
	public enum item_type { IT_NONE, IT_REGULARNEWS, IT_TOPNEWS, IT_QA, IT_PODCAST, IT_BANNER };
	
	public static final String MENU_URL_FOR_REACH = "www.buhgalteria.ru";
	public static final String MENU_URL = "http://www.buhgalteria.ru/rss/iphoneapp/iphonenews.php";
	public static final String TOPMENU_URL = "http://www.buhgalteria.ru/rss/iphoneapp/iphoneday.php";
	public static final String QAMENU_URL = "http://www.buhgalteria.ru/rss/iphoneapp/iphonefaq.php";
	public static final String SENDQ_URL = "http://www.buhgalteria.ru/addq/iphone.php";
	public static final String PODCAST_URL = "http://www.buhgalteria.ru/rss/iphoneapp/iphonepodcast.php";

	public static final String EMAIL_URL = "http://www.buhgalteria.ru/iphoneapp/mailget.php?email=%@";

	public static final String BANNER_URL = "http://www.buhgalteria.ru/rss/iphoneapp/iphonebanner.php";
	public static final String NOBANNER_URL = "http://www.buhgalteria.ru/rss/iphoneapp/iphonenobanner.php";

	public static final String ITEM_TAG = "item";
	public static final String TITLE_TAG = "title";
	public static final String LINK_TAG = "link";
	public static final String ITUNESLINK_TAG = "ituneslink";
	public static final String RUBRIC_TAG = "rubric";
	public static final String FULLTEXT_TAG = "full-text";
	public static final String DATE_TAG = "pubDate";
	public static final String IMAGE_TAG = "enclosure";
	public static final String DESCRIPTION_TAG = "description";
	public static final String SMALLBANNER_TAG = "smallb";
	public static final String BIGBANNER_TAG = "bigb";
	public static final String LINKBANNER_TAG = "clink";
	public static final String FAV_FNAME = "favourites";

	public static final String BANNER_FNAME = "bigbanner.png";
	
	public static RSSItem bannerItem;
	public static Context app_ctx;
	
	private static ArrayList<RSSItem> favourites;
	
	public static void addToFavr(RSSItem item) {
	
		favourites.add(item);
		
		saveFavr();
		
	}
	
	public static void delFavr(int i) {
		
		favourites.remove(i);
		Log.w(TAG, "size1 = " + favourites.size());
		saveFavr();
		
	}
	
	public static ArrayList<RSSItem> getFavrs() {
	
		Log.w(TAG, "size = " + favourites.size());
		return favourites;
	}
	
	public static void saveFavr() {
	
		FileOutputStream fos;
		try {
			
			fos = app_ctx.openFileOutput(FAV_FNAME, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(favourites);
			os.close();
			fos.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void loadFavr() {
	   
	FileInputStream fileInputStream;
	try {
		
		fileInputStream = app_ctx.openFileInput(FAV_FNAME);
		ObjectInputStream oInputStream = new ObjectInputStream(fileInputStream);
		Object one = oInputStream.readObject();
		favourites = (ArrayList<RSSItem>) one;
		oInputStream.close();
		fileInputStream.close();
		
	} catch (FileNotFoundException e) {
		
		//e.printStackTrace();
  	   Log.i(TAG, "creates blank. no file " + FAV_FNAME);
 	   favourites = new ArrayList<RSSItem>();
 	   
	} catch (StreamCorruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	//return favourites;
	}
	
	public static ArrayList<RSSItem> getNews() {
		
		RSSHandler handler = new RSSHandler(item_type.IT_REGULARNEWS);
		String errorMsg = generalWebServiceCall(MENU_URL, handler);
		
		if(errorMsg.length() > 0)
			Log.e(TAG, errorMsg);
		
		return handler.getParsedData();
	}

	public static ArrayList<RSSItem> getQAs() {
		
		RSSHandler handler = new RSSHandler(item_type.IT_QA);
		String errorMsg = generalWebServiceCall(QAMENU_URL, handler);
		
		if(errorMsg.length() > 0)
			Log.e(TAG, errorMsg);
		
		return handler.getParsedData();
	}
	
	public static ArrayList<RSSItem> getPods() {
		
		RSSHandler handler = new RSSHandler(item_type.IT_PODCAST);
		String errorMsg = generalWebServiceCall(PODCAST_URL, handler);
		
		if(errorMsg.length() > 0)
			Log.e(TAG, errorMsg);
		
		return handler.getParsedData();
	}
	
	public static ArrayList<RSSItem> getMainNews() {
		
		RSSHandler handler = new RSSHandler(item_type.IT_TOPNEWS);

		String errorMsg = generalWebServiceCall(TOPMENU_URL, handler);
		
		if(errorMsg.length() > 0)
			Log.e(TAG, errorMsg);
		
		return handler.getParsedData();
	}
	
	public static void getBanner() {
		
	}
	
	public static String generalWebServiceCall(String urlStr, ContentHandler handler) {
		
		String errorMsg = "";
		
		try {
			URL url = new URL(urlStr);
			
			HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
	        urlc.setRequestProperty("User-Agent", "Android Application: aBuh");
	        urlc.setRequestProperty("Connection", "close");
	        urlc.setRequestProperty("Accept-Charset", "windows-1251");
			
	        urlc.setConnectTimeout(1000 * 5); // mTimeout is in seconds
	        urlc.setDoInput(true);
	        urlc.connect();
	        
	        if(urlc.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// Get a SAXParser from the SAXPArserFactory.
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();
	
				// Get the XMLReader of the SAXParser we created.
				XMLReader xr = sp.getXMLReader();
				
				// Apply the handler to the XML-Reader
				xr.setContentHandler(handler);
	
				// Parse the XML-data from our URL.
				InputStream is = urlc.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                ByteArrayBuffer baf = new ByteArrayBuffer(500);
                int current = 0;
                while ((current = bis.read()) != -1) {
                        baf.append((byte) current);
                }
                ByteArrayInputStream bais = new ByteArrayInputStream(baf.toByteArray());
                Reader isr = new InputStreamReader(bais, "windows-1251");
                InputSource ist = new InputSource();
                //ist.setEncoding("UTF-8");
                ist.setCharacterStream(isr);
				xr.parse(ist);
				// Parsing has finished.
				
				bis.close();
				baf.clear();
				bais.close();
				is.close();
	        }
	        
	        urlc.disconnect();
			
		} catch (SAXException e) {
			// All is OK :)
		} catch (MalformedURLException e) {
			Log.e(TAG, errorMsg = "MalformedURLException");
		} catch (IOException e) {
			Log.e(TAG, errorMsg = "IOException");
		} catch (ParserConfigurationException e) {
			Log.e(TAG, errorMsg = "ParserConfigurationException");
		} catch (ArrayIndexOutOfBoundsException e) {
			Log.e(TAG, errorMsg = "ArrayIndexOutOfBoundsException");
		}
		
		return errorMsg;
	}
}
