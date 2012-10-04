package edu.ufl.cise.cop5555.sp12;

import edu.ufl.cise.cop5555.sp12.TokenStream;
import edu.ufl.cise.cop5555.sp12.TokenStream.Token;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import static edu.ufl.cise.cop5555.sp12.Kind.*;
import edu.ufl.cise.cop5555.sp12.ast.*;

	public class Parser {

		TokenStream stream;
		ListIterator<TokenStream.Token> TokenIterator;
		TokenStream.Token curToken;
		
		public Parser(TokenStream tk_stream)
		{ 
			stream = tk_stream;
			TokenIterator = stream.iterator();
		}
		
		private Kind getSymbol() {
			if(TokenIterator.hasNext()) {
				 curToken = TokenIterator.next();
				 return curToken.kind;
			}
			return null;
		}
		
		private void reset() {
			curToken = TokenIterator.previous();
		}
		
		public AST parse() throws SyntaxException {
			return Program();  //method corresponding to start symbol	
		}
		
		private TokenStream.Token expect(Kind compareTo) throws SyntaxException {
			Kind curSymKind = getSymbol();
			if(compareTo != curSymKind) 
				throw new SyntaxException(curToken, "Mismatch in kind");
			else
				return curToken;
		}
		
		private AST Program() throws SyntaxException {
			Block OBlock;
			Program OProgram;
			Token ident;
			
			expect(PROG);
			ident = expect(IDENTIFIER);
			OBlock = Block();
			expect(GORP);
			expect(EOF); 
			OProgram = new Program(ident, OBlock);
			return OProgram;
		}

		private Block Block() throws SyntaxException {
			Kind curSymKind;
			Block OBlock;
			Command OCommand;
			ArrayList<DecOrCommand> DCList;
			Declaration Odecl;
			Type type;
			Token ident;
			
			DCList = new ArrayList<DecOrCommand>();
			do {
				curSymKind = getSymbol();
				//Declaration 
				if(curSymKind == INT || curSymKind == BOOLEAN || curSymKind == STRING || curSymKind == MAP) {					
					if(curSymKind == MAP) {
						reset();
						type = CompoundType();
					} else {
						type = new SimpleType(curSymKind);
					}
					ident = expect(IDENTIFIER);
					expect(SEMI);
					Odecl = new Declaration(type, ident);
					DCList.add(Odecl);
				} 
				//Command
				else if(curSymKind != GORP && curSymKind != ELSE && curSymKind != FI && curSymKind != OD) { 
					reset();
					OCommand = Command(); 
					expect(SEMI);
					if(OCommand != null) {
						DCList.add(OCommand);
					}
				}
			} while(curSymKind != GORP  && curSymKind != ELSE && curSymKind != FI && curSymKind != OD);
			reset();
			
			OBlock = new Block(DCList);
			return OBlock;
		}
	    
		private CompoundType CompoundType() throws SyntaxException {
			Kind curSymKind;
			SimpleType oSimType;
			Type oType;
			CompoundType oCompType;
			
			expect(MAP);
			expect(LEFT_SQUARE);
			curSymKind = getSymbol();
			if(curSymKind != INT && curSymKind != BOOLEAN && curSymKind != STRING) {
				throw new SyntaxException(curToken, "Mismatch in compund type");
			} else {
				oSimType = new SimpleType(curSymKind);
			}
			expect(COMMA);
			curSymKind = getSymbol();
			if(curSymKind == INT || curSymKind == BOOLEAN || curSymKind == STRING || curSymKind == MAP) {
				if(curSymKind == MAP) {
					reset();
					oType = CompoundType();
				} else {
					oType = new SimpleType(curSymKind);
				}
			} else {
				throw new SyntaxException(curToken, "Mismatch in compund type");
			}
			expect(RIGHT_SQUARE);
			oCompType = new CompoundType(oSimType, oType);
			return oCompType;
		}

		private Command Command() throws SyntaxException {
			Kind curSymKind;
			Command OCommand;
			
			curSymKind = getSymbol();
			if(curSymKind == SEMI) {
				reset();
				OCommand = null;
			} 
			else if(curToken.kind == IDENTIFIER) {
				LValue oLValue;
				PairList oPList;
				Expression oExpress;
				
				reset();
				oLValue = LValue();
				
				curSymKind = getSymbol();
				if (curSymKind == ASSIGN) {
					curSymKind = getSymbol();
					if(curSymKind == LEFT_BRACE) {
						reset();
						oPList = PairList();
						OCommand = new AssignPairListCommand(oLValue, oPList);
					} else {
						reset();
						oExpress = Expression();
						OCommand = new AssignExprCommand(oLValue, oExpress);
					}
				} else {
					throw new SyntaxException(curToken, "Mismatch");
				}
			}
			else if(curToken.kind == PRINT) {
				Expression OExpress = Expression();
				OCommand = new PrintCommand(OExpress);
			}
			else if(curToken.kind == PRINTLN) {
				Expression OExpress = Expression();
				OCommand = new PrintlnCommand(OExpress);
			}
			else if(curToken.kind == IF) {
				Block ifBlock, elseBlock;
				Expression oExpression;
				
				expect(LEFT_PAREN);
				oExpression = Expression();
				expect(RIGHT_PAREN);
				ifBlock = Block();
				curSymKind = getSymbol();
				if(curToken.kind == ELSE) {
					elseBlock = Block();
					expect(FI);
					OCommand = new IfElseCommand(oExpression, ifBlock, elseBlock);
				} 
				else if(curToken.kind == FI) {
					OCommand = new IfCommand(oExpression, ifBlock);	
				} 
				else {
					throw new SyntaxException(curToken, "Mismatch");
				}
			}
			else if(curToken.kind == DO) {
				Token key, val;
				LValue oLVal;
				Block oBlock;
				Expression oExpression;
				
				curSymKind = getSymbol();
				if(curSymKind == LEFT_PAREN) {
					oExpression = Expression();
					expect(RIGHT_PAREN);
					oBlock = Block();
					expect(OD);
					OCommand = new DoCommand(oExpression, oBlock);
				} 
				else if (curSymKind == IDENTIFIER) {
					reset();
					oLVal = LValue();					
					curSymKind = getSymbol();
					if(curSymKind == COLON) {
						expect(LEFT_SQUARE);
						key = expect(IDENTIFIER);
						expect(COMMA);
						val = expect(IDENTIFIER);
						expect(RIGHT_SQUARE);
					} else {
						throw new SyntaxException(curToken, "Mismatch");
					}
				    oBlock = Block();
				    expect(OD);
				    OCommand = new DoEachCommand(oLVal, key, val, oBlock);
				} 
				else {
					throw new SyntaxException(curToken, "Mismatch");
				}
			} 
			else {
				throw new SyntaxException(curToken, "Mismatch");
			}
			return OCommand;
		}
		
		private Expression Expression() throws SyntaxException {
			Kind curSymKind, op;
			Expression oLExpression, oRExpression;
			BinaryOpExpression OBOpExpress;
			Expression oExpress;
			
			oExpress = Term();
			// Process one more additional terms.. 
			curSymKind = getSymbol();	
			while(curSymKind == OR || curSymKind == AND || curSymKind == EQUALS || curSymKind == NOT_EQUALS 
					|| curSymKind == LESS_THAN || curSymKind == GREATER_THAN || curSymKind == AT_MOST 
					|| curSymKind == AT_LEAST) {
				oLExpression = oExpress;
				op = curSymKind;
				oRExpression = Term();
				OBOpExpress = new BinaryOpExpression(oLExpression, op, oRExpression);
				oExpress = OBOpExpress;
				curSymKind = getSymbol(); // for the next iteration
			}
			reset();
			
			return oExpress;
		}
		
		private Expression Term() throws SyntaxException {
			Kind curSymKind, op;
			Expression oLExpression, oRExpression;
			BinaryOpExpression OBOpExpress;
			Expression oExpress;
			
			oExpress = Element();
			curSymKind = getSymbol();	
			while(curSymKind == PLUS || curSymKind == MINUS) {
				oLExpression = oExpress;
				op = curSymKind;
				oRExpression = Element();
				OBOpExpress = new BinaryOpExpression(oLExpression, op, oRExpression);
				oExpress = OBOpExpress;				
				curSymKind = getSymbol();
			}
			reset();			
			
			return oExpress;
		}		

        private Expression Element() throws SyntaxException {
        	Kind curSymKind, op;
			Expression oLExpression, oRExpression;
			BinaryOpExpression OBOpExpress;
			Expression oExpress;
        	
			oExpress = Factor();
			curSymKind = getSymbol();	
			while(curSymKind == TIMES || curSymKind == DIVIDE) {
				oLExpression = oExpress;
				op = curSymKind;
				oRExpression = Factor();
				OBOpExpress = new BinaryOpExpression(oLExpression, op, oRExpression);
				oExpress = OBOpExpress;		
				curSymKind = getSymbol();
			}
			reset();
			
			return oExpress;
		}

        private Expression Factor() throws SyntaxException {
        	Kind curSymKind;
        	Expression oExpression;
        	
			curSymKind = getSymbol();
			if(curToken.kind == IDENTIFIER) {
				reset();
				LValue OLVal = LValue();
				oExpression = new LValueExpression(OLVal);
			}
			else if(curSymKind == LEFT_PAREN) {
				oExpression = Expression();
				expect(RIGHT_PAREN);
			}
			else if(curSymKind == NOT || curSymKind == MINUS) {
				oExpression = Factor();
				Expression OUnOpExpress = new UnaryOpExpression(curSymKind, oExpression);
				oExpression = OUnOpExpress;
			}
			else if(curSymKind == INTEGER_LITERAL) {
				 oExpression = new IntegerLiteralExpression(curToken); 
			} 
			else if (curSymKind == STRING_LITERAL) {
				oExpression = new StringLiteralExpression(curToken);
			} 
			else if (curSymKind == BOOLEAN_LITERAL) {
				oExpression = new BooleanLiteralExpression(curToken);
			}
			else {
				throw new SyntaxException(curToken, "Mismatch");
			}
			
			return oExpression;
		}        
        
        private LValue LValue() throws SyntaxException {
        	Kind curSymKind;
        	Expression oExpression;
        	LValue oLVal;
        	Token ident; 
        	
        	curSymKind = getSymbol();
			if(curToken.kind == IDENTIFIER) {
				ident = curToken;
				curSymKind = getSymbol();
				if(curSymKind == LEFT_SQUARE) {
					oExpression = Expression();
					expect(RIGHT_SQUARE);
					oLVal = new ExprLValue(ident, oExpression);
				} else {
					reset();
					oLVal = new SimpleLValue(ident);
				}
			} else {
				throw new SyntaxException(curToken, "Mismatch");
			}
			
			return oLVal;
        }
        
		private PairList PairList() throws SyntaxException {
			Kind curSymKind;
			PairList oPairL;
			Pair oPair;
			ArrayList<Pair> oList;
			
			oList = new ArrayList<Pair>(); 
			expect(LEFT_BRACE);
			curSymKind = getSymbol();
			if(curSymKind != RIGHT_BRACE) {
				reset();
				oPair = Pair();
				oList.add(oPair);
				curSymKind = getSymbol();
				
				if(curSymKind != RIGHT_BRACE) {
					do {
						reset();
						expect(COMMA) ;
						oPair = Pair();	
						oList.add(oPair);
						curSymKind = getSymbol();
					} while(curSymKind != RIGHT_BRACE);
				}
			}
			reset();
			expect(RIGHT_BRACE);
			oPairL = new PairList(oList);
			return oPairL;
		}
		
		private Pair Pair() throws SyntaxException {
			Expression oExpr1, oExpr2;
			Pair oPair;
			
			expect(LEFT_SQUARE);
			oExpr1 = Expression();
			expect(COMMA);
			oExpr2 = Expression();
			expect(RIGHT_SQUARE);
			oPair = new Pair(oExpr1, oExpr2);
			return oPair;
		}				
	}
	