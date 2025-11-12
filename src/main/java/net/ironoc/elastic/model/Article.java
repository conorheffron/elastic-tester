package net.ironoc.elastic.model;

import org.joda.time.DateTime;

import com.google.gson.annotations.SerializedName;

public class Article {

	private String author;

	private String content;
	
	@SerializedName("@timestamp")
	private String timestamp;

	public Article(String author, String content) {
		this.author = author;
		this.content = content;
		this.timestamp = new DateTime().toString();
	}

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
		return "Article [author=" + author + ", content=" + content + ", timestamp=" + timestamp + "]";
	}
}
