package com.yh.movie.recommend.pachong;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yh.movie.recommend.dao.MovieDao;
import com.yh.movie.recommend.dao.BriefDao;
import com.yh.movie.recommend.entity.Brief;
import com.yh.movie.recommend.entity.Movie;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 豆瓣电影信息小爬虫
 * 
 */
public class DouBan3 implements PageProcessor {
	// 抓取网站的相关配置，包括：编码、抓取间隔、重试次数等
	private Site site = Site.me().setRetryTimes(3).setSleepTime(4000);
	
	private static int num = 1;
	private static int commentNum = 1;
	private MovieDao movieDao = new MovieDao();
	private BriefDao briefDao = new BriefDao();
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * process 方法是webmagic爬虫的核心<br>
	 * 编写抽取【待爬取目标链接】的逻辑代码在html中。
	 */
	@Override
	public void process(Page page) {
		if (page.getUrl().regex("https://movie.douban.com/subject/[0-9]*.*from.*").match()) {
			Movie movie = new Movie();
			String mid = page.getUrl().get();
			mid = mid.substring(33, mid.indexOf("?") - 1);
			String picUrl = page.getHtml().xpath("//*[@id='mainpic']/a/img/@src").get();
			String name = page.getHtml().xpath("//*[@id='content']/h1/span[1]/text()").get();
			String director = page.getHtml().xpath("//*[@id='info']/span[1]/span[2]/a/text()").get();
			// 多个值得用all()
			List<String> screenwriter = page.getHtml().xpath("//*[@id='info']/span[2]/span[2]/a/text()").all();
			List<String> actor = page.getHtml().xpath("//*[@id='info']/span[3]/span[2]/a[@rel='v:starring']/text()")
					.all();
			List<String> type = page.getHtml().xpath("//*[@id='info']/span[@property='v:genre']/text()").all();

			String area = "";
			Pattern patternArea = Pattern.compile(
					"制片国家/地区:</span>.+[\\u4e00-\\u9fa5]+.+[\\u4e00-\\u9fa5]+\\s+<br>|制片国家/地区:</span>.+[\\u4e00-\\u9fa5]+.+\\s+<br>");
			Matcher matcherArea = patternArea.matcher(page.getHtml().get());
			if (matcherArea.find()) {
				area = matcherArea.group().split("</span>")[1].split("<br>")[0].trim();// for example: >制片国家/地区:</span>
																						// 中国大陆 / 香港 <br>
			}

			String language = "";
			Pattern patternLanguage = Pattern.compile(
					"语言:</span>.+[\\u4e00-\\u9fa5]+.+[\\u4e00-\\u9fa5]+\\s+<br>|语言:</span>.+[\\u4e00-\\u9fa5]+.+\\s+<br>");
			Matcher matcherLanguage = patternLanguage.matcher(page.getHtml().get());
			if (matcherLanguage.find()) {
				language = matcherLanguage.group().split("</span>")[1].split("<br>")[0].trim();
			}

			List<String> showTime = page.getHtml()
					.xpath("//*[@id='info']/span[@property='v:initialReleaseDate']/text()").all();
			Integer runtime = 0;
			Pattern pattern = Pattern.compile("[0-9]+");
			String lengthStr = page.getHtml().xpath("//*[@id='info']/span[@property='v:runtime']/text()").get();
			if(lengthStr != null && lengthStr.length() != 0) {
				Matcher matcherRuntime = pattern.matcher(lengthStr.substring(0, lengthStr.indexOf("分")));
				if(matcherRuntime.find()) {
					runtime = Integer.parseInt(matcherRuntime.group());
				}
			}
			Double score = Double
					.parseDouble(page.getHtml().xpath("//*[@id='interest_sectl']/div[1]/div[2]/strong/text()").get());
			Integer people = Integer.parseInt(
					page.getHtml().xpath("//*[@id='interest_sectl']/div[1]/div[2]/div/div[2]/a/span/text()").get());
			List<String> summary = page.getHtml().xpath("//*[@id='link-report']/span[1]/text()").all();

			// 对象赋值
			movie.setMid(mid);
			movie.setPicUrl(picUrl);
			movie.setName(name);
			movie.setDirector(director);
			movie.setScreenwriter(screenwriter.toString().substring(1,screenwriter.toString().length()-1));
			movie.setActor(actor.toString().substring(1,actor.toString().length()-1));
			movie.setType(type.toString().substring(1,type.toString().length()-1));
			movie.setArea(area);
			movie.setLanguage(language);
			movie.setShowTime(showTime.toString().substring(1,showTime.toString().length()-1));
			movie.setRuntime(runtime);
			movie.setScore(score);
			movie.setPeople(people);
			movie.setSummary(summary.toString().substring(1,summary.toString().length()-1).trim());

			page.addTargetRequests(page.getHtml().xpath("//*[@id='recommendations']/div/dl/dd/a/@href").all());
			page.addTargetRequest("https://movie.douban.com/subject/" + mid + "/comments?status=P");
			movieDao.saveMovie(movie);// 保存用户信息到数据库
			System.out.println("num:" + num + " " + movie.toString());// 输出对象
			num++;
			
		} else if (page.getUrl().regex("https://movie.douban.com/subject/[0-9]*.*status=P").match()) {
			Brief brief = new Brief();
			String mid = page.getUrl().get();
			mid = mid.substring(33, mid.indexOf("?") - 9);
			int count = 0;
			while (21 > ++count) {
				String uname = page.getHtml().xpath("//*[@id='comments']/div[" + count + "]/div[2]/h3/span[2]/a/text()").get();
				String name = page.getHtml().xpath("//*[@id=\"content\"]/h1/text()").get();
				String scoreStr = page.getHtml()
						.xpath("//*[@id='comments']/div[" + count + "]/div[2]/h3/span[2]/span[2]/@title").get();
				Double score = 0.0;
				if ("很差".equals(scoreStr)) {
					score = 2.0;
				} else if ("较差".equals(scoreStr)) {
					score = 4.0;
				} else if ("还行".equals(scoreStr)) {
					score = 6.0;
				} else if ("推荐".equals(scoreStr)) {
					score = 8.0;
				} else if ("力荐".equals(scoreStr)) {
					score = 10.0;
				}
				String comment = page.getHtml().xpath("//*[@id='comments']/div[" + count + "]/div[2]/p/text()").get();
				String commentTime = page.getHtml().xpath("//*[@id='comments']/div[" + count + "]/div[2]/h3/span[2]/span[3]/text()").get();
				if(commentTime == null) {
					commentTime = "";
				}
				brief.setUname(uname);
				brief.setName(name.substring(0, name.length()-3));
				brief.setMid(mid);
				brief.setScore(score);
				brief.setComment(comment);
				brief.setCommentTime(commentTime);
				briefDao.saveBrief(brief);
				System.out.println("commentNum" + commentNum + uname + mid + "fen" + score + comment + commentTime);
				commentNum ++;
			}
		} else {
			page.addTargetRequests(page.getHtml()
					.xpath("//*[@id='content']/div/div[2]/div[4]/div[3]/div/div[1]/div/div[2]/a/@href").all());
		}
		
		try {
			new ProxyIP().getProxy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Site getSite() {
		return this.site;
	}
	public static void main(String[] args) {
		try {
			new ProxyIP().getProxy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long startTime, endTime;
		System.out.println("========电影信息小爬虫【启动】喽！=========");
		startTime = new Date().getTime();
		Spider.create(new DouBan3()).addUrl("https://movie.douban.com/subject/26630781/?from=subject-page").thread(5).run();
		endTime = new Date().getTime();
		System.out.println("========电影信息小爬虫【结束】喽！========="+sdf.format(endTime));
		System.out.println("一共爬到" + num + "部电影信息、" + commentNum + "评论,用时为：" + (endTime - startTime) / 1000 + "s");
	}
}