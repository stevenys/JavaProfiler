package ys.profiler.sample;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import ys.profiler.instrument.MethodTransformer;

public class SampleMethodTransformer extends MethodTransformer {

	public SampleMethodTransformer(int api, MethodVisitor mv) {
		super(api, mv);
	}

	protected void injectStart() {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "ys/profiler/sample/SampleTracer", "start", "()V", false);
	}

	protected void injectFinally() {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "ys/profiler/sample/SampleTracer", "end", "()V", false);
	}
	
}
