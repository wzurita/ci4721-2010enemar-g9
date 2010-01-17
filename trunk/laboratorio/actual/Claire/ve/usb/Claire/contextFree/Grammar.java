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
 * $Id: Grammar.java,v 1.2 1999/11/04 05:37:33 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: Grammar.java,v $
 * Revision 1.2  1999/11/04 05:37:33  Paul
 * Improved expansion of alias inside Regular Expressions and Code
 *
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 * Revision 1.3  1999/09/09 10:32:48  Paul
 * Removed some obsolete functions
 *
 * Revision 1.2  1999/09/09 09:53:55  Paul
 * Added some cvs comments
 *
 */
package ve.usb.Claire.contextFree;
import java.util.*;
import java.io.*;

import ve.usb.Claire.*;
import ve.usb.Claire.contextFree.symbol.*;

/**
 * Encapsulates a lista of rules
 * @version     $Revision: 1.2 $
 * @author      Paul Pacheco
 * @since       JDK1.1
 * @see Rule
 */

public class Grammar implements Cloneable
{		

      /**
       * the actual list of rules
       */
      private List list=new ArrayList();

      /**
       * give names to regular expresions
       */
      private Map namedSymbols= new HashMap();

      /**
       * the rules indexed by their leftHand
       */
      private Map broken=new HashMap();
		
      /**
       * return the enumeration of rules
       */
      public Iterator iterator()
      {
	 return list.iterator();
      }

      /**
       * names a symbol, typically a regular expresion
       */
      public void alias(String name, Symbol named)
      {
	 namedSymbols.put(name, named);
      }

      /**
       * gets a symbol previowsly named
       */
      public Symbol getAlias(String name)
      {
	 return (Symbol)namedSymbols.get(name);
      }

      /**
       * gets the map for aliases
       * @return the map of aliases
       */
      public Map getAlias()
      {
	 return Collections.unmodifiableMap(namedSymbols);
      }
      

      /**
       * calculates the firsts for all the rules
       */
      private void calcFirsts()
      {
	 boolean cambio = true;
			
	 while (cambio)
	 {
	    cambio = false;

	    for (int i=0;i<list.size(); i++)
	    {
	       Rule p = (Rule)list.get(i);
	       cambio = p.updateFirst() || cambio;
	    }
	 }
      }

      /**
       * determines what rules don't derive anything
       */
      private void calcFinish(Unit unit)
      {
	 boolean cambio = true;
			
	 while (cambio)
	 {
	    cambio = false;

	    for (int i=0;i<list.size(); i++)
	    {
	       Rule p = (Rule)list.get(i);
	       cambio = p.updateFinish() || cambio;
	    }
	 }

	 boolean first=true;
			

	 Iterator iter=list.iterator();
			
	 short id=0;
			
	 while (iter.hasNext())
	 {
	    Rule p=(Rule)iter.next();

	    if (p.finish())
	    {
					
	       Symbol leftHand=p.getLeftHand();

	       if (broken.containsKey(leftHand))
	       {
		  ((Collection)broken.get(leftHand)).add(p);
	       }
	       else
	       {
		  Collection newColl;

		  newColl=new HashSet();
							
		  newColl.add(p);
		  broken.put(leftHand,newColl);
	       }
					
	       p.setId(id++);
					
	    }
	    else
	    {
	       if (first)
		  unit.warning("Warning: The folowing rules don't derive anything", 1);

	       first=false;
	       unit.warning(p.toString(), 1);
					
	       iter.remove();
	    }
	 }

      }
		
      /**
       * gets the list of rules that start with the given symbol
       * @param sym the symbol that should start some rules
       * @return the list of rules
       */
      public Collection getRulesWith(Symbol sym)
      {

			
	 Collection result=(Collection)broken.get(sym);

	 if (result==null)
	    return Collections.EMPTY_LIST;
	 else
	    return result;
			
			
      }

      /**
       * removes all the macro Rules, should be called after expanding
       * the rules
       */		
      private void removeMacros()
      {
	 Iterator iter = list.iterator();

	 while (iter.hasNext())
	 {
	    Rule p=(Rule)iter.next();
				
	    if (p.getLeftHand() instanceof MacroDefSymbol)
	       iter.remove();
	 }
      }

		
      /**
       * contains all the symbols in the grammar
       * @see SymbolTable
       */
      private SymbolTable symTable=new SymbolTable();
		
      /**
       * declares a symbol, or gets a declared symnbol
       * @param symbol the symbol to be declared
       * @return the declared symbol
       */
      public Symbol declare(Symbol symbol)
      {
	 return symTable.declare(symbol);
      }

      public Symbol declared(Symbol symbol)
      {
	 return symTable.declared(symbol);
      }		

		
      public void setSymbolsId()			
      {
	 symTable.setIds(initialSymbols());			
      }

      /**
       * initial symbol of the grammar,
       * if null, will use the first production
       */
      private List initialSymbols=new ArrayList();

      /**
       * adds an initial symbol for the grammar
       */
      public void initialSymbol(Symbol initialSymbol)
      {
	 if (!initialSymbols.contains(initialSymbol))
	    this.initialSymbols.add(initialSymbol);
      }

      /**
       * gets the initial symbols in the grammar
       */
      public List initialSymbols()
      {
	 return Collections.unmodifiableList(initialSymbols);
      }

      private List  whites= new ArrayList();
		
      public void white(Symbol sym)
      {
	 whites.add(sym);
      }		
		
      /**
       * expand the rules, instance the macro rules, and work with
       * the code symbols. it make the rules in BNF format
       * @param options the options for the compilation
       */
      public void expand(Unit unit)
      {
	 //System.out.println(list);
			
	 ListIterator iter = initialSymbols.listIterator();
	 while (iter.hasNext())
	 {
	    Symbol sym= (Symbol)iter.next();
	    iter.set(declare(sym));
	 }

	 iter= whites.listIterator();
	 while (iter.hasNext())
	 {
	    Symbol sym= (Symbol)iter.next();				
	    iter.set(declare(sym));
	 }

			
	 for (int i=0;i<list.size();i++)
	 {
	    Rule p=(Rule)list.get(i);
	    p.expand(this,unit);
	 }
			
	 removeMacros();
	 calcFirsts();
	 calcFinish(unit);
      }

      void dumpSymbols(PrintWriter out)
      {
	 symTable.dumpSymbols(out);
      }

      /**
       * adds a rule to the list
       * @param rule the rule to be added
       */
      public void add(Rule rule)
      {
	 list.add(rule);
      }

      /**
       * gets the count of rules in this list
       * @return the size of the list
       */
      public int size()
      {
	 return list.size();
      }

      /**
       * inserts a rule at a given position
       * @param rule the rule to be inserted
       * @param position the position where the rule is to be inserted
       */
      public void add(int position, Rule rule)
      {
	 list.add(position,rule);
      }
		
      /**
       * writes the length table
       * for each rule, it dumps the size
       * @param the file to where the table is to be written
       */
      public void emitLength(PrintWriter out)
      {
	 Iterator iter =list.iterator();
			
	 while (iter.hasNext())
	 {

	    Rule rule=(Rule)iter.next();

	    out.print(rule.size());

	    if (iter.hasNext())
	       out.print(", ");
				
	 }
			
      }

      /**
       * writes the left hand table
       * for each rule, it dumps the left hand
       * @param out the file to where the table is to be written
       * @param options the options for the compilation
       */
      public void emitLeftHand(PrintWriter out, Unit unit)
      {
	 Iterator iter=list.iterator();

	 while (iter.hasNext())
	 {

	    Rule rule=(Rule)iter.next();

	    out.print(rule.getLeftHand().getEmitedId());

	    if (iter.hasNext())
	       out.print(", ");
					
	 }
      }
		
      /**
       * gets a rule at a given position
       * @param index the index where the rule is
       * @return the rule in that position
       */
      public Rule get(int index)
      {
	 return (Rule)list.get(index);
      }

      /**
       * removes a rule at a given position
       * @param the index where the rule is
       */
      public void remove(int index)
      {
	 list.remove(index);
      }
		
		
      /**
       * creates a copy of this list
       * @return the clone of this list
       */
      public Object clone()
      {
	 Grammar clon=new Grammar();
	 clon.list.addAll(list);
	 return clon;
			
      }

      /**
       * transform the list in a human readable form
       * @return the string of the translated list
       */
      public String toString()
      {
	 return list.toString();
      }
}

