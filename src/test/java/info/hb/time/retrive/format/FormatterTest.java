package info.hb.time.retrive.format;

import static org.junit.Assert.assertEquals;
import info.hb.time.retrive.core.TimeExtract;
import info.hb.time.retrive.domain.MatchedTime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class FormatterTest {

	@Test
	public void testFormatterSafe() throws FileNotFoundException, IOException, InterruptedException {
		TimeExtract timeExtract = new TimeExtract();
		String html = "";
		try (BufferedReader br = new BufferedReader(new FileReader(new File("test-data/test")));) {
			String str = "";
			while ((str = br.readLine()) != null) {
				html += str;
			}
		}
		final ThreadPoolExecutor pool = getThreadPoolExector(20);

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				pool.shutdown();
			}

		}));

		//		final AtomicInteger COUNT = new AtomicInteger(0);

		for (int i = 0; i < 1000; i++) {
			//			System.err.println(COUNT.addAndGet(1));
			pool.execute(new TimeRetriveRunnable(timeExtract, html));
		}

		pool.shutdown();
		pool.awaitTermination(5, TimeUnit.SECONDS);
	}

	public static class TimeRetriveRunnable implements Runnable {

		private TimeExtract timeExtract;
		private String html;

		public TimeRetriveRunnable(TimeExtract timeExtract, String html) {
			this.timeExtract = timeExtract;
			this.html = html;
		}

		@Override
		public void run() {
			List<MatchedTime> times = timeExtract.extractInput(html);
			assertEquals("2015-08-14 14:24", times.get(0).getRawString());
		}

	}

	public static ThreadPoolExecutor getThreadPoolExector(int threadsNum) {

		final ThreadPoolExecutor result = new ThreadPoolExecutor(threadsNum, threadsNum, 0L, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(threadsNum * 2), new ThreadPoolExecutor.CallerRunsPolicy());
		result.setThreadFactory(new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

					@Override
					public void uncaughtException(Thread t, Throwable e) {
						result.shutdown();
					}

				});
				return t;
			}

		});

		return result;
	}

}
