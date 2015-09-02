package info.hb.time.retrive.io;

import info.hb.time.retrive.core.TimeBundle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class IO {

	public static final int LINESNUM = 20;

	public static List<String> readRegex(String fileName) {
		List<String> regexes = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			for (String line; (line = br.readLine()) != null;) {
				if (line == null || line.isEmpty() || line.substring(0, 2).equals("//")) {
					continue;
				}
				regexes.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Read file " + fileName + " failed!");
		}
		return regexes;
	}

	public static List<String> readArticle(String fileName, int linesNum) {
		if (linesNum < 1) {
			System.out.println("Warning: you must read at least oneline each time, setted as default");
			linesNum = LINESNUM;
		}
		List<String> paragraphs = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			int i = linesNum;
			StringBuilder builder = new StringBuilder();
			for (String line; (line = br.readLine()) != null;) {
				if (i == 0) {
					paragraphs.add(builder.toString().toLowerCase());
					builder.setLength(0);
					i = linesNum;
					continue;
				} else if (line == null || line.isEmpty()) {
					if (line.length() > 1 && line.substring(0, 2).equals("//")) {
						continue;
					}
				} else {
					builder.append(line + " ");
					i--;
				}
			}
			if (builder.length() > 0) {
				paragraphs.add(builder.toString().toLowerCase());
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Read file " + fileName + " failed!");
		}
		return paragraphs;
	}

	public static List<String> readDateFormat(String fileName) {
		List<String> formats = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			for (String line; (line = br.readLine()) != null;) {
				if (line == null || line.isEmpty() || (line.length() > 1 && line.substring(0, 2).equals("//"))) {
					continue;
				}
				formats.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Read file " + fileName + " failed!");
		}
		return formats;
	}

	public static void writeNormalizedTime(TimeBundle timeBundle) {
		if (timeBundle.getCalendar() == null) {
			return;
		}
		String pattern = "MM-dd-yyyy h:mm a";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		System.out.print(formatter.format(timeBundle.getCalendar().getTime()) + " || " + timeBundle.getRawValue());
		System.out.println();
	}

}
