package project.alarm.database;

import java.io.IOException;

import project.alarm.model.AlarmModel;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DB_CLASS {

	protected static final String TAG = "DB_callSetting";
	private Context mContext;
	private SQLiteDatabase mDb;
	private DatabaseHandler mDbHelper;
	
	public DB_CLASS(Context context)
	{
		this.mContext = context;
		mDbHelper = new DatabaseHandler(mContext);
	}
	
	public DB_CLASS CreateDatabase() throws SQLException
	{
		try {
			mDbHelper.createDataBase();

		} catch (IOException mIOException) {
			Log.e(TAG, mIOException.toString() + " UnableToCreateDatabase");
			throw new Error("UnableToCreateDatabase");
		}
		return this;
		
	}
	
	public DB_CLASS open() throws SQLException
	{
		try {
			mDbHelper.openDataBase();
			mDbHelper.close();
			mDb = mDbHelper.getReadableDatabase();
		} catch (SQLException mSQLException) {
			Log.e(TAG, "open >>" + mSQLException.toString());
			throw mSQLException;
		}
		return this;
	}
	
	public void close() {
		mDbHelper.close();
	}
	
	//Set Alarm To DATABASE 
	public void setAlarm(String label,String time,int vibration,String tone,int repeat,String displaytime,int AlarmStatus)
	{
			ContentValues cv = new ContentValues();
			try {
				cv.put("label", label);
				cv.put("time", time);
				cv.put("vibration", vibration);
				cv.put("tone", tone);
				cv.put("repeat", repeat);
				cv.put("displaytime", displaytime);
				cv.put("alarmstatus", AlarmStatus);
				
				mDb.insert("alarm_table", null, cv);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			finally
			{
				cv = null;
			}
		
	}
	
	//Fetch AlarmInfo From alarm_table
	public Cursor  FetchAlarmTable()
	{
		Cursor cursor =null;
		try {
			cursor = mDb.rawQuery("Select * from alarm_table", null);
			if(cursor !=null)
			{
				cursor.moveToNext();
			}
			return cursor;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	//Update alarm_table for particular Id
	public void UpdateAlarmInfo(AlarmModel obj)
	{
		ContentValues cv = new ContentValues();
		int alarmstat;
		try {
			if(obj.getAlarmStatus() == false) alarmstat =0;
			else alarmstat = 1;
			cv.put("alarmstatus", alarmstat);
			Log.d("Update at Id-->", +obj.getId()+"");
			mDb.update("alarm_table", cv, "id=?", new String[]{String.valueOf(obj.getId())});
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	//Delete alarm info from Alarm_table for partuicular Id
	public void DeleteAlarmInfo(AlarmModel obj)
	{
		ContentValues cv = new ContentValues();
		try {
			mDb.delete("alarm_table", "id=?", new String[]{String.valueOf(obj.getId())});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
