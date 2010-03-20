package utils;

public class ValStr extends Val {
	  String str;

	  ValStr(String x) {
	    str = x; //aki se tranforman los caracteres escapados (\n, \", ect)
	  }
	}