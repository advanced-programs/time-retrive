package info.hb.time.retrive.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class RelativeTimeConverter {

	Calendar referenceTime;
	List<String> relativeRegexs;

	public RelativeTimeConverter(Calendar referenceTime, List<String> relativeRegexs) {
		this.referenceTime = referenceTime;
		this.relativeRegexs = relativeRegexs;
	}

	public Calendar convert(String relativeTime, String regex) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(referenceTime.getTimeInMillis());
		String[] substrings = null;
		int delta = 0;
		switch (regex) {
		case "(today|yesterday|tomorrow) at \\d{1,2}(:\\d{1,2})?\\p{Blank}?(am|pm)?\\b":
			if (relativeTime.contains("am")) {
				calendar.set(Calendar.AM_PM, Calendar.AM);
				relativeTime = relativeTime.replace("am", "");
			} else if (relativeTime.contains("pm")) {
				calendar.set(Calendar.AM_PM, Calendar.PM);
				relativeTime = relativeTime.replace("pm", "");
			}
			substrings = relativeTime.split(" ");
			switch (substrings[0]) {
			case "yesterday":
				calendar.add(Calendar.DATE, -1);
				break;
			case "tomorrow":
				calendar.add(Calendar.DATE, 1);
				break;
			default:
				break;
			}
			if (substrings[2].contains(":")) {
				String[] hhmm = substrings[2].split(":");
				calendar.set(Calendar.HOUR, Integer.parseInt(hhmm[0]));
				calendar.set(Calendar.MINUTE, Integer.parseInt(hhmm[1]));
				return calendar;
			} else {
				int hour = Integer.parseInt(substrings[2]);
				if (hour > 0 && hour < 24) {
					calendar.set(Calendar.HOUR, hour);
					return calendar;
				}
				return null;
			}

		case "this (week|month|year)":
			return calendar;

		case "\\b\\d{1,2} in the (morning|evening)":
			substrings = relativeTime.split(" ");
			delta = Integer.parseInt(substrings[0]);
			calendar.set(Calendar.HOUR, delta);
			calendar.set(Calendar.MINUTE, 0);
			if (substrings[3].equals("evening")) {
				calendar.set(Calendar.AM_PM, Calendar.PM);
			}
			return calendar;
		case "\\blast (spring|summer|autumn|fall|winter)":
			calendar.set(Calendar.DATE, 1);
			calendar.add(Calendar.YEAR, -1);
			switch (relativeTime.split(" ")[1]) {
			case "spring":
				calendar.set(Calendar.MONTH, 3);
				break;
			case "summer":
				calendar.set(Calendar.MONTH, 6);
				break;
			case "fall":
			case "autumn":
				calendar.set(Calendar.MONTH, 9);
			case "winter":
				calendar.set(Calendar.MONTH, 0);
			default:
				break;
			}
			return calendar;
		case "spring|summer|fall|autumn|winter":
			calendar.set(Calendar.DATE, 1);
			switch (relativeTime) {
			case "spring":
				calendar.set(Calendar.MONTH, 3);
				break;
			case "summer":
				calendar.set(Calendar.MONTH, 6);
				break;
			case "fall":
			case "autumn":
				calendar.set(Calendar.MONTH, 9);
			case "winter":
				calendar.set(Calendar.MONTH, 0);
			default:
				break;
			}
			return calendar;
		case "\\bthis second\\b":
			return calendar;
		case "\\blast night\\b":
			calendar.add(Calendar.DATE, -1);
			calendar.set(Calendar.HOUR, 9);
			calendar.set(Calendar.AM_PM, Calendar.PM);
			return calendar;
		case "\\b\\d{1,2} month(s?) before now\\b":
			delta = Integer.parseInt(relativeTime.substring(0, 2).trim());
			calendar.set(Calendar.DATE, 1);
			calendar.add(Calendar.MONTH, -1 * delta);
			return calendar;
		case "\\b\\d{1,2} (hour|day|week|month|year)(s?) ((before now)|ago|earlier)\\b":
			substrings = relativeTime.split(" ");
			delta = Integer.parseInt(substrings[0]) * -1;
			switch (substrings[1]) {
			case "hour":
			case "hours":
				calendar.add(Calendar.HOUR, delta);
				return calendar;
			case "day":
			case "days":
				calendar.add(Calendar.DATE, delta);
				return calendar;
			case "week":
			case "weeks":
				calendar.add(Calendar.DATE, delta * 7);
				return calendar;
			case "month":
			case "months":
				calendar.add(Calendar.MONTH, delta);
				return calendar;
			case "year":
			case "years":
				calendar.add(Calendar.YEAR, delta);
				return calendar;
			default:
				break;
			}
		case "\\b\\d{1,2} (hour|day|week|month|year)(s?) ((from now)|hence|later)\\b":
			substrings = relativeTime.split(" ");
			delta = Integer.parseInt(substrings[0]);
			switch (substrings[1]) {
			case "hour":
			case "hours":
				calendar.add(Calendar.HOUR, delta);
				return calendar;
			case "day":
			case "days":
				calendar.add(Calendar.DATE, delta);
				return calendar;
			case "week":
			case "weeks":
				calendar.add(Calendar.DATE, delta * 7);
				return calendar;
			case "month":
			case "months":
				calendar.add(Calendar.MONTH, delta);
				return calendar;
			case "year":
			case "years":
				calendar.add(Calendar.YEAR, delta);
				return calendar;
			default:
				break;
			}
		case "\\bin \\d{1,2} (hour|day|week|month|year)(s?)\\b":
			substrings = relativeTime.split(" ");
			delta = Integer.parseInt(substrings[1]);
			switch (substrings[2]) {
			case "hour":
			case "hours":
				calendar.add(Calendar.HOUR, delta);
				return calendar;
			case "day":
			case "days":
				calendar.add(Calendar.DATE, delta);
				return calendar;
			case "week":
			case "weeks":
				calendar.add(Calendar.DATE, delta * 7);
				return calendar;
			case "month":
			case "months":
				calendar.add(Calendar.MONTH, delta);
				return calendar;
			case "year":
			case "years":
				calendar.add(Calendar.YEAR, delta);
				return calendar;
			default:
				break;
			}
		case "this (monday|mon|tuesday|tue|wednesday|wed|thursday|thu|friday|fri|saturday|sat|sunday|sun)":
			substrings = relativeTime.split(" ");
			switch (substrings[1]) {
			case "monday":
			case "mon":
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				return calendar;
			case "tuesday":
			case "tue":
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
				return calendar;
			case "wednesday":
			case "wed":
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
				return calendar;
			case "thursday":
			case "thu":
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
				return calendar;
			case "friday":
			case "fri":
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
				return calendar;
			case "saturday":
			case "sat":
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
				return calendar;
			case "sunday":
			case "sun":
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				return calendar;
			default:
				break;
			}
		case "\\blast week (monday|mon|tuesday|tue|wednesday|wed|thursday|thu|friday|fri|saturday|sat|sunday|sun)\\b":
			substrings = relativeTime.split(" ");
			calendar.add(Calendar.DATE, -7);
			switch (substrings[2]) {
			case "monday":
			case "mon":
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				return calendar;
			case "tuesday":
			case "tue":
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
				return calendar;
			case "wednesday":
			case "wed":
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
				return calendar;
			case "thursday":
			case "thu":
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
				return calendar;
			case "friday":
			case "fri":
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
				return calendar;
			case "saturday":
			case "sat":
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
				return calendar;
			case "sunday":
			case "sun":
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				return calendar;
			default:
				break;
			}
		case "\\b(monday|mon|tuesday|tue|wednesday|wed|thursday|thu|friday|fri|saturday|sat|sunday|sun) last week\\b":
			substrings = relativeTime.split(" ");
			calendar.add(Calendar.DATE, -7);
			switch (substrings[0]) {
			case "monday":
			case "mon":
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				return calendar;
			case "tuesday":
			case "tue":
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
				return calendar;
			case "wednesday":
			case "wed":
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
				return calendar;
			case "thursday":
			case "thu":
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
				return calendar;
			case "friday":
			case "fri":
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
				return calendar;
			case "saturday":
			case "sat":
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
				return calendar;
			case "sunday":
			case "sun":
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				return calendar;
			default:
				break;
			}
		case "\\b(january|jan|february|feb|march|mar|april|apr|may|june|jun|july|jul|august|aug|september|sep|october|oct|november|nov|december|dec) \\d{1,2} at \\d{1,2}(:\\d{1,2})?\\p{Blank}?(am|pm)?\\b":
			relativeTime = relativeTime.replace(" at", "");
			SimpleDateFormat format = new SimpleDateFormat("MMM dd ha");
			try {
				calendar.setTime(format.parse(relativeTime));
				calendar.set(Calendar.YEAR, referenceTime.get(Calendar.YEAR));
				return calendar;
			} catch (ParseException e) {
				return calendar;
			}
		default:
			break;
		}

		if (relativeTime.contains("today")) {
			return calendar;
		}
		if (relativeTime.contains("yesterday")) {
			calendar.add(Calendar.DATE, -1);
			return calendar;
		}
		if (relativeTime.contains("tomorrow")) {
			calendar.add(Calendar.DATE, 1);
			return calendar;
		}
		if (relativeTime.contains("next month") || relativeTime.contains("a month later")) {
			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DATE, 1);
			return calendar;
		}
		if (relativeTime.contains("last month") || relativeTime.contains("a month ago")) {
			calendar.add(Calendar.MONTH, -1);
			calendar.set(Calendar.DATE, 1);
			return calendar;
		}
		if (relativeTime.contains("next year")) {
			calendar.add(Calendar.YEAR, 1);
			return calendar;
		}
		if (relativeTime.contains("last year") || relativeTime.contains("past year")
				|| relativeTime.contains("year ago") || relativeTime.contains("year earlier")) {
			calendar.add(Calendar.YEAR, -1);
			return calendar;
		}
		if (relativeTime.contains("years ago") || relativeTime.contains("years earlier")) {
			int year = Integer.parseInt(relativeTime.replaceAll("[^0-9]", ""));
			calendar.add(Calendar.YEAR, year * -1);
			return calendar;
		}
		if (relativeTime.contains("years later")) {
			int year = Integer.parseInt(relativeTime.replaceAll("[^0-9]", ""));
			calendar.add(Calendar.YEAR, year);
			return calendar;
		}
		if (relativeTime.contains("months ago") || relativeTime.contains("months earlier")) {
			int year = Integer.parseInt(relativeTime.replaceAll("[^0-9]", ""));
			calendar.add(Calendar.MONTH, year * -1);
			return calendar;
		}
		if (relativeTime.contains("months later")) {
			int year = Integer.parseInt(relativeTime.replaceAll("[^0-9]", ""));
			calendar.add(Calendar.MONTH, year);
			return calendar;
		}
		if (relativeTime.contains("this morning")) {
			calendar.set(Calendar.HOUR, 9);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.AM_PM, Calendar.AM);
			return calendar;
		}

		System.out.println("- Unconverted: " + relativeTime);

		return null;
	}

	public int hash(String regex) {
		int hash = 7;
		for (int i = 0; i < regex.length(); i++) {
			hash = hash * 31 + regex.charAt(i);
		}
		return hash;
	}

}
