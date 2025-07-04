SELECT
	   pa.pysalueid AS pysalueid,
	   pa.nimi AS nimi,
	   pa.nimir AS nimir,
	   p.pysnimipitka AS nimipitka,
	   p.pysnimipitkar AS nimipitkar,
	   p.pyspaikannimi AS paikannimi,
	   p.pyspaikannimir AS paikannimir
FROM jr_lij_pysakkialue pa
INNER JOIN (
	SELECT p.pysalueid, p.pysnimipitka, p.pysnimipitkar, p.pyspaikannimi, p.pyspaikannimir
    FROM jr_pysakki p
    WHERE p.soltunnus IN (SELECT MIN(soltunnus) FROM jr_pysakki GROUP BY pysalueid)
) p ON pa.pysalueid = p.pysalueid
ORDER BY pa.pysalueid ASC;
