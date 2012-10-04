package edu.ufl.cise.cop5555.sp12.context;

import edu.ufl.cise.cop5555.sp12.Kind;
import edu.ufl.cise.cop5555.sp12.ast.ASTVisitor;
import edu.ufl.cise.cop5555.sp12.ast.AssignExprCommand;
import edu.ufl.cise.cop5555.sp12.ast.AssignPairListCommand;
import edu.ufl.cise.cop5555.sp12.ast.BinaryOpExpression;
import edu.ufl.cise.cop5555.sp12.ast.Block;
import edu.ufl.cise.cop5555.sp12.ast.BooleanLiteralExpression;
import edu.ufl.cise.cop5555.sp12.ast.CompoundType;
import edu.ufl.cise.cop5555.sp12.ast.DecOrCommand;
import edu.ufl.cise.cop5555.sp12.ast.Declaration;
import edu.ufl.cise.cop5555.sp12.ast.DoCommand;
import edu.ufl.cise.cop5555.sp12.ast.DoEachCommand;
import edu.ufl.cise.cop5555.sp12.ast.ExprLValue;
import edu.ufl.cise.cop5555.sp12.ast.IfCommand;
import edu.ufl.cise.cop5555.sp12.ast.IfElseCommand;
import edu.ufl.cise.cop5555.sp12.ast.IntegerLiteralExpression;
import edu.ufl.cise.cop5555.sp12.ast.LValueExpression;
import edu.ufl.cise.cop5555.sp12.ast.Pair;
import edu.ufl.cise.cop5555.sp12.ast.PairList;
import edu.ufl.cise.cop5555.sp12.ast.PrintCommand;
import edu.ufl.cise.cop5555.sp12.ast.PrintlnCommand;
import edu.ufl.cise.cop5555.sp12.ast.Program;
import edu.ufl.cise.cop5555.sp12.ast.SimpleLValue;
import edu.ufl.cise.cop5555.sp12.ast.SimpleType;
import edu.ufl.cise.cop5555.sp12.ast.StringLiteralExpression;
import edu.ufl.cise.cop5555.sp12.ast.ToStringVisitor;
import edu.ufl.cise.cop5555.sp12.ast.Type;
import edu.ufl.cise.cop5555.sp12.ast.UnaryOpExpression;

public class ContextCheckVisitor implements ASTVisitor {
	String prog_name_ident;
	SymbolTable symbol_table;
	
	public ContextCheckVisitor() {
		symbol_table = new SymbolTable();
		prog_name_ident = null;
	}
	
	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		// TODO Auto-generated method stub
		prog_name_ident = program.ident.getText();
		symbol_table.enterScope();
		symbol_table.insert(prog_name_ident, new Declaration(new SimpleType(Kind.PROG), program.ident));
		program.block.visit(this, arg);
		symbol_table.exitScope();
		return null;
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		// TODO Auto-generated method stub
		symbol_table.enterScope();
		if (!block.decOrCommands.isEmpty()) {
			for (DecOrCommand cd : block.decOrCommands) {				
				cd.visit(this, arg);
			}
		}
		symbol_table.exitScope();
		return null;
	}

	@Override
	public Object visitDeclaration(Declaration declaration, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		if(declaration.ident.getText().equals(prog_name_ident)) {
			String error_msg = new String("Declaration " + declaration.ident.getText() + " same as progam name\n");
			throw new ContextException(declaration, error_msg);	
		}
		else if(!symbol_table.insert(declaration.ident.getText(), declaration)) {
			String error_msg = new String("Declaration " + declaration.ident.getText() + " redefined in the current scope\n");
			throw new ContextException(declaration, error_msg);
		} 
		
		return null;
	}

	@Override
	public Object visitSimpleType(SimpleType simpleType, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitCompoundType(CompoundType compoundType, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitAssignExprCommand(AssignExprCommand assignExprCommand,
			Object arg) throws Exception {
		// TODO Auto-generated method stub	
		Type lhsType = (Type) assignExprCommand.lValue.visit(this,arg);
		Type exprType = (Type) assignExprCommand.expression.visit(this,arg);
		
//		if(lhsType instanceof SimpleType || exprType instanceof SimpleType) {	
//			if(lhsType instanceof CompoundType) {
//				lhsType = ((CompoundType)(lhsType)).valType;
//			}
//			if(exprType instanceof CompoundType) {
//				exprType = ((CompoundType)(exprType)).valType;
//			}
//		}
		
		if(!lhsType.equalsTo(exprType)) {
			String error_msg = new String(ToStringVisitor.printAST(assignExprCommand) + "\n incompatible types in assignment\n");
			throw new ContextException(assignExprCommand, error_msg);
		}
		//check(, assignExprCommand, );
		return null;
	}

	@Override
	public Object visitPrintCommand(PrintCommand printCommand, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		printCommand.expression.visit(this, arg);
		return null;
	}

	@Override
	public Object visitPrintlnCommand(PrintlnCommand printlnCommand, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		printlnCommand.expression.visit(this, arg);
		return null;
	}

	@Override
	public Object visitDoCommand(DoCommand doCommand, Object arg)
			throws Exception {
		Type condType = (Type)doCommand.expression.visit(this, arg);
		
		if(!condType.equalsTo(new SimpleType(Kind.BOOLEAN))) {
			String error_msg = new String(ToStringVisitor.printAST(doCommand.expression) + " does not evaluate to boolean\n");
			throw new ContextException(doCommand, error_msg);	
		}
		doCommand.block.visit(this,arg); 	
		return null;
	}

	@Override
	public Object visitDoEachCommand(DoEachCommand doEachCommand, Object arg)
			throws Exception {
		Declaration DecKey = symbol_table.lookup(doEachCommand.key.getText());
		if(DecKey == null) {
			String error_msg = new String("Identifier " + doEachCommand.key.getText() + "in DoEachCommand isn't declared\n");
			throw new ContextException(doEachCommand, error_msg);
		}
		Declaration DecVal = symbol_table.lookup(doEachCommand.val.getText());
		if(DecVal == null) {
			String error_msg = new String("Identifier " + doEachCommand.val.getText() + "in DoEachCommand isn't declared\n");
			throw new ContextException(doEachCommand, error_msg);
		}
		
		Type lhstype = (Type)doEachCommand.lValue.visit(this, arg);
		if(lhstype instanceof CompoundType) {
			if( !((CompoundType)lhstype).keyType.equalsTo(DecKey.type) || !((CompoundType)lhstype).valType.equalsTo(DecVal.type)) {
				String error_msg = new String(ToStringVisitor.printAST(doEachCommand) + "DoEachCommand type incompatiable\n");
				throw new ContextException(doEachCommand, error_msg);
			}
		} else {
			String error_msg = new String("In DoEachCommand: LValue " + ToStringVisitor.printAST(doEachCommand.lValue) + " isn't declared as a compound type\n");
			throw new ContextException(doEachCommand, error_msg);
		}
		
		doEachCommand.block.visit(this, arg);
		return null;
	}

	@Override
	public Object visitIfCommand(IfCommand ifCommand, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		Type condType = (Type)ifCommand.expression.visit(this, arg);
		
		if(!condType.equalsTo(new SimpleType(Kind.BOOLEAN))) {
			String error_msg = new String(ToStringVisitor.printAST(ifCommand.expression) + " does not evaluate to boolean\n");
			throw new ContextException(ifCommand, error_msg);	
		}
		ifCommand.block.visit(this,arg);
		return null;
	}

	@Override
	public Object visitIfElseCommand(IfElseCommand ifElseCommand, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		Type condType = (Type)ifElseCommand.expression.visit(this, arg);
		
		if(!condType.equalsTo(new SimpleType(Kind.BOOLEAN))) {
			String error_msg = new String(ToStringVisitor.printAST(ifElseCommand.expression) + " does not evaluate to boolean\n");
			throw new ContextException(ifElseCommand, error_msg);	
		}
		ifElseCommand.ifBlock.visit(this,arg); 
		ifElseCommand.elseBlock.visit(this,arg);
		return null;
	}

	@Override
	public Object visitSimpleLValue(SimpleLValue simpleLValue, Object arg)
			throws Exception {
		Declaration Dec = symbol_table.lookup(simpleLValue.identifier.getText());
		if(Dec == null) {
			String error_msg = new String("Identifier " + simpleLValue.identifier.getText() + " isn't declared\n");
			throw new ContextException(simpleLValue, error_msg);
		} else {
			simpleLValue.type = Dec.type;		
			return simpleLValue.type;
		}
	}

	@Override
	public Object visitExprLValue(ExprLValue exprLValue, Object arg)
			throws Exception {
		Declaration Dec = symbol_table.lookup(exprLValue.identifier.getText());
		if(Dec == null) {
			String error_msg = new String("Identifier " + exprLValue.identifier.getText() + " isn't declared\n");
			throw new ContextException(exprLValue, error_msg);
		}
		
		Type exprType = (Type)exprLValue.expression.visit(this, arg);
		if(Dec.type instanceof CompoundType) {
			if(((CompoundType)Dec.type).keyType.equalsTo(exprType)) {
				exprLValue.type = ((CompoundType)Dec.type).valType;
				//exprLValue.type = Dec.type;
				return exprLValue.type;
			} else {
				String error_msg = new String("LValueExpression " + ToStringVisitor.printAST(exprLValue) + " keyType and expression type incompatiable\n");
				throw new ContextException(exprLValue, error_msg);
			}
		} else {
			String error_msg = new String("Identifier " + exprLValue.identifier.getText() + " isn't a compound type\n");
			throw new ContextException(exprLValue, error_msg);
		}
	}

	@Override
	public Object visitPair(Pair pair, Object arg) throws Exception {
		Type exprType1, exprType2;
		exprType1 = (Type)pair.expression0.visit(this, arg);
		exprType2 = (Type)pair.expression1.visit(this, arg);
		
//		if(!(exprType1 instanceof SimpleType)) {
//			return null;
//		}
		pair.type1 = (Type)exprType1;
		pair.type2 = (Type)exprType2;
		return null;
	}

	@Override
	public Object visitPairList(PairList pairList, Object arg) throws Exception {
		Type curPairType1, curPairType2, prevPairType1, prevPairType2;
		
		curPairType1 = curPairType2 = prevPairType1 = prevPairType2 = null;
		if (!pairList.pairs.isEmpty()) {
			for (Pair pair : pairList.pairs) {
				pair.visit(this, arg);   
				curPairType1 = pair.type1;
				curPairType2 = pair.type2;
				
				if(curPairType1 == null || curPairType2 == null) {
					String error_msg = new String(ToStringVisitor.printAST(pairList) + " pair types incompatiable\n");
					throw new ContextException(pairList, error_msg);
				} else {
					if((prevPairType1 != null && prevPairType2 != null) 
							&& ( !prevPairType1.equalsTo(curPairType1) || !prevPairType2.equalsTo(curPairType2) ) ) {
						String error_msg = new String(ToStringVisitor.printAST(pairList) + " pair types incompatiable\n");
						throw new ContextException(pairList, error_msg);
					}
					prevPairType1 = curPairType1;
					prevPairType2 = curPairType2;
				}
			}
			// After looking through all the pairs, assign the types for the pairlist 
			pairList.type1 = curPairType1;
			pairList.type2 = curPairType2;
		} else {
			pairList.type1 = null;
			pairList.type2 = null;
		}
		return null;
	}

	@Override
	public Object visitAssignPairListCommand(
			AssignPairListCommand assignPairListCommand, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		Type lhsType = (Type)assignPairListCommand.lValue.visit(this, arg);
		assignPairListCommand.pairList.visit(this, arg);
		
		if (!assignPairListCommand.pairList.pairs.isEmpty()) {
			if(!(lhsType instanceof CompoundType)) {
				String error_msg = new String(ToStringVisitor.printAST(assignPairListCommand) + "lhs and rhs types incompatiable; either of them is not a compound type\n");
				throw new ContextException(assignPairListCommand, error_msg);
			} else if (!((CompoundType)lhsType).keyType.equalsTo(assignPairListCommand.pairList.type1) || 
					   !((CompoundType)lhsType).valType.equalsTo(assignPairListCommand.pairList.type2)  ) {
				String error_msg = new String(ToStringVisitor.printAST(assignPairListCommand) + "lhs and rhs compound types incompatiable\n");
				throw new ContextException(assignPairListCommand, error_msg);
			}
		}
		return null;
	}
	
	@Override
	public Object visitLValueExpression(LValueExpression lValueExpression,
			Object arg) throws Exception {
		// TODO Auto-generated method stub
		Type LValType = (Type)lValueExpression.lValue.visit(this, arg);
		lValueExpression.type = LValType; 
		return lValueExpression.type;
	}

	@Override
	public Object visitIntegerLiteralExpression(
			IntegerLiteralExpression integerLiteralExpression, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		if(integerLiteralExpression.integerLiteral.kind == Kind.INTEGER_LITERAL) {
			integerLiteralExpression.type = new SimpleType(Kind.INT);
			return integerLiteralExpression.type;
		} else {
			String error_msg = new String("IntegerLiteral " + integerLiteralExpression.integerLiteral.getText() + " isn't of type INT\n");
			throw new ContextException(integerLiteralExpression, error_msg);			
		}	
	}

	@Override
	public Object visitBooleanLiteralExpression(
			BooleanLiteralExpression booleanLiteralExpression, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		if(booleanLiteralExpression.booleanLiteral.kind == Kind.BOOLEAN_LITERAL) {
			booleanLiteralExpression.type = new SimpleType(Kind.BOOLEAN);
			return booleanLiteralExpression.type;
		} else {
			String error_msg = new String("BooleanLiteral " + booleanLiteralExpression.booleanLiteral.getText() + " isn't of type BOOL\n");
			throw new ContextException(booleanLiteralExpression, error_msg);			
		}		
	}

	@Override
	public Object visitStringLiteralExpression(
			StringLiteralExpression stringLiteralExpression, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		
		if(stringLiteralExpression.stringLiteral.kind == Kind.STRING_LITERAL) {
			stringLiteralExpression.type = new SimpleType(Kind.STRING);
			return stringLiteralExpression.type;
		} else {
			String error_msg = new String("StringLiteral " + stringLiteralExpression.stringLiteral.getText() + " isn't of type STRING\n");
			throw new ContextException(stringLiteralExpression, error_msg);			
		}
	}

	@Override
	public Object visitUnaryOpExpression(UnaryOpExpression unaryOpExpression,
			Object arg) throws Exception {
		if(unaryOpExpression.op != Kind.NOT && unaryOpExpression.op != Kind.MINUS) {
			String error_msg = new String("UnaryOpExpression " + ToStringVisitor.printAST(unaryOpExpression) + " is illegal\n");
			throw new ContextException(unaryOpExpression, error_msg);						
		}
		Type ExprType = (Type)unaryOpExpression.expression.visit(this, arg);
		if(unaryOpExpression.op == Kind.NOT && !ExprType.equalsTo(new SimpleType(Kind.BOOLEAN))) {
			String error_msg = new String("UnaryOpExpression " + ToStringVisitor.printAST(unaryOpExpression) + " is not boolean\n");
			throw new ContextException(unaryOpExpression, error_msg);				
		} 
		else if(unaryOpExpression.op == Kind.MINUS && !ExprType.equalsTo(new SimpleType(Kind.INT))) {
			String error_msg = new String("UnaryOpExpression " + ToStringVisitor.printAST(unaryOpExpression) + " is not int\n");
			throw new ContextException(unaryOpExpression, error_msg);				
		} else {
			unaryOpExpression.type = ExprType;
			return ExprType;
		}
	}

	@Override
	public Object visitBinaryOpExpression(
			BinaryOpExpression binaryOpExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Type Expr0 = (Type)binaryOpExpression.expression0.visit(this, arg);
		Type Expr1 = (Type)binaryOpExpression.expression1.visit(this, arg);
		
		//SimpleType sExpr0 = (SimpleType) Expr0;
		//SimpleType sExpr1 = (SimpleType) Expr1;
		
		binaryOpExpression.type = null;
		switch(binaryOpExpression.op) {
		case PLUS :
			if(!Expr0.equalsTo(Expr1) && ( Expr0.equalsTo(new SimpleType(Kind.STRING)) || Expr1.equalsTo(new SimpleType(Kind.STRING)) )) {
				if(Expr0.equalsTo(new SimpleType(Kind.STRING))) {
					if(!Expr1.equalsTo(new SimpleType(Kind.INT)) && !Expr1.equalsTo(new SimpleType(Kind.BOOLEAN))) {
						String error_msg = new String(ToStringVisitor.printAST(binaryOpExpression) + " has incompatiable types;\n");
						throw new ContextException(binaryOpExpression, error_msg);
					}
				} 
				else if(Expr1.equalsTo(new SimpleType(Kind.STRING))) {
					if(!Expr0.equalsTo(new SimpleType(Kind.INT)) && !Expr0.equalsTo(new SimpleType(Kind.BOOLEAN))) {
						String error_msg = new String(ToStringVisitor.printAST(binaryOpExpression) + " has incompatiable types;\n");
						throw new ContextException(binaryOpExpression, error_msg);
					}
				} 
				binaryOpExpression.type = new SimpleType(Kind.STRING); 
			}
			// Types are same and both are NOT BOOLEAN
			else if(Expr0.equalsTo(Expr1) && !Expr0.equalsTo(new SimpleType(Kind.BOOLEAN)) && !Expr1.equalsTo(new SimpleType(Kind.BOOLEAN))) {
				binaryOpExpression.type = Expr0;
			}  
			else {
				String error_msg = new String(ToStringVisitor.printAST(binaryOpExpression) + " has incompatiable types;\n");
				throw new ContextException(binaryOpExpression, error_msg);
			}
			
			break;
		case MINUS: case TIMES:
			if(Expr0.equalsTo(Expr1) && (Expr0.equalsTo(new SimpleType(Kind.INT)) || Expr0 instanceof CompoundType) ) {
				binaryOpExpression.type = Expr0;
			} else {
				String error_msg = new String(ToStringVisitor.printAST(binaryOpExpression) + " has incompatiable types; NOT INT or MAP\n");
				throw new ContextException(binaryOpExpression, error_msg);
			}			
			break;	
		case AND: case OR:
			if(Expr0.equalsTo(Expr1) && Expr0.equalsTo(new SimpleType(Kind.BOOLEAN))) {
				binaryOpExpression.type = new SimpleType(Kind.BOOLEAN);
			} else {
				String error_msg = new String(ToStringVisitor.printAST(binaryOpExpression) + " has incompatiable types; NOT BOOL\n");
				throw new ContextException(binaryOpExpression, error_msg);
			}
			break;
		case DIVIDE:
			if(Expr0.equalsTo(Expr1) && Expr0.equalsTo(new SimpleType(Kind.INT))) {
				binaryOpExpression.type = new SimpleType(Kind.INT);
			} else {
				String error_msg = new String(ToStringVisitor.printAST(binaryOpExpression) + " has incompatiable types; NOT INT\n");
				throw new ContextException(binaryOpExpression, error_msg);
			}
			break;
		case EQUALS: case NOT_EQUALS: case LESS_THAN: case GREATER_THAN: case AT_MOST: case AT_LEAST:
			if(Expr0.equalsTo(Expr1)) {
				binaryOpExpression.type = new SimpleType(Kind.BOOLEAN);
			} else {
				String error_msg = new String(ToStringVisitor.printAST(binaryOpExpression) + " has incompatiable types\n");
				throw new ContextException(binaryOpExpression, error_msg);
			}
			break;
		}
		return binaryOpExpression.type;
	}


}
