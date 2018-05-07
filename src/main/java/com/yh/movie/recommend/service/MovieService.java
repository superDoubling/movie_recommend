package com.yh.movie.recommend.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.yh.movie.recommend.dao.MovieDao;
import com.yh.movie.recommend.util.CsvUtil;
import com.yh.movie.recommend.util.MathUtil;
import com.yh.movie.recommend.util.TestMovieListThread;

@Service
public class MovieService {

	@Autowired
	private MovieDao movieDao;

	@Autowired
	@Qualifier("testMovieListExecutor")
	private ThreadPoolTaskExecutor testMovieListThread;
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public List<Map<String, Object>> loadPopularList(Integer start,
			Integer count) {
		List<Map<String, Object>> list = this.movieDao.loadPopularList(start,
				count);
		for (Map<String, Object> map : list) {
			String type = map.get("type").toString();
			List<String> types = Arrays.asList(type.replaceAll(" ", "").split(
					","));
			if (types.size() > 4) {
				types = types.subList(0, 4);
			}
			map.put("type", types);
		}
		return list;
	}

	public Map<String, Object> getMovieDetailsByMid(String mid) {
		Map<String, Object> map = this.movieDao.getMovieDetailsByMid(mid);
		List<String> actors = Arrays.asList(map.get("actor").toString()
				.replaceAll(" ", "").split(","));
		if (actors.size() > 4) {
			actors = actors.subList(0, 4);
		}
		List<String> languages = Arrays.asList(map.get("language").toString()
				.replaceAll(" ", "").split("/"));
		if (languages.size() > 3) {
			languages = languages.subList(0, 3);
		}
		List<String> types = Arrays.asList(map.get("type").toString()
				.replaceAll(" ", "").split(","));
		map.put("actor", actors);
		map.put("language", languages);
		map.put("type", types);
		return map;
	}

	public List<Map<String, Object>> loadMovieListByType(String keyword,
			Integer start, Integer count) {
		List<Map<String, Object>> list = this.movieDao.loadMovieListByType(
				keyword, start, count);
		for (Map<String, Object> map : list) {
			String type = map.get("type").toString();
			List<String> types = Arrays.asList(type.replaceAll(" ", "").split(
					","));
			if (types.size() > 4) {
				types = types.subList(0, 4);
			}
			map.put("type", types);
		}
		return list;
	}

	public List<Map<String, Object>> loadMovieListByKeyword(String keyword,
			Integer start, Integer count) {
		List<Map<String, Object>> list = this.movieDao.loadMovieListByKeyword(
				keyword, start, count);
		for (Map<String, Object> map : list) {
			String type = map.get("type").toString();
			List<String> types = Arrays.asList(type.replaceAll(" ", "").split(
					","));
			if (types.size() > 4) {
				types = types.subList(0, 4);
			}
			map.put("type", types);
		}
		return list;
	}

	public List<Map<String, Object>> loadTopList(Integer start, Integer count) {
		List<Map<String, Object>> list = this.movieDao
				.loadTopList(start, count);
		for (Map<String, Object> map : list) {
			String type = map.get("type").toString();
			List<String> types = Arrays.asList(type.replaceAll(" ", "").split(
					","));
			if (types.size() > 4) {
				types = types.subList(0, 4);
			}
			map.put("type", types);
		}
		return list;
	}

	public List<Map<String, Object>> getAllType() {
		List<Map<String, Object>> list = this.movieDao.getAllType();
		HashSet<String> set = new HashSet<String>();
		for (Map<String, Object> map : list) {
			String type = map.get("type").toString();
			List<String> types = Arrays.asList(type.replaceAll(" ", "").split(
					","));
			if (types.size() != 0 && type != null) {
				for (String str : types) {
					set.add(str);
				}
			}
			map.put("type", types);
		}
		Long count = 0L;
		String[] strs = { "喜剧", "科幻", "恐怖", "犯罪", "动画", "传记", "纪录片", "冒险",
				"奇幻", "历史", "悬疑", "剧情", "短片", "爱情", "家庭", "战争", "动作" };
		for (String string : strs) {
			count += this.movieDao.getNumByType(string);
			System.out.println(string + ": "
					+ this.movieDao.getNumByType(string).toString());
		}
		System.out.println("total:" + count + set.toString());
		return list;
	}

	/**
	 * 决策出所属类型且区分度最大电影，每个类型4部
	 * 对每个类型标签线程处理，效率提高一倍  耗时9-->13s(2-5个)
	 * @param typeList
	 * @return
	 */
	public List<Map<String, Object>> getTestMovieList(List<String> typeList) {
		List<Integer> midList = new ArrayList<Integer>(); // 区分度最高的电影mid列表
		List<String> userList = new ArrayList<String>(); // 获得最活跃的 i名用户名称列表
		CountDownLatch cdl = new CountDownLatch(typeList.size()); //CountDownLatch计数器保证所有线程执行完毕后继续
		List<Map<String, Object>> activeList = this.movieDao.getActiveUname(50);
		for (Map<String, Object> map : activeList) {
			userList.add(map.get("uname").toString());
		}
		System.out.println("loading...");

		// 获得区分度最高的电影mid，每个type取4部, 粗细粒度可控
		for (String type : typeList) {
			testMovieListThread.execute(new TestMovieListThread(movieDao, type, midList,
					userList, cdl));
		}
		try {
			cdl.await();
		} catch (InterruptedException e) {

		}
		System.out.println("result:" + midList.toString());

		List<Map<String, Object>> list = this.movieDao
				.getTestMovieList(midList);
		for (Map<String, Object> map : list) {
			String type = map.get("type").toString();
			List<String> types = Arrays.asList(type.replaceAll(" ", "").split(","));
			if (types.size() > 4) {
				types = types.subList(0, 4);
			}
			map.put("type", types);
		}
		return list;
	}
	
	public List<Map<String, Object>> getTestMovieListFast(List<String> typeList) {
		List<Integer> midList = new ArrayList<Integer>(); // 区分度最高的电影mid列表
		for (String type : typeList) {
			String testStr = this.movieDao.getTestMovieListFastByType(type);
			List<String> testList = Arrays.asList(testStr.replaceAll(" ", "").split(
					","));
			for(String str : testList) {
				  int i = Integer.parseInt(str);
				  midList.add(i);
			}
		}
		System.out.println("result:" + midList.toString());
		
		List<Map<String, Object>> list = this.movieDao
				.getTestMovieList(midList);
		for (Map<String, Object> map : list) {
			String type = map.get("type").toString();
			List<String> types = Arrays.asList(type.replaceAll(" ", "").split(","));
			if (types.size() > 4) {
				types = types.subList(0, 4);
			}
			map.put("type", types);
		}
		return list;
	}

	public int getIsOldUser(String uname) {
		return this.movieDao.getIsOldUser(uname);
	}

	public int setIsOldByUname(String uname, int isOld, List<String> typeList) {
		return this.movieDao.setIsOldByUname(uname, isOld, typeList);
	}

	public boolean getIsFavorite(String uname, String mid) {
		String storeStr = this.movieDao.getStoreByUname(uname);
		String[] stores = storeStr.split(",");
		for (String str : stores) {
			if(str.equals(mid)){
				return true;
			}
		}
		return false;
	}

	public boolean deleteFromStore(String uname, String mid) {
		String storeStr = this.movieDao.getStoreByUname(uname);
		String[] stores = storeStr.split(",");
		List<String> list = Arrays.asList(stores); //Arrays.asList()返回的是Arrays的内部类ArrayList,而不是java.util.ArrayList
		List<String> storeList = new ArrayList<String>(list);
		for (int i = storeList.size()-1; i >= 0; i--) {
			if(storeList.get(i).equals(mid)){
				storeList.remove(i);
				break;
			}
		}
		storeStr = StringUtils.join(storeList.toArray(), ",");
		this.movieDao.setStoreByUname(uname, storeStr);
		return false;
	}
	
	public boolean addToStore(String uname, String mid) {
		String storeStr = this.movieDao.getStoreByUname(uname);
		String[] stores = storeStr.split(",");
		List<String> list = Arrays.asList(stores);
		List<String> storeList = new ArrayList<String>(list);
		if(storeList.get(0).equals("")){ //防止无数据时生成的是 ,mid
			storeList = new ArrayList<String>();
		}
		storeList.add(mid);
		storeStr = StringUtils.join(storeList.toArray(), ",");
		this.movieDao.setStoreByUname(uname, storeStr);
		return true;
	}

	public int getUserScore(String uname, String mid) {
		return this.movieDao.getUserScore(uname, mid);
	}

	public int judgeMovie(String uname, String mid, String name, int score) {
		return this.movieDao.judgeMovie(uname, mid, name, score);
	}

	public List<Map<String, Object>> getStoreByUname(String uname) {
		String storeStr = this.movieDao.getStoreByUname(uname);
		if(storeStr == null || storeStr.length() == 0){
			return new ArrayList<Map<String, Object>>();
		}
		return this.movieDao.getStoreMovieList(storeStr);
		
	}

	public int addToHistory(String uname, String mid) {
		return this.movieDao.addToHistory(uname, mid);
	}

	public List<Map<String, Object>> getHistoryByUname(String uname) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list = this.movieDao.getHistoryByUname(uname);
		if(list.size() == 0 || list == null){
			new ArrayList<Map<String, Object>>();
		}
		for (Map<String, Object> map : list) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap = this.movieDao.getMovieDetailsByMid(map.get("mid").toString());
			resultMap.put("createTime", sdf.format(new Date((Long)map.get("createTimeStamp"))));
			resultList.add(resultMap);
		}
		return resultList;
	}

	public void clearMemory(String uname) {
		this.movieDao.clearMemory(uname);
	}

	public Map<String, Object> getUserInfo(String uname) {
		return this.movieDao.getUserInfo(uname);
	}

	public void setUserInfo(String uname, String name, String gender,
			String age, String birthday, String constellation, String company,
			String school, String tel, String email, String intro) {
		this.movieDao.setUserInfo(uname, name, gender, age, birthday, constellation, company, school, tel, email, intro);
	}

	public void addToFeedback(String uname, String content) {
		this.movieDao.addToFeedback(uname, content);
	}

	public Map<String, Object> getRandomMovie() {
		List<Map<String, Object>> popularList = this.movieDao.loadPopularList(0, 250);
		List<Map<String, Object>> topList = this.movieDao.loadTopList(0, 250);
		List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
		allList.addAll(popularList);
		allList.addAll(topList);
		return allList.get((int) (Math.random() * allList.size()));
	}

	public void userLogin(String uname) {
		this.movieDao.userLogin(uname);
	}

	public List<Map<String, Object>> getRecommendList(String uname, String[] likeList,
			String[] unknownList, String[] dislikeList) {
		List<String> midList = new ArrayList<String>();
		for (String likeMid : likeList) {
			likeMid = likeMid.replaceAll("\\[", "").replaceAll("\\]", "");
			if(!likeMid.equals("")){
				this.movieDao.judgeMovie(uname, likeMid, this.movieDao.getNameByMid(likeMid), 5);
			}
		}
		for (String unknownMid : unknownList) {
			unknownMid = unknownMid.replaceAll("\\[", "").replaceAll("\\]", "");
			if(!unknownMid.equals("")){
				this.movieDao.judgeMovie(uname, unknownMid, this.movieDao.getNameByMid(unknownMid), 3);
			}
		}
		for (String dislikeMid : dislikeList) {
			dislikeMid = dislikeMid.replaceAll("\\[", "").replaceAll("\\]", "");
			if(!dislikeMid.equals("")){
				this.movieDao.judgeMovie(uname, dislikeMid, this.movieDao.getNameByMid(dislikeMid), 1);
			}
		}
		List<Map<String, Object>> briefList = this.movieDao.getBriefList();
		String csvFilePath = CsvUtil.writeCSV(briefList); //全部导出需要5s多
		System.out.println(CsvUtil.unameToUidMap.size());
		try {
			CsvUtil.calculateUserCF(csvFilePath, uname, midList);
			CsvUtil.calculateItemCF(csvFilePath, uname, midList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<Map<String, Object>> list = this.movieDao.getRecommentMovieList(midList);
		for (Map<String, Object> map : list) {
			String type = map.get("type").toString();
			List<String> types = Arrays.asList(type.replaceAll(" ", "").split(
					","));
			if (types.size() > 4) {
				types = types.subList(0, 4);
			}
			map.put("type", types);
		}
		return list;
	}

	

}
