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
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

/**
 * suggest to use the visitor manner which is more efficient,
 * tree manner needs to traverse class structure twice.
 *
 */
@Deprecated
public class TreeTransformer implements ClassFileTransformer {

	private List<TreeClassTransformer> classTransformerList = new ArrayList<TreeClassTransformer>();
	
	public TreeTransformer() {
		initClassTransformerList();
	}
	
	protected void initClassTransformerList() {
		InputStream is = null;
		BufferedReader reader = null;
		try {
			is = TreeTransformer.class.getClassLoader().getResourceAsStream("ys/profiler/config/profiler.treeTransformer");
			reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				
				try {
					classTransformerList.add((TreeClassTransformer) Class.forName(line).newInstance());
				} catch (Throwable e) {
					e.printStackTrace();
				}		
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	protected TreeClassTransformer determineClassTransformer(String className) {
		for (TreeClassTransformer classTransformer: classTransformerList) {
			if (classTransformer.isNeedToTransform(className)) {
				return classTransformer;
			}
		}
		return null;
	}
	
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		TreeClassTransformer classTransformer = determineClassTransformer(className);
		if (classTransformer == null) {
			return classfileBuffer;
		}
		
		ClassReader reader = new ClassReader(classfileBuffer);
		ClassNode classNode = new ClassNode();  
		reader.accept(classNode, 0);  
		classTransformer.transform(classNode);
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);  
		classNode.accept(writer); 
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