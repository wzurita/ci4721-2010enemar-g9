@package
@imports

@declaration
{

   @usercode

   @parsemethods
      
      /**
       *runs the grammar over an input file, or the standard input
       *@param args the initial arguments of the program, if empty,
       * the standard input will be parsed.
       */
      public static void main(String args[]) throws Exception
      {
	 try
	 {
	    @classname parser = new @classname();
	    int initial=0;
	    java.io.Reader in=new java.io.InputStreamReader(System.in);
	    for (int i=0;i< args.length; i++)
	    {
	       try
	       {
		  initial= java.lang.Integer.parseInt(args[i]);
	       }
	       catch (java.lang.NumberFormatException e)
	       {
		  in=new java.io.FileReader(args[i]);
	       }
	    }

	    parser.parse(new ve.usb.Claire.runtime.java.ClaireReader(in),initial);
	 }
	 catch (ve.usb.Claire.runtime.java.LanguageException e)
	 {
	    System.err.println(e.getMessage());
	 }
      }
   

   private static final java.lang.String[] yyRawTable= @table;
   private static final java.lang.String[] yyCheckTable=@check;

   /**
    * the data pool containing all the necesary data
    */
   private static final ve.usb.Claire.runtime.java.DataPool yyTable =
      new ve.usb.Claire.runtime.java.DataPool(
	 yyRawTable,
	 yyCheckTable,
	 @gziped);


   public int yyGetAction(int state,int symbol)
      {

	 // if the symbol is EOF, and the state does not accept the EOF,
	 // then return an error
	 if (symbol == EOF && !yyTable.getBit(@EOFIndex , state))
	    return 0;
	 
	 // else, search for the action table for the state
	 int index= yyTable.getInt(@actionIndex, state);

	 // and get the action for the symbol
	 return yyTable.getShort(index,symbol);
      };

   public int yyDefaultReduce(int state)
      {
	 // if the state does not have a default reduce, the return
	 // an error
	 
	 if (!yyTable.getBit(@defaultReduceIndex , state))
	    return 0;

	 // else, we have a default reduce, just return the default reduce index,
	 // wich is stored in the action index array
	 return yyTable.getInt(@actionIndex, state);
      }
   
   /**
    *gets the next token
    *@param state the state where the LR(1) automata is
    *@param in the input file 
    */
   public ve.usb.Claire.runtime.java.Symbol yyNextToken(int state, ve.usb.Claire.runtime.java.ClaireReader in) throws ve.usb.Claire.runtime.java.LexerException, java.io.IOException
      {
	
	 return ve.usb.Claire.runtime.java.Algorithm.getToken(this, yyTable.getShort(@initialLexerIndex, state, @initialLexerCheck)-1, in);
      }

   
   public int yyReduced(int sintacticState, int lexicalState)
      {
	 // first find where the lexer is for this sintactic state
	 int lexerIndex=yyTable.getInt(@lexerIndex, lexicalState*2, @lexerCheck);
	 int checkIndex=yyTable.getInt(@lexerIndex, lexicalState*2+1, @lexerCheck);

	 // then we find the reduce symbol for the lexical state
	 return yyTable.getShort(lexerIndex, 7 , checkIndex)-1;
      }

   public int yyTransition(int sintacticState, int lexicalState, char nextChar)
      {
	 // first find where the lexer is for this sintactic state
	 int lexerIndex=yyTable.getInt(@lexerIndex, lexicalState*2, @lexerCheck);	 
	 int checkIndex=yyTable.getInt(@lexerIndex, lexicalState*2+1, @lexerCheck);

	 // now find where the domain is, and it's size
	 int domainIndex=yyTable.getInt(lexerIndex,0, checkIndex);
	 int domainCheck=yyTable.getShort(lexerIndex,2,checkIndex);
	 int domainSize=yyTable.getChar(lexerIndex,3,checkIndex);
	 
	 // try to find where the character fits in the domain
	 int category = yyTable.binarySearch(nextChar, domainIndex, domainSize, domainCheck);

	 // find where the range for the lexical state is
	 int rangeIndex=yyTable.getInt(lexerIndex,2,checkIndex);
	 int rangeCheck=yyTable.getShort(lexerIndex,6,checkIndex);

	 // finaly, ubicate the category in the range, and return the jumping result	 
	 return yyTable.getShort(rangeIndex, category, rangeCheck)-1;
      }

   public int yyGetGoto(int state,int symbol)
      {
	 // find where the goto table for this simbol is
	 int index= yyTable.getInt(@gotoIndex, state);

	 // now, find the goto transition using the given symbol
	 
	 return yyTable.getShort(index,symbol);
      }

   // return the length of a given rule
   public int yyLength(int rule)
      {
	 // find the length of the rule in the table
	 return yyTable.getShort(@ruleLengthIndex, rule);
      }
   
   // return the lefthand of a given rule
   public int yyLeftHand(int rule)
      {
	 // find the left hand of the rule in the table
	 return yyTable.getShort(@leftHandIndex, rule);
      }
   
   public ve.usb.Claire.runtime.java.Symbol yyDoAction(int yyRule,java.util.Stack yyValueStack) throws Exception
      {
	 // according to the rule, do an action
	 switch(yyRule)
	 {
	    @actions
         }
	 return new ve.usb.Claire.runtime.java.DefaultValueSymbol();
      }
}
