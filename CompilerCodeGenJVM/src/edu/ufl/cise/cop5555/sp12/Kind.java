package edu.ufl.cise.cop5555.sp12;

public enum Kind {
	IDENTIFIER,
	/* keywords */
	PROG, GORP, STRING, INT, BOOLEAN, MAP, IF, ELSE, FI, DO, OD, PRINT, PRINTLN, 
	/* literals */
	INTEGER_LITERAL, STRING_LITERAL, BOOLEAN_LITERAL,  //true and false in input return Token of kind BOOLEAN_LITERAL
	/* separators */
	DOT, SEMI, COMMA, LEFT_PAREN, RIGHT_PAREN, LEFT_SQUARE, RIGHT_SQUARE, LEFT_BRACE, RIGHT_BRACE, COLON,
	/* operators */
	ASSIGN, OR, AND, EQUALS, NOT_EQUALS, LESS_THAN, GREATER_THAN, AT_MOST, AT_LEAST, PLUS, MINUS, TIMES, DIVIDE, NOT, EOF, 
	
	ILLEGAL_CHAR { //Used when there is an illegal character that is not in a string or comment
		@Override
		public boolean isError() {
			return true;
		}
	},
	MALFORMED_STRING { //Used when there is an illegal character in string (a lone /) or EOF is reached before string terminated 
		@Override
		public boolean isError() {
			return true;
		}
	},
	MALFORMED_COMMENT { //Used when there is a lone # in a comment, or EOF is reached before comment finished
		@Override
		public boolean isError() {
			return true;
		}
	};
	public boolean isError() {
		return false;
	}
}