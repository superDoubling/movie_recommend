package com.yh.movie.recommend.entity;

/**
 * 电影详细信息
 * @author zhangyh
 *
 */
public class Movie {

	
	private String mid;//电影条目id号
	private String picUrl;//海报url
	private String name;//电影名
	private String director;//导演
	private String screenwriter;//编剧
	private String actor;//主演
	private String type;//类型
	private String area;//制片国家或地区
	private String language;//语言
	private String showTime;//上映日期
	private Integer runtime;//片长
	private Double score;//评分
	private Integer people;//评价人数
	private String summary;//简介
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getScreenwriter() {
		return screenwriter;
	}
	public void setScreenwriter(String screenwriter) {
		this.screenwriter = screenwriter;
	}
	public String getActor() {
		return actor;
	}
	public void setActor(String actor) {
		this.actor = actor;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getShowTime() {
		return showTime;
	}
	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}
	public Integer getRuntime() {
		return runtime;
	}
	public void setRuntime(Integer runtime) {
		this.runtime = runtime;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public Integer getPeople() {
		return people;
	}
	public void setPeople(Integer people) {
		this.people = people;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Movie [mid=");
		builder.append(mid);
		builder.append(", picUrl=");
		builder.append(picUrl);
		builder.append(", name=");
		builder.append(name);
		builder.append(", director=");
		builder.append(director);
		builder.append(", screenwriter=");
		builder.append(screenwriter);
		builder.append(", actor=");
		builder.append(actor);
		builder.append(", type=");
		builder.append(type);
		builder.append(", area=");
		builder.append(area);
		builder.append(", language=");
		builder.append(language);
		builder.append(", showTime=");
		builder.append(showTime);
		builder.append(", runtime=");
		builder.append(runtime);
		builder.append(", score=");
		builder.append(score);
		builder.append(", people=");
		builder.append(people);
		builder.append(", summary=");
		builder.append(summary);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
}
