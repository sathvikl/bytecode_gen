package edu.ufl.cise.cop5555.sp12.ast;

import edu.ufl.cise.cop5555.sp12.TokenStream.Token;

public class BooleanLiteralExpression extends Expression {

	public final Token booleanLiteral;
	
	public BooleanLiteralExpression(Token booleanLiteral) {
		this.booleanLiteral = booleanLiteral;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitBooleanLiteralExpression(this,arg);
	}


}
