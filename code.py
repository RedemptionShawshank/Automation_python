import re

# Specify the input SQL file name and output column names file
input_sql_file = 'input.sql'  # Replace with your file name
output_column_names_file = 'column_names.txt'  # Name of the output file

# Define a function to extract column names from a DDL statement
def extract_column_names(ddl_statement):
    column_pattern = r'\s+([^,()]+)\s'
    columns = re.findall(column_pattern, ddl_statement)
    return columns

# Initialize a list to store DDL statements
ddl_statements = []

# Read the input SQL file and extract DDL statements
with open(input_sql_file, 'r') as file:
    sql_content = file.read()
    ddl_statements = re.split('CREATE OR REPLACE TABLE', sql_content)

# Remove empty and whitespace-only entries
ddl_statements = [stmt.strip() for stmt in ddl_statements if stmt.strip()]

# Initialize a dictionary to store column names for each table
table_columns = {}

# Extract column names for each DDL statement
for statement in ddl_statements:
    table_name = statement.split('\n', 1)[0].strip()
    columns = extract_column_names(statement)
    table_columns[table_name] = columns

# Write the extracted column names to the output file
with open(output_column_names_file, 'w') as output_file:
    for table, columns in table_columns.items():
        output_file.write(f"Columns for {table}:\n")
        for column in columns:
            output_file.write(f"{column}\n")
        output_file.write("\n")

print(f"Column names saved to {output_column_names_file}")
