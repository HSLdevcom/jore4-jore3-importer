import csv

def fix_coordinate_precision(coordinate):
	while len(coordinate) < 9:
		coordinate += '0'
	return coordinate	

def compare_infrastructure_nodes(row, source, target):
	error = False
	error_message = ''

	#SOLTUNNUS
	if source['soltunnus'] != target['soltunnus']:
		error = True
		error_message += f'Expected soltunnus: {source["soltunnus"]} but found: {target["soltunnus"]},'
	
	#SOLTYYPPI
	source_node_type = source['soltyyppi']
	target_node_type = target['soltyyppi']

	#Municipal border
	if source_node_type == '-':
		if target_node_type != 'B':
			error = True
			error_message += f'Expected soltyyppi: B but found: {target_node_type},'
	#Crossroads
	elif source_node_type == 'X':
		if target_node_type != 'X':
			error = True
			error_message += f'Expected soltyyppi: X but found: {target_node_type},'
	#Bus stop		
	elif source_node_type == 'P':
		if target_node_type != 'S': 
			error = True
			error_message += f'Expected soltyyppi: S but found: {target_node_type},'
	#Unknown
	elif source_node_type == '?':
		if target_node_type != 'U':
			error = True
			error_message += f'Expected soltyyppi: U but found: {target_node_type},'
	else:
		error = True
		error_message += f'Unknown source soltyyppi: {source_node_type},'	

	#SOLOMX
	target_solomx = fix_coordinate_precision(target['solomx'])

	if source['solomx'] != target_solomx:
		error = True
		error_message += f'Expected solomx: {source["solomx"]} but found: {target_solomx},'

	#SOLOMY
	target_solomy = fix_coordinate_precision(target['solomy'])

	if source['solomy'] != target_solomy:
		error = True
		error_message += f'Expected solomy: {source["solomy"]} but found: {target_solomy},'

	#SOLSTMX

	#Only bus stops have a projected location
	if source_node_type == "P":
		target_solstmx = fix_coordinate_precision(target['solstmx'])
		if source['solstmx'] != target_solstmx:
			error = True
			error_message += f'Expected solstmx: {source["solstmx"]} but found: {target_solstmx},'
	else:
		if target['solstmx'] != 'NULL':
			error = True
			error_message += f'Expected solstmx: NULL but found: {target["solstmx"]},'	

	#SOLSTMY

	#Only bus stops have a projected location
	if source_node_type == "P":
		target_solstmy = fix_coordinate_precision(target['solstmy'])
		if source['solstmy'] != target_solstmy:
			error = True
			error_message += f'Expected solstmy: {source["solstmy"]} but found: {target_solstmy},'
	else:
		if target['solstmy'] != 'NULL':
			error = True
			error_message += f'Expected solstmy: NULL but found: {target["solstmy"]},'	

	if error:
		print(f'Error in row #{row}: {error_message}')	

with open('infrastructure_nodes_source.csv', mode='r') as source_csv_file, open('infrastructure_nodes_target.csv', mode='r') as target_csv_file:
	source_csv_reader = csv.DictReader(source_csv_file)
	target_csv_reader = csv.DictReader(target_csv_file)

	source_file_row = 2
	target_file_row = 2

	for source_row in source_csv_reader:
		compare_infrastructure_nodes(source_file_row, source_row, next(target_csv_reader))
		source_file_row += 1	
		target_file_row += 1

	for target_row in target_csv_reader:
		print(f'Extra row #{target_file_row}: {target_row}')
		target_file_row += 1

	if source_file_row != target_file_row:
		#We need to subtract one from both row numbers so that they match with 
		#the actual row counts.
		print(f'ERROR: row count wont match. The source file has: {source_file_row - 1} rows BUT the target file has: {target_file_row - 1} rows')		