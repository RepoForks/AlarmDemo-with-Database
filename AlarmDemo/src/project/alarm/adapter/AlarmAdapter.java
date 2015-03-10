package project.alarm.adapter;

import java.util.ArrayList;
import java.util.List;

import project.alarm.R;
import project.alarm.model.AlarmModel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmAdapter extends BaseAdapter {

	
	
	AlarmStateInterface mlistener;
	Context context;
	List<AlarmModel> alarmlist = new ArrayList<AlarmModel>();
	public AlarmAdapter(Context ctx, List<AlarmModel> alarmlist2) {
		// TODO Auto-generated constructor stub
		this.context = ctx;
		this.alarmlist = alarmlist2;
		mlistener = (AlarmStateInterface)context;
	}

	public void RemoveItem(AlarmModel obj,int position)
	{
		if(obj!=null)
		{
			if(alarmlist!=null)
			{
				alarmlist.remove(position);
			}
		}
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return alarmlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return alarmlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return alarmlist.indexOf(getItem(position));
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		final ViewHolder holder = new ViewHolder();
		if(convertView == null)
		{
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			view = inflater.inflate(R.layout.view_alarmlist, null);
			
//			view.setTag(holder);
		}
		else
		{
			view = convertView;
//			view.getTag();
		}

		holder.AlarmTime = (TextView) view.findViewById(R.id.time);
		holder.moreoptions = (LinearLayout) view.findViewById(R.id.moreoptions);
		holder.IsAlarmOnOff = (Switch) view.findViewById(R.id.alarm_onoff);
		holder.optionlayout = (LinearLayout)view.findViewById(R.id.optionslayout);
		holder.delete = (TextView)view.findViewById(R.id.delete);
		holder.mainalarmlayout = (LinearLayout)view.findViewById(R.id.mainalarmlayout);
		
		final AlarmModel obj = alarmlist.get(position);
		//set data
		holder.AlarmTime.setText(obj.getDisplaytime());
		if(obj.getAlarmStatus() == true)
		{
			holder.IsAlarmOnOff.setChecked(true);
		}
		else
		{
			holder.IsAlarmOnOff.setChecked(false);
		}
		//set click listener
		holder.moreoptions.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mlistener.onMoreOptionClickedListener(obj, holder.optionlayout, holder.mainalarmlayout);
			}
		});
		
		holder.IsAlarmOnOff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mlistener.onSwitchChangeListener(obj);
			}
		});
		
		holder.delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
				mlistener.onDeleteAlarmListener(obj,position);
			}
		});
		
		return view;
	}
	
	static class ViewHolder
	{
		TextView AlarmTime;
		LinearLayout moreoptions;
		Switch IsAlarmOnOff;
		LinearLayout optionlayout;
		TextView delete;
		LinearLayout mainalarmlayout;
	}

}
