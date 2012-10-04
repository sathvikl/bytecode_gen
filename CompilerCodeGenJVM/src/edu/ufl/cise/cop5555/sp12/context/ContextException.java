package edu.ufl.cise.cop5555.sp12.context;

import edu.ufl.cise.cop5555.sp12.ast.AST;

@SuppressWarnings("serial")
public class ContextException extends Exception {
	
	public AST ast;

	public ContextException(AST ast, String message) {
		super(message);
		this.ast = ast;
	}

}
