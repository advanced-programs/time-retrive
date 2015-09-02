package info.hb.time.retrive.core;

public class MatchedTime {

	public TYPE timeType;
	public String regex;
	public String rawString;

	public MatchedTime(TYPE timeType, String regex, String rawString) {
		this.timeType = timeType;
		this.regex = regex;
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
