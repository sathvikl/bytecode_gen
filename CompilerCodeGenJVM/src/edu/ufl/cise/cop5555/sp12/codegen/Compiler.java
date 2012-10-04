package edu.ufl.cise.cop5555.sp12.codegen;



import static java.lang.System.err;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.HeaderTokenizer.Token;

import edu.ufl.cise.cop5555.sp12.Parser;
import edu.ufl.cise.cop5555.sp12.Scanner;
import edu.ufl.cise.cop5555.sp12.SyntaxException;
import edu.ufl.cise.cop5555.sp12.TokenStream;
import edu.ufl.cise.cop5555.sp12.ast.*;
import edu.ufl.cise.cop5555.sp12.context.ContextCheckVisitor;
import edu.ufl.cise.cop5555.sp12.context.ContextException;

public class Compiler {

	// overrides (protected) defineClass method to make it public
	static class COP5555ClassLoader extends ClassLoader {
		@SuppressWarnings("rawtypes")
		public Class defineClass(String name, byte[] b) {
			return defineClass(name, b, 0, b.length);
		}
	}

	static boolean WRITE_TO_FILE = true; // write generated classfile?
	static boolean EXECUTE = true; // dynamically execute generated classfile?

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.out.println("error:  missing input file");
			System.exit(1);
		}
		BufferedReader inputReader = new BufferedReader(new FileReader(args[0]));
		TokenStream stream = new TokenStream(inputReader);
		//String inputString ="prog TestMapNeq1 " +
		//TokenStream stream = new TokenStream(inputString);
		
		Scanner scanner = new Scanner(stream);
		scanner.scan();
		Parser parser = new Parser(stream);
		AST tree = null;
		String progName = null;
		byte[] bytes = null;
		boolean codeGenSuccess = false;
		try {
			tree = parser.parse();
			ContextCheckVisitor tcv = new ContextCheckVisitor();
			tree.visit(tcv, null);
			progName = ((Program) tree).ident.getText();
			CodeGenVisitor gen = new CodeGenVisitor();
			bytes = (byte[]) tree.visit(gen, progName);
			codeGenSuccess = true;
		} catch (SyntaxException e) {
			err.println(e.t.getLineNumber() + ": Syntax error: "  + e.getMessage());
		} catch (ContextException e) {
			err.println("Type error: " + e.getMessage());
		} catch (Exception e) {
			err.println("Error " + e.getMessage());
			e.printStackTrace();
		}
		if (codeGenSuccess) {
			if (WRITE_TO_FILE) {  //  write classfile
				FileOutputStream f;
				System.out.println("writing class " + progName + ".class");
				f = new FileOutputStream(progName + ".class");
				f.write(bytes);
				f.close();
			}
			if (EXECUTE) {  //dynamically execute generated code
				COP5555ClassLoader cl = new COP5555ClassLoader();
				Class c = cl.defineClass(progName, bytes);
				try {
					//get Method object for main method in generated code
					@SuppressWarnings("unchecked")
					Method mainMethod = c.getMethod("main", String[].class);
					//set up command line arguments for generated class
					// arg 0 was the source file, the rest should be passed
					// to generated code
//					int numArgs = args.length - 1; 
//					String[] params = new String[numArgs];
//						for (int j = 0; j != numArgs; j++) {
//							params[j] = args[j + 1];
//						}
//					for (String p : params) System.out.println(p);
//					Class[] paramTypes = mainMethod.getParameterTypes();
//					for (Class p : paramTypes) System.out.println(p.getName());
					Object[] objectParams = new Object[1];
//					objectParams[0] = params;
					mainMethod.invoke(null, objectParams);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		} // else (!codeGenSuccess) terminate
	}
}
