package view;      

import java.util.ArrayList;
import java.util.List;

import com.hedy.usedbook.R;

import entity.UsedBookInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
    public class UsedBookListAdapter extends BaseAdapter{  
		private LayoutInflater mInflater;
        private List<UsedBookInfo> mList;
        public ArrayList<Boolean> mIsOpen;

        
        public UsedBookListAdapter(Context context,List list){  
            this.mInflater = LayoutInflater.from(context); 
            this.mList=list;
            int length = list.size();            
            this.mIsOpen=new ArrayList<Boolean>();
            for(int i=0;i<length;i++){
            	mIsOpen.add( true);
            }
        }
     
        public int getCount() {  
            return mList.size();  
        }  
  
        public Object getItem(int arg0) {  
            return mList.get(arg0);  
        }  
  
        public long getItemId(int position) {  
            return position;  
        }  
  
        public View getView(int position, View convertView, ViewGroup parent) {  
            if(getCount() == 0){  
                return null;  
            }  
              
            ViewHolder holder = null;  
            if(convertView == null){  
                convertView = mInflater.inflate(R.layout.usedbook_listitem, null);  
                  
                holder = new ViewHolder();  
                holder.iv_isOffer = (ImageView) convertView.findViewById(R.id.iv_usedbook_listitem_isoffer);  
                holder.iv_isFree = (ImageView) convertView.findViewById(R.id.iv_usedbook_listitem_isfree);  
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_usedbook_listitem_title);  
                holder.tv_userid = (TextView) convertView.findViewById(R.id.tv_usedbook_listitem_username);  
                holder.tv_date = (TextView) convertView.findViewById(R.id.tv_usedbook_listitem_date);
                holder.tv_content=(TextView) convertView.findViewById(R.id.tv_usedbook_listitem_content);
                holder.v_content=convertView.findViewById(R.id.usedbook_listitem_footer);
                convertView.setTag(holder);
            }else{  
                holder = (ViewHolder) convertView.getTag();  
            }
            
            UsedBookInfo book = mList.get(position);
            if(book.isFree())
            holder.iv_isFree.setImageResource(R.drawable.free_32);
            else holder.iv_isFree.setImageResource(0);
            holder.tv_title.setText(book.getTitle());  
            holder.tv_userid.setText(book.getUserId());  
            holder.tv_date.setText(DateFormat.format("yyyy-MM-dd kk:mm:ss",book.getCreateDate()));  
            holder.tv_content.setText(book.getContent());
            
            holder.v_content.setVisibility(mIsOpen.get(position)?View.GONE:View.VISIBLE); 
            
            
            
            return convertView;  
        }
        public List<UsedBookInfo> getData() {
			return mList;
		}

		public void setData(List<UsedBookInfo> mList) {
			this.mList = mList;
			for(int i=0;i<mList.size();i++){
				mIsOpen.set(i,true);
			}
		}
        public void addData(List<UsedBookInfo> list) {
			mList.addAll(list);
			for(int i=0;i<list.size();i++){
				mIsOpen.add(true);
			}
		}
        }
        
        class ViewHolder{


			ImageView iv_isOffer;
            ImageView iv_isFree;  
            TextView tv_title;
            TextView tv_userid;
            TextView tv_date;  
            TextView tv_content;
            View v_content;
            
			public View getV_content() {
				return v_content;
			}
			public void setV_content(View v_content) {
				this.v_content = v_content;
			}
            public ImageView getIv_isOffer() {
				return iv_isOffer;
			}
			public void setIv_isOffer(ImageView iv_isOffer) {
				this.iv_isOffer = iv_isOffer;
			}
			public ImageView getIv_isFree() {
				return iv_isFree;
			}
			public void setIv_isFree(ImageView iv_isFree) {
				this.iv_isFree = iv_isFree;
			}
			public TextView getTv_title() {
				return tv_title;
			}
			public void setTv_title(TextView tv_title) {
				this.tv_title = tv_title;
			}
			public TextView getTv_userid() {
				return tv_userid;
			}
			public void setTv_userid(TextView tv_userid) {
				this.tv_userid = tv_userid;
			}
			public TextView getTv_date() {
				return tv_date;
			}
			public void setTv_date(TextView tv_date) {
				this.tv_date = tv_date;
			}
			public TextView getTv_content() {
				return tv_content;
			}
			public void setTv_content(TextView tv_content) {
				this.tv_content = tv_content;
			} 
        }
     
    