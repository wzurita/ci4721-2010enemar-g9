package utils;

public class CmdWhile extends Cmd{
	  Expr   condicion;
	  Bloque bloque;
	 
	  CmdIf(Expr c, Bloque b) {
	    condicion = c;
	    bloque    = b;
	  }
	}