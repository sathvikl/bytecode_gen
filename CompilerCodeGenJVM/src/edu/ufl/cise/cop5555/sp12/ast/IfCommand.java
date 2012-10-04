package edu.ufl.cise.cop5555.sp12.ast;

public class IfCommand extends Command {

	public final Expression expression;
	public final Block block;
	
	public IfCommand(Expression expression, Block block) {
		this.expression = expression;
		this.block = block;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIfCommand(this,arg);
	}

}
