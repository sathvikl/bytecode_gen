package edu.ufl.cise.cop5555.sp12.ast;

import edu.ufl.cise.cop5555.sp12.Kind;

public class UnaryOpExpression extends Expression {
	
	public final Kind op;
	public final Expression expression;
	
	public UnaryOpExpression(Kind op, Expression expression) {
		this.op = op;
		this.expression = expression;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitUnaryOpExpression(this,arg);
	}

}
