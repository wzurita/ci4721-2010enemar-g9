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
 * $Id: ModifiedSymbol.java,v 1.1.1.1 1999/10/31 07:47:02 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: ModifiedSymbol.java,v $
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
import ve.usb.Claire.regexp.*;
import ve.usb.Claire.contextFree.*;

/**
 * Represents a symbol that is some kind of modification of other
 * symbol. That is the case of a casting 
 * @version     $Revision: 1.1.1.1 $
 * @author      Paul Pacheco
 * @since       JDK1.2
 */

public abstract class ModifiedSymbol extends Symbol
{

		/*
		 * Symbol whose properties are to be modified
		 */
		protected Symbol original=null;

		
		/** 
		 * Class constructor, creates a symbol with a value and a type
		 */
		public ModifiedSymbol (Symbol original)
		{
			this.original=original;
		}
		
		/**
		 * Gets the name of this Symbol, this is the name that uniquely
		 * identifies the symbol
		 * @return this value
		 */
		public String getName ()
		{
			return original.getName();
		}
		
		/**
		 * this is the string that is going to be used when someone is replacing
		 * this symbol in a macro
		 * @return the text used in substitution
		 */
		public String getSubsString()
		{
			return original.getSubsString();
		}

		/**
		 * determines if the symbol can or can't be a macro name
		 * @return true if the symbol can be a macro name
		 * false if not. by default, none symbol can
		 */
		public boolean canBeMacroName()
		{
			return original.canBeMacroName();
		}
		
		/**
		 * add a symbol to the list of firsts
		 * @param other the new symbol to be added to the list
		 * @see Rule
		 */
		public boolean addFirst(Symbol other)
		{
			return original.addFirst(other);
		}

		/**
		 * gets the firsts of this symbol
		 * @return the list of firsts for this symbol
		 */
		public Set firsts()
		{
			return original.firsts();
		}

		/**
		 * set the used flag for this symbol
		 * @param used true means that this symbol is used
		 * false otherwise
		 */
		public void used(boolean used)
		{
			original.used(used);
		}

		/**
		 * determines if this symbol is used
		 * @return true if this sysbol is used
		 * false if this symbol is not used
		 */
		public boolean used()
		{
			return original.used();
		}
		
		/**
		 * determines if this symbol finishes
		 * @return true if this symbol finishes
		 * false otherwise
		 */
		public boolean finish()
		{
			return original.finish();
		}

		/**
		 * set the finish flag for this symbol
		 * @param finish is true if this symbol finishes
		 * @see Rule
		 */
		public void finish(boolean finish)
		{
			original.finish(finish);
		}

		/**
		 * concatenates the first for this symbol
		 * @param other, the other symbol which contains the firsts to be added
		 * @return true if succesful, false if nothing where added
		 */
		public boolean concatFirst(Symbol other)
		{
			return original.concatFirst(other);
		}


		/**
		 * sets the flag to tell when the symbol derives epsilon
		 * @param derive true means the symbol derives epsilon
		 * false, otherwise
		 * @see Rule
		 */		
		public void deriveEpsilon(boolean derive)
		{
			original.deriveEpsilon(derive);
		}

		/**
		 *	determines if the symbol derives epsilon or not
		 * @return true means that this symbol derives epsilon
		 * false otherwise
		 */
		public boolean deriveEpsilon()
		{
			return original.deriveEpsilon();
		}

		
		/**
		 * determines if this symbol should be instanced with the substitution
		 * @param sustitution the symbol that is expanding
		 * @return true if this symbol should be instanced
		 * @see MacroDefSymbol
		 */
		public boolean shouldInstance(MacroSymbol sustitution)
		{
			return original.shouldInstance(sustitution);
		}
		
		/**
		 * Gets the type of this Symbol
		 * @return String this type
		 */
		public String getType()
		{
			return original.getType();
		}

		/**
		 * Sets the type for this symbol
		 * @param type the type for this symbol
		 */
		public void setType(String type)
		{
			original.setType(type);
		}

		/**
		 * sets the terminal flag for this symbol.
		 * @param isTerminal true means this symbol is terminal
		 * false means is non terminal
		 */
		public void setTerminal(boolean isTerminal)
		{
			original.setTerminal(isTerminal);
		}
		
		/**
		 * Gets the identifier of this Symbol
		 * @return int this identifier
		 */
		public int getId ()
		{
			return original.getId();
		}

		/**
		 * Sets the identifier for this symbol
		 * @param id the posible identifier for this symbol
		 * @return the next identifier to be used
		 */
		public int setId(int id)
		{
			return id;
		}
		
		
		/**
		 * return the identifier to be used in the grammar file
		 */
		public String getEmitedId()
		{
			return original.getEmitedId();
		}
		
		/**
		 * Test to see if this symbol is terminal
		 * @return boolen true is this symbols is of type terminal or false if not
		 */  
		public boolean isTerminal ()
		{
			return original.isTerminal();
		}

		/**
		 * Test to see if this symbol is non terminal
		 * @return boolen true is this symbols is of type Nonterminal or false if not
		 */  
		public boolean isNonTerminal ()
		{
			return !original.isTerminal();
		}
		

		public Symbol declaredSymbol()
		{
			return original.declaredSymbol();
		}
		
		/**
		 * Converts this object to String
		 * @return String this object to string
		 */  
		public String toString ()
		{
			return original.toString();
		}
  
		/**
		 * gives the list of parameters of the symbol
		 * this is usefull if the symbol is a macro
		 * @return the list of parameters
		 */
		public List params()
		{
			return original.params();
		}

		/**
		 * instances a macro if this is a macro production
		 * otherwise, does nothing
		 * @param expanded the symbol that is expanding
		 * @param unit the unit to where errors are displayed
		 */
		public Symbol instance(Symbol expanded, Unit unit)
		{
			return original.instance(expanded,unit);
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
			original = original.expandLeftHand(unit,grammar, parent);

			return this;
		}
		
		/**
		 * Calculates the hashCode for the symbol
		 */
		public int hashCode()
		{
			return original.hashCode();
		}

		
		/**
		 * determines if this symbol is a macro
		 * @return true if the original symbol is a macro
		 *         false otherwise
		 */
		public boolean isMacro()
		{
			return original.isMacro();
		}

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
		public int expand(Unit unit,Grammar grammar, Rule parent, List replacement)
		{
			
			int index= original.expand(unit, grammar, parent, replacement);

			if (index >= 0)
			{
				Symbol sym= (Symbol)replacement.get(index);
				original=sym;
				replacement.set(index,this);
			}
			return index;
		}

		/**
		 * gives the regular expression that this symbol holds
		 * @return the regular expression this symbol holds
		 *         null if non is hold
		 */
		public RE regexp()
		{
			return original.regexp();
		}
}
