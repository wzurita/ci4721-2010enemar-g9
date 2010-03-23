package utils;

public class DefType extends Def {
	  private String nombre;
	  private Type   tipo;
	  
	  public DefType(String n, Type t) {
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