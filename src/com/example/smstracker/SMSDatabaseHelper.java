package com.example.smstracker;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SMSDatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "SMS.db";
	public static final String TABLE_NAME = "SMSTrack";
	public static final String COLUMN_NAME = "senderid";
	public static final String COLUMN_MESSAGE_COUNT = "messagecount";
	private static SMSDatabaseHelper smsDatabaseHelper;

	private SMSDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);

	}

	public static SMSDatabaseHelper getInstance(Context context) {
		if (smsDatabaseHelper == null) {
			smsDatabaseHelper = new SMSDatabaseHelper(context);
		}
		return smsDatabaseHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table SMSTrack "
				+ "(senderid text primary key,messagecount integer)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS SMSTrack");
		onCreate(db);

	}

	public synchronized boolean insertSender(String senderId, int messageCount) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(COLUMN_NAME, senderId);
		contentValues.put(COLUMN_MESSAGE_COUNT, messageCount);
		long result = database.insert(TABLE_NAME, null, contentValues);
		if (result == -1) {
			return false;
		}
		database.close();
		return true;
	}

	public synchronized Integer deleteSender(String senderId) {
		SQLiteDatabase database = this.getWritableDatabase();
		return database.delete(TABLE_NAME, "senderid = ? ",
				new String[] { senderId });
	}

	public synchronized int getSenderMessageCount(String senderId) {
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = database.rawQuery(
				"select * from SMSTrack where senderid='" + senderId + "'",
				null);
		cursor.moveToFirst();
		int messageCount = cursor.getInt(cursor
				.getColumnIndex(COLUMN_MESSAGE_COUNT));
		cursor.close();
		database.close();
		return messageCount;
	}

	public synchronized boolean UpdateMessageCount(String senderId) {
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database
				.rawQuery("select * from SMSTrack where senderid=" + "'"
						+ senderId + "'", null);
		if (cursor == null || cursor.getCount() == 0) {
			return false;
		}
		cursor.moveToFirst();
		int messageCount = cursor.getInt(cursor
				.getColumnIndex(COLUMN_MESSAGE_COUNT));
		messageCount++;
		ContentValues contentValues = new ContentValues();
		contentValues.put(COLUMN_MESSAGE_COUNT, messageCount);
		database.update(TABLE_NAME, contentValues, "senderid = ? ",
				new String[] { senderId });
		cursor.close();
		database.close();
		return true;
	}

	public synchronized ArrayList<String> getAllSender() {
		ArrayList<String> senderList = new ArrayList<String>();

		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = database.rawQuery("select * from SMSTrack", null);
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		}
		cursor.moveToFirst();

		while (cursor.isAfterLast() == false) {
			senderList
					.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
			cursor.moveToNext();
		}
		cursor.close();
		database.close();
		return senderList;
	}

}
