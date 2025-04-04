SELECT r.reitunnus,
       r.lintunnus,
       r.reinimi,
       r.reinimir
FROM jr_reitti r
WHERE NULLIF(LTRIM(RTRIM(r.reitunnus)), '') IS NOT NULL
  AND NULLIF(LTRIM(RTRIM(r.lintunnus)), '') IS NOT NULL;
