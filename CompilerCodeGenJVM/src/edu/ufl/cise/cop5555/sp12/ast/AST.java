package edu.ufl.cise.cop5555.sp12.ast;


//this is the super class of all AST types
public abstract class AST 
{
	public abstract Object visit(ASTVisitor v, Object arg) throws Exception;

//	/* toString method uses a PrintVisitor to print an AST */
//	@Override
//	public String toString() {
//		ToStringVisitor v = new ToStringVisitor();
//		try {
//			visit(v, "");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return v.getString();
//	}
}
