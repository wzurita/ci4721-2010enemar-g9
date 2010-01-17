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
 * $Id: Algorithm.java,v 1.2 1999/11/21 02:12:39 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: Algorithm.java,v $
 * Revision 1.2  1999/11/21 02:12:39  Paul
 * Dramatically reduced the size of the generated tables (suggested by Ascander Suarez)
 *
 * Revision 1.1  1999/11/07 11:53:25  Paul
 * Copied the java runtime classes inside this package
 *
 * Revision 1.4  1999/11/04 05:37:33  Paul
 * Improved expansion of alias inside Regular Expressions and Code
 *
 * Revision 1.3  1999/11/01 11:30:21  Paul
 * *** empty log message ***
 *
 * Revision 1.2  1999/11/01 09:47:11  Paul
 * Starting to add exception suport
 *
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 * Revision 1.3  1999/10/12 04:16:41  Paul
 * *** empty log message ***
 *
 * Revision 1.2  1999/09/09 09:53:55  Paul
 * Added some cvs comments
 *
 */
package ve.usb.Claire.runtime.java;
import java.util.*;
import java.io.*;
import java.util.zip.*;

/**
 * Contains the runtime algorithms for the generated parser.
 * This class contains the LR(1) recognizing algorithm as well as the
 * DFA algorithm.
 * @version     $Revision: 1.2 $
 * @author      Maria Eugenia Ahues
 * @author      Bernardo Munoz
 * @author      Rui Santos
 * @since       JDK1.1
 */
public abstract class Algorithm
{

			
      /**
       * using a claire scanner, this method contains the DFA algorithm necesary
       * to recognize tokens
       * @param scanner the scanner to be used
       * @param sintacticState the sintactic state where the LR(1) automata is
       * @param in the stream from where characters are to be readed
       * @return the recognized symbol
       * @throws IOException if there is an error reading the stream
       * @throws LexerException if no token is recognized
       */
      public static Symbol nextToken(ClaireScanner scanner,  int sintacticState, ClaireReader in) throws IOException, LexerException
      {
	 int lexicalState= 0;
	 int lastTerminal=-1;

	 String terminal=null;
			
	 StringBuffer acummulated=new StringBuffer();

	 int lastMarkIndex=0;
			
	 in.mark();
			
	 int line = in.line();
	 int col = in.col();
	 int pos = in.pos();
			
	 //System.out.println("Scanning from " + lexerStart);
			
			
	 while(true)
	 {
				//System.out.println("from " + lexicalState);
				
	    int reduced= scanner.yyReduced(sintacticState, lexicalState);
				
	    if (reduced >= 0)
	    {
	       lastTerminal=reduced;
	       terminal=acummulated.toString();
	       in.mark();
	    }

	    int nextChar= in.read();

				
				//System.out.println("State: " + lexicalState + " getting "+ (char)nextChar);

				
				
	    if (nextChar == -1)
	    { 					
	       in.reset();
						
	       if (lastTerminal==-1)
	       {
		  if (acummulated.length()==0)
		     return new Token(Language.EOF, null, line,col,pos, line, col,pos);
		  else
		     throw new LexerException("Unexpected end of file while recognizing", line, col, pos);
	       }
	       else
		  return new Token(lastTerminal,terminal);
	    }

	    acummulated.append((char)nextChar);
				
	    lexicalState = scanner.yyTransition(sintacticState, lexicalState, (char)nextChar);
				
	    if (lexicalState == -1)
	    {
	       in.reset();

	       if (lastTerminal==-1)
		  throw new LexerException("Unexpected character "+ nextChar, line, col, pos);
	       else
		  return new Token(lastTerminal,terminal, line, col, pos, in.line(), in.col(), in.pos());
	    }
	 }
      }

      /**
       * using a claire scanner, this method contains the DFA algorithm necesary
       * to recognize tokens
       * @param scanner the scanner to be used
       * @param sintacticState the sintactic state where the LR(1) automata is
       * @param in the stream from where characters are to be readed
       * @return the recognized symbol
       * @throws IOException if there is an error reading the stream
       * @throws LexerException if no token is recognized
       */
      public static Symbol getToken(ClaireScanner scanner,  int lexicalState, ClaireReader in) throws IOException, LexerException
      {
	 int lastTerminal=-1;

	 String terminal=null;
			
	 StringBuffer acummulated=new StringBuffer();

	 int lastMarkIndex=0;
			
	 in.mark();
			
	 int line = in.line();
	 int col = in.col();
	 int pos = in.pos();
			
	 //System.out.println("Scanning from " + lexicalState);
			
			
	 while(true)
	 {
				//System.out.println("from " + lexicalState);
				
	    int reduced= scanner.yyReduced(0, lexicalState);
				
	    if (reduced >= 0)
	    {
	       lastTerminal=reduced;
	       terminal=acummulated.toString();
	       in.mark();
	    }

	    int nextChar= in.read();

				
				//System.out.println("State: " + lexicalState + " getting "+ (char)nextChar);

				
				
	    if (nextChar == -1)
	    { 					
	       in.reset();
						
	       if (lastTerminal==-1)
	       {
		  if (acummulated.length()==0)
		     return new Token(Language.EOF, null, line,col,pos, line, col,pos);
		  else
		     throw new LexerException("Unexpected end of file while recognizing", line, col, pos);
	       }
	       else
		  return new Token(lastTerminal,terminal);
	    }

	    acummulated.append((char)nextChar);
				
	    lexicalState = scanner.yyTransition(0, lexicalState, (char)nextChar);
				
	    if (lexicalState == -1)
	    {
	       in.reset();

	       if (lastTerminal==-1)
		  throw new LexerException("Unexpected character "+ nextChar, line, col, pos);
	       else
		  return new Token(lastTerminal,terminal, line, col, pos, in.line(), in.col(), in.pos());
	    }
	 }
      }

		
      /**
		 * Parses a stream using the desired language
		 * @param lang the parser and scanner necesary to parse the stream
		 * @param initialState the initial sintactic state. This is necesary to allow
		 *        starting the automata from diffrent positions
		 * @return Symbol the symbol containing the resulting data after parsing the stream
		 * @throws ParserException if there is a sintactic error. If the scanner is generated
		 *         by Claire, the only sintactic error there can be is unexpected eof
		 * @throws LexerException if the input is not recognized by any token
		 * @throws initialState the initial state of the automata
		 */
      public static Symbol parse (Language lang, ClaireReader in, int initialState) throws ParserException, LexerException, Exception
      {
	 return parse(lang, lang, in, initialState);
      }
		
		
      /**
		 * Parses a stream using the desired language
		 * @param scanner the scanner necesary to parse the stream
		 * @param parser the parser necesary to parse the tokens returned by the scanner
		 * @param initialState the initial sintactic state. This is necesary to allow
		 *        starting the automata from diffrent positions
		 * @return Symbol the symbol containing the resulting data after parsing the stream
		 * @throws ParserException if there is a sintactic error. If the scanner is generated
		 *         by Claire, the only sintactic error there can be is unexpected eof
		 * @throws LexerException if the input is not recognized by any token
		 * @throws initialState the initial state of the automata
		 */
      public static Symbol parse (Scanner scanner, Parser parser, ClaireReader in, int initialState) throws ParserException, LexerException, Exception
      {
			
	 Stack stack=new Stack();
	 
	 Symbol initial = new intSymbol(0);
	 
	 initial.stateId(initialState);
	 
	 stack.push(initial);
	 
	 Symbol token=null;
	 
	 
	 while (true)
	 {
	    
	    
	    
	    int state=((Symbol)stack.peek()).stateId();
	    
				//System.out.println("s: "+state);
	    
	    int action= parser.yyDefaultReduce(state);
				
				
	    if (action >= 0)
	    {
	       if (token == null)
	       {
		  token= scanner.yyNextToken(state,in);

		  //System.out.println("token: "+ token + " id " + token.id());
	       }					
					
	       action = parser.yyGetAction(state,token.id());
	       //System.out.println(action);
	    }
					
				// the positive actions are all shift. and the number is the new
				// state to shift to
	    if (action > 0)
	    {

	       //state_stack.push (token);

	       if (token.id() == Parser.EOF)
		  return (Symbol)stack.peek();
					
	       token.stateId(action);

	       stack.push(token);

	       token=null; 

					
	    }
	  
				// the negative actions are all reduce, and the negative number
				// is the rule to be reduced
	    else if (action < 0)
	    {
	      
	       int prod_number = -action;

	       int prod_length = parser.yyLength(prod_number);

	       int left_hand=parser.yyLeftHand(prod_number);
					
	       //System.out.println ("Reducing by: " + prd_number);

	       Symbol new_value=null;
	       
	       new_value=parser.yyDoAction(prod_number,stack);

	       Symbol lastSymbol= (Symbol)stack.peek();
					
	       if (prod_length == 0)
	       {
		  if (token != null)
		  {
		     new_value.lineEnd(token.lineStart());
		     new_value.colEnd(token.colStart());
		     new_value.charEnd(token.charStart());
		  }
		  else
		  {
		     new_value.lineEnd(in.line());
		     new_value.colEnd(in.col());
		     new_value.charEnd(in.pos());
		  }

		  new_value.lineStart(new_value.lineEnd());
		  new_value.colStart(new_value.colEnd());
		  new_value.charStart(new_value.charEnd());
	       }
	       else
	       {
		  new_value.lineEnd(lastSymbol.lineEnd());
		  new_value.colEnd(lastSymbol.colEnd());
		  new_value.charEnd(lastSymbol.charEnd());
	       }
					
	       // get the states out of the stack
	       for (int j=0;j<prod_length;j++)
		  lastSymbol = (Symbol)stack.pop();
					

	       if (prod_length > 0)
	       {
		  new_value.lineStart(lastSymbol.lineStart());
		  new_value.colStart (lastSymbol.colStart());
		  new_value.charStart(lastSymbol.charStart());
	       }
					
	       int newstate=parser.yyGetGoto(((Symbol)stack.peek()).stateId(),left_hand);

	       // if there is no goto, then the reduce was an accept
	       if (newstate == 0)
	       {
		  System.err.println("internal error, please contact Paul Pacheco <93-25642@ldc.usb.ve>");
	       }
					

	       new_value.stateId(newstate);
					
	       // else go to the new state
	       stack.push (new_value);
	       //System.out.println ("Go to : "+state.getId ());
	      
	    }
	    else 
	    {
					
	       throw (new ParserException("Syntax Error, unexpected token "+ token, token.lineStart(), token.colStart(), token.charStart() ));
							 
	       /*
		 tuple = new StateSymbol ((State) state_stack.peek (),
		 new Terminal (sym.EOF));
					  
		 action = action_table.getAction (tuple);	  	  
		 if (action==null)
		 throw (new ParserException ("Syntax Error"));
	       */
					
				
	    }
		   
	 }
      }
}
