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
 * $Id: Lexer.java,v 1.4 1999/11/21 02:12:39 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: Lexer.java,v $
 * Revision 1.4  1999/11/21 02:12:39  Paul
 * Dramatically reduced the size of the generated tables (suggested by Ascander Suarez)
 *
 * Revision 1.3  1999/11/07 14:13:45  Paul
 * Moved the java runtime library to another package.
 *
 * Revision 1.2  1999/11/06 01:43:16  Paul
 * Improved the table generation
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
import ve.usb.Claire.table.*;
import ve.usb.Claire.runtime.java.Pack;

/**
 * Represents a lexicographical analizer. A lexicographical analizer
 * is the combination between several regular expressions. the resulting
 * regular expresion is also able to distinguish to whom the
 * resulting final DFA states belong to.
 * @version     $Revision: 1.4 $
 * @author      Paul Pacheco
 * @since       JDK1.2
 */

public class Lexer extends RE
{

      /**
       * the set of regular expressions recognized by the lexer
       */
      private Set regexps = new HashSet();


      /**
       * the map from sintactic states as Integers, to the set of
       * regular expressions that it recognizes
       */
      private Map sintacticLexers = new HashMap();

      /**
       * Creates a lexicographical analizer.
       */
      public Lexer()
      {
      }

      /**
       * adds a private lexer to a sintactic state
       */
      public void put(int state, Collection regexps)
      {
	 sintacticLexers.put(new Integer(state), regexps);
	 this.regexps.addAll(regexps);
      }

      /**
       * adds all the regular expressions to the automata
       * @return a map from regular expressions to the starting point of it
       */
      
      private Map addExpressions()
      {
	 Iterator iter=regexps.iterator();

	 Map startingStates = new HashMap();

	 
	 while (iter.hasNext())
	 {
	    RE next = (RE)iter.next();
	    next.markStates();

	    State first = addAll(next, end);

	    startingStates.put(next,first);

	    //System.out.println(next.getId()+ " starts with " + first.getId());
	 }

	 
	 return startingStates;
      }

      /**
       * adds the initial transitions for sintactic state
       * @param state the sintactic state
       * @param recognized the collection of regular expressions recognized by the state
       * @param startingStates the map from regular expressions to their starting lexical state
       */      
      private void addInitialTransitions(int state, Collection recognized, Map startingStates)
      {
	 Iterator iter = recognized.iterator();
	 //System.out.println("for" + state);
	 
	 while (iter.hasNext())
	 {
	    RE regexp = (RE)iter.next();
	    
	    State next= (State)startingStates.get(regexp);
	    if (next != null)
	       add((char)state, start, next);

	 }
      }
      
      /**
       * creates the automata from the constructed lexical analizer
       */
      private void createAutomata()
      {
	 Map startingStates = addExpressions();	 
	 Iterator iter = sintacticLexers.entrySet().iterator();

	 while (iter.hasNext())
	 {
	    Map.Entry entry = (Map.Entry)iter.next();

	    int state = ((Integer)entry.getKey()).intValue();

	    Collection recognized = (Collection)entry.getValue();

	    addInitialTransitions(state, recognized, startingStates);
	 }
	 
      }

      /**
       * divides the states into unmergeable groups.
       * this is usefull for the minimal automata
       * @param deterministic the deterministic automata
       * @return resulting partition of states
       */
      private static Partition initialPartition(RE deterministic)
      {
	 Map endingMap=new HashMap();
			
	 Iterator iter = deterministic.end.iterator();

	 Set endingSets=new HashSet();
			
	 while (iter.hasNext())
	 {

	    GroupedState next= (GroupedState)iter.next();

	    RE original=next.getOwner();
	    Set hisSet=(Set)endingMap.get(original);
	    if (hisSet!=null)
	    {
	       hisSet.add(next);
	    }
	    else
	    {
	       Set newSet= new HashSet();
	       newSet.add(next);
	       endingMap.put(original,newSet);
	       endingSets.add(newSet);
	    }
	 }
				
	 Partition part = new Partition(deterministic.states);
	 
	 iter=endingSets.iterator();
	 while (iter.hasNext())
	 {
	    Set next= (Set) iter.next();
	    part.split(next);
	 }

	 return part;
      }

      /**
       * set the identifiers for the lexical states
       */
      private void setIds()
      {
	 start.setId(0);

	 // no one can ever go back to start. therefore
	 // it does not need its own id.
	 int id = 0;
	 
	 Iterator iter = states.iterator();
	 while (iter.hasNext())
	 {
	    State state = (State) iter.next();
	    if (state != start)
	       setId(id++);
	 }
      }
      
      /**
       * creates the minimalistic automata for the constructed lexical analizer
       */
      private void build()
      {
	 // first add all the expressions and the initial transitions
	 createAutomata();
	 

	 // now, get the deterministic automata
	 RE deterministic=deterministic();


	 // do not merge everything. split the states in minimal groups	 
	 Partition part = initialPartition(deterministic);

	 // using the initial partition that does not mix regexps,
	 // get the minimal automata.
	 RE minimal= deterministic.minimal(part);

	 //System.out.println(minimal);

	 // copy the minimal automata into this automata
	 start=minimal.start;
	 end = minimal.end;
	 epsilons = minimal.epsilons;
	 states=minimal.states;

			
	 // set the identifiers for the states. no matter the order, as
	 // long as the initial state is 0
	 setIds();
      }


      /**
       * comparator to sort the States in size order
       * If we store the bigger states first in the pool,
       * compression should be better
       */
      private static class SizeComparator implements Comparator
      {
				/**
				 * compares between two lexicographical states
				 * @param o1 the first state
				 * @param o2 the second state
				 * @return -1 if o1.size() < o2.size()
				 *          0 if o1.size() == o2.size()
				 *          1 if o1.size() > o2.size()
				 */
	    public int compare (Object o1, Object o2)
	    {
	       State o1state = (State) o1;
	       State o2state = (State) o2;
					
	       if (o1state.size() > o2state.size())
		  return -1;
	       if (o1state.size() < o2state.size())
		  return 1;
	       return 0;							 
	    }
      };


      /**
       * The size comparator to be used in sorting the state
       * in size order
       */
      private static SizeComparator sizecomparator = new SizeComparator();
		

      private char[] mergedDomain()
      {
	 Set mergedDomain = new TreeSet();

	 Iterator iter = states.iterator();
	 while (iter.hasNext())
	 {
	    State state = (State)iter.next();
	    state.mergeDomain(mergedDomain);
	 }

	 char insertedDomain[]=new char[mergedDomain.size()];
		   
	 iter = mergedDomain.iterator();
		   
	 for (int i=0; i< mergedDomain.size(); i++)
	    insertedDomain[i]=((Character)iter.next()).charValue();

	 return insertedDomain;
      }
      
      /**
       * Stores the lexical analizer in the data pool
       * currently, it stores each state in size order (bigger first)
       * then it stores the array of positions and checks into the
       * pool returning the result. For example the storage would
       * contain something like:
       * <code>
       * { p1, c1, p2, c2, p3, c3, ... }
       * where
       *    pi:  the position of the ith state
       *    ci:  the check position of the ith state
       *    and pi,ci is returned by each state
       * </code>
       * @see State.table(DataPool)
       * @param pool the pool where the lexer will be
       * @return the position of the states array in the pool
       */
      public long table(DataPool pool, short [] initialStep)
      {

	 build();

	 
	 // we dont need to store the start state, because it will be
	 // saved in the given array
	 long [] statePositions = new long [states.size() ];
	 
	 List sorted = states; //sortedStates();
	 Collections.sort(sorted, sizecomparator);

	 Iterator iter=sorted.iterator();
		
	 while (iter.hasNext())
	 {
	    State state = (State)iter.next();

	    if (state != start)
	       statePositions[state.getId()] = state.table(pool);
	 }

	 //System.out.println(start);
	 
	 start.getRange(initialStep);

	 //System.out.println(this);

	 //for (int i=0;i< initialStep.length ; i++)
	 //   System.out.println("for " + i + " goto " + initialStep[i]);
	 
	 return pool.ubicate(statePositions);

      }
}
