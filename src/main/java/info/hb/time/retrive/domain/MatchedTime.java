package info.hb.time.retrive.domain;

/**
 * 匹配的时间
 *
 * @author wanggang
 *
 */
public class MatchedTime {

	private TYPE timeType;
	private String regex;
	private String rawString;

	public MatchedTime(TYPE timeType, String regex, String rawString) {
		this.timeType = timeType;
		this.regex = regex;
		this.rawString = rawString;
	}

	public TYPE getTimeType() {
		return timeType;
	}

	public void setTimeType(TYPE timeType) {
		this.timeType = timeType;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getRawString() {
		return rawString;
	}

	public void setRawString(String rawString) {
		this.rawString = rawString;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(timeType == TYPE.ABSOLUTE ? "ABSOLUTE " : "RELATIVE ");
		sb.append(regex);
		sb.append(" ");
		sb.append(rawString);
		return sb.toString();
	}

}
