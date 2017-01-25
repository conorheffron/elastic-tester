package com.ironoc.elastic.model;

import org.elasticsearch.common.joda.time.DateTime;

import com.google.gson.annotations.SerializedName;

public class Article {

	private String authour;

	private String content;
	
	@SerializedName("@timestamp")
	private String timestamp;

	public Article(String authour, String content) {
		this.authour = authour;
		this.content = content;
		this.timestamp = new DateTime().toString();
	}

	public String getAuthour() {
		return authour;
	}

	public void setAuthour(String authour) {
		this.authour = authour;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Article [authour=" + authour + ", content=" + content + ", timestamp=" + timestamp + "]";
	}

}
