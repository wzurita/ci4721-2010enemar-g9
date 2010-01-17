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
 * $Id: CodeSymbol.java,v 1.2 1999/11/04 05:37:33 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: CodeSymbol.java,v $
 * Revision 1.2  1999/11/04 05:37:33  Paul
 * Improved expansion of alias inside Regular Expressions and Code
 *
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
import ve.usb.Claire.translator.*;

/**
 * Symbol is the most basic symbol of the gramar
 * @Implements java.io.Serializable
 * @version     $Revision: 1.2 $
 * @author      Maria Eugenia Ahues
 * @author      Bernardo Munoz
 * @author      Rui Santos
 * @since       JDK1.1
 * @see Terminal
 * @see NonTerminal
 * @see Variable
 */

public class CodeSymbol extends DeclarableSymbol
{

		/**
		 * the rule where this code was originally in
		 */
		private Rule parent=null;

		/**
		 * the string that contains the code
		 */
		private String code=null;
		
		/** 
		 * Creates a code symbol,
		 * @param name the name for the symbol
		 * @param code is the code for the symbol
		 * @param type is the return type for the code		 
		 */
		public CodeSymbol (String code,String type)
		{
			super(null,type);
			setName("%code_"+hashCode());
			
			this.code=code;
		}

		/**
		 * gets the substitution string when the code is passed as a parameter
		 * for a macro
		 * @return the substitution string
		 */
		public String getSubsString()
		{
			return getCode();
		}

		/**
		 * determines if this code is empty
		 * @return true if the code is only white spaces
		 */
		public boolean isEmpty()
		{
			return getCode().trim().equals("");
		}
		
		/**
		 * sets the parent for this code
		 * if the code has already a parent, then does nothing
		 * @param parent the rule that contins this code
		 */
		public void setParent(Rule parent)
		{
			if (this.parent==null)
			{
				this.parent=parent;
			}
		}

		public void setParent(Rule parent, int position)
		{
			if (this.parent==null)
			{
				this.parent=parent;
			}
		}

		/**
		 * takes an identifier for the code. if the code is an internal acction
		 * then it takes an identifier, otherwise, it doesn't
		 * @param currentId the current identifier that the code can take
		 * @return the next identifier for the next symbol
		 */
		public int setId(int currentId)
		{
			// if this action doesn't have a parent, is because it was in a
			// macro, and the macro was removed, therefore, we don't need
			// an identifier
			if (parent == null)
				return currentId;
			
			if (parent.find(this) >= 0)
				return super.setId(currentId);
			else
				return currentId;
		}

		/**
		 * determines if the symbol derives epsilon.
		 * Since the code generates an empty rule, the code
		 * allways derives epsilon
		 * @return true if the symbol derives an epsylon
		 * false otherwise
		 */
		public boolean deriveEpsilon()
		{
			return true;
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
		public int expand(Unit unit, Grammar grammar, Rule parent, List replacement)
		{
			
			setParent(parent);

			   int maximum=0;
			   
			   String oldCode;
			   do
			   {
			      oldCode = code;
			      code = instance(oldCode, grammar.getAlias(), unit);
			      maximum++;
			   } while (!code.equals(oldCode) && maximum < unit.expansion() );

			   if (maximum >= unit.expansion())
			   {
			      unit.error("Can not replace alias inside code");
			      unit.error("{" + code + "}");
			      unit.error("\tThis may mean that definitions like a = \"${b}\", b = \"${a}\"  have been");
			      unit.error("\tfound, or that you should increase the expansion limit. (see %expand)");
			   }

			   code = instance(code, grammar.getAlias(), unit);
			
			if (parent.size() - 1 == parent.find(this) && parent.action() == null)
			{
				parent.action(this);

				if (getType()==null)
					setType(parent.getLeftHand().getType());

				return -1;
			}
			
			replacement.add(this);

			if (expanded)
				return replacement.size()-1;


			Rule dummyProd=new Rule(this);

			dummyProd.action(this);

			grammar.add(dummyProd);

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
			return grammar.declare(this);
		}
		
		/**
		 * gets the code inside the symbol
		 */
		public String getCode()
		{
			return code;
		}


		/**
		 * dumps the content of the code to a given translator.
		 * Also translates values such as $x and #x using the translator
		 * @param trans the translator to be used
		 * @param unit the unit that contains the options
		 */
		public String translate(Translator trans, Unit unit)
		{
			int myIndex= parent.find(this);

			if (myIndex==-1)
				myIndex=parent.size();
			
			Map map= parent.getValues(myIndex,trans);

			StringBuffer result = new StringBuffer();
			
			for (int i=0;i<code.length();i++)
			{
				switch (code.charAt(i))
				{

					case '$':
						if (code.charAt(i+1)=='$')
						{
							result.append(trans.returnVar());
							i++;
							break;
						}						
					case '#':

						StringBuffer variable=new StringBuffer();
						// step right after the $
						int nexti=getVariable(code, i+1, variable,unit)-1;

						String replacement= (String)map.get(code.charAt(i)+variable.toString());

						if (replacement == null)
							result.append(trans.defaultValue());
						else
							result.append(replacement);
						
						i=nexti;
						break;
					case '\\':
						if (code.charAt(i+1) == '$' || code.charAt(i+1)== '#')
						{
							result.append(code.charAt(i+1));
							i++;
							break;
						}
						
					default:
						result.append(code.charAt(i));
				}
			}

			return result.toString();
		}

		/**
		 * return the instance for this symbol
		 * @param subs the substitutions to be made
		 * @param options the options for the grammar
		 * @return a list of symbols once the substitution has been make
		 */
		public List instance(Map subs,Unit unit)
		{
			String newCode=instance(getCode(),subs,unit);
			
  			List result=new ArrayList();

			Symbol newSym=new CodeSymbol(new String(newCode),instance(getType(),subs,unit));

			result.add(newSym);

			return result;
		}
}










