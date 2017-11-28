package ys.profiler.instrument;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public abstract class MethodTransformer extends MethodVisitor {

	private Label labelTryStart = null;

	private Label labelTryEnd = null;
	
	private Label labelAfterThrow = null;

	private Label labelTryHandle = null;

	public MethodTransformer(int api, MethodVisitor mv) {
		super(api, mv);
	}

	/**
	 * Notice not to throw any exception by your own.
	 */
	protected abstract void injectStart();

	/**
	 * Notice not to throw any exception by your own.
	 */
	protected abstract void injectFinally();

	/**
	 * Place the exception handle code at the beginning of method for simple and add one goto to skip it as the first opcode.
	 * 
	 * Do the start job and place one try-catch start label.
	 */
	public void visitCode() {
		mv.visitCode();

		Label labelCodeStart = new Label();
		mv.visitJumpInsn(Opcodes.GOTO, labelCodeStart);
		
		mv.visitLabel(labelTryHandle = new Label());
		injectFinally();
		mv.visitInsn(Opcodes.ATHROW);

		mv.visitLabel(labelCodeStart);
		injectStart();
		
		mv.visitLabel(labelTryStart = new Label());
	}

	/**
	 * The core part implementation of try-finally.
	 * 
	 * 1.  before any return opcode, add one try-catch block, do the end job both in try and catch blocks.
	 *     (since the end job opcode may be written more than once, suggest to put more method context pass opcodes in start job block)
	 *     
	 * 2.  do nothing for throw opcode but just keep the last throw opcode offset for final throw process, since it will be handled by outer catch block too. 
	 *     (and if you do the end job before the throw opcode, if the throw exception is caught by user code, then the end job will be done more than once which 
	 *     will make some of your tracer code work incorrectly, such as if you use stack to push method context in the start job and pop in the end job)
	 *     
	 *     (start_job)
	 *     try {
	 *     	   X(end_job)X
	 *         throw e;
	 *     } catch (e) {
	 *     	   ......
	 *     }
	 *     (end_job)
	 *     return;
	 */
	public void visitInsn(int opcode) {
		if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
			mv.visitLabel(labelTryEnd = new Label());
			injectFinally();
			mv.visitInsn(opcode);
			mv.visitTryCatchBlock(labelTryStart, labelTryEnd, labelTryHandle, null);

			// eagerly new one try start label.
			mv.visitLabel(labelTryStart = new Label());
		} else {
			mv.visitInsn(opcode);
			if (opcode == Opcodes.ATHROW) {
				// save the offset of last throw opcode, which will be used in end of code.
				mv.visitLabel(labelAfterThrow = new Label());	
 			}	
		}
	}
	
	/**
	 * Handle the final throw opcode ending case.
	 * 
	 * try {
	 *     return;
	 * } catch (e1) {
	 *     throw e1;
	 * } catch (e2) {
	 *     throw e2;
	 * }
	 */
	public void visitEnd() {
		if (labelAfterThrow != null && labelAfterThrow.getOffset() > labelTryStart.getOffset()) {
			mv.visitTryCatchBlock(labelTryStart, labelAfterThrow, labelTryHandle, null);
		}
	}
}