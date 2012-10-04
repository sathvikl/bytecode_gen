package edu.ufl.cise.cop5555.sp12.codegen;

import org.junit.runner.Computer;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import sun.java2d.pipe.SpanShapeRenderer.Simple;

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
import edu.ufl.cise.cop5555.sp12.ast.Type;
import edu.ufl.cise.cop5555.sp12.ast.UnaryOpExpression;
import edu.ufl.cise.cop5555.sp12.context.SymbolTable;
import edu.ufl.cise.cop5555.sp12.Runtime;


public class CodeGenVisitor implements ASTVisitor, Opcodes {
	
	String className;
	SymbolTable symbol_table = new SymbolTable();
	ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
	FieldVisitor fv;
	MethodVisitor mv;
	AnnotationVisitor av0;

	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		className = program.ident.getText();
		cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, className, null, "java/lang/Object", null);
		mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
		mv.visitCode();
		//set label on first instruction of main method
		Label lstart = new Label();
		mv.visitLabel(lstart);
		symbol_table.enterScope();
		symbol_table.insert(program.ident.getText(), new Declaration(new SimpleType(Kind.PROG), program.ident));
		//visit block to generate code and field declarations
		program.block.visit(this,null);
		symbol_table.exitScope();
		//add return instruction
		mv.visitInsn(RETURN);
		Label lend= new Label();
		mv.visitLabel(lend);
		//visit local variable--the only one in our project is the String[] argument to the main method
		mv.visitLocalVariable("args", "[Ljava/lang/String;", null, lstart, lend, 0);
		mv.visitMaxs(2, 1);
		mv.visitEnd();
		cw.visitEnd();
		//convert class file to byte array and return
		return cw.toByteArray();
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		//visit children
		symbol_table.enterScope();
		for (DecOrCommand cd : block.decOrCommands) {
			cd.visit(this, null);
		}
		symbol_table.exitScope();
		return null;
	}

	@Override
	public Object visitDeclaration(Declaration declaration, Object arg)
			throws Exception {
		symbol_table.insert(declaration.ident.getText(), declaration);
		
		// Prefix the scope number to the variable name to handle name mangling .. 
		String symbol_name = declaration.scope_number + declaration.ident.getText();
		
		if(declaration.type instanceof SimpleType) {
			switch(((SimpleType)declaration.type).type) {
				case INT:
					fv = cw.visitField(ACC_STATIC, symbol_name, "I", null, new Integer(0));
					fv.visitEnd();
					break;
				case BOOLEAN:
					fv = cw.visitField(ACC_STATIC, symbol_name, "Z", null, null);
					fv.visitEnd();
					break;
				case STRING:
					fv = cw.visitField(ACC_STATIC, symbol_name, "Ljava/lang/String;", null, new String(""));
					fv.visitEnd();
					break;
			}
		} else if(declaration.type instanceof CompoundType) {
			String signature;
			signature = getSignature((CompoundType) declaration.type);
			fv = cw.visitField(ACC_STATIC, symbol_name, "Ljava/util/HashMap;", signature, null);
			fv.visitEnd();
			mv.visitTypeInsn(NEW, "java/util/HashMap");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V");
			mv.visitFieldInsn(PUTSTATIC, className, symbol_name, "Ljava/util/HashMap;");
		}
		return null;
	}

	String getSignature(Type ctype) {
		
		if(ctype instanceof SimpleType) {
			switch( ((SimpleType)ctype).type) {
			case STRING:
				return "Ljava/lang/String;";
			case BOOLEAN:
				return "Ljava/lang/Boolean;";
			case INT:
				return "Ljava/lang/Integer;";
			}
		} 
		else if(ctype instanceof CompoundType) {
			return "Ljava/util/HashMap<" + getSignature(((CompoundType)ctype).keyType) +  
							 getSignature(((CompoundType)ctype).keyType) + 
					         getSignature(((CompoundType)ctype).valType) + ">;";
			
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
		//compoundType.keyType.visit(this, arg);
		//compoundType.valType.visit(this, arg);
		return null;
	}

	private void box(Type type) {
		if(type instanceof SimpleType) {
			switch( ((SimpleType)type).type ) {
			//case STRING:
				//mv.visitMethodInsn(INVOKESTATIC, "java/lang/String", "valueOf", "(Ljava/lang/String;)Ljava/lang/String;");
				//break;
			case BOOLEAN:
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
				break;
			case INT:
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
				break;
			}
		}
	}
	
	@Override
	public Object visitAssignExprCommand(AssignExprCommand assignExprCommand,
			Object arg) throws Exception {
		// TODO Auto-generated method stub
		
		if(assignExprCommand.lValue instanceof SimpleLValue) {
			//visit the assign expression 
			//visit the lvalue to set the value
			//assignExprCommand.lValue.visit(this,arg);
			assignExprCommand.expression.visit(this,arg);
			
			String lval_symbol_name = ((SimpleLValue)(assignExprCommand.lValue)).identifier.getText();
			lval_symbol_name = (symbol_table.lookup(lval_symbol_name)).scope_number + lval_symbol_name;
			
			if( ((SimpleLValue)(assignExprCommand.lValue)).type instanceof SimpleType) {
				switch( ((SimpleType)((SimpleLValue)(assignExprCommand.lValue)).type).type ) {
				case INT:	
					mv.visitFieldInsn(PUTSTATIC, className, lval_symbol_name, "I");
					break;
				case BOOLEAN:
					mv.visitFieldInsn(PUTSTATIC, className, lval_symbol_name, "Z");
					break;
				case STRING:
					mv.visitFieldInsn(PUTSTATIC, className, lval_symbol_name, "Ljava/lang/String;");
					break;	
				}
			} else if(((SimpleLValue)(assignExprCommand.lValue)).type instanceof CompoundType) {
				mv.visitFieldInsn(PUTSTATIC, className, lval_symbol_name, "Ljava/util/HashMap;");
			}
		} 
		else if(assignExprCommand.lValue instanceof ExprLValue) {
			//visit the lvalue
			//visit the lval expression
			//visit the Assign expression
			String lval_symbol_name = ((ExprLValue)(assignExprCommand.lValue)).identifier.getText();
			lval_symbol_name = (symbol_table.lookup(lval_symbol_name)).scope_number + lval_symbol_name;
			
			mv.visitFieldInsn(GETSTATIC, className, lval_symbol_name, "Ljava/util/HashMap;");
			((ExprLValue)(assignExprCommand.lValue)).expression.visit(this, arg);
			box(((ExprLValue)(assignExprCommand.lValue)).expression.type);
			assignExprCommand.expression.visit(this, arg);
			box(assignExprCommand.expression.type);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
			mv.visitInsn(POP);
		}
		return null;
	}


	@Override
	public Object visitPrintCommand(PrintCommand printCommand, Object arg)
			throws Exception {
		//TODO Fix this to work with other types
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		printCommand.expression.visit(this,arg);
		
		if(printCommand.expression.type instanceof SimpleType) {
			switch( ((SimpleType)printCommand.expression.type).type ) {
			case STRING:
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/String;)V");
				break;
			case BOOLEAN:
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Z)V");
				break;
			case INT:
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(I)V");
				break;
			}			
		}
		else if(printCommand.expression.type instanceof CompoundType) {
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(Ljava/lang/Object;)V");
		}
		return null;
	}

	@Override
	public Object visitPrintlnCommand(PrintlnCommand printlnCommand, Object arg)
			throws Exception {
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		printlnCommand.expression.visit(this,arg);
		
		if(printlnCommand.expression.type instanceof SimpleType) {
			switch( ((SimpleType)printlnCommand.expression.type).type ) {
			case STRING:
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
				break;
			case BOOLEAN:
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Z)V");
				break;
			case INT:
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V");
				break;
			}			
		}
		else if(printlnCommand.expression.type instanceof CompoundType) {
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V");
		}
		return null;
	}

	@Override
	public Object visitDoCommand(DoCommand doCommand, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		Label endOfDoLabel = new Label();
		mv.visitJumpInsn(GOTO, endOfDoLabel);
		
		Label DoBeginLabel = new Label();
		mv.visitLabel(DoBeginLabel);
		
		doCommand.block.visit(this,arg);
		mv.visitLabel(endOfDoLabel);
		doCommand.expression.visit(this, arg);
		mv.visitJumpInsn(IFNE, DoBeginLabel);
		return null;
	}

	void unBox(Type type) {
		if(type instanceof SimpleType) {
			switch( ((SimpleType)type).type) {
			case INT:
				mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I");				
				break;
			case STRING:
				mv.visitTypeInsn(CHECKCAST, "java/lang/String");
				break;
			case BOOLEAN:
				mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z");
				break;
			}
		} else if(type instanceof CompoundType) {
			Label ubl = new Label();
			mv.visitInsn(DUP);
			mv.visitJumpInsn(IFNONNULL, ubl);
			mv.visitInsn(POP);
			//Instantiate a new HashMap
			mv.visitTypeInsn(NEW, "java/util/HashMap");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V");
			mv.visitLabel(ubl);
			mv.visitTypeInsn(CHECKCAST, "java/util/HashMap");
		}
	}
	
	@Override
	public Object visitDoEachCommand(DoEachCommand doEachCommand, Object arg)
			throws Exception {
		doEachCommand.lValue.visit(this, arg);
		
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "entrySet", "()Ljava/util/Set;");
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "iterator", "()Ljava/util/Iterator;");
		Label endOfDoLoop = new Label();
		mv.visitJumpInsn(GOTO, endOfDoLoop);
		
		Label startDoLoop = new Label();
		mv.visitLabel(startDoLoop);
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;");
		mv.visitTypeInsn(CHECKCAST, "java/util/Map$Entry");
		
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map$Entry", "getKey", "()Ljava/lang/Object;");
		//mv.visitInsn(POP);
		
		unBox( ((CompoundType)((SimpleLValue)doEachCommand.lValue).type).keyType );
		String key_symbol_name = doEachCommand.key.getText();
		key_symbol_name = (symbol_table.lookup(key_symbol_name)).scope_number + key_symbol_name;
		switch( (((CompoundType)((SimpleLValue)doEachCommand.lValue).type).keyType).type ) {
		case INT:	
			mv.visitFieldInsn(PUTSTATIC, className, key_symbol_name, "I");
			break;
		case BOOLEAN:
			mv.visitFieldInsn(PUTSTATIC, className, key_symbol_name, "Z");
			break;
		case STRING:
			mv.visitFieldInsn(PUTSTATIC, className, key_symbol_name, "Ljava/lang/String;");
			break;	
		}
		
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map$Entry", "getValue", "()Ljava/lang/Object;");
		//mv.visitInsn(POP);
		
		unBox( ((CompoundType)((SimpleLValue)doEachCommand.lValue).type).valType);
		String val_symbol_name = doEachCommand.val.getText();
		val_symbol_name = (symbol_table.lookup(val_symbol_name)).scope_number + val_symbol_name;
		if(((CompoundType)((SimpleLValue)doEachCommand.lValue).type).valType instanceof SimpleType) {
			switch( ((SimpleType)((CompoundType)((SimpleLValue)doEachCommand.lValue).type).valType).type ) {
			case INT:	
				mv.visitFieldInsn(PUTSTATIC, className, val_symbol_name, "I");
				break;
			case BOOLEAN:
				mv.visitFieldInsn(PUTSTATIC, className, val_symbol_name, "Z");
				break;
			case STRING:
				mv.visitFieldInsn(PUTSTATIC, className, val_symbol_name, "Ljava/lang/String;");
				break;	
			}
		} else {
			mv.visitFieldInsn(PUTSTATIC, className, val_symbol_name, "Ljava/util/HashMap;");
		}
		
		doEachCommand.block.visit(this, arg);
		mv.visitLabel(endOfDoLoop);
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z");
		mv.visitJumpInsn(IFNE, startDoLoop);
		mv.visitInsn(POP);
		return null;
	}

	@Override
	public Object visitIfCommand(IfCommand ifCommand, Object arg)
			throws Exception {
		ifCommand.expression.visit(this, arg);
		Label Onfalse = new Label();
		mv.visitJumpInsn(IFEQ, Onfalse);
		ifCommand.block.visit(this,arg);
		mv.visitLabel(Onfalse);
		return null;
	}

	@Override
	public Object visitIfElseCommand(IfElseCommand ifElseCommand, Object arg)
			throws Exception {
		
		ifElseCommand.expression.visit(this, arg);
		Label Onfalse = new Label();
		mv.visitJumpInsn(IFEQ, Onfalse);
		ifElseCommand.ifBlock.visit(this,arg);
		Label endOfIfLabel = new Label();
		mv.visitJumpInsn(GOTO, endOfIfLabel);
		
		mv.visitLabel(Onfalse);
		ifElseCommand.elseBlock.visit(this,arg);
		mv.visitLabel(endOfIfLabel);
		return null;
	}

	@Override
	public Object visitSimpleLValue(SimpleLValue simpleLValue, Object arg)
			throws Exception {
		String symbol_name = simpleLValue.identifier.getText();
		symbol_name = (symbol_table.lookup(symbol_name)).scope_number + symbol_name;
		
		if(simpleLValue.type instanceof SimpleType) {
			switch(((SimpleType)(simpleLValue.type)).type) {
			case INT:	
				mv.visitFieldInsn(GETSTATIC, className, symbol_name, "I");
				break;
			case BOOLEAN:
				mv.visitFieldInsn(GETSTATIC, className, symbol_name, "Z");
				break;
			case STRING:
				mv.visitFieldInsn(GETSTATIC, className, symbol_name, "Ljava/lang/String;");
				break;	
			}		
		} 
		else if(simpleLValue.type instanceof CompoundType) {
			mv.visitFieldInsn(GETSTATIC, className, symbol_name, "Ljava/util/HashMap;");
		}
		return null;
	}

	@Override
	public Object visitExprLValue(ExprLValue exprLValue, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		
		String symbol_name = exprLValue.identifier.getText();
		symbol_name = (symbol_table.lookup(symbol_name)).scope_number + symbol_name;
		
		mv.visitFieldInsn(GETSTATIC, className, symbol_name, "Ljava/util/HashMap;");
		exprLValue.expression.visit(this, arg);
		box(exprLValue.expression.type);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
		unBox(exprLValue.type);
		
		return null;
	}

	@Override
	public Object visitPair(Pair pair, Object arg) throws Exception {
		// TODO Auto-generated method stub
		pair.expression0.visit(this, arg);
		box(pair.expression0.type);
		pair.expression1.visit(this, arg);
		box(pair.expression1.type);
		return null;
	}

	@Override
	public Object visitPairList(PairList pairList, Object arg) throws Exception {
		// TODO Auto-generated method stub
		
		//Create temporary hash map reference 
		mv.visitTypeInsn(NEW, "java/util/HashMap");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V");
		
		for (Pair pair : pairList.pairs) {
			mv.visitInsn(DUP);
			pair.visit(this, arg);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
			mv.visitInsn(POP);
		}
		
		return null;
	}

	@Override
	public Object visitAssignPairListCommand(
			AssignPairListCommand assignPairListCommand, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		assignPairListCommand.lValue.visit(this, arg);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "clear", "()V");
		
		if(assignPairListCommand.lValue instanceof SimpleLValue) {
			assignPairListCommand.lValue.visit(this, arg);
			assignPairListCommand.pairList.visit(this, arg);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "putAll", "(Ljava/util/Map;)V");
		} 
		else if (assignPairListCommand.lValue instanceof ExprLValue) {
			ExprLValue exprLValue = (ExprLValue)assignPairListCommand.lValue; 
			String symbol_name = exprLValue.identifier.getText();
			symbol_name = (symbol_table.lookup(symbol_name)).scope_number + symbol_name;
			
			mv.visitFieldInsn(GETSTATIC, className, symbol_name, "Ljava/util/HashMap;");
			exprLValue.expression.visit(this, arg);
			box(exprLValue.expression.type);		
			assignPairListCommand.pairList.visit(this, arg);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
			mv.visitInsn(POP);
		}
		return null;
	}	
	
	
	@Override
	public Object visitLValueExpression(LValueExpression lValueExpression,
			Object arg) throws Exception {
		// TODO Auto-generated method stub
		lValueExpression.lValue.visit(this, arg);
		return null;
	}

	@Override
	public Object visitIntegerLiteralExpression(
			IntegerLiteralExpression integerLiteralExpression, Object arg)
			throws Exception {
		//gen code to leave value of literal on top of stack
		mv.visitLdcInsn(integerLiteralExpression.integerLiteral.getIntVal());
		return null;
	}

	@Override
	public Object visitBooleanLiteralExpression(
			BooleanLiteralExpression booleanLiteralExpression, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		mv.visitLdcInsn(booleanLiteralExpression.booleanLiteral.getText().equalsIgnoreCase("true"));
		return null;
	}

	@Override
	public Object visitStringLiteralExpression(
			StringLiteralExpression stringLiteralExpression, Object arg)
			throws Exception {
		// TODO Auto-generated method stub
		mv.visitLdcInsn(stringLiteralExpression.stringLiteral.getText());
		return null;
	}

	@Override
	public Object visitUnaryOpExpression(UnaryOpExpression unaryOpExpression,
			Object arg) throws Exception {
		// TODO Auto-generated method stub
		unaryOpExpression.expression.visit(this, arg);
		if(unaryOpExpression.op == Kind.MINUS) {
			mv.visitInsn(INEG);
		} 
		else if(unaryOpExpression.op == Kind.NOT) {
			Label EndofJumpLabel = new Label();
			Label falseLabel = new Label();
			mv.visitJumpInsn(IFEQ, falseLabel);
			mv.visitInsn(ICONST_0);
			mv.visitJumpInsn(GOTO, EndofJumpLabel);
			mv.visitLabel(falseLabel);
			mv.visitInsn(ICONST_1);
			mv.visitLabel(EndofJumpLabel);
		} 
		return null;
	}

	private void integerBoolBinaryOp(BinaryOpExpression binaryOpExpression) {
		
		if(binaryOpExpression.op == Kind.PLUS || binaryOpExpression.op == Kind.MINUS ||
				binaryOpExpression.op == Kind.TIMES || binaryOpExpression.op == Kind.DIVIDE) {
			switch(binaryOpExpression.op) {
			case PLUS:
				mv.visitInsn(IADD);
				break;
			case MINUS:
				mv.visitInsn(ISUB);
				break;
			case TIMES:
				mv.visitInsn(IMUL);
				break;
			case DIVIDE:
				mv.visitInsn(IDIV);
				break;			
			}
		} 
		else if(binaryOpExpression.op == Kind.AND || binaryOpExpression.op == Kind.OR) {
			Label blockEndLabel = new Label();
			switch(binaryOpExpression.op) {
			case AND:
				mv.visitInsn(SWAP);
				Label valFalseOp1 = new Label();
				mv.visitJumpInsn(IFEQ, valFalseOp1);
				Label valFalseOp2 = new Label();
				mv.visitJumpInsn(IFEQ, valFalseOp2);
				mv.visitInsn(ICONST_1);
				mv.visitJumpInsn(GOTO, blockEndLabel);
				mv.visitLabel(valFalseOp1);
				mv.visitInsn(POP);
				mv.visitLabel(valFalseOp2);
				mv.visitInsn(ICONST_0);
				mv.visitLabel(blockEndLabel);				
				break;
			case OR:
				mv.visitInsn(SWAP);
				Label valTrueOp1 = new Label();
				mv.visitJumpInsn(IFNE, valTrueOp1);
				Label valTrueOp2 = new Label();
				mv.visitJumpInsn(IFNE, valTrueOp2);
				mv.visitInsn(ICONST_0);
				mv.visitJumpInsn(GOTO, blockEndLabel);
				mv.visitLabel(valTrueOp1);
				mv.visitInsn(POP);
				mv.visitLabel(valTrueOp2);
				mv.visitInsn(ICONST_1);
				mv.visitLabel(blockEndLabel);
				break;
			}
		}
		else {
			Label JumpOnTrueLabel = new Label();
			switch(binaryOpExpression.op) {
			case GREATER_THAN:
				mv.visitJumpInsn(IF_ICMPGT, JumpOnTrueLabel);
				break;		
			case LESS_THAN: 
				mv.visitJumpInsn(IF_ICMPLT, JumpOnTrueLabel);
				break;
			case EQUALS:
				mv.visitJumpInsn(IF_ICMPEQ, JumpOnTrueLabel);
				break;
			case NOT_EQUALS:
				mv.visitJumpInsn(IF_ICMPNE, JumpOnTrueLabel);
				break;	
			case AT_LEAST: // greater than OR equal to
				mv.visitJumpInsn(IF_ICMPGE, JumpOnTrueLabel);
				break;
			case AT_MOST: // less than OR equal to
				mv.visitJumpInsn(IF_ICMPLE, JumpOnTrueLabel);
				break;	
			}
			Label EndofJumpLabel = new Label();
			mv.visitInsn(ICONST_0);
			mv.visitJumpInsn(GOTO, EndofJumpLabel);
			mv.visitLabel(JumpOnTrueLabel);
			mv.visitInsn(ICONST_1);
			mv.visitLabel(EndofJumpLabel);
		}
	}
	
	private void stringBinaryOp(BinaryOpExpression binaryOpExpression, Object arg) throws Exception {
		
		if(binaryOpExpression.op == Kind.PLUS) {
			mv.visitInsn(POP2);
			
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			
			binaryOpExpression.expression0.visit(this, arg);
			switch(((SimpleType)binaryOpExpression.expression0.type).type) {
				case STRING:
					mv.visitMethodInsn(INVOKESTATIC, "java/lang/String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;");
					break;
				case BOOLEAN:
					mv.visitMethodInsn(INVOKESTATIC, "java/lang/String", "valueOf", "(Z)Ljava/lang/String;");
					break;
				case INT:
					mv.visitMethodInsn(INVOKESTATIC, "java/lang/String", "valueOf", "(I)Ljava/lang/String;");
					break;
			}
			
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
			
			binaryOpExpression.expression1.visit(this, arg);
			switch(((SimpleType)binaryOpExpression.expression1.type).type) {
			case STRING:
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
				break;
			case BOOLEAN:
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Z)Ljava/lang/StringBuilder;");
				break;
			case INT:
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;");
				break;
			}	
			
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
//			
//			if( ((SimpleType)binaryOpExpression.expression0.type).type != Kind.STRING ||
//				((SimpleType)binaryOpExpression.expression1.type).type != Kind.STRING) {
//				if( ((SimpleType)binaryOpExpression.expression0.type).type == Kind.BOOLEAN ||
//					((SimpleType)binaryOpExpression.expression1.type).type == Kind.BOOLEAN ) { 
//					mv.visitMethodInsn(INVOKESTATIC, "java/lang/String", "valueOf", "(Z)Ljava/lang/String;");
//				} else {
//					mv.visitMethodInsn(INVOKESTATIC, "java/lang/String", "valueOf", "(I)Ljava/lang/String;");
//				}
//			}
//			
//			if( ((SimpleType)binaryOpExpression.expression0.type).type != Kind.STRING) {
//				mv.visitInsn(SWAP);
//			}
//			
//			mv.visitInsn(SWAP);
//			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
//			mv.visitInsn(DUP2_X1);
//			mv.visitInsn(SWAP);
//			mv.visitMethodInsn(INVOKESTATIC, "java/lang/String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;");
//			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
//			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
//			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
		}
		else if(binaryOpExpression.op == Kind.EQUALS || binaryOpExpression.op == Kind.NOT_EQUALS) {	
			Label JumpOnTrueLabel = new Label();
			switch(binaryOpExpression.op) {
			case NOT_EQUALS:
				mv.visitJumpInsn(IF_ACMPNE, JumpOnTrueLabel);
				break;

			case EQUALS:
				mv.visitJumpInsn(IF_ACMPEQ, JumpOnTrueLabel);
				break;
			}
			Label EndofJumpLabel = new Label();
			mv.visitInsn(ICONST_0);
			mv.visitJumpInsn(GOTO, EndofJumpLabel);
			mv.visitLabel(JumpOnTrueLabel);
			mv.visitInsn(ICONST_1);
			mv.visitLabel(EndofJumpLabel);
		} else if(binaryOpExpression.op == Kind.AT_MOST) {
			mv.visitInsn(SWAP);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z");
		}
	}
	
	private void compoundBinaryOp(BinaryOpExpression binaryOpExpression) {
		// TODO Auto-generated method stub
		switch(binaryOpExpression.op) {
		case PLUS:
			mv.visitMethodInsn(INVOKESTATIC, Runtime.className, "plus", Runtime.binopDescriptor);
			break;
		case MINUS:
			mv.visitMethodInsn(INVOKESTATIC, Runtime.className, "minus", Runtime.binopDescriptor);
			break;
		case TIMES:
			mv.visitMethodInsn(INVOKESTATIC, Runtime.className, "times", Runtime.binopDescriptor);
			break;
		case EQUALS:
			mv.visitMethodInsn(INVOKESTATIC, Runtime.className, "equals", Runtime.relopDescriptor);
			break;
		case NOT_EQUALS:
			mv.visitMethodInsn(INVOKESTATIC, Runtime.className, "not_equals", Runtime.relopDescriptor);
			break;	
		case GREATER_THAN:
			mv.visitMethodInsn(INVOKESTATIC, Runtime.className, "greater_than", Runtime.relopDescriptor);
			break;
		case AT_LEAST: // >=
			mv.visitMethodInsn(INVOKESTATIC, Runtime.className, "at_least", Runtime.relopDescriptor);
			break;
		case LESS_THAN: 
			mv.visitMethodInsn(INVOKESTATIC, Runtime.className, "less_than", Runtime.relopDescriptor);
			break;
		case AT_MOST: // <= 
			mv.visitMethodInsn(INVOKESTATIC, Runtime.className, "at_most", Runtime.relopDescriptor);
			break;
		}
		
	}
	
	@Override
	public Object visitBinaryOpExpression(
			BinaryOpExpression binaryOpExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub
		binaryOpExpression.expression0.visit(this, arg);
		binaryOpExpression.expression1.visit(this, arg);
		
	
		if( binaryOpExpression.expression0.type instanceof CompoundType || 
		    binaryOpExpression.expression1.type instanceof CompoundType ) {
			compoundBinaryOp(binaryOpExpression);				
		} else {
			if((((SimpleType)(binaryOpExpression.expression0).type).type == Kind.STRING) || 
				((SimpleType)(binaryOpExpression.expression1).type).type == Kind.STRING) {
				stringBinaryOp(binaryOpExpression, arg);
			} 
			else {
				//(((SimpleType)(binaryOpExpression.type)).type == Kind.INT) || 
				//((SimpleType)(binaryOpExpression.type)).type == Kind.BOOLEAN)
				integerBoolBinaryOp(binaryOpExpression);
			}
		}	
		return null;
	}

}
