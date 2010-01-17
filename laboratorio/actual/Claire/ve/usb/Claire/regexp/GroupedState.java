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
 * $Id: GroupedState.java,v 1.1.1.1 1999/10/31 07:47:02 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: GroupedState.java,v $
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 * Revision 1.2  1999/09/09 09:53:55  Paul
 * Added some cvs comments
 *
 */
package ve.usb.Claire.regexp;
import java.util.*;

/**
 * Represents a normal lexical State, that has been generated
 * from the combination of other states. This is the case of
 * finding the DFA from a FA, or when findin the minimal DFA.
 * @version     $Revision: 1.1.1.1 $
 * @author      Paul Pacheco
 * @since       JDK1.2
 */

class GroupedState extends State
{

		/**
		 * set of the original states this state was originated
		 * from
		 */
		private Set grouped=new HashSet();

		/**
		 * Create a state that is the group combination of other
		 * states.
		 * @param states the collection of states this state is
		 *        originated from
		 */
		GroupedState(Collection states)
		{
			grouped.addAll(states);

			Iterator iter=grouped.iterator();
			while (iter.hasNext())
			{
				State next = (State)iter.next();
				
				merge(next);

				if (next.getOwner()==null)
					;
				else if (getOwner() == null)
					setOwner(next.getOwner());
				else if (next.getOwner().getId() < getOwner().getId())
					setOwner(next.getOwner());
			}
			
		}

		/**
		 * creates a grouped state with empty list of generated
		 * states
		 */
		GroupedState()
		{
		}

		/**
		 * adds a state to the list of states that generated this.
		 * @param other the new child of this state
		 */
		void add(State other)
		{

			if (other.getOwner()==null)
				;
			else if (getOwner() == null)
				setOwner(other.getOwner());
			else if (other.getOwner().getId() < getOwner().getId())
				setOwner(other.getOwner());
			
			grouped.add(other);
			merge(other);
		}
		
		/**
		 * gets the set of states that originated this
		 * @return the set of childs
		 */
		Set grouped()
		{
			return grouped;
		}

		/**
		 * Calculate the transitions acording to the set of childs
		 * wich might be in other states creating the DFA.
		 * if the transited states dont exist, they are created.
		 * @param original the original regular expresion this group
		 *        is created from
		 * @param grouped the map that given a state in the original
		 *        RE, maps to the group it belongs to
		 * @param added the groups already created
		 */
		void expand(RE original, Map grouped, Collection added)
		{
			ListIterator iter = childs.listIterator();
			while (iter.hasNext())
			{
				Object child = iter.next();
				Collection clossure;
				
				if (child == null)
					clossure = null;
				else if (child instanceof Collection)
					clossure = original.epsilonClossure((Collection)child);
				else
					clossure = original.epsilonClossure(Collections.singleton(child));

				if (!clossure.isEmpty())
				{
					GroupedState previows=(GroupedState)grouped.get(clossure);

					if (previows==null)
					{
						previows=new GroupedState(clossure);
						grouped.put(clossure, previows);
						added.add(previows);
					}
					
					iter.set(previows);
				}
			}
		}

		/**
		 * Calculates the transitions according to the childs.
		 * This is used in creating the minimal DFA
		 * @param grouped the map that tells wich states belong
		 *        to wich group.
		 */
		void expand(Map grouped)
		{
			ListIterator iter = childs.listIterator();
			while (iter.hasNext())
			{
				Object child = iter.next();
				if (child == null)
					;
				else if (child instanceof Collection)
				{
					child = ((Collection)child).iterator().next();
					iter.set(grouped.get(child));
				}
				else
					iter.set(grouped.get(child));
			}			
		}

		/**
		 * tells if this group contains any of the given states
		 * @param set the set of states that want to be tested
		 * @return true if this group intersects the set
		 *         false otherwise
		 */
		boolean containsAny(Set set)
		{
			Iterator iter=set.iterator();
			while (iter.hasNext())
			{
				if (grouped.contains(iter.next()))
					return true;
			}
			return false;

		}

		/**
		 * scans the group to find out if the given state belongs
		 * to it
		 * @param state the state that wants to be searched for
		 * @return true if the state belongs to the group
		 *         false otherwise
		 */
		boolean contains(State state)
		{
			return grouped.contains(state);
		}
}
