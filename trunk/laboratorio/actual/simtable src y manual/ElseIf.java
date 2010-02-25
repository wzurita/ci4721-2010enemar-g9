package simtable;

public class ElseIf extends instruction{
	
	private absTree condition;
	private block instructions;
	
	
	ElseIf(absTree Condition, block Instrucciones, absTree Complemento){
		condition = Condition;
		instructions = Instrucciones;
	}
	
	absTree condition(){
		return condition;
	}
	
	block instructions(){
		return instructions;
	}
	
}