package utils;

public class ValChar extends Val {
	  char letra;

	  ValChar(String x) {
	    letra = x.charAt(0); // recordar que se permiten: '\NUL', '\64', '\n', ect
	  }
	}
