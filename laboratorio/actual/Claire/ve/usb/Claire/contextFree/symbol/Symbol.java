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
 * $Id: Symbol.java,v 1.2 1999/11/04 05:37:33 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: Symbol.java,v $
 * Revision 1.2  1999/11/04 05:37:33  Paul
 * Improved expansion of alias inside Regular Expressions and Code
 *
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 * Revision 1.5  1999/09/12 12:57:18  Paul
 * replacement of named symbols now respect expansion limits
 *
 * Revision 1.4  1999/09/09 12:20:23  Paul
 * Added some javadoc comments
 *
 * Revision 1.3  1999/09/09 10:18:38  Paul
 * Removed some obsolete methods
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
 * Symbol is the most basic symbol of the gramar
 * @version     $Revision: 1.2 $
 * @author      Maria Eugenia Ahues
 * @author      Bernardo Munoz
 * @author      Rui Santos
 * @author      Paul Pacheco
 * @since       JDK1.1
 * @see NamedSymbol
 */

public abstract class Symbol implements Comparable
{
		
      /** 
       * Class constructor, creates a symbol with a value and a type
       */
      public Symbol ()
      {
      }
		
      /**
       * Gets the name of this Symbol, this is the name that uniquely
       * identifies the symbol
       * @return this value
       */
      public abstract String getName ();
		
      /**
       * this is the string that is going to be used when someone is replacing
       * this symbol in a macro
       * @return the text used in substitution
       */
      String getSubsString()
      {
	 return getName();
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
	 return false;
      }

      /**
       * gets the firsts of this symbol
       * @return the list of firsts for this symbol
       */
      public Set firsts()
      {
	 return null;
      }

      /**
       * set the used flag for this symbol
       * @param used true means that this symbol is used
       * false otherwise
       */
      public void used(boolean used)
      {
      }
		
      /**
       * determines if this symbol is used
       * @return true if this sysbol is used
       * false if this symbol is not used
       */
      public boolean used()
      {
	 return true;
      }
		
      /**
       * determines if this symbol finishes
       * @return true if this symbol finishes
       * false otherwise
       */
      public boolean finish()
      {
	 return true;
      }

      /**
       * set the finish flag for this symbol
       * @param finish is true if this symbol finishes
       * @see Rule
       */
      public void finish(boolean finish)
      {
      }

      /**
       * concatenates the first for this symbol
       * @param other, the other symbol which contains the firsts to be added
       * @return true if succesful, false if nothing where added
       */
      public boolean concatFirst(Symbol other)
      {
	 return false;
      }


      /**
       * sets the flag to tell when the symbol derives epsilon
       * @param derive true means the symbol derives epsilon
       * false, otherwise
       * @see Rule
       */		
      public void deriveEpsilon(boolean derive)
      {
      }

      /**
       *	determines if the symbol derives epsilon or not
       * @return true means that this symbol derives epsilon
       * false otherwise
       */
      public abstract boolean deriveEpsilon();

		
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
	 return null;
      }

      /**
       * Sets the type for this symbol
       * @param type the type for this symbol
       */
      public void setType(String type)
      {
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
	 return 0;
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
	 return getId()+"";
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
       * Test to see if this symbol is non terminal
       * @return boolen true is this symbols is of type Nonterminal or false if not
       */  
      public boolean isNonTerminal ()
      {
	 return !isTerminal();
      }

      /**
       * Test to see if this symbol is equal to other symbol
       * @param Object object sym to compare this object to
       * @return boolean true if the two objects are equal, false if not
       */  		
      public boolean equals (Object sym)
      {
	 return declaredSymbol() == ((Symbol)sym).declaredSymbol();
      }
		

      /**
       * gets the symbol that has a declaration
       * @param the used symbol that has a declaration
       */
      public Symbol declaredSymbol()
      {
	 return this;
      }
		
      /**
       * Converts this object to String
       * @return String this object to string
       */  
      public String toString ()
      {
	 return (getName() + " ");
      }
  		

      /**
       * determines if this symbol is a macro
       * @return false by default, indicating the symbol is not a macro
       */
      public boolean isMacro()
      {
	 return false;
      }
		
      /**
       * return the instance for this symbol
       * @param subs the substitutions to be made
       * @param options the options for the grammar
       * @return a list of symbols once the substitution has been make
       */
      public List instance(Map subs,Unit unit)
      {
	 List res = new ArrayList();
	 res.add(this);
	 return res;
      }

      /**
       * instances a macro if this is a macro production
       * otherwise, does nothing
       * @param expanded the symbol that is expanding
       * @param unit the unit to where errors are displayed
       */
      public Symbol instance(Symbol expanded, Unit unit)
      {
	 return expanded;
      }
		
      /**
       * instances a list of symbols and return the instanced list
       * @param list the list of symbols to be instanced
       * @param subs the substitutions to be make
       * @param options the options for this grammar
       */
      protected static List instance(List list, Map subs, Unit unit)
      {
	 List resultList=new ArrayList();

	 for (int i=0;i<list.size();i++)
	 {
	    Symbol sym=(Symbol)list.get(i);
	    List replacement=sym.instance(subs,unit);
	    resultList.addAll(replacement);
	 }

	 return resultList;
      }


      /**
       * searches through a string and gets the variable name for it.
       * removes the {} if necesary
       * @param string the original name
       * @param i the index at where the variable starts
       * @param name the variable that receives the calculated name
       * @param unit the unit to where errors and warnings are reported
       * @return the index at where the variable finishes
       */
      protected static int getVariable(String string, int i, StringBuffer name, Unit unit)
      {
	 boolean hasBrace=string.charAt(i) == '{';
	 if (hasBrace)
	    i++;

						
	 while (i< string.length() && Character.isJavaIdentifierPart(string.charAt(i)))
	 {
	    name.append(string.charAt(i));
	    i++;
	 }

	 if (hasBrace && i< string.length())
	 {
	    if (string.charAt(i)!= '}')
	       unit.error("error: malformed parameter call in "+string);
	    else
	       i++;
	 }
	 else if (hasBrace && i >= string.length())
	 {
	    unit.error("error: malformed parameter call in "+string);
	 }
				
	 return i;
			
      }
		
      /**
       * instances a string of code
       * @param string the string to be instanced
       * @param subs the substitutions to be make
       * @param options the options for the compiler
       */
      protected static String instance(String string, Map subs, Unit unit)
      {
	 if (string == null)
	    return null;

	 StringBuffer result=new StringBuffer();
			
	 for (int i=0;i<string.length();i++)
	 {

	    switch (string.charAt(i))
	    {
	       case '\\':
		  result.append(string.charAt(i));
		  i++;
		  result.append(string.charAt(i));
		  break;
	       case '$':
		  StringBuffer variable=new StringBuffer();
		  // step right after the $
		  int oldi=i;
						
		  i=getVariable(string, i+1, variable,unit)-1;

		  Iterator keys=subs.entrySet().iterator();
						
		  boolean replaced=false;
						
		  while (keys.hasNext())
		  {
		     Map.Entry entry= (Map.Entry)keys.next();

		     String key;
		     
		     if (entry.getKey() instanceof Symbol)
		     {
			Symbol orig=(Symbol)entry.getKey();
			key=orig.getName();
		     }
		     else
			key=(String)entry.getKey();
							
		     if (key.equals(variable.toString()))
		     {
			Object repl = entry.getValue();

			if (repl instanceof Collection)
			{
							      
			   Collection colrepl= ( Collection)repl;
			   Iterator iter = colrepl.iterator();
			   while (iter.hasNext())
			      result.append(((Symbol)iter.next()).getSubsString());

			   replaced=true;
			}
			else if (repl instanceof Symbol)
			{
			   Symbol symrepl = (Symbol) repl;
			   result.append(symrepl.getSubsString());
			   replaced=true;
			}
							   
			break;
		     }
		  }
						
		  if (!replaced)
		  {
		     i=oldi;
		     result.append(string.charAt(i));
		  }
		  break;
						
	       default:
		  result.append(string.charAt(i));
		  break;
	    }
	 }

	 return result.toString();
      }


      private static int substitutionLevel=0;
		
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
      public int expand(Unit unit, Grammar grammar, Rule parent, List replacement)
      {
	 Symbol named = grammar.getAlias(getName());
	 if (named != null)
	 {
	    if (substitutionLevel < unit.expansion())
	    {
	       substitutionLevel++;
	       int result= named.expand(unit,grammar, parent,replacement);
	       substitutionLevel--;

	       return result;
	    }
	    else
	    {
	       unit.error("error: Can't replace symbol " + this+". Expansion limit reached.");
	       unit.error("\tThis may mean that definitions like a = b, b = a  has been");
	       unit.error("\tfound, or that you should increase the expansion limit. (see %expand)");
	    }
	 }

	 Symbol result=grammar.declare(this);
/*			if (getType() != null)
			{
			if (result.getType()!=null && result != this && !result.getType().equals(getType()))
			unit.warning("Warning: diffrent data type for the symbol "+this, 1);

			result.setType(getType());
			}
*/
	 replacement.add(result);
	 return replacement.size()-1;
			
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
	 Symbol result= grammar.declare(this);
	 if (getType() != null)
	 {
	    if (result.getType()!=null && result != this && !result.getType().equals(getType()))
	       unit.warning("Warning: diffrent data type for the symbol "+this, 1);

	    result.setType(getType());
	 }

	 return result;
      }
		
      /**
       * determines if the symbol has the same declaration as other
       * @return true if the symbols have the same declaration
       *         false otherwise
       */
      public boolean sameDecl(Symbol other)
      {
	 return getName().equals(other.getName());
      }
		
      /**
       * given a list of symbols, determines wich symbol have the same declaration
       * as the one suplied
       * @param sym the symbol to whom a match is to be found
       * @param col the list of symbols that may match.
       * @return the symbol that has the same declaration
       *         null if non is found
       */
      public static Symbol sameDecl(Symbol sym, Collection col)
      {
	 Iterator iter= col.iterator();

	 while (iter.hasNext())
	 {
	    Symbol tentative=(Symbol)iter.next();

	    if (tentative.sameDecl(sym))
	       return tentative;
	 }
	 return null;
      }

      /**
       * gives the list of parameters of the symbol
       * this is usefull if the symbol is a macro
       * @return the list of parameters
       */
      public List params()
      {
	 return Collections.EMPTY_LIST;
      }

      /**
       * compares lexicographically the name for the symbols
       * usefull for sorting symbols.
       * @param other the other symbol to whitch this one is to be compared
       * @return -1 if this symbol is less than other
       *  0 if this symbol is equal to the other
       *  1 if this symbol is more than the other
       */
      public int compareTo(Object other)
      {
	 String myStr=getName();
	 String otherStr=((Symbol)other).getName();

	 return myStr.compareTo(otherStr);
      }				

      /**
       * gives the regular expression that this symbol holds
       * @return the regular expression this symbol holds
       *         null if non is hold
       */
      public RE regexp()
      {
	 return null;
      }
}
