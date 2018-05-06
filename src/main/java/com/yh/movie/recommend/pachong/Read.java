package com.yh.movie.recommend.pachong;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class Read {
	public static String parse(String str) {
		JsonParser parse = new JsonParser(); // 创建json解析器
		try {
			JsonObject json = (JsonObject) parse.parse(str); // 创建jsonObject对象
			System.out.println("count:" + json.get("count").getAsDouble()); // 将json数据转为为int型的数据
			System.out.println("start:" + json.get("start").getAsString()); // 将json数据转为为String型的数据

			JsonArray subjects = json.get("subjects").getAsJsonArray();
//			JsonObject rating = subjects.get("rating").getAsJsonObject();
//			System.out.println("max:" + rating.get("max").getAsString());
//			System.out.println("title:" + subjects.get("title").getAsString());
			for(int i = 0; i < subjects.size(); i++) {
				JsonObject subject = (JsonObject)subjects.get(i);
				JsonObject rating = subject.get("rating").getAsJsonObject();
				System.out.println("max:" + rating.get("max").getAsString());
				System.out.println("title:" + subject.get("title").getAsString());
			}

		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
		return "success";
	}
}
