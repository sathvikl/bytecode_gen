package edu.ufl.cise.cop5555.sp12;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

import edu.ufl.cise.cop5555.sp12.ast.AST;
import edu.ufl.cise.cop5555.sp12.ast.ASTVisitor;
import edu.ufl.cise.cop5555.sp12.ast.AssignPairListCommand;
import edu.ufl.cise.cop5555.sp12.ast.ToStringVisitor;
import edu.ufl.cise.cop5555.sp12.context.ContextCheckVisitor;
import edu.ufl.cise.cop5555.sp12.context.ContextException;

public class TestContextCheckVisitor {

	private TokenStream getInitializedTokenStream(String input) {
		TokenStream stream = new TokenStream(input);
		Scanner s = new Scanner(stream);
		s.scan();
		return stream;
	}
		
	@Test
	public void testEmptyProg() {
		String input = "prog Test1  gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			ASTVisitor contextChecker = new ContextCheckVisitor();
			ast.visit(contextChecker,null);
		}
		catch(Exception ErrorAST) {
			
		}
	}		
	
	@Test
	public void testInType1() {
		String input = "prog Test1 boolean x; int y; if (y == 1) int x; x = 1;  do(x == 1) int k; k=8; x = 56; od; fi; if(true) int j; j=9; fi; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		ContextException ASTNode = null;
		try {
			AST ast = parser.parse();
			ASTVisitor contextChecker = new ContextCheckVisitor();
			ast.visit(contextChecker,null);
		}
		catch(Exception ErrorAST) {
			ASTNode = (ContextException)ErrorAST;
			//ErrorAST.printStackTrace();
			System.out.println(ErrorAST.getMessage() + " AST Node: " + ASTNode.ast.getClass().getName());
		}
		assertNull(ASTNode);
	}	
	
	@Test
	public void testProgNameRpt() {
		String input = "prog Test1 boolean x; int y; if (y == 1) int Test1; fi; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		ContextException ASTNode = null;
		try {
			AST ast = parser.parse();
			ASTVisitor contextChecker = new ContextCheckVisitor();
			ast.visit(contextChecker,null);
		}
		catch(Exception ErrorAST) {
			ASTNode = (ContextException)ErrorAST;
			//ErrorAST.printStackTrace();
			System.out.println(ErrorAST.getMessage() + " AST Node: " + ASTNode.ast.getClass().getName());
		}
		assertNotNull(ASTNode);
	}	
	
	@Test
	public void testProgSelSort() {
		String input = "prog selectsort map[int, int] array;int i; int j;  int sizeofarr;i =0; j=0; sizeofarr = 100;do (i < sizeofarr)do ( j < sizeofarr)if ( array[i] > array[j])int tmp; tmp=0;tmp = array[i];array[i] = array[j]; array[j] = array[i];fi;od;od;gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		ContextException ASTNode = null;
		try {
			AST ast = parser.parse();
			ASTVisitor contextChecker = new ContextCheckVisitor();
			ast.visit(contextChecker,null);
		}
		catch(Exception ErrorAST) {
			ASTNode = (ContextException)ErrorAST;
			//ErrorAST.printStackTrace();
			System.out.println(ErrorAST.getMessage() + " AST Node: " + ASTNode.ast.getClass().getName());
		}
		assertNull(ASTNode);
	}	
	
	@Test
	public void testProgassign() {
		String input = "prog selectsort map[int, string] array; int i; string tmp; tmp = array[i]; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		ContextException ASTNode = null;
		try {
			AST ast = parser.parse();
			ASTVisitor contextChecker = new ContextCheckVisitor();
			ast.visit(contextChecker,null);
		}
		catch(Exception ErrorAST) {
			ASTNode = (ContextException)ErrorAST;
			//ErrorAST.printStackTrace();
			System.out.println(ErrorAST.getMessage() + " AST Node: " + ASTNode.ast.getClass().getName());
		}
		assertNull(ASTNode);
	}
	
	@Test
	public void testBinEx1() {
		String input = "prog Test2 map[int, string] array1; map[int, string] array2; map[int, string] K1; K1 = array1 * array2; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		ContextException ASTNode = null;
		try {
			AST ast = parser.parse();
			ASTVisitor contextChecker = new ContextCheckVisitor();
			ast.visit(contextChecker,null);
		}
		catch(Exception ErrorAST) {
			ASTNode = (ContextException)ErrorAST;
			//ErrorAST.printStackTrace();
			System.out.println(ErrorAST.getMessage() + " AST Node: " + ASTNode.ast.getClass().getName());
		}
		assertNull(ASTNode);
	}	
	
	@Test
	public void testBinEx2() {
		String input = "prog Test2 string array1; map[int,int] a2; string K1; K1 = array1 + a2; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		ContextException ASTNode = null;
		try {
			AST ast = parser.parse();
			ASTVisitor contextChecker = new ContextCheckVisitor();
			ast.visit(contextChecker,null);
		}
		catch(Exception ErrorAST) {
			ASTNode = (ContextException)ErrorAST;
			//ErrorAST.printStackTrace();
			System.out.println(ErrorAST.getMessage() + " AST Node: " + ASTNode.ast.getClass().getName());
		}
		assertNotNull(ASTNode);
	}

	@Test
	public void testBinEx3() {
		String input = "prog Test2 int a1; int a2; boolean a3; a1 = -a2; if(a1==4) a3=!a3; fi; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		ContextException ASTNode = null;
		try {
			AST ast = parser.parse();
			ASTVisitor contextChecker = new ContextCheckVisitor();
			ast.visit(contextChecker,null);
		}
		catch(Exception ErrorAST) {
			ASTNode = (ContextException)ErrorAST;
			//ErrorAST.printStackTrace();
			System.out.println(ErrorAST.getMessage() + " AST Node: " + ASTNode.ast.getClass().getName());
		}
		assertNull(ASTNode);
	}
	
	@Test
	public void testBinEx4() {
		String input = "prog Test2 int a1; int a2; int a3; string s1; string s2; a3 = a1 / a2; if((a1 >= a2) | (s2 == s1)) fi; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		ContextException ASTNode = null;
		try {
			AST ast = parser.parse();
			ASTVisitor contextChecker = new ContextCheckVisitor();
			ast.visit(contextChecker,null);
		}
		catch(Exception ErrorAST) {
			ASTNode = (ContextException)ErrorAST;
			//ErrorAST.printStackTrace();
			System.out.println(ErrorAST.getMessage() + " AST Node: " + ASTNode.ast.getClass().getName());
		}
		assertNull(ASTNode);
	}
	
	@Test
	public void testBinEx5() {
		String input = "prog Test2 int a1; int a2; boolean res; res = a1 >= a2 & a1 >= a2; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		ContextException ASTNode = null;
		try {
			AST ast = parser.parse();
			ASTVisitor contextChecker = new ContextCheckVisitor();
			ast.visit(contextChecker,null);
		}
		catch(Exception ErrorAST) {
			ASTNode = (ContextException)ErrorAST;
			//ErrorAST.printStackTrace();
			System.out.println(ErrorAST.getMessage() + " AST Node: " + ASTNode.ast.getClass().getName());
		}
		assertNotNull(ASTNode);
	}
	
	@Test
	public void testDoEach() {
		String input = "prog Test2 map[int, boolean] it; string key; boolean val; do it:[key,val] key = 1; val=false; od; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		ContextException ASTNode = null;
		try {
			AST ast = parser.parse();
			ASTVisitor contextChecker = new ContextCheckVisitor();
			ast.visit(contextChecker,null);
		}
		catch(Exception ErrorAST) {
			ASTNode = (ContextException)ErrorAST;
			//ErrorAST.printStackTrace();
			System.out.println(ErrorAST.getMessage() + " AST Node: " + ASTNode.ast.getClass().getName());
		}
		assertNotNull(ASTNode);
	}
	
	@Test
	public void testAssignLVal() {
		String input = "prog Test2 map[int, boolean] it; map[int,boolean] kt; int val; val =kt; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		ContextException ASTNode = null;
		try {
			AST ast = parser.parse();
			ASTVisitor contextChecker = new ContextCheckVisitor();
			ast.visit(contextChecker,null);
		}
		catch(Exception ErrorAST) {
			ASTNode = (ContextException)ErrorAST;
			//ErrorAST.printStackTrace();
			System.out.println(ErrorAST.getMessage() + " AST Node: " + ASTNode.ast.getClass().getName());
		}
		assertNotNull(ASTNode);
	}	
	
	@Test
	public void testAssignPairList1() {
		String input = "prog Test2 int ab; int ac; int a; int b; map[boolean, map[int,boolean]] it; map[int,boolean] kt; it = {[kt,true], [kt,false], [kt,a>b]}; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		ContextException ASTNode = null;
		try {
			AST ast = parser.parse();
			ASTVisitor contextChecker = new ContextCheckVisitor();
			ast.visit(contextChecker,null);
		}
		catch(Exception ErrorAST) {
			ASTNode = (ContextException)ErrorAST;
			//ErrorAST.printStackTrace();
			System.out.println(ErrorAST.getMessage() + " AST Node: " + ASTNode.ast.getClass().getName());
		}
		assertNotNull(ASTNode);
		assertEquals("edu.ufl.cise.cop5555.sp12.ast.AssignPairListCommand", ASTNode.ast.getClass().getName());
		//assertThat(ASTNode, instanceOf());
	}	
	
	@Test
	public void testAssignPairList2() {
		String input = "prog Test2 map[boolean,string] a1; string w; int a; a1 = {[true,w], [false,\"str\"], [true,w]}; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		ContextException ASTNode = null;
		try {
			AST ast = parser.parse();
			ASTVisitor contextChecker = new ContextCheckVisitor();
			ast.visit(contextChecker,null);
		}
		catch(Exception ErrorAST) {
			ASTNode = (ContextException)ErrorAST;
			//ErrorAST.printStackTrace();
			System.out.println(ErrorAST.getMessage() + " AST Node: " + ASTNode.ast.getClass().getName());
		}
		assertNull(ASTNode);
		//assertEquals("edu.ufl.cise.cop5555.sp12.ast.AssignPairListCommand", ASTNode.ast.getClass().getName());
		//assertThat(ASTNode, instanceOf());
	}	
	
	@Test
	public void testIf() {
		String input = "prog Test2 int ab; int ac; string sim; int a; int b; if(a>b) if(ab>ac) a = ab + \"sima\"; fi; else a = a + 1; fi; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		ContextException ASTNode = null;
		try {
			AST ast = parser.parse();
			ASTVisitor contextChecker = new ContextCheckVisitor();
			ast.visit(contextChecker,null);
		}
		catch(Exception ErrorAST) {
			ASTNode = (ContextException)ErrorAST;
			//ErrorAST.printStackTrace();
			System.out.println(ErrorAST.getMessage() + " AST Node: " + ASTNode.ast.getClass().getName());
		}
		assertNotNull(ASTNode);
	}	
	
	@Test
	public void testString() {
		//String input = "prog Test2 string sim; string a; int ab; a = ab + \"sima\"; gorp";
		String input = " prog Test1 map [int, map[string, boolean]] m; map[string, boolean] m1; m1 = {[\"as\" + \"qr\", true != false]}; int a; string s1; m ={[a - a * a / a, m1 + m1]};  gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		ContextException ASTNode = null;
		try {
			AST ast = parser.parse();
			ASTVisitor contextChecker = new ContextCheckVisitor();
			ast.visit(contextChecker,null);
		}
		catch(Exception ErrorAST) {
			ASTNode = (ContextException)ErrorAST;
			//ErrorAST.printStackTrace();
			System.out.println(ErrorAST.getMessage() + " AST Node: " + ASTNode.ast.getClass().getName());
		}
		assertNull(ASTNode);
	}	
}



