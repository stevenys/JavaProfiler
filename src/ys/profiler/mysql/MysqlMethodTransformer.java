package ys.profiler.mysql;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import ys.profiler.instrument.MethodTransformer;

public class MysqlMethodTransformer extends MethodTransformer {

	public MysqlMethodTransformer(int api, MethodVisitor mv) {
		super(api, mv);
	}

	/**
	 * For the prepared statement, the SQL is from the 4th parameter named packet, otherwise the 2nd parameter named sql.
	 * 
	 * For the packet, the first 5 bytes is the packet header and the follower is the sql we need.
	 * header(5 bytes) = sql_length(3 bytes) + packet_sequence(1 byte) + packet_command(1 byte)
	 * 
	 */
	protected void injectStart() {
		Label labelPacketNullStart = new Label();
		Label labelPacketNullEnd = new Label();
		
		mv.visitVarInsn(Opcodes.ALOAD, 4);
		mv.visitJumpInsn(Opcodes.IFNULL, labelPacketNullStart);
		
		doAddStartParams();
		mv.visitTypeInsn(Opcodes.NEW, "java/lang/String"); 
		mv.visitInsn(Opcodes.DUP);
		mv.visitVarInsn(Opcodes.ALOAD, 4);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/mysql/jdbc/Buffer", "getByteBuffer", "()[B", false);
		mv.visitInsn(Opcodes.ICONST_5); //ignore the first 5 packet header bytes.
		mv.visitVarInsn(Opcodes.ALOAD, 4);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/mysql/jdbc/Buffer", "getPosition", "()I", false);
		mv.visitInsn(Opcodes.ICONST_5);
		mv.visitInsn(Opcodes.ISUB); 
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/String", "<init>", "([BII)V", false);
		doInvokeStart();
		mv.visitJumpInsn(Opcodes.GOTO, labelPacketNullEnd); 
		
		//load parameter and call in another branch to avoid need to create a new local variable.
		mv.visitLabel(labelPacketNullStart); 
		doAddStartParams();
		mv.visitVarInsn(Opcodes.ALOAD, 2);
		doInvokeStart();
		mv.visitLabel(labelPacketNullEnd); 
	}
	
	private void doAddStartParams() {
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "com/mysql/jdbc/ConnectionImpl", "host", "Ljava/lang/String;");
		
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "com/mysql/jdbc/ConnectionImpl", "port", "I");
		
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "com/mysql/jdbc/ConnectionImpl", "database", "Ljava/lang/String;");
	}
	
	private void doInvokeStart() {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "ys/profiler/mysql/MysqlContextTracer", 
				"start", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;)V", false);
	}

	protected void injectFinally() {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "ys/profiler/mysql/MysqlContextTracer", "end", "()V", false);
	}
	
}