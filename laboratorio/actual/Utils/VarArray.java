package utils;

public class VarArray extends Var {
	  Var  variable;
	  Expr indice  ;

	  VarArray(Var v, Expr i){
	    variable = v;
	    indice   = i;
	  }
	}