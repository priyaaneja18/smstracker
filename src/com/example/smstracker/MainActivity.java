package com.example.smstracker;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String ACTION_REFRESH_LIST = "com.example.smstracker.REFRESH_LIST";

	private static final String ACTION_SHOW_DETAIL = "com.example.smstracker.SHOW_DETAIL";

	private EditText senderName;
	private Button save;
	private ListView senderList;
	private Context mContext;
	private SMSDatabaseHelper smsDatabaseHelper;

	private Receiver receiver;

	@Override
	protected void onPause() {
		save.setOnClickListener(null);
		mContext.unregisterReceiver(receiver);
		super.onPause();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		senderName = (EditText) findViewById(R.id.sender_id);
		save = (Button) findViewById(R.id.save);
		senderList = (ListView) findViewById(R.id.sender_list);
		smsDatabaseHelper = SMSDatabaseHelper.getInstance(mContext);
		receiver = new Receiver();
	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_REFRESH_LIST);
		intentFilter.addAction(ACTION_SHOW_DETAIL);
		mContext.registerReceiver(receiver, intentFilter);
		showList();
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String senderId = senderName.getText().toString();
				Boolean result = smsDatabaseHelper.insertSender(senderId, 0);
				senderName.setText("");
				if (!result) {
					Toast.makeText(mContext, "Already existed sender",
							Toast.LENGTH_LONG).show();
				}
				showList();
			}
		});
	}

	private class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ACTION_REFRESH_LIST)) {
				showList();
			} else if (intent.getAction().equals(ACTION_SHOW_DETAIL)) {
				String senderId = intent.getStringExtra("senderId");
				Intent detailView = new Intent();
				detailView.putExtra("senderId", senderId);
				detailView.setClass(mContext, DetailView.class);
				startActivity(detailView);
			}

		}

	}

	private void showList() {
		ArrayList<String> senderListItems = smsDatabaseHelper.getAllSender();
		CustomAdapter customAdapter = new CustomAdapter(this, senderListItems);
		senderList.setAdapter(customAdapter);
	}

}
