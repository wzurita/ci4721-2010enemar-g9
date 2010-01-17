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
 * $Id: RE.java,v 1.2 1999/11/21 02:12:39 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: RE.java,v $
 * Revision 1.2  1999/11/21 02:12:39  Paul
 * Dramatically reduced the size of the generated tables (suggested by Ascander Suarez)
 *
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 * Revision 1.2  1999/09/09 09:53:55  Paul
 * Added some cvs comments
 *
 */

package ve.usb.Claire.regexp;
import java.util.*;
import ve.usb.Claire.util.*;

/**
 * Represents a regular expresion.
 * @version     $Revision: 1.2 $
 * @author      Paul Pacheco
 * @since       JDK1.2
 */

public class RE
{

		/**
		 * contains the list of states in the automata
		 */
		protected List states=new ArrayList();

		/**
		 * determines what epsilon clossures are there in the
		 * automata
		 */
		protected Map epsilons = new HashMap();
		
		/**
		 * determines wich is the starting state
		 */
		protected State start=null;

		/**
		 * determines the set of ending states
		 */
		protected Set end= new HashSet();

		/**
		 * Creates regular expression,
		 * @param empty true means not to add any states to the RE
		 *        false otherwise
		 */
		private RE(boolean empty)
		{
			if (!empty)
				addStart();
		}
							
		/** 
		 * Creates a regular expression that does not recognize
		 * anything
		 */
		public RE ()
		{
			this(false);
		}

		/**
		 * tells wether this RE accepts the empty string
		 * the RE must be a DFA automata
		 * @return true if the RE accepts the empty string
		 *         false otherwise		 
		 */
		public boolean acceptEpsilon()
		{
			return end.contains(start);
		}
		
		/**
		 * the identifier for this RE
		 */
		private int id;
		
		/**
		 * sets the identifier for this RE
		 * @param id the new identifier
		 */
		public void setId(int id)
		{
			this.id=id;
		}

		/**
		 * get the identifier for this RE
		 * @return the identifier
		 */
		public int getId()
		{
			return this.id;
		}
		
		/**
		 * Marks the child states so they will know
		 * who their original RE is
		 */
		protected void markStates()
		{
			Iterator iter = states.iterator();
			while (iter.hasNext())
			{
				State next=(State)iter.next();
				next.setOwner(null);
			}
			
			iter = end.iterator();
			while (iter.hasNext())
			{
				State next= (State)iter.next();
				next.setOwner(this);
			}
		}

		/**
		 * determines the starting state
		 * @return the state the automata starts with
		 */
		private State start()
		{
			return start;
		}

		/**
		 * adds the given states to the automata without
		 * transitions
		 * @param states the set of states to be added
		 */
		private void addState(Set states)
		{
			Iterator iter=states.iterator();
			while (iter.hasNext())
				addState((State)iter.next());
		}

		/**
		 * adds the given state to the automata
		 * without transitions
		 * @param newone the new state to be inserted
		 */
		private void addState(State newone)
		{
			newone.setId(states.size());

			states.add(newone);

			Set epsilonClossure= new HashSet();			
			epsilonClossure.add(newone);
			epsilons.put(newone,epsilonClossure);
		}
		
		/**
		 * adds a state that will also be the new start
		 * @return the newlly created state
		 */
		protected State addStart()
		{
			State newStart=addState();
			start=newStart;
			return newStart;
		}
		
		/**
		 * adds a state that will also be a new end
		 * @return the newlly created state
		 */
		private State  addEnd()
		{			
			State added=addState();
			end.add(added);
			return added;
		}

		/**
		 * creates and adds a state
		 * @return the newlly created state
		 */
		private State addState()
		{
			State newone=new State();
			addState(newone);
			return newone;
		}
		
		/**
		 * adds a transition from a state to other state
		 * @param start the character where the transition starts from
		 * @param end the character where the transition ends with
		 * @param from the state from where the transition goes
		 * @param to the state to where the transition goes
		 */
		protected void add(char start, char end, State from, State to)
		{
			from.add(start,end,to);
		}

		/**
		 * adds a transition from a state to other state
		 * @param ch the character with wich the transition is made
		 * @param from the state from where the transition goes
		 * @param to the state to where the transition goes
		 */
		protected void add(char ch, State from, State to)
		{
			from.add(ch,to);
		}

		/**
		 * adds an epsilon transition from a state to another
		 * @param from the state from where the transition starts
		 * @param to the state to where the transition ends
		 */
		protected void add(State from, State to)
		{
			Set dest= (Set)epsilons.get(from);
			dest.add(to);
		}
			
		/**
		 * calculates a regular expresion that recognizes
		 * any character
		 * @return the calculater regular expression
		 */
		public static RE dot()
		{
			return ch(Character.MIN_VALUE,Character.MAX_VALUE);
		}

		/**
		 * calculates a regular expression that recognizes
		 * only one character
		 * @param ch the character to be recognized by the
		 * regular expression
		 * @return the calculated regular expression
		 */
		 
		public static RE ch(char chr)
		{
			return ch(chr,chr);
		}

		/**
		 * calculates a regular expression that recognizes
		 * only one character in a range
		 * @param start the character from where the range starts
		 * @param end the character to where the range ends
		 * @return the calculated regular expression
		 */
		public static RE ch(char start, char end)
		{
			RE result = new RE();

			State from= result.start();			
			State to=result.addEnd();			

			result.add(start,end, from,to);

			return result;
		}

		/**
		 * calculates a regular expression that recognizes
		 * only the empty string
		 * @return the calculated regular expression
		 */
		public static RE epsilon()
		{
			RE result = new RE();

			State from= result.start;			
			State to=result.addEnd();			

			result.add(from,to);

			return result;
		}
							  
		/**
		 * adds all the states from other regular expression
		 * however, the RE recognizes the same.
		 * @param other the other regular expression
		 * @param otherend the list to where the new ending states
		 *        will be added
		 * @return the new starting state
		 */
		protected State addAll(RE other, Set otherend)
		{			
			Map map= new HashMap();

			Iterator iter=other.states.iterator();
			while (iter.hasNext())
				map.put(iter.next(),addState());

			
			iter=other.epsilons.entrySet().iterator();
			while (iter.hasNext())
			{
				Map.Entry entry=(Map.Entry)iter.next();
				State from = (State)entry.getKey();
				Set to = (Set)entry.getValue();

				from = (State) map.get(from) ;
				if (from != null)
				{
					epsilons.put(from,State.map(map,to));
				}

				
			}
					
			otherend.addAll(State.map(map,other.end));
			
			iter=other.states.iterator();
			while (iter.hasNext())
			{
				State state=(State)iter.next();
				state.cloneTo(map);
			}

			return (State)map.get(other.start());
		}
			
		/**
		 * makes this regular expression also recognize
		 * what other regular expression recognizes
		 * this is similar to addAll, but this modifies
		 * what is recognized
		 * @param other the other regular expression
		 * @param end the set that receives the new ending states
		 * @return the new starting state
		 */
		private State thisOr(RE other, Set end)
		{
			State otherStart=addAll(other,end);
			State oldStart=start();
			
			State newStart=addStart();

			add(newStart,oldStart);
			add(newStart,otherStart);
			return newStart;
		}
		
		/**
		 * makes this regular expression also recognize what
		 * other regular expression recognizes
		 * @param other the other regular expression
		 */
		public void thisOr(RE other)
		{
			thisOr(other,end);
		}
		
		/**
		 * creates a regular expression that recognizes
		 * what this recognizes and other recognizes,
		 * in other words, creates a RE that recognizes
		 * the union of the languages. Unlike thisOr, this
		 * method does not modify the current RE.
		 * @param other the other regular expression
		 * @return the calculated regular expression
		 */
		public RE or(RE other)
		{
			RE result=(RE)this.clone();

			result.thisOr(other);
			return result;
		}

		/**
		 * calculates the concatenation of two RE
		 * and store it in this regular expression
		 * @param other the other regular expression
		 */
		void thisConcat(RE other)
		{
			Set endSet= new HashSet();
			State otherStart=addAll(other,endSet);
			
			Iterator iter=end.iterator();
			while (iter.hasNext())
			{
				State endState=(State)iter.next();
				add(endState,otherStart);
			}
			end=endSet;			
		}
		
		/**
		 * calculates the concatenation of two RE
		 * unlike thisConcat, this method does not modify
		 * the current RE;
		 * @param other the other regular expression
		 */
		public RE concat(RE other)
		{
			RE result=(RE)this.clone();

			result.thisConcat(other);
			return result;
		}

		/**
		 * calculates a regular expression wich is the
		 * n concatenation of this. 
		 * @param the number of concatenations to be applied
		 * @return the calculated re
		 */
		public RE repeat(int n)
		{
			RE result=epsilon();

			for (int i=0;i< n; i++)
				result.thisConcat(this);

			return result;
		}

		/**
		 * calculates a regular expression wich recognizes
		 * from zero to max concatenation of this
		 * @param max the maximum number
		 * @return the calculated re
		 */
		public RE repeatTo(int max)
		{
			RE result=epsilon();

			Set endSet=new HashSet();

			endSet.addAll(result.end);
			
			for (int i=0;i< max; i++)
			{
				result.thisConcat(this);
				endSet.addAll(result.end);
			}

			result.end=endSet;
			return result;
		}
		
		/**
		 * calculates a regular expression wich recognizes
		 * at least min concatenation of this
		 * @param min the minimum number of concatenations
		 * @return the calculated re
		 */
		public RE repeatFrom(int min)
		{
			RE result= epsilon();

			for (int i=1;i< min; i++)
				result.thisConcat(this);

			RE ending= epsilon();
			ending.thisConcat(this);
			ending.thisConcat(epsilon());

			Iterator iter= ending.end.iterator();

			while (iter.hasNext())
			{
				State endingState=(State)iter.next();

				if (min == 0)
					ending.add(ending.start(), endingState);
				
				ending.add(endingState, ending.start());
			}

			result.thisConcat(ending);
			return result;
		}
		
		/**
		 * calculates a regular expression wich recognizes
		 * at least min concatenation of this and at most
		 * max concatenations of this
		 * @param min the minimum number of concatenations
		 * @param max the maximum number of concatenations
		 * @return the calculated re
		 */
		public RE repeat(int min, int max)
		{			
			RE result= repeat(min);
			result.thisConcat(repeatTo(max-min));
			return result;
		}

		/**
		 * calculates the transitive clossure of the relationship
		 * given by the epsilon transitions
		 */
		private void epsilonClossure()
		{
			boolean changed;

			do
			{
		
				Iterator iter=epsilons.entrySet().iterator();

				changed=false;
				
				while (iter.hasNext())
				{
					
					Map.Entry entry=(Map.Entry)iter.next();

					State from=(State)entry.getKey();
					Set tos= (Set)entry.getValue();

					Set added=new HashSet();					

					Iterator dest=tos.iterator();

					while (dest.hasNext())
					{
						State to=(State)dest.next();
						
						added.addAll((Collection)epsilons.get(to));
					}
					
					changed= tos.addAll(added) || changed;
				}
			}
			while (changed);
		}

		/**
		 * get all the states that can be reached throw
		 * epsilon transitions from a given state
		 * @param state the state from where the clossure
		 *         wants to be calculated
		 * @return the set of all states that can be reached
		 */
		Set epsilonClossure(State state)
		{
			return (Set)epsilons.get(state);
		}

		/**
		 * get all the states that can be reached throw
		 * epsilon transitions from any of a given states
		 * @return set the set of states from where the clossure
		 *         wants to be calculated
		 * @return the set of all states that can be reached
		 */
		Set epsilonClossure(Collection set)
		{
			Set result=new HashSet();

			Iterator iter=set.iterator();
			while (iter.hasNext())
			{
				State one=(State)iter.next();
				
				result.addAll(epsilonClossure(one));
			}
			return result;
		}

		private final int AND = 0;
		private final int XOR = 1;
		private final int MINUS = 2;
		
		/**
		 * calculates a binary operation between two RE
		 * given l1 and l2 the languages generated by the
		 * two regular expressions, this methods calculates:
		 * <code>
		 * l1 & l2     // & = intersecction
		 * l1 ^ l2     // (l1 U l2) - (l1 & l2)
		 * l1 - l2
		 * </code>
		 * @param other the other regular expression
		 * @param operation the kind of operation to be
		 *        applied, AND, XOR, or MINUS		 
		 */
		private RE binary(RE other, int operation)
		{

			// standard operaton for all binary operations
			RE result= (RE)this.clone();

			Set myEnding=new HashSet();
			Set hisEnding=new HashSet();

			myEnding.addAll(result.end);
			result.thisOr(other,hisEnding);
			result.end.addAll(hisEnding);
			result=result.deterministic();

			// the and condition, if a final state contains only one of the ending states,
			// then it is not a final state

			Iterator iter=result.end.iterator();
			while (iter.hasNext())
			{
				GroupedState state= (GroupedState) iter.next();

				boolean myContains=state.containsAny(myEnding);
				boolean hisContains=state.containsAny(hisEnding);

				boolean keep=false;
				
				switch (operation)
				{
					case AND:
						keep=myContains && hisContains;
						break;
					case XOR:
						keep=myContains ^ hisContains;
						break;
					case MINUS:
						keep=myContains && !hisContains;
						break;
				}

				if (!keep)
					iter.remove();
			}


			return result;
		}

		/**
		 * performs the intersection between the two regular
		 * expression. This means the result will recognize
		 * a word if and only if both regular expressions
		 * recognize it
		 * @param other the other regular expression
		 * @return the calculated RE
		 */
		public RE and(RE other)
		{
			return binary(other,AND);
		}
		
		/**
		 * performs the difference between the two regular
		 * expression. This means the result will recognize
		 * a word if and only if only the first RE
		 * recognize it
		 * @param other the other regular expression
		 * @return the calculated RE
		 */
		public RE minus(RE other)
		{
			return binary(other,MINUS);
		}

		/**
		 * performs the xor between the two regular
		 * expression. This means the result will recognize
		 * a word if and only if just one RE 
		 * recognize it
		 * @param other the other regular expression
		 * @return the calculated RE
		 */
		public RE xor(RE other)
		{
			return binary(other,XOR);
		}

		/**
		 * calculates a regular expression that does not contain
		 * this as a substring. For example if this RE recognizes
		 * <code> ab </code>
		 * then this method returns a regular expression
		 * that recognizes anything (of any length) that
		 * doesn't contain "ab". So with the input
		 * <code> acdgbabasdf </code> the result would only
		 * recognize:
		 * <code> acdgba </code>
		 */
		public RE dontContain()
		{
			RE result=dot().repeatFrom(0);			
			result.thisConcat(this);

			result=result.deterministic();

			Set oldend=result.end;

			result.end=new HashSet();

			Iterator iter=result.states.iterator();

			while (iter.hasNext())
			{
				State next= (State)iter.next();

				if (oldend.contains(next))
					next.removeTransitions();
				else
					result.end.add(next);
			}
					
			return result;
		}


		/**
		 * removes superfluows states in the automata
		 * superfluows states are states that can not be
		 * reached from the start, or that can not reach
		 * an ending state
		 */
		private void removeUseless()
		{
			boolean changed;
			
			do
			{
				changed=false;
				Set toRemove=new HashSet();

				Iterator iter=states.iterator();
				int id=0;
				
				while (iter.hasNext())
				{
					State next=(State)iter.next();

					next.setId(id++);
					
					if (!end.contains(next))
					{
						if (next.useless())
						{
							toRemove.add(next);
							epsilons.remove(next);
							iter.remove();
							changed=true;
						}
					}
				}

				if (changed)
				{
					iter=states.iterator();
					while (iter.hasNext())
					{
						State state=(State)iter.next();
						state.remove(toRemove);
					}
				}
				
			} while (changed);

			if (states.size()==0)
				addStart();
			
		}
				
		/**
		 * tells each state to compress itself to be as small
		 * as it can be
		 */
		private void compressStates()
		{
			Iterator iter=states.iterator();
			while (iter.hasNext())
			{
				State state=(State)iter.next();
				state.compress();
			}
		}

		/**
		 * given a partition of the states, calculates
		 * the map with the associated grouped states
		 * @param part the partition partition
		 * @param states the set that will receive
		 *        all the grouped states
		 * @return the map of the states mapping to their group
		 * @see GroupedState
		 */
		private Map  groupedMap(Partition part,Set states)
		{
			Map result= new HashMap();
			
			Iterator iter = part.sets().iterator();

			int id=0;
			
			while (iter.hasNext())
			{				
				Set next=(Set)iter.next();
				
				GroupedState newState=new GroupedState(next);
				newState.setId(id++);
				
				states.add(newState);
				
				Iterator stateiter=next.iterator();
				while (stateiter.hasNext())
					result.put(stateiter.next(),newState);					
			}

			return result;			
		}
		
		/**
		 * given an initial partition calculates the minimal
		 * DFA possible. The resulting RE will recognize exaclty
		 * the same language as this, but the automata might be
		 * smaller
		 * @param part the partition to start with the algorithm
		 * @return the resulting RE
		 */
		protected RE minimal( Partition part)
		{

			//System.out.println("deterministic");
			//System.out.println(this);
			part.repartition();


			//System.out.println("partition");
			//System.out.println(part.sets());
			
			Set states= new HashSet();			
			Map grouped=groupedMap(part,states);
			
			RE result=new RE(true);

			result.addState(states);

			Iterator iter= result.states.iterator();
			
			while (iter.hasNext())
			{
				GroupedState state =(GroupedState)iter.next();

				if (state.containsAny(end))
					result.end.add(state);

				state.expand(grouped);
			}

			result.start=(State)grouped.get(start);

			result.removeUseless();
			result.compressStates();

			return result;
		}

		/**
		 * calculates the minimal
		 * DFA possible. The resulting RE will recognize exaclty
		 * the same language as this, but the automata might be
		 * smaller
		 * @param part the partition to start with the algorithm
		 * @return the resulting RE
		 */
		public RE minimal()
		{
			RE deterministic=deterministic();
			
			Partition part=new Partition(deterministic.states);
			
			part.split(deterministic.end);

			return deterministic.minimal(part);
		}
		
		/**
		 * calculates a deterministic DFA
		 * The resulting RE will recognize exaclty
		 * the same language as this, but the automata wont
		 * have any epsilon transition nor undeterministic transitions
		 * @return the resulting RE
		 */
		RE deterministic()
		{
			RE result= new RE(true);
			
			// calculates the transitive epsilon clossure for the states
		
			State other = addState();

			add(Character.MIN_VALUE, Character.MAX_VALUE, other, other);

			add(Character.MIN_VALUE, Character.MAX_VALUE, start, other);
			
			epsilonClossure();
			
			Map mappedStates=new HashMap();

			GroupedState newStart = new GroupedState(epsilonClossure(start()));
			
			mappedStates.put(epsilonClossure(start()), newStart);
			
			List toAnalize=new LinkedList();

			toAnalize.add(newStart);

			while (!toAnalize.isEmpty())
			{
				GroupedState nextState=(GroupedState)toAnalize.remove(0);
				result.addState(nextState);				
				nextState.expand(this,mappedStates,toAnalize);
			}

			result.start=newStart;

			
			Iterator iter=result.states.iterator();
			while (iter.hasNext())
			{
				GroupedState state=(GroupedState)iter.next();
				if (state.containsAny(end))
					result.end.add(state);
			}

			return result;
		}
		
		/**
		 * Transforms this regular expression into a human readable
		 * string. This is only usefull for debugging
		 * @return a human readable string
		 */
		public String toString()
		{
			String result=states.toString()+"\n";

			Iterator iter=epsilons.entrySet().iterator();
			while (iter.hasNext())
			{
				Map.Entry entry=(Map.Entry)iter.next();
				State from = (State)entry.getKey();
				Set to = (Set)entry.getValue();

				if (to.size()> 1)
					result += from.getId() + ": -> " + State.printIds(to)+"\n";
			}
				
			result+="initial: " + start().getId() + "\n";
			result+="final: " + State.printIds(end);

			return result;
		}
		
		/**
		 * duplicates this regular expression
		 * @return a clone of this re
		 */
		public Object clone()
		{
			RE clon=new RE(true);
			clon.start = clon.addAll(this, clon.end);
			return clon;
		}

}
