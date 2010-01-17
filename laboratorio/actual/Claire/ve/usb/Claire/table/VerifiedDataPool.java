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
 * $Id: VerifiedDataPool.java,v 1.3 1999/11/07 14:14:08 Paul Exp $
 *
 * The changes to this file are:
 *
 * $Log: VerifiedDataPool.java,v $
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
import ve.usb.Claire.util.*;
import ve.usb.Claire.translator.*;
import ve.usb.Claire.regexp.*;
import ve.usb.Claire.runtime.java.Pack;

/**
 * Represents a Data pool wich contains extra compression through adding
 * bit verification of every byte. For example, an array like { 0, 1, 0, 3, 0}
 * will be represented by { ?, 1, ?,  3, ? } and the verification of
 * { false, true, false, true, false }
 * @version     $Revision: 1.3 $
 * @author      Paul Pacheco
 * @since       JDK1.2
 * @see ve.usb.Claire.table.DataPool
 */
public class VerifiedDataPool extends DataPool
{

		/**
		 * the Data pool for the data to be stored
		 */
		DataPool original;

		/**
		 * the data pool for the verification arrays
		 */
		DataPool verify;
		
		/**
		 * Creates a verified data pool
		 * @param original the data pool where the data is to be stored
		 * @param verify the data pool where the verification is to be stored
		 */
		public VerifiedDataPool(DataPool original, DataPool verify)
		{
			this.original = original;
			this.verify = verify;
		}

		/**
		 * Stores an array of data
		 * @param data the array to be stored
		 * @return the position where the data is stored
		 */
		public long ubicate(byte data[])
		{
			int nonZeroCount=0;
			for (int i=0;i< data.length ; i++)
			{
				if (data[i] != 0)
					nonZeroCount++;
			}
			
			int indexes[]= new int[nonZeroCount];
			byte nonZeros[] = new byte[nonZeroCount];
			byte check[] = new byte[(data.length+7) / 8];
			
			int pos=0;
			for (int i=0;i< data.length; i++)
			{
				if (data[i]!= 0)
				{
					indexes[pos]=i;
					nonZeros[pos]=data[i];
					check[i/8]|= (1 << (i % 8));
					pos++;
				}
			}			

			int index= (int)original.ubicate(nonZeros,indexes);
			int checkIndex= (int)verify.ubicate(check);
			
			return Pack.pack(index,checkIndex);
		}

		
		/**
		 * Stores an array of data wich is not necesarily consecutive
		 * @param data the array to be stored
		 * @param indexes indexes of each of the data elemets
		 *        to be stored.
		 * @return the position where the data is stored
		 */

		public long ubicate(byte data[], int indexes[])
		{
			return original.ubicate(data,indexes);
		}

		/**
		 * gets the byte array of the pool
		 */
		public byte[] toByteArray()
		{
		   return null;
		}
		
}					
