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
 * $Id: Pack.java,v 1.1 1999/11/07 11:53:25 Paul Exp $
 *
 * the changes to this file are
 *
 * $Log: Pack.java,v $
 * Revision 1.1  1999/11/07 11:53:25  Paul
 * Copied the java runtime classes inside this package
 *
 * Revision 1.2  1999/11/06 01:43:28  Paul
 * Improved the table generation
 *
 * Revision 1.1.1.1  1999/10/31 07:47:02  Paul
 * Imported Claire files
 *
 * Revision 1.2  1999/09/09 09:53:55  Paul
 * Added some cvs comments
 *
 */
package ve.usb.Claire.runtime.java;
import java.util.*;

/**
 * Handles packing and unpacking of data types
 * @version     $Revision: 1.1 $
 * @author      Paul Pacheco
 * @since       JDK1.1
 */

public class Pack
{
      /**
       * Packs an array of bytes into an array of int. 4 bytes make an int
       * @param unpacked the array of bytes
       * @return the packed array of int
       */
      public static int [] pack(byte [] unpacked)
      {
	 int [] packed = new int[(unpacked.length+3) / 4];
			
	 for (int i=0; i< unpacked.length; i++)
	 {
	    packed[i/4]|= (((int)unpacked[i]) & 0xff) << ((i % 4)*8);
	 }
			
	 return packed;
			
      }

      /**
		 * unpack an array of int into an array of bytes. 4 bytes make an int
		 * @param packed the packed array of int
		 * @return the array containging the unpacked byte array
		 */
      public static byte [] unpack(int [] packed)
      {
	 int DataSize=4;
			
	 byte unpacked[] = new byte[packed.length * DataSize];
			
	 for (int i=0;i< packed.length; i++)
	 {
	    for (int j=0; j< DataSize; j++)
	    {
	       unpacked[i*DataSize + j] = (byte)(packed[i] >>> (j*8));
	    }
	 }

	 return unpacked;
      }

      /**
		 * unpack an array of long into an array of bytes. 8 bytes make a long
		 * @param packed the packed array of int
		 * @return the array containging the unpacked byte array
		 */
      public static byte [] unpack(long [] packed)
      {
	 int DataSize=8;
			
	 byte unpacked[] = new byte[packed.length * DataSize];
			
	 for (int i=0;i< packed.length; i++)
	 {
	    for (int j=0; j< DataSize; j++)
	    {
	       unpacked[i*DataSize + j] = (byte)(packed[i] >>> (j*8));
	    }
	 }

	 return unpacked;
      }

      /**
       * pack two ints into a long
       * @param i1 the first int
       * @param i2 the second int
       * @return the long containing the packed combination
       */
      public static long pack(int i1, int i2)
      {
	 long result = i2;
	 result <<= 32;
	 result |= i1 & 0x00000000ffffffffl;
	 return result;
      }

      /**
       * pack two ints into a long
       * @param i1 the first int
       * @param i2 the second int
       * @return the long containing the packed combination
       */
      public static int packInt(short i1, short i2)
      {
	 int result = i2;
	 result <<= 16;
	 result |= i1 & 0x0000ffffl;
	 return result;
      }

      /**
		 * pack two bytes into a short
		 * @param i1 the first short
		 * @param i2 the second short
		 * @return the short containing the packed combination
		 */
      public static short pack(byte i1, byte i2)
      {
	 short result = (short)(i2 & 0xff);
	 result <<=8;
	 result |= (i1 & 0xff);
	 return result;
      }

      /**
		 * pack four bytes into an int
		 * @param i1 the first byte
		 * @param i2 the second byte
		 * @param i3 the third byte
		 * @param i4 the fourth byte
		 * @return the int containing the packed combination
		 */
      public static int pack(byte i1, byte i2, byte i3, byte i4)
      {			
	 int result = (i4 & 0xff);
	 result <<= 8;
	 result |= (i3 & 0xff);
	 result <<= 8;
	 result |= (i2 & 0xff);
	 result <<= 8;
	 result |= (i1 & 0xff);
	 return result;
      }

      /**
		 * pack two bytes into a char
		 * @param i1 the first byte
		 * @param i2 the second byte
		 * @return the short containing the packed combination
		 */
      public static char packChar(byte i1, byte i2)
      {
	 return (char)((((char)i2) << 8) | i1);
      }

      /**
       * gets the first int from a packed long
       * @param packed the long containing the packed ints
       * @return the unpacked int
       */
      public static int first(long packed)
      {
	 return (int) packed;
      }

    
      /**
       * gets the second int from a packed long
       * @param packed the long containing the packed ints
       * @return the unpacked int
       */
      public static int second(long packed)
      {
	 return  (int) (packed >>> 32);
      }
      
      /**
       * gets the first short from a packed int
       * @param packed the int containing the packed shorts
       * @return the unpacked short
       */
      public static short firstShort(int packed)
      {
	 return (short) packed;
      }

      /**
       * gets the second short from a packed int
       * @param packed the int containing the packed shorts
       * @return the unpacked short
       */
      public static short secondShort(long packed)
      {
	 return  (short) (packed >>> 16);
      }
		
      /**
		 * unpack an array of char into an array of bytes. 2 bytes make a char
		 * @param packed the packed array of char
		 * @return the array containging the unpacked byte array
		 */
      public static byte [] unpack(char [] packed)
      {
	 int DataSize=2;
			
	 byte unpacked[] = new byte[packed.length * DataSize];
			
	 for (int i=0;i< packed.length; i++)
	 {
	    for (int j=0; j< DataSize; j++)
	    {
	       unpacked[i*DataSize + j] = (byte)(packed[i] >>> (j*8));
	    }
	 }

	 return unpacked;
      }

      /**
		 * unpack an array of short into an array of byte. 2 bytes make a short
		 * @param packed the packed array of short
		 * @return the array containging the unpacked byte array
		 */
      public static byte [] unpack( short [] packed)
      {
	 int DataSize=2;
			
	 byte unpacked[] = new byte[packed.length * DataSize];
			
	 for (int i=0;i< packed.length; i++)
	 {
	    for (int j=0; j< DataSize; j++)
	    {
	       unpacked[i*DataSize + j] = (byte)(packed[i] >>> (j*8));
	    }
	 }

	 return unpacked;
      }
}
