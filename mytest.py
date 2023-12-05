import re

# Read the SQL file
input_sql_file = 'your_file.sql'  # Replace with your SQL file name

with open(input_sql_file, 'r') as file:
    sql_content = file.read()

# Define a function to remove comments from the DDL
def remove_comments(ddl):
    # Remove comments within single quotes
    ddl_no_comments = re.sub(r"'(.*?)'", "", ddl, flags=re.DOTALL)
    return ddl_no_comments

# Split SQL content into separate DDL statements
ddl_statements = re.split(r';\s*\n*', sql_content)

# Apply the function to each DDL statement
ddl_statements_no_comments = [remove_comments(ddl) for ddl in ddl_statements]

# Join the modified DDL statements into a single string
sql_content_no_comments = ';\n'.join(ddl_statements_no_comments)

# Print or save the result
print(sql_content_no_comments)




import subprocess

def switch_app(app_name):
    applescript_command = f'tell application "{app_name}" to activate'
    subprocess.run(["osascript", "-e", applescript_command])

# Example: Switch to Safari
switch_app("Safari")



ioreg -c IOHIDSystem | awk '/HIDMouse/ || /HIDTablet/ {print $1, $2, $3, $10, $11, $12}' | sed 's/[^[:digit:][:space:]]//g'


