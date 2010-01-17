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
 * $Id: Claire.java,v 1.2 1999/11/07 11:44:03 Paul Exp $
 *
 * Here are the changes that has been done
 *
 * $Log: Claire.java,v $
 * Revision 1.2  1999/11/07 11:44:03  Paul
 * changed the name of the translator from JAVA to java.
 * Also improved the package hieritachy, now the java translator
 * is completely under ve.usb.Claire.translator.java
 *
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 * Revision 1.7  1999/10/12 06:30:33  Paul
 * Added a shell script to simplify invoking the compiler
 * Modified a small grammar error
 *
 * Revision 1.6  1999/10/09 01:57:52  Paul
 * Removed the --optimize option. Now the compression algorithm is fast enough
 * to run allways
 * Also a small bug causing some grammars not to work was fixed
 *
 * Revision 1.5  1999/09/09 12:20:23  Paul
 * Added some javadoc comments
 *
 * Revision 1.4  1999/09/09 09:53:54  Paul
 * Added some cvs comments
 *
 * Revision 1.3  1999/09/09 09:11:36  Paul
 * Some more cvs commands (I am still lerning)
 *
 * Revision 1.2  1999/09/09 08:59:05  Paul
 * Added some cvs commands
 *
 */

import java.io.*;
import ve.usb.Claire.*;
import java.util.*;
import gnu.getopt.*;
import ve.usb.Claire.translator.Translator;

	
/**
 * Class where the compiler begins. Is responsible for
 * parsing the command line and display help.
 * @version     $Revision: 1.2 $
 * @author      Paul Pacheco
 * @since       JDK1.1
 */

public class Claire
{

      /**
       * the version string for this product
       */
      
      private final static String versionStr= "Claire version $Revision: 1.2 $, $Date: 1999/11/07 11:44:03 $";
      
      
      /**
       * prints a command line error and exits
       */
      private static void error()			
      {
	 System.err.println("Invalid command line.");
	 help();
	 System.exit(1);
      }					
      
      /**
       * prints some help information()
       */
      private static void help()
      {
	 System.out.println("Usage:");
	 System.out.println("\tClaire --help | -h             this screen");
	 System.out.println("\tClaire --help<lang> | -h<lang> help for a language");
	 System.out.println("\tClaire --version               the version number");
	 System.out.println("\tClaire --list                  lists the available translators");
	 System.out.println("\tClaire [options] <file>        compiles a file");
	 
	 System.out.println("");
	 System.out.println("<file> is the input file name, use \"-\" for standard input.");
	 System.out.println("[options] is zero or more of the following:");
	 System.out.println("");
					
	 System.out.println("-l <lang> | --language <lang>  specify the language <lang> to be used");
	 System.out.println("-e <num>  | --expand <num>     specify the macro expansion level");
	 System.out.println("-d <dir>  | --dir <dir>        specify the output directory");
	 System.out.println("-v <file> | --verbose <file>   write verbose information to file <file>");
	 System.out.println("-n | --nomain                  don't write a main for the output file");
	 System.out.println("-p | --perf                    prints some performance status");
	 
	 System.out.println("");
	 System.out.println(versionStr+", Copyright (C) 1999 Paul Pacheco <93-25642@ldc.usb.ve>");
	 System.out.println("Claire comes with ABSOLUTELY NO WARRANTY. This is free software, ");
	 System.out.println("and you are welcome to redistribute it under certain contitions;");
	 System.out.println("see LICENSE for details");
			
      }

      /**
       * gets the resource bundle of translators
       */
      private static ResourceBundle loadTranslators() throws Exception
      {
	 return new PropertyResourceBundle(ClassLoader.getSystemResourceAsStream("ve/usb/Claire/translator/translators.list"));

      }
      
      /**
       * load a translator by name
       * @param name the name of the translator
       * @return the translator to be used
       */
      private static Translator loadTranslator(String name, ResourceBundle translators) throws Exception
      {
	 return (Translator)Class.forName(translators.getString(name)).newInstance();
      }

      /**
       * makes a string a given length. If the string is shorter, it is filled
       * with white spaces. If the string is larger, is truncated
       * @param string the string to be fixed
       * @param size the new size of the string
       * @return the fixed string
       */
      private static String ensureSize(String string, int size)
      {
	 StringBuffer result = new StringBuffer();
	 result.append(string);
	
	 for (int i= string.length(); i< size; i++)
	 {
	    result.append (" ");
	 }
	
	 return result.toString();
      }
		
      /**
       * gives a list of all the translators available, as well as
       * a short help describing each one
       */
      private static void listTranslators(ResourceBundle translators)
      {
	 try
	 {
	    System.out.println("The available translators are:");
	    System.out.println("");

	    Enumeration enum = translators.getKeys();
	    while (enum.hasMoreElements())
	    {

	       String key = (String)enum.nextElement();
	       
	       Translator trans = loadTranslator(key, translators);
					
	       System.out.println(ensureSize(key,15) + " "+trans.shortHelp());
	    }

	    System.out.println("");
	    System.out.println("use Claire -h<leng> for more details on each translator");
	 }
	 catch (Exception e)
	 {
	    System.err.println("Error listing translators");
	    e.printStackTrace();
	    System.exit(1);
	 }
      }
		
      /**
       * Parses the command line
       * @param args the arguments to be used
       * @param unit the unit in wich options are to be filled
       * @return the command line for the translator
       */
      private static String[] cmdLine(String args[], Unit unit, ResourceBundle translators)
      {

	 LongOpt[] longopts = {
	    new LongOpt("help", LongOpt.OPTIONAL_ARGUMENT, null, 'h'),
	    new LongOpt("version", LongOpt.NO_ARGUMENT, null, 'V'),
	    new LongOpt("language", LongOpt.REQUIRED_ARGUMENT, null, 'l'),
	    new LongOpt("expand", LongOpt.REQUIRED_ARGUMENT, null, 'e'),
	    new LongOpt("dir", LongOpt.REQUIRED_ARGUMENT, null, 'd'),
	    new LongOpt("verbose", LongOpt.REQUIRED_ARGUMENT, null, 'v'),
	    new LongOpt("nomain", LongOpt.NO_ARGUMENT, null, 'n'),
	    new LongOpt("perf", LongOpt.NO_ARGUMENT, null, 'p'),
	    new LongOpt("list", LongOpt.NO_ARGUMENT, null, 'L')
	       };
						
	 Getopt opts = new Getopt("Claire", args, "-:h::l:e:d:v:nop", longopts);
	 opts.setOpterr(false); // We'll do our own error handling

	 int option;

	 List languageArgs = new ArrayList();
			
	 String arg;
			
	 while ((option = opts.getopt()) != -1)
	 {
	    switch (option)
	    {
	       case 'h': // help
		  arg= opts.getOptarg();
						
		  if (arg == null)
		     help();
		  else
		  {
		     try
		     {
			Translator trans= loadTranslator(arg,translators);
			System.out.print(trans.help());
		     }
		     catch (Exception e)
		     {
			unit.error("Can not find translator for language "+ arg);
			System.exit(1);
		     }
		  }

		  System.exit(0);
	       case 'L':
		  listTranslators(translators);
		  System.exit(0);						
	       case 'V': // version
		  System.out.println(versionStr);
		  System.exit(0);						
						
	       case 'l': // language
		  arg=opts.getOptarg();
		  unit.language(arg);
		  break;
	       case 'e': // expansion
		  arg= opts.getOptarg();
		  unit.expansion(Integer.parseInt(arg));
		  break;
	       case 'd': // output directory
		  arg= opts.getOptarg();
		  unit.outputDir(arg);
		  break;
	       case 'v': // verbose
		  arg = opts.getOptarg();
		  unit.verboseFile(arg);
		  break;
	       case 'n': // no main
		  unit.hasMain(false);
		  break;
	       case 'p':
		  unit.perfReport(true);
		  break;
	       case 1:
		  if (opts.getOptind() == args.length)
		  {
		     sourceFile = opts.getOptarg();
		     break;
		  }						
	       default:
		  languageArgs.add(args[opts.getOptind()-1]);
		  break;

	    }
	 }

			
	 if (sourceFile == null)
	 error();
			
	 try
	 {
				
	    if (unit.outputDir() == null && !sourceFile.equals("-"))
	       unit.outputDir((new File(sourceFile)).getParent());
	 }
	 catch (Exception e)
	 {
	    unit.error("File not found " + sourceFile);
	    System.exit(1);
	 }


	 args = new String[languageArgs.size()];

	 for (int i=0;i< args.length; i++)
	 args[i]= (String)languageArgs.get(i);
				
	 return args;
			
      }

      private static String sourceFile=null;
		
      /**
       * starts the compiler
       * @param args the command line for the compiler
       */
      public static void main (String args[])
      {
			
	 try
	 {
				//Ejecucion del algoritmo y las acciones
	    ve.usb.Claire.contextFree.syntax.Parser parser = new ve.usb.Claire.contextFree.syntax.Parser ();

	    ResourceBundle translators = loadTranslators();
	    
	    Unit unit = new Unit();
				
	    String langArgs[] = cmdLine(args, unit, translators);

	    Reader in;

	    if (sourceFile.equals("-"))
	       in = new InputStreamReader(System.in);
	    else
	       in = new FileReader(sourceFile);
								
	    parser.parse (in,unit);

	    Translator trans=null;
				
	    try
	    {
	       trans= loadTranslator(unit.language(), translators);
	    }
	    catch (Exception e)
	    {
	       unit.error("Can not find translator for language "+ unit.language());
	       System.exit(1);
	    }

	    trans.translate(langArgs, unit);
				
	 }
	 catch (Exception e)
	 {
	    if (e.getMessage() != null && !e.getMessage().equals(""))
	       System.err.println ("error:" + e.getMessage());

	    if (Boolean.getBoolean("Claire.debug"))
	       e.printStackTrace();
	    System.exit(1);
	 }
      }
}










