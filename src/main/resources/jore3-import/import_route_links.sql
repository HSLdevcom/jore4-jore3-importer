SELECT rl.reitunnus,
       rl.suusuunta,
       rl.suuvoimast,
       rl.reljarjnro,
       rl.relid,
       rl.lnkverkko,
       rl.lnkalkusolmu,
       rl.lnkloppusolmu,
       rl.relohaikpys,
       rl.relpysakki,
       rl.ajantaspys,
       IIF(rl.relvpistaikpys = '1', 1, 0) AS rl_relvpistaikpys,
       IIF(rl.paikka = '1', 1, 0)         AS rl_paikka,
       IIF(vn.viasuomi IS NOT NULL, 1, 0) as rl_via,
       vn.maaranpaa2 AS rl_maaranpaa2,
       vn.maaranpaa2r AS rl_maaranpaa2r,
       IIF(rl.kirjaan = '1', 1, 0)        AS rl_kirjaan,
       rl.kirjasarake,
       IIF(rs.kirjaan = '1', 1, 0)        AS rs_kirjaan,
       rs.kirjasarake                     AS rs_kirjasarake,
       loppusolmu.soltyyppi               as loppusolmu_tyyppi
FROM jr_reitinlinkki rl
         -- Fetch attributes of the last node from the parent route direction
         LEFT JOIN jr_reitinsuunta rs
                   ON rs.reitunnus = rl.reitunnus AND rs.suusuunta = rl.suusuunta AND rs.suuvoimast = rl.suuvoimast
    -- Fetch the node type of the last node from the node table
         LEFT JOIN jr_solmu loppusolmu
                   ON rl.lnkloppusolmu = loppusolmu.soltunnus
         LEFT JOIN jr_via_nimet vn
                   ON rl.relid = vn.relid
ORDER BY rl.reitunnus,
         rl.suusuunta,
         rl.suuvoimast,
         rl.reljarjnro;
