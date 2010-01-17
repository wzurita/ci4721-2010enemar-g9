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
 * $Id: DataPool.java,v 1.3 1999/11/07 14:14:08 Paul Exp $
 *
 * The changes to this file are:
 *
 * $Log: DataPool.java,v $
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
 * Represents a pool of data, used to store the tables
 * @version     $Revision: 1.3 $
 * @author      Paul Pacheco
 * @since       JDK1.2
 */
public abstract class DataPool
{
		
      /**
       * Stores an array of data
       * @param data the array to be stored
       * @return the position where the data is stored
       */
      public long ubicate(long data[])
      {			
	 return ubicate(Pack.unpack(data));
      }

      /**
       * Stores an array of data
       * @param data the array to be stored
       * @return the position where the data is stored
       */
      public long ubicate(int data[])
      {			
	 return ubicate(Pack.unpack(data));
      }

      /**
       * Stores an array of data
       * @param data the array to be stored
       * @return the position where the data is stored
       */
      public long ubicate(char data[])
      {			
	 return ubicate(Pack.unpack(data));
      }
		
      /**
       * Stores an array of data
       * @param data the array to be stored
       * @return the position where the data is stored
       */
      public long ubicate(short data[])
      {			
	 return ubicate(Pack.unpack(data));
      }

      /**
       * Stores an array of data
       * @param data the array to be stored
       * @return the position where the data is stored
       */
      public long ubicate(byte data[])
      {			
	 int indexes[]= new int[data.length];

	 for (int i=0;i< data.length; i++)
	    indexes[i]=i;

	 return ubicate(data,indexes);
      }

      /**
       * Stores an array of data wich is not necesarily consecutive
       * @param data the array to be stored
       * @param indexes indexes of each of the data elemets
       *        to be stored.
       * @return the position where the data is stored
       */
      public long ubicate(int data[], int indexes[])
      {
	 int DataSize=4;

	 int newIndexes[]= new int[indexes.length*DataSize];

	 for (int i=0;i< indexes.length; i++)
	 {
	    for (int j=0;j< DataSize; j++)
	    {
	       newIndexes[i*DataSize + j] = indexes[i]*DataSize + j;
	    }
	 }
	 return ubicate(Pack.unpack(data), newIndexes);
      }

      /**
       * Stores an array of data wich is not necesarily consecutive
       * @param data the array to be stored
       * @param indexes indexes of each of the data elemets
       *        to be stored.
       * @return the position where the data is stored
       */
      public long ubicate(short data[], int indexes[])
      {
	 int DataSize=2;

	 int newIndexes[]= new int[indexes.length*DataSize];

	 for (int i=0;i< indexes.length; i++)
	 {
	    for (int j=0;j< DataSize; j++)
	    {
	       newIndexes[i*DataSize + j] = indexes[i]*DataSize + j;
	    }
	 }
	 return ubicate(Pack.unpack(data), newIndexes);
      }

      /**
       * Stores an array of data wich is not necesarily consecutive
       * @param data the array to be stored
       * @param indexes indexes of each of the data elemets
       *        to be stored.
       * @return the position where the data is stored
       */
      public long ubicate(char data[], int indexes[])
      {
	 int DataSize=2;

	 int newIndexes[]= new int[indexes.length*DataSize];

	 for (int i=0;i< indexes.length; i++)
	 {
	    for (int j=0;j< DataSize; j++)
	    {
	       newIndexes[i*DataSize + j] = indexes[i]*DataSize + j;
	    }
	 }
	 return ubicate(Pack.unpack(data), newIndexes);
      }
		
      /**
       * Stores an array of data wich is not necesarily consecutive
       * @param data the array to be stored
       * @param indexes indexes of each of the data elemets
       *        to be stored.
       * @return the position where the data is stored
       */
      public abstract long ubicate(byte data[], int indexes[]);

      /**
       * writes the data content to a translator.
       * @param name the name for the array to be stored
       * @param trans the translator to where the data is to be written
       */
      public abstract byte []  toByteArray();

}					
