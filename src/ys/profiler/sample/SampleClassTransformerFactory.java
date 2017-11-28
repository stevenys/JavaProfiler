package ys.profiler.sample;

import org.objectweb.asm.ClassVisitor;

import ys.profiler.instrument.ClassTransformer;
import ys.profiler.instrument.ClassTransformerFactory;

public class SampleClassTransformerFactory implements ClassTransformerFactory {

	public ClassTransformer getClassTransformer(final int api, final ClassVisitor cv) {
		return new SampleClassTransformer(api, cv);
	}

	public boolean isNeedToTransform(String className) {
		return className.equals("ys/profiler/sample/SampleTestee"); 
	}
	
}