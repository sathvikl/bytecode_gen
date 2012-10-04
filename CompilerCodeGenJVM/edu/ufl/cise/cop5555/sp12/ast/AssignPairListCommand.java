package edu.ufl.cise.cop5555.sp12.ast;

public class AssignPairListCommand extends Command {

	public final LValue lValue;
	public final PairList pairList;
	
	public AssignPairListCommand(LValue lValue, PairList pairList) {
		this.lValue = lValue;
		this.pairList = pairList;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws Exception {
		return v.visitAssignPairListCommand(this,arg);
	}

}
