package com.yh.movie.recommend.pachong;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Api {

	private static String createSign(String param1, String param2, String param3, String signkey) {
		Map<String, String> paramMap = new HashMap<String, String>();// 创建一个保存参数K,V的map
		paramMap.put("akey1", param1);
		paramMap.put("ckey2", param2);
		paramMap.put("bkey3", param3);
		List<String> paramList = new ArrayList<String>();// 用于排序的list
		for (String key : paramMap.keySet()) {
			paramList.add(key);
		}
		Collections.sort(paramList); // 对list进行排序
		StringBuffer signsb = new StringBuffer();
		for (String str : paramList) { // 将排序后的参数组成字符串
			if (paramMap.get(str) == null || paramMap.get(str).isEmpty() || paramMap.get(str).equals(""))
				continue;
			signsb.append("&" + str + "=" + paramMap.get(str));
		}
		signsb.append(signkey); // 添加私钥
		String signStr = signsb.toString().substring(1, signsb.toString().length());// 减去首位&符号
		System.out.println(signStr);
		return signStr;
	}

	private static String getMD5(byte[] source) {
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
										// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
											// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
											// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
															// >>>
															// 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	private static String getResponse(String requsetUrl, String content) {
		try {
			URL url = new URL(requsetUrl);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setDoOutput(true); // 使用 URL 连接进行输出
			httpConn.setDoInput(true); // 使用 URL 连接进行输入
			httpConn.setUseCaches(false); // 忽略缓存
			httpConn.setRequestMethod("POST"); // 设置URL请求方法
			OutputStream outputStream = httpConn.getOutputStream();
			outputStream.write(content.getBytes("utf-8"));
			outputStream.close();
			BufferedReader responseReader = new BufferedReader(
					new InputStreamReader(httpConn.getInputStream(), "utf-8"));
			String readLine;
			StringBuffer responseSb = new StringBuffer();
			while ((readLine = responseReader.readLine()) != null) {
				responseSb.append(readLine);
			}
			return responseSb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "ERROR";
		}

	}

	public static void main(String[] args) {
		connect("111", "222", "333", "abcde");
	}

	public static void connect(String param1, String param2, String param3, String signkey) {
		StringBuilder param = new StringBuilder();
		param.append("akey1=" + param1 + "&");
		param.append("bkey2=" + param2 + "&");
		param.append("ckey3=" + param3 + "&");
		String sn = createSign(param1, param2, param3, signkey);
		param.append("sn=" + getMD5(sn.getBytes()) + "");
		String str = getResponse("http://api.douban.com/v2/movie/top250", param.toString());
		System.out.println(str);
		Read read = new Read();
		System.out.println(read.parse(str));
	}

}