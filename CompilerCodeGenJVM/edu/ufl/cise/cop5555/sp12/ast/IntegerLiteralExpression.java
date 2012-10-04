package edu.ufl.cise.cop5555.sp12.ast;

import edu.ufl.cise.cop5555.sp12.TokenStream.Token;

public class IntegerLiteralExpression extends Expression {
	
	public final Token integerLiteral;

	public IntegerLiteralExpression(Token integerLiteral) {
		this.integerLiteral = integerLiteral;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIntegerLiteralExpression(this,arg);
	}

}
