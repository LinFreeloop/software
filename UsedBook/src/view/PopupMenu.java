package view;

import java.util.ArrayList;

import com.hedy.usedbook.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PopupMenu {
	private ArrayList<String> mItemList = new ArrayList<String>();
	private Context context;
	private PopupWindow popupWindow;
	private ListView listView;

	public PopupMenu(Context context) {
		this.context = context;
		View view = LayoutInflater.from(context).inflate(
				R.layout.usedbook_popupmenu, null);

		// 设置 listview
		listView = (ListView) view.findViewById(R.id.popupmenu_listview);
		listView.setAdapter(new PopAdapter());
		listView.setFocusableInTouchMode(true);
		listView.setFocusable(true);

		popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setTouchable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景(很神奇的)
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);

	}

	// 设置菜单项点击监听器
	public void setOnItemClickListener(OnItemClickListener listener) {
		listView.setOnItemClickListener(listener);
	}

	// 批量添加菜单项
	public void addItems(String[] items) {
		for (String s : items) {
			mItemList.add(s);
		}
	}

	// 单个添加菜单项
	public void addItem(String item) {
		mItemList.add(item);
	}

	// 下拉式 弹出 pop菜单 parent 右下角
	public void showAsDropDown(View parent) {
		popupWindow.showAsDropDown(parent);
		// 刷新状态
		popupWindow.update();
	}

	// 隐藏菜单
	public void dismiss() {
		popupWindow.dismiss();
	}

	// 适配器
	private final class PopAdapter extends BaseAdapter {

		public int getCount() {
			return mItemList.size();
		}

		public Object getItem(int position) {
			return mItemList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.usedbook_popupmenu_item, null);
				holder = new ViewHolder();
				convertView.setTag(holder);

				holder.groupItem = (TextView) convertView
						.findViewById(R.id.tv_popupmenu_item);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.groupItem.setText(mItemList.get(position));

			return convertView;
		}

		private class ViewHolder {
			TextView groupItem;
		}
	}
}
