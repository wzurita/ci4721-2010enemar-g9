package utils;

public class ValExpr extends Val {
	  Expr expresion;

	  ValExpr(Expr expr){
	    expresion = expr; // notese que las expresiones no necesitan transformacion
	  }
	}
