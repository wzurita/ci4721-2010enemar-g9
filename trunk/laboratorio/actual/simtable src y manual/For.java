package simtable;

public class For extends instruction{
	
	private absTree condition;
	private block instructions;
	private absTree variable;
	
	For(absTree Condition, block Instrucciones, absTree Variable){
		condition = Condition;
		instructions = Instrucciones;
		variable = Variable;
	}
	
	absTree variable(){
		return variable;
	}
	
	absTree condition(){
		return condition;
	}
	
	block instructions(){
		return instructions;
	}
	
}