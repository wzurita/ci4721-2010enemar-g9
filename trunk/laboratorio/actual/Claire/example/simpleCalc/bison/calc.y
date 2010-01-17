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

/* Infix notation calculator--calc */

%{
#include <stdio.h>
#include <math.h>
%}

%union
{
		double val;
}

/* BISON Declarations */
%token <val> NUM
%left '-' '+'
%left '*' '/'
%left NEG     /* negation--unary minus */
%right '^'    /* exponentiation        */


%type <val> exp
/* Grammar follows */
%%

input:    /* empty string */
| input line
;

line:     '\n'
| exp '\n'  { printf ("\t%.10g\n", $1); }
;

exp:      NUM                { $$ = $1;         }
| exp '+' exp        { $$ = $1 + $3;    }
| exp '-' exp        { $$ = $1 - $3;    }
| exp '*' exp        { $$ = $1 * $3;    }
| exp '/' exp        { $$ = $1 / $3;    }
| '-' exp  %prec NEG { $$ = -$2;        }
| exp '^' exp        { $$ = pow ($1, $3); }
| '(' exp ')'        { $$ = $2;         }
;

%%

main ()
{
	yyparse ();
}


yyerror (s)  /* Called by yyparse on error */
	char *s;
{
	printf ("%s\n", s);
}
