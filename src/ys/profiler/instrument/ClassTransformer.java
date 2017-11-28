package ys.profiler.instrument;

import org.objectweb.asm.ClassVisitor;

public class ClassTransformer extends ClassVisitor {

	public ClassTransformer(final int api, final ClassVisitor cv) {
		super(api, cv);
	}
}
