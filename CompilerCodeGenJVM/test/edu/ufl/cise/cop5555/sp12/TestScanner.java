package edu.ufl.cise.cop5555.sp12;


import java.util.StringTokenizer;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.ufl.cise.cop5555.sp12.Scanner;
import edu.ufl.cise.cop5555.sp12.TokenStream;
import edu.ufl.cise.cop5555.sp12.TokenStream.IllegalStringLiteralException;
import edu.ufl.cise.cop5555.sp12.TokenStream.Token;
import edu.ufl.cise.cop5555.sp12.TokenStream.TokenStreamIterator;
import static edu.ufl.cise.cop5555.sp12.Kind.*;

public class TestScanner {

	// This expected values of the text of the input tokens are delineated by '~'.  Thus this method cannot
	// handle programs containing '~' in comments or strings.  Fixing this is not worth the trouble.
	private void compareText(TokenStream stream, String expected)
			throws IllegalStringLiteralException {
		StringTokenizer expectedTokenizer = new StringTokenizer(expected, "~");
		TokenStreamIterator iter = stream.iterator();
		while (iter.hasNext()) {
			assertEquals(expectedTokenizer.nextElement(), iter.next().getText());
		}
		assertFalse(expectedTokenizer.hasMoreElements());
	}

	private void compareKinds(TokenStream stream, Kind[] expected) {
		TokenStreamIterator iter = stream.iterator();
		int i = 0;
		while (iter.hasNext()) {
			assertEquals(expected[i++], iter.next().kind);
		}
		assertTrue(expected.length == i);
	}

	private void compareLineNumbers(TokenStream stream, String expected) {
		StringBuffer sb = new StringBuffer();
		for (Token t : stream.tokens) {
			if (t.kind != EOF)
				sb.append(t.getLineNumber());
		}
		String output = sb.toString();
		assertEquals(expected, output);
	}

	private TokenStream getInitializedTokenStream(String input) {
		TokenStream stream = new TokenStream(input);
		Scanner s = new Scanner(stream);
		s.scan();
		return stream;
	}

	@Test
	public void testScan0() throws IllegalStringLiteralException {
		String input = "\u001axyz";  //control z at end of file should be ignored, and yield just an EOF token
		TokenStream stream = getInitializedTokenStream(input);
		String expectedText = "EOF";
		Kind[] expectedKinds = { EOF};
		compareText(stream, expectedText);
		compareKinds(stream, expectedKinds);
	}

	@Test
	public void testScan1() throws IllegalStringLiteralException {
		String input = "";  //empty input should yield and EOF token
		String expected = "EOF";
		Kind[] expectedKinds = { EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
	}

	@Test
	public void testScan2() throws IllegalStringLiteralException {
		String input = "prog";  //prog is a keyword, should see a PROG token with text prog, followed by EOF
		String expected = "prog~EOF";
		Kind[] expectedKinds = { PROG, EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
	}

	@Test
	public void testScan3() throws IllegalStringLiteralException {
		String input = "xyz ";  //this is an identifier with text xyz followed by an EOF token
		String expected = "xyz~EOF";
		Kind[] expectedKinds = { IDENTIFIER, EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
	}

	@Test
	public void testScan4() throws IllegalStringLiteralException {
		String input = "0";
		String expected = "0~EOF";
		Kind[] expectedKinds = { INTEGER_LITERAL, EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
	}

	@Test
	public void testScan5() throws IllegalStringLiteralException {
		String input = "01";  //this is a 0 token followed by a 1 token followed by an EOF token.  01 is not legal in the language,
		  //but the error will be found by the parser, not the scanner.
		String expected = "0~1~EOF";
		Kind[] expectedKinds = { INTEGER_LITERAL, INTEGER_LITERAL, EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
		Kind[] kindarray = { INTEGER_LITERAL, INTEGER_LITERAL, EOF };
		compareKinds(stream, kindarray);
	}

	@Test
	public void testScan6() throws IllegalStringLiteralException {
		String input = "10 ";
		String expected = "10~EOF";
		Kind[] expectedKinds = { INTEGER_LITERAL, EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
	}

	@Test
	public void testScan7() throws IllegalStringLiteralException {
		String input = "10 11 xyz 12";
		String expected = "10~11~xyz~12~EOF";
		Kind[] expectedKinds = { INTEGER_LITERAL, INTEGER_LITERAL, IDENTIFIER,
				INTEGER_LITERAL, EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
	}

	@Test
	public void testScan8() throws IllegalStringLiteralException {
		String input = "10 \n 11\nxyz \n12";
		String expected = "10~11~xyz~12~EOF";
		Kind[] expectedKinds = { INTEGER_LITERAL, INTEGER_LITERAL, IDENTIFIER,
				INTEGER_LITERAL, EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
	}

	@Test
	public void testScan9() throws IllegalStringLiteralException {
		String input = "10 \r 11\rxyz \r12";
		String expected = "10~11~xyz~12~EOF";
		Kind[] expectedKinds = { INTEGER_LITERAL, INTEGER_LITERAL, IDENTIFIER,
				INTEGER_LITERAL, EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
	}

	@Test
	public void testScan10() throws IllegalStringLiteralException {
		String input = "10 \n\r 11\n\rxyz \n\r12";
		String expected = "10~11~xyz~12~EOF";
		Kind[] expectedKinds = { INTEGER_LITERAL, INTEGER_LITERAL, IDENTIFIER,
				INTEGER_LITERAL, EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
	}

	@Test
	public void testScan11() throws IllegalStringLiteralException {
		String input = "\n\r10 \n\r 11\n\rxyz \n\r12";
		String expected = "10~11~xyz~12~EOF";
		Kind[] expectedKinds = { INTEGER_LITERAL, INTEGER_LITERAL, IDENTIFIER,
				INTEGER_LITERAL, EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
	}

	@Test
	public void testScan12() throws IllegalStringLiteralException {
		String input = "\n\r  10 \n\r 11\n\rxyz \n\r12\n\r";
		String expected = "10~11~xyz~12~EOF";
		Kind[] expectedKinds = { INTEGER_LITERAL, INTEGER_LITERAL, IDENTIFIER,
				INTEGER_LITERAL, EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
	}

	@Test
	public void testScan13() throws IllegalStringLiteralException {
		String input = "\r  10 \n\r 11\n\rxyz \n\r12";
		String expected = "10~11~xyz~12~EOF";
		Kind[] expectedKinds = { INTEGER_LITERAL, INTEGER_LITERAL, IDENTIFIER,
				INTEGER_LITERAL, EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
	}

	@Test
	public void testScan14() throws IllegalStringLiteralException {
		String input = "\n  \n\n ";
		String expected = "EOF";
		Kind[] expectedKinds = { EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
	}

	@Test
	public void testScan15() throws IllegalStringLiteralException {
		String input = "xyz,123,x12,==";
		String expected = "xyz~,~123~,~x12~,~==~EOF";
		Kind[] expectedKinds = { IDENTIFIER, COMMA, INTEGER_LITERAL, COMMA,
				IDENTIFIER, COMMA, EQUALS, EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
	}

	@Test
	public void testScan16() throws IllegalStringLiteralException {
		String input = "<=<==>";
		String expected = "<=~<=~=~>~EOF";
		Kind[] expectedKinds = { AT_MOST, AT_MOST, ASSIGN, GREATER_THAN, EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
	}

	@Test
	public void testScan17() throws IllegalStringLiteralException {
		String input = "true+false";
		String expected = "true~+~false~EOF";
		Kind[] expectedKinds = { BOOLEAN_LITERAL, PLUS, BOOLEAN_LITERAL, EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
	}

	@Test
	public void testScan18() throws IllegalStringLiteralException {
		String input = "true##+#+##false";
		String expected = "true~false~EOF";
		Kind[] expectedKinds = { BOOLEAN_LITERAL, BOOLEAN_LITERAL, EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
	}

	@Test
	public void testScan19() throws IllegalStringLiteralException {
		String input = "true##+##+##false";
		String expected = "true~+~%###false%#~EOF";
		Kind[] expectedKinds = { BOOLEAN_LITERAL, PLUS, MALFORMED_COMMENT, EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
	}
	
	@Test
	public void testScan20() throws IllegalStringLiteralException {
		String input = "\"test+string\"";
		String expected = "test+string~EOF";
		Kind[] expectedKinds = { STRING_LITERAL, EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
	}
	
	@Test
	public void testScan21() throws IllegalStringLiteralException {
		String input = "\"abc\\def\r\nrad\"abc";
		String expected = "%#\"abc\\%#~def~rad~%#\"abc%#~EOF";
		Kind[] expectedKinds = { MALFORMED_STRING, IDENTIFIER, IDENTIFIER, MALFORMED_STRING, EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
	}	

	@Test
	public void testScan22() throws IllegalStringLiteralException {
		String input = "xyz+0#52abc\"abc\"";
		String expected = "xyz~+~0~%##%#~52~abc~abc~EOF";
		Kind[] expectedKinds = { IDENTIFIER, PLUS, INTEGER_LITERAL, MALFORMED_COMMENT, INTEGER_LITERAL, IDENTIFIER, STRING_LITERAL, EOF };
		TokenStream stream = getInitializedTokenStream(input);
		compareText(stream, expected);
		compareKinds(stream, expectedKinds);
	}
	
	@Test
	public void testErrorInput0() throws IllegalStringLiteralException {
		String input = "abc def @ 123"; 
		Kind[] expectedKinds = { IDENTIFIER, IDENTIFIER, ILLEGAL_CHAR, INTEGER_LITERAL, EOF };
		TokenStream stream = getInitializedTokenStream(input);
		// compareText(stream, expected); We won't worry about exactly what text goes into error tokens.
		compareKinds(stream, expectedKinds);
	}
	
	@Test 
	public void testErrorInput1() throws IllegalStringLiteralException {
		String input = "\"abc\\\"";  //This is tricky.  Removing the escapes in the String needed for Java, we have input "abc\"
		                             //including the quotes.  "abc\" is the what we would have if the input were going to be read
		                             // from a file.  However, the final quote is escaped so it is a quote within the string
		                             //
		Kind[] expectedKinds = {MALFORMED_STRING, EOF};
		TokenStream stream = getInitializedTokenStream(input);
		compareKinds(stream, expectedKinds);
	}


	@Test
	public void testLineNumbers0() {
		String input = "abd\r\ndef++a123\nabd";
		TokenStream stream = getInitializedTokenStream(input);
		String expected = "122223";
		compareLineNumbers(stream, expected);
	}
}
