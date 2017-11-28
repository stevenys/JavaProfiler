# JavaProfiler

The core code is to implement a try-finally syntax using asm (write using asm-6.0, test under jdk1.5 & 1.8)
https://raw.githubusercontent.com/stevenys/JavaProfiler/master/src/ys/profiler/instrument/MethodTransformer.java

1.  Between any return opcode and the previous one, add one try-catch block, do the end job both in try and catch blocks.
(since the end job opcode may be written more than once, suggest to put more method context pass opcodes in start job block)
	      
2.  Do nothing before throw opcode, since it will be handled by outer try-catch block too. 
(And if you do the end job before the throw opcode, if the throw exception is caught by user code, then the end job will be run more than once,  which will make some of your tracer code work incorrectly, such as if you use stack to push method context in the start job and pop in the end job)
	      
	      (start_job)
	      try {
	      	   XX(end_job)XX
	          throw e;
	      } catch (e) {
	      	   ......
	      }
	      (end_job)
	      return;
	      
3.  If throw is the final opcode which means it is after the final return opcode, just add only one try-catch block between final return and the final throw.

	      (start_job)
	      try {
	          .......
	          return;
	      } catch (e1) {
	      	  throw e1;
	      } catch (e2) {
	          throw e2;
	      }
	      (end_job)
	      
	      
