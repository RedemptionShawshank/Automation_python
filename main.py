import os
def modify_table_ddls_etl(statements, schema_name, suffix):
    original_table_name = statements.split('CREATE OR REPLACE TABLE ')[1].split('\n', 1)[0].strip()

    columns_and_comments = statements.split('(\n', 1)[1].rsplit(')\n', 1)
    columns = columns_and_comments[0]
    comments = columns_and_comments[1]

    modified_statement = f'CREATE OR REPLACE TABLE {schema_name}.{original_table_name}_{suffix}\n(\n'
    modified_statement += columns
    modified_statement += '\tSTG_TBL_NM VARCHAR(50) NULL,\n'
    modified_statement += '\tEVNT_ID VARIANT NULL\n'
    modified_statement += '\n)' + comments + ';\n\n'

    return modified_statement


def modify_alter_etl(statements, schema_name, suffix):

    parts = statements.split('ALTER TABLE ')
    if len(parts) != 2:
        return statements

    original_table_name, rest_statement = parts[1].split(None, 1)

    modified_statement = f'ALTER TABLE {schema_name}.{original_table_name}_{suffix} {rest_statement};'
    modified_statement += '\n'

    return modified_statement
def etl():
##/Users/skumarv3/Documents/PythonExecutable
    script_dir = os.environ.get("MY_PATH")

    # os.chdir(script_dir)
    # print("current working dir",os.getcwd())

    inputFolder = os.path.join(script_dir,"SQL_Templates")
    outputFolder = os.path.join(script_dir,"Output_files")

    inputddls = os.path.join(inputFolder,"input_ddl.sql")
    outputfile = os.path.join(outputFolder,"output_ETL.sql")

    # inputddls = '/Users/skumarv3/PycharmProjects/automation/SQL_Templates/input_ddl.sql'
    # outputfile = '/Users/skumarv3/PycharmProjects/automation/Output_files/output_ETL.sql'

    # input variables
    # schema_name = 'CARE_PLAN_ETL'
    suffix = 'ICUE'
    schema_name = input("Enter schema name: ")


    with open(inputddls, 'r') as file:
        sql_stats = file.read()

    sql_statements = sql_stats.split(';')

    mod_stats = []
    mod_alter = []
    in_create_or_replace_statement = False
    in_alter_statement = False

    for statements in sql_statements:
        statements = statements.strip()
        if statements.strip().startswith('CREATE OR REPLACE TABLE'):
            in_create_or_replace_statement = True
            mod_statements = modify_table_ddls_etl(statements, schema_name, suffix)
            mod_stats.append(mod_statements)
        elif statements.strip().startswith('ALTER TABLE'):
            in_create_or_replace_statement = False
            in_alter_statement = True
            mod_alter_statements = modify_alter_etl(statements, schema_name, suffix)
            mod_alter.append(mod_alter_statements)
        # else:
        #     if statements:
        #         mod_stats.append(statements)

    with open(outputfile, 'a') as file:
        file.writelines(mod_stats)
        file.write('\n')

    with open(outputfile, 'a') as file:
        file.writelines(mod_alter)
        file.write('\n')



def modify_table_ddls_fdn(stmt, schema_name):
    table_name = stmt.split('CREATE OR REPLACE TABLE ')[1].split('\n', 1)[0].strip()

    column_comments = stmt.split('(\n', 1)[1].rsplit(')\n', 1)
    columns = column_comments[0]
    comments = column_comments[1]

    col_list = columns.split(',\n')
    col_list = [column for column in col_list if column.strip().startswith('SRC_REC_PARTN') is False]
    mod_cols = ',\n'.join(col_list)

    modified_stmt = f'CREATE OR REPLACE TABLE {schema_name}.{table_name}\n(\n'
    modified_stmt += mod_cols
    modified_stmt += '\n)' + comments + ';\n\n'

    return modified_stmt

def modify_alter_fdn(stmt, schema_name):
    parts = stmt.split('ALTER TABLE ')
    if len(parts) != 2:
        return stmt

    table_name, rest_statement = parts[1].split(None, 1)

    modified_stmt = f'ALTER TABLE {schema_name}.{table_name} {rest_statement};'
    modified_stmt += '\n'

    return modified_stmt

def fdn():

    script_dir = os.environ.get("MY_PATH")

    inputFolder = os.path.join(script_dir,"SQL_Templates")
    outputFolder = os.path.join(script_dir,"Output_files")

    inputddls = os.path.join(inputFolder,"input_ddl.sql")
    outputfile = os.path.join(outputFolder,"output_FDN.sql")

    # inputddls = '/Users/skumarv3/PycharmProjects/automation/SQL_Templates/input_ddl.sql'
    # outputfile = '/Users/skumarv3/PycharmProjects/automation/Output_files/output_FDN.sql'

    #input
    # schema_name = 'CARE_PLAN_FOUNDATION'
    schema_name = input("Enter schema name: ")


    with open(inputddls, 'r') as file:
        sql_stats = file.read()

    sql_statements = sql_stats.split(';')

    mod_stats = []
    mod_alter = []
    in_CR_table = False
    in_alter = False

    for stmt in sql_statements:
        stmt = stmt.strip()
        if stmt.strip().startswith('CREATE OR REPLACE TABLE'):
            in_CR_table = True
            mod_stmt = modify_table_ddls_fdn(stmt, schema_name)
            mod_stats.append(mod_stmt)
        elif stmt.strip().startswith('ALTER TABLE'):
            in_CR_table = False
            in_alter = True
            mod_alter_stmt = modify_alter_fdn(stmt, schema_name)
            mod_alter.append(mod_alter_stmt)
        # else:
        #     if stmt:
        #         mod_stats.append(stmt)

    with open(outputfile, 'a') as file:
        file.writelines(mod_stats)
        file.write('\n')

    with open(outputfile, 'a') as file:
        file.writelines(mod_alter)
        file.write('\n')


def compaction():
    import pandas as pd

    script_dir = os.environ.get("MY_PATH")

    inputFolder = os.path.join(script_dir,"SQL_Templates")
    outputFolder = os.path.join(script_dir,"Output_files")

    inputpath_proc = os.path.join(inputFolder,"input_pro.sql")
    inputpath_mbrLookup_proc = os.path.join(inputFolder,"mbrLookup_procedure.sql")
    inputpath_stg = os.path.join(inputFolder,"input_stg.sql")
    inputpath_task = os.path.join(inputFolder,"input_task.sql")
    inputpath_stream = os.path.join(inputFolder,"input_stream.sql")


    outputpath = os.path.join(outputFolder,"output.sql")

    # inputpath_proc = "/Users/skumarv3/PycharmProjects/automation/SQL_Templates/input_pro.sql"
    # inputpath_mbrLookup_proc = "/Users/skumarv3/PycharmProjects/automation/SQL_Templates/mbrLookup_procedure.sql"
    # inputpath_stg = "/Users/skumarv3/PycharmProjects/automation/SQL_Templates/input_stg.sql"
    # inputpath_task = "/Users/skumarv3/PycharmProjects/automation/SQL_Templates/input_task.sql"
    # inputpath_stream = "/Users/skumarv3/PycharmProjects/automation/SQL_Templates/input_stream.sql"
    # outputpath = "/Users/skumarv3/PycharmProjects/automation/Output_files/output.sql"

    def input_table_names():
        table_names = []

        while True:
            t_name = input("Enter table name: ")

            if (t_name == ""):
                break

            table_names.append(t_name)

        return table_names

    # input
    table_names = input_table_names()
    # schema_name = input("Enter the schema name: ")
    # error_log = input("Enter the error log name: ")
    # success_log = input("Enter the success log name: ")
    # compactor_name = input("Enter the compactor name: ")

    schema_name = "CARE_PLAN"
    error_log = "ERROR_LOG_CARE_PLAN_ICUE"
    success_log = "SUCCESS_LOG_CARE_PLAN_ICUE"
    compactor_name = "COMPACTOR_CARE_PLAN_ICUE"
    domain = "Individual"

    for table_name in table_names:

        replace_dict_procedure = {
            "tbl_nm": table_name,
            "schema_nm": schema_name,
            "error_log": error_log,
            "success_log": success_log,
            "compactor_name": compactor_name
        }

        replace_dict_stagePipes = {
            "tbl_nm": table_name,
            "schema_nm": schema_name
        }

        lines_to_add_after1 = 'var fields=`'

        home_directory = os.path.expanduser("~")
        download_folder = os.path.join(home_directory,"Downloads")
        file_name = f"{domain}_{table_name}_S2T_Mapping_ICUE_2_Snowflake.xlsx"
        inpath = os.path.join(download_folder,file_name)

        # inpath = f"/Users/skumarv3/Downloads/{domain}_{table_name}_S2T_Mapping_ICUE_2_Snowflake.xlsx"

        df = pd.read_excel(inpath, sheet_name='S2T Mapping Template', skiprows=1)
        table_columns = df['Physical Column Name']
        columnsToAdd_stage = table_columns.tolist()
        columnsProcedure = columnsToAdd_stage

        # removing the column
        columnToRemove = ['SRC_REC_PARTN', 'SRC_REC_GUID']

        columnsProcedure = [col for col in columnsProcedure if col not in columnToRemove]

        if columnToRemove in columnsToAdd_stage:
            columnsToAdd_stage.remove(columnToRemove)

        ################################### PROCEDURE #######################################
        mod_lines_procedure = []
        if "INDV_KEY_VAL" in columnsProcedure:
            additionalColumnsToRemove = ['INDV_KEY_TYP_REF_ID', 'INDV_KEY_TYP_REF_CD', 'INDV_KEY_TYP_REF_DSPL',
                                         'INDV_KEY_TYP_REF_DESC', 'INDV_KEY_VAL', 'ORIG_SYS_REF_ID', 'ORIG_SYS_REF_CD',
                                         'ORIG_SYS_REF_DSPL', 'ORIG_SYS_REF_DESC']
            newProcedureColumns = [col for col in columnsProcedure if col not in additionalColumnsToRemove]
            with open(inputpath_mbrLookup_proc, 'r') as file:
                lines_proc = file.readlines()
            for line in lines_proc:
                for old_str, new_str in replace_dict_procedure.items():
                    line = line.replace(old_str, new_str)
                mod_lines_procedure.append(line)

                if line.strip() == lines_to_add_after1:
                    for i, val in enumerate(newProcedureColumns):
                        if i != len(newProcedureColumns) - 1:
                            mod_lines_procedure.extend(3 * '\t' + str(val) + ',' + '\n')
                        else:
                            mod_lines_procedure.extend(3 * '\t' + str(val) + '\n')
        else:
            with open(inputpath_proc, 'r') as file:
                lines_proc = file.readlines()
            for line in lines_proc:
                for old_str, new_str in replace_dict_procedure.items():
                    line = line.replace(old_str, new_str)
                mod_lines_procedure.append(line)

                if line.strip() == lines_to_add_after1:
                    for i, val in enumerate(columnsProcedure):
                        if i != len(columnsProcedure) - 1:
                            mod_lines_procedure.extend(3 * '\t' + str(val) + ',' + '\n')
                        else:
                            mod_lines_procedure.extend(3 * '\t' + str(val) + '\n')

        with open(outputpath, 'a') as file:
            file.writelines(mod_lines_procedure)
            file.writelines(3 * '\n')

    ########################## STAGES AND PIPES ###########################
    for table_name in table_names:

        replace_dict_stagePipes = {
            "tbl_nm": table_name,
            "schema_nm": schema_name
        }

        lines_to_add_after2 = '('
        lines_to_add_after3 = 'SELECT'

        home_directory = os.path.expanduser("~")
        download_folder = os.path.join(home_directory,"Downloads")
        file_name = f"{domain}_{table_name}_S2T_Mapping_ICUE_2_Snowflake.xlsx"
        inpath = os.path.join(download_folder,file_name)

        # inpath = f"/Users/skumarv3/Downloads/{domain}_{table_name}_S2T_Mapping_ICUE_2_Snowflake.xlsx"

        df = pd.read_excel(inpath, sheet_name='S2T Mapping Template', skiprows=1)
        table_columns = df['Physical Column Name']
        table_columns = table_columns.tolist()

        mod_lines_stagesPipes = []

        with open(inputpath_stg, 'r') as file:
            lines_stg = file.readlines()
        for line in lines_stg:
            for old_str, new_str in replace_dict_stagePipes.items():
                line = line.replace(old_str, new_str)
            mod_lines_stagesPipes.append(line)

            # check for the line
            if line.strip() == lines_to_add_after2:
                for val in table_columns:
                    mod_lines_stagesPipes.extend(3 * '\t' + str(val) + ',' + '\n')
                mod_lines_stagesPipes.extend(3 * '\t' + "STG_TBL_NM," + '\n' + 3 * '\t' + "EVNT_ID" + '\n')

            if line.strip() == lines_to_add_after3:
                for val in table_columns:
                    mod_lines_stagesPipes.extend('T.$1:' + str(val) + ',' + '\n')
                mod_lines_stagesPipes.extend("T.$1:STG_TBL_NM," + '\n' + "T.$1:EVNT_ID" + '\n')

        with open(outputpath, 'a') as file:
            file.writelines(mod_lines_stagesPipes)
            file.writelines(3 * '\n')

    ########################################## TASKS ############################################
    for table_name in table_names:
        replace_dict = {
            "tbl_nm": table_name,
            "schema_nm": schema_name
        }
        mod_lines_tasks = []

        with open(inputpath_task, 'r') as file:
            lines_task = file.readlines()
        for line in lines_task:
            for old_str, new_str in replace_dict.items():
                line = line.replace(old_str, new_str)
            mod_lines_tasks.append(line)

        with open(outputpath, 'a') as file:
            file.writelines(mod_lines_tasks)

    with open(outputpath, 'a') as file:
        file.writelines(3 * '\n')

    ########################################## STREAMS ############################################
    for table_name in table_names:
        replace_dict = {
            "tbl_nm": table_name,
            "schema_nm": schema_name
        }
        mod_lines_streams = []

        with open(inputpath_stream, 'r') as file:
            lines_stream = file.readlines()
        for line in lines_stream:
            for old_str, new_str in replace_dict.items():
                line = line.replace(old_str, new_str)
            mod_lines_streams.append(line)

        with open(outputpath, 'a') as file:
            file.writelines(mod_lines_streams)


if __name__ == '__main__':
    operation = input("enter 1 for ETL ddls, 2 for FDN ddls and 3 for Compaction ")
    if operation=='1':
        etl()
    elif operation=='2':
        fdn()
    elif operation=='3':
        compaction()
    else:
        print("wrong operation")







