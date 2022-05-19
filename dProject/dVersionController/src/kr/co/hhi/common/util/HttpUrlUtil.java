package kr.co.hhi.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONValue;

import com.google.gson.Gson;

public class HttpUrlUtil {
	public static void main(String args[]) throws JSONException {

//		String url = "http://192.168.0.68:8080/getcontrollerlist";

//		ModbusTCPBean result = new ModbusTCPBean();
//		try {
//			HttpUrlUtil.getGSONFromURL(result, url);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		System.out.println("-------------");
//		System.out.println(result.getId());
//		System.out.println(result.getState());
//		System.out.println(result.getContswitch());
		

	}

	/**
	 * url�� �̿��� body �����͸� ������ �´�
	 * 
	 * @param _url
	 *            url
	 * @param _method
	 *            POST, GET
	 * @return
	 */
	public static String getHttpBodyData(String _url, String _method) {
		String result = "";
		try {

			URL url = new URL(_url);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();

			http.setDefaultUseCaches(false);
			http.setDoInput(true);
			http.setDoOutput(true);
			http.setRequestMethod(_method);
			
			http.setRequestProperty("content-type",	"application/x-www-form-urlencoded");

			StringBuffer buffer = new StringBuffer();

			OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
			PrintWriter writer = new PrintWriter(outStream);
			writer.write(buffer.toString());
			writer.flush();

			InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
			BufferedReader reader = new BufferedReader(tmp);
			StringBuilder builder = new StringBuilder();
			String str;
			while ((str = reader.readLine()) != null) {
				builder.append(str + "\n");
			}
			result = builder.toString();

		} catch (MalformedURLException e) {

		} catch (IOException e) {
		}

		return result;
	}

	//////////////////////////////
	public static JSONObject getJSONFromUrl(String url) throws JSONException {
		InputStream is = null;
		JSONObject jObj = null;
		String json = "";

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();

			if (!true) {
				HttpPost httpPost = new HttpPost(url);

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			} else {
				HttpGet httpGet = new HttpGet(url);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			//1��
			System.out.println("getJSONFromUrl====================================");
			System.out.println(sb.toString());
			System.out.println("====================================");

			json = sb.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			jObj = new JSONObject(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jObj;
	}
	
	//////////////////////////////
	public static org.json.simple.JSONObject getSimpleJSONFromUrl(String url) throws JSONException {
		InputStream is = null;
		org.json.simple.JSONObject jObj = new org.json.simple.JSONObject();
		String json = "";
		
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			
			HttpGet httpGet = new HttpGet(url);
			
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			
			json = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			org.json.simple.JSONArray ja=(org.json.simple.JSONArray) JSONValue.parse(json);
			
			if(ja.size()==0){
				jObj.put("msg", "���� �����ϴ�.");
				return jObj;
			}
			
			jObj=(org.json.simple.JSONObject) ja.get(0);
			
		} catch (ClassCastException cce) {
			//error �޽����� �㶩 �迭�� �ƴ϶� json�̶� ĳ���ÿ��� �߻�
			jObj.put("msg", "���� �����ϴ�.");
			return jObj;
		}
		
		

		return jObj;
	}
	
	public static String[] getArrayFromUrl(String url) throws JSONException {
		InputStream is = null;
		String json = "";
		
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			
			HttpGet httpGet = new HttpGet(url);
			
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			
			json = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		json = json.replace("\"", "");
		json = json.replace("[", "");
		json = json.replace("]", "");
		json = json.replace("\n", "").replace("\r", "");
		String[] jsonArray=json.split(",");
		return jsonArray;
	}

	////////////////////////////////////
	public static String getJSONFromUrlToString(String url) {
		InputStream is = null;
		JSONObject jObj = null;
		String json = "";

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();

			if (!true) {
				HttpPost httpPost = new HttpPost(url);

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			} else {
				HttpGet httpGet = new HttpGet(url);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			//3��
			System.out.println("getJSONFromUrlToString1#####====================================");
			System.out.println(sb.toString());
			System.out.println("2#####====================================");

			json = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}
	
	
	public static void sendToURL(String url) throws Exception {

		HttpParams httpParams = new BasicHttpParams();
		//ConnectionTimeOut, SocketTimeOut ����(ms����)
		HttpConnectionParams.setConnectionTimeout(httpParams, 1800);
		HttpConnectionParams.setSoTimeout(httpParams, 1800);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
		HttpGet httpGet = new HttpGet(url);
		httpClient.execute(httpGet);
		httpClient = null; httpGet = null;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getGSONFromURL(String url) throws Exception {
		
		Gson gson = new Gson();
		List<Map<String, Object>> result = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		
		InputStream is = null;
		HttpParams httpParams = new BasicHttpParams();
		//ConnectionTimeOut, SocketTimeOut ����(ms����)
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity httpEntity = httpResponse.getEntity();
		is = httpEntity.getContent();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		is.close();
		
		org.json.simple.JSONArray ja=(org.json.simple.JSONArray) JSONValue.parse(sb.toString());
		
		if(sb != null){
			for(int i = 0; i < ja.size(); i++){
				result.add(gson.fromJson(ja.get(i).toString(), map.getClass()));
			}
		}
		
		return result;
	}
	
	/**
	 * url�� �̿��� body �����͸� ������ �´�
	 */
	public static String getHttpBodyFromURL(String _url, String _method, String _errMsg) {
		String result = "";
		try {

			InputStream is = null;
			HttpParams httpParams = new BasicHttpParams();
			//ConnectionTimeOut, SocketTimeOut ����(ms����)
			HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
			HttpConnectionParams.setSoTimeout(httpParams, 5000);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
			HttpGet httpGet;
			HttpPost httpPost;
			HttpResponse httpResponse;
			if(_method.equals("POST")) {
				httpPost = new HttpPost(_url);
				httpResponse = httpClient.execute(httpPost);
			}else { // �� �ܿ��� GET��� ����
				httpGet = new HttpGet(_url);
				httpResponse = httpClient.execute(httpGet);
			}
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			if(_errMsg != null) {
				result = _errMsg;
			}else {
				result = e.getMessage();
			}
		}

		return result;
	}


}
