------------------------------DW_tbl_nm_ICUE-------------------------
CREATE OR REPLACE task schema_nm_ETL.DW_tbl_nm_ICUE_TASK warehouse = ECT_DEV_CM_LOAD_WH  schedule = '30 minutes' USER_TASK_TIMEOUT_MS = 10800000
             as call schema_nm_ETL.DW_tbl_nm_ICUE_PROC();
