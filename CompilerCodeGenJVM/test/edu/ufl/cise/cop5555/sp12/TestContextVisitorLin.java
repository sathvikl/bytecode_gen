package edu.ufl.cise.cop5555.sp12;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.ufl.cise.cop5555.sp12.Scanner;
import edu.ufl.cise.cop5555.sp12.TokenStream;
import edu.ufl.cise.cop5555.sp12.TokenStream.IllegalStringLiteralException;
import edu.ufl.cise.cop5555.sp12.ast.AST;
import edu.ufl.cise.cop5555.sp12.ast.ASTVisitor;
import edu.ufl.cise.cop5555.sp12.ast.ToStringVisitor;
import edu.ufl.cise.cop5555.sp12.context.ContextCheckVisitor;
import edu.ufl.cise.cop5555.sp12.context.ContextException;

public class TestContextVisitorLin {
	
	int count = 0;
	private static TokenStream getInitializedTokenStream(String input) {
		TokenStream stream = new TokenStream(input);
		Scanner s = new Scanner(stream);
		try {
			s.scan();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stream;
	}
	
	public void getresult(String input){
		Exception e1 = null;
		try {
			TokenStream stream = getInitializedTokenStream(input);
			Parser parser = new Parser(stream);
			AST ast = null;
			ast = parser.parse();
			ASTVisitor contextChecker = new ContextCheckVisitor();
			ast.visit(contextChecker,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			e1 = e;
			//System.out.println(e1.getMessage());
		}
		
		if(e1 == null) {
			System.out.println(count + " Pass");
		} else {
			System.out.println(count + " Fail");
		}
		count++;
		return;
	}
	
	@Test
	public void testCV0() throws IllegalStringLiteralException {
		String input = "prog a int a; gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void testCV1() throws IllegalStringLiteralException {
		String input = "prog a int a; a= 5; gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV2() throws IllegalStringLiteralException {
		String input = "prog p int a; map[int,string] c; if(5>3) string a;  fi; c={[3,\"aaaa\"],[5,\"ssssss\"],[8,\"555555\"],[7,\"ooooo\"]}; string b;b = c[5];  gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV3() throws IllegalStringLiteralException {
		String input = "prog p int a; map[int,string] c; if(5>3) string a; if(true) boolean a;fi; string d; string b; d=a+5; fi; string b;b = c[5];  gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV4() throws IllegalStringLiteralException {
		String input = "prog p int a; map[int,string] c; if(5>3) string a; if(true) boolean a;fi; string d; d=a+5; d= c[8];fi; string b;b = c[5];  gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV5() throws IllegalStringLiteralException {
		String input = "prog p int a; map[int,string] c; string b;b = c[5]; f = {}; gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV6() throws IllegalStringLiteralException {
		String input = "prog p int a; map[int,string] c; string b;b = c[5]; a = {}; gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV7() throws IllegalStringLiteralException {
		String input = "prog p int a;  do (a>5) int c; boolean c;c = true; od;gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV8() throws IllegalStringLiteralException {
		String input = "prog p int a; map[int,string] c;do a:[b,c] int c; c = 3; od;gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV9() throws IllegalStringLiteralException {
		String input = "prog p int a; string b;map[int,string] c;do c:[a,c] int c; c = 3; od;gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV10() throws IllegalStringLiteralException {
		String input = "prog p int a; string b;map[int,string] c; a=c;gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV11() throws IllegalStringLiteralException {
		String input = "prog p int a; string b;map[int,string] c; b=c[c];gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV12() throws IllegalStringLiteralException {
		String input = "prog p int a; string b;map[int,string] c; b=c[5];gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV13() throws IllegalStringLiteralException {
		String input = "prog p int a; int b; string c; boolean d; if(!a) fi;b = -a; gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV14() throws IllegalStringLiteralException {
		String input = "prog p int a; int b; string c; boolean d; if(-a) fi;b = -a; gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV15() throws IllegalStringLiteralException {
		String input = "prog p int a; int b; string c; boolean d; b = -c; gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV16() throws IllegalStringLiteralException {
		String input = "prog p int a; int b; string c; boolean d; b = c; gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV17() throws IllegalStringLiteralException {
		String input = "prog p int a; int b; string c; boolean d; b = a+d; gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV18() throws IllegalStringLiteralException {
		String input = "prog p int a; int b; string c; boolean d; c = a+c; gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV19() throws IllegalStringLiteralException {
		String input = "prog p int a; int b; string c; boolean d; c = a+d; gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV20() throws IllegalStringLiteralException {
		String input = "prog p int a; int b; string c; boolean d; c = b+a; gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV21() throws IllegalStringLiteralException {
		String input = "prog p int a; int b; string c; boolean d; c = a>=a; gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV22() throws IllegalStringLiteralException {
		String input = "prog p int a; int b; string c; boolean d; d = a>=a; gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV23() throws IllegalStringLiteralException {
		String input = "prog p int a; int b; map[int,string] c; boolean d;string e; d= !(a!=b); gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV24() throws IllegalStringLiteralException {
		String input = "prog p int a; int b; map[int,string] c; boolean d;string e; d= !(a!=b); gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV25() throws IllegalStringLiteralException {
		String input = "prog p int a; int b; map[int,string] c;map[int,string] f; boolean d;string e; a = a*5; gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV26() throws IllegalStringLiteralException {
		String input = "prog p int a; int b; boolean d;string e; d = d|true; gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	
	@Test
	public void TESTCV28() throws IllegalStringLiteralException {
		String input = "prog selectsort map[int, int] array;int i; int j;  int sizeofarr;i =0; j=0; sizeofarr = 100;do (i < sizeofarr)do ( j < sizeofarr)if ( array[i] > array[j])int tmp; tmp=0;tmp = array[i];array[i] = array[j]; array[j] = array[i];fi;od;od;gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	@Test
	public void TESTCV29() throws IllegalStringLiteralException {
		String input = "prog selectsort map[int, string] array; int i; string tmp; tmp = array[i]; gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}
	@Test
	public void TESTCV30() throws IllegalStringLiteralException {
		String input = "prog Test2 map[int, string] array1; map[int, string] array2; map[int, string] K1; K1 = array1 * array2; gorp";  //control z at end of file should be ignored, and yield just an EOF token
		getresult(input);
	}


}
