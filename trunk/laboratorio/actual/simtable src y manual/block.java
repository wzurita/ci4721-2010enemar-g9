package simtable;

import java.util.LinkedList;
import java.util.List;

public class block extends instruction{

	List<instruction> instructions;
	simtable tablasimbolos;
	
	
	block(){
		instructions = new LinkedList<instruction>();
	}
	
	
	boolean add(instruction ins){		
			return instructions.add(ins);	
	}
	
	instruction get(int i){
		if(i < instructions.size() && i >= 0){
			return instructions.get(i);
		}else{
			return null;
		}
	}
	
	// Elimina todos los objetos de la tabla.
	void clear(){
		instructions.clear();		
	}
	
}
