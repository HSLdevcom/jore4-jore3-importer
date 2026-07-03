INSERT INTO jr_varustelutiedot_uusi
(
	tunnus,
	jcd_nro,
	cc_nro,
	pysakkityyppi,
	kpl_pysakkityyppi,
	katoksen_omistaja,
	ilme,
	infonhoito,
	lisavarusteet,
	kpl_lisavarusteet,
	sahko,
	nayttolaitteet,
	katos_kunto,
	runkolinjavarustus,
	kunnossapito,
	selite,
	kpl_kilvet
    )
VALUES (
    'c',
    '1234, 5678',
    '',
    '02', -- Steel shelter
    2,
    '08', -- Finavia
    '02', -- New,
    '14', -- ELY,
    '01', -- Info board, no bicycle parking
    1,
    '02', -- Continuous electricity
    '1',
    '03', -- Bad condition
    '02', -- Advertisiment, main line
    '02', -- Vantaa maintenance
    'Additional maintenance info',
    2
);
