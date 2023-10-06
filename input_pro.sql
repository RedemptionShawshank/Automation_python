--------------------------DW_tbl_nm_ICUE---------------------------
create or replace procedure schema_nm_ETL.DW_tbl_nm_ICUE_PROC()
        RETURNS String
        LANGUAGE javascript AS
$$
            var streamName=`schema_nm_ETL.DW_tbl_nm_ICUE_STRM`;
            var foundationTable=`schema_nm_FOUNDATION.DW_tbl_nm`;
            var fields=`
            `;
            var error_table = foundationTable;
            var keys=`DW_tbl_nm_REC_ID`;
            var stmt = snowflake.createStatement({
            sqlText: 'CALL schema_nm_ETL.compactor_name(:1, :2, :3, :4, :5)',
            binds: [streamName, foundationTable, fields, keys,5000000]
                });
try {
                    var result1= stmt.execute();
                    result1.next();
                  query =`DW_tbl_nm_ICUE_PROC()`+` Compaction Successful `;
                    var success_statement = snowflake.createStatement({ sqlText:
                    `INSERT INTO schema_nm_ETL.success_log (table_name ,query_name )
                        VALUES (?,?) ` ,binds: [error_table,query] });
                    success_statement.execute();
                    return result1.getColumnValue(1); }
catch(err){
            error_query=`DW_tbl_nm_ICUE_PROC()`+` Compaction Issues `;
            var error_log_um_statement=snowflake.createStatement({sqlText:
            `insert into schema_nm_ETL.error_log(error_table , error_query ,error_code , error_state , error_message , stack_trace )
                        VALUES (?,?,?,?,?,?) `
                      ,binds: [error_table,error_query,err.code, err.state, err.message, err.stackTraceTxt]
                      });
                            error_log_um_statement.execute();
                            snowflake.execute({ sqlText: "commit" });
                            return err;
   }
$$;