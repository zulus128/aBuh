package com.vkassin;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

import com.vkassin.Common.item_type;

public class RSSItem implements Serializable {

	private static final long serialVersionUID = 22L;
	
	public String title;
	public String description;
	public Date pubDate;
	public String fulltext;
	public String rubric;
	public Common.item_type type;
	public URL imageUrl;
	public String ituneslink;
	public String mplink;
	public String clink;
	public String bigb;
	
	public RSSItem(item_type t) {
		
		this.title = "";
		this.description = "";
		this.fulltext = "";
		this.rubric = "";
		this.ituneslink = "";
		this.mplink = "";
		this.clink = "";
		this.bigb = "";
		type = t;//item_type.IT_NONE;
		this.pubDate = new Date(Date.UTC(110, 0, 0, 0, 0, 0));
	}
		
	public String getShortTitle() {
		return title.length() > 48 ? title.substring(0, 45) + "..." : title;
	}
	
	public String getShortContent() {
		return description.length() > 100 ? description.substring(0, 97) + "..." : description;
	}

	public Date getPubDate() {
		return pubDate == null ? new Date(Date.UTC(110, 0, 0, 0, 0, 0)) : pubDate;
	}
	
}

