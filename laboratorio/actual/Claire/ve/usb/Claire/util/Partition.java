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
 * $Id: Partition.java,v 1.1.1.1 1999/10/31 07:47:02 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: Partition.java,v $
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 * Revision 1.3  1999/09/29 02:47:34  Paul
 * Drammatically improved the speed of the compression algorithm
 *
 * Revision 1.2  1999/09/09 09:53:56  Paul
 * Added some cvs comments
 *
 */

package ve.usb.Claire.util;
import java.util.*;

/**
 * Handles a partition of repartitionable objects
 * @version     $Revision: 1.1.1.1 $
 * @author      Paul Pacheco
 * @since       JDK1.2
 * @see Repartitionable
 */
public final class Partition
{

		/**
		 * Maps between objects and their partition classes represented
		 * as sets
		 */
		private Map classMap = new HashMap();

		/**
		 * list of all the partition classes, represented each as a set
		 */
		private List sets=new ArrayList();


		/**
		 * Creates a partition, initially, the partition will be empty
		 */
		public Partition()
		{
		}
		
		/**
		 * Creates a partition, initially, the partition will have only
		 * one class, therefore, it will be { elements }
		 * @param elements the list of all elements in the partition
		 */
		public Partition(Collection elements)
		{
			Set clon=new HashSet();
			clon.addAll(elements);
			put(clon);
		}

		/**
		 * puts a class in the partition
		 * @param set the class to be inserted
		 */
		public void put(Set set)
		{
			Iterator iter=set.iterator();
			while (iter.hasNext())
				classMap.put(iter.next(), set);

			sets.add(set);
		}

		/**
		 * Creates a new class in the partition. If the new class to be created
		 * contains elements already in the partition, then they are removed
		 * first from the partition.
		 * @param set the new class to be created
		 */
		public void split(Set set)
		{
			Set clon=new HashSet();
			clon.addAll(set);
			
			Iterator iter=set.iterator();

			while (iter.hasNext())
			{
				Object obj=iter.next();

				Set previowsClass= (Set)classMap.get(obj);

				if (previowsClass != null)
				{
					previowsClass.remove(obj);

					if (previowsClass.isEmpty())
						sets.remove(previowsClass);
				}
			}

			put(clon);
		}

		/**
		 * given an object, determines what class it belongs to
		 * @param item the object in the partition whose class is to be found
		 * @return the class the object belongs to, null if the object is not
		 *         in the partition
		 */
		public Set get(Object item)
		{
			return (Set)classMap.get(item);			
		}

		/**
		 * gets the set of all classes in the partition
		 * @return the set of all classes
		 */
		public Set sets()
		{
			Set result=new HashSet();
			result.addAll(sets);
			return result;
		}

		/**
		 * determines if two objects are in the same class
		 * @param one object in the partition
		 * @param other object in the partition
		 * @return true if the two objects are in the same class
		 *         false otherwise
		 */
		public boolean sameClass(Object one, Object other)
		{
			return get(one)==get(other);
		}
		
		/**
		 * calculates a map where each object maps to its class
		 * @return the map representing the partition
		 */
		public Map classMap()
		{
			return Collections.unmodifiableMap(classMap);
		}

		/**
		 * Asks each object if they want to stay in a class. If an object
		 * does not want to stay, a new class is created for him.
		 * the process is repeated untill all objects stay.
		 */
		public void repartition()
		{
			boolean changed=false;

			Set splitted=new HashSet();
			

			do
			{
				changed=false;
				
				for (int i=0;i< sets.size(); i++)
				{
					Set nextClass= (Set) sets.get(i);

					
					Iterator possibleIter=nextClass.iterator();
					Repartitionable reference=(Repartitionable)possibleIter.next();
						
					while (possibleIter.hasNext())
					{
						Repartitionable possible=(Repartitionable)possibleIter.next();
						
						if (!possible.sameClass(reference,this))
						{
							possibleIter.remove();
							splitted.add(possible);
						}
					}

					if (splitted.size()>0)
					{
						put(splitted);
						splitted=new HashSet();
						changed=true;
					}
				}
			}
			while (changed);
			
		}
}
