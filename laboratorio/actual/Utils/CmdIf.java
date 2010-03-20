package utils;

public class CmdIf extends Cmd{
	  Expr   condicion;
	  Bloque bloqueThen;
	  Bloque bloqueElse;
	 
	  CmdIf(Expr c, ThenElse te) {
	    condicion  = c;
	    bloqueThen = te.t;
	    bloqueElse = te.e;
	  }
	}
