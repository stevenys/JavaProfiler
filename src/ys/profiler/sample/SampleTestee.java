package ys.profiler.sample;

public class SampleTestee {
	
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
	
	public void hello(String name) {
		System.out.println("hello: " + name); 
	}
}