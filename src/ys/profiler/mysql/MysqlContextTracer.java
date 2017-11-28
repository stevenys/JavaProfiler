package ys.profiler.mysql;

public class MysqlContextTracer {
	
	/**
	 * Just a sample, your case can use other efficient thread local manner to store method start context.
	 * 
	 * Since the end code will be put before every return opcode and our try-catch block, 
	 * so suggest to put the method context collect code into start block as many as possible like our context sample shows.
	 * 
	 */
	public static ThreadLocal<Context> CONTEXT = new ThreadLocal<Context>();
	
	public static class Context {
		public long startTime;
		
		public Object[] data;
		
		public Context(long startTime, Object[] data) {
			this.startTime = startTime;
			this.data = data;
		}
	}
	
	public static void start(String host, int port, String database, String sql) {
		long nanoTime = System.nanoTime();
		Object[] data = new Object[] {host, port, database, sql};
		System.out.println("MysqlContextTracer.start=" + nanoTime + " data.length=" + data.length);
		CONTEXT.set(new Context(nanoTime, data)); 
	}
	
	public static void end() {
		Context context = CONTEXT.get();
		if (context != null) {
			long endTime = System.nanoTime();
			System.out.println("MysqlContextTracer.end=" + endTime + " start=" + context.startTime + " elapse=" + (endTime - context.startTime));
			printData(context.data);
			System.out.println("---------------------------------------------\n"); 
		} else {
			System.err.println("context is null in end method, shouldn't be.");
		}
	}
	
	protected static void printData(Object[] data) {
		for (Object obj: data) {
			System.out.println(obj); 
		}	
	}
}