package activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;

import net.Net;

import touchgallery.GalleryUrlActivity;
import view.UsedBookListAdapter;

import com.hedy.usedbook.R;

import entity.UsedBookInfo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout.LayoutParams;
import asynctask.AsyncImageTask;

public class UsedBook_Me_Fragment extends Fragment implements 
OnItemLongClickListener,android.view.View.OnClickListener,OnItemClickListener{
    private int longClickPosition=0;
    private int CONTEXTMENUCHOICE=0;
    private static final int ALTERTOPIC=1;
    private static final int FINISHTOPIC=2;
    private static final int DELETETOPIC=3;
    
    ListView lv_books;
    ImageView iv_back;
    LayoutParams params ;
    UsedBookListAdapter adapter;
    List<UsedBookInfo> booklist;
    int cacheSize=1024*1024;
    LruCache<String,Bitmap> mBitmapCache = new LruCache<String,Bitmap>(cacheSize);
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.usedbook_management, container, false);
		lv_books=(ListView)view.findViewById(R.id.lv_usedbook_list);
        iv_back=(ImageView)view.findViewById(R.id.iv_usedbook_title_back);
		initialize();
		return view;
	}
	public void findViews(){
		
	}
	private void initialize(){
		params=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 10, 10, 10);
    	iv_back.setOnClickListener(this);
		booklist = new ArrayList<UsedBookInfo>();
		addItems(booklist);
		adapter=new UsedBookListAdapter(getActivity(), booklist);
		lv_books.setAdapter(adapter);
		lv_books.setOnItemClickListener(this);
		lv_books.setOnItemLongClickListener(this);
        registerForContextMenu(lv_books);
	}
    private void addItems(List<UsedBookInfo> booklist){
    	String content = "中国近现代史纲要是全国高等学校本科生必修的思想政治理论课之一。" +
				"通过学习，要认识近现代中国社会发展和革命、建设、改革的历史进程及其内在的规律性" +
				"了解国史、国情，社科领会历史和人民是怎样选择了马克思主义，选择了共产党，选择了社" +
				"会主义道路选择了改革开放。";
    	for(int i=0;i<10;i++){
    		UsedBookInfo book = new UsedBookInfo();
    		book.setFree(i%2==0);
    		book.setOffer(true);
    		book.setWant(false);
    		book.setEffective(i%2==1);
    		book.setTitle("中国近现代史纲要");
    		book.setUserId("本书编写组");
    		book.setCreateDate(new Date());
    		book.setContent(content);
    		book.setHasPhoto(true);
    		ArrayList<String> zippedPhotoURL = new ArrayList<String>();
    		ArrayList<String> originalPhotoURL = new ArrayList<String>();
    		originalPhotoURL.add("http://media.ufc.tv/fighter_images/Anderson_Silva/AndersonSilva_Headshot.png");
    		zippedPhotoURL.add("http://www.iconpng.com/png/elegant-business_illustrated/wallet.png");
    		book.setZippedPhotoURL(zippedPhotoURL);
    		book.setOriginalPhotoURL(originalPhotoURL);

    		booklist.add(book);
    	}    	
    }
	public void onCreateContextMenu(ContextMenu menu, View v,
	        ContextMenuInfo menuInfo) {
	    menu.add(0, ALTERTOPIC, Menu.NONE, "修改");
	    if(booklist.get(longClickPosition).isEffective())
	        menu.add(0, FINISHTOPIC, Menu.NONE, "结束事务");
	    else
		    menu.add(0, FINISHTOPIC, Menu.NONE, "恢复事务");
	    menu.add(0, DELETETOPIC, Menu.NONE, "删除");
	}
	public boolean onContextItemSelected(final MenuItem item){
		CONTEXTMENUCHOICE=item.getItemId();
 	    final UsedBookInfo book = booklist.get(longClickPosition);

		//编辑主题直接跳
		if(CONTEXTMENUCHOICE==ALTERTOPIC){
			Intent intent = new Intent(getActivity(),UsedBookUpdateActivity.class);
			intent.putExtra("book",book);
            startActivityForResult(intent, 1);
			Toast.makeText(getActivity(),item.getTitle()+""+longClickPosition, Toast.LENGTH_SHORT).show();
		}
        //结束删除问一问
		else{
			//先写监听
			OnClickListener listener = new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if(which==Dialog.BUTTON_POSITIVE){
				 	    switch(item.getItemId()){
				 	     case FINISHTOPIC:
				 	    Toast.makeText(getActivity(),item.getTitle()+""+longClickPosition, Toast.LENGTH_SHORT).show();

//				 	    将原事务的状态取反
//				 	    book.setEffective(!book.isEffective());
//				 	    new Thread(new Runnable(){ 
//				 	    	public void run(){
//				 	    		HttpEntity entity = Net.updateUsedBookInfo(book);
//				 	    }}).start();				 	    
				 	    break;
			         case DELETETOPIC:
				 		Toast.makeText(getActivity(),item.getTitle()+""+longClickPosition, Toast.LENGTH_SHORT).show();
//				 	    new Thread(new Runnable(){ 
//				 	    	public void run(){
//				 	    		HttpEntity entity= Net.deleteUsedBookInfo(book.getInfoId());
//				 	    }}).start();                     
				 		break;
			         }
			         }
				}
			};
			//构造对话框
			AlertDialog dialog = new AlertDialog.Builder(getActivity())
			.setTitle("提示")
			.setMessage("确定"+item.getTitle()+"?")
			.setPositiveButton("确定", listener)
			.setNegativeButton("取消", listener)
			.show();
		}

		return false;
	}

	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		longClickPosition=position;
		return false;
	}
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.iv_usedbook_title_back:
			Toast.makeText(getActivity(), "返回主页", Toast.LENGTH_SHORT).show();
			break;
			}
	}
	public void onItemClick(AdapterView<?> parent, View view, final int position, long arg3) {		
		View footer = view.findViewById(R.id.usedbook_listitem_footer);
		footer.setVisibility(View.VISIBLE);
		adapter.mIsOpen.set(position,!adapter.mIsOpen.get(position));
		adapter.notifyDataSetChanged();
		if(adapter.getData().get(position).isHasPhoto()){
			ArrayList<String> URLs = adapter.getData().get(position).getZippedPhotoURL();
			LinearLayout layout = (LinearLayout)view.	findViewById(R.id.layout_usedbook_listitem_image);
			layout.setGravity(Gravity.CENTER_HORIZONTAL);
			layout.removeAllViews();
			int i=0;
			for(i=0;i<URLs.size();i++){
				ImageView iv=null;
				iv = new ImageView(layout.getContext());
				iv.setOnClickListener(new View.OnClickListener() {					
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),GalleryUrlActivity.class);
						intent.putExtra("book", booklist.get(position));
						startActivity(intent);
					}
				});
				layout.addView(iv);
				new AsyncImageTask(URLs.get(i),iv,mBitmapCache).execute("");
			}
			adapter.notifyDataSetChanged();
		}
     }
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (requestCode == 1&&resultCode==200) {
              Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
		  }
	}
	Handler handler = new Handler(){
		public void handleMessage(Message msg){
			
		}
	};
	@Override
	public void onSaveInstanceState(Bundle outState) {
	}
}