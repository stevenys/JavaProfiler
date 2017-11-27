package ys.profiler.sample;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import ys.profiler.instrument.ClassTransformer;

public class SampleClassTransformer extends ClassTransformer {

	protected boolean isNeedToTransform(MethodNode methodNode) {
		return methodNode.name.startsWith("case");
	}

	protected boolean isNeedToTransform(String className) {
		return className.equals("ys/profiler/sample/SampleTestee"); 
	}

	/**
	 * if your tracer method import new operand, you can simply add your own.
	 * 
	 * The sample tracer methods have no parameters, so just return the original one.
	 */
	protected int getMaxStack(MethodNode methodNode, int maxStack) {
		return maxStack;
	}
	
	protected void injectStart(InsnList insnList) {
		insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ys/profiler/sample/SampleTracer", "start", "()V", false));
	}

	protected void injectFinally(InsnList insnList) {
		insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ys/profiler/sample/SampleTracer", "end", "()V", false));
	}
}