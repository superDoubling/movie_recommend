package com.yh.movie.recommend.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;



import org.apache.commons.lang.StringUtils;

import com.yh.movie.recommend.dao.MovieDao;


public class TestMovieListThread extends Thread{

	private MovieDao movieDao;
	private String type;
	private List<Integer> midList;
	private List<String> userList;
	private CountDownLatch cdl;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Integer> getMidList() {
		return midList;
	}

	public void setMidList(List<Integer> midList) {
		this.midList = midList;
	}

	public List<String> getUserList() {
		return userList;
	}

	public void setUserList(List<String> userList) {
		this.userList = userList;
	}


	public CountDownLatch getCdl() {
		return cdl;
	}

	public void setCdl(CountDownLatch cdl) {
		this.cdl = cdl;
	}


	public MovieDao getMovieDao() {
		return movieDao;
	}

	public void setMovieDao(MovieDao movieDao) {
		this.movieDao = movieDao;
	}


	public TestMovieListThread(MovieDao movieDao, String type,
			List<Integer> midList, List<String> userList, CountDownLatch cdl) {
		super();
		this.movieDao = movieDao;
		this.type = type;
		this.midList = midList;
		this.userList = userList;
		this.cdl = cdl;
	}

	@Override
	public void run() {
		try {
			List<Integer> testList = new ArrayList<Integer>();
			Map<Integer, Double> midToVarianceMap = new HashMap<Integer, Double>(); //<mid,方差>
			List<Map<String, Object>> movieList = this.movieDao.getAllMovieByType(type, 1000);
			for (Map<String, Object> map : movieList) {
				Integer mid = (Integer)map.get("mid");
				List<Double> scoreList = new ArrayList<Double>();
				//获得用户群对该电影的评分列表
				for (String uname : userList) {
					scoreList.add(this.movieDao.getScoreByUnameAndMid(uname, mid));
				}
				//计算方差variance
				midToVarianceMap.put(mid, MathUtil.getVariance(scoreList));
			}
			//利用List<Map.Entry<>>对map进行value排序
			List<Map.Entry<Integer, Double>> list  = new ArrayList<Map.Entry<Integer, Double>>(midToVarianceMap.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>(){
				public int compare(Entry<Integer, Double> o1,
	                    Entry<Integer, Double> o2) {
	                return o2.getValue().compareTo(o1.getValue());
	            }
	 
	        });
			for (int i = 0; i < 4; i++){ 
				midList.add(list.get(i).getKey());
				testList.add(list.get(i).getKey());
	            System.out.println(list.get(i).getKey()  +":" + list.get(i).getValue()); 
	        } 
			
			//防止用户一脸懵逼，每个类型选取3部最近热门的相关电影  
			List<Map<String, Object>> hotList = this.movieDao.getHotMovieByType(type);
			for (Map<String, Object> map : hotList) {
				midList.add((Integer)map.get("mid"));
				testList.add((Integer)map.get("mid"));
			}
			
			this.movieDao.addIntoTestMovie(type, StringUtils.join(testList.toArray(), ","));
			cdl.countDown();
			System.out.println("线程数剩余: " + cdl.getCount());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
