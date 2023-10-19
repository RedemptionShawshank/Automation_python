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


import re

# Sample input containing DDL statements
input_sql = """
CREATE OR REPLACE TABLE table1
(
       Col1 STRING(1000) NOT NULL COMMENT 'this is a comment
additional comment
additional comment',
       Col2 STRING(1000) NOT NULL COMMENT 'this is a comment
comment',
       Col3 STRING(1000) NOT NULL COMMENT 'this is a comment'
)
COMMENT = 'this is comment';

CREATE OR REPLACE TABLE table2
(
       Col4 STRING(1000) NOT NULL COMMENT 'this is a comment',
       Col5 STRING(1000) NOT NULL COMMENT 'this is a comment
multiline comment'
)
COMMENT = 'this is comment
multiline comment';

CREATE OR REPLACE TABLE table3
(
       Col5 STRING(1000) NOT NULL COMMENT 'this is a comment',
       Col6 STRING(1000) NOT NULL COMMENT 'this is a comment',
       Col7 STRING(1000) NOT NULL COMMENT 'this is a comment'
)
COMMENT = 'this is comment';
"""

# Define a function to extract column names from a DDL statement
def extract_column_names(ddl_statement):
    # Use regular expressions to find column names
    column_pattern = r'(\w+)\s\w+\(\d+\)'
    columns = re.findall(column_pattern, ddl_statement)
    return columns

# Split the input SQL into individual DDL statements
ddl_statements = input_sql.split('CREATE OR REPLACE TABLE')[1:]

# Initialize a dictionary to store column names for each table
table_columns = {}

# Extract column names for each DDL statement
for statement in ddl_statements:
    table_name = statement.split('\n', 1)[0].strip()
    columns = extract_column_names(statement)
    table_columns[table_name] = columns

# Write the extracted column names to an output file
output_file = 'column_names.txt'
with open(output_file, 'w') as file:
    for table, columns in table_columns.items():
        file.write(f'Columns for {table}:\n')
        file.write('\n'.join(columns))
        file.write('\n\n')

print(f"Column names saved to {output_file}")

