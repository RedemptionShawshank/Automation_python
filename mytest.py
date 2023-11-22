def remove_comments(ddl):
    # Remove comments within single quotes
    ddl_no_comments = re.sub(r"'(.*?)'", "", ddl, flags=re.DOTALL)
    return ddl_no_comments

# Apply the function to the sample DDL
ddl_no_comments = remove_comments(ddl_statement)

# Print the result
print(ddl_no_comments)
