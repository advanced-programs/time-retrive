package info.hb.time.retrive.demo;

import info.hb.time.retrive.core.TimeExtract;
import info.hb.time.retrive.domain.MatchedTime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class HTMLTimeDemo {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		TimeExtract timeExtract = new TimeExtract();
		// 读取HTML文件
		String html = "";
		try (BufferedReader br = new BufferedReader(new FileReader(new File("test-data/test")));) {
			String str = "";
			while ((str = br.readLine()) != null) {
				html += str;
			}
		}
		List<MatchedTime> times = timeExtract.extractInput(html);
		System.out.println(times.get(0).getTimeType() + "  ,  " + times.get(0).getRawString());
		//		for (MatchedTime time : times) {
		//			System.out.println(time.getTimeType() + "  ,  " + time.getRawString());
		//		}
	}

}
