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
 * $Id: TemplateTranslator.java,v 1.2 1999/11/07 14:14:18 Paul Exp $
 *
 * The changes to this file are:
 *
 * $Log: TemplateTranslator.java,v $
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

/**
 * given a template class, fills the template and writes it to another file
 * using the desired TemplateTranslator
 * @version     $Revision: 1.2 $
 * @author      Paul Pacheco
 * @since       JDK1.2
 */

public interface TemplateTranslator
{
      public abstract void translate(String key, PrintWriter out);
}
