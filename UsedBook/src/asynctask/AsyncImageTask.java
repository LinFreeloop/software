package asynctask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import util.MD5;
import entity.Constant;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.ImageView;
public class AsyncImageTask extends AsyncTask<String, Integer, Bitmap> {
	private String imageURL;
	private ImageView mImageView;
	LruCache<String,Bitmap> mBitmapCache;
	
	public AsyncImageTask(String imageURL, ImageView imageView,LruCache<String,Bitmap> cache) {
		this.imageURL = imageURL;
		this.mImageView = imageView;
		this.mBitmapCache=cache;
	}

	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		if (result != null) {
			mImageView.setImageBitmap(result);
			mImageView.requestLayout();
		}
	}

	protected Bitmap doInBackground(String... params) {
		Bitmap bm = null;
		try {
			bm=mBitmapCache.get(imageURL);
			if(bm==null){
			    bm = getImageFromURL(imageURL);
			    mBitmapCache.put(imageURL, bm);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bm;
	}
	public  Bitmap getImageFromURL(String imageURL) throws Exception {
		MD5 md5 = new MD5();
		//文件名是整个URL的MD5，后缀名还是原来的
		String fileName = md5.getMD5(imageURL)
				+ imageURL.substring(imageURL.lastIndexOf("."));
		File file = new File(Constant.TEMPPATH, fileName);
		InputStream is;
		//看文件存在否，在不在都是 从文件读 getBitmapFromFile(file)
		if (file.exists()) {
			return getBitmapFromFile(file);
		} else {
			URL url = new URL(imageURL);
			HttpURLConnection conn = (HttpURLConnection) url
					.openConnection();
			conn.setConnectTimeout(5*1000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				is = conn.getInputStream();
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				is.close();
				fos.close();
				return getBitmapFromFile(file);
			}
			return null;
		}
	}
	
	
	private  Bitmap getBitmapFromFile(File file){
	    if (file == null)return null;
	    //在服务器获得的图片无条件使用
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }
}