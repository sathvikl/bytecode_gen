package edu.ufl.cise.cop5555.sp12;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ufl.cise.cop5555.sp12.Scanner;
import edu.ufl.cise.cop5555.sp12.Parser;
import edu.ufl.cise.cop5555.sp12.TokenStream;
import edu.ufl.cise.cop5555.sp12.ast.*;

public class TestSimpleParser {
   
	private TokenStream getInitializedTokenStream(String input) {
		TokenStream stream = new TokenStream(input);
		Scanner s = new Scanner(stream);
		s.scan();
		return stream;
	}
	
	
	@Test
	public void testEmptyProgg(){
		String input = "prog Test1 ; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			
		}
	}

/*	
	@Test
	public void testIntDecg() {
		String input = "prog Test1 int x; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		SimpleParser parser = new SimpleParser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			
		}
		catch(Exception Error) {
			
		}
		//assertNull(result);
	}
	
	@Test
	public void testBooleanDecg(){
		String input = "prog Test1 boolean x; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		SimpleParser parser = new SimpleParser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			
		}		
		//SyntaxException result = parser.parse();
		//assertNull(result);
	}
	
	@Test
	public void testMapDecg(){
		String input = "prog Test1 map[int,string] y; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		SimpleParser parser = new SimpleParser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			
		}	
		//SyntaxException result = parser.parse();
		//assertNull(result);
	}
	
	@Test
	public void testMapDec2g(){
		String input = "prog Test1 map[int,map[string,boolean]] m; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		SimpleParser parser = new SimpleParser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			
		}
		///SyntaxException result = parser.parse();
		//assertNull(result);
	}
	
	@Test
	public void xtestMapDec2g(){
		String input = "prog Test1 map[int,map[string,map[int,boolean]]] sat; gorp";  //semi is the error, should be ident
		TokenStream stream = getInitializedTokenStream(input);
		SimpleParser parser = new SimpleParser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNotNull(result);
		//assertEquals(SEMI, result.t.kind);
	}
	
	@Test
	public void testEmptyProg() {
		String input = "";
		TokenStream stream = getInitializedTokenStream(input);
		SimpleParser parser = new SimpleParser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertEquals(EOF, result.t.kind);
	}

	@Test
	public void testEmptyProg0() {
		String input = "prog Test1 gorp";
		TokenStream stream = getInitializedTokenStream(input);
		SimpleParser parser = new SimpleParser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			
		}
		
		//SyntaxException result = parser.parse();
		//assertNull(result);
	}

	@Test
	public void testEmptyProg1() {
		String input = "prog";
		TokenStream stream = getInitializedTokenStream(input);
		SimpleParser parser = new SimpleParser(stream);
		SyntaxException result = parser.parse();
		assertNotNull(result);
		assertEquals(EOF, result.t.kind);
	}

	@Test
	public void testEmptyProg2() {
		String input = "prog abc";
		TokenStream stream = getInitializedTokenStream(input);
		SimpleParser parser = new SimpleParser(stream);
		SyntaxException result = parser.parse();
		assertNotNull(result);
		assertEquals(EOF, result.t.kind);
	}

	@Test
	public void testEmptyProg3() {
		String input = "prog abc gorp";
		TokenStream stream = getInitializedTokenStream(input);
		SimpleParser parser = new SimpleParser(stream);
		SyntaxException result = parser.parse();
		assertNull(result);
	}

	@Test
	public void testIntDec() {
		String input = "prog Test1 int x; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		SimpleParser parser = new SimpleParser(stream);
		SyntaxException result = parser.parse();
		assertNull(result);
	}

	@Test
	public void testBooleanDec() {
		String input = "prog Test1 boolean x; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		SimpleParser parser = new SimpleParser(stream);
		SyntaxException result = parser.parse();
		assertNull(result);
	}

	@Test
	public void testBooleanStringDec() {
		String input = "prog Test1 boolean x; int y; string d; ; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		SimpleParser parser = new SimpleParser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNotNull(result);
		//assertEquals(GORP, result.t.kind);
	}

	@Test
	public void testCommand() {
		String input = "prog Test1 abc=12;def=14; boolean y; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		SimpleParser parser = new SimpleParser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNull(result);
	}

	@Test
	public void testPrintCommand() {
		String input = "prog Test1 print a; println f; ;def=14;gorp";
		TokenStream stream = getInitializedTokenStream(input);
		SimpleParser parser = new SimpleParser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNull(result);
	}

	@Test
	public void semiColonTest() {
		String input = "prog abc ngh=14; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		SimpleParser parser = new SimpleParser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNull(result);
	}	
	
	@Test
	public void testMapDec() {
		String input = "prog Test1 map[int,string] y; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		SimpleParser parser = new SimpleParser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNull(result);
	}

	@Test
	public void testMapDec2() {
		String input = "prog Test1 map[int,map[string,boolean]] m; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		SimpleParser parser = new SimpleParser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNull(result);
	}

	@Test
	public void xtestMapDec2() {
		String input = "prog Test1 map[int,map[string,boolean]] kl; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		SimpleParser parser = new SimpleParser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNotNull(result);
		//assertEquals(SEMI, result.t.kind);
	}
*/
	@Test
	public void testDo1() {
		String input = "prog Test1 map[int,map[string,boolean]] m; do (id) od; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNotNull(result);
		//assertEquals(GORP, result.t.kind);
	}

	@Test
	public void testDo2() {
		String input = "prog Test1 map[int,map[string,boolean]] m; do (\"str\" * true) od; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNull(result);
		// assertEquals(GORP, result.t.kind);
	}

	@Test
	public void testDo3() {
		String input = "prog Test1 do str:[a,b] od; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNotNull(result);
		//assertEquals(GORP, result.t.kind);
	}

	@Test
	public void testPairList() {
		String input = "prog Test1 ab ={[ab<cd,ab&cd],[ab<cd,ab&cd]}; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNotNull(result);
		//assertEquals(SEMI, result.t.kind);
	}

	@Test
	public void testIf() {
		String input = "prog Test1 if (ab<cd) else ab ={[ab<cd,ab&cd],[ab<cd,ab&cd]}; fi; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNull(result);
	}
	
	@Test
	public void myTest1(){
		String input = "prog Test1 map[int,map[string,boolean]] test; gorp";  //prog is written wrong
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNotNull(result);
		//System.out.println(result.t.kind);
		//assertEquals(IDENTIFIER, result.t.kind);//?????????????????
	}
	@Test
	public void myTest2(){
		String input = "prog Test1 map[int,map[string,boolean]] test; gorp";  //missing prog
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNotNull(result);
		//System.out.println(result.t.kind);
		//assertEquals(IDENTIFIER, result.t.kind);//?????????????????
	}
	@Test
	public void myTest3(){
		String input = "prog Test1 map[int,map[string,boolean]] test; gorp";  //success
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNull(result);
		//System.out.println(result.t.kind);
	//	assertEquals(IDENTIFIER, result.t.kind);//?????????????????
	}
	@Test
	public void myTest4(){
		String input = "prog progname map[int,map[string,boolean]] test; gorp";  //missing identifier after prog
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNotNull(result);
		//System.out.println(result.t.kind);
	//	assertEquals(IDENTIFIER, result.t.kind);//?????????????????type is map?
	}
	@Test
	public void myTest5(){
		String input = "prog abc123 map[string,map[string,map[int,boolean]]] test; gorp";  //success
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNull(result);
		//System.out.println(result.t.kind);
	//	assertEquals(IDENTIFIER, result.t.kind);//?????????????????type is map?
	}
	@Test
	public void myTest6(){
		String input = "prog abc123 map[string,map[string,map[int,boolean]]] test; gorp";  //gorp written wrong
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNotNull(result);
		//System.out.println(result.t.kind);
		//assertEquals(EOF, result.t.kind);//?????????????????type is identifier
	}
	@Test
	public void myTest7(){
		String input = "prog abc123 print false;gorp";  //success
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNull(result);
		//System.out.println(result.t.kind);
		//assertEquals(IDENTIFIER, result.t.kind);//?????????????????type is identifier
	}
	@Test
	public void myTest8(){
		String input = "prog abc123 map[string,map[string,map[int,boolean]]] test ; print false ; gorp";  //success
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNull(result);
		//System.out.println(result.t.kind);
		//assertEquals(IDENTIFIER, result.t.kind);//?????????????????type is identifier
	}
	@Test
	public void myTest9(){
		String input = "prog abc123 map[string,map[string,map[int,boolean]]] test ; do(-3) od; println true; gorp";  //success
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNull(result);
		//System.out.println(result.t.kind);
		//assertEquals(IDENTIFIER, result.t.kind);//?????????????????type is identifier
	}
	@Test
	public void CheckTest10(){
		String input = "prog abc123 map[string,map[string,map[int,boolean]]] test ; do((23*15/3-2+\"ad23\\n\\tijk\"-false&7/8)) od; println true; gorp";  //success
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNull(result);
		//System.out.println(result.t.kind);
		//assertEquals(IDENTIFIER, result.t.kind);//?????????????????type is identifier
	}
	@Test
	public void CheckTest11(){
		String input = "prog abc123 map[string,map[string,map[int,boolean]]] test ; if((23*15/3-2+\"ad23\\n\\tijk\">=7/8)) fi; println true; gorp";  //success
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNotNull(result);
		//System.out.println(result.t.kind);
		//assertEquals(IDENTIFIER, result.t.kind);//?????????????????type is identifier
	}
	@Test
	public void CheckTest12(){
		String input = "prog abc123 map[string,map[string,map[int,boolean]]] test ; if((23*15/3-2+\"ad23\\n\\tijk\"<7/8) fi; println true; gorp";  //success
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNotNull(result);
		//System.out.println(result.t.kind);
		//assertEquals(FI, result.t.kind);//?????????????????type is identifier
	}
	@Test
	public void CheckTest13(){
		String input = "prog abc123 map[string,map[string,map[int,boolean]]] test ; if((23*15/3-2+\"ad23\\n\\tijk\"&7/8)) else s=1 fi; println true; gorp";  //success
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNull(result);
		//System.out.println(result.t.kind);
		//assertEquals(RIGHT_PAREN, result.t.kind);//?????????????????type is identifier
	}
	@Test
	public void CheckTest14(){
		String input = "prog abc123 map[string,map[string,map[int,boolean]]] test ; if((23*15/3-2+\"ad23\\n\\tijk\"&7/8)) else hj fi; println true; gorp";  //success
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNotNull(result);
		//System.out.println(result.t.kind);
		//assertEquals(RIGHT_PAREN, result.t.kind);//?????????????????type is identifier
	}

	//===============================================================================
	
	@Test
	public void liuTest1(){
		String input = "prog Test1 map[int,map[string,boolean]] m;;;;; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNull(result);
		//System.out.println(result.t.kind);
	}
	
	@Test
	public void liuTest2(){
		String input = "prog Test1 int x = 2;  gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNotNull(result);
		//assertEquals(ASSIGN, result.t.kind);
	}
	
	@Test
	public void liuTest3(){
		String input = "prog name gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNotNull(result);
		//assertEquals(EOF, result.t.kind);
	}
	
	@Test
	public void liuTest4(){
		String input = "prog name ;";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNotNull(result);
		//assertEquals(EOF, result.t.kind);
	}
	
	@Test
	public void liuTest5(){
		String input = "prog name print x + 1; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNull(result);
	}
	
	@Test
	public void liuTest6(){
		String input = "prog name print x + \"y\"; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNull(result);
	}
	
	@Test
	public void liuTest7(){
		String input = "prog name print x+1+h ; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNotNull(result);
		//assertEquals(PLUS, result.t.kind);
	}
	
	@Test
	public void liuTest8(){
		String input = "prog name print (((5) + 3) * x + -y / !c) < 0; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNull(result);
		//System.out.println(result.t.kind);
	}
	
	@Test
    // empty block after if
	public void liuTest9(){
		String input = "prog name if(x); fi; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		 
		//assertNull(result);
	}
	
	@Test
    // empty command in block after if
	public void liuTest10(){
		String input = "prog name if(x);;; fi;;; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		 
		//assertNull(result);
		//System.out.println(result.t.kind);
	}
	
	@Test
	// should be od ; fi
	public void liuTest11(){
		String input = "prog name if(x) x[i+1] = y + 5; else do(x<9) x=x-1; od; fi; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		 
		//assertNotNull(result);
		//assertEquals(FI, result.t.kind);
	}
	
	@Test
	public void liuTest12(){
		String input = "prog name gorp;";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		 
		//assertNotNull(result);
		//assertEquals(SEMI, result.t.kind);
	}
	
	@Test
	public void liuTest13(){
		String input = "prog name a = {[x,y]}; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		 
		//assertNull(result);
	}
	
	@Test
	public void liuTest14(){
		String input = "prog name a = {[x,y], [i+1, array[1]]}; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		 
		//assertNull(result);
		//assertEquals(INTEGER_LITERAL, result.t.kind);
	}
	
	@Test
	public void SLTest1() {
		String input = "prog Test1 if (ab<cd) else ab ={ab,}; fi; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNotNull(result);
	}	
	
	@Test
	public void SLTest2() {
		String input = "prog Test1 if (ab<cd) do lin[!5/(true)--k]:[a,er] lin[!5/(true)--k] = {[x,y], [i+1, array[1]]}; od; else ab ={[a,b]}; fi; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNull(result);
	}	
	
	@Test
	public void SLTest3() {
		String input = "prog Test1 lin[!5/(true)--k+] = {[x,y], [i+1, array[1]]}; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNotNull(result);
	}	

	@Test
	public void SLTest4() {
		String input = "prog Test1 if (ab<cd) do lin[!5/(true)--k]:[a,er] lin[!5/(true)--k+!a*a] = {[x,y], [i+1, array[1]]}; od; else ab ={[a,b]}; fi; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNull(result);
	}	
	
	@Test
	public void SLTest5() {
		String input = "prog Test1 lin[!5/(true)--k] = {[x,y], [i+1, array[1]]}; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNull(result);
	}
	
	@Test
	public void FTest1() {
		String input = "prog Test1 kl = 23*15/3-2>=5; gorp";
		TokenStream stream = getInitializedTokenStream(input);
		Parser parser = new Parser(stream);
		try {
			AST ast = parser.parse();
			String ASTstring = ToStringVisitor.printAST(ast);
			//assertEquals(ASTstring , "prog Test1");
		}
		catch(Exception Error) {
			assertNotNull(Error);
		}		
		//assertNotNull(result);
		//assertEquals(PRINTLN, result.t.kind);
	}	
	
}
