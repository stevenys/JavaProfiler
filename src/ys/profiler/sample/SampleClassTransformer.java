package ys.profiler.sample;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import ys.profiler.instrument.ClassTransformer;

public class SampleClassTransformer extends ClassTransformer {

	public SampleClassTransformer(final int api, final ClassVisitor cv) {
		super(api, cv);
	}

	public MethodVisitor visitMethod(int arg, String name, String descriptor, String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(arg, name, descriptor, signature, exceptions);
		if (name.startsWith("case5")) {
			return new SampleContextMethodTransformer(api, mv);
		}
		if (name.startsWith("case")) { 
			return new SampleMethodTransformer(api, mv);
		}
		return mv;
	}

}
