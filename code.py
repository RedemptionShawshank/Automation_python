import re

# Specify the input SQL file name
input_sql_file = 'input.sql'  # Replace with your file name

# Define a function to extract column names from a DDL statement
def extract_column_names(ddl_statement):
    # Use regular expressions to find column names
    column_pattern = r'\s+([^,()]+)\s'
    columns = re.findall(column_pattern, ddl_statement)
    return columns

# Initialize a list to store DDL statements
ddl_statements = []

# Read the input SQL file and extract DDL statements
with open(input_sql_file, 'r') as file:
    sql_content = file.read()
    # Use a regular expression to split DDL statements
    ddl_statements = re.split('CREATE OR REPLACE TABLE', sql_content)

# Remove empty and whitespace-only entries
ddl_statements = [stmt.strip() for stmt in ddl_statements if stmt.strip()]

# Initialize a dictionary to store column names for each table
table_columns = {}

# Extract column names for each DDL statement
for statement in ddl_statements:
    # Extract the table name
    table_name = statement.split('\n', 1)[0].strip()
    # Extract column names using the function
    columns = extract_column_names(statement)
    # Store the column names in the dictionary
    table_columns[table_name] = columns

# Print the extracted column names for each table
for table, columns in table_columns.items():
    print(f"Columns for {table}: {columns}")
  
