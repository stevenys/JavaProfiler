package ys.profiler.sample;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import ys.profiler.instrument.MethodTransformer;

public class SampleContextMethodTransformer extends MethodTransformer {

	public SampleContextMethodTransformer(int api, MethodVisitor mv) {
		super(api, mv);
	}

	protected void injectStart() {
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "ys/profiler/sample/SampleTestee", "field1", "Ljava/lang/String;");
		
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "ys/profiler/sample/SampleTestee", "field2", "I");
		
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitVarInsn(Opcodes.ILOAD, 2);
		mv.visitVarInsn(Opcodes.ILOAD, 3);
		
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "ys/profiler/sample/SampleContextTracer", "start", "(Ljava/lang/String;ILjava/lang/String;II)V", false);
	}

	protected void injectFinally() {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "ys/profiler/sample/SampleContextTracer", "end", "()V", false);
	}
	
}
