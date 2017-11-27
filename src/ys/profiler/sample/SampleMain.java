package ys.profiler.sample;

/**
 *  java -javaagent:lib/agent.jar -classpath lib/*.jar:bin -Ddebug.location=. ys.profiler.sample.SampleMain
 *
 */
public class SampleMain {

	public static void main(String[] args) {
		SampleTestee testee = new SampleTestee();
		System.out.println("SampleTestee.className=" + testee.getClass().getName()); 
		
		String name = "ys";
		testee.case1(name);
		System.out.println("----------------------------------------\n"); 
		
		long result2_1 = testee.case2(null);
		System.out.println("case2-1.result==1 ? " + (result2_1 == 1));
		System.out.println("----------------------------------------\n"); 
		
		long result2_2 = testee.case2(name);
		System.out.println("case2-2.result==2 ? " + (result2_2 == 2));
		System.out.println("----------------------------------------\n"); 
		
		try {
			testee.case3(null);
		} catch (Throwable e) {
			e.printStackTrace(); 
		}
		System.out.println("----------------------------------------\n"); 
		
		long result3_2 = testee.case3(name);
		System.out.println("case3-2.result==2 ? " + (result3_2 == 2));
		System.out.println("----------------------------------------\n"); 
		
		long result4_3 = testee.case4(null);
		System.out.println("case4-3.result==3 ? " + (result4_3 == 3));
		System.out.println("----------------------------------------\n"); 
		
		testee.hello(name);
	}

}
