#!/usr/bin/env python3

import sys

if sys.version_info[0] != 3:
    raise Exception("Python 3 is required to run this script.")

import pymssql
import requests
import simplejson as json

# Replace these to use different target
graphql = 'http://localhost:3201/v1/graphql'
secret = 'hasura'

jore3Username = 'sa'
jore3Password = 'P@ssw0rd'
jore3DatabaseUrl = "localhost:1433"
jore3DatabaseName = "jore3testdb"

def get_jore3_stops_for_area(pysakki_alue):
    tulos = []

    with pymssql.connect(jore3DatabaseUrl, jore3Username, jore3Password, jore3DatabaseName) as conn:
        
        with conn.cursor(as_dict=True) as cursor:

            cursor.execute(f"""select * from jr_pysakki p
            inner join jr_solmu s on (p.soltunnus = s.soltunnus)
            inner join jr_lij_pysakkialue pa on (p.pysalueid = pa.pysalueid)
            inner join jr_esteettomyys e on (p.soltunnus = e.tunnus)
            inner join jr_varustelutiedot_uusi vt on (p.soltunnus = vt.tunnus)
            where p.pysalueid = '{pysakki_alue}'
            order by p.pysviimpvm asc;""")
            
            for row in cursor:
                tulos.append(row)

    if (tulos):
        return tulos

    return ''

def get_jore3_stop_areas():
    tulos = []
    
    with pymssql.connect(jore3DatabaseUrl, jore3Username, jore3Password, jore3DatabaseName) as conn:
        
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
    with pymssql.connect(jore3DatabaseUrl, jore3Username, jore3Password, jore3DatabaseName) as conn:
        
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
        result_dict = {}
        values = [{
            'label': x['label'],
            'lat': x['measured_location']['coordinates'][1],
            'lon': x['measured_location']['coordinates'][0],
            'priority': str(x['priority']),
            'validity_start': x['validity_start'],
            'validity_end': x['validity_end']
            } for x in stop_points]
        for v in values:
            result_dict.setdefault(v['label'], []).append(v)
        return result_dict
    return ''

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

  print(response)
  print(response.content)

  if (json.loads(response.content)['data']):
    return json.loads(response.content)['data']['stop_registry']['mutateStopPlace'][0]['quays']

  return {}

stopPoints = get_stop_points()
print(f"Found {len(stopPoints)} stop points")
added = 0
stopAreas = get_jore3_stop_areas()
print(f"Found {len(stopAreas)} stop areas")

for stopArea in stopAreas:
    areaStops = get_jore3_stops_for_area(stopArea['pysalueid'])
    if (areaStops):
        quayInput = []
        latCoords = []
        lonCoords = []
        validityStarts = []
        validityEnds = []
        for stop in areaStops:
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
            except:
                print("Failed to handle stop {stop}")
        if (len(lonCoords) > 0 and len(latCoords) > 0 and len(validityStarts) > 0 and len(validityEnds) > 0):
            # Average coordinates of quays for the stop place
            stopPlaceLon = sum(lonCoords) / len(lonCoords)
            stopPlaceLat = sum(latCoords) / len(latCoords)

            # Use min / max validity period of the stop points for the stop place
            stopPlaceValidityStart = min(validityStarts)
            stopPlaceValidityEnd = max(validityEnds)

            netexIds = update_stop_place(stopPlaceLat, stopPlaceLon, stopPlaceValidityStart, stopPlaceValidityEnd, areaStops[0], quayInput)
            if (netexIds):
                added += 1 
                for netexAssociation in netexIds:
                    update_stop_point(netexAssociation['publicCode'], netexAssociation['id'])

    
print(f"Added {added} stop places")
