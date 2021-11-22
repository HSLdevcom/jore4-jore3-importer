SELECT
       p.soltunnus AS soltunnus,
       p.elynumero AS elynumero,
       p.pysnimi AS pysnimi,
       p.pysnimir AS pysnimir,
       s.sollistunnus AS sollistunnus,
       s.solkirjain AS solkirjain
FROM jr_pysakki p
         JOIN jr_solmu s ON (p.soltunnus = s.soltunnus)
ORDER BY p.soltunnus ASC