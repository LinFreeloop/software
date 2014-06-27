package activity;
import java.util.ArrayList;
import java.util.List;

import net.Net;

import touchgallery.GalleryUrlActivity;
import view.PopupMenu;
import view.UsedBookListAdapter;

import com.hedy.usedbook.R;

import entity.Constant;
import entity.UsedBookInfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import asynctask.AsyncImageTask;

public class UsedBook_Browse_Fragment extends Fragment 
         implements OnClickListener,OnScrollListener{
	ListView lv_books;
    ImageView iv_back;
    ImageView iv_more;
    EditText et_search;
    Button btn_search;
    PopupMenu menu;
    UsedBookListAdapter adapter;
	LayoutParams params ;
    String[] strs_more = new String[]{"只看供应","只看需求","只看Free!"};   
    int cacheSize=1*1024*1024;
    int lastItem;
	boolean isOffer = true;
	boolean isWant = true;
	boolean isFree = false;
    LruCache<String,Bitmap> mBitmapCache = new LruCache<String,Bitmap>(cacheSize);
    

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.usedbook_browse, container,false);
		lv_books=(ListView)view.findViewById(R.id.lv_usedbook_list);
		iv_more=(ImageView)view.findViewById(R.id.iv_usedbook_title_more);
        iv_back=(ImageView)view.findViewById(R.id.iv_usedbook_title_back);
        et_search=(EditText)view.findViewById(R.id.et_usedbook_search);
        btn_search=(Button)view.findViewById(R.id.btn_usedbook_search);
		menu = new PopupMenu(getActivity());
		return view;
	}
	//这个方法在生命周期中排在onCreateView后，发起请求
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		requestUsedBookInfos(isOffer,isWant,isFree,Constant.SUCCESS);
	}
	private void requestUsedBookInfos(final boolean isOffer,final boolean isWant,final boolean isFree,final int what){
		new Thread(new Runnable(){
			public void run() {
				Object o = Net.getUsedBookInfos(isOffer, isWant, isFree);
				Message msg=new Message();
				if(o!=null){
					msg.what=what;
					msg.obj=o;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}
	private void initialze(){
		params=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 10, 10, 10);
		menu.addItems(strs_more);
		menu.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				switch(position){
				case 0:
					isOffer = true;
					isWant = false;
					isFree = false;
					Toast.makeText(getActivity(), strs_more[0], Toast.LENGTH_SHORT).show();
					break;
				case 1:
					isOffer = false;
					isWant = true;
					isFree = false;
					Toast.makeText(getActivity(),strs_more[1], Toast.LENGTH_SHORT).show();
					break;
				case 2:
					isOffer = true;
					isWant = false;
					isFree = true;
					Toast.makeText(getActivity(), strs_more[2], Toast.LENGTH_SHORT).show();
					break;
				}
				requestUsedBookInfos(isOffer,isWant,isFree,Constant.CLASSIFY);
				menu.dismiss();
			}
		});
		iv_more.setOnClickListener(this);
    	iv_back.setOnClickListener(this);
    	btn_search.setOnClickListener(this);
		lv_books.setAdapter(adapter);
		lv_books.setOnScrollListener(this);
		lv_books.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view, final int position, long arg3) {		
				View footer = view.findViewById(R.id.usedbook_listitem_footer);
				footer.setVisibility(View.VISIBLE);
				adapter.mIsOpen.set(position,!adapter.mIsOpen.get(position));
				adapter.notifyDataSetChanged();
				//该项有相片
				if(adapter.getData().get(position).isHasPhoto()){
					ArrayList<String> URLs = adapter.getData().get(position).getZippedPhotoURL();
					LinearLayout layout = (LinearLayout)view.	findViewById(R.id.layout_usedbook_listitem_image);
					layout.setGravity(Gravity.CENTER_HORIZONTAL);
					layout.removeAllViews();
					int i=0;
					for(i=0;i<URLs.size();i++){
						ImageView iv=null;
						iv = new ImageView(layout.getContext());
						iv.setLayoutParams(params);
						iv.setOnClickListener(new OnClickListener() {							
							public void onClick(View v) {
								Intent intent = new Intent(getActivity(),GalleryUrlActivity.class);
								intent.putExtra("book", adapter.getData().get(position));
								startActivity(intent);
							}
						});
						layout.addView(iv);
						new AsyncImageTask(URLs.get(i),iv,mBitmapCache).execute("");

					}
					adapter.notifyDataSetChanged();
				}
			}			
		});			
	}

	
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.iv_usedbook_title_back:
			Toast.makeText(getActivity(), "返回主页", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btn_usedbook_search:
			new Thread(new Runnable(){
				public void run() {
					Object o = Net.searchUsedBookInfos(et_search.getText().toString());
				}
			}).start();            
			break;
		case R.id.iv_usedbook_title_more:
			menu.showAsDropDown(iv_more);
			break;
	    }
	}
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastItem=	firstVisibleItem + visibleItemCount;
	}
	public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(lastItem==adapter.getCount()){
			requestUsedBookInfos(isOffer,isWant,isFree,Constant.ADDMORE);
        }
	}
    Handler handler = new Handler(){
    	public void handleMessage(Message msg){
    		switch(msg.what){
    		case Constant.CLASSIFY:
    			adapter.setData((List<UsedBookInfo>)msg.obj);
    			adapter.notifyDataSetChanged();
    			break;
    		case Constant.SEARCH:
    			adapter.setData((List<UsedBookInfo>)msg.obj);
    			adapter.notifyDataSetChanged();
    			break;
    		case Constant.ADDMORE:
    			adapter.addData((List<UsedBookInfo>)msg.obj);
    			adapter.notifyDataSetChanged();
    			break;
    		case Constant.SUCCESS:
    		    List<UsedBookInfo> booklist;
    			booklist=(List<UsedBookInfo>)msg.obj;
    			adapter=new UsedBookListAdapter(getActivity(),booklist);
    			initialze();
    			break;
    		}
    	}
    };
	@Override
	public void onSaveInstanceState(Bundle outState) {
	}
}