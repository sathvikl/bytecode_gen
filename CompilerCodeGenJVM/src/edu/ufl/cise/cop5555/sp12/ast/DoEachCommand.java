package edu.ufl.cise.cop5555.sp12.ast;

import edu.ufl.cise.cop5555.sp12.TokenStream.Token;

public class DoEachCommand extends Command {
	
	public final LValue lValue;
	public final Token key;
	public final Token val;
	public final Block block;

	public DoEachCommand(LValue lValue, Token key, Token val, Block block){
		this.lValue = lValue;
		this.key = key;
		this.val = val;
		this.block = block;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitDoEachCommand(this,arg);
	}

}
