package edu.ufl.cise.cop5555.sp12.ast;

public class CompoundType extends Type {
	
	public final SimpleType keyType;
	public final Type valType;

	public CompoundType(SimpleType keyType, Type valType) {
		this.keyType = keyType;
		this.valType = valType;
	}
	
	public CompoundType(CompoundType Type) {
		this.keyType = Type.keyType;
		this.valType = Type.valType;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitCompoundType(this, arg);
	}
	
	public boolean equalsTo(Type OpB) {
		if(OpB == null) {
			return false;
		}
		
		if(OpB instanceof SimpleType) {
			return false;
		}
		// OpB is a compound type; Checkif the two compound types are now equal or not 
		if(keyType.equalsTo(((CompoundType)OpB).keyType) && valType.equalsTo(((CompoundType)OpB).valType) ) {
			return true;	
		} else {
			return false;
		}
	}

}
