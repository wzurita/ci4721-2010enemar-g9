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
 * $Id: PredefSymbol.java,v 1.1.1.1 1999/10/31 07:47:02 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: PredefSymbol.java,v $
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
import ve.usb.Claire.*;
import ve.usb.Claire.contextFree.*;

/**
 * this is a special kind of symbol, that is predefined in the system
 * @version     $Revision: 1.1.1.1 $
 * @author      Paul Pacheco
 * @since       JDK1.1
 * @see Symbol
 */

public class PredefSymbol extends DeclarableSymbol
{
		
		/** 
		 * Creates a predefined symbol
		 * @param id the identifier for the symbol
		 * @param value the name for the symbol
		 * @param type the return type for the symbol
		 */
		public PredefSymbol (int id,String value,String type)
		{
			super(value,type);

			super.setId(id);
		}

		/**
		 * Sets the id for the symbol. Since the predefined symbols already
		 * have son identifier, this does nothing
		 * @param id the posible identifier for the symbol
		 */
		public int setId(int id)
		{
			return id;
		}
		
		/**
		 * gets the name for the symbol once used in the file
		 * @param options the options for the grammar
		 */
		public String getEmitedId()
		{
			return getName();
		}

		/**
		 * determines if the symbol is terminal or not.
		 * @return true indicating this simbol is terminal
		 */
		public boolean isTerminal()
		{
			return true;
		}
}










