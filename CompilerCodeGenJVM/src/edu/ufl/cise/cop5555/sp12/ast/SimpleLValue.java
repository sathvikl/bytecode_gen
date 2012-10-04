package edu.ufl.cise.cop5555.sp12.ast;

import edu.ufl.cise.cop5555.sp12.TokenStream.Token;

public class SimpleLValue extends LValue {
	
	public final Token identifier;
	public Type type;

	public SimpleLValue(Token identifier) {
		this.identifier = identifier;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitSimpleLValue(this,arg);
	}

}
