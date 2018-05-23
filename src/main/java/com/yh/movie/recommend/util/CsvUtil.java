package com.yh.movie.recommend.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;
import org.apache.mahout.cf.taste.impl.model.file.*;
import org.apache.mahout.cf.taste.impl.neighborhood.*;
import org.apache.mahout.cf.taste.impl.recommender.*;
import org.apache.mahout.cf.taste.impl.similarity.*;
import org.apache.mahout.cf.taste.model.*;
import org.apache.mahout.cf.taste.neighborhood.*;
import org.apache.mahout.cf.taste.recommender.*;
import org.apache.mahout.cf.taste.similarity.*;

import java.io.*;
import java.util.*;

import com.csvreader.CsvWriter;

public class CsvUtil {

	public static Map<String, Long> unameToUidMap = new HashMap<String, Long>(); // size:56134

	public static String writeCSV(List<Map<String, Object>> list) {
		MemoryIDMigrator thingToLong = new MemoryIDMigrator();  //mahout封装的方法将字符串加密成唯一long
		String csvFilePath = "D://csvData/" + System.currentTimeMillis()
				+ ".csv";
		try {
			// 创建CSV写对象 例如:CsvWriter(文件路径，分隔符，编码格式);
			CsvWriter csvWriter = new CsvWriter(csvFilePath, ',',
					Charset.forName("UTF-8"));
			// 写表头
			// String[] csvHeaders = { "用户id", "物品id", "评分" };
			// csvWriter.writeRecord(csvHeaders);
			// 写内容
			for (Map<String, Object> map : list) {
				String uname = map.get("uname").toString();
				Long uid = Math.abs(thingToLong.toLongID(uname)); // 存在负值
				unameToUidMap.put(uname, uid);
				String[] csvContent = { uid.toString(),
						map.get("mid").toString(), map.get("score").toString() };
				csvWriter.writeRecord(csvContent);
			}
			csvWriter.close();
			System.out.println("--------CSV文件已经写入，在--------" + csvFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return csvFilePath;
	}

	public static void calculateUserCF(String csvFilePath, String uname, List<String> midList)
			throws Exception {
		// step:1 构建模型 2 计算相似度 3 查找k紧邻 4 构造推荐引擎
		Long uid = unameToUidMap.get(uname);
		DataModel model = new FileDataModel(new File(csvFilePath));// 文件名一定要是绝对路径
		UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(100,
				similarity, model);  //ThresholdUserNeighborhood
		Recommender recommender = new GenericUserBasedRecommender(model,
				neighborhood, similarity);
		List<RecommendedItem> recommendations = recommender.recommend(uid, 10);// 为用户推荐10个
		for (RecommendedItem recommendation : recommendations) {
			System.out.println(recommendation.getItemID() + ": " + recommendation.getValue());
			midList.add(recommendation.getItemID() + "");
		}
	}
	
	public static void calculateItemCF(String csvFilePath, String uname, List<String> midList)
			throws Exception {
		Long uid = unameToUidMap.get(uname);
		DataModel model = new FileDataModel(new File(csvFilePath));// 文件名一定要是绝对路径
		ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
		Recommender recommender = new GenericItemBasedRecommender(model,similarity);
		List<RecommendedItem> recommendations = recommender.recommend(uid, 20);// 为用户推荐20个
		for (RecommendedItem recommendation : recommendations) {
			System.out.println(recommendation.getItemID() + ": " + recommendation.getValue());
			midList.add(recommendation.getItemID() + "");
		}
	}

}
