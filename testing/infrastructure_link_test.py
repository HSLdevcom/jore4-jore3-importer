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
	link_type_mapping = dict({'1': 'road', '2': 'metro_track', '3': 'tram_track', '4': 'railway', '7': 'waterway', '-1': 'unknown'})

	if source_link_type in link_type_mapping:
		if link_type_mapping[source_link_type] != target_link_type:
			error = True
			error_message += f'Expected lnkverkko: {link_type_mapping[source_link_type]} but found: {target_link_type},'
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