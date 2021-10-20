SELECT p.soltunnus AS soltunnus,
    p.elynumero AS elynumero,
    p.pysnimi AS pysnimi,
    p.pysnimir AS pysnimir
FROM jr_pysakki p
    JOIN jr_solmu s ON (p.soltunnus = s.soltunnus)
WHERE EXISTS
    (
        SELECT l.lnkverkko
        FROM jr_linkki l
        WHERE l.lnkverkko = '1' AND (l.lnkalkusolmu = s.soltunnus OR l.lnkloppusolmu = s.soltunnus)
    )
ORDER BY p.soltunnus ASC