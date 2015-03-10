package project.alarm.adapter;

import project.alarm.model.AlarmModel;
import android.widget.LinearLayout;

public  interface AlarmStateInterface
{
	public void onMoreOptionClickedListener(AlarmModel obj, LinearLayout layout, LinearLayout mainlayout);
	void onSwitchChangeListener(AlarmModel obj);
	void onDeleteAlarmListener(AlarmModel obj, int position);
}