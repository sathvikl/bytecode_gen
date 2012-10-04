package edu.ufl.cise.cop5555.sp12.ast;

public class PrintCommand extends Command {

	public final Expression expression;
	
	public PrintCommand(Expression expression) {
		this.expression = expression;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitPrintCommand(this,arg);
	}

}
