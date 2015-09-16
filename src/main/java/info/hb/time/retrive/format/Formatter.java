package info.hb.time.retrive.format;

import info.hb.time.retrive.domain.TimeBundle;
import info.hb.time.retrive.io.IOUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Formatter {

	private static Logger logger = LoggerFactory.getLogger(Formatter.class);

	private List<SimpleDateFormat> formatters = new ArrayList<>();
	private Calendar referenceTime;

	public Formatter(String fileName, Calendar referenceTime) {
		List<String> formatterStr = IOUtils.readDateFormat(fileName);
		for (String format : formatterStr) {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			formatter.setLenient(false);
			formatters.add(formatter);
		}
		this.referenceTime = referenceTime;
	}

	public Calendar format(TimeBundle timeBundle) {
		Calendar resultDate = Calendar.getInstance();
		for (SimpleDateFormat formatter : formatters) {
			resultDate = parse(formatter, timeBundle.getRawValue());
			if (resultDate != null) {
				timeBundle.setDateFormat(formatter);
				relativeHandler(formatter, resultDate);
				return resultDate;
			}
		}
		logger.error("- Can't parse this absolute time: {}", timeBundle.getRawValue());
		return null;
	}

	// used to handle relative-like format, e.g friday 13:00
	private void relativeHandler(SimpleDateFormat formatter, Calendar resultDate) {
		if (formatter.toPattern().equals("E H:m") || formatter.toPattern().equals("E ha")
				|| formatter.toPattern().equals("E")) {
			Calendar tmp = (Calendar) referenceTime.clone();
			int dayDelta = resultDate.get(Calendar.DAY_OF_WEEK) - tmp.get(Calendar.DAY_OF_WEEK);
			tmp.add(Calendar.DATE, dayDelta);
			resultDate.set(Calendar.YEAR, tmp.get(Calendar.YEAR));
			resultDate.set(Calendar.MONTH, tmp.get(Calendar.MONTH));
			resultDate.set(Calendar.DATE, tmp.get(Calendar.DATE));
		}
		if (formatter.toPattern().equals("MMM dd") || formatter.toPattern().equals("M/dd")) {
			resultDate.set(Calendar.YEAR, referenceTime.get(Calendar.YEAR));
		}
		if (formatter.toPattern().equals("ha") || formatter.toPattern().equals("k:mm")) {
			resultDate.set(Calendar.YEAR, referenceTime.get(Calendar.YEAR));
			resultDate.set(Calendar.MONTH, referenceTime.get(Calendar.MONTH));
			resultDate.set(Calendar.DATE, referenceTime.get(Calendar.DATE));
		}
		if (formatter.toPattern().equals("MMM")) {
			resultDate.set(Calendar.YEAR, referenceTime.get(Calendar.YEAR));
		}

	}

	private Calendar parse(SimpleDateFormat sdf, String rawStr) {
		Calendar myCal = Calendar.getInstance();
		try {
			myCal.setTime(sdf.parse(rawStr));
		} catch (ParseException e) {
			myCal = null;
		}
		return myCal;
	}

}
