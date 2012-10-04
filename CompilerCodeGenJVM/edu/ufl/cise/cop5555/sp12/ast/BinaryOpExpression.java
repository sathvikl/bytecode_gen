package edu.ufl.cise.cop5555.sp12.ast;

import edu.ufl.cise.cop5555.sp12.Kind;

public class BinaryOpExpression extends Expression {
	
	public final Expression expression0;
	public final Kind op;
	public final Expression expression1;


	public BinaryOpExpression(Expression expression0, Kind op, Expression expression1) {
		this.expression0 = expression0;
		this.op = op;
		this.expression1 = expression1;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitBinaryOpExpression(this, arg);
	}

}
