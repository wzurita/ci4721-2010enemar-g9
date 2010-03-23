package utils;

public class DefFun extends Def {
	  private String   nombre;
	  private Type     tipo;
	  private DefVar[] vars;
	  private Bloque   bloque;
	  
	  DefFun(String n, Type t, DefVar[] v, Bloque b) {
	    nombre = n;
	    tipo = t;
	    vars = v;
	    bloque = b;
	  }
	  
	  public String getName(){
		  return nombre;
	  }
	  
	  public Type getType(){
		  return tipo;
	  }
	  
	  public DefVar[] getVar(){
		  return vars;
	  }
	}
