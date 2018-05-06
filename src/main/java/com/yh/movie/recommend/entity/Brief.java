package com.yh.movie.recommend.entity;

/**
 * 短评
 */
public class Brief {

	private String uname;
	private String mid;
	private String name;
	private Double score;
	private String comment;
	private String commentTime;
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Brief [uname=");
		builder.append(uname);
		builder.append(", mid=");
		builder.append(mid);
		builder.append(", name=");
		builder.append(name);
		builder.append(", score=");
		builder.append(score);
		builder.append(", comment=");
		builder.append(comment);
		builder.append(", commentTime=");
		builder.append(commentTime);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
