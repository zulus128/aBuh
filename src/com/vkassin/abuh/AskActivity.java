package com.vkassin.abuh;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class AskActivity extends Activity {

	WebView mWebView;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.ask);

	    mWebView = (WebView) findViewById(R.id.webview);
	    mWebView.getSettings().setJavaScriptEnabled(true);
	    mWebView.loadUrl(Common.SENDQ_URL);
	}
}
