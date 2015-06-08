package com.example.smstracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {
	static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
	private static final String LOG_TAG = "SMSReceiver";
	private SMSDatabaseHelper smsDatabaseHelper;

	@Override
	public void onReceive(Context context, Intent intent) {

		smsDatabaseHelper = SMSDatabaseHelper.getInstance(context);

		if (intent.getAction().equals(ACTION)) {
			Log.d(LOG_TAG, "SMS Received");
			Bundle bundle = intent.getExtras();
			SmsMessage[] msgs = null;
			String str;

			if (bundle != null) {
				// Retrieve the SMS Messages received
				Object[] pdus = (Object[]) bundle.get("pdus");
				msgs = new SmsMessage[1];

				// Convert Object array
				msgs[0] = SmsMessage.createFromPdu((byte[]) pdus[0]);
				// Sender's phone number
				str = msgs[0].getOriginatingAddress().toString();
				boolean messageCount = smsDatabaseHelper
						.UpdateMessageCount(str);
				Log.d("LOG_TAG", str);
			}
		}

	}

}
