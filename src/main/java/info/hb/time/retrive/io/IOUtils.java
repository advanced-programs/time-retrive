package info.hb.time.retrive.io;

import info.hb.time.retrive.domain.TimeBundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOUtils {

	private static Logger logger = LoggerFactory.getLogger(IOUtils.class);

	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("MM-dd-yyyy h:mm a");

	private static final int LINESNUM = 20;

	public static List<String> readRegex(String fileName) {
		List<String> regexes = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(Thread.currentThread()
				.getContextClassLoader().getResourceAsStream(fileName)));) {
			for (String line; (line = br.readLine()) != null;) {
				if (line == null || line.isEmpty() || line.substring(0, 2).equals("//")) {
					continue;
				}
				regexes.add(line);
			}
		} catch (IOException e) {
			logger.error("Read file {} failed!", fileName);
			throw new RuntimeException(e);
		}
		return regexes;
	}

	public static List<String> readArticle(String fileName, int linesNum) {
		if (linesNum < 1) {
			logger.warn("Warning: you must read at least oneline each time, setted as default");
			linesNum = LINESNUM;
		}
		List<String> paragraphs = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(Thread.currentThread()
				.getContextClassLoader().getResourceAsStream(fileName)));) {
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
			logger.error("Read file {} failed!", fileName);
			throw new RuntimeException(e);
		}
		return paragraphs;
	}

	public static List<String> readDateFormat(String fileName) {
		List<String> formats = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(Thread.currentThread()
				.getContextClassLoader().getResourceAsStream(fileName)));) {
			for (String line; (line = br.readLine()) != null;) {
				if (line == null || line.isEmpty() || (line.length() > 1 && line.substring(0, 2).equals("//"))) {
					continue;
				}
				formats.add(line);
			}
		} catch (IOException e) {
			logger.error("Read file {} failed!", fileName);
			throw new RuntimeException(e);
		}
		return formats;
	}

	public static void writeNormalizedTime(TimeBundle timeBundle) {
		if (timeBundle.getCalendar() == null) {
			return;
		}
		logger.info(FORMATTER.format(timeBundle.getCalendar().getTime()) + " || " + timeBundle.getRawValue());
	}

}
