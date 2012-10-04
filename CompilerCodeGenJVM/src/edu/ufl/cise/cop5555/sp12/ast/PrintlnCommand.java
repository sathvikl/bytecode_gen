package edu.ufl.cise.cop5555.sp12.ast;

public class PrintlnCommand extends Command {

	public final Expression expression;
	
	public PrintlnCommand(Expression expression) {
		this.expression = expression;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitPrintlnCommand(this,arg);
	}
}
