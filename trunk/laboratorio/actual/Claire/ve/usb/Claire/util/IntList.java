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
 * $Id: IntList.java,v 1.1.1.1 1999/10/31 07:47:02 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: IntList.java,v $
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 * Revision 1.2  1999/09/09 09:53:55  Paul
 * Added some cvs comments
 *
 */

package ve.usb.Claire.util;
import java.util.*;

/**
 * Handles a list of integers.
 * @version     $Revision: 1.1.1.1 $
 * @author      Paul Pacheco
 * @since       JDK1.2
 */

public final class IntList implements Comparable
{
		/**
		 * Default grouth of the data array
		 */
		private static final int DEFAULTINCREMENT=100;
		
		/**
		 * array that contains the data
		 */
		private int data[];		

		/**
		 * Size of the list
		 */
		private int size=0;

		/**
		 * grouth of the data array
		 */
		private int jumpCapacity;
		
		/**
		 * Makes sure there is enough space to store data
		 * @param capacity the capacity needed
		 */
		private void ensureCapacity(int capacity)
		{
			if ( data == null || data.length < capacity)
			{
				int newData[]= new int[capacity+jumpCapacity];

				if (data != null)
					System.arraycopy(data, 0, newData, 0, size);
				
				data=newData;
			}
		}		
		
		/**
		 * Creates a list of ints
		 * @param initialCapacity the initial capacity of the list
		 * @param jumpCapacity the grouth of the capacity
		 */
		public IntList(int initialCapacity, int jumpCapacity)
		{
			this.jumpCapacity= jumpCapacity;
			ensureCapacity(initialCapacity);
		}

		/**
		 * Creates a list of ints
		 * @param initialCapacity the initial capacity of the list
		 */
		public IntList(int initialCapacity)
		{
			this(initialCapacity,DEFAULTINCREMENT);
		}
		
		/**
		 * Creates a list of ints, using the default capacity and grouth
		 */
		public IntList()
		{
			this(DEFAULTINCREMENT);
		}

		/**
		 * Creates a list of ints that is a copy of other
		 * @param other the original list
		 */
		public IntList(IntList other)
		{
			this();

			addAll(other);
		}
		
		/**
		 * Adds a value to the end of the list
		 * @param data the value to be added
		 */
		public boolean add(int data)
		{
			ensureCapacity(size()+1);
			this.data[size++]=data;
			return true;
		}

		/**
		 * Adds a value to the list
		 * @param index the index at where the data is to be added
		 * @param data the data to be added
		 */
		public void add(int index, int data)
		{
			ensureCapacity(size()+1);
			System.arraycopy(this.data, index, this.data, index+1, size()-index);
			this.data[index]=data;
		}

		/**
		 * Adds all the values contained in other list
		 * @param other the other list that contains the data
		 * @return true if anything was added
		 *         false otherwise
		 */
		public boolean addAll(IntList other)
		{
			ensureCapacity(size()+other.size());
			System.arraycopy(other.data, 0, data, size(), other.size());
			size+=other.size();
			return other.size()!=0;				
		}

		/**
		 * Adds all the values contained in other list
		 * @param index the index at where the other list is to be added
		 * @param other the other list that contains the data
		 * @return true if anything was added
		 *         false otherwise
		 */
		public boolean addAll(int index, IntList other)
		{
			ensureCapacity(size()+other.size());
			System.arraycopy(data, index, data, index+other.size(), size()-index);
			System.arraycopy(other.data, 0, data, index, other.size());
			size+=other.size();
			return other.size()!=0;				
		}
		
		/**
		 * Removes all elements from the list
		 */
		public void clear()
		{
			size=0;
		}
		
		/**
		 * searches for a data in the array
		 * @param data the data to be searches
		 * @return true if the list contains the data
		 *         false otherwise
		 */
		public boolean contains(int data)
		{
			for (int i=0;i< size(); i++)
				if (get(i) == data)
					return true;

			return false;
		}

		/**
		 * determines if this list is a super set of other list
		 * @param list the sublist
		 * @return true if this list is a super set of the other list
		 *         false otherwise
		 */
		public boolean containsAll( IntList list)
		{
			for (int i=0;i< list.size(); i++)
				if (!contains(list.get(i)))
					return false;

			return true;
		}

		/**
		 * compares two lists and determines if they are equal or not
		 * @param other the other list to be compared
		 * @return true if the lists are equal
		 *         false otherwise
		 */
		public boolean equals(Object other)
		{
			if (!(other instanceof IntList))
				return false;

			IntList list = (IntList)other;

			if (size() != list.size())
				return false;

			for (int i=0;i< size() ; i++)
				if (get(i) != list.get(i))
					return false;

			return true;
		}
		
		/**
		 * gets a byte from the list
		 * @param index the index at where the byte is
		 * @return the byte at the given index
		 */
		public int get(int index)
		{
			return data[index];
		}
		
		/**
		 * Calculates a hashcode for the list
		 * @return the hashcode for this list
		 */
		public int hashCode()
		{
			int result=0;
			
			for (int i=0;i< size(); i++)
				result+= 31 * get(i);

			return result;
		}
		

		/**
		 * determines if the list is empty
		 * @return true if the list is empty
		 *         false otherwise
		 */
		public boolean isEmpty()
		{
			return size() == 0;
		}

		/**
		 * gets the index of a given value
		 * @param data the data to be searched
		 * @return the index at where the data is.
		 *         -1 if the data is not in the list
		 */
		public int indexOf(int data)
		{
			for (int i=0; i< size(); i++)
				if (get(i) == data)
					return i;

			return -1;
		}
		
		/**
		 * gets the last index of a given value
		 * @param data the data to be searched
		 * @return the last index of the value
		 *         -1 if the data is not in the list
		 */
		public int lastIndexOf(int data)
		{
			for (int i=size()-1; i>= 0 ; i--)
				if (get(i) == data)
					return i;
			
			return -1;
		}

		/**
		 * removes a value from the list
		 * @param data the data to be removed
		 * @return true if anything was removed
		 *         false otherwise
		 */
		public boolean remove(int data)
		{
			int index = indexOf(data);

			if (index == -1)
				return false;

			removeAt(index);

			return true;
		}

		/**
		 * removes a value from the list
		 * @param index the index of the value to be removed
		 * @return the value that was in the index
		 */		
		public int removeAt(int index)
		{
			int result= get(index);			
			System.arraycopy(this.data, index +1 , this.data, index, size() - index -1);
			size--;
			return result;
		}

		/**
		 * Makes the list larger. If the new size is smaller than the
		 * list, then the list is truncated
		 * @param newSize the new size of the list
		 * @return the resulting size of the list
		 */
		public int grow(int newSize)
		{
			ensureCapacity(newSize);

			Arrays.fill(data, size(), data.length, 0);
			
			size= newSize;

			return size;
		}


		/**
		 * Makes the list larger. If the new size is smaller than the
		 * list, then nothing is done
		 * @param newSize the new size of the list
		 * @return the resulting size of the list
		 */
		public int growNoTruncate(int newSize)
		{
			if (size < newSize)
				grow(newSize);

			return size;
		}
		
		/**
		 * sorts the data in the list
		 */
		public void sort()
		{
			Arrays.sort(data, 0, size());			
		}

		/**
		 * Searches the specified array of ints for the specified value using the binary search algorithm. The array
		 * <b>must</b> be sorted (as by the sort method, above) prior to making this call. If it is not sorted, the results are
		 * undefined. If the array contains multiple elements with the specified value, there is no guarantee which one
		 * will be found.
		 * @param data the value to be searched for.
		 * @return index of the search key, if it is contained in the list; otherwise, (-(insertion point) - 1). The
		 * insertion point is defined as the point at which the key would be inserted into the list: the index of the
		 * first element greater than the key, or list.size(), if all elements in the list are less than the specified
		 * key. Note that this guarantees that the return value will be >= 0 if and only if the key is found.
		 */
		public int binarySearch(int data)
		{
			int min = 0;
			int max = size()-1;

			if (this.data[min] == data)
				return min;

			if (this.data[max] == data)
				return max;

			if (this.data[max] < data)
				return -max -2;

			if (this.data[min] > data)
				return -min -1;
			
			int middle;

			while (min < max -1)
			{
				middle = (min + max) / 2;

				if (this.data[middle] > data)
					max = middle;
				else if (this.data[middle] < data)
					min = middle;
				else
					return middle;
					
			}

			return -max -1;
			
		}

		/**
		 * finds the minimum value in the array
		 * @return the minimum.
		 */
		public int min()
		{
			int min= get(0);
			for (int i=1;i< size(); i++)
				min = Math.min(min, get(i));

			return min;
		}

		/**
		 * finds the maximum value in the array
		 * @return the maximum.
		 */
		public int max()
		{
			int min= get(0);
			for (int i=1;i< size(); i++)
				min = Math.max(min, get(i));

			return min;
		}
									
		/**
		 * Sets a value in a given position
		 * @param index the index at where the value is to be set
		 * @param data the data to be set
		 * @return the data that was in the position
		 */
		public int set(int index, int data)
		{
			int result = get(index);
			this.data[index]=data;
			return result;
		}
		
		/**
		 * gets the size of the list
		 * @return the size of the list
		 */
		public int size()
		{
			return this.size;
		}

		/**
		 * Calculates the list of elements between two sections
		 * @param fromIndex the start of the sub list inclusive
		 * @param toIndex the end of the sub list exclusive
		 * @return the resulting sublist
		 */
		public IntList subList(int fromIndex, int toIndex)
		{
			IntList result = new IntList(toIndex-fromIndex);

			for (int i=fromIndex; i< toIndex; i++)
				result.add(i-fromIndex, get(i));

			return result;
		}
		
		/**
		 * fills the list with a given element
		 * @param data the data with wich the list is to be filled
		 */
		public void fill(int data)
		{
			Arrays.fill(this.data,0,size(), data);
		}
		
		/**
		 * Transforms the list to an array of bytes
		 * @param byte the array of bytes containing the resulting list
		 */
		public int [] toArray()
		{
			int result[] = new int[size()];
			System.arraycopy(data, 0, result, 0, size());
			return result;
		}

		/**
		 * Returns a string representation of this list. The string representation consists of
		 * a list of the list's elements in the order they appear,
		 * enclosed in square brackets ("[]"). Adjacent elements are separated by the
		 * characters ", " (comma and space). 

		 * This implementation creates an empty string buffer, appends a left square bracket,
		 * and iterates over the collection appending the string representation of each element
		 * in turn. After appending each element except the last, the string ", " is appended.
		 * Finally a right bracket is appended. A string is obtained from the string buffer, and
		 * returned.
		 * @return a string representation of this list.
		 */
		public String toString()
		{
			StringBuffer result = new StringBuffer("[ ");

			for (int i=0;i< size(); i++)
			{
				if (i > 0)
					result.append(", ");

				result.append(""+get(i));
			}

			result.append(" ]");
			return result.toString();
		}

		/**
		 * compare the size of two lists
		 * @param otherObject the other list to whom the size is to be compared
		 * @return -1 if this list is smaller,
		 *          0 if the lists are the same size,
		 *          1 if this list is bigger
		 */
		public int compareTo(Object otherObject)
		{
			IntList otherList = (IntList)otherObject;

			if (this.size() < otherList.size())
				return -1;

			if (this.size() > otherList.size())
				return 1;

			return 0;
		}
		
}
