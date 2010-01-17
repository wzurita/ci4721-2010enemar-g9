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
 * $Id: Parser.java,v 1.1 1999/11/07 11:53:25 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: Parser.java,v $
 * Revision 1.1  1999/11/07 11:53:25  Paul
 * Copied the java runtime classes inside this package
 *
 * Revision 1.2  1999/11/01 09:47:11  Paul
 * Starting to add exception suport
 *
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 * Revision 1.2  1999/09/09 09:53:55  Paul
 * Added some cvs comments
 *
 */
package ve.usb.Claire.runtime.java;
import java.util.*;
import java.io.*;

/**
 * Represents a Parser designed to be used with Claire.
 * A parser contains all the necesary tables as well as the semantic
 * actions.
 * @version     $Revision: 1.1 $
 * @author      Paul Pacheco
 * @since       JDK1.1
 * @see NamedSymbol
 */

public interface Parser
{

		/* Predefined tokens */
		public static final int EOF=-1;
		
		
		/**
		 * given a production, return its length
		 * @param production, the production from where we want the length
		 */
		public int yyLength(int production);

		/**
		 * given a production, returns its leftHand
		 * @param production, the production from where we want the leftHand
		 */
		public int yyLeftHand(int production);

		
		/**
		 * given a state, and a symbol, determines what to do
		 * @param state, the current state of the Automata
		 * @param symbol, the symbol in the input, or a reduced one
		 */
		public int yyGetAction(int state,int symbol);

		/**
		 * given a state, and a non Terminal, determines were to go
		 * @param state, the current state of the Automata
		 * @param symbol, the symbol in the input, or a reduced one
		 */
		public int yyGetGoto(int state,int symbol);
		
		/**
		 * given a state, determines if a reduce is possible without knowing
		 * the terminal
		 * @param state the lexical state where the automata is
		 * @return the number of the state where we are going to
		 *     0 if no default reduce is available
		 */

		public int yyDefaultReduce(int state);		
		
		/**
		 * does the sintactic action suplied by the user
		 * @param yyProduction the production that has been reduced
		 * @param yyValueStack the stack that contains the semantic values
		 * @return the symbol containing the semantic value for the reduced symbol
		 */
		public Symbol yyDoAction(int yyProduction,Stack yyValueStack) throws Exception;
		
}
