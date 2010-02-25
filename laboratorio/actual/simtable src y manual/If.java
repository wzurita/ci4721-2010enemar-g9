package simtable;

import java.util.LinkedList;
import java.util.List;

public class If extends instruction{
	
	private absTree condition;
	private block instructions;
	private absTree Else;
	List<ElseIf> ElseIf;
	
	If(absTree Condition, block Instrucciones, absTree Complemento){
		condition = Condition;
		instructions = Instrucciones;
		Else = Complemento;
		ElseIf = new LinkedList<ElseIf>();
	}
	
	absTree Else(){
		return Else;
	}
	
	absTree condition(){
		return condition;
	}
	
	block instructions(){
		return instructions;
	}
	
}