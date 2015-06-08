package com.example.smstracker;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
	private static final String ACTION_REFRESH_LIST = "com.example.smstracker.REFRESH_LIST";

	private static final String ACTION_SHOW_DETAIL = "com.example.smstracker.SHOW_DETAIL";

	private Context mContext;
	private ArrayList<String> senderList;
	private static LayoutInflater inflater = null;
	private SMSDatabaseHelper smsDatabaseHelper;

	public CustomAdapter(Context context, ArrayList<String> senderList) {
		mContext = context;
		this.senderList = senderList;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		smsDatabaseHelper = SMSDatabaseHelper.getInstance(mContext);
	}

	@Override
	public int getCount() {
		if(senderList != null) {
		return senderList.size();
		} else
			return -1;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {
		public TextView text;
		public Button delete;
		public Button show;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (convertView == null) {
			view = inflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.text = (TextView) view.findViewById(R.id.number);
			holder.delete = (Button) view.findViewById(R.id.delete);
			holder.show = (Button) view.findViewById(R.id.show);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final String sender = (String) senderList.get(position);
		holder.text.setText(sender);
		holder.delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				smsDatabaseHelper.deleteSender(sender);
				Intent intent = new Intent(ACTION_REFRESH_LIST);
				mContext.sendBroadcast(intent);
			}
		});
		holder.show.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(ACTION_SHOW_DETAIL);
				intent.putExtra("senderId", sender);
				mContext.sendBroadcast(intent);
			}
		});
		return view;
	}

}
