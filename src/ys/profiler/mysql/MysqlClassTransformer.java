package ys.profiler.mysql;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import ys.profiler.instrument.ClassTransformer;

public class MysqlClassTransformer extends ClassTransformer {

	public MysqlClassTransformer(final int api, final ClassVisitor cv) {
		super(api, cv);
	}

	public MethodVisitor visitMethod(int arg, String name, String descriptor, String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(arg, name, descriptor, signature, exceptions);
		if (name.equals("execSQL") && 
				descriptor.equals("(Lcom/mysql/jdbc/StatementImpl;Ljava/lang/String;ILcom/mysql/jdbc/Buffer;IIZLjava/lang/String;[Lcom/mysql/jdbc/Field;Z)Lcom/mysql/jdbc/ResultSetInternalMethods;")) {
			return new MysqlMethodTransformer(api, mv);
		}
		return mv;
	}

}
