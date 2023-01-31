SELECT l.lnkverkko,
       l.lnkmitpituus,
       l.lnkpituus,
       sa.soltunnus AS alku_soltunnus,
       sa.soltyyppi AS alku_soltyyppi,
       sa.solomx    AS alku_solomx,
       sa.solomy    AS alku_solomy,
       sa.solstmx   AS alku_solstmx,
       sa.solstmy   AS alku_solstmy,
       sb.soltunnus AS loppu_soltunnus,
       sb.soltyyppi AS loppu_soltyyppi,
       sb.solomx    AS loppu_solomx,
       sb.solomy    AS loppu_solomy,
       sb.solstmx   AS loppu_solstmx,
       sb.solstmy   AS loppu_solstmy
FROM jr_linkki l
         LEFT JOIN jr_solmu sa ON sa.soltunnus = l.lnkalkusolmu
         LEFT JOIN jr_solmu sb ON sb.soltunnus = l.lnkloppusolmu
WHERE sa.solomx IS NOT NULL
  AND sa.solomy IS NOT NULL
  AND sa.solstmx IS NOT NULL
  AND sa.solstmy IS NOT NULL
  AND sb.solomx IS NOT NULL
  AND sb.solomy IS NOT NULL
  AND sb.solstmx IS NOT NULL
  AND sb.solstmy IS NOT NULL
