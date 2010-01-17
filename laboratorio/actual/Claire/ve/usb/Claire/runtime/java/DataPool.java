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
 * $Id: DataPool.java,v 1.1 1999/11/07 11:53:25 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: DataPool.java,v $
 * Revision 1.1  1999/11/07 11:53:25  Paul
 * Copied the java runtime classes inside this package
 *
 * Revision 1.2  1999/11/06 01:43:28  Paul
 * Improved the table generation
 *
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 * Revision 1.3  1999/10/12 04:16:41  Paul
 * *** empty log message ***
 *
 * Revision 1.2  1999/09/09 09:53:55  Paul
 * Added some cvs comments
 *
 */
package ve.usb.Claire.runtime.java;
import java.io.*;
import java.util.zip.*;

/**
 * Handles the data pool at runtime, this class is responsible for uncompressing
 * and retrieving information from the pool
 * @version     $Revision: 1.1 $
 * @author      Paul Pacheco
 * @since       JDK1.2
 */

public class DataPool
{
		
      /**
       * the array with the actual data
       */
      private byte data[];

      /**
       * the array with the check for the data
       */
      private byte check[];
		
      /**
       * uncompresses a gziped array
       * @param src the array containing the gziped information
       * @return the array containing the gunziped information
       */
      private static byte[] gunzip(int src[])
      {
	 return gunzip(Pack.unpack(src));
      }

      /**
       * uncompresses a gziped array
       * @param src the array containing the gziped information
       * @return the array containing the gunziped information
       */
      private static byte[] gunzip(byte src[])
      {
	 try
	 {
	    InputStream in = new GZIPInputStream(new ByteArrayInputStream(src));
				
	    byte result[] = new byte[in.available()];

	    in.read(result);

	    in.close();
				
	    return result;
	 }
	 catch (Exception e)
	 {
	    System.err.println(e);
	 }

	 return null;
			
      }

      /**
       * Creates a data pool from a packed int array
       * @param data the packed int array
       */
      public DataPool(int [] data)
      {
	 this(data,false);
      }

      /**
       * Creates a data pool from a packed int array
       * @param data the packed int array
       * @param gziped true means the array is gziped, false otherwise
       */
      public DataPool(int [] data, boolean gziped)
      {
	 if (gziped)
	 {
	    this.data=this.check=gunzip(data);
	 }
	 else
	 {
	    this.data=this.check=Pack.unpack(data);
	 }
	 /*
	   for (int i=0;i< this.data.length ; i++)
	   {
	   if (i % 10 == 0)
	   System.out.println("");
	   System.out.print(this.data[i]+" ");
	   }
	   System.out.println("");
			
	 */
      }

      /**
       * Creates a data pool from a string array
       * @param data the array containing the data
       * @param gziped is true when the data is gziped, false otherwise
       */
      public DataPool(String [] data, boolean gziped)
      {
	 int size = 0;
	 for (int i=0;i< data.length ; i++)
	    size+=data[i].length();

	 this.check=this.data= new byte[size];

	 int pos=0;
	 //System.out.println("size =" + check.length);
			
	 for (int i=0;i< data.length ; i++)
	 {
	    data[i].getBytes(0, data[i].length(), this.data, pos);
	    pos+=data[i].length();
	 }
	 //System.out.println("size =" + check.length);
			
	 if (gziped)
	 {
	    this.data=this.check=gunzip(this.data);
	 }

	 //System.out.println("size =" + check.length);
      }

      /**
       * Creates a data pool from a string array
       * @param data the array containing the data
       * @param gziped is true when the data is gziped, false otherwise
       */
      public DataPool(String [] data, String [] check, boolean gziped)
      {
	 int size = 0;
	 for (int i=0;i< data.length ; i++)
	    size+=data[i].length();

	 this.data=new byte[size];

	 size = 0;
	 for (int i=0;i< check.length ; i++)
	    size+=check[i].length();
	 
	 this.check=new byte[size];

	 
	 int pos=0;
	 //System.out.println("size =" + check.length);
	 
	 for (int i=0;i< data.length ; i++)
	 {
	    data[i].getBytes(0, data[i].length(), this.data, pos);
	    pos+=data[i].length();
	 }
	 //System.out.println("size =" + check.length);
	 
	 pos=0;
	 //System.out.println("size =" + check.length);
	 
	 for (int i=0;i< check.length ; i++)
	 {
	    check[i].getBytes(0, check[i].length(), this.check, pos);
	    pos+=check[i].length();
	 }

	 if (gziped)
	 {
	    this.data=gunzip(this.data);
	    this.check=gunzip(this.check);
	 }
	 
	 //System.out.println("size =" + check.length);
      }
      
      /**
       * Get a byte at a given position
       * @param position the position at where the byte is
       * @return the byte at the position
       */
      public final byte getByte(int position)
      {
	 return this.data[position];
      }
      
      /**
       * get a bit at a given position
       * @param position the position at where the bit array starts
       * @param despl the index inside the array
       * @return true if the bit is on
       */
      public final boolean getBit(int position, int despl)
      {
	 return (this.check[position+despl/8] & (1 << (despl % 8))) != 0;
      }
      
      /**
       * get a byte at a given position
       * @param position the position at where the byte array is
       * @param despl the position from the begining of the array
       * @param checkPosition the position of the check array
       */
      public final byte getByte(int position, int despl, int checkPosition)
      {
	 checkPosition += (despl / 8);
	 //System.out.println("index = " + checkPosition);
	 //System.out.println("size = " + check.length);
			
	 if ((this.check[checkPosition] & (1 << (despl % 8))) != 0)
	    return data[position+despl];
	 else
	    return 0;
      }
		
      /**
       * get a short at a given position
       * @param position the position at where the byte array is
       * @param despl the position from the begining of the array
       */
      public final short getShort(int position, int despl)
      {
	 final int dataSize=2;
			
	 position+= despl * dataSize;			
	 return Pack.pack(getByte(position),
			  getByte(position+1));
      }

      /**
       * get a char at a given position
       * @param position the position at where the byte array is
       * @param despl the position from the begining of the array
       */
      public final char getChar(int position, int despl)
      {
	 final int dataSize=2;
			
	 position+= despl * dataSize;			
	 return Pack.packChar(getByte(position),
			      getByte(position+1));
      }

      /**
       * get a int at a given position
       * @param position the position at where the byte array is
       * @param despl the position from the begining of the array
       */
      public final int getInt(int position, int despl)
      {
	 final int dataSize=4;
			
	 position+= despl * dataSize;			
	 return Pack.pack(getByte(position),
			  getByte(position+1),
			  getByte(position+2),
			  getByte(position+3));
      }

      /**
       * Performs a binary search for char values and gets the position
       * at where the value would be inserted
       * @param value the value to be searched
       * @param start the start position of the array in the pool
       * @param count the amount of elements in the array in the pool
       * @param check the position of the check array
       * @return the position at where the value would be inserted
       */
      public int binarySearch(char value, int start, int count, int check)
      {
	 if (count==0)
	    return 0;

	 char inf = getChar(start,0,check);
			
	 if (value < inf)
	    return 0;

	 char sup = getChar(start,count-1,check);

	 if (value >= sup)
	    return count;

	 int realStart=0;
	 int realEnd=count-1;
			
	 while (realEnd - realStart > 1)
	 {
	    int middle= (realStart + realEnd) / 2;

	    char middleValue= getChar(start,middle,check);
				
	    if (value < middleValue)
	       realEnd=middle;
	    else if (value==middleValue)
	    {
	       realStart=middle;
	       break;
	    }
	    else
	       realStart=middle;
	 }

	 return realStart+1;
      }

      /**
       * get a short at a given position
       * @param position the position at where the byte array is
       * @param despl the position from the begining of the array
       * @param check the position of the check array
       */
      public final short getShort(int position, int despl, int check)
      {
	 final int dataSize=2;
			
	 despl*=dataSize;
			
	 return Pack.pack(getByte(position, despl, check),
			  getByte(position, despl+1, check));
      }
		
      /**
       * get a char at a given position
       * @param position the position at where the byte array is
       * @param despl the position from the begining of the array
       * @param check the position of the check array
       */
      public final char getChar(int position, int despl, int check)
      {
	 final int dataSize=2;
			
	 despl*=dataSize;
			
	 return Pack.packChar(getByte(position, despl, check),
			      getByte(position, despl+1, check));
      }

      /**
       * get a int at a given position
       * @param position the position at where the byte array is
       * @param despl the position from the begining of the array
       * @param check the position of the check array
       */
      public final int getInt(int position, int despl, int check)
      {
	 final int dataSize=4;
			
	 despl*=dataSize;
			
	 return Pack.pack(getByte(position, despl, check),
			  getByte(position, despl+1, check),
			  getByte(position, despl+2, check),
			  getByte(position, despl+3, check));
      }
		
			
}
