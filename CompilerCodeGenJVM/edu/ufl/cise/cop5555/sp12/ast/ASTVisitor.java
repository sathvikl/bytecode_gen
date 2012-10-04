package edu.ufl.cise.cop5555.sp12.ast;

public interface ASTVisitor {
	Object visitProgram(Program program, Object arg) throws Exception;
	Object visitBlock(Block block, Object arg) throws Exception;
	Object visitDeclaration(Declaration declaration, Object arg) throws Exception;
	Object visitSimpleType(SimpleType simpleType, Object arg) throws Exception;
	Object visitCompoundType(CompoundType compoundType, Object arg) throws Exception;
	Object visitAssignExprCommand(AssignExprCommand assignExprCommand,
			Object arg) throws Exception;
	Object visitAssignPairListCommand(
			AssignPairListCommand assignPairListCommand, Object arg) throws Exception;
	Object visitPrintCommand(PrintCommand printCommand, Object arg) throws Exception;
	Object visitPrintlnCommand(PrintlnCommand printlnCommand, Object arg) throws Exception;
	Object visitDoCommand(DoCommand doCommand, Object arg) throws Exception;
	Object visitDoEachCommand(DoEachCommand doEachCommand, Object arg) throws Exception;
	Object visitIfCommand(IfCommand ifCommand, Object arg) throws Exception;
	Object visitIfElseCommand(IfElseCommand ifElseCommand, Object arg) throws Exception;
	Object visitSimpleLValue(SimpleLValue simpleLValue, Object arg) throws Exception;
	Object visitExprLValue(ExprLValue exprLValue, Object arg) throws Exception;
	Object visitPair(Pair pair, Object arg) throws Exception;
	Object visitPairList(PairList pairList, Object arg) throws Exception;
	Object visitLValueExpression(LValueExpression lValueExpression, Object arg) throws Exception;
	Object visitIntegerLiteralExpression(
			IntegerLiteralExpression integerLiteralExpression, Object arg) throws Exception;
	Object visitBooleanLiteralExpression(
			BooleanLiteralExpression booleanLiteralExpression, Object arg) throws Exception;
	Object visitStringLiteralExpression(
			StringLiteralExpression stringLiteralExpression, Object arg) throws Exception;
	Object visitUnaryOpExpression(UnaryOpExpression unaryOpExpression,
			Object arg) throws Exception;
	Object visitBinaryOpExpression(BinaryOpExpression binaryOpExpression,
			Object arg) throws Exception;

}
