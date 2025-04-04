-- Because there appears to be temporal overlap between line headers, at least
-- in the JORE3 test database, we read them in descending temporal order so that
-- constraint violation errors do not discard newer versions of line headers.

SELECT h.lintunnus,
       h.linalkupvm,
       h.linloppupvm,
       h.linnimi,
       h.linnimilyh,
       h.linnimir,
       h.linnimilyhr,
       h.linlahtop1,
       h.linlahtop1r,
       h.linlahtop2,
       h.linlahtop2r
FROM jr_linjannimet h
ORDER BY h.lintunnus ASC, h.linloppupvm DESC, h.linalkupvm DESC
