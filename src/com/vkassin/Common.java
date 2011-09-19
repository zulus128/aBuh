package com.vkassin;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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


public class Common {

	private static final String TAG = "aBuh.Common"; 
	
	public static final String MENU_URL_FOR_REACH = "www.buhgalteria.ru";
	public static final String MENU_URL = "http://www.buhgalteria.ru/rss/iphoneapp/iphonenews.php";
//	public static final String MENU_URL = "http://avtofon.com/sheet/xmlprice";
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

	/*
    private static String sUserAgent = null;
    private static final int HTTP_STATUS_OK = 200;
 
  
    private static byte[] sBuffer = new byte[50000];
    
	public static class ApiException extends Exception {

		private static final long serialVersionUID = 1L;

		public ApiException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public ApiException(String detailMessage) {
            super(detailMessage);
        }
    }
    
	public static void prepareUserAgent(Context context) {
        try {
            // Read package name and version number from manifest
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            sUserAgent = String.format("%s/%s (Linux; Android)",
                    info.packageName, info.versionName);

        } catch(NameNotFoundException e) {
            Log.e(TAG, "Couldn't find package information in PackageManager", e);
        }
    }  
    
    public static synchronized String getUrlContent(String url) throws ApiException {
        if (sUserAgent == null) {
            throw new ApiException("User-Agent string must be prepared");
        }

        // Create client and set our specific user-agent string
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        request.setHeader("User-Agent", sUserAgent);

        try {
            HttpResponse response = client.execute(request);

            // Check if server response is valid
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != HTTP_STATUS_OK) {
                throw new ApiException("Invalid response from server: " +
                        status.toString());
            }

            // Pull content stream from response
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();

            ByteArrayOutputStream content = new ByteArrayOutputStream();

            // Read response into a buffered stream
            int readBytes = 0;
            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                content.write(sBuffer, 0, readBytes);
            }

            // Return result from buffered stream
            return new String(content.toByteArray());
        } catch (IOException e) {
        	Log.e(TAG, "Error:",e);
            throw new ApiException("Problem communicating with API", e);
        }
    }
*/
    
	public static ArrayList<RSSItem> getNews() {
		
		RSSHandler handler = new RSSHandler();
//		handler.SetArticleLimit(5);
//		handler.SetOffset(offset);
		
		String errorMsg = generalWebServiceCall(MENU_URL, handler);
		
		if(errorMsg.length() > 0)
			Log.e(TAG, errorMsg);
		
		return handler.getParsedData();
	}
	
	public static ArrayList<RSSItem> getMainNews() {
		
		RSSHandler handler = new RSSHandler();

		String errorMsg = generalWebServiceCall(TOPMENU_URL, handler);
		
		if(errorMsg.length() > 0)
			Log.e(TAG, errorMsg);
		
		return handler.getParsedData();
	}
	
	private static String generalWebServiceCall(String urlStr, ContentHandler handler) {
		
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
