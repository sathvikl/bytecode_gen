package edu.ufl.cise.cop5555.sp12;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.List;

public class TokenStream {
	
	
	//This inner class provides an Iterator over the recognized tokens.  
	//Instances cannot be created directly (the constructor is private); use the public iterator() method.
	public class TokenStreamIterator implements ListIterator<Token>{
		
		int i = 0;
		
		private TokenStreamIterator(){
			super();
		}
		
		@Override
		public boolean hasNext() {
			return i< tokens.size();
		}

		@Override
		public Token next() {
			return tokens.get(i++);
		}
		
		@Override		
		public Token previous() {
			return tokens.get(--i);
		}

		@Override
		public void add(Token T) {
			throw new UnsupportedOperationException();	
		}
		
		@Override
		public void set(Token T) {
			throw new UnsupportedOperationException();	
		}
		
		@Override
		public boolean hasPrevious() {
			throw new UnsupportedOperationException();	
		}
				
		@Override
		public int previousIndex() {
			throw new UnsupportedOperationException();	
		}

		@Override
		public int nextIndex() {
			throw new UnsupportedOperationException();	
		}
				
		@Override
		public void remove() {
			throw new UnsupportedOperationException();			
		}		
		
	}
	
	//This method returns a new TokenStreamIterator for this TokenStream instance.
	public TokenStreamIterator iterator(){
		return new TokenStreamIterator();
	}

	//This exception may be thrown when there are errors in a String Literal
	@SuppressWarnings("serial")
	public class IllegalStringLiteralException extends Exception {
		public IllegalStringLiteralException(String msg) {
			super(msg);
		}
	}

	char[] inputChars;
	public final List<Token> tokens;
	public int[] lineBreaks;

	//constructor that takes an array of chars
	public TokenStream(char[] inputChars) {
		this.inputChars = inputChars;
		tokens = new ArrayList<Token>();
	    //TODO: complete constructor if necessary
	}

	//constructor that takes a Reader.  
	//Pass this a FileReader to read input from a file.
	public TokenStream(Reader r) {
		this.inputChars = getChars(r);
		tokens = new ArrayList<Token>();
	    //TODO: complete constructor if necessary
	}

	//constructor that takes a String.
	public TokenStream(String inputString) {
		int length = inputString.length();
		inputChars = new char[length];
		inputString.getChars(0, length, inputChars, 0);
		tokens = new ArrayList<Token>();
	    //TODO: complete constructor if necessary
	}


	//utility method
	//read all the characters from the given Reader into a char array.
	private char[] getChars(Reader r) {
		StringBuilder sb = new StringBuilder();
		try {
			int ch = r.read();
			while (ch != -1) {
				sb.append((char) ch);
				ch = r.read();
			}
		} catch (IOException e) {
			throw new RuntimeException("IOException");
		}
		char[] chars = new char[sb.length()];
		sb.getChars(0, sb.length(), chars, 0);
		return chars;
	}


	// This is a non-static inner class. Each instance is implicitly linked with an
	// instance of StreamToken and can access that instance's variables.
	// Note usage:
	//     TokenStream stream = ...
	//     Token t = stream.new Token(....)

	public class Token {
		public final Kind kind;
		public final int beg;
		public final int end;
		public int lno;

		public Token(Kind kind, int beg, int end) {
			this.kind = kind;
			this.beg = beg;
			this.end = end;
		}

		// this should only be applied to Tokens with kind==INTEGER_LITERAL
		public int getIntVal() throws NumberFormatException,
				IllegalStringLiteralException {
			assert kind == Kind.INTEGER_LITERAL : "attempted to get value of non-number token";
			return Integer.valueOf(getText());
		}

		// removes quotes and handles escapes
		String getStringVal() throws IllegalStringLiteralException {
			assert kind == Kind.STRING_LITERAL : "attempted to get string value of non-string token";
			StringBuilder sb = new StringBuilder();
			assert inputChars[beg] == '"' && inputChars[end - 1] == '"' : "malformed STRING_LITERAL token";

			for (int i = beg + 1; i < end - 1; i++) {
				char curr = inputChars[i];
				if (curr == '\\') {
					char ch = inputChars[++i];
					switch (ch) { // \ t | \n | \f | \r | \" | \\
					case 't':
						sb.append('\t');
						break;
					case 'n':
						sb.append('\n');
						break;
					case 'f':
						sb.append('\f');
						break;
					case 'r':
						sb.append('\r');
						break;
					case '\"':
						sb.append('\"');   //note that the quote is escaped for Java.  Only the " will be appended
						break;
					case '\\':
						sb.append('\\');   //note that the \ is escaped for Java.  Only one \ will be appended.
						break;
					default:
						throw new IllegalStringLiteralException(
								"attempted to escape ch in String");
					}
				} else
					sb.append(curr);
			}
			return sb.toString();
		}

		
		
		public int getLineNumber() {
			/*
			 * A line termination character sequence is a character or character pair
			 * from the set: \n, \r, \r\n, \u0085, \u2028, and \u2029.
			 */
			
			//TODO: IMPLEMENT ME
			return lno;
		}

		// returns string containing raw text of token. 
		//precondition: beg <= end, end < inputChars.length
		String getRawText() {
			assert (beg <= end &&  end <= inputChars.length):"called getRatText with invalid beg and end";
			return String.valueOf(inputChars, beg, end - beg);
		}

		// converts to String, if String literal, removes delimiting "s and handles escapes.
		// if token is error, surrounds text with %# #%, if EOF, the text is EOF.
		public String getText() throws IllegalStringLiteralException {
			if (kind == Kind.STRING_LITERAL) {
				return getStringVal();
			}
			if (kind.isError()) {
				return "%#" + getRawText() + "%#";
			}
			if (kind == Kind.EOF) {
				return "EOF";
			}
			return getRawText();
		}

		public String toString() {
			return (new StringBuilder("<").append(kind).append(",")
					.append(getRawText()).append(",").append(beg).append(",")
					.append(end).append(">")).toString();
		}

		public boolean equals(Object o) {
			if (!(o instanceof TokenStream.Token))
				return false;
			Token other = (Token) o;
			try {
				return kind.equals(other.kind) && getText().equals(other.getText());
			} catch (IllegalStringLiteralException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
}
