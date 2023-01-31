INSERT INTO jr_reitinlinkki
(reitunnus, suusuunta, suuvoimast, reljarjnro, relid, relohaikpys, relpysakki, lnkverkko, lnkalkusolmu, lnkloppusolmu,
 ajantaspys, paikka, kirjaan, kirjasarake)
VALUES
    -- c->d
    ('1001', '1', '2020-06-03 00:00:00', 1, 1337, '3', 'P', '1', 'c', 'd', '1', '1', '1', 5),
    -- d->e
    ('1001', '1', '2020-06-03 00:00:00', 2, 1338, null, 'X', '1', 'd', 'e', null, null, null, null),
    -- e->f
    ('1001', '1', '2020-06-03 00:00:00', 3, 1339, null, 'X', '1', 'e', 'f', null, null, null, null);
