package simtable;

import java.util.List;
import java.util.LinkedList;
import simtable.function;

public class functionTable {

	List<function> functions;
	simtable globalTable;
	
	functionTable(){
		functions = new LinkedList<function>();
	}
	
	// funcion que devuelve el indice de un simbolo o -1 si no existe
	int exists(String nombre, String TipoOut){
		int index = -1;
		
		if(functions.size() > 0){
			for(int tmp=0; tmp < functions.size(); tmp++){
				if (functions.get(tmp).Nombre().equals(nombre) && functions.get(tmp).TipoSalida().equals(TipoOut)){
					index = tmp;
					tmp = functions.size();
				}				
			}			
		}		
		return index;
	}
	
	// Agrega un objeto no repetido a la tabla, regresa false si el objeto existe en la tabla
	boolean add(String nombre, String[] inTypes, String outType, simtable variableEntrada){
		if(this.exists(nombre,outType) == -1){
			return functions.add(new function(nombre,inTypes,outType,variableEntrada));
		}		
		return false;
	}
	
	// Obtiene un objeto de la tabla, regresa null si el objeto no existe
	function get(String nombre, String outype){
		int index =this.exists(nombre,outype);
		
		if(index != -1){
			return functions.get(index);
		}		
		return null;		
	}
	
	// Elimina todos los objetos de la tabla.
	void clear(){
		functions.clear();		
	}
	
}
