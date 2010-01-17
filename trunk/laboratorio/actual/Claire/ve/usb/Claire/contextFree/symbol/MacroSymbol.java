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
 * $Id: MacroSymbol.java,v 1.1.1.1 1999/10/31 07:47:02 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: MacroSymbol.java,v $
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 * Revision 1.4  1999/09/10 02:03:41  Paul
 * fixed a bug that caused hanging with recursive macros
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
 * This kind of symbol is a non terminal that is an instance of a macro 
 * @version     $Revision: 1.1.1.1 $
 * @author      Paul Pacheco
 * @since       JDK1.1
 * @see MacroDefSymbol
 * @see Rule
 */

public class MacroSymbol extends DeclarableSymbol
{
		/** 
		 * list of parameters that this macro instance receives
		 */
		private List params=new ArrayList();
		
		/**
		 * Creates an uninstanced macro symbol
		 * @param value the name of the macro symbol
		 * @param params the parameters for the macro
		 */
		public MacroSymbol (String value,List params)
		{
			super(value,null);
			this.params.addAll(params);
		}

		/**
		 * return the list of parameters for this macro symbol
		 */
		public List params()
		{
			return Collections.unmodifiableList(params);
		}

		/**
		 * return the name of the macro, without the parameters
		 */
		public String macroName()
		{
			return super.getName();
		}
		
		/**
		 * Gets the name for this symbol, it includes the parameters
		 * @return String the name for this symbol
		 */
		public String getName ()
		{
			String value=super.getName() + "(";

			for (int i=0;i<params.size();i++)
			{
				if (i > 0)
					value+=", ";
				
				List param=(List)params.get(i);

				for (int j=0;j<param.size();j++)
				{
					value+=((Symbol)param.get(j)).getName();
					value+=" ";
				}
			}

			return value+")";
		}
				
		private boolean expanded=false;
		
		
		/**
		 * expands the symbol to make the rule BNF.
		 * @param unit the unit to where errors and warnings are reported
		 * @param grammar the grammar to be used
		 * @param parent the rule that is expanding
		 * @param replacement receives the list of symbols that are going
		 *         to replace this symbol
		 * @return the index of the symbol that represents the value once expanded
		 *         -1 if the symbol does not have any value
		 */
		public int expand(Unit unit, Grammar grammar, Rule parent,List replacement)
		{
			int index= super.expand(unit,grammar, parent, replacement);

			MacroSymbol declared= (MacroSymbol)replacement.get(index);
			if (declared.expanded)
				return index;
			
			declared.expanded=true;
			
			int expansionLevel=0;
			int stringPos=-1;
			
			String name=declared.getName();
			
			while ((stringPos=name.indexOf('(',stringPos+1))!=-1)
				expansionLevel++;
			
			if (expansionLevel <= unit.expansion())
			{
				for (int i=0;i< grammar.size();i++)
				{
					Rule next=(Rule)grammar.get(i);
					next.instantiate(declared,unit);
				}
			}
			else
			{
				unit.error("error: Can't instantiate macro. Expansion limit reached.");
				unit.error("\tThis may mean that a production like f(x) -> f(f(x)) has been");
				unit.error("\tfound, or that you should increase the expansion limit. (see %expand)");
			}

			return index;
		}
		/**
		 * Test to see if this symbol is terminal
		 * @return true is this symbols is of type terminal or false if not
		 */  
		public boolean isTerminal ()
		{
			return false;
		}

		/**
		 * does a substitution if this symbol is in a rule been instanced
		 * @param subs the substitutions to be carried out
		 * @param options the options for the compiler
		 */
		public List instance(Map subs,Unit unit)
		{
			
			Symbol lastone=new IDSymbol(super.getName(),getType());

			Symbol parameter= Symbol.sameDecl(lastone,subs.keySet());
			
			List result=new ArrayList();
				
			if (parameter != null)
			{
				
				result.addAll((List)subs.get(parameter));

				if (result.isEmpty())
					return result;

				Symbol last=(Symbol)result.get(result.size()-1);

				if (!last.canBeMacroName())
				{
					unit.error("error: can't make macro substitution in "+getName()+" with "+last.getName());
				}
				else
				{
					lastone=last;
					result.remove(result.size()-1);
				}
			}						  			

			List newParams=new ArrayList(params.size());

			for (int i=0;i<params.size();i++)
			{
				List param=(List)params.get(i);
				List newParam=instance(param,subs,unit);
				newParams.add(newParam);
			}
				
			Symbol newSymbol=new MacroSymbol(lastone.getName(),newParams);
			
			result.add(newSymbol);

			return result;
		}	 
}
