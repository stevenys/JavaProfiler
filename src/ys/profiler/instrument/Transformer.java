package ys.profiler.instrument;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class Transformer implements ClassFileTransformer {

	private List<ClassTransformerFactory> classTransformerFactoryList = new ArrayList<ClassTransformerFactory>();
	
	public Transformer() {
		initClassTransformerList();
	}
	
	protected void initClassTransformerList() {
		InputStream is = null;
		BufferedReader reader = null;
		try {
			is = Transformer.class.getClassLoader().getResourceAsStream("ys/profiler/config/profiler.transformerFactory");
			reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				
				try {
					classTransformerFactoryList.add((ClassTransformerFactory) Class.forName(line).newInstance());
				} catch (Throwable e) {
					e.printStackTrace();
				}		
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	protected ClassTransformerFactory determineClassTransformerFactory(String className) {
		for (ClassTransformerFactory classTransformer: classTransformerFactoryList) {
			if (classTransformer.isNeedToTransform(className)) {
				return classTransformer;
			}
		}
		return null;
	}
	
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		ClassTransformerFactory classTransformerFactory = determineClassTransformerFactory(className);
		if (classTransformerFactory == null) {
			return classfileBuffer;
		}
		
		int classWriterFlag = classTransformerFactory.getClassWriterFlag();
		ClassReader reader = new ClassReader(classfileBuffer);
		ClassWriter writer = new ClassWriter(classWriterFlag);  
		ClassVisitor classTransformer = classTransformerFactory.getClassTransformer(Opcodes.ASM4, writer);
		reader.accept(classTransformer, 0);
		
		byte[] bytes =  writer.toByteArray();
		debugClass(className, bytes);
	    return bytes;
	}
	
	protected void debugClass(String className, byte[] bytes) {
		String debugLocation = System.getProperty("debug.location");
		if (debugLocation == null) {
			return;
		}
		
		FileOutputStream fos = null;
		try {
			File file = new File(debugLocation + File.separatorChar + className.replace('/', File.separatorChar) + ".class");
			file.getParentFile().mkdirs();
			
			fos = new FileOutputStream(file);
			fos.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}