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





import subprocess

def get_cursor_coordinates():
    command = "ioreg -c IOHIDSystem | awk '/HIDMouse/ || /HIDTablet/ {print $1, $2, $3, $10, $11, $12}' | sed 's/[^[:digit:][:space:]]//g'"
    result = subprocess.run(command, shell=True, stdout=subprocess.PIPE, text=True)
    output = result.stdout.strip().split()
    
    if len(output) == 2:
        x_coordinate, y_coordinate = map(int, output)
        return x_coordinate, y_coordinate
    else:
        return None

# Example usage
coordinates = get_cursor_coordinates()
if coordinates:
    print(f"Cursor Coordinates: {coordinates}")
else:
    print("Unable to retrieve cursor coordinates.")
    



import pyautogui

def get_cursor_coordinates():
    try:
        x, y = pyautogui.position()
        return x, y
    except Exception as e:
        print(f"Error: {e}")
        return None

# Example usage
coordinates = get_cursor_coordinates()
if coordinates:
    print(f"Cursor Coordinates: {coordinates}")
else:
    print("Unable to retrieve cursor coordinates.")







Stored procedure execution error: Scoped transaction started in stored procedure is incomplete and it was rolled back.




import subprocess
import pyautogui as pg
import time

def switch_app(app_name):
    applescript_command = f'tell application "{app_name}" to activate'
    subprocess.run(["osascript", "-e", applescript_command])


switch_app("Microsoft Teams classic")
cd = [(1697,60),(1475,157),(1536,331)]
pg.moveTo(cd[0][0],cd[0][1])
time.sleep(1)
for coordinates in cd:
    pg.click(coordinates[0],coordinates[1])
    pg.PAUSE=1
    pg.mouseUp(coordinates[0], coordinates[1])



import subprocess
import pyautogui as pg
import time

def switch_app(app_name):
    applescript_command = f'tell application "{app_name}" to activate'
    subprocess.run(["osascript", "-e", applescript_command])


app_a = "Microsoft Teams classic"
app_b = "Google Chrome"

interval = 5

try:
    while True:
        switch_app(app_a)

        time.sleep(interval)
        # pg.leftClick(x=244,y=101)
        pg.mouseDown(x=1697,y=60)
        pg.PAUSE = 1
        pg.mouseUp(x=1697, y=60)

        switch_app(app_b)

        time.sleep(interval)

except KeyboardInterrupt:
    print("terminated")
    
