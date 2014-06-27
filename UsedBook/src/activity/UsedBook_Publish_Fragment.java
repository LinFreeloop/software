package activity;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import net.Net;

import com.hedy.usedbook.R;

import entity.Constant;
import entity.UsedBookInfo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

public class UsedBook_Publish_Fragment extends Fragment implements OnClickListener{
    ImageView iv_back;
    EditText et_title;
    EditText et_content;
    Button btn_publish;
    RadioButton radio_want;
    RadioButton radio_offer;
    CheckBox cbox_free;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.usedbook_publish, container, false);
        iv_back=(ImageView)view.findViewById(R.id.iv_usedbook_title_back);
        btn_publish=(Button)view.findViewById(R.id.btn_usedbook_commit);
        et_title=(EditText)view.findViewById(R.id.et_usedbook_topic_title);
        et_content=(EditText)view.findViewById(R.id.et_usedbook_topic_content);
        radio_want=(RadioButton)view.findViewById(R.id.radio_usedbook_topic_want);
        radio_offer=(RadioButton)view.findViewById(R.id.radio_usedbook_topic_offer);
        cbox_free=(CheckBox)view.findViewById(R.id.cbox_usedbook_topic_isFree);
        
        initialze();
        return view;
	}
	private void initialze(){
		iv_back.setOnClickListener(this);
		btn_publish.setOnClickListener(this);
		cbox_free.setEnabled(false);
		radio_want.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){cbox_free.setChecked(false);cbox_free.setEnabled(false);}
				else cbox_free.setEnabled(true);
			}
		});
	}	
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.iv_usedbook_title_back:
			Toast.makeText(getActivity(), "返回主页", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btn_usedbook_commit:
			if(et_title.getText().toString().equals("")||et_content.getText().toString().equals("")){
				Toast.makeText(getActivity(), "标题和内容不能为空", Toast.LENGTH_SHORT).show();
			     return ;
			}
			final UsedBookInfo book = new UsedBookInfo();
//			book.setUserId("陈锦航");
			book.setTitle(et_title.getText().toString());
			book.setContent(et_content.getText().toString());
			book.setFree(cbox_free.isChecked());
			book.setOffer(radio_offer.isChecked());
			book.setWant(radio_want.isChecked());
//			book.setHasPhoto(true);
			new Thread(new Runnable(){
				public void run() {
					HttpEntity entity = Net.publishUsedBookInfo(book);
					Message msg = new Message();
					if(entity!=null){
						try {
							JSONObject object = new JSONObject(EntityUtils.toString(entity));
							if(object!=null){
								msg.what=Constant.SUCCESS;
								msg.obj=object.get("InfoID");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else	 msg.what=Constant.FAIL;
                    handler.sendMessage(msg);
				}				
			}).start();
			break;
	    }
	}
    Handler handler = new Handler(){
    	public void handleMessage(Message msg){
    		switch(msg.what){
    		case Constant.SUCCESS:
    			final String InfoID = (String)msg.obj;
//				new Thread(new Runnable(){
//					public void run() {
//						File file = new File(Environment.getExternalStorageDirectory()+"/苏三 - 八连杀.mp3");
//						Net.publishUsedBookPhoto(InfoID, file);
//					}
//				}).start();

				break;
    		case Constant.FAIL:
    			Toast.makeText(getActivity(), "失败", Toast.LENGTH_SHORT).show();
    			break;
    		}
    	}
    };
	@Override
	public void onSaveInstanceState(Bundle outState) {
	}
}