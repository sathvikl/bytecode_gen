package edu.ufl.cise.cop5555.sp12.ast;

import edu.ufl.cise.cop5555.sp12.TokenStream.Token;

public class Declaration extends DecOrCommand {
	
	public final Type type;
	public final Token ident;
	public int scope_number;

	public Declaration(Type type, Token ident) {
		this.type = type;
		this.ident = ident;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitDeclaration(this, arg);
	}

}
