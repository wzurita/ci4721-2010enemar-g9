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
 * $Id: Table.java,v 1.4 1999/11/21 02:12:40 Paul Exp $
 *
 * The changes to this file are:
 *
 * $Log: Table.java,v $
 * Revision 1.4  1999/11/21 02:12:40  Paul
 * Dramatically reduced the size of the generated tables (suggested by Ascander Suarez)
 *
 * Revision 1.3  1999/11/07 14:14:08  Paul
 * Moved the java runtime library to another package.
 *
 * Revision 1.2  1999/11/06 01:43:44  Paul
 * Improved the table generation
 *
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 * Revision 1.2  1999/09/09 09:53:55  Paul
 * Added some cvs comments
 *
 */

package ve.usb.Claire.table;

import java.util.*;
import java.io.*;
import ve.usb.Claire.regexp.Lexer;
import ve.usb.Claire.contextFree.State;
import ve.usb.Claire.util.*;
import ve.usb.Claire.contextFree.symbol.*;
import ve.usb.Claire.contextFree.*;
import ve.usb.Claire.translator.*;
import ve.usb.Claire.regexp.*;
import ve.usb.Claire.runtime.java.Pack;

/**
 * Encapsulates the creation, compression and writting of the parsing tables
 * @version     $Revision: 1.4 $
 * @author      Paul Pacheco
 * @since       JDK1.1
 */
public class Table
{
						
      /**
       * the actual compressed table, contains the actions to be carried out
       */
      private DataPool pool = new RawDataPool();


      /**
       * the actual compressed table, contains the actions to be carried out
       */
      public DataPool pool()
      {
	 return pool;
      }
      
      /**
       * the pool containing the check arrays
       */
      private DataPool check = new RawDataPool();

      /**
       * the pool containing the check arrays
       */
      public DataPool checkPool()
      {
	 return check;
      }

      /**
       * the compressed table. This pool also uses a check mecanism.
       * @see VerifiedDataPool
       */
      private DataPool verifiedPool = new VerifiedDataPool(pool, check);		

      private int initialLexerIndex;
      private short initialLexerCheck;
      
      /**
       * the indexes of the lexer per sintactic state
       */

      private int lexerIndex;

      /**
       * the check index of the lexer table
       */

      private short lexerCheck;


      /**
       * the indexes of the lexer per sintactic state
       */
      public int lexerIndex()
      {
	 return lexerIndex;
      }

      /**
       * the check index of the lexer table
       */
      public short lexerCheck()
      {
	 return lexerCheck;
      }
      /**
       * the indexes of the lexer per sintactic state
       */
      public int initialLexerIndex()
      {
	 return initialLexerIndex;
      }


      /**
       * the check index of the lexer table
       */
      public short initialLexerCheck()
      {
	 return initialLexerCheck;
      }
      
      /**
       * indexes for the shift and reduce. The actual states are located in shiftIndex[state]+symbol
       * in the array table
       */
      private int actionIndex;

      /**
       * indexes for the shift and reduce. The actual states are located in shiftIndex[state]+symbol
       * in the array table
       */
      public int actionIndex()
      {
	 return actionIndex;
      }
      
      /**
       * indexes for the goto. The actual states are located in gotoIndex[state]+symbol
       * in the array table
       */
      private int gotoIndex;

      /**
       * indexes for the goto. The actual states are located in gotoIndex[state]+symbol
       * in the array table
       */

      public int gotoIndex()
      {
	 return gotoIndex;
      }
      
      /**
       * table for the default reduce. Some states have a default reduce, and this
       * compresses the tables a lot
       */
      private short defaultReduceIndex;


      /**
       * table for the default reduce. Some states have a default reduce, and this
       * compresses the tables a lot
       */
      public short defaultReduceIndex()
      {
	 return defaultReduceIndex;
      }

      /**
       * table for the EOF acceptance. It is a bit array inside the check pool
       * that contains true if an state accepts EOF
       */
      private short EOFIndex;

      /**
       * table for the default reduce. Some states have a default reduce, and this
       * compresses the tables a lot
       */
      public short EOFIndex()
      {
	 return EOFIndex;
      }
      
      /**
       * the left hands for each production
       */
		
      private int leftHands;

      /**
       * the left hands for each production
       */
      public int leftHandsIndex()
      {
	 return leftHands;
      }
      
      /**
       * the lenth of each production
       */
      private int ruleLengthIndex;

      /**
       * the lenth of each production
       */
      public int ruleLengthIndex()
      {
	 return ruleLengthIndex;
      }
		
      /**
       * Creates the tables.
       * @param states the list of states of the automata
       * @see State
       */
      public Table(Grammar grammar, Automata automata)
      {
	 calcGrammar(grammar);

	 List sorted=new ArrayList();
	 sorted.addAll(automata.states());
	 Collections.sort(sorted);
			
	 compressLexers(sorted);
	 compress(sorted);
      }


      /**
       * Calculates properties of the grammar like the left hands and
       * the length of each production
       * @param grammar the grammar that contains the information
       */
      private void calcGrammar(Grammar grammar)
      {
	 short leftHands[]= new short[grammar.size()];
	 short ruleLengthIndex[] = new short[grammar.size()];
			
	 Iterator iter = grammar.iterator();
			
	 while (iter.hasNext())
	 {
	    Rule next = (Rule)iter.next();
				
	    leftHands[next.getId()]= (short)next.getLeftHand().getId();
	    ruleLengthIndex[next.getId()] = (short)next.size();
	 }

	 this.leftHands= (int)pool.ubicate(leftHands);
	 this.ruleLengthIndex=(int)pool.ubicate(ruleLengthIndex);
			
      }
				
      /**
       * turns on a bit in a byte array
       */
      private static void setBit(byte array[], int index)
      {
	 array[index / 8] |= (byte)(1 << (index % 8));
      }
      
      /**
       * performs the compression for the states in the grammar
       * calculates the shift, reduce, and goto indexes, and the table
       * @param states the list of states of the automata
       */
      private void compress(Collection states)
      {
	 int actionIndex[]=new int[states.size()];
	 int gotoIndex[]=new int[states.size()];
	 
	 byte acceptEOF[] = new byte[(states.size()+7) / 8];
	 byte hasDefaultReduce[]=new byte[(states.size()+7) /8];
	 
	 Iterator iter=states.iterator();
			
	 while (iter.hasNext())
	 {
	    State next=(State)iter.next();

	    if (next.hasDefaultReduce())
	    {
	       setBit(hasDefaultReduce,next.getId());
	       actionIndex[next.getId()]=- next.defaultReduceId();
	    }
	    else
	    {
	       actionIndex[next.getId()]=ubicate(next,next.terminalSymbols());
	       if (next.acceptEOF())
		  setBit(acceptEOF, next.getId());
	    }
	    							
	    gotoIndex[next.getId()]=ubicate(next,next.gotoSymbols());
	 }

	 this.actionIndex=(int)pool.ubicate(actionIndex);
	 this.gotoIndex=(int)pool.ubicate(gotoIndex);

	 this.EOFIndex=(short)check.ubicate(acceptEOF);
	 this.defaultReduceIndex=(short)check.ubicate(hasDefaultReduce);

      }		

      /**
       * stores the lexers into the pool
       * @param states the sintactic states that contains the lexers
       */
      void compressLexers(Collection states)
      {
			
	 short initialLexerIndex[]= new short[states.size()];
			
	 Lexer lexer = new Lexer();
	 
	 // now try the lexicographical rules
			
	 Iterator iter= states.iterator();

			
	 while (iter.hasNext())
	 {
	    State next= (State)iter.next();

	    if (next.hasDefaultReduce())
	       lexer.put(next.getId(), Collections.EMPTY_SET);
	    else
	    {
	    
	       Set possibleLexer= new HashSet();
	       
	       Iterator possibleIter= next.symbols().iterator();
	       
	       while (possibleIter.hasNext())
	       {
		  Symbol sym = (Symbol)possibleIter.next();
		  
		  RE regexp= sym.regexp();
		  
		  if (regexp!=null)
		     possibleLexer.add(regexp);
	       }

	       lexer.put(next.getId(), possibleLexer);
	    }
	 }
	 
	 long pos = lexer.table(verifiedPool, initialLexerIndex);

	 long initialpos = verifiedPool.ubicate(initialLexerIndex);
	 
	 this.lexerIndex=Pack.first(pos);
	 this.lexerCheck=(short)Pack.second(pos);

	 this.initialLexerIndex=Pack.first(initialpos);
	 this.initialLexerCheck=(short)Pack.second(initialpos);
      }

      /**
       * Gets the regular expressions of the symbols
       * @param symbols the symbols that contain the regular expresions
       * @return the list of regular expressions.
       */
      private static Collection regexps(Collection symbols)
      {
	 List result= new ArrayList();

	 Iterator iter= symbols.iterator();
	 while (iter.hasNext())
	 {
	    Symbol sym= (Symbol)iter.next();
	    result.add(sym.regexp());
	 }
	 return result;
      }

      /**
       * gets the action to be performed by a state under a symbol
       * @param state the state that contains the action
       * @param sym the symbol under where the action is to be taken
       * @return the integer representing the action to be carried out.
       * negative for reduce, positive for shift or goto
       */
      private short getAction(State state, Symbol sym)
      {
	 State next=state.getTransition(sym);
			
	 // si no tiene destino, es un reduce
	 if (next==null)
	 {
				// if it is a reduce, then make it negative
	    Rule prod=state.getReduction(sym);
	    return (short)-prod.getId();
	 }
	 else
				// otherwise, a shift or goto, make it positive
	    return next.getId();
      }
		
		
      /**
       * Given a set of symbols calculates the actions for a given state
       * and store them in arrays.
       * @param state the state that contains the actions
       * @param symbols the symbols whose actions need to be calculated
       * @param actions receives the actions to be used
       * @param indexes receives the indexes of the actions
       */
      void prepareBuffer(State state, Collection symbols, short actions[], int indexes[])
      {
	 Iterator iter = symbols.iterator();

	 int pos =0;

	 while (iter.hasNext())
	 {
	    Symbol sym = (Symbol)iter.next();

	    short action = getAction(state, sym);
											  
	    indexes[pos] = sym.getId();
	    actions[pos] = action;
				
	    pos++;
	 }

      }
		
      /**
       * ubicates a set of actions performed under a state
       * @param state the state that is to carry out the actions
       * @param symbols the set of symbols that represent the actions
       */
		
      private int ubicate(State state, Collection symbols)
      {

	 int position=  0;
			
	 if (symbols.isEmpty())
	    return 0;
			
	 int [] indexes = new int[symbols.size()];
	 short [] actions = new short[symbols.size()];
			
	 prepareBuffer(state,symbols, actions, indexes);
			
	 return (int)pool.ubicate(actions,indexes);
      }	  		
		
		
      /**
       * writes the tables to a file
       * @param out the file to where the tables are to be written
       * @param options the options for the grammar
       */
      /*public void emit(Translator trans)
      {
	 trans.writeArray("yyActionIndex", actionIndex);
	 trans.writeArray("yyGotoIndex", gotoIndex);

	 trans.writeArray("yyLexerIndex", lexerIndex);
	 trans.writeArray("yyLexerCheck", lexerCheck);

	 pool.emit("yyTable", trans);

	 trans.writeArray("yyLength", ruleLengthIndex);
	 trans.writeArray("yyLeftHand", leftHands);
			
	 trans.writeConstant("YYDEFAULTREDUCECONSTANT", defaultReduceIndex);
	 }*/
}					
