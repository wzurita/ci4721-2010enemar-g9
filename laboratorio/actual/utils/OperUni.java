package utils;

public class OperUni extends Expr {
	  String operador ;
	  Expr   expresion;

	  OperUni(String oper) {
	    operador  = oper;
	    expresion = null;
	  }

	  void exp(Expr e) {
	    expresion = e;
	  } 

	}