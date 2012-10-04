package edu.ufl.cise.cop5555.sp12.ast;

import edu.ufl.cise.cop5555.sp12.TokenStream.Token;

public class StringLiteralExpression extends Expression {

	public final Token stringLiteral;	

	public StringLiteralExpression(Token stringLiteral) {
		this.stringLiteral = stringLiteral;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitStringLiteralExpression(this,arg);
	}
}
