package ys.profiler.sample;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import ys.profiler.instrument.TreeClassTransformer;

/**
 * suggest to use the visitor manner which is more efficient,
 * tree manner needs to traverse class structure twice.
 *
 */
@Deprecated
public class TreeSampleClassTransformer extends TreeClassTransformer {

	protected boolean isNeedToTransform(MethodNode methodNode) {
		return methodNode.name.startsWith("case");
	}

	protected boolean isNeedToTransform(String className) {
		return className.equals("ys/profiler/sample/SampleTestee"); 
	}
	
	protected void injectStart(InsnList insnList) {
		insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ys/profiler/sample/SampleTracer", "start", "()V", false));
	}

	protected void injectFinally(InsnList insnList) {
		insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ys/profiler/sample/SampleTracer", "end", "()V", false));
	}
}