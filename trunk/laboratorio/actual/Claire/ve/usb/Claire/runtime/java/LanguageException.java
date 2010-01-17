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
 * $Id: LanguageException.java,v 1.1 1999/11/07 11:53:25 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: LanguageException.java,v $
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
 * Signals that a Lexer exception has ocurred. Also tells the line, column
 * and position of the error
 * @version     $Revision: 1.1 $
 * @author      Paul Pacheco
 * @since       JDK1.1
 */

public class LanguageException extends java.lang.Exception
{


		private int line=0;
		private int col=0;
		private int pos=0;
		
		/**
		 * Constructs a LexerException 
		 */ 
		public LanguageException ()
		{
			super ();
		}
		/**
		 * Constructs a LexerException with a message
		 * @param msg the detail message
		 */ 
		public LanguageException (String msg, int line, int col, int pos)
		{
			super ("error("+line+", "+col+"):"+msg);
			this.line=line;
			this.col=col;
			this.pos=pos;
		}

		/**
		 * tells where the exception ocurred
		 * @return the line where the exception ocured
		 */
		public int line()
		{
			return this.line;
		}

		/**
		 * tells where the exception ocurred
		 * @return the column where the exception ocured
		 */
		public int col()
		{
			return this.col;
		}

		/**
		 * tells where the exception ocurred
		 * @return the position where the exception ocured
		 */
		public int pos()
		{
			return this.pos;
		}		
}
