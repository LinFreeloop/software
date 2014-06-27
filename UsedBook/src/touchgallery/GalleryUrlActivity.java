/*
 Copyright (c) 2012 Roman Truba

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial
 portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package touchgallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.Window;
import android.widget.Toast;
import touchgallery.BasePagerAdapter.OnItemChangeListener;
import touchgallery.GalleryViewPager;
import touchgallery.UrlPagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hedy.usedbook.R;

import entity.UsedBookInfo;

public class GalleryUrlActivity extends Activity {
    private GalleryViewPager mViewPager;
    int cacheSize=1*1024*1024;
    LruCache<String,Bitmap> mBitmapCache = new LruCache<String,Bitmap>(cacheSize);
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gallery_url);
        Intent intent = getIntent();
        UsedBookInfo book = (UsedBookInfo) intent.getSerializableExtra("book");
        List<String> items = book.getOriginalPhotoURL();

        UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, items,mBitmapCache);
//        pagerAdapter.setOnItemChangeListener(new OnItemChangeListener()
//		{
//			@Override
//			public void onItemChange(int currentPosition)
//			{
//				Toast.makeText(GalleryUrlActivity.this, "Current item is " + currentPosition, Toast.LENGTH_SHORT).show();
//			}
//		});
        
        mViewPager = (GalleryViewPager)findViewById(R.id.viewer);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(pagerAdapter);
        
    }

}