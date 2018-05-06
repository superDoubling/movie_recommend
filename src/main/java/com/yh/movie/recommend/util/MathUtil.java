package com.yh.movie.recommend.util;

import java.util.List;

public class MathUtil {

	public static Double getVariance(List<Double> score) {
		int m = score.size();
		double sum = 0;
		for (int i = 0; i < m; i++) {// 求和
			sum += score.get(i);
		}
		double dAve = sum / m;// 求平均值
		double dVar = 0;
		for (int i = 0; i < m; i++) {// 求方差
			dVar += (score.get(i) - dAve) * (score.get(i) - dAve);
		}
		return dVar / m;
	}
}
