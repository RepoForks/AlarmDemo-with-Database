package project.alarm.model;

public class AlarmModel {

private int Id;
public int getId() {
	return Id;
}


public void setId(int id) {
	Id = id;
}


public String getLabel() {
	return Label;
}


public void setLabel(String label) {
	Label = label;
}


public String getTime() {
	return Time;
}


public void setTime(String time) {
	Time = time;
}


public Boolean getVibration() {
	return Vibration;
}


public void setVibration(Boolean vibration) {
	Vibration = vibration;
}


public String getTone() {
	return Tone;
}


public void setTone(String tone) {
	Tone = tone;
}


public Boolean getRepeat() {
	return Repeat;
}


public void setRepeat(Boolean repeat) {
	Repeat = repeat;
}


public String getDisplaytime() {
	return Displaytime;
}


public void setDisplaytime(String displaytime) {
	Displaytime = displaytime;
}


public Boolean getAlarmStatus() {
	return AlarmStatus;
}


public void setAlarmStatus(Boolean alarmStatus) {
	AlarmStatus = alarmStatus;
}


private String Label;
private String Time;
private Boolean Vibration;
private String Tone;
private Boolean Repeat;
private String Displaytime;
private Boolean AlarmStatus;


	public AlarmModel(int id, String label, String time, Boolean vibration, String tone, Boolean repeat,String displaytime,Boolean alarmstatus) {
		// TODO Auto-generated constructor stub
		this.Id = id;
		this.Label = label;
		this.Time = time;
		this.Vibration = vibration;
		this.Tone = tone;
		this.Repeat = repeat;
		this.Displaytime = displaytime;
		this.AlarmStatus = alarmstatus;
	}
	
	
}
