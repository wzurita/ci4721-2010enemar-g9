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
 * $Id: PrecSymbol.java,v 1.1.1.1 1999/10/31 07:47:02 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: PrecSymbol.java,v $
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 * Revision 1.3  1999/09/09 12:20:23  Paul
 * Added some javadoc comments
 *
 * Revision 1.2  1999/09/09 09:53:55  Paul
 * Added some cvs comments
 *
 */
package ve.usb.Claire.contextFree.symbol;
import java.io.*;
import java.util.*;
import ve.usb.Claire.*;
import ve.usb.Claire.contextFree.*;

/**
 * Symbol is the most basic symbol of the gramar
 * @version     $Revision: 1.1.1.1 $
 * @author      Maria Eugenia Ahues
 * @author      Bernardo Munoz
 * @author      Rui Santos
 * @author      Paul Pacheco
 * @since       JDK1.1
 * @see NamedSymbol
 */

public class PrecSymbol extends Symbol
{

		private int prec;

		/** 
		 * Class constructor, creates a symbol with a value and a type
		 */
		public PrecSymbol (int prec)
		{
			this.prec=prec;
		}
		
		/**
		 * Gets the name of this Symbol, this is the name that uniquely
		 * identifies the symbol
		 * @return this value
		 */
		public String getName ()
		{
			return ""+prec;
		}
						

		/**
		 *	determines if the symbol derives epsilon or not
		 * @return true means that this symbol derives epsilon
		 * false otherwise
		 */
		public boolean deriveEpsilon()
		{
			return true;
		}


		/**
		 * expands the symbol to make the rule BNF. In this case, just
		 * sets the precedence of the rule, and then removes itself
		 * from the rule by living the list unchanged and returning -1
		 * @param unit the unit to where errors and warnings are reported
		 * @param grammar the grammar to be used
		 * @param parent the rule that is expanding
		 * @param replacement receives the list of symbols that are going
		 *         to replace this symbol
		 * @return the index of the symbol that represents the value once expanded
		 *         -1 if the symbol does not have any value
		 */
		public int expand(Unit unit, Grammar grammar, Rule parent, List replacement)
		{
			parent.precedence(prec);
			return -1;
		}
		
		/**
		 * Test to see if this symbol is equal to other symbol
		 * @param Object object sym to compare this object to
		 * @return boolean true if the two objects are equal, false if not
		 */  		
		public boolean equals (Object sym)
		{
			if (!(sym instanceof PrecSymbol))
				return false;

			return ((PrecSymbol)sym).prec == prec;
		}

}
