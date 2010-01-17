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
 * $Id: Template.java,v 1.3 1999/11/21 02:12:40 Paul Exp $
 *
 * The changes to this file are:
 *
 * $Log: Template.java,v $
 * Revision 1.3  1999/11/21 02:12:40  Paul
 * Dramatically reduced the size of the generated tables (suggested by Ascander Suarez)
 *
 * Revision 1.2  1999/11/07 14:14:18  Paul
 * Moved the java runtime library to another package.
 *
 * Revision 1.1  1999/11/06 01:44:26  Paul
 * Improved the translator, now a template file is used to make it
 * easier to modify.
 *
 *
 */

package ve.usb.Claire.translator;
import java.io.*;
import java.util.*;

/**
 * given a template class, fills the template and writes it to another file
 * using the desired TemplateTranslator
 * @version     $Revision: 1.3 $
 * @author      Paul Pacheco
 * @since       JDK1.2
 */

public class Template
{
      /**
       * Creates a template object
       */
      public Template()
      {
      }

      public static void translate(String templateFile, PrintWriter out, TemplateTranslator trans)
      {
	 try
	 {
	    InputStream in = ClassLoader.getSystemResourceAsStream(templateFile);
	    
	    Reader inReader = new BufferedReader(new InputStreamReader(in));

	    translate(inReader,out,trans);
	 }
	 catch (Exception e)
	 {
	    System.err.println("Error opening template file");
	    e.printStackTrace();
	    System.exit(1);
	 }
	 
      }

      /**
       * translate an input to an output using a template translator
       */

      public static void translate(Reader in, PrintWriter out, TemplateTranslator trans) throws IOException
      {
	 int inChar = in.read();
	 boolean eofPending=false;

	 StringBuffer buffer = new StringBuffer();
	 
	 while (inChar != -1)
	 {

	    char ch = (char)inChar;

	    // if it is the special @ symbol used for keywords, then get the next word
	    if (ch == '@')
	    {
	       buffer.setLength(0);
	       buffer.append(ch);

	       inChar = in.read();
	       while (inChar != -1 && Character.isJavaIdentifierPart((char)inChar))
	       {
		  buffer.append((char)inChar);
		  inChar = in.read();
	       }

	       // now, try to translate what's in the buffer
	       trans.translate(buffer.toString(), out);
	       
	    }
	    else
	    {
	       // nothing special recognized, let's see if it is an eol
	       if (ch == '\n' || eofPending)
	       {
		  out.println("");
		  eofPending=false;
	       }

	       if (ch == '\r')
		  eofPending=true;
	       else if (ch != '\n')		       
		  out.write(ch);
	       
	       inChar = in.read();
	    }
	 }    

	 if (eofPending)
	    out.println("");	    
      }
      
}
