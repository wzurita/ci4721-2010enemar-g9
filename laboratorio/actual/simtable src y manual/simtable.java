package simtable;

import java.util.List;
import java.util.LinkedList;
import simtable.simbol;

/* clase de la tabla de simbolos:
 * Contiene una lista de simbolos y las funciones 
 * apropiadas para manejarla
 */

public class simtable{

	List<simbol> symbols;
	
	// Constructor
	simtable(){
		symbols = new LinkedList<simbol>();
	}
	
	// funcion que devuelve el indice de un simbolo o -1 si no existe
	int exists(String nombre){
		int index = -1;
		
		if(symbols.size() > 0){
			for(int tmp=0; tmp < symbols.size(); tmp++){
				if (symbols.get(tmp).Nombre().equals(nombre)){
					index = tmp;
					tmp = symbols.size();
				}				
			}			
		}		
		return index;
	}
	
	// Agrega un objeto no repetido a la tabla, regresa false si el objeto existe en la tabla
	boolean add(String nombre, String tipo, char varType){
		if(this.exists(nombre) == -1){
			return symbols.add(new simbol(nombre,tipo,varType));
		}		
		return false;
	}
	
	// Obtiene un objeto de la tabla, regresa null si el objeto no existe
	simbol get(String nombre){
		int index =this.exists(nombre);
		
		if(index != -1){
			return symbols.get(index);
		}		
		return null;		
	}
	
	// Elimina todos los objetos de la tabla.
	void clear(){
		symbols.clear();		
	}
}