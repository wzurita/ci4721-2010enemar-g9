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
 * $Id: CastingSymbol.java,v 1.1.1.1 1999/10/31 07:47:02 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: CastingSymbol.java,v $
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
 * Implements a symbol that is a data casting of other symbol
 * @Implements java.io.Serializable
 * @version     $Revision: 1.1.1.1 $
 * @author      Paul Pacheco
 * @since       JDK1.2
 */

public class CastingSymbol extends ModifiedSymbol
{

		/**
		 * the data type of the symbol
		 */
		private String type=null;
		
		/** 
		 * Class constructor
		 */
		public CastingSymbol (Symbol original, String type)
		{
			super(original);
			setType(type);
		}

		
		/**
		 * return the instance for this symbol
		 * @param subs the substitutions to be made
		 * @param options the options for the grammar
		 * @return a list of symbols once the substitution has been make
		 */
		public List instance(Map subs,Unit unit)
		{
			List instanced=original.instance(subs,unit);

			if (!instanced.isEmpty())
			{
				Symbol first=(Symbol)instanced.get(0);
				CastingSymbol instancedCasting=new CastingSymbol(first,instance(getType(),subs,unit));

				instanced.set(0,instancedCasting);
			}

			return instanced;
		}

}










