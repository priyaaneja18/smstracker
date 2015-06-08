package com.example.smstracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailView extends Activity {
	TextView detailedView;
	Intent intent;
	String senderId;
	SMSDatabaseHelper smsDatabaseHelper = SMSDatabaseHelper.getInstance(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.detail_view);
		detailedView = (TextView) findViewById(R.id.detailed_view);
		intent = getIntent();
		senderId = intent.getStringExtra("senderId");
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		int messageCount = smsDatabaseHelper.getSenderMessageCount(senderId);
		detailedView.setText("Total message received from sender " + senderId
				+ " is :" + messageCount);
		super.onResume();
	}

}
