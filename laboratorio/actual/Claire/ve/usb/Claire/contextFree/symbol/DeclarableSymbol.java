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
 * $Id: DeclarableSymbol.java,v 1.1.1.1 1999/10/31 07:47:02 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: DeclarableSymbol.java,v $
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
 * Represents a symbol that can be declared, and has a value
 * @version     $Revision: 1.1.1.1 $
 * @author      Paul Pacheco
 * @since       JDK1.2
 */

public abstract class DeclarableSymbol extends Symbol
{
		/**
		 * Symbol id. will identify the symbol once written to the grammar tables
		 */
		private int id;

		/**
		 * name of the symbol
		 */
		private String value;

		/**
		 * determines if a symbol derives something
		 */
		private boolean finish = false;
		
		/**
		 * determines if a symbol derives epsilon
		 */
		private boolean deriveEpsilon = false;

		/**
		 * firsts of a symbol
		 */
		private Set firsts=new HashSet();
		
		/**
		 * type returned by the symbol
		 */
		private String type;


		/**
		 * determines if the symbol is used or not
		 */
		private boolean used =false;
		
		private boolean expanded=false;
		
		/** 
		 * Class constructor, creates a symbol with a value and a type
		 */
		public DeclarableSymbol (String value,String type)
		{
			this.value = value;
			this.type = type;
		}
		
		/**
		 * Gets the name of this Symbol, this is the name that uniquely
		 * identifies the symbol
		 * @return this value
		 */
		public String getName ()
		{
			return this.value;
		}

		protected void setName(String value)
		{
			this.value=value;
		}
		

		/**
		 * determines if the symbol can or can't be a macro name
		 * @return true if the symbol can be a macro name
		 * false if not. by default, none symbol can
		 */
		public boolean canBeMacroName()
		{
			return false;
		}
		
		
		/**
		 * add a symbol to the list of firsts
		 * @param other the new symbol to be added to the list
		 * @see Rule
		 */
		public boolean addFirst(Symbol other)
		{
			if (isTerminal())
				return false;

			return firsts.add(other);
		}

		/**
		 * gets the firsts of this symbol
		 * @return the list of firsts for this symbol
		 */
		public Set firsts()
		{
			if (isTerminal())
				return Collections.singleton(this);

			//return Collections.unmodifiableSet(firsts);
			return firsts;
		}

		/**
		 * set the used flag for this symbol
		 * @param used true means that this symbol is used
		 * false otherwise
		 */
		public void used(boolean used)
		{
			this.used=used;
		}

		/**
		 * determines if this symbol is used
		 * @return true if this sysbol is used
		 * false if this symbol is not used
		 */
		public boolean used()
		{
			return this.used;
		}
		
		/**
		 * determines if this symbol finishes
		 * @return true if this symbol finishes
		 * false otherwise
		 */
		public boolean finish()
		{
			return this.finish || isTerminal() || deriveEpsilon();
		}

		/**
		 * set the finish flag for this symbol
		 * @param finish is true if this symbol finishes
		 * @see Rule
		 */
		public void finish(boolean finish)
		{
			this.finish=finish;
		}

		/**
		 * concatenates the first for this symbol
		 * @param other, the other symbol which contains the firsts to be added
		 * @return true if succesful, false if nothing where added
		 */
		public boolean concatFirst(Symbol other)
		{
			return firsts.addAll(other.firsts());
		}					


		/**
		 * sets the flag to tell when the symbol derives epsilon
		 * @param derive true means the symbol derives epsilon
		 * false, otherwise
		 * @see Rule
		 */		
		public void deriveEpsilon(boolean derive)
		{
			this.deriveEpsilon=derive;
		}

		/**
		 *	determines if the symbol derives epsilon or not
		 * @return true means that this symbol derives epsilon
		 * false otherwise
		 */
		public boolean deriveEpsilon()
		{
			return this.deriveEpsilon && !isTerminal();
		}

				

		/**
		 * determines if this symbol should be instanced with the substitution
		 * @param sustitution the symbol that is expanding
		 * @return true if this symbol should be instanced
		 * @see MacroDefSymbol
		 */
		public boolean shouldInstance(MacroSymbol sustitution)
		{
			return false;
		}
		
		/**
		 * Gets the type of this Symbol
		 * @return String this type
		 */
		public String getType()
		{
			return this.type;
		}

		/**
		 * Sets the type for this symbol
		 * @param type the type for this symbol
		 */
		public void setType(String type)
		{
			this.type=type;
		}

		/**
		 * sets the terminal flag for this symbol.
		 * @param isTerminal true means this symbol is terminal
		 * false means is non terminal
		 */
		public void setTerminal(boolean isTerminal)
		{
		}
		
		/**
		 * Gets the identifier of this Symbol
		 * @return int this identifier
		 */
		public int getId ()
		{
			return this.id;
		}

		/**
		 * Sets the identifier for this symbol
		 * @param id the posible identifier for this symbol
		 * @return the next identifier to be used
		 */
		public int setId(int id)
		{
			this.id=id;
			return id+1;
		}

		/**
		 * writes the declaration for this symbol
		 * @param out the file to where the declaration is to be written
		 * @param options the options for the compiler
		 * @see NamedSymbol
		 */
		public void emitDecl(PrintWriter out,Unit unit)
		{
		}
				
		/**
		 * Test to see if this symbol is terminal
		 * @return boolen true is this symbols is of type terminal or false if not
		 */  
		public boolean isTerminal ()
		{
			return false;
		}

		
		/**
		 * gets the declared Symbol
		 */
		public Symbol declaredSymbol()
		{
			return this;
		}
		
		/**
		 * return the instance for this symbol
		 * @param subs the substitutions to be made
		 * @param options the options for the grammar
		 * @return a list of symbols once the substitution has been make
		 */
		
		public List instance(Map subs,Unit unit)
		{
			Symbol substitution= sameDecl(this,subs.keySet());
			
			if (substitution != null)
			{
				List result=new ArrayList();

				result.addAll((List)subs.get(substitution));
				
				return result;
			}
			else
			{				

				List JustMyself=new ArrayList();
				JustMyself.add(this);
				return JustMyself;
			}
		}

}
