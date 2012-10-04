package edu.ufl.cise.cop5555.sp12.ast;

import edu.ufl.cise.cop5555.sp12.TokenStream.IllegalStringLiteralException;

/**
 * *Creates a String representing an AST.
 * 
 * Sample usage: AST tree = ... 
 *               ToStringVisitor pv = new ToStringVisitor();
 *               tree.visit(pv,""); 
 *               String s = pv.getString(); 
 *               System.out.println(s);
 * 
 */
public class ToStringVisitor implements ASTVisitor {
	private StringBuilder s = new StringBuilder();

	
	private String sh = "  "; // shift string

	public String getString() {
		return s.toString();
	}

	private void indent(String fi) {
		s.append(fi);
	}

	private void ln() {
		s.append("\n");
	}



//	@Override
//	public Object visitAssignStatement(AssignStatement assignStatement,
//			Object arg) throws Exception {
//		s.append("AssignStatement:");
//		ln();
//		indent(fi);
//		s.append("ident=" + assignStatement.ident.getText());
//		ln();
//		indent(fi);
//		s.append("e=");
//		assignStatement.e.visit(this, fi);
//		return null;
//	}
//
//
//

//
//	@Override
//	public Object visitCompositeExpression(
//			CompositeExpression compositeExpression, Object arg)
//			throws Exception {
//		String indent = (String) arg;
//		String fi = indent + sh;
//		s.append("CompositeExpr:");
//		ln();
//		indent(fi);
//		s.append("redExpr=");
//		compositeExpression.redExpr.visit(this, fi);
//		ln();
//		indent(fi);
//		s.append("grnExpr=");
//		compositeExpression.grnExpr.visit(this, fi);
//		ln();
//		indent(fi);
//		s.append("bluExpr=");
//		compositeExpression.bluExpr.visit(this, fi);
//		return null;
//	}
//
//	@Override
//	public Object visitConditionalExpression(
//			ConditionalExpression conditionalExpression, Object arg)
//			throws Exception {
//		String indent = (String) arg;
//		String fi = indent + sh;
//		s.append("ConditionalExpr:");
//		ln();
//		indent(fi);
//		s.append("e1=");
//		conditionalExpression.e1.visit(this, fi);
//		ln();
//		indent(fi);
//		s.append("e2=");
//		conditionalExpression.e2.visit(this, fi);
//		ln();
//		indent(fi);
//		s.append("e3=");
//		conditionalExpression.e3.visit(this, fi);
//		return null;
//	}
//
//	@Override
//	public Object visitShowStatement(ShowStatement showStatement,
//			Object arg) throws Exception {
//		String indent = (String) arg;
//		String fi = indent + sh;
//		s.append("ShowStatement:");
//		ln();
//		indent(fi);
//		s.append("frame=");
//		showStatement.frame.visit(this,fi);
//		ln();
//		indent(fi);
//		s.append("image=");
//		showStatement.image.visit(this,fi);
//		return null;
//	}
//
//	@Override
//	public Object visitHeightExpression(HeightExpression heightExpression,
//			Object arg) throws Exception {
//		String indent = (String) arg;
//		String fi = indent + sh;
//		s.append("ImageHeightExpr:");
//		ln();
//		indent(fi);
//		heightExpression.identExpression.visit(this,arg);
//		return null;
//	}
//
//	@Override
//	public Object visitIdentExpression(IdentExpression identExpression,
//			Object arg) throws Exception {
//		s.append("IdentExpression: " + identExpression.ident.getText());
//		return null;
//	}
//
//	@Override
//	public Object visitIdentWithColorExpression(
//			IdentWithColorExpression identWithColorExpression, Object arg)
//			throws Exception {
//		String indent = (String) arg;
//		String fi = indent + sh;
//		s.append("IdentWithColorExpression:");
//		ln();
//		indent(fi);
//		s.append("ident=");
//		s.append( ((IdentExpression)(identWithColorExpression.e)).ident.getText());
//		ln();
//		indent(fi);
//		s.append("color=");
//		s.append(identWithColorExpression.color);
//		return null;
//	}
//
//	@Override
//	public Object visitIdentWithLocationExpression(
//			IdentWithLocationExpression identWithLocationExpression, Object arg)
//			throws Exception {
//		String indent = (String) arg;
//		String fi = indent + sh;
//		s.append("IdentWithLocationExpression:");
//		ln();
//		indent(fi);
//		s.append("ident=");
//		s.append(((IdentExpression)(identWithLocationExpression.e)).ident.getText());
//		ln();
//	    identWithLocationExpression.loc.visit(this, fi);
//		return null;
//	}
//
//
//	@Override
//	public Object visitArgsExpression(ArgsExpression intArgExpression,
//			Object arg) throws Exception {
//		String indent = (String) arg;
//		String fi = indent + sh;
//		s.append("IntArgExpression:");
//		ln();
//		indent(fi);
//		s.append("index=" + intArgExpression.index);
//		return null;
//	}
//
//	@Override
//	public Object visitIntLitExpression(IntLitExpression intLitExpression,
//			Object arg) {
//		s.append("IntLitExpr:");
//		s.append(intLitExpression.val);
//		return null;
//	}
//
//	@Override
//	public Object visitLocationSelector(LocationSelector locationSelector,
//			Object arg) throws Exception {
//		String indent = (String) arg;
//		String fi = indent + sh;
//		indent(fi);
//		s.append("LocationSelector:");
//		ln();
//		fi = fi + sh;
//		indent(fi);
//		s.append("e1=");
//		locationSelector.e1.visit(this, fi);
//		ln();
//		indent(fi);
//		s.append("e2=");
//		locationSelector.e2.visit(this, fi);
//		return null;
//	}
//
//	@Override
//	public Object visitLoopStatement(LoopStatement loopStatement, Object arg)
//			throws Exception {
//		String indent = (String) arg;
//		String fi = indent + sh;
//		s.append("LoopStatement:");
//		ln();
//		indent(fi);
//		s.append("slist=");
//		String fis = fi+sh;
//		ln();
//		for (Statement stmt : loopStatement.slist)
//		{	indent(fis);
//		    stmt.visit(this, fis);
//		    ln();
//		}
//		indent(fi);
//		s.append("doneExpr=");
//		ln();
//		fis = fi+sh;
//		indent(fis);
//		loopStatement.e.visit(this, fis);
//
//		return null;
//	}
//
//	@Override
//	public Object visitPredefVarExpression(
//			PredefVarExpression predefVarExpression, Object arg) {
//		s.append("PreDefExpression:");
//		s.append(predefVarExpression.var.kind);
//		return null;
//	}
//
//	@Override
//	public Object visitProgram(Program program, Object arg) throws Exception {
//		String indent = (String) arg;
//		String fi = indent + sh;
//		s.append("Program:");
//		ln();
//		indent(fi);
//		s.append("ident=" + program.ident.getText());
//		ln();
//		indent(fi);
//		s.append("sdlist=\n");
//		fi = fi + sh;
//
//		if (program.sdlist.isEmpty()) {
//			indent(fi);
//			s.append("empty");
//		} else {
//			for (StatementOrDec sd : program.sdlist) {
//				indent(fi);
//				sd.visit(this, fi);
//				s.append("\n");
//			}
//		}
//		return null;
//	}
//
//	@Override
//	public Object visitIdentWithLocationAndColorExpression(
//			IdentWithLocationAndColorExpression identWithLocationAndColorExpression,
//			Object arg) throws Exception {
//		String indent = (String) arg;
//		String fi = indent + sh;
//		s.append("SampleExpression:");
//		ln();
//		indent(fi);
//		s.append("ident=");
//		s.append(((IdentExpression)(identWithLocationAndColorExpression.e)).ident.getText());
//		ln();
//		identWithLocationAndColorExpression.loc.visit(this, fi);
//		ln();
//		indent(fi);
//		s.append("color = ");
//		s.append(identWithLocationAndColorExpression.color);
//		return null;
//	}
//
//	@Override
//	public Object visitDec(Dec dec, Object arg) throws Exception {
//		s.append("Dec:" + dec.type.toString());
//		return null;
//	}
//
//	@Override
//	public Object visitSleepStatement(SleepStatement sleepStatement, Object arg)
//			throws Exception {
//		String indent = (String) arg;
//		String fi = indent + sh;
//		s.append("SleepStatement:");
//		ln();
//		indent(fi);
//		s.append("e=");
//		sleepStatement.e.visit(this, fi);
//		return null;
//	}
//
//	@Override
//	public Object visitUnaryExpression(UnaryExpression unaryExpression,
//			Object arg) throws Exception {
//		String indent = (String) arg;
//		String fi = indent + sh;
//		s.append("UnaryExpr:");
//		ln();
//		indent(fi);
//		s.append("e=");
//		unaryExpression.e1.visit(this, fi);
//		ln();
//		indent(fi);
//		s.append("op=" + unaryExpression.op);
//		return null;
//	}
//
//	@Override
//	public Object visitWidthExpression(WidthExpression widthExpression,
//			Object arg) throws Exception {
//		String indent = (String) arg;
//		String fi = indent + sh;
//		s.append("ImageWidthExpr:");
//		ln();
//		indent(fi);
//		widthExpression.identExpression.visit(this, arg);
//		return null;
//	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		String indent = (String) arg;
		String fi = indent + sh;
		s.append("Block:");
		ln();
		if (block.decOrCommands.isEmpty()) {
			indent(fi);
			s.append("empty");
		} else {
			for (DecOrCommand cd : block.decOrCommands) {
				indent(fi);
				cd.visit(this, fi);
				ln();
			}
		}
		return null;
	}

	@Override
	public Object visitDeclaration(Declaration declaration, Object arg) throws Exception {
		String indent = (String) arg;
		String fi = indent + sh;
		s.append("Declaration:");
		ln();
		indent(fi);
		declaration.type.visit(this, fi);
		ln();
		indent(fi);
		s.append("identifier=" + declaration.ident.getText());
		return null;
	}

	@Override
	public Object visitSimpleType(SimpleType simpleType, Object arg) {
		s.append("type=" + simpleType.type);
		return null;
	}

	@Override
	public Object visitCompoundType(CompoundType compoundType, Object arg) throws Exception {
		String indent = (String) arg;
		String fi = indent + sh;
		s.append("type=[");
		compoundType.keyType.visit(this,fi);
		s.append(",");
		compoundType.valType.visit(this,fi);
		s.append("]");
		return null;
	}

	@Override
	public Object visitAssignExprCommand(AssignExprCommand assignExprCommand,
			Object arg) throws Exception {
		String indent = (String) arg;
		String fi = indent + sh;
		s.append("AssignExprCommand:");
		ln();
		indent(fi);
		s.append("lValue=");
		assignExprCommand.lValue.visit(this,fi); 
		ln();
		indent(fi);
		s.append("expression=");
		assignExprCommand.expression.visit(this,fi);
		return null;
	}

	@Override
	public Object visitAssignPairListCommand(
			AssignPairListCommand assignPairListCommand, Object arg) throws Exception {
		String indent = (String) arg;
		String fi = indent + sh;
		s.append("AssignPairListCommand:");
		ln();
		indent(fi);
		s.append("lValue=");
		assignPairListCommand.lValue.visit(this,fi); 
		ln();
		indent(fi);
		s.append("pairList=");
		assignPairListCommand.pairList.visit(this,fi);
		return null;
	}

	@Override
	public Object visitPrintCommand(PrintCommand printCommand, Object arg) throws Exception {
		String indent = (String) arg;
		String fi = indent + sh;
		s.append("PrintCommand:");
		ln();
		indent(fi);
		s.append("expression=");
		printCommand.expression.visit(this,fi); 
		return null;
	}

	@Override
	public Object visitPrintlnCommand(PrintlnCommand printlnCommand, Object arg) throws Exception {
		String indent = (String) arg;
		String fi = indent + sh;
		s.append("PrintlnCommand:");
		ln();
		indent(fi);
		s.append("expression=");
		printlnCommand.expression.visit(this,fi); 
		return null;
	}

	@Override
	public Object visitDoCommand(DoCommand doCommand, Object arg) throws Exception {
		String indent = (String) arg;
		String fi = indent + sh;
		s.append("DoCommand:");
		ln();
		indent(fi);
		s.append("expression=");
		doCommand.expression.visit(this,fi); 
		ln();
		indent(fi);
		s.append("block=");
		doCommand.block.visit(this,fi); 	
		return null;
	}

	@Override
	public Object visitDoEachCommand(DoEachCommand doEachCommand, Object arg) throws Exception {
		String indent = (String) arg;
		String fi = indent + sh;
		s.append("DoEachCommand:");
		ln();
		indent(fi);
		s.append("lValue=");
		doEachCommand.lValue.visit(this,fi); 
		ln();
		indent(fi);
		s.append("key=");
		s.append(doEachCommand.key.getText());
		ln();
		indent(fi);
		s.append("val=");
		s.append(doEachCommand.val.getText());
		ln();
		indent(fi);
		s.append("block=");
		doEachCommand.block.visit(this,fi); 	
		return null;
	}

	@Override
	public Object visitIfCommand(IfCommand ifCommand, Object arg) throws Exception {
		String indent = (String) arg;
		String fi = indent + sh;
		s.append("IfCommand:");
		ln();
		indent(fi);
		s.append("expression=");
		ifCommand.expression.visit(this,fi); 
		ln();
		indent(fi);
		s.append("block=");
		ifCommand.block.visit(this,fi); 	
		return null;
	}

	@Override
	public Object visitIfElseCommand(IfElseCommand ifElseCommand, Object arg) throws Exception {
		String indent = (String) arg;
		String fi = indent + sh;
		s.append("IfElseCommand:");
		ln();
		indent(fi);
		s.append("expression=");
		ifElseCommand.expression.visit(this,fi); 
		ln();
		indent(fi);
		s.append("ifBlock=");
		ifElseCommand.ifBlock.visit(this,fi); 
		indent(fi);
		s.append("elseBlock=");
		ifElseCommand.elseBlock.visit(this,fi); 	
		return null;

	}

	@Override
	public Object visitSimpleLValue(SimpleLValue simpleLValue, Object arg) throws Exception {
		s.append("identifier=" + simpleLValue.identifier.getText());
		return null;
	}

	@Override
	public Object visitExprLValue(ExprLValue exprLValue, Object arg) throws Exception {
		String indent = (String) arg;
		String fi = indent + sh;
		s.append("ExprLValue:"); 
		ln();
		indent(fi);
		s.append("identifier=" + exprLValue.identifier.getText());
		ln();
		indent(fi);
		s.append("expression=");
		exprLValue.expression.visit(this,fi);
		return null;
	}

	@Override
	public Object visitPair(Pair pair, Object arg) throws Exception {
		String indent = (String) arg;
		String fi = indent + sh;
		s.append("Pair:");
		ln();
		indent(fi);
		s.append("expression0=");
		pair.expression0.visit(this,fi);
		ln();
		indent(fi);
		s.append("expression1=");
		pair.expression1.visit(this,fi);
		return null;
	}

	@Override
	public Object visitPairList(PairList pairList, Object arg) throws Exception {
		String indent = (String) arg;
		String fi = indent + sh;
		s.append("PairList:");
		ln();
		if (pairList.pairs.isEmpty()) {
		indent(fi);
		s.append("empty");
	} else {
		for (Pair pair : pairList.pairs) {
			indent(fi);
			pair.visit(this, fi);
			ln();
		}
	}		
		return null;
	}

	@Override
	public Object visitLValueExpression(LValueExpression lValueExpression,
			Object arg) throws Exception {
		String indent = (String) arg;
		String fi = indent + sh;
		s.append("LValueExpression:");
		lValueExpression.lValue.visit(this,fi);
		return null;
	}

	@Override
	public Object visitIntegerLiteralExpression(
			IntegerLiteralExpression integerLiteralExpression, Object arg) throws NumberFormatException, IllegalStringLiteralException {
		s.append("IntegerLiteralExpr:");
		s.append(integerLiteralExpression.integerLiteral.getIntVal());
		return null;
	}

	@Override
	public Object visitBooleanLiteralExpression(
			BooleanLiteralExpression booleanLiteralExpression, Object arg) throws IllegalStringLiteralException {
		s.append("BooleanLiteralExpr:");
		s.append(booleanLiteralExpression.booleanLiteral.getText());
		return null;
	}

	@Override
	public Object visitStringLiteralExpression(
			StringLiteralExpression stringLiteralExpression, Object arg) throws IllegalStringLiteralException {
		s.append("StringLiteralExpr:");
		s.append(stringLiteralExpression.stringLiteral.getText());
		return null;
	}

	@Override
	public Object visitUnaryOpExpression(UnaryOpExpression unaryOpExpression,
			Object arg) throws Exception {
		String indent = (String) arg;
		String fi = indent + sh;
		s.append("UnaryOpExpression:");
		ln();
		indent(fi);
		s.append("op=" + unaryOpExpression.op);
		ln();
		indent(fi);
		s.append("expression1=");
		unaryOpExpression.expression.visit(this, fi);
		return null;
	}

	@Override
	public Object visitBinaryOpExpression(
			BinaryOpExpression binaryOpExpression, Object arg) throws Exception {
		String indent = (String) arg;
		String fi = indent + sh;
		s.append("BinaryOpExpression:");
		ln();
		indent(fi);
		s.append("expression0=");
		binaryOpExpression.expression0.visit(this, fi);
		ln();
		indent(fi);
		s.append("op=" + binaryOpExpression.op);
		ln();
		indent(fi);
		s.append("expression1=");
		binaryOpExpression.expression1.visit(this, fi);
		return null;
	}

	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		String indent = (String) arg;
		String fi = indent + sh;
		s.append("Program:");
		ln();
		indent(fi);
		s.append("identfier=" + program.ident.getText());
		ln();
		indent(fi);
		s.append("block=");
		program.block.visit(this,fi);
		return null;
	}

	public static String printAST(AST result) throws Exception {
		ToStringVisitor toStringVisitor = new ToStringVisitor();
		result.visit(toStringVisitor, " ");
		String ASTstring = toStringVisitor.getString();
		System.out.println(ASTstring);
		return ASTstring;
	}
	
    public static String getStringFromAST(AST result) throws Exception {
		ToStringVisitor toStringVisitor = new ToStringVisitor();
		result.visit(toStringVisitor, " ");
		return toStringVisitor.getString();
	}

}
