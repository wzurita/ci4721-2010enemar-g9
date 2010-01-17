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
 * $Id: WhiteSymbol.java,v 1.1.1.1 1999/10/31 07:47:02 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: WhiteSymbol.java,v $
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
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
 * represents a white symbol
 * @version     $Revision: 1.1.1.1 $
 * @author      Paul Pacheco
 * @since       JDK1.1
 * @see Symbol
 * @see StringSymbol
 * @see CharSymbol
 */

public class WhiteSymbol extends DeclarableSymbol
{

		/** 
		 * Creates a named symbol.
		 * @param value is the name for the symbol
		 * @param type is the type for the symbol
		 */
		public WhiteSymbol (int id)
		{
			super("%white_"+id,null);
		}

		public int expand(Unit unit, Grammar grammar, Rule parent, List replacement)
		{
			Symbol whiteSymbol= grammar.declared(this);

			if (whiteSymbol == null)
				return -1;

			replacement.add(whiteSymbol);
			return replacement.size()-1;
		}

		/**
		 * determines that this symbol can be used as a macro name, when expanding
		 * another macro
		 * @return true indicating that this symbol can be used as a macro
		 */
		public boolean canBeMacroName()
		{
			return false;
		}

}
