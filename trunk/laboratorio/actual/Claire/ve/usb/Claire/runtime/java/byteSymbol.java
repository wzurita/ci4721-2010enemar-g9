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
 * $Id: byteSymbol.java,v 1.1 1999/11/07 11:53:25 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: byteSymbol.java,v $
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
 * Creates a sintactic symbol with a short sintactic value
 * @version     $Revision: 1.1 $
 * @author      Paul Pacheco
 * @since       JDK1.1
 */
public class byteSymbol extends Symbol
{

		/**
		 * value for the symbol
		 */
		private byte value;

		/**
		 * Creates a symbol with a short value
		 * @parem value the value of the symbol
		 */
		public byteSymbol(byte value)
		{
			this.value=value;
		}		
		
		/**
		 * the byte sintactic value of the symbol
		 * @return the byte value
		 */		
		public byte byteValue()
		{
			return this.value;
		}

		/**
		 * the char sintactic value of the symbol
		 * @return the char value
		 */		
		public char charValue()
		{
			return (char)value;
		}
		
		/**
		 * the double sintactic value of the symbol
		 * @return the double value
		 */		
		public double doubleValue()
		{
			return (double)value;
		}

		/**
		 * the float sintactic value of the symbol
		 * @return the float value
		 */		
		public float floatValue()
		{
			return (float)value;
		}

		/**
		 * the int sintactic value of the symbol
		 * @return the int value
		 */		
		public int intValue()
		{
			return (int)value;
		}

		/**
		 * the long sintactic value of the symbol
		 * @return the long value
		 */		
		public long longValue()
		{
			return (long)value;
		}

		/**
		 * the short sintactic value of the symbol
		 * @return the short value
		 */		
		public short shortValue()
		{
			return (short)value;
		}
		
}		
