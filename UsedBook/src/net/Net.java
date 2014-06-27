package net;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Constant;
import entity.UsedBookInfo;

public  class Net{
	
	public static HttpEntity publishUsedBookInfo(UsedBookInfo book){
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(Constant.PublishURL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("UserID", book.getUserId()));
		params.add(new BasicNameValuePair("Title", book.getTitle()));
		params.add(new BasicNameValuePair("Content", book.getContent()));
		params.add(new BasicNameValuePair("IsOffer",String.valueOf(book.isOffer())));
		params.add(new BasicNameValuePair("IsWant",String.valueOf(book.isWant())));
		params.add(new BasicNameValuePair("IsFree",String.valueOf(book.isFree())));
		params.add(new BasicNameValuePair("HasPhoto",String.valueOf(book.isHasPhoto())));

        HttpEntity requestEntity = null;
        HttpEntity responseEntity = null;
		try {
			requestEntity = new UrlEncodedFormEntity(params, "utf-8");
	        post.setEntity(requestEntity);
            HttpResponse response = client.execute(post);
            responseEntity = response.getEntity();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return responseEntity;
	}
	
	public static String publishUsedBookPhoto(String usedBookInfoID,File file){
    	SequenceInputStream sis = null;
    	InputStream is1 = null;
    	InputStream is2 = null;

    	String twoHyphens = "--"; 
    	String boundary = "abcdefghij";
    	String CRLF="\r\n";
    	InputStreamEntity entity = null;
    	HttpClient client = new DefaultHttpClient();
    	HttpPost post = new HttpPost(Constant.UpLoadURL);
    	post.addHeader("Charset", "UTF-8");
    	post.addHeader("Content-Type","multipart/form-data;boundary=" + boundary);
	    StringBuilder sb = new StringBuilder();
	    
	    sb.append(twoHyphens+boundary+CRLF);
	    sb.append("Content-Disposition: form-data;"	+ "name=\"file\"; filename=\"" + file.getName() + "\"" + CRLF);
        sb.append("Content-Type: application/octet-stream;charset=UTF-8"+CRLF );
        sb.append(CRLF);
        is1=new ByteArrayInputStream(sb.toString().getBytes());

        FileInputStream fis=null;
        String result=null;
        try {
			fis = new FileInputStream(file);
        	sb.delete(0, sb.length());
        
        	sb.append(CRLF);
	    	sb.append(twoHyphens+boundary+CRLF);
	   		sb.append("Content-Disposition: form-data;"	+ "name=\"InfoID\"" + CRLF);
       		sb.append("Content-Type: text/plain;charset=UTF-8" + CRLF);
        	sb.append(CRLF);
        	sb.append(usedBookInfoID);
        	sb.append(CRLF);
        	sb.append(twoHyphens+boundary+twoHyphens+CRLF);
        
        	is2=new ByteArrayInputStream(sb.toString().getBytes());
        	Vector<InputStream> vector = new Vector<InputStream>();  
        	vector.addElement(is1);  
        	vector.addElement(fis);  
        	vector.addElement(is2);  
        	Enumeration<InputStream> e = vector.elements();
        	sis = new SequenceInputStream(e);

			int a =is1.available();
			int b =fis.available();
			int c =is2.available();
			entity=new InputStreamEntity(sis,a+b+c);
	        post.setEntity(entity);
			client.execute(post);
            sis.close();

		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	
        return result;
	}
	public static HttpEntity deleteUsedBookInfo(String UsedBookInfoID){
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(Constant.PublishURL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("UsedBookInfoID", UsedBookInfoID));

        HttpEntity requestEntity = null;
        HttpEntity responseEntity = null;
		try {
			requestEntity = new UrlEncodedFormEntity(params, "utf-8");
	        post.setEntity(requestEntity);
            HttpResponse response = client.execute(post);
            responseEntity = response.getEntity();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return responseEntity;
	}
	public static  ArrayList<UsedBookInfo> getUsedBookInfos(boolean isOffer,boolean isWant,boolean isFree){
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(Constant.GetInfosURL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("IsOffer", String.valueOf(isOffer)));
		params.add(new BasicNameValuePair("IsWant", String.valueOf(isWant)));
		params.add(new BasicNameValuePair("IsFree", String.valueOf(isFree)));

        HttpEntity requestEntity = null;
        HttpEntity responseEntity = null;
        ArrayList<UsedBookInfo> list = null;
		try {
			requestEntity = new UrlEncodedFormEntity(params, "utf-8");
	        post.setEntity(requestEntity);
            HttpResponse response = client.execute(post);
            String result = null;
            if(response.getStatusLine().getStatusCode()==200){
                responseEntity = response.getEntity();
                result=EntityUtils.toString(response.getEntity(),"UTF-8");
                JSONArray array = new JSONArray(result);                
                list = getInfos(array);
                return list;
            }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static  ArrayList<UsedBookInfo> searchUsedBookInfos(String searchContent){
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(Constant.SearchInfosURL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("SearchContent", searchContent));
        HttpEntity requestEntity = null;
        HttpEntity responseEntity = null;
        ArrayList<UsedBookInfo> list = null;
		try {
			requestEntity = new UrlEncodedFormEntity(params, "utf-8");
	        post.setEntity(requestEntity);
            HttpResponse response = client.execute(post);
            String result = null;
            if(response.getStatusLine().getStatusCode()==200){
                responseEntity = response.getEntity();
                result=EntityUtils.toString(response.getEntity(),"UTF-8");
                JSONArray array = new JSONArray(result);
                list = getInfos(array);
                return list;
            }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static  ArrayList<UsedBookInfo> getMyUsedBookInfos(String UserId){
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(Constant.GetMyInfosURL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("UserID", UserId));
        HttpEntity requestEntity = null;
        HttpEntity responseEntity = null;
        ArrayList<UsedBookInfo> list = null;
		try {
			requestEntity = new UrlEncodedFormEntity(params, "utf-8");
	        post.setEntity(requestEntity);
            HttpResponse response = client.execute(post);
            String result = null;
            if(response.getStatusLine().getStatusCode()==200){
                responseEntity = response.getEntity();
                result=EntityUtils.toString(response.getEntity(),"UTF-8");
                JSONArray array = new JSONArray(result);
                list = getInfos(array);
                return list;
            }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static HttpEntity updateUsedBookInfo(UsedBookInfo book){
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(Constant.PublishURL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("InfoID", book.getInfoId()));
		params.add(new BasicNameValuePair("Title", book.getTitle()));
		params.add(new BasicNameValuePair("Content", book.getContent()));
		params.add(new BasicNameValuePair("IsFree",String.valueOf(book.isFree())));
		params.add(new BasicNameValuePair("IsEffective",String.valueOf(book.isEffective())));
        HttpEntity requestEntity = null;
        HttpEntity responseEntity = null;
		try {
			requestEntity = new UrlEncodedFormEntity(params, "utf-8");
	        post.setEntity(requestEntity);
            HttpResponse response = client.execute(post);
            responseEntity = response.getEntity();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return responseEntity;
	}

	private static ArrayList<UsedBookInfo> getInfos(JSONArray array) throws JSONException{
		ArrayList<UsedBookInfo> list = new ArrayList<UsedBookInfo>();
        for(int i=0;i<array.length();i++){
        	UsedBookInfo book = new UsedBookInfo();
        	JSONObject obj = (JSONObject) array.get(i);
        	book.setInfoId(obj.getString("InfoID"));
        	book.setUserId(obj.getString("UserID"));
        	book.setTitle(obj.getString("Title"));
        	book.setContent(obj.getString("Content"));
        	book.setOffer(obj.getBoolean("IsOffer"));
        	book.setWant(obj.getBoolean("IsWant"));
        	book.setFree(obj.getBoolean("IsFree"));
        	book.setHasPhoto(obj.getBoolean("IsHasPhoto"));
        	book.setCreateDate(new Date(obj.getLong("CreateDate")));             	
        	if(book.isHasPhoto()){
        	    JSONArray originalPhotoURLArray = obj.getJSONArray("OriginalPhotoURL");
        	    JSONArray zipedPhotoURLArray =obj.getJSONArray("ZippedPhotoURL");
        	    ArrayList<String> originalPhotoURL = new ArrayList<String>();
        	    ArrayList<String> zippedPhotoURL = new ArrayList<String>();
        	    for(int j=0;j<originalPhotoURLArray.length();j++){
        	    	originalPhotoURL.add(originalPhotoURLArray.getString(j));
        	    	zippedPhotoURL.add(zipedPhotoURLArray.getString(j));
        	    }
        	    book.setOriginalPhotoURL(originalPhotoURL);
        	    book.setZippedPhotoURL(zippedPhotoURL);
        	}
        	list.add(book);
        }
        return list;
	}
}