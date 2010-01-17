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
 * $Id: Conflict.java,v 1.1.1.1 1999/10/31 07:47:02 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: Conflict.java,v $
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 */
package ve.usb.Claire.contextFree;
import java.util.*;
import java.io.*;
import ve.usb.Claire.util.*;
import ve.usb.Claire.*;
import ve.usb.Claire.contextFree.symbol.*;

/**
 * Represents a conflict inside a grammar
 * @Implements java.io.Serializable
 * @version     $Revision: 1.1.1.1 $
 * @author      Paul Pacheco
 * @since       JDK1.2
 * @see Automata
 * @see Item
 */

class Conflict
{
      
      /**
       *the first item in the conflict
       */
      private Item item1;

      /**
       * the second item in the conflict
       */
      private Item item2;

      /**
       * the state where the conflict generated
       */
      private State state;

      /**
       * the symbol under which the conflict generated
       */
      private Symbol symbol;
      

      /**
       * Creates a conflict in a state
       * @param item1 the first conflicting item
       * @param item2 the second conflicting item
       * @param state the sintactic state in where the conflict generated
       * @param symbol the symbol that generates the conflict
       */
      
      Conflict(Item item1, Item item2, State state, Symbol symbol)
      {
	 this.item1 = item1;
	 this.item2 = item2;
	 this.state = state;
	 this.symbol=symbol;
      }

	    
      /**
       * takes a default desition of how to solve the conflict
       * @return 1 if the conflict should be solved in favor of the first item       
       *         2 if the conflict should be solved in favor of the second item
       */
      int solve()
      {
	 if (!item1.last())
	    return 1;
	 else if (item2.last())
	    return 2;
	 else if (item1.getId() < item2.getId())
	    return 1;
	 else
	    return 2;
      }

      /**
       * prints the conflict in a human readable form
       * @return the string representing the conflict
       */
      public String toString()
      {
      	    
	 StringWriter confWriter = new StringWriter();
	 PrintWriter out = new PrintWriter(confWriter);
	 
	 if (item1.last())
	    out.print("REDUCE / ");
	 else
	    out.print("SHIFT / ");

	 if (item2.last())
	    out.print("REDUCE");
	 else
	    out.print("SHIFT");

					
	 out.println(" conflict between rules:");
	 out.print("\t"+item1);
	 out.print("\t"+item2);
						

	 out.print("\tunder the symbol "+symbol.toString()+", solving in favor of ");

	 if (solve() == 1)
	    out.print("the first rule");
	 else
	    out.print("the second rule");
	    
	 out.close();

	 return confWriter.toString();
      }

}


