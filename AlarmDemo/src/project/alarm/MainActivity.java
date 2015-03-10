package project.alarm;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import com.material.widget.CircleButton;

import project.alarm.adapter.AlarmAdapter;
import project.alarm.adapter.AlarmStateInterface;
import project.alarm.database.DB_CLASS;
import project.alarm.database.DatabaseHandler;
import project.alarm.model.AlarmModel;
import project.utils.Constants;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends Activity implements View.OnClickListener,
		AlarmStateInterface {

	private AlarmManagerBroadcasterReceiver alarm;
	static ListView listview_alarm;
	TextView setalarm;
	com.material.widget.CircleButton setalarm_circlebtn;
	Calendar calendar;
	static final int TIME_DIALOG_ID = 0;
	int hour, min;

	// List Items
	List<AlarmModel> alarmlist = new ArrayList<AlarmModel>();
	AlarmAdapter adapter;
	static Boolean AlarmOnonce = false;

	// Database Handler
	DatabaseHandler dbhandler;
	DB_CLASS dbhelper;
	Cursor alarmcursor;

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// Create Database in Oncreate
		dbhandler = new DatabaseHandler(MainActivity.this);
		try {
			dbhandler.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		alarm = new AlarmManagerBroadcasterReceiver();
		calendar = Calendar.getInstance();
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		min = calendar.get(Calendar.MINUTE);

		GetAlarmInfo();
	}

	void init() {
		setalarm = (TextView) findViewById(R.id.set_alarm);
		setalarm_circlebtn = (CircleButton)findViewById(R.id.circle_button);
		listview_alarm = (ListView) findViewById(R.id.alarmlist);
		
		// set click listener
		setalarm.setOnClickListener(this);
		setalarm_circlebtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == setalarm) {
			// display dialog to select time for alarm
			AlarmOnonce = false;
			DisplayAlarmDialog();
		}
		else if(v == setalarm_circlebtn)
		{
			AlarmOnonce = false;
			DisplayAlarmDialog();
		}
	}

	void DisplayAlarmDialog() {
		showDialog(TIME_DIALOG_ID);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog(MainActivity.this, timepickerlistener,
					hour, min, false);

		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener timepickerlistener = new OnTimeSetListener() {
		

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
		
			 if(!AlarmOnonce)
			 {
			hour = hourOfDay;
			min = minute;
			Log.d("Selected Time->>", hourOfDay + ":" + minute);
			// set alarm time to list and update list
			String AM_PM;
			int select_hour, select_minute = 0;
			int am_pm;
			Long SetAlarmTime;
			if (hourOfDay < 12) {
				AM_PM = "AM";
				am_pm = 0;
				select_hour = hourOfDay;
				select_minute = minute;
			} else {
				AM_PM = "PM";
				am_pm = 1;
				select_hour = hourOfDay - 12;
				if(select_hour == 0)
				{
					select_hour = 12;
				}
				select_minute = minute;
			}

			// Calendar cal = new GregorianCalendar();
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					Constants.DateFormatter);
			dateFormat.setTimeZone(cal.getTimeZone());
			cal.set(Calendar.DAY_OF_WEEK, 2);
			cal.set(Calendar.HOUR, hourOfDay);
			cal.set(Calendar.MINUTE, select_minute);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.AM_PM, am_pm);
			System.out.println("--->>>  " + dateFormat.format(cal.getTime()));
			SetAlarmTime = cal.getTimeInMillis();
			String FormattedTime = dateFormat.format(cal.getTime()); // set into
																		// DB

			String SelectedTime = select_hour + ":" + select_minute + " "
					+ AM_PM;
			UpdateAlarmList(SelectedTime, SetAlarmTime, FormattedTime);
			// set calander instance to current time
			calendar = Calendar.getInstance();
			hour = calendar.get(Calendar.HOUR_OF_DAY);
			min = calendar.get(Calendar.MINUTE);
			 AlarmOnonce = true;
			 }
			
		}
	};

	void UpdateAlarmList(String time, Long alarmtime, String formattedtime) {
		// insert alarm info to DB
		InsertAlarmInfo("label", formattedtime, false, "", false, time, false);

		// put data into list and display in listview
		GetAlarmInfo();
	}

	private void GetAlarmInfo() {
		Cursor cursor = null;
		try {
			if (dbhelper == null) {
				dbhelper = new DB_CLASS(MainActivity.this);
			}
			dbhelper.CreateDatabase();
			dbhelper.open();
			cursor = dbhelper.FetchAlarmTable();

			if (alarmlist.size() > 0) {
				alarmlist.clear();
			}
			if (adapter == null) {
				adapter = new AlarmAdapter(MainActivity.this, alarmlist);
			}
			listview_alarm.setAdapter(adapter);

			if (cursor != null && cursor.getCount() > 0) {
				// cursor.moveToFirst();
				do {
					int id = cursor.getInt(cursor.getColumnIndex("id"));
					String label = cursor.getString(cursor
							.getColumnIndex("label"));
					String time = cursor.getString(cursor
							.getColumnIndex("time"));
					int vibration = cursor.getInt(cursor
							.getColumnIndex("vibration"));
					String tone = cursor.getString(cursor
							.getColumnIndex("tone"));
					int repeat = cursor.getInt(cursor.getColumnIndex("repeat"));
					String displayime = cursor.getString(cursor
							.getColumnIndex("displaytime"));
					int Alarmstatus = cursor.getInt(cursor
							.getColumnIndex("alarmstatus"));
					Log.d("Id-->", id + "");
					Log.d("label-->", label);
					Log.d("time-->", time + "");
					Log.d("vibration-->", vibration + "");
					Log.d("tone-->", tone + "");
					Log.d("repeat-->", repeat + "");
					Log.d("displayime-->", displayime + "");

					Boolean vib, rep, stat;
					if (vibration == 0)
						vib = false;
					else
						vib = true;

					if (repeat == 0)
						rep = false;
					else
						rep = true;

					if (Alarmstatus == 0)
						stat = false;
					else
						stat = true;
					alarmlist.add(new AlarmModel(id, label, time, vib, tone,
							rep, displayime, stat));
					adapter.notifyDataSetChanged();

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			dbhelper.close();
			dbhelper = null;
		}
	}

	private void InsertAlarmInfo(String label, String alarmtime,
			boolean vibration, String tone, boolean repeat, String displaytime,
			Boolean AlarmStatus) {
		// TODO Auto-generated method stub
		try {
			int vib, rep, alarmstat;
			if (dbhelper == null) {
				dbhelper = new DB_CLASS(MainActivity.this);
			}
			dbhelper.CreateDatabase();
			dbhelper.open();
			if (vibration == false)
				vib = 0;
			else
				vib = 1;

			if (repeat == false)
				rep = 0;
			else
				rep = 1;

			if (AlarmStatus == false)
				alarmstat = 0;
			else
				alarmstat = 1;

			dbhelper.setAlarm(label, alarmtime, vib, tone, rep, displaytime,
					alarmstat);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			dbhelper.close();
			dbhelper = null;
		}
	}

	@Override
	public void onMoreOptionClickedListener(AlarmModel obj, LinearLayout layout, LinearLayout mainlayout) {
		// TODO Auto-generated method stub

		if(layout.getVisibility() == View.VISIBLE)
		{
			layout.setVisibility(View.GONE);
			mainlayout.setBackgroundColor(getResources().getColor(R.color.screenbg));
		}
		else
		{
			layout.setVisibility(View.VISIBLE);
			mainlayout.setBackgroundColor(getResources().getColor(R.color.screenbg_light));
		}
	}

	@Override
	public void onSwitchChangeListener(AlarmModel obj) {
		// TODO Auto-generated method stub
		if (obj.getAlarmStatus() == false) {
			obj.setAlarmStatus(true);
		} else {
			obj.setAlarmStatus(false);
		}
		// Set Alarm Info To DAtabase
		// Update Alarm for particular Id in alarm_table
		UpdateAlarmInfo(obj);

		if (obj.getAlarmStatus() == true) {
			if (alarm == null) {
				alarm = new AlarmManagerBroadcasterReceiver();
			}
			
			// Temp Data for playing alarm on 7:00 AM for every monday
						//check time of new york
						
			Calendar calendar = Calendar.getInstance();       
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DateFormatter);
            sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
            System.out.println(sdf.format(calendar.getTime()));   
            calendar.set(Calendar.DAY_OF_WEEK, 1);
            calendar.set(Calendar.HOUR, 7);
            calendar.set(Calendar.MINUTE, 00);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.AM_PM, 0);
			
	        
			Long SetAlarmTime = calendar.getTimeInMillis();
			String FormattedTime = obj.getTime();
			Log.e("American/Newyork Time-->>> ", SetAlarmTime+"");
			
			
			//check time on local
			Calendar alarmCalendar1 = Calendar.getInstance(TimeZone.getDefault());
//			
			System.out.println("Hour: " + alarmCalendar1.getTime().getHours());
	        System.out.println("Minutes: " + alarmCalendar1.getTime().getMinutes());
	        
			Long SetAlarmTime1 = alarmCalendar1.getTimeInMillis();
			Log.e("Indian Time-->>> ", SetAlarmTime1+"");
			
			// SimpleDateFormat dateFormat = new
			// SimpleDateFormat(Constants.DateFormatter);
			// Date convertedDate = new Date();
			// try {
			// convertedDate = dateFormat.parse(obj.getTime());
			// } catch (ParseException | java.text.ParseException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// Long timeinmillis = convertedDate.getTime();
			// Log.d("Alarm Time In Millis-->", timeinmillis+"");
			
			alarm.SetAlarm(MainActivity.this, SetAlarmTime);

		} else {
			if (alarm == null) {
				alarm = new AlarmManagerBroadcasterReceiver();
			}
			alarm.CancelAlarm(MainActivity.this);
		}
	}

	void UpdateAlarmInfo(AlarmModel obj) {
		if (obj != null) {
			try {
				if (dbhelper == null) {
					dbhelper = new DB_CLASS(MainActivity.this);
				}
				dbhelper.CreateDatabase();
				dbhelper.open();
				dbhelper.UpdateAlarmInfo(obj);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				dbhelper.close();
				dbhelper = null;
			}
		}
	}

	@Override
	public void onDeleteAlarmListener(AlarmModel obj, int position) {
		// TODO Auto-generated method stub
		if(obj!=null)
		{
			try {
				if (dbhelper == null) {
					dbhelper = new DB_CLASS(MainActivity.this);
				}
				dbhelper.CreateDatabase();
				dbhelper.open();
				dbhelper.DeleteAlarmInfo(obj);
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			finally {
				dbhelper.close();
				dbhelper = null;
				if(adapter!=null)
				{
					adapter.RemoveItem(obj, position);
					adapter.notifyDataSetChanged();
				}
			}
		}
	}
}
