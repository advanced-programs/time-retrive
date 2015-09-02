package info.hb.time.retrive.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeBundle {

	private TYPE timeType;
	private String rawValue;
	private Calendar calendar;
	private SimpleDateFormat dateFormat;
	private String regex;

	public TimeBundle(String rawValue, TYPE timeType, String regex) {
		this.rawValue = rawValue;
		this.timeType = timeType;
		this.regex = regex;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public void setDateFormat(SimpleDateFormat format) {
		this.dateFormat = format;
	}

	public SimpleDateFormat getDateFormat() {
		return this.dateFormat;
	}

	public String getRawValue() {
		return this.rawValue;
	}

	public Calendar getCalendar() {
		return this.calendar;
	}

	public TYPE getType() {
		return this.timeType;
	}

	public String getRegex() {
		return this.regex;
	}

}
