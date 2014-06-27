package activity;

import java.io.File;
import com.hedy.usedbook.R;

import entity.Constant;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class UsedBookActivity extends FragmentActivity implements OnClickListener {
    
    TextView tab_browse;
    TextView tab_publish;
    TextView tab_mine;
    TextView tab_collection;
    
	private FragmentManager mFragmentManager;
	private FragmentTransaction mFragmentTransaction;    
    int fromPosition=0;
    int toPosition=0;
    Fragment mContent=null;
    private Fragment[] fragments = new Fragment[4];
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_usedbook);

		findViews();
		initialize();
	}


    private void findViews(){
    	tab_browse = (TextView)findViewById(R.id.tv_usedbook_tabbar_browse);
        tab_publish= (TextView)findViewById(R.id.tv_usedbook_tabbar_publish);
        tab_mine= (TextView)findViewById(R.id.tv_usedbook_tabbar_mine);
        tab_collection= (TextView)findViewById(R.id.tv_usedbook_tabbar_collection);
    }
    private void initialize(){
    	tab_browse.setOnClickListener(this);
    	tab_publish.setOnClickListener(this);
    	tab_mine.setOnClickListener(this);
    	tab_collection.setOnClickListener(this);
    	fragments[0]= new UsedBook_Browse_Fragment();
    	fragments[1]=new UsedBook_Publish_Fragment();
    	fragments[2]=new UsedBook_Me_Fragment();
    	fragments[3]=new UsedBook_Collect_Fragment();
    	mFragmentManager =getSupportFragmentManager();
    	mFragmentTransaction = mFragmentManager.beginTransaction();
    	mFragmentTransaction.add(R.id.layout_usedbook_frame,fragments[0], null);
    	mFragmentTransaction.commit();
    	
		//设置文件暂存位置
		File file = new File(Constant.TEMPPATH);
		if(!file.exists())file.mkdirs();
    	
    }

	public void onClick(View view) {
		switch(view.getId()){
		case R.id.tv_usedbook_tabbar_browse:
			toPosition=0;
			switchFragment(fragments[fromPosition],fragments[toPosition]);
			fromPosition=toPosition;			break;
		case R.id.tv_usedbook_tabbar_publish:
			toPosition=1;
			switchFragment(fragments[fromPosition],fragments[toPosition]);
			fromPosition=toPosition;			break;
		case R.id.tv_usedbook_tabbar_mine:
			toPosition=2;
			switchFragment(fragments[fromPosition],fragments[toPosition]);
			fromPosition=toPosition;			break;
		case R.id.tv_usedbook_tabbar_collection:
			toPosition=3;
			switchFragment(fragments[fromPosition],fragments[toPosition]);
			fromPosition=toPosition;
			break;
			default:
				break;
		}
		
	} 

    public void switchFragment(Fragment from, Fragment to) {
        if(!from.equals(to)){
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            // 先判断是否被add过
            if (!to.isAdded()) {
            	// 隐藏当前的fragment，add下一个到Activity中
                transaction.hide(from).add(R.id.layout_usedbook_frame, to).commit(); 
            } else {
            	// 隐藏当前的fragment，显示下一个
                transaction.hide(from).show(to).commit(); 
            }
        }
    }


}
