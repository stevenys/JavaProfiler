package ys.profiler.sample;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import ys.profiler.instrument.ClassTransformer;
import ys.profiler.instrument.ClassTransformerFactory;

public class SampleClassTransformerFactory implements ClassTransformerFactory {

	public ClassTransformer getClassTransformer(final int api, final ClassVisitor cv) {
		return new SampleClassTransformer(api, cv);
	}

	public boolean isNeedToTransform(String className) {
		return className.equals("ys/profiler/sample/SampleTestee"); 
	}

	/**
	 * Testing environment is JDK1.8, so set the flag to COMPUTE_FRAMES.
	 */
	public int getClassWriterFlag() {
		return ClassWriter.COMPUTE_FRAMES;
	}
	
}