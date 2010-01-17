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
 * $Id: Token.java,v 1.1 1999/11/07 11:53:25 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: Token.java,v $
 * Revision 1.1  1999/11/07 11:53:25  Paul
 * Copied the java runtime classes inside this package
 *
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 * Revision 1.2  1999/09/09 09:53:55  Paul
 * Added some cvs comments
 *
 */
package ve.usb.Claire.runtime.java;

/**
 * Symbol that represents a token in the algorithm
 * @version     $Revision: 1.1 $
 * @author      Paul Pacheco
 * @since       JDK1.1
 * @see NamedSymbol
 */

public class Token extends ObjectSymbol
{		

		/**
		 * Creates a token with null value
		 * @param id the identifier for the token
		 */
		public Token(int id)
		{
			this(id,null);
		}

		/**
		 * Creates a token with a suplied value
		 * @param id the identifier for the token
		 * @param data the value of the token
		 */
		public Token(int id,Object data)
		{
			this(id,data,0,0);
		}

		/**
		 * Creates a token with suplied position
		 * @param id the identifier for the token
		 * @param lineStart the line at where the token starts
		 * @param colStart the column at where the token starts
		 */
		public Token(int id,int lineStart, int colStart)
		{
			this(id,null,lineStart,colStart);
		}
		
		/**
		 * Creates a token with suplied position
		 * @param id the identifier for the token
		 * @param data the value of the token
		 * @param lineStart the line at where the token starts
		 * @param colStart the column at where the token starts
		 */
		public Token(int id,Object data,int lineStart, int colStart)
		{
			this(id,data,lineStart,colStart,0);
			
		}
		
		/**
		 * Creates a token with suplied position
		 * @param id the identifier for the token
		 * @param data the value of the token
		 * @param lineStart the line at where the token starts
		 * @param colStart the column at where the token starts
		 * @param charStart the character position at where the token starts
		 */
		public Token(int id,Object data,int lineStart, int colStart,int charStart)
		{
			this(id, data, lineStart, colStart, charStart, lineStart, colStart, charStart);
		}

		/**
		 * Creates a token with suplied position
		 * @param id the identifier for the token
		 * @param data the value of the token
		 * @param lineStart the line at where the token starts
		 * @param colStart the column at where the token starts
		 * @param charStart the character position at where the token starts
		 * @param lineEnd the line at where the token ends
		 * @param colEnd the column at where the token ends
		 * @param charEnd the character position at where the token ends
		 */
		public Token(int id, Object data, int lineStart, int colStart, int charStart, int lineEnd, int colEnd, int charEnd)
		{
			super(data);
			id(id);
			
			lineStart(lineStart);
			colStart(colStart);
			charStart(charStart);

			lineEnd(lineEnd);
			colEnd(colEnd);
			charEnd(charEnd);
		}

		/**
		 * converts this token to string
		 */
		public String toString()
		{
			if (ObjectValue() == null)
				return "EOF";
			
					
			return ObjectValue()+ "";
		}

}		
