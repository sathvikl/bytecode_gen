package edu.ufl.cise.cop5555.sp12.context;

import java.util.Hashtable;
import java.util.Stack;

import edu.ufl.cise.cop5555.sp12.ast.*;


public class SymbolTable {
	Stack <Integer> scope_stack;
	Integer current_scope, next_scope; 
	Hashtable<String, Hashtable<Integer, Declaration>> symbol_table;

	public SymbolTable(){
     //TODO Implement me
		scope_stack = new Stack<Integer>();
		symbol_table = new Hashtable<String, Hashtable<Integer, Declaration>>(); 
		current_scope = 0;
		next_scope = 0;
	}

	public void enterScope() {
		current_scope = next_scope++; 
		scope_stack.push(current_scope);
		//TODO Implement me
	}

	public void exitScope() {
		scope_stack.pop();
		//TODO Implement me
	}

	// returns the in-scope declaration of the name if there is one, 
	//otherwise it returns null
	public Declaration lookup(String ident) {
    //TODO Implement me
		@SuppressWarnings("unchecked")
		Stack<Integer> SSCopy = (Stack<Integer>)scope_stack.clone();
		Hashtable<Integer, Declaration> ident_list;
		
		if(symbol_table.containsKey(ident)) {
			ident_list = symbol_table.get(ident);
		} else {
			return null;
		}
		
		// Remove this after testing !
//		System.out.println("Printing Symbol table Stack");
//		for(Integer k : scope_stack) {
//			System.out.println(k + " ");
//		}
		
		while(!SSCopy.isEmpty()) {
			if(ident_list.containsKey(SSCopy.peek())) {
				Declaration Dec = ident_list.get(SSCopy.peek());
				return Dec;
			}
			SSCopy.pop();
		}
		return null;
	}
	

	// if the name is already declared IN THE CURRENT SCOPE, returns false. 
	//Otherwise inserts the declaration in the symbol table
	public boolean insert(String ident, Declaration dec) {
	//TODO Implement me
		Hashtable<Integer, Declaration> ident_list;
		boolean error = false;
		
		if(symbol_table.containsKey(ident) == false) {
			ident_list = new Hashtable<Integer, Declaration>();
			dec.scope_number = scope_stack.peek();
			ident_list.put(dec.scope_number, dec);
		} else {
			ident_list = symbol_table.get(ident);
			dec.scope_number = scope_stack.peek();
			if(ident_list.containsKey(dec.scope_number)) {
				error = true;
			} else {
				error = false; 
				ident_list.put(dec.scope_number, dec);
			}	
		}
		
		if(!error) {
			symbol_table.put(ident, ident_list);
			return true;
		} else {
			return false;
		}
	}
}