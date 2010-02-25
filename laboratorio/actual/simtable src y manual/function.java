package simtable;

import java.util.LinkedList;
import java.util.List;

public class function {
	
	private String Name;
	
	private String intypes[];
	 
	simtable localTable;
	
	private String outType; 
	
	List<instruction> instructions;
	
	function(String Nombre, String tiposentrada[], String tipoSalida, simtable variablesEntrada){
		Name = Nombre;
		intypes = tiposentrada;
		outType = tipoSalida;
		localTable = variablesEntrada;
		instructions = new LinkedList<instruction>();
	}
	
	String Nombre(){
		return Name;		
	}
	
	String[] TiposEntrada(){
		return intypes;		
	}
	
	String TipoSalida(){
		return outType;
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
