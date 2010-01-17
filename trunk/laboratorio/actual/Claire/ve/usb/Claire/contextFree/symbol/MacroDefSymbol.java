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
 * $Id: MacroDefSymbol.java,v 1.1.1.1 1999/10/31 07:47:02 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: MacroDefSymbol.java,v $
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
 * This is a macro definition left hand
 * @version     $Revision: 1.1.1.1 $
 * @author      Paul Pacheco
 * @since       JDK1.1
 * @see MacroSymbol
 */

public class MacroDefSymbol extends DeclarableSymbol
{
		
		/** 
		 * Parameters that this macro receives
		 */
		private List params=new ArrayList();
		
		/**
		 * Creates a macro left hand
		 * @param value the name for the macro
		 * @param params the parameter that the macro receives
		 */
		public MacroDefSymbol (String value,List params)
		{
			super(value,null);
			this.params.addAll(params);
		}

		/**
		 * Creates a macro left hand from a macro symbol
		 * @param original the macro symbol from where this is been created
		 */
		public MacroDefSymbol(MacroSymbol original,String type, Unit unit)
		{
			super(original.macroName(),type);

			List originalParams=original.params();

			Iterator iter =originalParams.iterator();
			
			while (iter.hasNext())
			{
				List argument=(List)iter.next();
				
				if (argument.size()!=1)
					unit.error("error: Invalid parameter count in " + original.getName());
				else
				{
					Symbol sym= (Symbol)argument.get(0);
					if (! (sym instanceof IDSymbol))
						unit.error("error: Invalid macro parameter for macro" + original.getName());
					else
						params.add((Symbol)argument.get(0));
				}
			}
		}
					

		/**
		 * expands the left hand to make the rule BNF.
		 * @param unit the unit to where errors and warnings are reported
		 * @param grammar the grammar to be used
		 * @param parent the rule that is expanding
		 * @return the symbol that is going to be the definitive left hand
		 */
		public Symbol expandLeftHand(Unit unit, Grammar grammar, Rule parent)
		{
			return this;
		}

		/**
		 * gets the list of parameters for this macro
		 * @return the list of parameters
		 */
		public List params()
		{
			return Collections.unmodifiableList(params);
		}
		
		/**
		 * Gets the name for this macro with the parameters
		 * @return the name for this macro
		 */
		public String getName ()
		{
			String result= super.getName()+"[ ";

			for (int i=0;i<params.size();i++)
				result+=((Symbol)params.get(i)).getName()+" ";

			return result+"]";
		}

		/**
		 * tells if the macro is used
		 * since after the macro is instanciated, it won't be necesary no long,
		 * always return false
		 * @return true if the symbol is used
		 */
		public boolean used()
		{
			return false;
		}
		
		/**
		 * determines if the symbol derives anything
		 * since the macro is going to be eliminated after instanced
		 * it wont derive nothing, therefore, always return false
		 * @return true if the symbol derives something
		 */
		 
		public boolean finish()
		{
			return false;
		}
		
		/**
		 * sets the identifier for the symbol
		 * since is going to be removed, de symbol does'nt have an identifier
		 * and this does nothing
		 * @return the identifier for the next symbol
		 */
		public int setId(int id)
		{
			return id;
		}
		
		/**
		 * determines if the macro should be instanced given a macro symbol
		 * that is desired to be instanced
		 */
		public boolean shouldInstance(MacroSymbol sustitution)
		{
			return sustitution.params().size() == params.size() &&
				sustitution.macroName().equals(super.getName());
		}

		/**
		 * determines if this symbol is a macro
		 * @return true by default, indicating the symbol is a macro
		 */
		public boolean isMacro()
		{
			return true;
		}
		
}
