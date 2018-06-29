select
  to_char(
    TO_DATE('01/01/1970 00:00:00','DD/MM/YYYY HH24:MI:SS') + (revtstmp /1000/60/60/24),
    'DD/MM/YYYY HH24:MI:SS'
  ) timestamp
from SIGATP.REVINFO order by rev desc