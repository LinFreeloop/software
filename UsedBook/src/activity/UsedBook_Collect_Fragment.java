package activity;

import com.hedy.usedbook.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class UsedBook_Collect_Fragment extends Fragment implements OnClickListener{
    ImageView iv_back;

	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.usedbook_collect, container, false);
        iv_back=(ImageView)view.findViewById(R.id.iv_usedbook_title_back);
        initiaize();
		return view;
	}
	private void initiaize(){
		iv_back.setOnClickListener(this);
	}


	public void onClick(View v) {
		switch(v.getId()){
		case R.id.iv_usedbook_title_back:
			Toast.makeText(getActivity(), "返回主页", Toast.LENGTH_SHORT).show();
			break;
	    }
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
	}
}