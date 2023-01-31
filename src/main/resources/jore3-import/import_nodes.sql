SELECT n.soltunnus AS soltunnus,
       n.soltyyppi AS soltyyppi,
       n.solomx    AS solomx,
       n.solomy    AS solomy,
       n.solstmx   AS solstmx,
       n.solstmy   AS solstmy
FROM jr_solmu n
WHERE n.solomx IS NOT NULL
  AND n.solomy IS NOT NULL
  AND n.solstmx IS NOT NULL
  AND n.solstmy IS NOT NULL
