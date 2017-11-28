package ys.profiler.mysql;

import org.objectweb.asm.ClassVisitor;

import ys.profiler.instrument.ClassTransformer;
import ys.profiler.instrument.ClassTransformerFactory;

public class MysqlClassTransformerFactory implements ClassTransformerFactory {

	public ClassTransformer getClassTransformer(final int api, final ClassVisitor cv) {
		return new MysqlClassTransformer(api, cv);
	}

	public boolean isNeedToTransform(String className) {
		return className.equals("com/mysql/jdbc/ConnectionImpl"); 
	}

	/**
	 * Testing MYSQL driver class version is JDK1.5, so don't compute stack map frame.
	 */
	public int getClassWriterFlag() {
		return 0;
	}
	
}