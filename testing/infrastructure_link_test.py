import csv

def fix_coordinate_precision(coordinate):
	while len(coordinate) < 9:
		coordinate += '0'
	return coordinate	

def compare_infrastructure_links(row, source, target):
	error = False
	error_message = ''

	#LNK_ID
	if source['lnk_id'] != target['lnk_id']:
		error = True
		error_message += f'Expected lnk_id: {source["lnk_id"]} but found: {target["lnk_id"]},'
	
	#LNKVERKKO
	source_link_type = source['lnkverkko']
	target_link_type = target['lnkverkko']

	#Road
	if source_link_type == '1':
		if target_link_type != 'road':
			error = True
			error_message += f'Expected lnkverkko: road but found: {target_link_type},'
	#Metro track
	elif source_link_type == '2':
		if target_link_type != 'metro_track':
			error = True
			error_message += f'Expected lnkverkko: metro_track but found: {target_link_type},'
	#Tram track				
	elif source_link_type == '3':
		if target_link_type != 'tram_track':
			error = True
			error_message += f'Expected lnkverkko: tram_track but found: {target_link_type},'
	#Railway
	elif source_link_type == '4':
		if target_link_type != 'railway':
			error = True
			error_message += f'Expected lnkverkko: railway but found: {target_link_type},'
	#Waterway
	elif source_link_type == '7':
		if target_link_type != 'waterway':
			error = True
			error_message += f'Expected lnkverkko: waterway but found: {target_link_type},'
	#Unknown
	elif source_link_type == '-1':
		if target_link_type != 'unknown':
			error = True
			error_message += f'Expected lnkverkko: unknown but found: {target_link_type},'
	#Unknown source lnkverkko
	else:
		error = True
		error_message += f'Unknown source lnkverkko: {source_link_type},'					
				

	#ALKU_SOLTUNNUS
	if source['alku_soltunnus'] != target['alku_soltunnus']:
		error = True
		error_message += f'Expected alku_soltunnus: {source["alku_soltunnus"]} but found: {target["alku_soltunnus"]},'

	#ALKU_SOLOMX
	target_alku_solomx = fix_coordinate_precision(target['alku_solomx'])

	if source['alku_solomx'] != target_alku_solomx:
		error = True
		error_message += f'Expected alku_solomx: {source["alku_solomx"]} but found: {target_alku_solomx},'

	#ALKU_SOLOMY
	target_alku_solomy = fix_coordinate_precision(target['alku_solomy'])

	if source['alku_solomy'] != target_alku_solomy:
		error = True
		error_message += f'Expected alku_solomy: {source["alku_solomy"]} but found: {target_alku_solomy},'

	#LOPPU_SOLTUNNUS
	if source['loppu_soltunnus'] != target['loppu_soltunnus']:
		error = True
		error_message += f'Expected loppu_soltunnus: {source["loppu_soltunnus"]} but found: {target["loppu_soltunnus"]},'

	#LOPPU_SOLOMX
	target_loppu_solomx = fix_coordinate_precision(target['loppu_solomx'])

	if source['loppu_solomx'] != target_loppu_solomx:
		error = True
		error_message += f'Expected loppu_solomx: {source["loppu_solomx"]} but found: {target_loppu_solomx},'

	#LOPPU_SOLOMY
	target_loppu_solomy = fix_coordinate_precision(target['loppu_solomy'])

	if source['loppu_solomy'] != target_loppu_solomy:
		error = True
		error_message += f'Expected loppu_solomy: {source["loppu_solomy"]} but found: {target_loppu_solomy},'	

	if error:
		print(f'Error in row #{row}: {error_message}')	

with open('infrastructure_links_source.csv', mode='r') as source_csv_file, open('infrastructure_links_target.csv', mode='r') as target_csv_file:
	source_csv_reader = csv.DictReader(source_csv_file)
	target_csv_reader = csv.DictReader(target_csv_file)

	source_file_row = 2
	target_file_row = 2

	for source_row in source_csv_reader:
		compare_infrastructure_links(source_file_row, source_row, next(target_csv_reader))
		source_file_row += 1	
		target_file_row += 1

	for target_row in target_csv_reader:
		print(f'Extra row #{target_file_row}: {target_row}')
		target_file_row += 1

	if source_file_row != target_file_row:
		#We need to subtract one from both row numbers so that they match with 
		#the actual row counts.
		print(f'ERROR: row count wont match. The source file has: {source_file_row - 1} rows BUT the target file has: {target_file_row - 1} rows')		