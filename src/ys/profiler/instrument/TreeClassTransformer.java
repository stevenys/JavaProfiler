package ys.profiler.instrument;
import java.util.Iterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;

/**
 * suggest to use the visitor manner which is more efficient,
 * tree manner needs to traverse class structure twice.
 *
 */
@Deprecated
public abstract class TreeClassTransformer {
	
	protected abstract boolean isNeedToTransform(String className);
	
	protected abstract boolean isNeedToTransform(MethodNode methodNode);
	
	/**
	 * Notice not to throw any exception by your own.
	 */
	protected abstract void injectStart(InsnList insn);
	
	/**
	 * Notice not to throw any exception by your own.
	 */
	protected abstract void injectFinally(InsnList insn);
	
	public void transform(ClassNode classNode) {  
		for (MethodNode methodNode: classNode.methods) {
			if (isNeedToTransform(methodNode)) {
				processMethod(methodNode);
			}
		}
	}
	
	/**
	 * The core part implementation of try-finally.
	 * 
	 * 1.  before any return opcode, add one try-catch block, do the end job both in try and catch blocks.
	 *     (since the end job opcode may be written more than once, suggest to put more method context pass opcodes in start job block)
	 *     
	 * 2.  do nothing for throw opcode, since it will be handled by outer catch block too. 
	 *     (and if you do the end job before the throw opcode, if the throw exception is caught by user code, then the end job will be done more than once which 
	 *     will make some of your tracer code work incorrectly, such as if you use stack to push method context in the start job and pop in the end job)
	 *     
	 *     (start_job)
	 *     try {
	 *     	   (end_job)
	 *         throw e;
	 *     } catch (e) {
	 *     	   ......
	 *     }
	 *     (end_job)
	 *     return;
	 */
	protected void processMethod(MethodNode methodNode) {
		LabelNode tryStartNode = null;
		LabelNode tryEndNode = null;
		LabelNode tryHandleNode = new LabelNode();
	
		InsnList insnFinal = new InsnList();
		injectStart(insnFinal);
		
		Iterator<AbstractInsnNode> ite = methodNode.instructions.iterator();  
        while (ite.hasNext()) {  
            AbstractInsnNode insn = ite.next();  
            int opcode = insn.getOpcode();  
            if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) { 
            	insnFinal.add(tryEndNode = new LabelNode());
            	injectFinally(insnFinal);
            	insnFinal.add(insn); 
            	
            	methodNode.tryCatchBlocks.add(new TryCatchBlockNode(tryStartNode, tryEndNode, tryHandleNode, null)); 
            	tryStartNode = null;
            } else {
            	// start a new try-finally start label after return opcode or at the beginning of method.
            	if (tryStartNode == null) {
            		insnFinal.add(tryStartNode = new LabelNode()); 
            	}
            	insnFinal.add(insn); 
            }
        }
        insnFinal.add(tryHandleNode); 
        injectFinally(insnFinal);
		insnFinal.add(new InsnNode(Opcodes.ATHROW));
		
		methodNode.instructions = insnFinal;
	}
}