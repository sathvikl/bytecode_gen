package edu.ufl.cise.cop5555.sp12.ast;

public class AssignExprCommand extends Command {
	
	public final LValue lValue;
	public final Expression expression;
	
	public AssignExprCommand(LValue lValue, Expression expression) {
		this.lValue = lValue;
		this.expression = expression;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitAssignExprCommand(this,arg);
	}

}
