package utils;

public class DefFun extends Def {
	  String   nombre;
	  Type     tipo;
	  DefVar[] vars;
	  Bloque   bloque;
	  
	  DefFun(String n, Type t, DefVar[] v, Bloque b) {
	    nombre = n;
	    tipo = t;
	    vars = v;
	    bloque = b;
	  }
	}
