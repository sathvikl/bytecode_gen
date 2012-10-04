package edu.ufl.cise.cop5555.sp12.ast;

import edu.ufl.cise.cop5555.sp12.TokenStream.Token;

public class ExprLValue extends LValue {
	
	public final Token identifier;
	public final Expression expression;
	public Type type;

	public ExprLValue(Token identifier, Expression expression) {
		this.identifier = identifier;
		this.expression = expression;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitExprLValue(this,arg);
	}

}
