# Enumerated tables

Jore 3 contains a table (`jr_koodisto`), which stores several different types of enumerated values.

```tsql
create table jr_koodisto
(
    koolista      varchar(20)  not null,
    koojarjestys  numeric(3),
    kookoodi      varchar(20)  not null,
    kooselite     varchar(100) not null,
    koonumero     int,
    tyyppinumero1 int,
    tyyppinimi1   varchar(30),
    tyyppinimi2   varchar(60),
    koodityyppi   varchar(50)
)
create unique clustered index jr_koodisto_cind
    on jr_koodisto (koolista, kookoodi)
```

The enumerated types are stored as follows:

- `koolista`: The name of the enum type, e.g. `Tilaajaorganisaatio`
- `kookoodi`: The "primary key" of the enum value, used in tables which reference `jr_koodisto`, e.g. `HKI`
- `kooselit`: The description of the enum value, used in the UI, e.g. `Helsinki`
- `koojarjestys`: The sorting order of the enum values, for the UI.

_Most_ of the enumerated types are modified seldom => we can model them as Java enumerations.
