package edu.ufl.cise.cop5555.sp12.ast;

import java.util.List;

public class Block extends AST {
	
	public List<DecOrCommand> decOrCommands;
	
	public Block(List<DecOrCommand> decOrCommands){
		this.decOrCommands = decOrCommands;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitBlock(this,arg);
	}
	
	

}
