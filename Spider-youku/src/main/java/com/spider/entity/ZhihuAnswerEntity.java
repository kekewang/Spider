package com.spider.entity;

import java.io.Serializable;
import java.util.List;

public class ZhihuAnswerEntity implements Serializable {
	
	private String id;

    private String authorName;
	
	private String aid;
	
	private String title;
	
	private String type;
	
	private String voters;
	
	private String content;
	
	private String date;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVoters() {
		return voters;
	}

	public void setVoters(String voters) {
		this.voters = voters;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
