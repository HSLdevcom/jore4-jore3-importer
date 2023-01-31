SELECT r.reitunnus,
       r.lintunnus,
       r.reinimi,
       r.reinimir
FROM jr_reitti r
WHERE r.reitunnus != ''
