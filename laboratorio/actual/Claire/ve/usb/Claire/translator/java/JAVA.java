/**********************************************************************
 * Claire a parser generator.                                         *
 * Copyright (C) 1999  Paul Pacheco <93-25642@ldc.usb.ve>             *
 *                                                                    *
 * This library is free software; you can redistribute it and/or      *
 * modify it under the terms of the GNU Lesser General Public         *
 * License as published by the Free Software Foundation; either       *
 * version 2 of the License, or (at your option) any later version.   *
 *                                                                    *
 * This library is distributed in the hope that it will be useful,    *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of     *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU  *
 * Lesser General Public License for more details.                    *
 *                                                                    *
 * You should have received a copy of the GNU Lesser General Public   *
 * License along with this library; if not, write to the Free         *
 * Software Foundation, Inc., 59 Temple Place, Suite 330,             *
 * Boston, MA  02111-1307  USA                                        *
 *                                                                    *
 * Please contact Paul Pacheco <93-25642@ldc.usb.ve> to submit any    *
 * suggestion or bug report.                                          *
 **********************************************************************/

/*
 * $Id: JAVA.java,v 1.3 1999/11/21 02:12:40 Paul Exp $
 *
 * The changes to this file are:
 *
 * $Log: JAVA.java,v $
 * Revision 1.3  1999/11/21 02:12:40  Paul
 * Dramatically reduced the size of the generated tables (suggested by Ascander Suarez)
 *
 * Revision 1.2  1999/11/07 12:18:43  Paul
 * Moved the runtime library to the java directory
 *
 * Revision 1.1  1999/11/07 11:46:03  Paul
 * Moved the java translator files into the java package
 *
 * Revision 1.4  1999/11/06 01:44:26  Paul
 * Improved the translator, now a template file is used to make it
 * easier to modify.
 *
 * Revision 1.3  1999/11/04 05:37:33  Paul
 * Improved expansion of alias inside Regular Expressions and Code
 *
 * Revision 1.2  1999/11/01 11:30:21  Paul
 * *** empty log message ***
 *
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 * Revision 1.5  1999/10/13 02:06:10  Paul
 * Better output of error messages
 *
 * Revision 1.4  1999/10/12 04:16:41  Paul
 * *** empty log message ***
 *
 * Revision 1.3  1999/09/09 09:58:11  Paul
 * Updated versioning report to use cvs
 *
 * Revision 1.2  1999/09/09 09:53:55  Paul
 * Added some cvs comments
 *
 */

package ve.usb.Claire.translator.java;
import java.io.*;
import java.util.*;
import ve.usb.Claire.contextFree.symbol.*;
import ve.usb.Claire.contextFree.*;
import ve.usb.Claire.table.*;
import ve.usb.Claire.*;
import ve.usb.Claire.runtime.java.Pack;
import ve.usb.Claire.translator.*;

import java.io.*;
import java.util.zip.*;
import gnu.getopt.*;

/**
 * Translator that takes a compiler unit and translates it to java
 * @version     $Revision: 1.3 $
 * @author      Paul Pacheco
 * @since       JDK1.2
 */

public class JAVA implements Translator, TemplateTranslator
{

      /**
       * Unit to be compiled into a new file
       * @see ve.usb.Claire.Unit
       */
      protected Unit unit;

      /**
       * Grammar of the unit to be compiled
       * @see ve.usb.Claire.contextFree.Grammar
       */
      protected Grammar grammar;

      /**
       * Table to be generated by the unit
       * @see ve.usb.Claire.table.Table
       */
      protected Table table;


      /**
       * Creates a java translator. A java translator translates a unit
       * to java
       */
      public JAVA()
      {
      }

      /**
       * Default class name for the generated Class
       */
      private String className="Parser";

      /**
       * determines if zip compression should be aplied
       */
      private boolean fullCompress=false;
		
      private JAVAParser javaParser;
      
      /**
       * Translate the parser to the java language.
       * @param args the command line arguments for this translator
       * @param unit the compiler unit to be translated
       */
      public void translate(String args[], Unit unit) throws Exception
      {
	 this.unit = unit;

	 cmdLine(args);
			
	 this.table = unit.compile();
	 this.grammar= unit.grammar();
			
	 String fileName= unit.outputDir();
			
	 javaParser= new JAVAParser(unit);
	 javaParser.className= className;

	 //System.out.println(unit.languageDependant());
	 javaParser.parse(new StringReader(unit.languageDependant()));

	 if (fileName == null)
	    fileName= javaParser.className;
	 else
	    fileName+= javaParser.className;

	 fileName+=".java";

	 PrintWriter out = new PrintWriter( new BufferedWriter( new FileWriter( fileName)));
						
	 Template.translate("ve/usb/Claire/translator/java/Template.tpl",out, this);

	 out.close();

	 unit.reportProgress("Finished writing parser, time:");
      }
		
      /**
       * returns the command to get the line start for a given symbol.
       * @param position is the possition in the stack where the symbol is
       *    going to be
       */
      public String lineStart(int position)
      {
	 String value= "yyValueStack.elementAt(yyValueStack.size() - " + position + ")";
	 return "((ve.usb.Claire.runtime.java.Symbol)"+value+").lineStart()";
      }
		
      /**
       * returns the command to get the line end for a given symbol.
       * @param position is the possition in the stack where the symbol is
       *    going to be
       */
      public String lineEnd(int position)
      {
	 String value= "yyValueStack.elementAt(yyValueStack.size() - " + position + ")";
	 return "((ve.usb.Claire.runtime.java.Symbol)"+value+").lineEnd()";
      }

      /**
       * returns the command to get the column start for a given symbol.
       * @param position is the possition in the stack where the symbol is
       *    going to be
       */
      public String colStart(int position)
      {
	 String value= "yyValueStack.elementAt(yyValueStack.size() - " + position + ")";
	 return "((ve.usb.Claire.runtime.java.Symbol)"+value+").colStart()";
      }

      /**
       * returns the command to get the char start for a given symbol.
       * @param position is the possition in the stack where the symbol is
       *    going to be
       */
      public String colEnd(int position)
      {
	 String value= "yyValueStack.elementAt(yyValueStack.size() - " + position + ")";
	 return "((ve.usb.Claire.runtime.java.Symbol)"+value+").colEnd()";
      }

      /**
       * returns the command to get the char start for a given symbol.
       * @param position is the possition in the stack where the symbol is
       *    going to be
       */
      public String charStart(int position)
      {
	 String value= "yyValueStack.elementAt(yyValueStack.size() - " + position + ")";
	 return "((ve.usb.Claire.runtime.java.Symbol)"+value+").charStart()";
      }

      /**
       * returns the command to get the char end for a given symbol.
       * @param position is the possition in the stack where the symbol is
       *    going to be
       */
      public String charEnd(int position)
      {
	 String value= "yyValueStack.elementAt(yyValueStack.size() - " + position + ")";
	 return "((ve.usb.Claire.runtime.java.Symbol)"+value+").charEnd()";
      }

      static byte [] compress (byte src[])
      {
	 try
	 {

				
	    ByteArrayOutputStream outArray = new ByteArrayOutputStream();
				
	    OutputStream out = new GZIPOutputStream(outArray);
				
	    out.write(src);
				
	    out.close();
				

	    return outArray.toByteArray();
	 }
	 catch (Exception e)
	 {
	 }
	 return null;
      }

      /**
       * writes an array, this is usefull for writing the tables
       * @param name the name of the array
       * @param array the content of the array
       * 
       */
      public void writeArray(byte array[], PrintWriter out)
      {
			
	 out.print("{");

	 int originalSize= array.length;

	 int packed[];
			
	 if (fullCompress)
	    array = compress(array);


	 out.println("");
	 //out.print("\t\t\"");
	 StringBuffer buffer= new StringBuffer(400*4);
			
	 for (int i=0;i< array.length; i++)
	 {

	    if (i % 400 == 0 && i < array.length-1 )
	    {

	       out.print(buffer.toString());
	       buffer.setLength(0);
	       if (i > 0)
	       {
						
		  out.println("\",");
	       }
	       out.print("\t\t\"");
	    }

	    buffer.append('\\');
	    buffer.append(Integer.toOctalString(array[i] & 0xff));
	 }

	 out.print(buffer.toString());
	 out.println("\"}");

			
      }

      /**
       * return the substitution for $1, $2, $3, ... for the desired language.
       * @param position is the position in the stack where the value is going to be
       * @param type the type for the value
       */		  
      public String valueAt(int position, String type)
      {
	 String value= "yyValueStack.elementAt(yyValueStack.size() - " + position + ")";
	 return castToType(value, type);
      }
		
      /**
       * returns the substitution for $$ for the desired language
       * @return the string used for the $$ value
       */
      public String returnVar()
      {
	 return "yyResult";
      }

      /**
       * returns the default value for when there is no substitution, for example
       * the value of %left or the non existant value of a $1
       * @return the string used as a default value
       */
      public String defaultValue()
      {
	 return "null";
      }
		
      /**
       * returns the data type for tokens. For example it could be String
       * @return the data type for tokens (RE)
       */
      public String tokenType()
      {
	 return "java.lang.String";
      }
		

      /**
       * writes all the code for the rules
       * simply tells every rule to dump its code
       */
      public void emitActions(PrintWriter out)
      {
		
	 Iterator iter =grammar.iterator();
	 while (iter.hasNext())
	 {
	    Rule rule=(Rule)iter.next();

	    CodeSymbol code= rule.action();

	    if (code != null)
	    {
	       out.println("\t\t\tcase " + rule.getId() +": //" + rule);
	       out.println("\t\t\t{");

	       String returntype=code.getType();
					
	       if (returntype==null)
		  returntype="Object";
										
	       out.println("\t\t\t\t"+returntype+" yyResult ="+defaultValue(returntype)+";");
					
	       out.println("\t\t\t\t// sorry for this stupid work around, but java would'nt let me throw exceptions otherwise");
	       // work around so exceptions can be thrown
	       out.println("\t\t\t\tif (true)");
	       out.println("\t\t\t\t{");
					
	       String translatedCode = code.translate(this, unit);


	       out.println(translatedCode);

	       out.println("");
	       out.println("\t\t\t\t}");
	       out.println("\t\t\t\treturn "+castFromType("yyResult",returntype)+";");
	       out.println("\t\t\t}");
	    }				
	 }
				
      }
		
      
      /**
       * writes the methods for starting the automata.
       * @param name the name of the method
       * @param sym the symbol from where the parsing will be carried out
       */	 		
      private void emitStart(String name, Symbol sym, PrintWriter out)
      {
	 String retType=null;

	 if (sym!=null)
	    retType=sym.getType();

	 if (retType==null)
	    retType="Object";

	 out.println("\t/**");
	 out.println("\t *parses a source inputstream using the default scanner");
	 out.println("\t *@param in the source to be parsed");
	 out.println("\t *@return the value calculated for the start symbol");
	 out.println("\t */");
			
	 out.println("\tpublic "+retType+" "+name+"(ve.usb.Claire.runtime.java.ClaireReader in) throws ve.usb.Claire.runtime.java.ParserException,ve.usb.Claire.runtime.java.LexerException, java.lang.Exception");
	 out.println("\t{");
	 out.println("\t\treturn "+ castToType("ve.usb.Claire.runtime.java.Algorithm.parse(this, in, "+ sym.getId()+")", retType)+";");
	 out.println("\t}");
	 out.println("\t/**");


	 out.println("\t *parses a source inputstream using the default scanner");
	 out.println("\t *@param in the source to be parsed");
	 out.println("\t *@return the value calculated for the start symbol");
	 out.println("\t */");
			
	 out.println("\tpublic "+retType+" "+name+"(java.io.Reader in) throws ve.usb.Claire.runtime.java.ParserException,ve.usb.Claire.runtime.java.LexerException, java.lang.Exception");
	 out.println("\t{");
	 out.println("\t\treturn "+ castToType("ve.usb.Claire.runtime.java.Algorithm.parse(this, new ve.usb.Claire.runtime.java.ClaireReader(in), "+ sym.getId()+")", retType)+";");
	 out.println("\t}");
      }
		
		
      /**
       * writes all the methods to start the parser
       */
      void emitStarters(PrintWriter out)
      {
	 Iterator initials=grammar.initialSymbols().iterator();

	 if (initials.hasNext())
	 {
	    Symbol sym = (Symbol)initials.next();
	    emitStart("parse",sym,out);

	    initials=unit.grammar().initialSymbols().iterator();
				
	    while (initials.hasNext())
	    {
	       sym=(Symbol)initials.next();
	       emitStart("parse" + sym.getId(),sym,out);
	    }
	 }
						
	 out.println("\t/**");
	 out.println("\t *parses a source inputstream using the default scanner");
	 out.println("\t *@param in the source to be parsed");
	 out.println("\t *@param state the state from where the parsing is to start");
	 out.println("\t *@return the value calculated for the start symbol");
	 out.println("\t */");
			
	 out.println("\tpublic ve.usb.Claire.runtime.java.Symbol parse(ve.usb.Claire.runtime.java.ClaireReader in,int state) throws ve.usb.Claire.runtime.java.ParserException,ve.usb.Claire.runtime.java.LexerException, java.lang.Exception");
	 out.println("\t{");
	 out.println("\t\treturn ve.usb.Claire.runtime.java.Algorithm.parse(this,in,state);");
	 out.println("\t}");

      }

      /**
       * gets the casting of a value to the type of this symbol
       * @param original the string that originaly contains a expression to be casted
       * @return the casting from the expression to the type of this symbol
       */
      private String castToType(String original, String type)
      {
	 // first get the symbol representation
	 original = "((ve.usb.Claire.runtime.java.Symbol)"+original+")";
			
	 if (type==null)
	    return original+".ObjectValue()";
	 else if (type.equals("boolean"))
	    return original+".booleanValue()";
	 else if (type.equals("byte"))
	    return original+".byteValue()";
	 else if (type.equals("char"))
	    return original+".charValue()";
	 else if (type.equals("double"))
	    return original+".doubleValue()";
	 else if (type.equals("float"))
	    return original+".floatValue()";
	 else if (type.equals("int"))
	    return original+".intValue()";
	 else if (type.equals("long"))
	    return original+".longValue()";
	 else if (type.equals("short"))
	    return original+".shortValue()";

	 else
	    return "(("+type+")"+original+".ObjectValue())";
      }


      public void translate(String key, PrintWriter out)
      {
	 if (key.equals("@package"))
	    out.print(javaParser.packages);
	 else if (key.equals("@imports"))
	    out.print(javaParser.imports);
	 else if (key.equals("@declaration"))
	    out.print(javaParser.declaration);
	 else if (key.equals("@usercode"))
	    out.print(javaParser.code);
	 else if (key.equals("@parsemethods"))
	    emitStarters(out);
	 else if (key.equals("@classname"))
	    out.print(javaParser.className);
	 else if (key.equals("@table"))
	    writeArray(table.pool().toByteArray(),out);
	 else if (key.equals("@check"))
	    writeArray(table.checkPool().toByteArray(),out);
	 else if (key.equals("@gziped"))
	    out.print(fullCompress);
	 else if (key.equals("@EOFIndex"))
	    out.print(table.EOFIndex());
	 else if (key.equals("@actionIndex"))
	    out.print(table.actionIndex());
	 else if (key.equals("@defaultReduceIndex"))
	    out.print(table.defaultReduceIndex());
	 else if (key.equals("@initialLexerIndex"))
	    out.print(table.initialLexerIndex());
	 else if (key.equals("@initialLexerCheck"))
	    out.print(table.initialLexerCheck());
	 else if (key.equals("@lexerIndex"))
	    out.print(table.lexerIndex());
	 else if (key.equals("@lexerCheck"))
	    out.print(table.lexerCheck());
	 else if (key.equals("@gotoIndex"))
	    out.print(table.gotoIndex());
	 else if (key.equals("@ruleLengthIndex"))
	    out.print(table.ruleLengthIndex());
	 else if (key.equals("@leftHandIndex"))
	    out.print(table.leftHandsIndex());
	 else if (key.equals("@actions"))
	    emitActions(out);
	 else
	    out.print(key);
	 
      }
      

      /**
       * casts an expresion to the internal representation of a type
       * @param original the expression whose type match the type of this object
       * @return the casting from this type to Object
       */
      private String castFromType(String original, String type)
      {
	 if (type==null)
	    return "(new ve.usb.Claire.runtime.java.ObjectSymbol("+original+"))";
	 else if (type.equals("boolean"))
	    return "(new ve.usb.Claire.runtime.java.booleanSymbol("+original+"))";
	 else if (type.equals("byte"))
	    return "(new ve.usb.Claire.runtime.java.byteSymbol("+original+"))";
	 else if (type.equals("char"))
	    return "(new ve.usb.Claire.runtime.java.charSymbol("+original+"))";
	 else if (type.equals("double"))
	    return "(new ve.usb.Claire.runtime.java.doubleSymbol("+original+"))";
	 else if (type.equals("float"))
	    return "(new ve.usb.Claire.runtime.java.floatSymbol("+original+"))";
	 else if (type.equals("int"))
	    return "(new ve.usb.Claire.runtime.java.intSymbol("+original+"))";
	 else if (type.equals("long"))
	    return "(new ve.usb.Claire.runtime.java.longSymbol("+original+"))";
	 else if (type.equals("short"))
	    return "(new ve.usb.Claire.runtime.java.shortSymbol("+original+"))";

	 else
	    return "(new ve.usb.Claire.runtime.java.ObjectSymbol("+original+"))";
      }

      /**
       * gets the default Value for a data type
       * @return the string representing the defaul value
       */
      private String defaultValue(String type)
      {
	 if (type==null)
	    return "null";
	 else if (type.equals("boolean"))
	    return "false";
	 else if (type.equals("byte"))
	    return "0";
	 else if (type.equals("char"))
	    return "'\\0'";
	 else if (type.equals("double"))
	    return "0";
	 else if (type.equals("float"))
	    return "0";
	 else if (type.equals("int"))
	    return "0";
	 else if (type.equals("long"))
	    return "0";
	 else if (type.equals("short"))
	    return "0";

	 else
	    return "null";
      }

      /**
       * the version string of the translator for Claire
       */
      private final static String versionStr= "Java Translator for Claire version $Revision: 1.3 $, $Date: 1999/11/21 02:12:40 $";

      /**
       * gets the help string that describes how to use the language
       * @return the string containing the help
       */
      public String help()
      {
	 try
	 {
				
	    StringWriter stringout = new StringWriter();
	    PrintWriter out = new PrintWriter(stringout);
				
	    out.println("Java translator for Claire");
	    out.println("Besides the default Claire options (see --help),");
	    out.println("the java translator recognizes the following options:");
	    out.println("");
	    out.println("-c <class> | --class <class>   sets the class name to be generated");
	    out.println("-z | --zip                     compresses the tables using gzip");
				
	    out.println("");
	    out.println(versionStr);
	    out.println("Copyright (C) 1999 Paul Pacheco <93-25642@ldc.usb.ve>");
	    out.println("Claire comes with ABSOLUTELY NO WARRANTY. This is free software, ");
	    out.println("and you are welcome to redistribute it under certain contitions;");
	    out.println("see LICENSE for details");
	    out.close();

	    return stringout.toString();
	 }
	 catch (Exception e)
	 {
	    return "";
	 }
      }

      /**
       * parses the command line for the translator
       */
      private void cmdLine(String args[]) throws Exception
      {

	 LongOpt[] longopts = {
	    new LongOpt("class", LongOpt.OPTIONAL_ARGUMENT, null, 'c'),
	    new LongOpt("zip", LongOpt.NO_ARGUMENT, null, 'z'),
	 };

	 Getopt opts = new Getopt("Claire", args, "-:c:z", longopts);
	 opts.setOpterr(false);

	 int option;

	 String arg;
			
	 while ((option = opts.getopt()) != -1)
	 {
	    switch (option)
	    {
	       case 'c': // class name
		  className = opts.getOptarg();
		  break;
	       case 'z': // zip
		  fullCompress = true;
		  break;

	       default:
		  throw new Exception("Invalid Command line");
	    }
	 }									
      }

      /**
       * gets a short description for the translator
       * @return the string containing the short description
       */
      public String shortHelp()
      {
	 return "translates Claire results to java";
      }

}