package com.ironoc.elastic.model;

public class Article {

	private String authour;

	private String content;

	public Article(String authour, String content) {
		this.authour = authour;
		this.content = content;
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

	@Override
	public String toString() {
		return "Article [authour=" + authour + ", content=" + content + "]";
	}

}
