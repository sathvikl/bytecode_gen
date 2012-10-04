package edu.ufl.cise.cop5555.sp12.ast;

import edu.ufl.cise.cop5555.sp12.Kind;

public class SimpleType extends Type {
	
	public Kind type;
	
	public SimpleType(Kind type){
		this.type = type;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitSimpleType(this,arg);
	}
	
	public boolean equalsTo(Type OpB) {
		if(OpB == null) {
			return false;
		}
		
		if(OpB instanceof CompoundType) {
			return false;
		}
		if(this.type == ((SimpleType)OpB).type) {
			return true;
		} else {
			return false;
		}	
	}
	
}
