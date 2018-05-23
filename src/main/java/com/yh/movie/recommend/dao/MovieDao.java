package com.yh.movie.recommend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import com.yh.movie.recommend.util.DBHelper;
import com.yh.movie.recommend.util.JDBCUtil;
import com.yh.movie.recommend.entity.Brief;
import com.yh.movie.recommend.entity.Movie;

@Repository
public class MovieDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public void saveMovie(Movie movie) {
		StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO movie (mid, picUrl,name,director,screenwriter,actor,type,area,language,showTime,runtime,score,people,summary) ")
        .append("VALUE (? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?, ? ) ");
        StringBuffer searchSql = new StringBuffer("select * from movie where mid = ?");
        StringBuffer updateSql = new StringBuffer("update movie set score = ?, people = ? where mid = ?");
		Connection conn=JDBCUtil.getConnection();  
        try {  
        	PreparedStatement serachStmt=conn.prepareStatement(searchSql.toString()); 
        	serachStmt.setString(1, movie.getMid());
        	ResultSet rs = serachStmt.executeQuery(); 
        	if (rs.next()){
        		 PreparedStatement updateStmt=conn.prepareStatement(updateSql.toString()); 
        		 updateStmt.setString(1, ""+movie.getScore());
        		 updateStmt.setString(2, ""+movie.getPeople());
        		 updateStmt.setString(3, movie.getMid());
        		 updateStmt.execute();  
        	} else {
        		 PreparedStatement stmt=conn.prepareStatement(sql.toString()); 
                 stmt.setString(1, movie.getMid());
                 stmt.setString(2, movie.getPicUrl());
                 stmt.setString(3, movie.getName());
                 stmt.setString(4, movie.getDirector());
                 stmt.setString(5, movie.getScreenwriter());
                 stmt.setString(6, movie.getActor());
                 stmt.setString(7, movie.getType());
                 stmt.setString(8, movie.getArea());
                 stmt.setString(9, movie.getLanguage());
                 stmt.setString(10, movie.getShowTime());
                 stmt.setString(11, ""+movie.getRuntime());
                 stmt.setString(12, ""+movie.getScore());
                 stmt.setString(13, ""+movie.getPeople());
                 stmt.setString(14, movie.getSummary());
                 stmt.execute();  
        	}
        	 
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  finally {
        	JDBCUtil.closeConn(conn);
        }
	}
	
//	public void saveMovie(Movie movie){
//	  String mid = movie.getMid();
//      String picUrl = movie.getPicUrl();
//      String name = movie.getName();
//      String director = movie.getDirector();
//      String screenwriter = movie.getScreenwriter();
//      String actor = movie.getActor();
//      String type = movie.getType();
//      String area = movie.getArea();
//      String language = movie.getLanguage();
//      String showTime = movie.getShowTime();
//      String runtime = ""+movie.getRuntime();
//      String score = ""+movie.getScore();
//      String people = ""+movie.getPeople();
//      String summary = movie.getSummary();
//      
//      String sql = "select * from movie where mid = ?";
//      String insertSql = "insert into movie (mid, picUrl, name, director, screenwriter, actor, type, area, language, showTime, runtime, score, people, summary) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//      String updateSql = "update movie set score = ?, people = ? where mid = ?";
//      List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, mid);
//      if (list.size() == 0 || list == null){
//    	  this.jdbcTemplate.update(insertSql, mid, picUrl, name, director, screenwriter, actor, type, area, language, showTime, runtime, score, people, summary);
//      } else {
//    	  this.jdbcTemplate.update(updateSql, score, people); //已存在的更新评分、参评人数。
//    	  System.out.println("更新成功");
//      }
//	}
//	
//	public void saveBrief(Brief brief){
//		String uname = brief.getUname();
//		String mid = brief.getMid();
//		String name = brief.getName();
//		Double score = brief.getScore();
//		String comment = brief.getComment();
//		String commentTime = brief.getCommentTime();
//		
//		String sql = "select * from brief where uname = ? and mid = ?";
//		String insertSql = "insert into brief (uname, mid, name, score, comment, commentTime) values(?,?,?,?,?,?)";
//		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, uname, mid);
//		if (list.size() == 0 || list == null){
//			this.jdbcTemplate.update(insertSql, uname, mid, name, score, comment, commentTime);
//		} else {
//			
//		}
//	}

	public List<Map<String, Object>> loadPopularList(Integer start, Integer count) {
		String sql = "select * from movie order by showTime desc limit ?,?";
		return this.jdbcTemplate.queryForList(sql, start, count);
	}

	public Map<String, Object> getMovieDetailsByMid(String mid) {
		String sql = "select * from movie where mid = ?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, mid);
		if (list == null || list.size() == 0){
			return new HashMap<String, Object>();
		} else {
			return list.get(0);
		}
	}

	public List<Map<String, Object>> loadMovieListByType(String keyword,
			Integer start, Integer count) {
		String sql = "select * from movie where type like '%" + keyword + "%' limit ?,?";
		return this.jdbcTemplate.queryForList(sql, start, count);
	}
	
	public List<Map<String, Object>> loadMovieListByKeyword(String keyword,
			Integer start, Integer count) {
		String sql = "select * from movie where name like '%" + keyword + "%' or director like '%" + keyword + "%' "
				+ " or actor like '%" + keyword + "%' limit ?,?";
		return this.jdbcTemplate.queryForList(sql, start, count);
	}
	
	//top应参评人数达1W人以上
	public List<Map<String, Object>> loadTopList(Integer start, Integer count) {
		String sql = "select * from movie where people > 10000 order by score desc limit ?,?";
		return this.jdbcTemplate.queryForList(sql, start, count);
	}
	
	public List<Map<String, Object>> getAllType() {
		String sql = "select type from movie group by type";
		return this.jdbcTemplate.queryForList(sql);
	}

	public Long getNumByType(String type) {
		String sql = "select count(*) as num from movie where type like '%" + type + "%'";
		return (Long)this.jdbcTemplate.queryForList(sql).get(0).get("num");
	}
	
	public List<Map<String, Object>>  getTestMovieList(List<Integer> midList) {
		String str = midList.toString().replaceAll("\\[", "(").replaceAll("\\]", ")");
		String sql = "select * from movie where mid in " + str ;
		return this.jdbcTemplate.queryForList(sql);
	}

	//不加热门程度的倒序可能导致冷门电影被误判为区分度高的电影
	public List<Map<String, Object>> getAllMovieByType(String type, int i) {
		String sql = "select * from movie where type like '%" + type + "%' order by people desc limit ?";
		return this.jdbcTemplate.queryForList(sql, i);
	}

	//取最活跃的 i个用户名
	public List<Map<String, Object>> getActiveUname(int i) {
		String sql = "select uname, count(1) from brief group by uname order by count(1) desc limit ?";
		return this.jdbcTemplate.queryForList(sql,i);
	}

	public Double getScoreByUnameAndMid(String uname, Integer mid) {
		String sql = "select uname, score from brief where uname = ? and mid = ?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, uname, mid);
		if(list.size() == 0 || list == null || (Float)list.get(0).get("score") == 0L){ //没有评分的默认为5分
			return 5.0;
		} else {
			return Double.parseDouble(list.get(0).get("score").toString());
			
		}
	}

	//一天毫秒数86400000，取360天前相关类型人气最高的3部,  此处有坑：86400000L
	public List<Map<String, Object>> getHotMovieByType(String type) {
		String end = sdf.format(new Date(System.currentTimeMillis()));
		String start = sdf.format(new Date(System.currentTimeMillis() - 360 * 86400000L));
		String sql = "select mid from movie where type like '%" + type + "%' and showTime > ? and showTime < ? order by people desc limit 3";
		return this.jdbcTemplate.queryForList(sql, start, end);
	}

	public int getIsOldUser(String uname) {
		String sql = "select isOld from user where uname = ?";
		String insertSql = "insert into user(uname, isOld, createTimeStamp) values(?,?,?)";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, uname);
		if(list == null || list.size() == 0){
			this.jdbcTemplate.update(insertSql, uname, 0, System.currentTimeMillis());
			return 0;
		} else {
			return (Integer)list.get(0).get("isOld");
		}
	}

	public int setIsOldByUname(String uname, int isOld, List<String> typeList) {
		String getSql = "select * from user where uname = ?";
		String insertSql = "insert into user(uname, isOld, createTimeStamp, interestedType) values(?,?,?,?)";
		String updateSql = "update user set isOld = ?, interestedType = ? where uname = ?";
		String typeStr = String.join(",", typeList);
		List<Map<String, Object>> getList = this.jdbcTemplate.queryForList(getSql, uname);
		if(getList.size() == 0 || getList == null){
			this.jdbcTemplate.update(insertSql, uname, isOld, System.currentTimeMillis(), typeStr);
			return 1;
		} else {
			this.jdbcTemplate.update(updateSql, isOld, typeStr, uname);
			return 1;
		}
	}

	public String getStoreByUname(String uname) {
		String sql = "select * from user where uname = ?";
		Map<String, Object> map = this.jdbcTemplate.queryForList(sql, uname).get(0);
		if (map.get("store") == null || map.get("store").toString().length() == 0){
			return "";
		} else {
			return map.get("store").toString();
		}
	}

	public int setStoreByUname(String uname, String storeStr) {
		String sql = "update user set store = ? where uname = ?";
		return this.jdbcTemplate.update(sql, storeStr, uname);
		
	}

	public int getUserScore(String uname, String mid) {
		String sql = "select score from brief where uname = ? and mid = ?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, uname, mid);
		if (list == null || list.size() == 0){
			return 0;
		} else {
			return ((Float)list.get(0).get("score")).intValue() / 2;
		}
	}

	public int judgeMovie(String uname, String mid, String name, int score) {
		String sql = "select score from brief where uname = ? and mid = ?";
		String insertSql = "insert into brief(uname, name, mid, score, comment, commentTime) values(?,?,?,?,?,?)";
		String updateSql = "update brief set score = ?, commentTime = ? where uname = ? and mid = ?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, uname, mid);
		if (list == null || list.size() == 0){
			this.jdbcTemplate.update(insertSql, uname, name, mid, score*2, "", sdf.format(new Date(System.currentTimeMillis())));
		} else {
			this.jdbcTemplate.update(updateSql, score*2, sdf.format(new Date(System.currentTimeMillis())), uname, mid);
		}
		return score;
	}

	public void addIntoTestMovie(String type, String testStr) {
		String insertSql = "insert into testMovie (type, list) values(?,?)";
		String updateSql = "update testMovie set list = ? where type = ?";
		String sql = "select * from testMovie where type = ?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, type);
		if (list.size() == 0 || list == null){
			this.jdbcTemplate.update(insertSql, type, testStr);
		} else {
			this.jdbcTemplate.update(updateSql, testStr, type);
		}
		
	}

	public String getTestMovieListFastByType(String type) {
		String sql = "select list from testMovie where type = ?";
		return this.jdbcTemplate.queryForList(sql, type).get(0).get("list").toString();
	}

	public List<Map<String, Object>> getStoreMovieList(String storeStr) {
		String str = "(" + storeStr + ")";
		String sql = "select * from movie where mid in " + str;
		return this.jdbcTemplate.queryForList(sql);
	}

	public int addToHistory(String uname, String mid) {
		String sql = "select * from history where uname = ? and mid = ?";
		String insertSql = "insert into history (uname, mid, createTimeStamp) values(?,?,?)";
		String updateSql = "update history set createTimeStamp = ? where uname = ? and mid = ?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, uname, mid);
		if (list == null || list.size() == 0){
			return this.jdbcTemplate.update(insertSql, uname, mid, System.currentTimeMillis());
		} else {
			return this.jdbcTemplate.update(updateSql, System.currentTimeMillis(), uname, mid);
		}
		
		
	}

	public List<Map<String, Object>> getHistoryByUname(String uname) {
		String sql = "select * from history where uname = ? order by createTimeStamp desc limit 100";
		return this.jdbcTemplate.queryForList(sql, uname);
	}

	public void clearMemory(String uname) {
		String clearStoreStr = "update user set isOld = ?, store = ? where uname = ?";
		String clearHistoryStr = "delete from history where uname = ?";
		String clearBriefStr =  "delete from brief where uname = ?";
		this.jdbcTemplate.update(clearStoreStr, 0, "", uname);
		this.jdbcTemplate.update(clearHistoryStr, uname);
		this.jdbcTemplate.update(clearBriefStr, uname);
	}

	public Map<String, Object> getUserInfo(String uname) {
		String sql = "select * from user where uname = ?";
		return this.jdbcTemplate.queryForMap(sql, uname);
	}

	public void setUserInfo(String uname, String name, String gender,
			String age, String birthday, String constellation, String company,
			String school, String tel, String email, String intro) {
		String sql = "update user set name = ?, gender = ?, age = ?, birthday = ?, constellation = ?, company = ?, "
				+ " school = ?, tel = ?, email = ?, intro = ? where uname = ?";
		this.jdbcTemplate.update(sql, name, gender, age, birthday, constellation, company, school, tel, email, intro, uname);
	}

	public void addToFeedback(String uname, String content) {
		String sql = "insert into feedback (uname, content, createTimeStamp) values(?,?,?)";
		this.jdbcTemplate.update(sql, uname, content, System.currentTimeMillis());
	}

	public void userLogin(String uname) {
		String getSql = "select * from user where uname = ?";
		String sql = "insert into user (uname, createTimeStamp) values(?,?)";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(getSql, uname);
		if (list == null || list.size() == 0){
			this.jdbcTemplate.update(sql, uname, System.currentTimeMillis());
		} else {
			System.out.println("用户已存在！");
		}
		
	}

	public String getNameByMid(String mid) {
		String sql = "select name from movie where mid = ?";
		return this.jdbcTemplate.queryForMap(sql, mid).get("name").toString();
	}

	public List<Map<String, Object>> getBriefList() {
		String sql = "select uname, mid, score from brief";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	public List<Map<String, Object>> getRecommentMovieList(List<String> midList) {
		String str = midList.toString().replaceAll("\\[", "(").replaceAll("\\]", ")");
		String sql = "select * from movie where mid in " + str ;
		return this.jdbcTemplate.queryForList(sql);
	}

	public void setIsOld(String uname, int isOld) {
		String sql = "update user set isOld = ? where uname = ?";
		this.jdbcTemplate.update(sql, isOld, uname);
	}
	
}
