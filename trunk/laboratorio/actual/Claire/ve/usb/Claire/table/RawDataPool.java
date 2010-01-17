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
 * $Id: RawDataPool.java,v 1.2 1999/11/06 01:43:44 Paul Exp $
 *
 * The changes to this file are:
 *
 * $Log: RawDataPool.java,v $
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

/**
 * Represents a pool of data, used to store the tables
 * an array of data will be stored as is. i.e. { 1, 2, 3} will be stored
 * { 1, 2, 3 }. The pool of data allows to reuse data. i.e if the following
 * arrays are to be stored {1, 2, 3} and {2, 3, 4} and { 1, ?, ?, 4} the
 * full stored array will be { 1, 2, 3, 4 }, the first array will be at
 * position 0, the second at position 1, and the third at position 0
 * @version     $Revision: 1.2 $
 * @author      Paul Pacheco
 * @since       JDK1.1
 */
public class RawDataPool extends DataPool
{
		
		/**
		 * the actual array that contains the data
		 */
		protected ByteList data= new ByteList();

		/**
		 * flags of each data space. A data space can be used or free.
		 * if it is used, then it can not be changed, if it is free,
		 * it can be used to store any data. Once something is written
		 * to a data space, it will be set as used
		 */
		
		protected BitSet usedFlags = new BitSet();
		
		/**
		 * collection of unused position in the table.
		 */
		protected IntList unused = new IntList();		
		
		/**
		 * the positions of the values stored. i. e. if the stored array contains
		 * {1, 2, 3, 4, 1, 2, 4}, then this array would contain something like:
		 * positions:
		 *     {},        // there are no 0's
		 *     {0, 5 },   // 1 is in position 0 and 5
		 *     {1, 6 },   // 2 is in position 1 and 6
		 *     {2 },      // 3 is in position 2
		 *     {3, 7 },   // 4 is in position 3 and 7
		 *     {}         // there are no 5
		 *     ...        // there are no other values
		 */
		IntList positions[] = new IntList[256];
		

		/**
		 * find where a given data value is
		 * @param the data to be found
		 * @return the list of positions where the data is
		 */
		protected IntList getPositions(byte data)
		{
			return positions[data+128];
		}

		/**
		 * sets the positions of a given data
		 * @param data the data to be acknoleged
		 * @param list the positions where the data is
		 */
		protected void setPositions(byte data, IntList list)
		{
			positions[data+128]=list;
		}
		
		/**
		 * determines how easy it is to store an array using a data as
		 * index. The less the data is on the stored array, the easier
		 * it is to ubicate it.
		 * @param index the index of the data into de indexes array
		 * @param list the list of positions for the data
		 * @return an integer representing how easy it is for this value
		 *        the lower, the easier 
		 */
		protected int ease(int index, IntList list)
		{
			if (list == null)
				return index;

			return list.size() + index;
		}
					
		/**
		 * finds the best position for a given data array into the
		 * pool
		 * @param data the data to be stored
		 * @param indexes the indexes of the data array
		 */
		protected int bestPosition(byte data[], int indexes[])
		{
			
			int minIndex = indexes[0];

			int easier=0;
			IntList easierList= getPositions(data[easier]);
			int ease = ease(indexes[0], easierList);
			
			for (int i=1;i< data.length; i++)
			{

				minIndex= Math.min(minIndex, indexes[i]);

				IntList candidateList = getPositions(data[i]);

				int newease = ease(indexes[i], candidateList);

				if (ease > newease)
				{
					easier=i;
					easierList=candidateList;
					ease = newease;
				}
			}

			int easierIndex= indexes[easier];

			if (easierList != null)
			{
				
				for (int i=0;i< easierList.size(); i++)
				{
					int position = easierList.get(i) - easierIndex;
				
					if (isValid(data, indexes, position))
						return position;
				}
			}
			
			for (int i=0;i< unused.size() ; i++)
			{
				int position = unused.get(i) - easierIndex;
				
				if (isValid(data, indexes, position))
					return position;
			}
			
			for (int i=0;  i<=easierIndex - minIndex; i++)
			{
				int position = this.data.size()+i-easierIndex;
				
				if (isValid(data, indexes, position))
					return position;
			}

			return 0;
		}
		
		/**
		 * makes sure the data that is going to be inserted fits
		 * properly into the array. if it doesn't, makes the
		 * pool bigger
		 * @param indexes the indexes of the data to be stored
		 * @param position the position at where the data is going
		 *        to be stored
		 */
		private void checkOverflow (int indexes[], int position)
		{
			int maxIndex = indexes[0];
			
			for (int i=1;i< indexes.length; i++)
				maxIndex= Math.max(maxIndex, indexes[i]);

			ensureSize(maxIndex + position + 1);
		}

		/**
		 * verifies if the given position for the data is valid or not
		 * @param data the data to be stored
		 * @param indexes the indexes of the data
		 * @param position the position where it is going to be tested
		 * @return true if the position is valid
		 */
		private final boolean isValid(byte data[], int indexes[], int position)
		{
			
			for (int j=0 ; j< indexes.length; j++)
			{
				int index = indexes[j]+position;
				
				if (index >= this.data.size())
					continue;

				if (index < 0)
					return false;
				
				if (usedFlags.get(index) && this.data.get(index) != data[j])
					return false;
			}

			return true;
		}

		/**
		 * Stores a value into the pool. This method also sets the necesary
		 * flags and updates the necesary lists.
		 * @param data the data to be stored in the pool
		 * @param index the index at where the data is going to be stored
		 */
		private void burn(byte data, int index)
		{
			unused.removeAt(unused.binarySearch(index));
			usedFlags.set(index);
			
			IntList result = getPositions(data);
			
			if (result == null)
			{
				result = new IntList();
				setPositions(data,result);
			}
			result.add(index);
			
			this.data.set(index,data);
		}
		
		/**
		 * writes an array into the pool. This method stores each value
		 * of the array into the pool acording to their indexes
		 * @param data the data to be stored into the array
		 * @param indexes the indexes of the data array
		 * @param position the position at where the data is to be stored
		 */
		protected int burn(byte data[], int indexes[], int position)
		{
			for (int i=0;i< indexes.length; i++)
			{
				int index = indexes[i]+position;
				
				if (!usedFlags.get(index))
					burn(data[i], index);
			}

			return position;
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
				
			if (data.length == 0)
				return 0;
			
			int position = bestPosition(data, indexes);

			checkOverflow(indexes,position);

			return burn(data, indexes,position);
		}	  		

		/**
		 * gets the byte array of the pool
		 */
		public byte[] toByteArray()
		{
		   return data.toArray();
		}


		/**
		 * makes the pool bigger
		 * @param size the new size of the pool
		 */
		private void ensureSize(int size)
		{
			for (int i=this.data.size();i< size; i++)
				unused.add(i);
			
			data.growNoTruncate(size);
		}		
}					
