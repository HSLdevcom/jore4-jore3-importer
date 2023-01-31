SELECT p.pisid                                        AS pisid,
       p.pisjarjnro                                   AS pisjarjnro,
       p.lnkverkko                                    AS lnkverkko,
       p.lnkalkusolmu                                 AS lnkalkusolmu,
       p.lnkloppusolmu                                AS lnkloppusolmu,
       p.pismx                                        AS pismx,
       p.pismy                                        AS pismy,
       -- Join the coordinates of the start and end nodes
       -- However, for bus stops we use the projected location (solstm)
       IIF(sa.soltyyppi = 'P', sa.solstmx, sa.solomx) AS alkusolmux,
       IIF(sa.soltyyppi = 'P', sa.solstmy, sa.solomy) AS alkusolmuy,
       IIF(sb.soltyyppi = 'P', sb.solstmx, sb.solomx) AS loppusolmux,
       IIF(sb.soltyyppi = 'P', sb.solstmy, sb.solomy) AS loppusolmuy
FROM jr_piste p
         LEFT JOIN jr_solmu sa ON sa.soltunnus = p.lnkalkusolmu
         LEFT JOIN jr_solmu sb ON sb.soltunnus = p.lnkloppusolmu
WHERE p.pismx IS NOT NULL
  AND p.pismy IS NOT NULL
  AND sa.solstmx IS NOT NULL
  AND sa.solomx IS NOT NULL
  AND sa.solstmy IS NOT NULL
  AND sa.solomy IS NOT NULL
  AND sb.solstmx IS NOT NULL
  AND sb.solomx IS NOT NULL
  AND sb.solstmy IS NOT NULL
  AND sb.solomy IS NOT NULL
ORDER BY p.lnkalkusolmu ASC,
         p.lnkloppusolmu ASC,
         p.lnkverkko ASC,
         p.pisjarjnro ASC
