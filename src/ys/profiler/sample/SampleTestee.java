package ys.profiler.sample;

public class SampleTestee {
	
	public String field1 = null;
	
	public int field2 = 0;
	
	
	/**
	 *  check one return case.
	 */
	public void case1(String name) {
		 System.out.println("case1: " + name); 
	}
	
	/**
	 * check multiple return case.
	 */
	public long case2(String name) {
		if (name == null) {
			System.out.println("case2-1: " + name); 
			return 1L;
		}
		System.out.println("case2-2: " + name); 
		return 2L;
	}
	
	/**
	 * check throw | return case.
	 */
	public long case3(String name) {
		if (name == null) {
			System.out.println("case3-1: " + name); 
			throw new NullPointerException(name);
		}
		System.out.println("case3-2: " + name); 
		return 2L;
	}
	
	/**
	 * check throw->catch->throw | return case.
	 */
	public long case4(String name) {
		try {
			if (name == null) {
				System.out.println("case4-1: " + name); 
				throw new NullPointerException(name);
			}
		} catch (RuntimeException e) {
			System.out.println("case4-2: " + e.getMessage()); 
			name = "steven";
		}
		System.out.println("case4-3: " + name); 
		return 3L;
	}
	
	/**
	 * check multiple parameter and field case.
	 * check max stack recompute case (from 4 to 5).
	 */
	public long case5(String param1, int param2, int param3, String unusedParam4) {
		try {
			if (unusedParam4 == null) {
				System.out.println("case5-1: unusedParam4=" + unusedParam4); 
				throw new NullPointerException(param1);
			}
		} catch (RuntimeException e) {
			System.out.println("case5-2: " + e.getMessage()); 
			param1 = "steven";
		}
		System.out.println("case5-3: field1=" + field1 + " field2=" + field2 
				+ " param1=" + param1 + " param2=" + param2 + " param3=" + param3); 
		return 3L;
	}
	
	public void hello(String name) {
		System.out.println("hello: " + name); 
	}
}