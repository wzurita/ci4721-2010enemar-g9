/* Clase symbol
 * Contiene el nombre y el tipo de cualquier variable o funcion
 * como dos strings, futuras implementaciones incluiran tambien el 
 * posible valor en caso de ser una variable para facilitar la 
 * reduccion de operaciones en el arbol abstracto.
 */ 

package simtable;

public class simbol {

 private String Name;
 
 private String Type; 
 
 private char varType; 

 simbol(String name, String type, char var){
	 Name = name;
	 Type = type;
	 varType = var;
	}
	
 String Nombre(){
	 return Name;
	 }
	 
 String Tipo(){
	 return Type;
	 }
 
 char varType(){
	return varType; 	 
 	}
}