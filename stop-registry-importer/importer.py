#!/usr/bin/env python3

import datetime
import pymssql
import requests
import simplejson as json
import environ

env = environ.Env(
    GRAPHQL_URL=(str,'http://localhost:3201/v1/graphql'),
    GRAPHQL_SECRET=(str,'hasura'),
    JORE3_USERNAME=(str,'sa'),
    JORE3_PASSWORD=(str,'P@ssw0rd'),
    JORE3_DATABASE_URL=(str,'localhost:1433'),
    JORE3_DATABASE_NAME=(str,'jore3testdb')
)

environ.Env.read_env('.env')

graphql = env('GRAPHQL_URL')
secret = env('GRAPHQL_SECRET')

jore3Username = env('JORE3_USERNAME')
jore3Password = env('JORE3_PASSWORD')
jore3DatabaseUrl = env('JORE3_DATABASE_URL')
jore3DatabaseName = env('JORE3_DATABASE_NAME')

def get_jore3_stops():

    stopPlaces = []
    with pymssql.connect(jore3DatabaseUrl, jore3Username, jore3Password, jore3DatabaseName) as conn:

        with conn.cursor(as_dict=True) as cursor:

            cursor.execute(f"""SELECT * FROM jr_pysakki p
            INNER JOIN jr_solmu s ON (p.soltunnus = s.soltunnus)
            INNER JOIN jr_lij_pysakkialue pa ON (p.pysalueid = pa.pysalueid)
            INNER JOIN jr_varustelutiedot_uusi vt ON (p.soltunnus = vt.tunnus)
            LEFT JOIN jr_esteettomyys e ON (p.soltunnus = e.tunnus)
            ORDER BY p.pysviimpvm ASC;""")
            
            stopPlaces = cursor.fetchall()

    print(f"Found {len(stopPlaces)} stop places")

    stopPlacesByArea = {}

    for x in stopPlaces:
        stopPlacesByArea.setdefault(x['pysalueid'], []).append(x)

    return stopPlacesByArea

def get_jore3_stop_areas():
    with pymssql.connect(jore3DatabaseUrl, jore3Username, jore3Password, jore3DatabaseName) as conn:
        
        with conn.cursor(as_dict=True) as cursor:

            cursor.execute("""SELECT pa.nimi, pa.pysalueid, s.sollistunnus, s.solkirjain FROM jr_pysakki p
            INNER JOIN jr_solmu s ON (s.soltunnus = p.soltunnus)
            INNER JOIN jr_lij_pysakkialue pa ON (p.pysalueid = pa.pysalueid)
            WHERE p.pysalueid IN (
            SELECT jp.pysalueid FROM jr_pysakki jp
            INNER JOIN jr_lij_pysakkialue jlp ON (jp.pysalueid = jlp.pysalueid)
            WHERE jlp.verkko = 1
            GROUP BY jp.pysalueid
            HAVING COUNT(*) > 1);
            """)
            
            for row in cursor:
                yield row

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
    json_data = response.json()
    if not json_data['data']:
        return {}
    
    stop_points = json.loads(response.content)['data']['service_pattern_scheduled_stop_point']
    result_dict = {}
    
    for x in stop_points:
        result_dict.setdefault(x['label'], []).append({
            'label': x['label'],
            'lat': x['measured_location']['coordinates'][1],
            'lon': x['measured_location']['coordinates'][0],
            'priority': str(x['priority']),
            'validity_start': x['validity_start'],
            'validity_end': x['validity_end']
        })

    return result_dict

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
    formatted = response.json()
    if formatted['data']:
        print(f"Scheduled stop point {label} reference updated")
    else:
        print(f"Scheduled stop point {label} reference update failed")

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

def quayInputForJore3Stop(jore3row, label, validityStart, validityEnd, lon, lat):
    return {
      "publicCode": label,
      "privateCode": { 
        "value": jore3row['soltunnus'],
        "type": 'HSL'
      },
      "description": {
        "lang": "fin",
        "value": jore3row['pyspaikannimi']
      },
      "geometry": { 
          "type": "Point",
          "coordinates": [lon, lat]
      },
      "alternativeNames": [
        {
          "name": {
            "value": jore3row['pyspaikannimir'],
            "lang": 'swe'
          },
          "nameType": "other"
        }
      ],
      "keyValues": [
        { 
            "key": "stopState",
            "values": "InOperation"
        },
        { 
            "key": "mainLine",
            "values": "false"
        },
        { 
            "key": "virtual",
            "values": "false"
        },
        { 
            "key": "postalCode",
            "values": [jore3row['postinro']]
        },
        { 
            "key": "functionalArea",
            "values": [str(jore3row['pyssade'])]
        },
        { 
            "key": "streetAddress",
            "values": [jore3row['pysosoite']]
        },
        { 
            "key": "priority",
            "values": ['10']
        },
        { 
            "key": "validityStart",
            "values": [validityStart]
        },
        { 
            "key": "validityEnd",
            "values": [validityEnd]
        }
      ],
      "accessibilityAssessment": {
        "hslAccessibilityProperties": {
          "curbBackOfRailDistance": toFloat(jore3row['korotus_ajorataan']),
          "lowerCleatHeight": toFloat(jore3row['alapiena_korkeus']),
          "platformEdgeWarningArea": mapBoolean(jore3row['varoitusalue']),
          "stopAreaLengthwiseSlope": toFloat(jore3row['pituuskaltevuus']),
          "stopAreaSideSlope": toFloat(jore3row['sivukaltevuus']),
          "stopAreaSurroundingsAccessible": mapBoolean(jore3row['esteeton_kulku']),
          "stopElevationFromSidewalk": toFloat(jore3row['korotus_kaytavaan']),
          "stopType": mapStopModel(jore3row['pysakin_malli'])
        },
        "limitations": { 
          "audibleSignalsAvailable": "FALSE",
          "escalatorFreeAccess": "FALSE",
          "liftFreeAccess": "FALSE",
          "stepFreeAccess": "FALSE",
          "wheelchairAccess": "FALSE"
        }
      },
      "placeEquipments": {
        "shelterEquipment": ([
          {
            "enclosed": jore3row['pysakkityyppi'] == '01' or jore3row['pysakkityyppi'] == '02',
            "shelterType": mapStopType(jore3row['pysakkityyppi']),
            "shelterElectricity": mapStopElectricity(jore3row['sahko']),
            "shelterLighting": True,
            "shelterCondition": mapStopCondition(jore3row['katos_kunto']),
            "timetableCabinets": toFloat(jore3row['kpl_lisavarusteet']) if jore3row['lisavarusteet'] == '01' else 0,
            "trashCan": mapBoolean(jore3row['roska_astia']),
            "shelterHasDisplay": mapBoolean(jore3row['nayttolaitteet']),
            "bicycleParking": jore3row['lisavarusteet'] == '03' or jore3row['lisavarusteet'] == '04',
            "leaningRail": False,
            "outsideBench": False,
            "shelterFasciaBoardTaping": False
          }
          ] * jore3row['kpl_pysakkityyppi']) if jore3row['kpl_pysakkityyppi'] else None,
        "generalSign": {
          "numberOfFrames": toFloat(jore3row['kpl_kilvet'])
        }
      }
    }

def update_stop_place(lat, lon, validityStart, validityEnd, jore3result, quayInput):
  stopMutation2 = """
    mutation AddStopPlace(
        $privateCode: String!,
        $stopName: String!,
        $shortName: String,
        $longName: String,
        $stopNameSwe: String,
        $shortNameSwe: String,
        $longNameSwe: String,
        $validityStart: String,
        $validityEnd: String,
        $coordinates: stop_registry_Coordinates,
        $quays: [stop_registry_QuayInput]
    ) {
    stop_registry {
        mutateStopPlace(
        StopPlace: {
            geometry: { type: Point, coordinates: $coordinates },
            alternativeNames: [
                { name: {lang: "fin", value: $longName}, nameType: alias },
                { name: {lang: "swe", value: $longNameSwe}, nameType: alias },
                { name: {lang: "swe", value: $stopNameSwe}, nameType: translation }
            ],
            name: {lang: "fin", value: $stopName},
            privateCode: {value: $privateCode, type: "HSL"},
            keyValues: [
              { key: "validityStart", values: [$validityStart] },
              { key: "validityEnd", values: [$validityEnd] }
            ],
            quays: $quays,
            transportMode: bus
        }) {
        id
        quays {
            publicCode
            id            
        }
        }
    }
    }
    """

  variables2 = {
      "privateCode": jore3result['pysalueid'],
      "stopName": jore3result['pysnimi'],
      "longName": jore3result['pysnimipitka'],
      "stopNameSwe": jore3result['pysnimir'],
      "longNameSwe": jore3result['pysnimipitkar'],
      "coordinates": [lon, lat],
      "validityStart": validityStart,
      "validityEnd": validityEnd,
      "quays": quayInput
  }

  headers = {'content-type': 'application/json; charset=UTF-8',
                'x-hasura-admin-secret': secret}
  response = requests.post(graphql, headers=headers, json={"query": stopMutation2, "variables": variables2})

  formatted = response.json()

  if formatted['data']:
    return formatted['data']['stop_registry']['mutateStopPlace'][0]['quays']
  if formatted['errors']:
    if formatted['errors'][0]['message']:
        print(formatted['errors'][0]['message'])
    else:
        print(f"Stop place {jore3result['pysalueid']} update failed!")

  return {}

startTime = datetime.datetime.now()

stopPoints = get_stop_points()
print(f"Found {len(stopPoints)} stop points")
added = 0

stopPlaces = get_jore3_stops()
index = 0

for stopArea in get_jore3_stop_areas():
    index += 1
    print(f"Handling stop area {index}")
    quayInput = []
    latCoords = []
    lonCoords = []
    validityStarts = []
    validityEnds = []
    lastStop = None
    try:
        for stop in stopPlaces[stopArea['pysalueid']]:
            try:
                stopLabel = stop['solkirjain'] + stop['sollistunnus']
                if not stopLabel in stopPoints:
                    continue
                stopPoint = stopPoints[stopLabel][0]
                lat = stopPoint['lat']
                lon = stopPoint['lon']
                validityStart = stopPoint['validity_start']
                validityEnd = stopPoint['validity_end']
                quayInput.append(quayInputForJore3Stop(stop, stopPoint['label'], validityStart, validityEnd , lon, lat))
                latCoords.append(lat)
                lonCoords.append(lon)
                validityStarts.append(validityStart)
                validityEnds.append(validityEnd)
                lastStop = stop
            except:
                print(f"Failed to handle stop {stop['soltunnus']}: {stop['pysnimi']}")
        if (len(lonCoords) > 0 and len(latCoords) > 0 and len(validityStarts) > 0 and len(validityEnds) > 0):

            # Average coordinates of quays for the stop place
            stopPlaceLon = sum(lonCoords) / len(lonCoords)
            stopPlaceLat = sum(latCoords) / len(latCoords)

            # Use min / max validity period of the stop points for the stop place
            stopPlaceValidityStart = min(validityStarts)
            stopPlaceValidityEnd = max(validityEnds)

            netexIds = update_stop_place(stopPlaceLat, stopPlaceLon, stopPlaceValidityStart, stopPlaceValidityEnd, lastStop, quayInput)
            if (netexIds):
                added += 1 
                for netexAssociation in netexIds:
                    update_stop_point(netexAssociation['publicCode'], netexAssociation['id'])

    except Exception as e:
        print(f"Failed to handle stop area {stopArea['pysalueid']}")
        print(e)

endTime = datetime.datetime.now()
duration = endTime - startTime
print(f"Added {added} stop places")
minutes = duration.seconds // 60
print(f"Import took {minutes} minutes {duration.seconds - (minutes * 60)} seconds")
