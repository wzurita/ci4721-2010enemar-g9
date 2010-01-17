rem #!/bin/bash
rem ######################################################################
rem # Claire a parser generator.                                         #
rem # Copyright (C) 1999  Paul Pacheco <93-25642@ldc.usb.ve>             #
rem #                                                                    #
rem # This library is free software; you can redistribute it and/or      #
rem # modify it under the terms of the GNU Lesser General Public         #
rem # License as published by the Free Software Foundation; either       #
rem # version 2 of the License, or (at your option) any later version.   #
rem #                                                                    #
rem # This library is distributed in the hope that it will be useful,    #
rem # but WITHOUT ANY WARRANTY; without even the implied warranty of     #
rem # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU  #
rem # Lesser General Public License for more details.                    #
rem #                                                                    rem #
rem # You should have received a copy of the GNU Lesser General Public   rem #
rem # License along with this library; if not, write to the Free         rem #
rem # Software Foundation, Inc., 59 Temple Place, Suite 330,             rem #
rem # Boston, MA  02111-1307  USA                                        rem #
rem #                                                                    rem #
rem # Please contact Paul Pacheco <93-25642@ldc.usb.ve> to submit any    rem #
rem # suggestion or bug report.                                          rem #
rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #

rem # $Id $
rem # 
rem # this file simplifies the process of invoking claire. Used inside DOS
rem #
rem # $Log $
rem #
rem #


rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #rem #
rem # important, modify this line to tell Claire where it is installed
CLAIRE_HOME=c:\Claire



rem #the directory is in CLAIRE_HOME. All we have to do is start the java
rem #virtual machine with claire loaded

java -jar $CLAIRE_HOME\Claire.jar $@
