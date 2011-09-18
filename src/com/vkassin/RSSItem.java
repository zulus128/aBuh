package com.vkassin;

import java.io.Serializable;
import java.util.Date;

public class RSSItem implements Serializable {

	private static final long serialVersionUID = 2L;

	public String title;
	public String description;
	public Date pubDate;
	public String fulltext;
	public String rubric;
	
	public RSSItem() {
		
		this.title = "";
		this.description = "";
		this.fulltext = "";
		this.rubric = "";
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

