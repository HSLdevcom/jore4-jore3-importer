SELECT l.lintunnus,
       l.id,
       l.linverkko,
       l.lintilorg,
       l.linjoukkollaji,
       l.linjlkohde,
       -- koodi = '2' in jr_linja_vaatimus means we have a trunk line
       IIF(EXISTS (
           SELECT 1 FROM jr_linja_vaatimus lv WHERE lv.lintunnus COLLATE DATABASE_DEFAULT = l.lintunnus COLLATE DATABASE_DEFAULT
                                                AND lv.kookoodi COLLATE DATABASE_DEFAULT = '2' COLLATE DATABASE_DEFAULT
       ), 1, 0) AS linrunkolinja
FROM jr_linja l
