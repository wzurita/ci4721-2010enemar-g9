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
 * $Id: ClaireReader.java,v 1.1 1999/11/07 11:53:25 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: ClaireReader.java,v $
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
 * Implements a markable reader with an infinite buffer
 * @version     $Revision: 1.1 $
 * @author      Paul Pacheco
 * @since       JDK1.1
 */
public final class ClaireReader extends FilterReader
{

		/**
		 * The buffer to hold the marked characters
		 */
		private CharBuffer buffer = new CharBuffer();

		/**
		 * Creates a reader with infinite buffer
		 * @param in the original reader
		 */
		public ClaireReader(Reader in) throws IOException
		{			
			super(in);
		}

		/**
		 * the current line
		 */
		private int line=1;

		/**
		 * the current position
		 */
		private int pos=1;

		/**
		 * the current column
		 */		
		private int col=1;

		/**
		 * the last character
		 */
		private char lastChar='x';

		/**
		 * true if we have reached the end of file
		 */
		private boolean eof=false;
		
		/**
		 * Updates the positions. Determines if an eol has ocurred and updates
		 * the rest of the variables
		 * @param nextChar is the next char to be reed
		 */
		private void updatePosition(char nextChar)
		{
				pos++;
				switch (nextChar)
				{
					case '\n':
						if (lastChar != '\r')
						{
							col=1;
							line++;
						}
						break;
					case '\r':
						col=1;
						line++;
						break;
					default:
						col++;
				}
							
				lastChar=nextChar;
		}
		
		/**
		 * Read a single character.
		 *
		 * @exception  IOException  If an I/O error occurs
		 */
		public int read() throws IOException
		{
			
			int result = buffer.read();

			if (result != -1 )
			{
				updatePosition((char)result);
				return result;
			}

			if (eof)
				return -1;

			result = super.read();


			if (result > -1 )
			{
				buffer.add((char)result);
				buffer.read();
				updatePosition((char)result);
			}
			else
				eof=true;
			
			return result;
		}


		/**
		 * Read characters into a portion of an array.
		 *
		 * <p> This method implements the general contract of the corresponding
		 * <code>{@link Reader#read(char[], int, int) read}</code> method of the
		 * <code>{@link Reader}</code> class.  As an additional convenience, it
		 * attempts to read as many characters as possible by repeatedly invoking
		 * the <code>read</code> method of the underlying stream.  This iterated
		 * <code>read</code> continues until one of the following conditions becomes
		 * true: <ul>
		 *
		 *   <li> The specified number of characters have been read,
		 *
		 *   <li> The <code>read</code> method of the underlying stream returns
		 *   <code>-1</code>, indicating end-of-file, or
		 *
		 *   <li> The <code>ready</code> method of the underlying stream
		 *   returns <code>false</code>, indicating that further input requests
		 *   would block.
		 *
		 * </ul> If the first <code>read</code> on the underlying stream returns
		 * <code>-1</code> to indicate end-of-file then this method returns
		 * <code>-1</code>.  Otherwise this method returns the number of characters
		 * actually read.
		 *
		 * <p> Subclasses of this class are encouraged, but not required, to
		 * attempt to read as many characters as possible in the same fashion.
		 *
		 * <p> Ordinarily this method takes characters from this stream's character
		 * buffer, filling it from the underlying stream as necessary.  If,
		 * however, the buffer is empty, the mark is not valid, and the requested
		 * length is at least as large as the buffer, then this method will read
		 * characters directly from the underlying stream into the given array.
		 * Thus redundant <code>BufferedReader</code>s will not copy data
		 * unnecessarily.
		 *
		 * @param      cbuf  Destination buffer
		 * @param      off   Offset at which to start storing characters
		 * @param      len   Maximum number of characters to read
		 *
		 * @return     The number of characters read, or -1 if the end of the
		 *             stream has been reached
		 *
		 * @exception  IOException  If an I/O error occurs
		 */
		public int read(char cbuf[], int off, int len) throws IOException
		{
			for (int i=0;i< len; i++)
			{
				int next= read();

				if (next == -1)
					return i;
				
				cbuf[off+i] = (char)next;
			}

			return len;
		}
		
		/**
		 * Gets the current line
		 * @return the current line		 
		 */
		public int line()
		{
			return line;
		}

		/**
		 * gets the current position
		 * @return the current position
		 */
		public int pos()
		{
			return pos;
		}

		/**
		 * gets the current column
		 * @returen the current column
		 */
		public int col()
		{
			return col;
		}
		
		/**
		 * the line at where the mark is
		 */
		private int markLine=1;
		/**
		 * the position at where the mark is
		 */
		private int markPos=1;
		/**
		 * the column at where the mark is
		 */
		private int markCol=1;
		/**
		 * the last character before the mark
		 */
		private char markLastChar='x';

			
		/**
		 *	Mark the present position in the stream.
		 * @param readAheadLimit does nothing
		 */
		public void mark(int readAheadLimit)
		{
			mark();
		}
		
		/**
		 *	Mark the present position in the stream.
		 */
		public void mark()
		{
			markLine=line;
			markPos=pos;
			markCol=col;
			markLastChar=lastChar;

			buffer.mark();
		}
		
		/**
		 * Reset the stream
		 */
		public void reset() throws IOException
		{
			line=markLine;
			pos=markPos;
			col=markCol;
			lastChar=markLastChar;

			buffer.reset();
		}
			
}
