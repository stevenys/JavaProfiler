package ys.profiler.sample;

public class SampleTracer {
	
	/**
	 * Just a sample, your case can use other efficient thread local manner to store method start context.
	 * 
	 */
	public static ThreadLocal<Long> START_TIME = new ThreadLocal<Long>();
	
	public static void start() {
		long nanoTime = System.nanoTime();
		System.out.println("SampleProfiler.start=" + nanoTime);
		START_TIME.set(nanoTime);
	}
	
	public static void end() {
		Long startTime = START_TIME.get();
		if (startTime != null) {
			long endTime = System.nanoTime();
			System.out.println("SampleProfiler.end=" + endTime + " start=" + startTime + " elapse=" + (endTime - startTime));
		} else {
			System.err.println("startTime is null in end method, shouldn't be."); 
		}
 	}
}
