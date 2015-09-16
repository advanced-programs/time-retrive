package info.hb.time.retrive.core;

import info.hb.time.retrive.domain.MatchedTime;
import info.hb.time.retrive.domain.TYPE;
import info.hb.time.retrive.domain.TimeBundle;
import info.hb.time.retrive.format.Formatter;
import info.hb.time.retrive.io.IOUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Process {

	private List<MatchedTime> matchedTimes;
	private List<TimeBundle> timeBundles;
	//	private List<String> relativeRegexs;
	private Formatter formatter;
	private RelativeTimeConverter converter;
	private Calendar referenceTime;
	private TimeExtract extractor;

	public Process(String formattersFileName, Calendar referenceTime) {
		matchedTimes = new ArrayList<>();
		timeBundles = new ArrayList<>();
		this.formatter = new Formatter(formattersFileName, referenceTime);
		this.referenceTime = referenceTime;
		this.extractor = new TimeExtract();
		this.converter = new RelativeTimeConverter(this.referenceTime, this.extractor.getRelativeRegexList());
	}

	public void extractTimeFromFile(String articleFileName) {
		matchedTimes = extractor.extractFile(articleFileName, 20);
	}

	public void extractTimeFromInput(String input) {
		matchedTimes = extractor.extractInput(input);
	}

	public void createDateBundles() {
		for (MatchedTime time : matchedTimes) {
			timeBundles.add(new TimeBundle(time.getRawString(), time.getTimeType(), time.getRegex()));
		}
	}

	public void formatDate() {
		for (TimeBundle timeBundle : timeBundles) {
			if (timeBundle.getType() == TYPE.ABSOLUTE) {
				Calendar calendar = formatter.format(timeBundle);
				timeBundle.setCalendar(calendar);
			} else if (timeBundle.getType() == TYPE.RELATIVE) {
				Calendar date = converter.convert(timeBundle.getRawValue(), timeBundle.getRegex());
				timeBundle.setCalendar(date);
			}
		}
	}

	public void writeResult() {
		for (TimeBundle timeBundle : timeBundles) {
			IOUtils.writeNormalizedTime(timeBundle);
		}
	}

	public List<TimeBundle> getTimeBundles() {
		return this.timeBundles;
	}

	public void clear() {
		matchedTimes = new ArrayList<>();
		timeBundles = new ArrayList<>();
		//		relativeRegexs = new ArrayList<>();
	}

	public List<MatchedTime> getExtractedTimes() {
		return this.matchedTimes;
	}

}
