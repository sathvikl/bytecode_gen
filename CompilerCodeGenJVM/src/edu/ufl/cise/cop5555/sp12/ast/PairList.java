package edu.ufl.cise.cop5555.sp12.ast;

import java.util.List;

public class PairList extends AST {
	
	public final List<Pair> pairs;
	public Type type1;
	public Type type2;

	public PairList(List<Pair> pairs) {
		this.pairs = pairs;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitPairList(this,arg);
	}

}
