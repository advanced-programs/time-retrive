package info.hb.time.retrive.core;

import info.hb.time.retrive.io.IO;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeExtract {

	private List<String> absoluteRegexs;
	private List<String> relativeRegexs;

	public TimeExtract() {
		absoluteRegexs = IO.readRegex("TimeRegexAbsolute.txt");
		relativeRegexs = IO.readRegex("TimeRegexRelative.txt");
	}

	public List<MatchedTime> extractFile(String fileName, int linesNum) {
		List<String> texts = IO.readArticle(fileName, linesNum);
		List<MatchedTime> result = new ArrayList<>();
		for (String text : texts) {
			result.addAll(extractInput(text));
		}
		return result;
	}

	public List<MatchedTime> extractInput(String input) {
		input = input.toLowerCase();
		List<MatchedTime> result = new ArrayList<>();
		for (String regex : relativeRegexs) {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(input);
			while (matcher.find()) {
				String rawString = matcher.group();
				MatchedTime matchedTime = new MatchedTime(TYPE.RELATIVE, regex, rawString);
				result.add(matchedTime);
				input = input.replace(rawString, fixedLengthString("*", rawString.length()));
			}
		}
		for (String regex : absoluteRegexs) {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(input);
			while (matcher.find()) {
				String rawString = matcher.group();
				MatchedTime matchedTime = new MatchedTime(TYPE.ABSOLUTE, regex, rawString);
				result.add(matchedTime);
				input = input.replace(rawString, fixedLengthString("*", rawString.length()));
			}
		}
		return result;
	}

	private String fixedLengthString(String string, int length) {
		return String.format("%1$" + length + "s", string);
	}

	public List<String> getRelativeRegexList() {
		return relativeRegexs;
	}

}
