package edu.ufl.cise.cop5555.sp12.ast;

public class Pair extends AST {
	
	public final Expression expression0;
	public final Expression expression1;
	public Type type1;
	public Type type2;

	public Pair(Expression expression0, Expression expression1) {
		this.expression0 = expression0;
		this.expression1 = expression1;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitPair(this,arg);
	}

}
