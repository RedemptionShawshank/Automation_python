----------------------------DW_tbl_nm_ICUE-----------------------------
CREATE OR REPLACE stream schema_nm_ETL.DW_tbl_nm_ICUE_STRM ON TABLE schema_nm_ETL.DW_tbl_nm_ICUE APPEND_ONLY = TRUE SHOW_INITIAL_ROWS = TRUE;
