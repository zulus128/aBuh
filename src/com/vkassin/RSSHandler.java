package com.vkassin;

import java.util.ArrayList;
import java.util.Date;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.vkassin.RSSItem;

public class RSSHandler extends DefaultHandler {
	
	private static final String TAG = "aBuh.RSSHandler"; 
	
	private ArrayList<RSSItem> parsedDate = new ArrayList<RSSItem>();
	
	// Used to define what elements we are currently in
 /*   private boolean inItem = false;
    private boolean inTitle = false;
    private boolean inDescription = false;
    private boolean inPubDate = false;
    private boolean inLink = false;
    private boolean inAuthor = false;
   */ 

	private RSSItem currentItem;

	private StringBuilder sb = new StringBuilder();
    
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
    	
    	if(localName.trim().equals(Common.ITEM_TAG)) {
//    		inItem = true;
    		currentItem = new RSSItem();
    		Log.w(TAG, "Item created");
    	}
    	else if(localName.trim().equals(Common.IMAGE_TAG)) {
    		//try {
			//	currentItem.imageUrl = new URL(atts.getValue("url"));
			//} catch (MalformedURLException e) {
			//	Log.e(TAG, "MalformedURLException");
			//}
    	}
    } 
    
    @Override
    public void characters(char ch[], int start, int length) {
    	
    	String str = (new String(ch).substring(start, start + length));
        		
    	sb.append(str);

    }
    
    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    	
		Log.w(TAG, "endElement: " + sb.toString());

    	if(localName.trim().equals(Common.ITEM_TAG))
    		parsedDate.add(currentItem);
    	else
    		if(localName.trim().equals(Common.TITLE_TAG))
    			currentItem.title = new String(sb);
    		else
    			if(localName.trim().equals(Common.DESCRIPTION_TAG))     		
    				currentItem.description = new String(sb);
    			else
					if(localName.trim().equals(Common.DATE_TAG)) {
			       		try {
		        			currentItem.pubDate = new Date(Date.parse(new String(sb)));
		        		} catch (Exception e) {
		        			currentItem.pubDate = new Date(Date.UTC(110, 0, 0, 0, 0, 0));
		        		}
					}

    		    	
    	sb = new StringBuilder();
    }
}

