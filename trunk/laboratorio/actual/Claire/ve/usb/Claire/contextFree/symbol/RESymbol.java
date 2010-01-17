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
 * $Id: RESymbol.java,v 1.3 1999/11/07 11:44:50 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: RESymbol.java,v $
 * Revision 1.3  1999/11/07 11:44:50  Paul
 * Moved the syntax of regular expressions to regexp package
 *
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
import ve.usb.Claire.regexp.*;
import ve.usb.Claire.*;
import ve.usb.Claire.contextFree.*;
import ve.usb.Claire.regexp.syntax.*;

/**
 * Symbol is the most basic symbol of the gramar
 * @version     $Revision: 1.3 $
 * @author      Maria Eugenia Ahues
 * @author      Bernardo Munoz
 * @author      Rui Santos
 * @author      Paul Pacheco
 * @since       JDK1.1
 * @see CharSymbol
 */

public class RESymbol extends DeclarableSymbol
{

		/**
		 * Parser to be used to interpret the regular expressions
		 */
		private static Parser parser= new Parser();

		/**
		 * regular expression for the symbol
		 */
		private RE regexp=null;

		/**
		 * order of the symbol. Used to prioritize the regular expressions
		 */
		private int order=0;

		
		/** 
		 * Class constructor, creates a string symbol
		 * @param id is the name of the string
		 * @param type is the type of the symbol		 
		 */
		
		public RESymbol (String id,int order)
		{			
			super(id,"");
			this.order=order;
		}

		/**
		 * order of the symbol. Used to prioritize the regular expressions
		 * @return the order
		 */
		public int order()
		{
			return this.order;
		}
		
		/**
		 * sets the Identifier of the symbol.
		 * @param id the new identifier
		 * @return the next identifier
		 */
		public int setId(int id)
		{
			regexp.setId(id);
			return super.setId(id);
		}
			
		/**
		 * Gets the name of this Symbol, this is the name that uniquely
		 * identifies the symbol
		 * @return this value
		 */
		public String getName ()
		{
			return "\"" + super.getName()+"\"";
		}

      /**
       * this is the string that is going to be used when someone is replacing
       * this symbol in a macro
       * @return the text used in substitution
       */
      String getSubsString()
      {
	 return super.getName();
      }

		/**
		 * Test to see if this symbol is terminal
		 * @return boolen true is this symbols is of type terminal or false if not
		 */  
		public boolean isTerminal()
		{
			return true;
		}
		
		/**
		 * returns a instance for the symbol within a macro expansion
		 * @param subs the substitutions to be carried out
		 * @param options the options for the grammar
		 */
		public List instance(Map subs,Unit unit)
		{
			if (subs.containsKey(this))
			{
				List result=new ArrayList();
				result.addAll((List)subs.get(this));
				return result;
			}

			List result=new ArrayList();

			String substituted=instance(getName(),subs,unit);

			RESymbol newSymbol= new RESymbol(substituted,order);
			
			result.add(newSymbol);

			return result;			
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
		public int expand(Unit unit, Grammar grammar, Rule parent, List replacement)
		{
			
			try
			{
			   
			   String name = super.getName();

			   int maximum=0;
			   
			   String oldName;
			   do
			   {
			      oldName = name;
			      name = instance(oldName, grammar.getAlias(), unit);
			      maximum++;
			   } while (!name.equals(oldName) && maximum < unit.expansion() );

			   if (maximum >= unit.expansion())
			   {
			      unit.error("Can not replace alias inside regular expresion \"" +name + "\"");
			      unit.error("\tThis may mean that definitions like a = \"${b}\", b = \"${a}\"  have been");
			      unit.error("\tfound, or that you should increase the expansion limit. (see %expand)");
			   }
			   
			   
			   
				//name = name.substring(1,name.length()-1);
			   
				//System.out.println(name);
			   regexp= parser.parse(new StringReader(name)).minimal();
				//System.out.println(regexp);
				
			   if (regexp.acceptEpsilon())
			      unit.warning("warning: the regular expression "+getName()+" accepts zero length words",2);
				
			}
			catch (Exception e)
			{
				//e.printStackTrace();
				unit.error("error: malformed regular expression "+this);
				regexp= new RE();
			}

			
			return super.expand(unit, grammar,parent, replacement);
		}

		public boolean sameDecl(Symbol other)
		{
			return other == this;
		}

		public RE regexp()
		{
			return this.regexp;
		}
		
}
