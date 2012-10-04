package edu.ufl.cise.cop5555.sp12.ast;

import edu.ufl.cise.cop5555.sp12.TokenStream.Token;

public class Program extends AST {
	
	public final Token ident;
	public final Block block;
	
	public Program(Token ident, Block block){
		this.ident = ident;
		this.block = block;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitProgram(this,arg);
	}

}
