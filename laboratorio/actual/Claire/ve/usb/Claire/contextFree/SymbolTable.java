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
 * $Id: SymbolTable.java,v 1.1.1.1 1999/10/31 07:47:02 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: SymbolTable.java,v $
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
package ve.usb.Claire.contextFree;

import java.util.*;
import java.io.*;
import ve.usb.Claire.contextFree.symbol.*;
import ve.usb.Claire.*;


/**
 * Encapsulates a symbol table. Symbols can be declared, used, etc
 * @version     $Revision: 1.1.1.1 $
 * @author Paul Pacheco
 * @see Symbol
 * @since       JDK1.1
 */

class SymbolTable implements Cloneable
{

		/**
		 * contains the list of symbols for this symbolTable
		 */		
		private Collection  table=new ArrayList();

		
		/**
		 * declares a symbol. If the symbol is already declared, does nothing
		 * @param symbol the simbol to be inserted
		 * @return the inserted symbol. If the symbol is already inserted, return the
		 * previously declared symbol
		 */		
		Symbol declare(Symbol symbol)
		{
			Symbol previows= Symbol.sameDecl(symbol,table);

			if (previows != null)
				return previows;
			
			table.add(symbol);
			return symbol;
		}

		/**
		 * gets a symbol already declared
		 * @param symbol the symbol to search for
		 * @return the symbol that has the same declaration
		 */
		Symbol declared(Symbol symbol)
		{
			return Symbol.sameDecl(symbol,table);
		}

		/**
		 * prints the list of symbols to a writer
		 * @param out the writer to where symbols are to be written
		 */
		void dumpSymbols(PrintWriter out)
		{
			Iterator iter= table.iterator();
			while (iter.hasNext())
			{
				Symbol sym= (Symbol)iter.next();

				if (sym.getId()!=0)
					out.println( sym +" = "+sym.getId());
			}
		}

		/**
		 * duplicates the symbol table
		 * @return the clone of the symbol table
		 */		
		public Object clone()
		{
			SymbolTable clon=new SymbolTable();
			clon.table.addAll(table);
			return clon;
		}
			
		/**
		 * converts the symbol table to a human readable form
		 * @return the string representing this symbol table
		 */
		public String toString()
		{
			return table.toString();
		}

		/**
		 * Compares the regular expressions by the order they apear in the grammar
		 */
		static class SymbolComparator implements Comparator
		{

				public int compare(Object oneObject, Object twoObject)
				{
					RESymbol one= (RESymbol)oneObject;
					RESymbol two= (RESymbol)twoObject;
					
					if (one.order() < two.order())
						return -1;
					if (one.order() > two.order())
						return 1;
					
					return 0;
				}
		}

		/**
		 * comparator used to set the identifier in the order the regular
		 * expressions appear
		 */
		static private SymbolComparator symbolComparator= new SymbolComparator();

		/**
		 * set the final ids for the symbols
		 * @param initialSymbols the initial symbols of the grammar
		 */
		void setIds(List firsts)
		{

			/* the Starting id is 1, -1 = EOF */
			int currentId=0;

			Iterator iter = firsts.iterator();

			int pos=0;

			Set remainding=new HashSet();
			
			remainding.addAll(table);


			Set regexps= new TreeSet(symbolComparator);

			
			while (iter.hasNext())
			{
				Symbol sym=(Symbol)iter.next();
				remainding.remove(sym);
				currentId=sym.setId(currentId);
				
			}

			
			// take all the RE symbols

			iter = remainding.iterator();
			while (iter.hasNext())
			{
				Symbol sym= (Symbol)iter.next();
				if (sym.regexp()!=null)
				{
					regexps.add(sym);
					iter.remove();
				}
			}

			iter= regexps.iterator();
			while (iter.hasNext())
			{

				Symbol sym= (Symbol)iter.next();
				currentId=sym.setId(currentId);
			}

			iter= remainding.iterator();
			
			while (iter.hasNext())
			{

				Symbol sym= (Symbol)iter.next();
				currentId=sym.setId(currentId);
			}
		}
}
