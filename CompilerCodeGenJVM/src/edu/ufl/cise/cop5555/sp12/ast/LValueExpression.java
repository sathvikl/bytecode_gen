package edu.ufl.cise.cop5555.sp12.ast;

public class LValueExpression extends Expression {

	public final LValue lValue;
	
	public LValueExpression(LValue lValue) {
		this.lValue = lValue;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitLValueExpression(this,arg);
	}

}
