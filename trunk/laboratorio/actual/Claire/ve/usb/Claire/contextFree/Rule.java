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
 * $Id: Rule.java,v 1.1.1.1 1999/10/31 07:47:02 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: Rule.java,v $
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
import ve.usb.Claire.translator.*;

/**
 * Rule represents a rule from the grammar
 * @version     $Revision: 1.1.1.1 $
 * @author      Maria Eugenia Ahues
 * @author      Bernardo Munoz
 * @author      Rui Santos
 * @author      Paul Pacheco
 * @since       JDK1.1
 * @see Item
 * @see Symbol
 * @see Rules
 */

public class Rule
{

      /**
       * left associativity for the rule
       */
      public final static int LEFT=-1;

      /**
       * not associativity
       */
      public final static int NONASOC=0;

      /**
       * right associativity for the rule
       */
      public final static int RIGHT=1;

      /**
       * indicates that this rule has no precedence
       */
      public final static int NOPREC=-1;

      /**
       * the left hand of the rule
       */
      private Symbol left=null;

      /**
       * the right hand of the rule
       */
      private List right=new ArrayList();
		
      private List rightNames= new ArrayList();
		
		
      /**
       * the acction to be carried out when the rule is reduced
       */
      private CodeSymbol action=null;

      /**
       * the identifier of the rule
       */
      private short id=0;

      /**
       * the associativity of the rule
       */
      private int asoc=NONASOC;

      /**
       * the precedence number for the rule
       */
      private int precedence=NOPREC;

      /**
       * determines if the rule is ever reduced
       */
      private boolean reduced=false;

      /**
       * creates an empty rule.
       * @param left the left hand of the rule
       */
      public Rule(Symbol left)
      {
	 this.left=left;
      }

      /**
       * creates an empty rule. warning, a left hand will have to be added
       */		
      public Rule()
      {
      }
		

      /**
       * tells if this rule is ever reduced,
       * @param reduced true means this rule is reduced
       */
      void reduced(boolean reduced)
      {
	 this.reduced=reduced;
      }

      /**
       * determines if this rule is ever reduced
       * @return true if this rule is reduced
       */
      boolean reduced()
      {
	 return this.reduced;
      }

      /**
       * sets the precedence level for this rule
       * @param prec the preccedence for this rule
       */
      public void precedence(int prec)
      {
	 this.precedence=prec;
      }

      /**
       * gets the precedence for this rule
       * @return the precedence for this rule
       */
      int precedence()
      {
	 return precedence;
      }
		
      /**
       * adds a right symbol
       * @param right the symbol to be added
       */
      public void add(Symbol right)
      {
	 this.right.add(right);
	 this.rightNames.add(new Integer(this.right.size()));
      }

      public void addNoVar(int pos, Symbol right)
      {
	 this.right.add(pos, right);
	 this.rightNames.add(pos,null);
      }
			
      public void set(int pos, Symbol sym)
      {
	 this.right.set(pos,sym);
      }
		
      /**
       * adds a right symbol at a given position
       * @param right the symbol to be added
       * @param pos the position where the symbol is to be added		 
       */
      public void add(int pos, Symbol right)
      {
	 this.right.add(pos,right);
	 this.rightNames.add(pos, new Integer(pos+1));

	 for (int i=pos+1;i< rightNames.size(); i++)
	 {
	    rightNames.set(i, new Integer(i+1));
	 }
      }

      /**
       * removes the last symbol of the rule
       * @return the symbol removed
       */
      public Symbol removeLast()
      {
	 rightNames.remove(right.size()-1);
	 return (Symbol)right.remove(right.size()-1);
      }
		
      /**
       * determines if the rule starts with the given symbol
       * @param sym the symbol that this rule is supossed to start with
       * @return true if the rule starts with ge symbol
       */
      public boolean startsWith(Symbol sym)
      {
	 return left.equals(sym);
      }
		
		
      /**
       * updates the firsts for the left symbol of this grammar
       * @return true if something was updated
       */
      boolean updateFirst()
      {
	 Symbol leftHand=getLeftHand();

	 if (leftHand.isMacro())
	    return false;
			
	 boolean cambio = false;

			
	 for (int i=0;i<size();i++)
	 {
	    Symbol next = get(i);

	    cambio = leftHand.concatFirst(next) || cambio;

	    if (!next.deriveEpsilon())
	    {
	       return cambio;
	    }
	 }

	 if (!leftHand.deriveEpsilon())
	 {
	    leftHand.deriveEpsilon(true);
	    return true;
	 }
			
	 return false;
      }

      /**
       * marks all the symbols that this rule uses as used
       */		 
      void markSymbolsAsUsed()
      {
	 for (int i=0;i<size();i++)
	 {
	    Symbol next=get(i);
	    next.used(true);
	 }
      }
		

      /**
       * update the finish flag for the left hand. if all the right hand symbols
       * finish, then the left hand finish
       * @return true if something was updated
       */
      boolean updateFinish()
      {
	 Symbol leftHand=getLeftHand();

	 if (leftHand.isMacro())
	    return false;
			
	 if (leftHand.finish())
	    return false;
			
	 if (finish())
	 {			  
	    leftHand.finish(true);
	    return true;
	 }
	 else
	    return false;
      }

      /**
       * determines if this rule derives anything
       * @return true if this rule derives anything
       */
      public boolean finish()
      {

	 for (int i=0;i<size();i++)
	 {
	    Symbol next = get(i);

	    if (!next.finish())
	       return false;
	 }

	 return true;
      }
		
      /**
       * Gets the asociativity of this rule
       * @return the associativity of this rule
       */
      public int getAsoc ()
      {
	 return this.asoc;
      }

      /**
       * sets the associativity for this rule
       * @param asoc the new associativity to be set
       */
      public void setAsoc(int asoc)
      {
	 this.asoc=asoc;
      }

      /**
       * Gets the Symbol at a given position, in the rule's right hand 
       * @param pos a position in the RightHand
       * @return Symbol the Symbol at the position requested
       */
      public Symbol get (int pos)
      {
	 if (pos >= right.size())
	    return null;
			
	 return (Symbol)right.get(pos);
      }

      /**
       * Gets the Symbol at a given position, in the rule's right hand 
       * @param pos a position in the RightHand
       * @return Symbol the Symbol at the position requested
       */
      public Symbol remove(int pos)
      {
	 if (pos >= right.size())
	    return null;
			
	 rightNames.remove(pos);
	 return (Symbol)right.remove(pos);
      }
		
      /**
       * expands this rule, to make it BNF
       * @param options the options for the compilation
       * @param instanced the symbols instanced in the grammar
       */
      void expand(Grammar grammar, Unit unit)
      {
	 left= left.expandLeftHand(unit, grammar, this);
			
	 List replacement= new ArrayList();
			
	 if (!right.isEmpty() && !getLeftHand().isMacro())
	 {
	    List oldRight= right;

	    right = new ArrayList();
				
	    ListIterator iter = oldRight.listIterator(oldRight.size());

	    while (iter.hasPrevious())
	    {
	       replacement.clear();

	       Symbol next = (Symbol)iter.previous();

	       int index = next.expand(unit,grammar, this, replacement);

	       right.addAll(0, replacement);

	       for (int i=0;i< replacement.size(); i++)
		  rightNames.add(0,null);

	       if (index >= 0)
		  rightNames.set(index, new Integer(iter.nextIndex()+1));
					
	    }
	 }
					
      }

      /**
       * gets the iterator through the right hand of the rule
       * @return the iterator for the right hand
       */
      public Iterator iterator()
      {
	 return right.iterator();
      }
		
      /**
       * finds a symbol in the right hand
       * @param sym the symbol to be searched for
       * @return the index of the symbol
       *         -1 if the symbol is not found
       */
      public int find(Symbol sym)
      {
	 Iterator iter= right.iterator();
	 int pos=0;
			
	 while (iter.hasNext())
	 {
	    if (sym.equals(iter.next()))
	       return pos;
	    pos++;
	 }
	 return -1;
      }
		
      /**
       * calculates the map that given a name like $x, tells wich
       * what the string substitution would be, using the suplied translator
       * @param pos the position of the code symbol inside the rule
       * @param trans the translator to be used
       */
      public Map getValues(int pos, Translator trans)
      {
	 Map result= new HashMap();

	 for (int i=0;i< pos; i++)
	 {

	    Symbol sym = (Symbol)right.get(i);

	    String returnType = sym.getType();

	    if (sym.isTerminal())
	       returnType=trans.tokenType();
				
	    String value =    trans.valueAt(pos-i, returnType);
	    String lineStart= trans.lineStart(pos-i);
	    String lineEnd=   trans.lineEnd(pos-i);
	    String charStart= trans.charStart(pos-i);
	    String charEnd=   trans.charEnd(pos-i);
	    String colStart=  trans.colStart(pos-i);
	    String colEnd=    trans.colEnd(pos-i);
				
	    result.put ("#"+(i+1), value);
	    result.put("#"+(i+1)+"_row", lineStart);
	    result.put("#"+(i+1)+"_rowStart", lineStart);
	    result.put("#"+(i+1)+"_rowEnd", lineEnd);
	    result.put("#"+(i+1)+"_line", lineStart);
	    result.put("#"+(i+1)+"_lineStart", lineStart);
	    result.put("#"+(i+1)+"_lineEnd", lineEnd);
	    result.put("#"+(i+1)+"_col", colStart);
	    result.put("#"+(i+1)+"_colStart", colStart);
	    result.put("#"+(i+1)+"_colEnd", colEnd);
	    result.put("#"+(i+1)+"_char", charStart);
	    result.put("#"+(i+1)+"_charStart", charStart);
	    result.put("#"+(i+1)+"_charEnd", charEnd);

	    Integer name= (Integer)rightNames.get(i);
				
	    if (name != null)
	    {
					
	       result.put("$"+name,value);
	       result.put("$"+name+"_row", lineStart);
	       result.put("$"+name+"_rowStart", lineStart);
	       result.put("$"+name+"_rowEnd", lineEnd);
	       result.put("$"+name+"_line", lineStart);
	       result.put("$"+name+"_lineStart", lineStart);
	       result.put("$"+name+"_lineEnd", lineEnd);
	       result.put("$"+name+"_col", colStart);
	       result.put("$"+name+"_colStart", colStart);
	       result.put("$"+name+"_colEnd", colEnd);
	       result.put("$"+name+"_char", charStart);
	       result.put("$"+name+"_charStart", charStart);
	       result.put("$"+name+"_charEnd", charEnd);
	    }
	 }
	 return result;
      }
		
      /**
       * Returns the rules left hand
       * @return the left hand of this rule
       */  
      public Symbol getLeftHand ()
      {
	 return this.left;
      }
  
      /**
       * Test to see if the right hand of this rule is epsilon
       * @return boolean True if this RightHand is epsilon, false if not
       */ 
      public boolean isEpsilon ()
      {
	 return (right.isEmpty());
      }


      /**
       * Gets the Rule's RightHand's length
       * @return int the RightHand's length
       */
      public int size()
      {
	 return right.size ();
      }
		
      /**
       * Gets the Rule's Id
       * @return the identifier for this rule
       */
      public short getId ()
      {
	 return this.id;
      }
  
      /**
       * Sets the Rule's left hand to left
       * @param left Left hand for the rule
       */		
      public void setLeftHand (Symbol left)
      {
	 this.left = left;
      }
		
      /**
       * Sets the Rule's identifier to id
       * @param id Identifier
       */		
      public void setId (short id)
      {
	 this.id = id;
      }
		
      /**
       * Converts this object to String
       * @return this object to string
       */  
      public String toString ()
      {
	 return toString(-1);
      }

      /**
       * Converts this object to a human readable form
       * allso, inserts a dot, in a given position
       * @param dotpos the position where a dot is to be inserted
       * @return the human readable form for this rule
       */
      public String toString(int dotpos)
      {
	 String s = left.toString ();
			
	 s = s  + " -> ";

	 Iterator iter = right.iterator();

	 while (iter.hasNext())
	 {
	    if (dotpos==0)
	       s = s + ". ";

	    dotpos--;
	    s = s + iter.next() + " ";
	 }

	 if (dotpos == 0)
	    s = s + ".";
			
	 return s;
      }

      /**
       * gives the action to be taken by the rule
       * @return the code of the rule
       */
      public CodeSymbol action()
      {
	 return this.action;
      }

      /**
       * sets the action of the rule
       * @param action the new code of the rule
       */
      public void action(CodeSymbol action)
      {
	 this.action=action;
      }
		
      /**
       * makes a copy of this rule
       * @return the clone for this rule
       */
      public Object clone()
      {
	 Rule clon=new Rule();
	 clon.left=left;
	 clon.right.addAll(right);
	 clon.rightNames.addAll(rightNames);
	 clon.action=action;
	 clon.id=id;
	 clon.asoc=asoc;
	 clon.precedence=precedence;
	 clon.reduced=reduced;
	 return clon;
      }

      /**
       * given a map of parameters, instances the rule
       * by instancing each one of the right hands
       * @param table the map that points from a parameter to a list of symbols
       * @param unit the unit that contains the options.
       */
      private void instance(Map table, Unit unit)
      {
	 List newRight= new ArrayList();
	 List newRightNames= new ArrayList();
			
			
	 for (int i=0;i<right.size();i++)
	 {
	    Symbol sym=(Symbol)right.get(i);				

	    List replacement=sym.instance(table,unit);
				
	    newRight.addAll(replacement);

	    for (int j=0;j< replacement.size()-1;j++)
	       newRightNames.add(null);

	    if (replacement.size() > 0)
	       newRightNames.add(new Integer(i+1));
	 }

	 right= newRight;
	 rightNames=newRightNames;
      }

      /**
       * creates an instance of this rule. (useful if this rule is a macro rule)
       * @param sustitution the sustitution to be carried out
       * @param options the options for the compiler
       */
      public void instantiate(MacroSymbol sustitution,Unit unit)
      {
	 if (getLeftHand().shouldInstance(sustitution))
	 {
	    Rule instanced=(Rule)clone();

	    Symbol macroDef=(Symbol)instanced.left;
				
	    instanced.left= macroDef.instance(sustitution, unit);
				
	    Map table=new HashMap();

	    for (int i=0;i<macroDef.params().size();i++)
	       table.put(macroDef.params().get(i),sustitution.params().get(i));
					
	    instanced.instance(table, unit);
				
	    sustitution.setType(macroDef.getType());

	    unit.grammar().add(instanced);
	 }
      }
		
}



