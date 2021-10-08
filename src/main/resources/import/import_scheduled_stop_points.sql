SELECT
       p.soltunnus AS soltunnus,
       p.pysnimi AS pysnimi,
       p.pysnimir AS pysnimir,
       s.solomx AS solomx,
       s.solomy AS solomy
FROM jr_pysakki p
         JOIN jr_solmu s ON (p.soltunnus = s.soltunnus)
ORDER BY p.soltunnus ASC