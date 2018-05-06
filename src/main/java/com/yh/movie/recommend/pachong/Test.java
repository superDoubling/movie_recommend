package com.yh.movie.recommend.pachong;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.yh.movie.recommend.entity.Movie;
import com.yh.movie.recommend.dao.MovieDao;

public class Test {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static void main(String[] args) {
		
//		Movie movie = new Movie();
//		movie.setMid("1234678");
//		movie.setPicUrl("www.baidu.com");
//		movie.setName("dqwd");
//		movie.setDirector("dqwd");
//		movie.setScreenwriter("dqwd");
//		movie.setActor("dqwd");
//		movie.setType("dqwd");
//		movie.setArea("dqwd");
//		movie.setLanguage("dqwd");
//		movie.setShowTime("dqwd");
//		movie.setRuntime(120);
//		movie.setScore(5.9);
//		movie.setPeople(876541);
//		movie.setSummary("dqwd");
//
//		MovieDao movieDao = new MovieDao();
//		movieDao.saveMovie(movie);// 保存用户信息到数据库
//		System.out.println("success!");
		
		System.out.println(new Date().getTime());
		System.out.println(sdf.format(new Date().getTime()));
	}
}

