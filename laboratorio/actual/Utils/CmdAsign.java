package utils;

public class CmdAsign extends Cmd{
	  Var  variable;
	  Expr expresion;
	 
	  CmdAsign(Var v, Expr e) {
	    variable  = v;
	    expresion = e;
	  }
	}
