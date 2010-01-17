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
 * $Id: Translator.java,v 1.2 1999/11/06 01:44:26 Paul Exp $
 *
 * The changes to this file are:
 *
 * $Log: Translator.java,v $
 * Revision 1.2  1999/11/06 01:44:26  Paul
 * Improved the translator, now a template file is used to make it
 * easier to modify.
 *
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 * Revision 1.2  1999/09/09 09:53:55  Paul
 * Added some cvs comments
 *
 */

package ve.usb.Claire.translator;
import ve.usb.Claire.*;

/**
 * Emiter writes some code to the class
 * @version     $Revision: 1.2 $
 * @author      Paul Pacheco
 * @since       JDK1.2
 */

public interface Translator
{

		/**
		 * Translate the parser to the desired language.
		 * @param args the command line arguments for this translator
		 * @param unit the compiler unit to be translated
		 */
		public abstract void translate(String args[], Unit unit) throws Exception;

		/**
		 * return the substitution for $1, $2, $3, ... for the desired language.
		 * @param position is the position in the stack where the value is going to be
		 * @param type the type for the value
		 */		  
		public abstract String valueAt(int position, String type);

		/**
		 * returns the substitution for $$ for the desired language
		 * @return the string used for the $$ value
		 */
		public abstract String returnVar();

		/**
		 * returns the default value for when there is no substitution, for example
		 * the value of %left or the non existant value of a $1
		 * @return the string used as a default value
		 */
		public abstract String defaultValue();

		/**
		 * returns the data type for tokens. For example it could be String
		 * @return the data type for tokens (RE)
		 */
		public abstract String tokenType();
		
		/**
		 * returns the command to get the line start for a given symbol.
		 * @param position is the possition in the stack where the symbol is
		 *    going to be
		 */
		public abstract String lineStart(int position);

		/**
		 * returns the command to get the line end for a given symbol.
		 * @param position is the possition in the stack where the symbol is
		 *    going to be
		 */
		public abstract String lineEnd(int position);

		/**
		 * returns the command to get the column start for a given symbol.
		 * @param position is the possition in the stack where the symbol is
		 *    going to be
		 */
		public abstract String colStart(int position);

		/**
		 * returns the command to get the column end for a given symbol.
		 * @param position is the possition in the stack where the symbol is
		 *    going to be
		 */
		public abstract String colEnd(int position);

		/**
		 * returns the command to get the char start for a given symbol.
		 * @param position is the possition in the stack where the symbol is
		 *    going to be
		 */
		public abstract String charStart(int position);

		/**
		 * returns the command to get the char end for a given symbol.
		 * @param position is the possition in the stack where the symbol is
		 *    going to be
		 */
		public abstract String charEnd(int position);
				
		/**
		 * gets the help string that describes how to use the language
		 * @return the string containing the help
		 */
		public abstract String help();

		/**
		 * gets a short description for the translator
		 * @return the string containing the short description
		 */
		public abstract String shortHelp();
}
