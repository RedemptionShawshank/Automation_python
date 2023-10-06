--------------------------------------DW_tbl_nm_ICUE--------------------------------
create or replace stage INDV_ETL.DW_tbl_nm_ICUE_STAGE copy_options = (on_error=skip_file);
create or replace pipe DW_tbl_nm_ICUE_PIPE as
copy into schema_nm_ETL.DW_tbl_nm_ICUE
           (
           )
           FROM (
           SELECT

        FROM @DW_tbl_nm_ICUE_STAGE T )
           FILE_FORMAT = (FORMAT_NAME = ECP_JSON_FORMAT_ICUE) on_error = continue;