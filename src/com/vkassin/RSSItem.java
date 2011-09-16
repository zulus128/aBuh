package com.vkassin;

import java.util.Date;

public class RSSItem {
	public static final int MAX_ITEMS = 30;
	
	public String title;
	public String description;
	public Date pubDate;
	
	public RSSItem() {
		this.title = "";
		this.description = "";
		this.pubDate = new Date(Date.UTC(110, 0, 0, 0, 0, 0));
	}
		
	public String getShortTitle() {
		return title.length() > 48 ? title.substring(0, 45) + "..." : title;
	}
	
	public String getShortContent() {
		return description.length() > 70 ? description.substring(0, 67) + "..." : description;
	}
	
	public Date getPubDate() {
		return pubDate == null ? new Date(Date.UTC(110, 0, 0, 0, 0, 0)) : pubDate;
	}
	
}

