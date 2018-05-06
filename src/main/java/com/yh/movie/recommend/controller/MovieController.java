package com.yh.movie.recommend.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yh.movie.recommend.service.MovieService;

import static com.yh.movie.recommend.util.JsonHelper.jsonEntity;



@Controller
@RequestMapping(value = "/movie")
public class MovieController {
	
	@Autowired
	private MovieService movieService;
	
	@RequestMapping(value = "userLogin.do")
	public ResponseEntity<String> userLogin(String uname){
		System.out.println("userLogin call");
		
		Map<String, Object> result = new HashMap<String, Object>();
		this.movieService.userLogin(uname);
		result.put("code", 100);
		result.put("msg", "新增用户信息成功");
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "loadPopularList.do")
	public ResponseEntity<String> loadPopularList(Integer start, Integer count){
			System.out.println("loadPopularList call, start:" + start);

			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 100);
			result.put("msg", "查询成功");
			result.put("subjects", this.movieService.loadPopularList(start, count));
			return jsonEntity(result);
	}
	
	@RequestMapping(value = "getMovieDetailsByMid.do")
	public ResponseEntity<String> getMovieDetailsByMid(String mid){
		System.out.println("getMovieDetailsByMid call");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "查询成功");
		result.put("result", this.movieService.getMovieDetailsByMid(mid));
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "loadMovieListByType.do")
	public ResponseEntity<String> loadMovieListByType(String keyword, Integer start, Integer count){
			System.out.println("loadMovieListByType call");

			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 100);
			result.put("msg", "查询成功");
			result.put("subjects", this.movieService.loadMovieListByType(keyword, start, count));
			return jsonEntity(result);
	}
	
	@RequestMapping(value = "loadMovieListByKeyword.do")
	public ResponseEntity<String> loadMovieListByKeyword(String keyword, Integer start, Integer count){
		System.out.println("loadMovieListByKeyword call");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "查询成功");
		result.put("subjects", this.movieService.loadMovieListByKeyword(keyword, start, count));
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "loadTopList.do")
	public ResponseEntity<String> loadTopList(Integer start, Integer count){
		System.out.println("loadTopList call,begin:" + start + ",limit:" +  count);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "查询成功");
		result.put("subjects", this.movieService.loadTopList(start, count));
		return jsonEntity(result);
	}
	
	//获得热门标签，用于解决用户冷启动
	@RequestMapping(value = "getAllType.do")
	public ResponseEntity<String> getAllType(){
		System.out.println("getAllType");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "查询成功");
		result.put("subjects", this.movieService.getAllType());
		return jsonEntity(result);
	}
	
	/**
	 * 决策出所属类型且区分度最大电影,没有作定时任务处理
	 * @param chooseData
	 * @param uname
	 * @return
	 */
	@RequestMapping(value = "getTestMovieList.do")
	public ResponseEntity<String> getTestMovieList(String[] chooseData, String uname){
		System.out.println("getTestMovieList and uname:" + uname );
		
		List<String> typeList = new ArrayList<String>();
		for(int i = 0; i < chooseData.length; i++){
			System.out.println(chooseData[i]);
			String type = chooseData[i].replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "");
			typeList.add(type);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "查询成功");
		result.put("isOld", this.movieService.setIsOldByUname(uname, 1, typeList));
		result.put("subjects", this.movieService.getTestMovieList(typeList));
		return jsonEntity(result);
	}
	
	//新版的getTestMovieList,直接跑完每个类型用来测试的影片存数据库要用直接取
	@RequestMapping(value = "getTestMovieListFast.do")
	public ResponseEntity<String> getTestMovieListFast(String[] chooseData, String uname){
		System.out.println("getTestMovieList and uname:" + uname );
		
		List<String> typeList = new ArrayList<String>();
		for(int i = 0; i < chooseData.length; i++){
			System.out.println(chooseData[i]);
			String type = chooseData[i].replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "");
			typeList.add(type);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "查询成功");
		result.put("isOld", this.movieService.setIsOldByUname(uname, 1, typeList));
		result.put("subjects", this.movieService.getTestMovieListFast(typeList));
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "getIsOldUser.do")
	public ResponseEntity<String> getIsOldUser(String uname){
		System.out.println("getIsOldUser and uname: " + uname);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "查询成功");
		result.put("isOld", this.movieService.getIsOldUser(uname));
		return jsonEntity(result);
	}
	
	//获得是否收藏、评分，并记录到浏览历史表
	@RequestMapping(value = "getIsFavorite.do")
	public ResponseEntity<String> getIsFavorite(String uname, String mid){
		System.out.println("getIsFavorite");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "查询成功");
		result.put("isFavorite", this.movieService.getIsFavorite(uname, mid));
		result.put("userScore", this.movieService.getUserScore(uname, mid));
		result.put("isHistory", this.movieService.addToHistory(uname, mid));
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "deleteFromStore.do")
	public ResponseEntity<String> deleteFromStore(String uname, String mid){
		System.out.println("deleteFromStore");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "取消收藏成功");
		result.put("isFavorite", this.movieService.deleteFromStore(uname, mid));
		return jsonEntity(result);
	}
	
	
	@RequestMapping(value = "addToStore.do")
	public ResponseEntity<String> addToStore(String uname, String mid){
		System.out.println("addToStore");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "收藏成功");
		result.put("isFavorite", this.movieService.addToStore(uname, mid));
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "getStoreByUname.do")
	public ResponseEntity<String> getStoreByUname(String uname){
		System.out.println("getStoreByUname");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "查询成功");
		result.put("subjects", this.movieService.getStoreByUname(uname));
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "judgeMovie.do")
	public ResponseEntity<String> judgeMovie(String uname, String mid, String name, int score){
		System.out.println("judgeMovie");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "评分成功");
		result.put("userScore", this.movieService.judgeMovie(uname, mid, name, score));
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "getHistoryByUname.do")
	public ResponseEntity<String> getHistoryByUname(String uname){
		System.out.println("getHistoryByUname");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "查询成功");
		result.put("subjects", this.movieService.getHistoryByUname(uname));
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "clearMemory.do")
	public ResponseEntity<String> clearMemory(String uname){
		System.out.println("clearMemory");
		
		Map<String, Object> result = new HashMap<String, Object>();
		this.movieService.clearMemory(uname);
		result.put("code", 100);
		result.put("msg", "清除成功");
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "getUserInfo.do")
	public ResponseEntity<String> getUserInfo(String uname){
		System.out.println("getUserInfo");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "获取成功");
		result.put("result", this.movieService.getUserInfo(uname));
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "setUserInfo.do")
	public ResponseEntity<String> setUserInfo(String uname, String name, String gender, String age, String birthday,
			String constellation, String company, String school, String tel, String email, String intro){
		System.out.println("setUserInfo");
		
		Map<String, Object> result = new HashMap<String, Object>();
		this.movieService.setUserInfo(uname, name, gender, age, birthday, constellation, company, school, tel, email, intro);
		result.put("code", 100);
		result.put("msg", "修改成功");
		return jsonEntity(result);
	}
	
	@RequestMapping(value = "addToFeedback.do")
	public ResponseEntity<String> addToFeedback(String uname, String content){
		System.out.println("addToFeedback");
		
		Map<String, Object> result = new HashMap<String, Object>();
		this.movieService.addToFeedback(uname, content);
		result.put("code", 100);
		result.put("msg", "反馈成功");
		return jsonEntity(result);
	}
	
	
	@RequestMapping(value = "getRandomMovie.do")
	public ResponseEntity<String> getRandomMovie(){
		System.out.println("getRandomMovie");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "摇一摇");
		result.put("result", this.movieService.getRandomMovie());
		return jsonEntity(result);
	}
	
	
	//TODO
	@RequestMapping(value = "getRecommendList.do")
	public ResponseEntity<String> getRecommendList(String[] likeList, String[] unknownList, String[] dislikeList, String uname){
		System.out.println("getRecommendList");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 100);
		result.put("msg", "查询成功");
		//result.put("subjects", this.movieService.getRecommendList(uname));
		return jsonEntity(result);
	}
	
	
	
}
