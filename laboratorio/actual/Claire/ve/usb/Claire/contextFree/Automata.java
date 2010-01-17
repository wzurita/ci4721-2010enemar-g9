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
 * $Id: Automata.java,v 1.2 1999/11/07 14:13:40 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: Automata.java,v $
 * Revision 1.2  1999/11/07 14:13:40  Paul
 * Moved the java runtime library to another package.
 *
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 * Revision 1.9  1999/10/09 01:59:14  Paul
 * Added some more performance improvments.
 * Fixed a small bug that caused some grammars not to work properly
 * Compression is now allways done
 *
 * Revision 1.8  1999/10/09 00:15:08  Paul
 * Several speed improvments
 *
 * Revision 1.7  1999/09/29 03:03:09  Paul
 * Improved compression speed just a little more
 *
 * Revision 1.6  1999/09/29 02:47:27  Paul
 * Drammatically improved the speed of the compression algorithm
 *
 * Revision 1.5  1999/09/09 12:20:23  Paul
 * Added some javadoc comments
 *
 * Revision 1.4  1999/09/09 10:32:48  Paul
 * Removed some obsolete functions
 *
 * Revision 1.3  1999/09/09 10:07:34  Paul
 * Remove obsolete InitialWhiteSymbol wich was the white symbol inserted in the first rule
 *
 * Revision 1.2  1999/09/09 09:53:55  Paul
 * Added some cvs comments
 *
 */
package ve.usb.Claire.contextFree;
import java.util.*;
import java.io.*;
import ve.usb.Claire.util.*;
import ve.usb.Claire.*;
import ve.usb.Claire.contextFree.symbol.*;

/**
 * generates and manages the viable prefix automata,
 * fills the shift, goto and reduce tables and writes them to disk
 * @version     $Revision: 1.2 $
 * @author      Maria Eugenia Ahues
 * @author      Bernardo Munoz
 * @author      Rui Santos
 * @author      Paul Pacheco
 * @since       JDK1.2
 * @see State
 */
public class Automata
{
      /**
       * list of LR(1) states
       */
      private List states=new ArrayList();

      /**
		 * list of initial rules. Initial rules are rules
		 * that are introduced by the engine to start the parsing
		 */
      private List initialRules=new ArrayList();

      /**
		 * Grammar to be used
		 */
      private Grammar grammar;

      /**
		 * Unit that contains several options as well as a mecanism for warnings
		 */		 
      private Unit unit;

      /**
		 * The starting symbol of the grammar
		 */
      private static Symbol artificialSym=new StartSymbol("%start");

      /**
		 * the symbol wich represents the end of file
		 */
      public static Symbol eofSym=new PredefSymbol(ve.usb.Claire.runtime.java.Language.EOF,"EOF",null);
		
      /**
		 * adds the rules necesary to create the extended grammar
		 */
      private void extendedGrammar()
      {

	 Iterator initials=grammar.initialSymbols().iterator();
			
	 int initialCode=0;

	 Symbol initWhite= new WhiteSymbol(0);

	 while (initials.hasNext())
	 {
				
	    Rule artificialRule=new Rule(artificialSym);

	    Symbol init=(Symbol)initials.next();
				
	    if (initWhite != null)
	       artificialRule.add(initWhite);
				
	    artificialRule.add(init);
	    artificialRule.add(eofSym);				

	    artificialRule.reduced(true);

				//System.out.println(artificialRule);
				
	    grammar.add(initialCode,artificialRule);

	    initialRules.add(artificialRule);
	    initialCode++;

	 }
			
	 Rule artificialRule = new Rule(artificialSym);
			
	 grammar.add(initialCode,artificialRule);

	 initialRules.add(artificialRule);

	 artificialRule.reduced(true);
      }

      /**
		 * add the initial LR(1) states, from them, all the other
		 * states will be found
		 */
      private void addInitialStates()
      {

	 Iterator enum=initialRules.iterator();
			
	 short id=0;
			
	 while (enum.hasNext())
	 {
				
	    Rule artificialRule=(Rule)enum.next();
				
	    State InitialState= new State (new Item(artificialRule));
			
	    states.add(InitialState);

	    InitialState.setId(id++);

				//System.out.println(InitialState);
	 }
      }

      /** 
		 * Creates an LR(1) automata
		 * @param grammar the grammar to be used
		 * @param unit the compilation unit that contains several options
		 */
      public Automata (Grammar grammar, Unit unit)
      {

	 this.grammar = grammar;
	 this.unit=unit;
			
	 eofSym=grammar.declare(eofSym);
			
	 extendedGrammar();

	 unit.reportProgress("Finished adding fiction rules, time:");
			
	 grammar.expand(unit);
			
	 unit.reportProgress("Finished expanding rules, time:");

	 //System.out.println(rules);
			

	 addInitialStates();
	 // this.initial_symbol = rule.getLeftHand ();
			
	 expand();

	 //System.out.println(states);

	 removeUnused();

	 unit.reportProgress("Finished building automata, time:");

	 compress();
	 unit.reportProgress("Finished compressing automata, time:");
			
			
	 doVerbose();
			
      }
  
      /**
		 * gets the list of states of the automata
		 * @return the states of the automata
		 */
      public Collection states()
      {
	 return Collections.unmodifiableList(states);
      }
		
      /**
		 * Builds the viable prefix automata.
		 * This is done using the LR(1) automata generator algorithm
		 * @return the conflicts. It is a map from state, to collection of conflicts
		 */ 
      private Map expand () 
      {    
	 int state_number = 1;      
			
	 // Step 2, Para cada estado actual en es hacer
	 Iterator iter =states.iterator();

	 Map searcher=new HashMap();

	 while (iter.hasNext())
	 {
	    Object next=iter.next();
	    searcher.put(next,next);
	 }

	 Map conflicts= new HashMap();
			

	 for (int i=0;i< states.size();i++)
	 {
	    State actual = (State) states.get(i);

				//System.out.println(actual);

	    actual.expand(grammar);
				
				//Main.debug("State to be expand: " + actual.getId () + "\n Value: \n" + actual.toString ());

	    short startid=(short)states.size();

	    short id=startid;
				
				// eliminates useles States.
				
	    iter=actual.transitions().entrySet().iterator();

	    while (iter.hasNext())
	    {

	       Map.Entry entry=(Map.Entry)iter.next();
					
	       Symbol sym=(Symbol)entry.getKey();
	       State next=(State)entry.getValue();
					
	       State existing=(State)searcher.get(next);
					
	       if (existing==null)
	       {
		  states.add(next);
		  searcher.put(next,next);
		  next.setId(startid++);
	       }
	       else
		  actual.put(sym,existing);
	    }
	 }
	 return conflicts;
      }

      /**
		 * Removes the unused rules. Unused rules
		 * are those who are never reduced.
		 * Also issues a warning to the user for each unused rule
		 */
      private void removeUnused()
      {
	 boolean first=true;
			
	 for (short i=0;i< grammar.size();)
	 {

	    Rule rule=(Rule)grammar.get(i);

	    if (rule.reduced())
	    {
	       rule.setId(i++);

	       rule.markSymbolsAsUsed();
	    }
	    else
	    {
	       if (first)
		  unit.warning("Warning: the following rules are never reduced", 1);

	       first=false;
					
	       unit.warning("\t"+rule.toString(), 1);

	       grammar.remove(i);
	    }
	 }

	 grammar.setSymbolsId();
			
      }

      /**
		 * calculate the initial classes for the compression
		 * @return the list of initial classes
		 */
      private Collection initialClasses()
      {
	 Map classMap = new TreeMap();

	 List classes = new ArrayList();
			
	 Iterator iter = states.iterator();
	 while (iter.hasNext())
	 {
	    State state = (State)iter.next();

	    Object comparator = new Integer(state.hashCode());//initialRules();

	    Set clas = (Set)classMap.get(comparator);
	    if (clas == null)
	    {
	       Set newClass = new HashSet();
	       classes.add(newClass);
	       newClass.add(state);
	       classMap.put(comparator,newClass);
	    }
	    else
	    {
	       clas.add(state);
	    }
	 }
	 return classes;
      }
		
      /**
		 * Compresses the automata by eliminating redundant states.
		 * this compression algorithm is very slow, and the user
		 * enables it through the command line.
		 */
      private void compress()
      {
			
	 Collection col = initialClasses();

	 Partition part= new Partition();

	 Iterator iter = col.iterator();
	 while (iter.hasNext())
	    part.put((Set)iter.next());
						
	 part.repartition();
			
	 Map map= new HashMap();

	 iter= part.sets().iterator();
	 while (iter.hasNext())
	 {
	    Iterator subIter= ((Set)iter.next()).iterator();

	    State first= (State)subIter.next();

	    map.put(first,first);

	    while (subIter.hasNext())
	    {
	       State next= (State)subIter.next();
	       states.remove(next);
	       map.put(next,first);

	    }
	 }

	 short id=0;
			
	 iter= states.iterator();
	 while (iter.hasNext())
	 {
	    State next= (State)iter.next();
	    next.setId(id++);
	    next.compress(map);
	 }

      }

      /**
       * generates warnings corresponding to the conflicts in the grammar
       * @return true if there is any conflict
       *         false otherwise
       */
      private boolean WarningConflicts()
      {
	 Iterator iter = states.iterator();
	 boolean areConflicts=false;
	 while (iter.hasNext())
	 {
	    State state = (State)iter.next();

	    Collection conflicts = state.conflicts();

	    Iterator confIter = conflicts.iterator();
	    while (confIter.hasNext())
	    {
	       areConflicts=true;
	       unit.warning("Warning: In state " + state.getId() + " " + confIter.next(), 1);
	    }
	 }
	 return areConflicts;
      }

      /**
       * generates warnings corresponding to the conflicts in the grammar
       * @return true if there is any conflict
       *         false otherwise
       */
      private void dumpConflicts(PrintWriter out)
      {
	 Iterator iter = states.iterator();
	 while (iter.hasNext())
	 {
	    State state = (State)iter.next();

	    Collection conflicts = state.conflicts();

	    Iterator confIter = conflicts.iterator();
	    while (confIter.hasNext())
	    {
	       out.println("In state " + state.getId() + " " + confIter.next());
	    }
	 }
      }
      
      /**
       * generates verbose information about the automata
       * if the user enabled the option
       */
      private void doVerbose()
      {

	 boolean areConflicts= WarningConflicts();
		   
	 try
	 {
		      
	    String verboseFile=unit.verboseFile();
		      
	    if (verboseFile!=null)
	    {
	       PrintWriter verb=
		  new PrintWriter(
		     new BufferedWriter(
			new FileWriter(verboseFile)));

	       verb.println("Symbols:");
	       verb.println("");
	       grammar.dumpSymbols(verb);

	       verb.println("");
					
	       verb.println("Rules:");
	       verb.println("");

	       Iterator iter=grammar.iterator();

	       while (iter.hasNext())
	       {
		  verb.println(iter.next());
	       }

	       verb.println("");
	       if (areConflicts)
	       {
		  verb.println("Conflicts:");
		  dumpConflicts(verb);
		  verb.println("");
	       }
	       
	       verb.println("Automata States:");
	       verb.println("");

	       iter=states.iterator();

	       while (iter.hasNext())
	       {
		  State next = (State)iter.next();
		  verb.println(next);						 
	       }
	       verb.close();
					
	    }
	 }
	 catch (Exception exception)
	 {
	 }
      }
		
      /**
       * Converts this object to a human readable string String
       * @return the human readable version of the automata
       */  
      public String toString ()
      {
	 String res = "";
    
	 for (int i=0;i< states.size ();i++)
	 {
	    res+=states.get(i).toString();
	 }
	 return res;
    
      }
}

