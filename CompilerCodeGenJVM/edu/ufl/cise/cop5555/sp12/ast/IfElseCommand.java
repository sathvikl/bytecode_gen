package edu.ufl.cise.cop5555.sp12.ast;

public class IfElseCommand extends Command {
	
	public final Expression expression;
	public final Block ifBlock;
	public final Block elseBlock;

	public IfElseCommand(Expression expression, Block ifBlock, Block elseBlock) {
		this.expression = expression;
		this.ifBlock = ifBlock;
		this.elseBlock = elseBlock;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitIfElseCommand(this,arg);
	}

}
