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
 * $Id: State.java,v 1.4 1999/11/21 02:12:39 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: State.java,v $
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
 * Revision 1.3  1999/10/09 02:54:32  Paul
 * Some performance improvements
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
 * Represents a lexicographical state. A state contains
 * transitions to other states in the automata
 * @version     $Revision: 1.4 $
 * @author      Paul Pacheco
 * @since       JDK1.2
 */

class State  implements Comparable, Repartitionable
{		
      /**
       * represents the characters with wich the
       * state transits. this is in the form of
       * a sorted set. for example if the state contains
       * transitions with a-z, A-Z and 0-9 then this set
       * would contain
       * <code> { '0', '9'+1, 'a', 'z'+1, 'A', 'Z'+1 } </code>
		 */
      private SortedSet domain = new TreeSet();

      /**
       * represents the childs of the transitions
       * this list contains domain.size() + 1 elements
       * and an element is null if there is no transition
       * for example if the state contains transitions to x, y, z
       * with a-z, A-Z and 0-9 respectivelly, then this list
       * would contain <code>
       * { null,                \\ \u0000 - 0
       *   x,                   \\ 0-9
       *	  null,                \\ 9-a
       *	  y,                   \\ a-z
       *	  null,                \\ z-A
       *	  z,                   \\ A-Z
       *	  null}                \\ Z - \uffff
       * </code>
       */
      protected List childs = new LinkedList();
		
      /**
       * contains the id for this state
       */
      private int id;

      
      /**
       * contains the owner of this state, the owner
       * is the RE that would be recognized if this
       * state is an ending state
       */
      private RE owner;
      
      
      /**
       * creates a lexicographical state with no transitions
       */
      State()
      {
	 childs.add(null);
      }
      
      /**
       * Gets the identifier of this State
       * @return int this identifier
       */
      int getId ()
      {
	 return this.id;
      }
      
      /**
       * Sets the identifier of this State
       * @param id Identifier 
       * @return int this identifier
       */
      void setId (int id)
      {
	 this.id = id;
      }

      /**
		 * sets the owner of this regular expression. The owner
		 * is the RE that would be recognized if this
		 * state is an ending state
		 * @param owner the new owner
		 */
      void setOwner(RE owner)
      {
	 this.owner=owner;
      }
		
      /**
		 * gets the owner of this regular expression. The owner
		 * is the RE that would be recognized if this
		 * state is an ending state
		 * @return the owner, null if there is no owner
		 */
      RE getOwner()
      {
	 return this.owner;
      }

      private static short stateData[] = new short[8];
		


      /**
       * ubicates the domain of this state inside the pool of data
       * @param pool the pool where the domain is to be inserted
       * @return the position of the domain in the pool
       */      
      private long ubicateDomain(DataPool pool)
      {
	 char []domain = new char[this.domain.size()];

	 Iterator iter = this.domain.iterator();
	 int index=0;
	 
	 while (iter.hasNext())
	 {
	    domain[index++] = ((Character)iter.next()).charValue();
	 }

	 return pool.ubicate(domain);
      }

      /**
       * ubicates the range of this state inside the pool of data
       * @param pool the pool where the range is to be inserted
       * @return the position of the range in the pool
       */      
      private long ubicateRange(DataPool pool)
      {
	 short range[]= new short[childs.size()];
	 
	 Iterator iter = childs.iterator();
	 
	 for (int i=0;i< childs.size(); i++)
	 {
	    State child= (State)iter.next();
				
	    if (child == null)
	       range[i]=0;
	    else
	       range[i]=(short)(child.getId()+1);
	 }

	 return pool.ubicate(range);
      }
      
      /**
       * stores the data in the state in a data pool.
       * the data is stored in the following format:
       * the domain as an array containing what the set
       * contains. the childs as an array containing
       * the identifiers +1 of the childs or 0 if there is
       * no child.
       * the rest as an array of int containing:
       * <code>
       * { domainPos,        // the position of the domain
       *   domainCheck,      // the check position of the domain
       *   domainSize,       // the size of the domain
       *   childPos,         // the positin of the childs
       *   childCheck,       // the check position of the childs
       *   owner+1}          // the identifier of the owner of the state
       *                     // or 0 if there is no owner
       * </code>		 
       * @param pool the data pool
       * @return the packed position and check position of the
       *         state array to be used by the lexer
       * @see Pack.pack(int, int)
       * @see Lexer.table(DataPool)
       */
      
      long table(DataPool pool)
      {
	 
	 long domainPos = ubicateDomain(pool);
	 long rangePos = ubicateRange(pool);

	 int domainIndex = Pack.first(domainPos);
	 short domainCheck = (short)Pack.second(domainPos);
	 int rangeIndex = Pack.first(rangePos);
	 short rangeCheck = (short)Pack.second(rangePos);
	 
	 short owner =0;

	 if (getOwner() != null)
	 {
	    owner = (short)(getOwner().getId() + 1);
	 }

	 stateData[0]=Pack.firstShort(domainIndex);
	 stateData[1]=Pack.secondShort(domainIndex);
	 stateData[2]=domainCheck;	 
	 stateData[3]=(short)domain.size();
	 stateData[4]=Pack.firstShort(rangeIndex);
	 stateData[5]=Pack.secondShort(rangeIndex);
	 stateData[6]=rangeCheck;
	 stateData[7]=owner;

	 /*
	 System.out.println("id " + getId());
	 System.out.println("\tdomain    " + domainIndex);
	 System.out.println("\tdom chk   " + domainCheck);
	 System.out.println("\tsize      " + domain.size());
	 System.out.println("\trange     " + rangeIndex);
	 System.out.println("\trange chk " + rangeCheck);
	 System.out.println("\towner     " + owner);
	 */ 
	 
	 
	 return pool.ubicate(stateData);
			
      }

      void getRange(short [] range)
      {
	 char next = Character.MIN_VALUE;

	 Iterator domiter = domain.iterator();
	 Iterator childIter = childs.iterator();

	 State child = null;
	 
	 for (int i=0;i< range.length; i++)
	 {
	    if (i >= (int)next)
	    {
	       child = (State)childIter.next();
	       
	       if (domiter.hasNext())
		  next = ((Character)domiter.next()).charValue();
	       else
		  next = Character.MAX_VALUE;
	    }

	    if (child == null)
	       range[i] =0;
	    else
	       range[i]=(short)(child.getId()+1);
	       
	 }
	    
	 
      }
      
      void range(char [] mergeddomain, char [] range)
      {
	 Iterator domIter = domain.iterator();
	 Iterator childIter = childs.iterator();

	 int pos =0;
	 
	 while (childIter.hasNext())
	 {
	    State child = (State) childIter.next();

	    char id= 0;

	    if (child != null)
	       id = (char)(child.getId()+1);
	    
	    char nextChar = Character.MAX_VALUE;

	    if (domIter.hasNext())
	    {
	       nextChar = ((Character)domIter.next()).charValue();
	       nextChar --;
	    }

	    do
	    {
	       range[pos++]=id;
	    }
	    while (pos <= mergeddomain.length && mergeddomain[pos-1] <= nextChar);
	 }
      }
      
      /**
       * merges this state's domain to a global domain
       */
      void mergeDomain(Set mergedDomain)
      {
	 mergedDomain.addAll(domain);
      }
      
      
      /**
       * removes all the transitions of the state
       */
      void removeTransitions()
      {
	 domain.clear();
	 childs.clear();
	 childs.add(null);
      }

      /**
		 * gets an identifier for this state and carries on
		 * @param marked the set of all the states with an identifier
		 */
      void setIds(Set marked)
      {
	 Iterator iter = childs.iterator();
	 Set toVisit= new HashSet();
			
	 while (iter.hasNext())
	 {
	    State child = (State)iter.next();
	    if (child!= null)
	    {
	       if (!marked.contains(child))
	       {
		  toVisit.add(child);
		  child.setId(marked.size());
		  marked.add(child);
	       }
	    }
	 }

	 iter = toVisit.iterator();
	 while (iter.hasNext())
	 {
	    State child = (State)iter.next();
	    child.setIds(marked);
	 }			
				
      }
		
      /**
		 * Compresses the state by reducing redundant information
		 * in the transitions. If two consecutive transitions
		 * contain the same childs, then they can be considered
		 * just one transition
		 */
      void compress()
      {
	 Iterator domIter= domain.iterator();
	 Iterator childIter= childs.iterator();

	 Object lastChild = childIter.next();
			
	 while (childIter.hasNext())
	 {
	    Object child = childIter.next();
	    domIter.next();				

	    if (lastChild == child)
	    {
	       domIter.remove();
	       childIter.remove();
	    }

	    lastChild=child;
	 }
      }

      /**
		 * given a set of states, removes all the references
		 * to those states.
		 * @param states the set of states to whom the references
		 *        are to be removed
		 */
      void remove(Set states)
      {
	 ListIterator iter = childs.listIterator();

	 while (iter.hasNext())
	 {
	    Object next = iter.next();

	    if (next == null)
	       continue;
	    else if (states.contains(next))
	       iter.set(null);
	    else if (next instanceof Collection)
	    {
	       Collection nextCol = (Collection)next;

	       nextCol.removeAll(states);

	       switch (nextCol.size())
	       {
		  case 0:
		     iter.set(null);
		     break;
		  case 1:
		     iter.set(nextCol.iterator().next());
		     break;
	       }
	    }
	 }
      }
		
      /**
		 * adds a transition with a range of characters
		 * @param from the start of the range
		 * @param to the end of the range
		 * @param other the ending state of the transition
		 */
      void add(char from, char to, State other)
      {
	 add(from,to, Collections.singleton(other));
      }

      /**
		 * adds a transition with a range of characters to a set
		 * of childs
		 * @param from the start of the range
		 * @param to the end of the range
		 * @param other the ending states of the transition
		 */
      void add(char from, char to, Collection other)
      {

	 if (other.size()==0)
	    return;
			
	 Character fromChar = new Character(from);
	 Character toChar = new Character((char)(to+1));
			
	 int fromPos = domain.headSet(fromChar).size();
			
	 if (from > Character.MIN_VALUE)
	 {
	    if (domain.add(fromChar))
	    {
	       Object toAdd = childs.get(fromPos);

	       if (toAdd != null && toAdd instanceof Collection)
	       {
		  Collection newCol = new HashSet();
		  newCol.addAll((Collection)toAdd);
		  toAdd=newCol;
	       }
					
	       childs.add(fromPos, toAdd);
	    }
	    fromPos++;
	 }

	 int toPos=childs.size();
			
	 if (to < Character.MAX_VALUE)
	 {
	    toPos = domain.headSet(toChar).size();
				
	    if (domain.add(toChar))
	    {
	       Object toAdd = childs.get(toPos);

	       if (toAdd != null && toAdd instanceof Collection)
	       {
		  Collection newCol = new HashSet();
		  newCol.addAll((Collection)toAdd);
		  toAdd=newCol;
	       }
					
	       childs.add(toPos, toAdd);
	    }
	    toPos++;
	 }

	 ListIterator iter=childs.listIterator(fromPos);

	 Object subst= other.iterator().next();
			
	 for (int i=fromPos;i< toPos; i++)
	 {
	    Object child = (Object)iter.next();

	    Collection toAdd = null;
				
	    if (child == null)
	    {
	       if (other.size()==1)
	       {
		  iter.set(subst);
		  continue;
	       }
	       else
		  toAdd = new HashSet();
	    }
	    else if (child instanceof Collection)
	    {
	       toAdd = (Collection)child;
	    }
	    else
	    {
	       toAdd = new HashSet();
	       toAdd.add(child);
	    }

	    toAdd.addAll(other);
	    iter.set(toAdd);
	 }
      }


      /**
		 * adds a transition with a character
		 * @param ch the character with wich the transition
		 *        is to be make
		 * @param other the ending state of the transition
		 */
      void add(char ch, State other)
      {
	 add(ch,ch,other);
      }

      /**
		 * adds a transition with a character
		 * @param ch the character with wich the transition
		 *        is to be make
		 * @param other the ending states of the transition
		 */
      void add(char ch, Collection other)
      {
	 add(ch,ch,other);
      }

      /**
		 * adds the transitions of other state to this state
		 * @param other the state containing all the other transitions
		 */
      void merge(State other)
      {
	 char lastChar= Character.MIN_VALUE;

	 Iterator domIter = other.domain.iterator();
	 Iterator childIter = other.childs.iterator();

	 char nextChar;
			
	 while (childIter.hasNext())
	 {
	    nextChar = Character.MAX_VALUE;
	    char to = nextChar;
				
	    if (domIter.hasNext())
	    {
	       to=nextChar= ((Character)domIter.next()).charValue();
	       to--;
	    }

	    Object child = childIter.next();

	    if (child == null)
	       ;
	    else if (child instanceof Collection)
	       add(lastChar, to, (Collection)child);
	    else if (child instanceof State)
	       add (lastChar, to, (State)child);

	    lastChar= nextChar;
	 }
      }

      /**
		 * maps a set
		 * @return the set of mapped objects
		 */
      public static Set map(Map map, Collection set)
      {
	 Set result=new HashSet();
	 Iterator iter=set.iterator();
	 while (iter.hasNext())
	    result.add(map.get(iter.next()));

	 return result;
      }
		
      /**
		 * Copies this state to other state, but every
		 * transition goes through a mapping proccess
		 * before the copy is made
		 * @param map the map with wich to make the mapping
		 */
      void cloneTo(Map map)
      {
	 State myImage=(State)map.get(this);

	 myImage.domain.clear();
	 myImage.domain.addAll(domain);
	 myImage.childs.clear();
	 myImage.childs.addAll(childs);
	 myImage.owner=owner;
				
	 ListIterator iter = myImage.childs.listIterator();

	 while (iter.hasNext())
	 {
	    Object next = iter.next();

	    if (next == null)
	       ;
	    else if (next instanceof Collection)
	       iter.set(map(map, (Collection)next));
	    else 
	       iter.set(map.get(next));
	 }
			
      }

      /**
		 * prints the identifiers in an object to a string
		 * The object can be a state, null or a collection of
		 * states
		 * @param obj the object containing the ids.
		 * @return the string with a human readable ids
		 */
      public static String printIds(Object obj)
      {
	 if (obj == null)
	    return "";
	 else if (obj instanceof Collection)
	    return printIds((Collection)obj);
	 else
	    return ((State)obj).getId()+"";
      }
		
      /**
		 * prints the identifiers of objects to a string
		 * The objects can be a state, null or a collection of
		 * states
		 * @param coll the collection of objects. Each object
		 *        can be null, a state or a collection
		 * @return the string with a human readable ids
		 */
      public static String printIds(Collection coll)
      {
	 String result="";
			
	 Iterator iter = coll.iterator();
	 while (iter.hasNext())
	 {
	    result+=printIds(iter.next());
	    if (iter.hasNext())
	       result +=", ";
	 }
			
	 return result;
      }
		
      /**
		 * determines if this state transits to the same partition class
		 * as other state. This is necesary for the minimal DFA algorithm
		 * @param from the starting of the range to be tested
		 * @param to the ending of the range to be tested
		 * @param part the partition containing the classes
		 * @param destiny the ending of the transition, we will see if
		 *        we transit to the same class destiny is in
		 * @return true if the transition is contained
		 *         false otherwise
		 */
      private boolean transitsToClass(char from, char to, Partition part, State destiny)
      {
	 int fromPos = domain.headSet(new Character((char)(from+1))).size();

	 int toPos;

	 if (to == Character.MAX_VALUE)
	    toPos=childs.size();
	 else
	    toPos = domain.headSet(new Character((char)(to+1))).size()+1;

	 Iterator iter =  childs.listIterator(fromPos);

	 //System.out.println("Transito " + this);
			
	 for (int i=fromPos; i< toPos; i++)
	 {
				//System.out.println("desde " + fromPos + " - " + toPos);
				
	    State myDestiny= (State)iter.next();

	    if (myDestiny == null)
	       return false;
				
	    if (!part.sameClass(destiny,myDestiny))
	       return false;

	 }

	 return true;
      }

      /**
		 * For each transition here, tests if the other goes
		 * to the same class
		 * @param other the other state
		 * @param part the partitin this state is in
		 * @return true if the other state transit to the same place
		 */
      private boolean superSets(State other, Partition part)
      {
	 char lastChar= Character.MIN_VALUE;

	 Iterator domIter = domain.iterator();
	 Iterator childIter = childs.iterator();

	 char nextChar;

	 while (childIter.hasNext())
	 {
	    State child = (State)childIter.next();

	    nextChar = Character.MAX_VALUE;
	    char to= nextChar;
	    if (domIter.hasNext())
	    {
	       to = nextChar= ((Character)domIter.next()).charValue();
	       to--;
	    }

	    if (child != null)
	    {
	       //System.out.println("soy " + this + " viendo parte [" + lastChar + "-" + to + "] con child "+ child);
	       if (!other.transitsToClass(lastChar,to,part,child))
	       {
		  //System.out.println("dio falso");
		  return false;
	       }
	    }
	    lastChar=nextChar;
	 }
	 return true;
      }

      /**
		 * determines if this state belongs to the same class as other
		 * in a partition according to the minimal DFA algorithm
		 * @param other the other State
		 * @param part the partition
		 * @return true if they belong to the same class
		 */
      public boolean sameClass(Repartitionable other, Partition part)
      {
	 State otherState= (State)other;
			
	 /*System.out.println("Comparing " +this.getId() + " con " + ((State)other).getId());
			
	   boolean result1 =  superSets(otherState,part);
	   boolean result2 =otherState.superSets(this,part);

	   System.out.println("Resulto " + result1 + ", " + result2);
	   return result1 && result2;
	 */

	 return superSets(otherState,part); //&& otherState.superSets(this,part);

			
      }
		
      /**
		 * converts this state to a human readable string
		 * @return a human readable string of the state
		 *         usefull only for debugging
		 */
      public String toString()
      {
	 String result="\n" + getId();

	 if (getOwner()!=null)
	    result+="("+getOwner().getId()+")";
			
	 result+=": ";

			
	 char lastChar= Character.MIN_VALUE;

	 Iterator domIter = domain.iterator();
	 Iterator childIter = childs.iterator();

	 char nextChar;

	 while (childIter.hasNext())
	 {
	    Object child = childIter.next();

	    nextChar = Character.MAX_VALUE;
	    char to= nextChar;
	    if (domIter.hasNext())
	    {
	       to = nextChar= ((Character)domIter.next()).charValue();
					
	       to--;
	    }
				
	    if (child != null)
	    {
	       if (lastChar==to)
		  result += lastChar;
	       else
		  result += "["+(int)lastChar+"-"+(int)to+"] ";
					
	       result+=" -> ";
	       result += printIds(child)+"    ";
	    }
				
	    lastChar=nextChar;
					
	 }
						
	 return result;
      }

      /**
		 * determines if this state is useless, A state is
		 * useless if it does not transit to any other place
		 * but himself
		 * @return true if the state is useless
		 *         false otherwise
		 */
      boolean useless()
      {
	 Iterator iter = childs.iterator();
	 while (iter.hasNext())
	 {
	    State child = (State)iter.next();
	    if (child != null && child != this)
	       return false;
	 }

	 return true;
      }

      /**
		 * tells what the size of the domain is
		 * @return the size of the domain
		 */
      int size()
      {
	 return domain.size();
      }
		
      /**
		 * compares this state with some other. by ther identifier
		 * @param other the other state
		 * @return -1 if this.id() < other. id()
		 *          0 if this.id() = other.id()
		 *          1 if this.id() > other.id()
		 */
      public int compareTo(Object other)
      {
	 int otherId=((State)other).getId();

	 if (getId() > otherId)
	    return 1;

	 if (getId() < otherId)
	    return -1;

	 return 0;
      }
}


	
