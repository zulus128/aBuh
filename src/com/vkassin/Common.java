package com.vkassin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class Common {

	private static final String TAG = "aBuh.Common"; 
	
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

    private static String sUserAgent = null;
    private static final int HTTP_STATUS_OK = 200;
    private static byte[] sBuffer = new byte[50000];
    
	public static class ApiException extends Exception {
        /**
		 * 
		 */
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
            sUserAgent = String.format("%s/%s (Linux; Android)"/*context.getString(R.string.template_user_agent)*/,
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

}
