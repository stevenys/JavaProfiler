package ys.profiler.instrument;

import org.objectweb.asm.ClassVisitor;

/**
 * If one class transfer factory's isNeedToTransform return true, then break to only use that one,
 * so take care to the order place in profiler.transformerFactory configuration file.
 * 
 */
public interface ClassTransformerFactory {
	
	public ClassTransformer getClassTransformer(final int api, final ClassVisitor cv);
	
	public boolean isNeedToTransform(String className);
}
