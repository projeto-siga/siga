--------------------------------------------------------
--  DDL for Function REMOVE_ACENTO
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "SIGASR"."REMOVE_ACENTO" 
    (acentuado in
    VARCHAR2)

--	Enter the parameters for the function in
--	the brackets above.  If this function has
--	no parameters then delete the line

--  ***************************************************
--	*                                                 *
--	*   Author                                        *
--	*   Creation Date                                 *
--	*   Comments                                      *
--	*                                                 *
--  ***************************************************

RETURN  VARCHAR2

IS

--	Enter all variables cursors and constants following
--	this line
sem_acento VARCHAR2(4000);

begin

--	Enter the code for the function following
--	this line

      sem_acento := CONVERT(TRANSLATE(UPPER( acentuado ),'ÃÕÑ','AON'),'US7ASCII');

	return	sem_acento;
			
exception

--	Enter the code to handle exception conditions
--	following this line


	when others then
		null;
			
end;
 
/
