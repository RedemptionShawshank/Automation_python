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
            var sqlQuery = `UPDATE INDV_FOUNDATION.DW_tbl_nm AS A
            SET A.INDV_KEY_TYP_REF_CD = B.INDV_KEY_TYP_REF_CD, A.INDV_KEY_TYP_REF_DESC = B.INDV_KEY_TYP_REF_DESC, A.INDV_KEY_TYP_REF_DSPL=B.INDV_KEY_TYP_REF_DSPL, A.INDV_KEY_TYP_REF_ID=B.INDV_KEY_TYP_REF_ID, A.INDV_KEY_VAL=B.INDV_KEY_VAL, A.ORIG_SYS_REF_CD=B.ORIG_SYS_REF_CD, A.ORIG_SYS_REF_DESC=B.ORIG_SYS_REF_DESC, A.ORIG_SYS_REF_ID=B.ORIG_SYS_REF_ID, A.ORIG_SYS_REF_DSPL=B.ORIG_SYS_REF_DSPL
            FROM HSC_ETL.DW_MBR AS B
            WHERE A.INDV_SRC_ID::VARCHAR = B.MBR_ID::VARCHAR AND A.INDV_KEY_VAL IS NULL AND A.DW_SYS_REF_CD = 'ICUE'`;

            var keys=`DW_tbl_nm_REC_ID`;
            var stmt = snowflake.createStatement({
            sqlText: 'CALL schema_nm_ETL.compactor_name(:1, :2, :3, :4, :5)',
            binds: [streamName, foundationTable, fields, keys,5000000]
                });
            var stmt1 = snowflake.createStatement({ sqlText:sqlQuery });
                try {
                    var result1= stmt.execute();
                    result1.next();
                  query =`DW_tbl_nm_ICUE_PROC()`+` Compaction Successful `;
                    var success_statement = snowflake.createStatement({ sqlText: `
                    INSERT
                        INTO
                            schema_nm_ETL.success_log (table_name ,
                            query_name )
                        VALUES (?,
                        ?) ` ,
                        binds: [error_table,
                        query] });
                    success_statement.execute();
                } catch(err){
                    error_query=`DW_tbl_nm_ICUE_PROC()`+` Compaction Issues `;
                    var error_log_um_statement=snowflake.createStatement({
                      sqlText:  `insert into schema_nm_ETL.error_log (error_table , error_query ,error_code , error_state , error_message , stack_trace ) VALUES (?,?,?,?,?,?) `
                      ,binds: [error_table,error_query,err.code, err.state, err.message, err.stackTraceTxt]
                      });
                            error_log_um_statement.execute();
                            snowflake.execute({ sqlText: "commit" });
                            return err;
   }
              try {
                            var result2= stmt1.execute();
                               result2.next();
                            query =`DW_tbl_nm_POST_LOOKUP()`+` Compaction Successful `;
                            var success_statement = snowflake.createStatement({ sqlText: `
                            INSERT
                                INTO
                                     schema_nm_ETL.success_log (table_name ,
                                    query_name )
                                VALUES (?,
                                ?) ` ,
                                binds: [error_table,
                                query] });
                            success_statement.execute();
                                return result2.getColumnValue(1);

                   } catch(err){
                           error_query=`DW_tbl_nm_POST_LOOKUP()`+` Compaction Issues `;
                           var error_log_statement=snowflake.createStatement({
                              sqlText:  `insert into schema_nm_ETL.error_log (error_table , error_query ,error_code , error_state , error_message , stack_trace ) VALUES (?,?,?,?,?,?) `
                              ,binds: [error_table,error_query,err.code, err.state, err.message, err.stackTraceTxt]
                              });
                                    error_log_statement.execute();
                                 snowflake.execute({ sqlText: "commit" });

                                    return err;
                            }
$$;