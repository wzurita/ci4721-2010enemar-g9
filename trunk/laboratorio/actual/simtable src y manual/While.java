package simtable;

public class While extends instruction{
	
	private absTree condition;
	private block instructions;
	
	While(absTree Condition, block Instrucciones){
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
