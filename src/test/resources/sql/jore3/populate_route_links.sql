INSERT INTO jr_reitinlinkki
(reitunnus, suusuunta, suuvoimast, reljarjnro, relid, relohaikpys, relpysakki, lnkverkko, lnkalkusolmu, lnkloppusolmu,
 ajantaspys, paikka, kirjaan, kirjasarake)
VALUES
    -- 1000003->1000004
    ('1001', '1', '2020-06-03 00:00:00', 1, 1337, '3', 'P', '1', '1000003', '1000004', '1', '1', '1', 5),
    -- 1000004->1000005
    ('1001', '1', '2020-06-03 00:00:00', 2, 1338, null, 'X', '1', '1000004', '1000005', null, null, null, null),
    -- 1000005->1000006
    ('1001', '1', '2020-06-03 00:00:00', 3, 1339, null, 'X', '1', '1000005', '1000006', null, null, null, null);
