SELECT
       p.soltunnus AS soltunnus,
       p.elynumero AS elynumero,
       p.pysnimi AS pysnimi,
       p.pysnimir AS pysnimir,
       s.sollistunnus AS sollistunnus,
       s.solkirjain AS solkirjain,
       p.paitunnus AS paitunnus,
       count(DISTINCT v.reitunnus) AS usage_in_routes -- Number of times used in routes, after a defined date
FROM jr_pysakki p
INNER JOIN jr_solmu s ON (p.soltunnus = s.soltunnus)
LEFT OUTER JOIN (
    SELECT DISTINCT rl.reitunnus, rl.lnkalkusolmu, rl.lnkloppusolmu
    FROM jr_reitinlinkki rl
    INNER JOIN jr_reitti r ON (r.reitunnus = rl.reitunnus)
    WHERE r.reiviimpvm >= '01.01.2021 00:00:00' -- Only current routes are relevant
) v ON v.lnkalkusolmu = p.soltunnus OR v.lnkloppusolmu = p.soltunnus
GROUP BY p.soltunnus, p.elynumero, p.pysnimi, p.pysnimir, s.sollistunnus, s.solkirjain, p.paitunnus
ORDER BY p.soltunnus ASC;
