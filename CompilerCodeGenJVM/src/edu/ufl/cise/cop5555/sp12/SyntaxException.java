package edu.ufl.cise.cop5555.sp12;

import edu.ufl.cise.cop5555.sp12.TokenStream.Token;

@SuppressWarnings("serial")
public class SyntaxException extends Exception {

	public Token t;

	public SyntaxException(Token t, String msg) {
		super(msg);
		this.t=t;
	}

	public String toString(){ return super.toString() + "\n" + t.toString();}
}


