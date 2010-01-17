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
 * $Id: ClaireScanner.java,v 1.1 1999/11/07 11:53:25 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: ClaireScanner.java,v $
 * Revision 1.1  1999/11/07 11:53:25  Paul
 * Copied the java runtime classes inside this package
 *
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 * Revision 1.2  1999/09/09 09:53:55  Paul
 * Added some cvs comments
 *
 */
package ve.usb.Claire.runtime.java;
import java.io.*;

/**
 * A scanner specially designed to be used with Claire. Besides the fact
 * that a ClaireScanner is a Scanner, it also contains methods to be called
 * by the lexical runtime algorithm.
 * @version     $Revision: 1.1 $
 * @author      Paul Pacheco
 * @since       JDK1.1
 */
public interface ClaireScanner extends Scanner
{

		/**
		 * gets the reduced symbol for a lexical state.
		 * @param sintacticState the sintactic state who contains the lexer
		 * @param lexicalState the lexical state within the lexer
		 * @return the token reduced by the state
		 *         < 0 if there is no transition
		 */
		public int yyReduced(int sintacticState, int lexicalState);

		/**
		 * gets the transition from a lexical state to another lexical state
		 * @param sintacticState the sintactic state who contains the lexer
		 * @param lexicalState the lexical state within the lexer
		 * @param nextChar the next character recognized
		 * @return the state to where the transition is to be made.
		 *         < 0 if there is no transition
		 */
		public int yyTransition(int sintacticState, int lexicalState, char nextChar);
}
