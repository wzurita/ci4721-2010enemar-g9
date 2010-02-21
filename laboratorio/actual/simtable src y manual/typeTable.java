package simtable;

import java.util.List;
import java.util.LinkedList;

public class typeTable {
	
	List<String> types;
	
	// Crea la tabla y agrega los tipos basicos.
	typeTable(){
		types = new LinkedList<String>();	
		types.add("Int");
		types.add("Real");
		types.add("Char");
		types.add("Bool");		
	}
	
	// funcion que devuelve el indice de un tipo o -1 si no existe
	int exists(String nombre){
		int index = -1;
		
		if(types.size() > 0){
			for(int tmp=0; tmp < types.size(); tmp++){
				if (types.get(tmp).equals(nombre)){
					index = tmp;
					tmp = types.size();
				}				
			}			
		}		
		return index;
	}
	
	// Agrega un objeto no repetido a la tabla, regresa false si el objeto existe en la tabla
	boolean add(String nombre){
		if(this.exists(nombre) == -1){
			return types.add(nombre);
		}		
		return false;
	}
	
	// Obtiene un objeto de la tabla, regresa null si el objeto no existe
	String get(String nombre){
		int index =this.exists(nombre);
		
		if(index != -1){
			return types.get(index);
		}		
		return null;		
	}
}
