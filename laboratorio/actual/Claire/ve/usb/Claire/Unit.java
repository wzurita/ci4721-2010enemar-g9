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
 * $Id: Unit.java,v 1.2 1999/11/07 11:44:17 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: Unit.java,v $
 * Revision 1.2  1999/11/07 11:44:17  Paul
 * changed the name of the translator from JAVA to java.
 *
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 * Revision 1.4  1999/10/09 01:58:04  Paul
 * Removed the --optimize option. Now the compression algorithm is fast enough
 * to run allways
 *
 * Revision 1.3  1999/09/09 12:20:23  Paul
 * Added some javadoc comments
 *
 * Revision 1.2  1999/09/09 09:53:55  Paul
 * Added some cvs comments
 *
 */
package ve.usb.Claire;

import java.util.*;
import java.io.*;
import ve.usb.Claire.contextFree.symbol.*;
import ve.usb.Claire.contextFree.*;
import ve.usb.Claire.translator.*;
import ve.usb.Claire.table.*;


/**
 * Represents a grammar and all the options for a compiler
 * a source file is completely represented with this class
 * @version     $Revision: 1.2 $
 * @author      Paul Pacheco
 * @since       JDK1.2
 * @see Grammar
 */

public class Unit implements Cloneable
{


		/**
		 * contains all the language dependant code added at the beggining
		 */		
		private String languageDependant="";


		/**
		 * gets the language dependant code
		 */
		public String languageDependant()
		{
			return languageDependant;
		}

		/**
		 * sets the language dependant code
		 */
		public void languageDependant(String languageDependant)
		{
			this.languageDependant=languageDependant;
		}
		
		/**
		 * expansion level for the macros.		 
		 */
		private int expansion=8;

		/**
		 * sets the level of expansion for the macros. The level of expansion
		 * simply limits the number of parenthesis in a macro symbol
		 * this is useful to avoid strange behavior when expanding macros
		 * like
		 * <code>
		 * f(x): f(f(x))
		 * </code>
		 * whitch can't be expanded
		 * @param level the new level
		 */
		public void expansion(int level)
		{
			this.expansion=level;
		}

		/**
		 * gets the level of expansion for the macros
		 * @return the level of expansion
		 */
		public int expansion()
		{
			return this.expansion;
		}
		
		
		/**
		 * true means to generate a main for the class, this way, no aditional
		 * code is necesary to test the parser or scanner.
		 */
		private boolean hasMain=true; // listo

		/**
		 * tells if the grammar has a main
		 * @param has thrue if should generate a main
		 */		 
		public void hasMain(boolean has)
		{
			this.hasMain=has;
		}

		/**
		 * determines if the generated class has a main
		 * @return true if the class should have a main
		 */
		public boolean hasMain()
		{
			return this.hasMain;
		}
		
			
		/**
		 * output directory where the class will be written
		 */
		private String outputDir=null;

		/**
		 * sets the output directory where the class is to be written
		 * @param dir the directory
		 */
		public void outputDir(String dir)
		{
			if (dir != null)
				if (!dir.endsWith(File.separator))
					dir=dir+File.separator;

			this.outputDir=dir;
		}

		/**
		 * gets the output directory where the class is to be written
		 * @return the output directory for the class
		 */
		public String outputDir()
		{
			return this.outputDir;
		}
		
		
		/**
		 * the name of the file where verbose information will be written		 
		 */
		private String verboseFile=null;


		/**
		 * sets the file to where verbose information will be written
		 */
		public void verboseFile(String verb)
		{
			this.verboseFile = verb;
		}
		
		/**
		 * gets the name of the file to where verbose output will be written
		 * @return the filename
		 */
		public String verboseFile()
		{
			return verboseFile;
		}
		

		/**
		 * tells if to dump a performance report
		 * useful only for developers
		 */
		private boolean perfReport=false;

		/**
		 * tells whether to dump performance report
		 */
		public void perfReport(boolean perfReport)
		{
			this.perfReport=perfReport;
		}
		
		/**
		 * tells when the process started, for performance report
		 */
		private long perfStart= System.currentTimeMillis();

		
		/**
		 * false means there were no errors
		 * true means there were some errors
		 */
		private boolean wereErrors=false;
		
		/**
		 * Presents a warning to the user.
		 * @param warning the message of the warning
		 * @param level the level of the warning
		 */
		public void warning(String warning, int level)
		{
			System.err.println(warning);
		}


		/**
		 * Dumps an error to the user
		 * @param error the error message to be dumped
		 */
		public void error(String error)
		{
			System.err.println(error);
			wereErrors=true;
		}
		
		/**
		 * the list of rules of the grammar
		 * @see Rules
		 * @see Rule
		 */
		private Grammar grammar=null;

		/**
		 * sets the grammar for this compiler unit
		 * @param rule the new rule to be added
		 */
		public void grammar(Grammar grammar)
		{
			this.grammar=grammar;
		}

		/**
		 * gets the grammar for this compiler unit
		 */
		public Grammar grammar()
		{
			return this.grammar;
		}

		/**
		 * Specifies the translator to be used
		 * By default, the translator is java
		 */
		private String language="java";

		/**
		 * gets the name of the translator to be used 
		 */
		public String language()
		{
			return this.language;
		}

		/**
		 * sets the translator to be used
		 * @param lang the name of the translator to be used
		 */
		public void language(String lang)
		{
			this.language=lang;
		}
		

		/**
		 * translate the compilation unit to a table
		 * @return the generated table
		 */
		public Table compile() throws Exception
		{
			
			reportProgress("Finished parsing the file, time:");

				
			Automata automata = new Automata (grammar,this);

			Table table = new Table(grammar, automata);

			reportProgress("Finished building tables, time:");
						
			if (wereErrors)
				throw new Exception();

			return table;
		}			
		
		/**
		 * The time for the last performance report
		 */
		private long perfLast = System.currentTimeMillis();
		
		/**
		 * Reports the finishing of a step
		 */
		public void reportProgress(String message)
		{
			if (perfReport)
			{
				long now=System.currentTimeMillis();

				System.out.println(message + " " + (now-perfStart)+" diff: "+(now-perfLast));
				perfLast=now;
			}
		}

		/**
		 * creates a copy of the compilation unit
		 * @return the clon for the compilation unit
		 */
		public Object clone()
		{
			Unit clon=new Unit();
			clon.expansion=expansion;
			clon.hasMain=hasMain;
			clon.verboseFile=verboseFile;
			clon.outputDir=outputDir;
			clon.perfReport=perfReport;
			clon.perfStart=perfStart;
			clon.languageDependant=languageDependant;
			clon.language=language;
			
			return clon;
		}
}
