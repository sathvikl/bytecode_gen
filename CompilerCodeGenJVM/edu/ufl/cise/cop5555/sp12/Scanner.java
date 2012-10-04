package edu.ufl.cise.cop5555.sp12;


import java.util.HashMap;

import edu.ufl.cise.cop5555.sp12.TokenStream.Token;
import static edu.ufl.cise.cop5555.sp12.Kind.*;


public class Scanner {
    public TokenStream charStream; 
    private int index;
    private int line_number;
    
	public Scanner(TokenStream stream)  {
		charStream = stream; 
		index = 0;
		line_number = 1;
	}

	private enum State {
		START,GOT_EQUALS, IDENT_PART, GOT_ZERO, DIGITS, EOF, 
		GOT_GREATER_THAN, GOT_LESS_THAN, GOT_NOT, GOT_COMMENT, 
		GOT_STRING, GOT_HASH
	}
	private State state;
	
	private char getch() { 
		char ch = (char)-1;
		
		if(index < charStream.inputChars.length ) {
			ch = charStream.inputChars[index];
		}
		index++;
		return ch;
	}
	
	private void ungetch() {
		//if(index < charStream.inputChars.length ) {
		index--;
		//}
	}
	
	private Token check_identifier(int begOffset, int endpoint) {
		String identifier = new String (charStream.inputChars, begOffset, endpoint - begOffset);
		Token t = null;
		
		if (identifier.compareTo("true") == 0) {
			t = charStream.new Token(BOOLEAN_LITERAL, begOffset, endpoint);
		} else if (identifier.compareTo("false") == 0) {
			t = charStream.new Token(BOOLEAN_LITERAL, begOffset, endpoint);
		} else if (identifier.compareTo("prog") == 0) {
			t = charStream.new Token(PROG, begOffset, endpoint);
		} else if (identifier.compareTo("gorp") == 0) {
			t = charStream.new Token(GORP, begOffset, endpoint);
		} else if (identifier.compareTo("string") == 0) {
			t = charStream.new Token(STRING, begOffset, endpoint);
		} else if (identifier.compareTo("int") == 0) {
			t = charStream.new Token(INT, begOffset, endpoint);
		} else if (identifier.compareTo("boolean") == 0) {
			t = charStream.new Token(BOOLEAN, begOffset, endpoint);
		}  else if (identifier.compareTo("map") == 0) {
			t = charStream.new Token(MAP, begOffset, endpoint);
		} else if (identifier.compareTo("if") == 0) {
			t = charStream.new Token(IF, begOffset, endpoint);
		} else if (identifier.compareTo("else") == 0) {
			t = charStream.new Token(ELSE, begOffset, endpoint);
		} else if (identifier.compareTo("fi") == 0) {
			t = charStream.new Token(FI, begOffset, endpoint);
		} else if (identifier.compareTo("do") == 0) {
			t = charStream.new Token(DO, begOffset, endpoint);
		} else if (identifier.compareTo("od") == 0) {
			t = charStream.new Token(OD, begOffset, endpoint);
		} else if (identifier.compareTo("print") == 0) {
			t = charStream.new Token(PRINT, begOffset, endpoint);
		} else if (identifier.compareTo("println") == 0) {
			t = charStream.new Token(PRINTLN, begOffset, endpoint);
		} else {
			t = charStream.new Token(IDENTIFIER, begOffset, endpoint);
		}
		return t;
	}
	
	private boolean check_str(int start, int end) {
		boolean result = true;
		String identifier = new String (charStream.inputChars, start+1, (end-1)-start-1);
		int i = 0;
		
		while(i < identifier.length()) {
			if(identifier.charAt(i) == '\\') {
				if(i+1 == identifier.length()) {
					return false;
				}
				if(identifier.charAt(i+1) != 'r' && identifier.charAt(i+1) != 'n' && identifier.charAt(i+1) != 'f' && 
				   identifier.charAt(i+1) != '\\'&& identifier.charAt(i+1) != 'b' && identifier.charAt(i+1) != '\"' ) {
				    return false;
			    }
				i++;
			}
			i++;
		}
		return result;
	}
	
	public Token next() {
		state = State.START;
		Token t = null;
		int begOffset = 0;
		//int endOffset = 0;
		char ch;
		
		do {
			switch(state) {
			case START :
				ch = getch();
				begOffset = index-1;
				switch(ch) {
				case (char)-1:
					state = State.EOF;
					break; // end of file.
				case ' ': case '\t':case '\f':
					break; // white space
				
				case '\n':case '\u0085':case '\u2028': case '\u2029': // increment line numbers.
					line_number++;
					break;
				case '\r':
					ch = getch();
					if(ch != '\n') {
						ungetch();	
					}
					line_number++;
					break;
				case '\u001a':
					state = State.EOF; // Ctrl + Z unicode character
					break;
				case '=':
					state = State.GOT_EQUALS;
					break;
				case '>':
					state = State.GOT_GREATER_THAN;
					break;
				case '<':
					state = State.GOT_LESS_THAN;
					break;
				case '!':
					state = State.GOT_NOT;
					break;
				case '*':
					t = charStream.new Token(TIMES, begOffset, index);
					break;
				case '|':
					t = charStream.new Token(OR, begOffset, index);
					break;
				case '&':
					t = charStream.new Token(AND, begOffset, index);
					break;
				case '+':
					t = charStream.new Token(PLUS, begOffset, index);
					break;
				case '-':
					t = charStream.new Token(MINUS, begOffset, index);
					break;
				case '/':
					t = charStream.new Token(DIVIDE, begOffset, index);
					break;					
				case '0':
					state = State.GOT_ZERO;
					break;
				//separators handled here 	
				case '.':
					t = charStream.new Token(DOT, begOffset, index);
					break;
				case ';':
					t = charStream.new Token(SEMI, begOffset, index);
					break;
				case ',':
					t = charStream.new Token(COMMA, begOffset, index);
					break;
				case '(':
					t = charStream.new Token(LEFT_PAREN, begOffset, index);
					break;
				case ')':
					t = charStream.new Token(RIGHT_PAREN, begOffset, index);
					break;
				case '[':
					t = charStream.new Token(LEFT_SQUARE, begOffset, index);
					break;
				case ']':
					t = charStream.new Token(RIGHT_SQUARE, begOffset, index);
					break;
				case '{':
					t = charStream.new Token(LEFT_BRACE, begOffset, index);
					break;
				case '}':
					t = charStream.new Token(RIGHT_BRACE, begOffset, index);
					break;
				case ':':
					t = charStream.new Token(COLON, begOffset, index);
					break;					
				case '#':
					state = State.GOT_HASH;
					break;
				case '\"':
					state = State.GOT_STRING;
					break;
				default:
					if (Character.isDigit(ch)) {
						state = State.DIGITS;
					} else if (Character.isJavaIdentifierStart(ch)) {
						state = State.IDENT_PART;
					} else {
					    //handle error
						t = charStream.new Token(ILLEGAL_CHAR, begOffset, index);
					}
				}
				break; // end of state START

			case GOT_GREATER_THAN :
				ch = getch();
				switch(ch) {
				case '=':
					t = charStream.new Token(AT_LEAST, begOffset, index);
					break;
				default:
					t = charStream.new Token(GREATER_THAN, begOffset, begOffset+1);
					ungetch();
					break;										
				}
				state = State.START;
				break;
				
			case GOT_LESS_THAN :
				ch = getch();
				switch(ch) {
				case '=':
					t = charStream.new Token(AT_MOST, begOffset, index);
					break;
				default:
					t = charStream.new Token(LESS_THAN, begOffset, begOffset+1);
					ungetch();
					break;					
				}
				state = State.START;				
				break;
				
			case GOT_NOT :
				ch = getch();
				switch(ch) {
				case '=':
					t = charStream.new Token(NOT_EQUALS, begOffset, index);
					break;
				default:
					t = charStream.new Token(NOT, begOffset, begOffset+1);
					ungetch();
					break;					
				}
				state = State.START;
				break;
				
			case GOT_EQUALS :
				ch = getch();
				switch(ch) {
				case '=':
					t = charStream.new Token(EQUALS, begOffset, index);
					break;
				default: 
					t = charStream.new Token(ASSIGN, begOffset, begOffset+1);
					ungetch();
					break;
				}
				state = State.START;			
				break;
				
			case DIGITS :
				ch = getch();
				if (Character.isDigit(ch)) {
					state = State.DIGITS;
				} else {
					ungetch();
					t = charStream.new Token(INTEGER_LITERAL, begOffset, index);
					state = State.START;
				}
				break;
				
			case GOT_ZERO :
				t = charStream.new Token(INTEGER_LITERAL, begOffset, index);
				state = State.START;
				break;
				
			case IDENT_PART :
				ch = getch();
				if(Character.isJavaIdentifierPart(ch)) {
					state = State.IDENT_PART;
				} else {					
					ungetch();
					t = check_identifier(begOffset, index);
					state = State.START;
				}
				break;
			
			case GOT_STRING:
				ch = getch();
				switch(ch) {
				case '\r': case '\n': case (char)-1:
					ungetch();
					t = charStream.new Token(MALFORMED_STRING, begOffset, index);
					state = State.START;
					break;
				case '\"':
					t = charStream.new Token(STRING_LITERAL, begOffset, index);
					state = State.START;
					break;
				case '\\':
					ch = getch();
					if( ch != 'r' && ch != 'n' && ch != 'f' && 
					    ch != '\\'&& ch != 't' && ch != '\"' ) {
						ungetch();
						t = charStream.new Token(MALFORMED_STRING, begOffset, index);
						state = State.START;
					}
					break;
				}
			    break;// end of GOT_STRING 
			    
			case GOT_HASH:
				ch = getch();
				if(ch == '#') {
					state = State.GOT_COMMENT;
				} else {
					ungetch();
					t = charStream.new Token(MALFORMED_COMMENT, begOffset, index);
					state = State.START;
					//This a malformed comment .. don't know what action to take
				}
				break;
			
			case GOT_COMMENT :
				ch = getch();
				if(ch == '#') {       // Closing comment first #
					ch = getch();
					if(ch == '#') {
						state = State.START;
					}
				} else if(ch == (char)-1) {
					//This is a malformed comment 
					ungetch();
					t = charStream.new Token(MALFORMED_COMMENT, begOffset, index);
					state = State.START;
				}
				break;
				
			case EOF :
				//ungetch();
				t = charStream.new Token(EOF, begOffset, begOffset);
				break;
			default:
				assert false: "should not reach here";
			}
		} while(t == null); // come out of the loop as soon as a token is created.
		return t;
	}
	
	public void scan() {
		Token t;
		
		do {
			t = next();
			t.lno = line_number; 
			charStream.tokens.add(t);
		} while(!t.kind.equals(EOF));	
	}

   //You will need to add fields and additional methods
	
}
