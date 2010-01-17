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
 * $Id: CharBuffer.java,v 1.1 1999/11/07 11:53:25 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: CharBuffer.java,v $
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
import java.io.*;

/**
 * Handles a buffer of characters, this buffer is needed to allow the reader
 * to go back.
 * @version     $Revision: 1.1 $
 * @author      Paul Pacheco
 * @since       JDK1.1
 */
final class CharBuffer
{

		/**
		 * Default buffer size
		 */
		public static final int ARRAYSIZE=100;
		
		/**
		 * buffer of characters
		 */
		private char[] array;

		/**
		 * Position at where the buffer starts
		 */
		private int startPointer=0;

		/**
		 * Position at where the buffer ends
		 */
		private int endPointer=0;

		/**
		 * amount of characters in buffer
		 */
		private int count = 0;

		/**
		 * Position of the next character to be read from the buffer
		 * relative to the start of the buffer
		 */
		private int nextRelPointer=0;

		/**
		 * Creates a character buffer with the default buffer size
		 */
		public CharBuffer()
		{
			this(ARRAYSIZE);
		}

		/**
		 * Creates a character buffer with the suplied buffer size
		 * @param size the size of the buffer
		 */
		public CharBuffer(int size)
		{
			array= new char[size];
		}

		/**
		 * Makes the buffer bigger
		 * @param size the new size of the buffer
		 */
		private void enlargeBuffer(int size)
		{
			char [] oldArray= array;
			array = new char[size];

			if (count == 0)
				return;
			
			if (endPointer > startPointer)
			{
				System.arraycopy(oldArray, startPointer, array, 0, count);
			}
			else
			{
				System.arraycopy(oldArray, startPointer, array, 0, oldArray.length - startPointer);
				System.arraycopy(oldArray, 0, array, oldArray.length - startPointer, endPointer);				
			}

			startPointer =0;
			endPointer=startPointer+count;
		}

		/**
		 * Adds a character to the buffer
		 */
		public void add(char ch)
		{
			if (count == array.length)
				enlargeBuffer(array.length + ARRAYSIZE);

			array[endPointer++]=ch;
			count++;
			
			if (endPointer == array.length)
				endPointer=0;
		}

		/**
		 * Marks the current position
		 * After the buffer is marked, a reset will make de buffer
		 * go back to the last mark
		 */
		public void mark()
		{
			count -= nextRelPointer;
			startPointer= (startPointer + nextRelPointer) % array.length;
			nextRelPointer=0;
		}
		
		/**
		 * Moves the buffer back to its last mark
		 */
		public void reset()
		{
			nextRelPointer=0;
		}

		/**
		 * reads a character from the buffer
		 * @return the last character in the buffer
		 *         -1 if the buffer is empty
		 */
		public int read()
		{
			if (nextRelPointer == count)
				return -1;
			
			return array[((nextRelPointer++) + startPointer) % array.length];
		}
			
}
