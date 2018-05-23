package com.yh.movie.recommend.task;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yh.movie.recommend.pachong.DouBan3;
import com.yh.movie.recommend.service.MovieService;

@Component
public class MovieTask {

	@Autowired
	private MovieService movieService;
	
	@Scheduled(cron = "0 0 1 * * ?") //每天1:00执行
    public void taskCycle() {
        System.out.println("1:00到啦，定时任务启动！");
        new DouBan3().startSpider(); //爬取最新的电影数据
        List<String> typeList = Arrays.asList("恐怖", "科幻", "喜剧", "犯罪", "纪录片", "动画", "传记", "冒险", "历史", "奇幻", "剧情", "悬疑", "短片", "家庭", "战争", "爱情", "动作");
        this.movieService.getTestMovieList(typeList); //训练测试列表 
    }
}
