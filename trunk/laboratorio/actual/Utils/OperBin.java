package utils;

public class OperBin extends Expr {
	  String operador ;
	  Expr   derecho  ;
	  Expr   izquierdo;

	  OperBin(String oper) {
	    operador  = oper;
	    derecho   = null;
	    izquierdo = null;
	  }

	  void der(Expr d) {
	    derecho = d;
	  } 

	  void izq(Expr i) {
	    izquierdo = i;
	  } 
	}