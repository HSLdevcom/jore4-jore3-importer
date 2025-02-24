import psycopg
import pymssql
import requests
import simplejson as json

# Replace these to use different target
graphql = 'http://localhost:3201/v1/graphql'
secret = 'hasura'

def get_jore3(stopletter, stopid): 
    tulos = []

    with pymssql.connect("localhost:1433", "sa", "P@ssw0rd", "jore3testdb") as conn:
        
        with conn.cursor(as_dict=True) as cursor:

            cursor.execute(f"""select * from jr_pysakki p
            inner join jr_solmu s on (p.soltunnus = s.soltunnus)
            inner join jr_lij_pysakkialue pa on (p.pysalueid = pa.pysalueid)
            inner join jr_esteettomyys e on (p.soltunnus = e.tunnus)
            inner join jr_varustelutiedot_uusi vt on (p.soltunnus = vt.tunnus)
            where s.solkirjain = '{stopletter}' and s.sollistunnus = '{stopid}'
            order by p.pysviimpvm asc;""")

            
            for row in cursor:
                tulos.append(row)

    if (tulos):
        return tulos[0]

    return ''

def get_jore3_stop_areas():
    tulos = []
    
    with pymssql.connect("localhost:1433", "sa", "P@ssw0rd", "jore3testdb") as conn:
        
        with conn.cursor(as_dict=True) as cursor:

            cursor.execute(f"""select pa.nimi, pa.pysalueid, s.sollistunnus, s.solkirjain from jr_pysakki p
            inner join jr_solmu s on (s.soltunnus = p.soltunnus)
            inner join jr_lij_pysakkialue pa on (p.pysalueid = pa.pysalueid)
            where p.pysalueid in (
            select jp.pysalueid from jr_pysakki jp
            inner join jr_lij_pysakkialue jlp on (jp.pysalueid = jlp.pysalueid)
            where jlp.verkko = 1
            group by jp.pysalueid
            having count(*) > 1);
            """)

            
            for row in cursor:
                tulos.append(row)

    return tulos


def get_jore3_info_spots(stopletter, stopid):
    tulos = []
    with pymssql.connect("localhost:1433", "sa", "P@ssw0rd", "jore3testdb") as conn:
        
        with conn.cursor(as_dict=True) as cursor:

            cursor.execute(f"""select ip.* from jr_pysakki p
            inner join jr_solmu s on (p.soltunnus = s.soltunnus)
            inner join jr_infopaikka ip on (ip.tunnus = p.soltunnus)
            where s.solkirjain = '{stopletter}' and s.sollistunnus = '{stopid}'
            order by p.pysviimpvm asc;""")

            
            for row in cursor:
                tulos.append(row)

    if (tulos):
        return tulos

    return ''


def get_stop_points():
    query = """query {
        service_pattern_scheduled_stop_point {
            label
            measured_location
            priority
            validity_start
            validity_end
        }
    }
    """
    headers = {'content-type': 'application/json; charset=UTF-8',
             'x-hasura-admin-secret': secret}
    response = requests.post(graphql, headers=headers, json={"query": query})
    print(response)
    if (json.loads(response.content)['data']):
        stop_points = json.loads(response.content)['data']['service_pattern_scheduled_stop_point']
        v = [{
            'label': x['label'],
            'lat': x['measured_location']['coordinates'][1],
            'lon': x['measured_location']['coordinates'][0],
            'priority': str(x['priority']),
            'validity_start': x['validity_start'],
            'validity_end': x['validity_end']
            } for x in stop_points]
        return v
    return ''


def get_stop_points2():
    with psycopg.connect("host=localhost port=6432 dbname=jore4e2e user=dbadmin password=adminpassword") as conn:
        with conn.cursor() as cur:
            cur.execute("""
              select label, ST_Y(measured_location::geometry) as lat, ST_X(measured_location::geometry) as lon from service_pattern.scheduled_stop_point;
            """)
            return cur.fetchall()

def update_stop_point(label, netexid):
    mutation = """mutation UpdateStopRef(
      $netexid: String,
      $label: String) {
        update_service_pattern_scheduled_stop_point(where: {label: {_eq: $label}}, _set: {stop_place_ref: $netexid}) {
      affected_rows
      }
    }
    """
    variables = {
      "label": label,
      "netexid": netexid
    }
    headers = {'content-type': 'application/json; charset=UTF-8',
             'x-hasura-admin-secret': secret}
    response = requests.post(graphql, headers=headers, json={"query": mutation, "variables": variables})
    print(response.content)
  

def update_stop_point2(label, netexid):
    with psycopg.connect("host=localhost port=6432 dbname=jore4e2e user=dbadmin password=adminpassword") as conn:
        with conn.cursor() as cur:
            cur.execute(f"""
              update service_pattern.scheduled_stop_point set stop_place_ref = '{netexid}' where label = '{label}';
            """)


def mapStopModel(jore3model):
    match jore3model:
        case '1':
            return 'pullOut'
        case '2':
            return 'busBulb'
        case '4':
            return 'inLane'
        case _:
            return 'other'

def mapStopType(jore3type):
    match jore3type:
        case '01':
            return 'glass'
        case '02':
            return 'steel'
        case '04':
            return 'post'
        case '05':
            return 'urban'
        case '06':
            return 'concrete'
        case '07':
            return 'wooden'
        case _:
            return 'virtual'    
        
def mapStopElectricity(jore3elec):
    match jore3elec:
        case '01':
            return 'none'
        case '02':
            return 'continuous'
        case _:
            return 'light'

def mapStopCondition(jore3condition):
    match jore3condition:
        case '01':
            return 'good'
        case '02':
            return 'mediocre'
        case _:
            return 'bad'

def mapBoolean(jore3value):
    match jore3value:
        case 1:
            return True
        case _:
            return False

def toFloat(value):
    jsonval = json.dumps(value)
    if jsonval != 'null':
        return float(jsonval)
    return None


def update_stop_area(label, name, stopIds):
    stopAreaMutation = """
        mutation AddStopArea(
            $label: String!,
            $name: String,
            $members: [stop_registry_VersionLessEntityRefInput]
        ) {
            stop_registry {
                mutateGroupOfStopPlaces(
                    GroupOfStopPlaces: {
                        name: { lang: "fin", value: $label },
                        description: { lang: "fin", value: $name }
                        members: $members
                        validBetween: {
                            fromDate: "2019-12-31T23:00:00.001+0100"
                            toDate: null
                          }
                    }
                ) {
                    id
                }
            }
        }
    """

    variables = {
        "label": label,
        "name": name,
        "members": [ { "ref": netexId } for netexId in stopIds ]
    }

    headers = {'content-type': 'application/json; charset=UTF-8',
                'x-hasura-admin-secret': secret}
    response = requests.post(graphql, headers=headers, json={"query": stopAreaMutation, "variables": variables})
    print(response.content)


def update_stop_place(label, lat, lon, priority, validityStart, validityEnd, jore3result):
  stopMutation = """
    mutation AddStop(
        $label: String!,
        $idNumber: String!,
        $elyCode: String!,
        $stopName: String!,
        $shortName: String,
        $longName: String,
        $location: String,
        $stopNameSwe: String,
        $shortNameSwe: String,
        $longNameSwe: String,
        $locationSwe: String,
        $postalCode: String,
        $streetAddress: String,
        $functionalArea: String,
        $priority: String,
        $validityStart: String,
        $validityEnd: String,
        $coordinates: stop_registry_Coordinates,
        $hslAccessibilityProperties: stop_registry_HslAccessibilityPropertiesInput,
        $shelterEquipment: [stop_registry_ShelterEquipmentInput],
        $generalSign: [stop_registry_GeneralSignInput]
    ) {
    stop_registry {
        mutateStopPlace(
        StopPlace: {
            accessibilityAssessment: {
                hslAccessibilityProperties: $hslAccessibilityProperties
                limitations: { 
                    audibleSignalsAvailable: FALSE,
                    escalatorFreeAccess: FALSE,
                    liftFreeAccess: FALSE,
                    stepFreeAccess: FALSE,
                    wheelchairAccess: FALSE
                }
            },
            geometry: { type: Point, coordinates: $coordinates },
            description: {lang: "fin", value: $location},
            alternativeNames: [
                { name: {lang: "fin", value: $longName}, nameType: alias },
                { name: {lang: "swe", value: $longNameSwe}, nameType: alias },
                { name: {lang: "swe", value: $locationSwe}, nameType: other },
                { name: {lang: "swe", value: $stopNameSwe}, nameType: translation }
            ],
            name: {lang: "fin", value: $stopName},
            publicCode: $idNumber,
            privateCode: {value: $elyCode, type: "ELY"},
            keyValues: [
              { key: "stopState", values: "InOperation" },
              { key: "mainLine", values: "false" },
              { key: "virtual", values: "false" },
              { key: "postalCode", values: [$postalCode] },
              { key: "functionalArea", values: [$functionalArea] },
              { key: "streetAddress", values: [$streetAddress] },
              { key: "priority", values: [$priority] },
              { key: "validityStart", values: [$validityStart] },
              { key: "validityEnd", values: [$validityEnd] }
            ],
            weighting: interchangeAllowed,
            submode: railReplacementBus,
            quays: [
              {
                publicCode: $label,
                placeEquipments: {
                    shelterEquipment: $shelterEquipment,
                    generalSign: $generalSign
                }
              }
            ],
            transportMode: bus
        }) {
        id
        }
    }
    }
    """


  variables = {
      "label": label,
      "idNumber": jore3result['soltunnus'],
      "elyCode": jore3result['elynumero'],
      "stopName": jore3result['pysnimi'],
      "longName": jore3result['pysnimipitka'],
      "location": jore3result['pyspaikannimi'],
      "stopNameSwe": jore3result['pysnimir'],
      "longNameSwe": jore3result['pysnimipitkar'],
      "locationSwe": jore3result['pyspaikannimir'],
      "postalCode": jore3result['postinro'],
      "streetAddress": jore3result['pysosoite'],
      "functionalArea": str(jore3result['pyssade']),
      "coordinates": [lon, lat],
      "priority": priority,
      "validityStart": validityStart,
      "validityEnd": validityEnd,
      "hslAccessibilityProperties": {
        "curbBackOfRailDistance": toFloat(jore3result['korotus_ajorataan']),
        "lowerCleatHeight": toFloat(jore3result['alapiena_korkeus']),
        "platformEdgeWarningArea": mapBoolean(jore3result['varoitusalue']),
        "stopAreaLengthwiseSlope": toFloat(jore3result['pituuskaltevuus']),
        "stopAreaSideSlope": toFloat(jore3result['sivukaltevuus']),
        "stopAreaSurroundingsAccessible": mapBoolean(jore3result['esteeton_kulku']),
        "stopElevationFromSidewalk": toFloat(jore3result['korotus_kaytavaan']),
        "stopType": mapStopModel(jore3result['pysakin_malli'])
      },
      "generalSign": {
          "numberOfFrames": toFloat(jore3result['kpl_kilvet'])
      },
      "shelterEquipment": ([
        {
          "enclosed": jore3result['pysakkityyppi'] == '01' or jore3result['pysakkityyppi'] == '02',
          "shelterType": mapStopType(jore3result['pysakkityyppi']),
          "shelterElectricity": mapStopElectricity(jore3result['sahko']),
          "shelterLighting": True,
          "shelterCondition": mapStopCondition(jore3result['katos_kunto']),
          "timetableCabinets": toFloat(jore3result['kpl_lisavarusteet']) if jore3result['lisavarusteet'] == '01' else 0,
          "trashCan": mapBoolean(jore3result['roska_astia']),
          "shelterHasDisplay": mapBoolean(jore3result['nayttolaitteet']),
          "bicycleParking": jore3result['lisavarusteet'] == '03' or jore3result['lisavarusteet'] == '04',
          "leaningRail": False,
          "outsideBench": False,
          "shelterFasciaBoardTaping": False
        }
      ] * jore3result['kpl_pysakkityyppi']) if jore3result['kpl_pysakkityyppi'] else None

  }
  headers = {'content-type': 'application/json; charset=UTF-8',
             'x-hasura-admin-secret': secret}
  response = requests.post(graphql, headers=headers, json={"query": stopMutation, "variables": variables})
  print(response)
  print(response.content)
  if (json.loads(response.content)['data']):
    return json.loads(response.content)['data']['stop_registry']['mutateStopPlace'][0]['id']
  return ''

stops = get_stop_points()
print(f"Found {len(stops)} stop points")
added = 0

labelAssociation = {}

for stop in stops:
    jore3stop = get_jore3(stop['label'][0], stop['label'][1:])
    if (jore3stop):
        netexId = update_stop_place(stop['label'], stop['lat'], stop['lon'], stop['priority'], stop['validity_start'], stop['validity_end'], jore3stop)
        if (netexId):
            update_stop_point(stop['label'], netexId)
            labelAssociation[stop['label']] = netexId
            # TODO: Infospots
            #infospots = get_jore3_info_spots(stop[0][0], stop[0][1:])
            #for infospot in infospots:
            #    update_info_spot(stop[0])
            added += 1

print(f"Added {added} stop place definitions")

addedAreas = 0

stopAreas = get_jore3_stop_areas()

collectedAreas = {}
areaNames = {}

for area in stopAreas:
    if (area['sollistunnus']):
        label = area['solkirjain'] + area['sollistunnus']
        if (label in labelAssociation):
            collectedAreas.setdefault(area['pysalueid'], []).append(labelAssociation.get(label))
            areaNames[area['pysalueid']] = area['nimi']

for area, stops in collectedAreas.items():
    if (stops):
        update_stop_area(area, areaNames.get(area), stops)
        addedAreas += 1
    
print(f"Added {addedAreas} stop area definitions")
