package com.vkassin.abuh;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import android.util.Log;
import com.vkassin.abuh.RSSItem;
import com.vkassin.abuh.Common.item_type;

public class RSSHandler extends DefaultHandler {
	
	private static final String TAG = "aBuh.RSSHandler"; 
	
	private ArrayList<RSSItem> parsedDate = new ArrayList<RSSItem>();
	
	// Used to define what elements we are currently in
    private boolean inItem = false;
	private RSSItem currentItem;
	private StringBuilder sb = new StringBuilder();
	private item_type itemtype;
	
	public RSSHandler(item_type t) {
		
		itemtype = t;
	}
	
    public ArrayList<RSSItem> getParsedData() {
        return this.parsedDate;
    }
        
    @Override
    public void startDocument() throws SAXException {
    	
    } 
    
    @Override
    public void endDocument() throws SAXException {
    } 
    
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    	
    	//Log.w(TAG, "startElement: " + localName.trim());
    	
    	if(localName.trim().equals(Common.ITEM_TAG)) {
    		inItem = true;
    		currentItem = new RSSItem(itemtype);
    		//Log.w(TAG, "Item created");
    	}
    	else if(localName.trim().equals(Common.IMAGE_TAG)) {
    		try {
				currentItem.imageUrl = new URL(atts.getValue("url"));
			} catch (MalformedURLException e) {
				Log.e(TAG, "MalformedURLException");
			}
    	}
    } 
    
    @Override
    public void characters(char ch[], int start, int length) {
    	
    	String str = new String(ch).substring(start, start + length);
    	
    	//Log.i(TAG, "str = " + str.trim());
        		
    	sb.append(str.trim());

    }
    
    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    	
		String ss = sb.toString();
    	//Log.w(TAG, "endElement: " + localName.trim() + " sb: " + ss);

    	if(localName.trim().equals(Common.ITEM_TAG)) {
    		
    		if(inItem)
    			parsedDate.add(currentItem);
    			
    		inItem = false;
    	}
    	else
    		if(localName.trim().equals(Common.TITLE_TAG)){
    			if(inItem)
    				currentItem.title = ss;
    		}
    		else
    			if(localName.trim().equals(Common.DESCRIPTION_TAG)) {     		
    				if(inItem)
    					currentItem.description = ss;
    			}
    			else
					if(localName.trim().equals(Common.DATE_TAG)) {
			       		try {
		        			currentItem.pubDate = new Date(Date.parse(ss));
		        		} catch (Exception e) {
		        			Log.e(TAG, "Can't convert date: " + ss);
		        			currentItem.pubDate = new Date(Date.UTC(110, 0, 0, 0, 0, 0));
		        		}
					}
					else
						if(localName.trim().equals(Common.FULLTEXT_TAG))
	    					currentItem.fulltext = ss;
						else
							if(localName.trim().equals(Common.RUBRIC_TAG))
		    					currentItem.rubric = ss;
							else
								if(inItem && localName.trim().equals(Common.LINK_TAG))
			    					currentItem.mplink = ss;
								else
									if(inItem && localName.trim().equals(Common.BIGBANNER_TAG))
				    					currentItem.bigb = ss;
									else
										if(inItem && localName.trim().equals(Common.LINKBANNER_TAG))
					    					currentItem.clink = ss;

    		    	
    	sb = new StringBuilder();
    }
    
}

