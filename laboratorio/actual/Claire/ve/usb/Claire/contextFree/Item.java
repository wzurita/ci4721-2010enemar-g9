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
 * $Id: Item.java,v 1.1.1.1 1999/10/31 07:47:02 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: Item.java,v $
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 * Revision 1.4  1999/10/09 00:15:08  Paul
 * Several speed improvments
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

import ve.usb.Claire.contextFree.*;
import ve.usb.Claire.contextFree.symbol.*;

/**
 * represents a item for the viable automate prefix. An item is the combination
 * of a rule, a dot withing a rule and a look ahead set
 * @version     $Revision: 1.1.1.1 $
 * @author      Maria Eugenia Ahues
 * @author      Bernardo Munoz
 * @author      Rui Santos
 * @author      Paul Pacheco
 * @since       JDK1.2
 * @see Items
 * @see Automata
 * @see Rule
 */
class Item implements Cloneable
{

		/**
		 * the original rule of the item
		 */
		private Rule origen;
		
		/**
		 * positin of the dot withing the rule
		 */
		private int dot;

		/**
		 * look ahead of the item
		 */
		private Set lah=new HashSet();
   		
		/**
		 * Class constructor with a Rule
		 */
		Item (Rule P)
		{
			this.origen=P;

			this.dot = 0;
		}

		/**
		 * Class constructor with a Rule and a Symbol
		 */
		Item (Rule P, Symbol s)
		{
			this.origen=P;

			this.dot = 0;
			lah.add(s);
		}
  
		/**
		 * gets the precedence of the item. The precedence is the same precedence
		 * of the rule
		 * @return the precedence
		 */
		int precedence()
		{
			return origen.precedence();
		}
		
		/**
		 * Creates a copy of the item.
		 * @return the clon of the item.
		 */
		public Object clone()
		{
			Item newStateProd=new Item(origen);
			newStateProd.dot=dot;			
			newStateProd.lah.addAll(lah);
			return newStateProd;
		}
		
		/**
		 * gets the rule for this item
		 */
		Rule getRule()
		{
			return origen;
		}
		
		/**
		 * Gets the asociativity of this item 
		 * @return the associativity of the item
		 */
		int getAsoc ()
		{
			return origen.getAsoc();
		}
  
		/**
		 * Test if the asociativity for this item is right
		 * @return boolean true if the asociativity is right, false if not
		 */
		boolean asocRight ()
		{
			return (getAsoc() == 1);
		}
  
		/**
		 * Test if the asociativity for this item is right
		 * @return boolean true if the asociativity is left, false if not
		 */
		boolean asocLeft ()
		{
			return (getAsoc() == -1);
		}
  
		/**
		 * gets the look ahead of the item
		 */
		Set getLah()
		{
			return lah;
		}
		
		/**
		 * Gets the left hand for this item. The left hand is the same left hand
		 * of a rule
		 * @return Left Hand of the item
		 */
		Symbol getLeftHand ()
		{
			return origen.getLeftHand();
		}
  
		/**
		 * Gets the identifier for this item
		 * @return int 
		 */
		short getId ()
		{
			return origen.getId();
		}

		/**
		 * adds a set of elements as look ahead
		 * @param set the set of symbols to be added to the look ahead
		 */
		private boolean addLah(Collection set)
		{
			return lah.addAll(set);

			
		}
		
		/**
		 * Calculates the look a head for this item
		 * @param p Item from which this item was expanded
		 */
		void calcLookAHead (Item p)
		{

			Symbol B = getLeftHand();

			for (int i=p.dot + 1;i< p.origen.size() ; i++)
			{
				Symbol next=p.origen.get(i);

				addLah(next.firsts());

				if (!next.deriveEpsilon())
					return;
			}

			addLah(p.getLah());
		}
		
		/**
		 * Appends the look a head from a Item
		 * @param sp Item from which the look a head is going to be appended
		 * @return void
		 */
		boolean appendLah (Item sp)
		{
			return addLah(sp.getLah());
		}
		
		/**
		 * Gets the Symbol after the dot in this Item
		 * @return Symbol
		 */
		Symbol getAfterDot ()
		{
			return origen.get(dot);
		}

		/**
		 * Gets the dot's position  in this Item
		 * @return int
		 */
		int getDot ()
		{
			return this.dot;
		}

		/**
		 * Test if the right hand of this Item is epsilon
		 * @return boolean true if the right hand is epsilon, false if not
		 */
		boolean isEpsilon ()
		{
			return (origen.isEpsilon ());
		}

		/**
		 * Moves to dot one position ahead of its actual position. If the dot is at the last place, doesn't move
		 * @return void
		 */
		void advance ()
		{
			if (dot < origen.size())
				dot++;
		}

		/**
		 * Test if the dot is at the last place in the Item
		 * @return boolean true if the dot is at the end , false if not
		 */
		boolean last ()
		{
			return (dot == origen.size());
		}
  
		/**
		 * Test to see if the left hand, right hand and the dot position of this Item are equal to sproduction
		 * @param sproduction Object 
		 * @return boolean true if the left hand, right hand and the dot position are equal, false if not
		 */  
		boolean compareProd (Item other)
		{
			return  origen.equals(other.origen) && dot == other.dot;
		}

		/**
		 * Test to see if the look a head of this Item is equal to the sproduction look a head
		 * @param sproduction Object 
		 * @return boolean true if the look a head are equals, false if not
		 */  
		boolean compareLah (Item other)
		{
			return lah.equals(other.lah);
		}

		/**
		 * Test to see if this Item is equal to sproduction
		 * @param sproduction Object Item to compare this object to
		 * @return boolean true if the two objects are equal, false if not
		 */  
		public boolean equals (Object sproduction)
		{
			return (this.compareProd ((Item)sproduction) &&
					  this.compareLah ((Item)sproduction));
		}
  
		/**
		 * Converts this object to String
		 * @return String this object to string
		 */  
		public String toString ()
		{
			String s = origen.toString(dot);
			s = s + "      id: " + getId() + "\n";
			return s;
		}

		/**
		 * gets a hashCode for the item
		 * @return a hashcode for the item
		 */
		public int hashCode()
		{
			return origen.hashCode()*(dot+1);
		}
}

