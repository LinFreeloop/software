package activity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import net.Net;

import com.hedy.usedbook.R;

import entity.UsedBookInfo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class UsedBookUpdateActivity extends Activity {
    EditText et_title;
    EditText et_content;
    Button btn_commit;
    RadioButton radio_want;
    RadioButton radio_offer;
    CheckBox cbox_free;
    UsedBookInfo book;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_usedbook_update);
		Intent intent = getIntent();
		book  = (UsedBookInfo) intent.getSerializableExtra("book");
		System.out.println(book.toString());
		findViews();
		initialze();
	}
	void findViews(){
		btn_commit=(Button)findViewById(R.id.btn_usedbook_commit);
        et_title=(EditText)findViewById(R.id.et_usedbook_topic_title);
        et_content=(EditText)findViewById(R.id.et_usedbook_topic_content);
        radio_want=(RadioButton)findViewById(R.id.radio_usedbook_topic_want);
        radio_offer=(RadioButton)findViewById(R.id.radio_usedbook_topic_offer);
        cbox_free=(CheckBox)findViewById(R.id.cbox_usedbook_topic_isFree);
	}
	void initialze(){
		et_title.setText(book.getTitle());
		et_content.setText(book.getContent());
		radio_want.setChecked(book.isWant());
		radio_offer.setChecked(book.isOffer());
		cbox_free.setChecked(book.isFree());
		radio_want.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){cbox_free.setChecked(false);cbox_free.setEnabled(false);}
				else cbox_free.setEnabled(true);
			}
		});
		btn_commit.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				new Thread(new Runnable(){
					public void run(){
//						HttpEntity entity = Net.updateUsedBookInfo(book);
						handler.sendEmptyMessage(1);
					}
				}).start();
			}
		});
	}
	Handler handler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case 1:
				setResult(200);
				finish();
				break;
			case 2:
				break;
			}
		}
	};


}
