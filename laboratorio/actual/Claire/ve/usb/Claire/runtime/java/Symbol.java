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
 * $Id: Symbol.java,v 1.1 1999/11/07 11:53:25 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: Symbol.java,v $
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
 * Represents a symbol at runtime.
 * @version     $Revision: 1.1 $
 * @author      Paul Pacheco
 * @since       JDK1.1
 */

public abstract class Symbol
{
		/**
		 * the id of the symbol
		 */
		private int id;

		/**
		 * the id of the sintactic state where the symbol is
		 */
		private int stateId;

		/**
		 * the character at where the symbol starts
		 */
		private int charStart;

		/**
		 * the character at where the symbol ends
		 */
		private int charEnd;

		/**
		 * the column at where the symbol starts
		 */
		private int colStart;

		/**
		 * the column at where the symbol ends
		 */
		private int colEnd;

		/**
		 * the line at where the symbol starts
		 */
		private int lineStart;

		/**
		 * the line at where the symbol ends
		 */
		private int lineEnd;
				

		/**
		 * creates a new symbol
		 */
		public Symbol ()
		{
		}

		/**
		 * the identifier for the state where the symbol is
		 * @return the id of the sintactic state
		 */
		int stateId()
		{
			return this.stateId;
		}

		/**
		 * sets the identifier for the sintactic state where the symbol is
		 * @param stateId the id for the state
		 */
		void stateId( int stateId)
		{
			this.stateId=stateId;
		}
		
		/**
		 * gets the identifier of the symbol
		 * @return the identifier of the symbol
		 */
		final int id()
		{
			return id;
		}

		/**
		 * sets the identifier of the symbol
		 * @param id the identifier of the symbol
		 */
		public final void id(int id)
		{
			this.id=id;
		}

		/**
		 * gets the line at where the symbol starts
		 * @return the line
		 */
		public final int lineStart()
		{
			return this.lineStart;
		}

		/**
		 * sets the line at where the symbol starts
		 * @param start the line
		 */
		final void lineStart(int start)
		{
			this.lineStart=start;
		}

		/**
		 * gets the line at where the symbol ends
		 * @return the line
		 */
		public final int lineEnd()
		{
			return this.lineEnd;
		}

		/**
		 * sets the line at where the symbol ends
		 * @param end the line
		 */
		final void lineEnd(int end)
		{
			this.lineEnd=end;
		}

		/**
		 * gets the column at where the symbol starts
		 * @return the column
		 */
		public final int colStart()
		{
			return this.colStart;
		}

		/**
		 * sets the column at where the symbol starts
		 * @param start the column
		 */
		final void colStart(int start)
		{
			this.colStart=start;
		}

		/**
		 * gets the column at where the symbol ends
		 * @return the column
		 */
		public final int colEnd()
		{
			return this.colEnd;
		}

		/**
		 * sets the column at where the symbol ends
		 * @param end the column
		 */
		final void colEnd(int end)
		{
			this.colEnd=end;
		}

		/**
		 * gets the position at where the symbol starts
		 * @return the position
		 */
		public final int charStart()
		{
			return this.charStart;
		}

		/**
		 * sets the position at where the symbol starts
		 * @param start the position
		 */
		final void charStart(int start)
		{
			this.charStart=start;
		}

		/**
		 * gets the position at where the symbol ends
		 * @return the position
		 */
		public final int charEnd()
		{
			return this.charEnd;
		}

		/**
		 * sets the position at where the symbol ends
		 * @param end the position
		 */
		final void charEnd(int end)
		{
			this.charEnd=end;
		}

		/**
		 * the boolean sintactic value of the symbol
		 * @return the boolean value
		 */		
		public boolean booleanValue()
		{
			throw new ClassCastException("can not cast to boolean");
		}
		
		/**
		 * the byte sintactic value of the symbol
		 * @return the byte value
		 */		
		public byte byteValue()
		{
			throw new ClassCastException("can not cast to byte");
		}

		/**
		 * the char sintactic value of the symbol
		 * @return the char value
		 */		
		public char charValue()
		{
			throw new ClassCastException("can not cast to char");
		}
		
		/**
		 * the double sintactic value of the symbol
		 * @return the double value
		 */		
		public double doubleValue()
		{
			throw new ClassCastException("can not cast to double");
		}

		/**
		 * the float sintactic value of the symbol
		 * @return the float value
		 */		
		public float floatValue()
		{
			throw new ClassCastException("can not cast to float");
		}

		/**
		 * the int sintactic value of the symbol
		 * @return the int value
		 */		
		public int intValue()
		{
			throw new ClassCastException("can not cast to int");
		}

		/**
		 * the long sintactic value of the symbol
		 * @return the long value
		 */		
		public long longValue()
		{
			throw new ClassCastException("can not cast to long");
		}

		/**
		 * the short sintactic value of the symbol
		 * @return the short value
		 */		
		public short shortValue()
		{
			throw new ClassCastException("can not cast to short");
		}

		/**
		 * the object sintactic value of the symbol
		 * @return the object value
		 */		
		public Object ObjectValue()
		{
			throw new ClassCastException("can not cast to an Object");
		}
		
}		
