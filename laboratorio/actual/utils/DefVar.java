package utils;

public class DefVar extends Def {
	  private String nombre;
	  private Type   tipo;
	  
	  public DefVar(String n,Type t) {
	    nombre = n;
	    tipo = t;
	  }
	  
	  public String getName(){
		  return nombre;
	  }
	  
	  public Type getType(){
		return tipo;  
	  }	  
	}