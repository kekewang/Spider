package com.snm.entity;

import com.slob.dao.annotation.ID;
import com.slob.dao.annotation.Table;
import com.snm.entity.BaseEntity;
import com.slob.dao.annotation.Column;
import java.lang.String;
import com.slob.core.Data;
import java.lang.Integer;

@Table(name = "douban")
public class Douban extends BaseEntity implements Data<Integer> {
public static enum $ implements com.slob.core.Field {
	subject, title, year, mainpic, director, scenarist, actor, category, website, country, language, releaseTime, time, name1, imdburl, score, summary, content, celebrity;
}

private static final long serialVersionUID = 1502926155484914900L;

/*subject*/
@ID
private Integer subject;

/*标题*/
@Column
private String title;

/*年份*/
@Column
private String year;

/*海报*/
@Column
private String mainpic;

/*导演*/
@Column
private String director;

/*编剧*/
@Column
private String scenarist;

/*演员*/
@Column
private String actor;

/*类别*/
@Column
private String category;

/*网站*/
@Column
private String website;

/*国家*/
@Column
private String country;

/*语言*/
@Column
private String language;

/*上映时间*/
@Column
private String releaseTime;

/*时长*/
@Column
private String time;

/*又命名*/
@Column
private String name1;

/*imdb地址*/
@Column
private String imdburl;

/*评分*/
@Column
private String score;

/*内容简介*/
@Column
private String summary;

/*评论*/
@Column
private String content;

/*演员链接*/
@Column
private String celebrity;

public Integer getSubject() {
return this.subject;
};

public void setSubject(Integer subject) {
this.subject = subject;
};

public String getTitle() {
return this.title;
};

public void setTitle(String title) {
this.title = title;
};

public String getYear() {
return this.year;
};

public void setYear(String year) {
this.year = year;
};

public String getMainpic() {
return this.mainpic;
};

public void setMainpic(String mainpic) {
this.mainpic = mainpic;
};

public String getDirector() {
return this.director;
};

public void setDirector(String director) {
this.director = director;
};

public String getScenarist() {
return this.scenarist;
};

public void setScenarist(String scenarist) {
this.scenarist = scenarist;
};

public String getActor() {
return this.actor;
};

public void setActor(String actor) {
this.actor = actor;
};

public String getCategory() {
return this.category;
};

public void setCategory(String category) {
this.category = category;
};

public String getWebsite() {
return this.website;
};

public void setWebsite(String website) {
this.website = website;
};

public String getCountry() {
return this.country;
};

public void setCountry(String country) {
this.country = country;
};

public String getLanguage() {
return this.language;
};

public void setLanguage(String language) {
this.language = language;
};

public String getReleaseTime() {
return this.releaseTime;
};

public void setReleaseTime(String releaseTime) {
this.releaseTime = releaseTime;
};

public String getTime() {
return this.time;
};

public void setTime(String time) {
this.time = time;
};

public String getName1() {
return this.name1;
};

public void setName1(String name1) {
this.name1 = name1;
};

public String getImdburl() {
return this.imdburl;
};

public void setImdburl(String imdburl) {
this.imdburl = imdburl;
};

public String getScore() {
return this.score;
};

public void setScore(String score) {
this.score = score;
};

public String getSummary() {
return this.summary;
};

public void setSummary(String summary) {
this.summary = summary;
};

public String getContent() {
return this.content;
};

public void setContent(String content) {
this.content = content;
};

public String getCelebrity() {
return this.celebrity;
};

public void setCelebrity(String celebrity) {
this.celebrity = celebrity;
};

}
