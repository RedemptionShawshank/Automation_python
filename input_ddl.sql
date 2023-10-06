
CREATE OR REPLACE TABLE DW_MBR_SRVC_SCHED
(
	DW_MBR_SRVC_SCHED_REC_ID STRING(1000) NOT NULL  COMMENT 'Primary Key of the Record. Usually Concatenation of Natural Key ( DW_SYS_REF_CD, MBR_ID, SRVC_ENT_TYP_ID,SRVC_ENT_SEQ_NBR,SRVC_SCHED_SEQ_NBR)',
	DW_SYS_REF_CD STRING(50) NOT NULL  COMMENT 'Identity of the system which sent the data to OCM Snowflake',
	SRC_SYS_CREAT_DTTM TIMESTAMP NULL  COMMENT 'The date and time the object was created on the source system.',
	SRC_SYS_CREAT_USER_ID STRING(50) NULL  COMMENT 'The Id of who or what performed the object creation on the source system. This can be a person or process.',
	SRC_SYS_UPDT_DTTM TIMESTAMP NULL  COMMENT 'The date and time the object was updated on the source system.',
	SRC_SYS_UPDT_USER_ID STRING(50) NULL  COMMENT 'The Id of who or what performed the object update on the source system. This can be a person or process.',
	SRC_SYS_UPDT_VER_NBR NUMBER(10) NULL  COMMENT 'Most recent version of updated data. This field will be used to sync between legacy applications and new application.',
	SRC_SYS_INAC_IND STRING(10) NULL  COMMENT 'inactive indicator',
	SRC_SYS_REC_STS_CD STRING(50) NOT NULL  COMMENT 'A code indicating the status of the object. Examples: - Active - Inactive - SoftLogical Delete',
	SRC_SYS_DATA_QLTY_ISS_RULE_LIST VARIANT NULL  COMMENT 'json formated data quality issue associated with the record.

initial to record unmatched reference values from source, e.g.,  iMDM and CDB, and OCM default %ref_id = 0 (unknown).',
	SRC_SYS_DATA_SECUR_RULE_LIST VARIANT NULL  COMMENT 'data access/security rule list, a json array',
	CREAT_SYS_REF_ID INTEGER NULL  COMMENT 'reference for source application.',
	CREAT_SYS_REF_CD STRING(50) NULL  COMMENT 'reference for source application.',
	CREAT_SYS_REF_DSPL STRING(100) NULL  COMMENT 'reference for source application.',
	CREAT_SYS_REF_DESC STRING(500) NULL  COMMENT 'reference for source application.',
	CHG_SYS_REF_ID INTEGER NULL  COMMENT 'reference for update application.',
	CHG_SYS_REF_CD STRING(50) NULL  COMMENT 'reference for update application.',
	CHG_SYS_REF_DSPL STRING(100) NULL  COMMENT 'reference for update application.',
	CHG_SYS_REF_DESC STRING(500) NULL  COMMENT 'reference for update application.',
	INDV_SRC_ID STRING(30) NULL  COMMENT 'Individual ID or Member ID of the source identified in Create System Reference attributes.
For ICUE - this is the Member Sequence ID.',
	INDV_KEY_TYP_REF_ID INTEGER NULL  COMMENT 'Enterprise Member Identifier Type',
	INDV_KEY_TYP_REF_CD STRING(50) NULL  COMMENT 'Enterprise Member Identifier Type',
	INDV_KEY_TYP_REF_DSPL STRING(100) NULL  COMMENT 'Enterprise Member Identifier Type',
	INDV_KEY_TYP_REF_DESC STRING(500) NULL  COMMENT 'Enterprise Member Identifier Type',
	INDV_KEY_VAL STRING(100) NULL  COMMENT 'Actual Value for the Enterprise Individual Identifier Key Type',
	ORIG_SYS_REF_ID INTEGER NULL  COMMENT 'reference of system that feeds the member record to the enterprise eligibility system (CDB), which consolidates eligibility from multiple (originating or intermediate, e.g., CES) systems. Examples are

CO - Cosmos
CS - CES
GR - Golden Rule
OP - Optum
PR - Prime
UB - UBH

etc...

True originating system is captured under elig_sys_typ_id
',
	ORIG_SYS_REF_CD STRING(50) NULL  COMMENT 'reference of system that feeds the member record to the enterprise eligibility system (CDB), which consolidates eligibility from multiple (originating or intermediate, e.g., CES) systems. Examples are

CO - Cosmos
CS - CES
GR - Golden Rule
OP - Optum
PR - Prime
UB - UBH

etc...

True originating system is captured under elig_sys_typ_id',
	ORIG_SYS_REF_DSPL STRING(100) NULL  COMMENT 'reference of system that feeds the member record to the enterprise eligibility system (CDB), which consolidates eligibility from multiple (originating or intermediate, e.g., CES) systems. Examples are

CO - Cosmos
CS - CES
GR - Golden Rule
OP - Optum
PR - Prime
UB - UBH

etc...

True originating system is captured under elig_sys_typ_id',
	ORIG_SYS_REF_DESC STRING(500) NULL  COMMENT 'reference of system that feeds the member record to the enterprise eligibility system (CDB), which consolidates eligibility from multiple (originating or intermediate, e.g., CES) systems. Examples are

CO - Cosmos
CS - CES
GR - Golden Rule
OP - Optum
PR - Prime
UB - UBH

etc...

True originating system is captured under elig_sys_typ_id',
	SRVC_SCHED_SEQ_NBR NUMBER(5) NULL  COMMENT 'system generated sequence number to uniquely identify a schedule for member''s service.',
	SRVC_ENT_TYP_REF_ID INTEGER NULL  COMMENT 'unique id of where the service is enterred into iCUE, e.g., HSC, Service Task, Informal Support.',
	SRVC_ENT_TYP_REF_CD STRING(50) NULL  COMMENT 'unique id of where the service is enterred into iCUE, e.g., HSC, Service Task, Informal Support.',
	SRVC_ENT_TYP_REF_DSPL STRING(100) NULL  COMMENT 'unique id of where the service is enterred into iCUE, e.g., HSC, Service Task, Informal Support.',
	SRVC_ENT_TYP_REF_DESC STRING(500) NULL  COMMENT 'unique id of where the service is enterred into iCUE, e.g., HSC, Service Task, Informal Support.',
	SRVC_ENT_SEQ_NBR NUMBER(6) NULL  COMMENT 'sequence number in combination of mbr_id to uniquely identiy the service record in its entry table/entity, e.g. Servcie Task, Informal Support.',
	WK_DAY_TYP_REF_ID INTEGER NULL ,
	WK_DAY_TYP_REF_CD STRING(50) NULL ,
	WK_DAY_TYP_REF_DSPL STRING(100) NULL ,
	WK_DAY_TYP_REF_DESC STRING(500) NULL ,
	FROM_TM VARCHAR2(10) NULL  COMMENT 'from time of a day',
	TO_TM VARCHAR2(10) NULL  COMMENT 'to time of a day',
	MEAL_TYP_REF_ID INTEGER NULL  COMMENT 'unique id of the type of meal included in the service, e.g., vegetarian.',
	MEAL_TYP_REF_CD STRING(50) NULL  COMMENT 'unique id of the type of meal included in the service, e.g., vegetarian.',
	MEAL_TYP_REF_DSPL STRING(100) NULL  COMMENT 'unique id of the type of meal included in the service, e.g., vegetarian.',
	MEAL_TYP_REF_DESC STRING(500) NULL  COMMENT 'unique id of the type of meal included in the service, e.g., vegetarian.',
	SRVC_CNT NUMBER(5) NULL  COMMENT 'count of the service during the scheduled time if applicable.',
	DW_SRC_REC_STS_CD STRING(50) NOT NULL  COMMENT 'Status of the record sent down from the source system, allows for soft deletes',
	DW_DATA_SECUR_RULE_LIST VARIANT NULL  COMMENT 'list of Snowflake security tags associated with the record',
	DW_DATA_QLTY_ISS_LIST VARIANT NULL  COMMENT 'list of any data issues concerning the record',
	DW_DATA_SECURE_GROUP_ID NUMBER(38) NULL  COMMENT 'list of Snowflake security group id that associated with security tags associated with the record',
	EVNT_DTTM TIMESTAMP NULL  COMMENT 'Metadata Record: Kafka Stream Event Date time - Using to identify the ''latest'' transaction',
	DW_CREAT_DTTM TIMESTAMP NOT NULL  COMMENT 'timestamp when record was created in the data warehouse',
	DW_CHG_DTTM TIMESTAMP NULL  COMMENT 'timestamp when the record was last updated in the data warehouse',
	SRC_REC_GUID STRING(50) NULL  COMMENT 'Source Record GUID',
	SRC_REC_OFST NUMBER(18) NULL  COMMENT 'Metadata Record: Kafka offset - This column is used for compaction logic (choosing the most recent record) from the ETL Layer ONLY.
It has no business value and should NOT be present in the Foundation Layer (Remove this column from the Foundation Layer DDL).

The kafka offset is a simple integer number that is used by Kafka to maintain the current position within a given partition  and topic.
',
	SRC_REC_PARTN NUMBER(18) NULL  COMMENT 'Metadata Record: Kafka Partition - This column is used for compaction logic (choosing the most recent record) from the ETL Layer ONLY.
It has no business value and should NOT be present in the Foundation Layer (Remove this column from the Foundation Layer DDL).

In Apache Kafka, partitions are the main method of concurrency for topics. A topic, a dedicated location for events or messages, will be broken into multiple partitions among one or more Kafka brokers.'
)
COMMENT = 'Weekly schedule for the C&S service for the member.';

ALTER TABLE DW_MBR_SRVC_SCHED
	ADD CONSTRAINT DW_MBR_SRVC_SCHED_PK PRIMARY KEY (DW_MBR_SRVC_SCHED_REC_ID);


CREATE OR REPLACE TABLE DW_MBR_CLIN_TRIAL
(
	DW_MBR_CLIN_TRIAL_REC_ID STRING(1000) NOT NULL  COMMENT 'Primary key of the Record. Usually Concatenation of Natural Key (DW_SYS_REF_CD, MBR_ID,CLIN_TRIAL_SEQ_NBR)',
	DW_SYS_REF_CD STRING(50) NOT NULL  COMMENT 'Identity of the system which sent the data to OCM Snowflake',
	SRC_SYS_CREAT_DTTM TIMESTAMP NULL  COMMENT 'The date and time the object was created on the source system.',
	SRC_SYS_CREAT_USER_ID STRING(50) NULL  COMMENT 'The Id of who or what performed the object creation on the source system. This can be a person or process.',
	SRC_SYS_UPDT_DTTM TIMESTAMP NULL  COMMENT 'The date and time the object was updated on the source system.',
	SRC_SYS_UPDT_USER_ID STRING(50) NULL  COMMENT 'The Id of who or what performed the object update on the source system. This can be a person or process.',
	SRC_SYS_UPDT_VER_NBR NUMBER(10) NULL  COMMENT 'Most recent version of updated data. This field will be used to sync between legacy applications and new application.',
	SRC_SYS_INAC_IND STRING(10) NULL  COMMENT 'inactive indicator',
	SRC_SYS_REC_STS_CD STRING(50) NOT NULL  COMMENT 'A code indicating the status of the object. Examples: - Active - Inactive - SoftLogical Delete',
	SRC_SYS_DATA_QLTY_ISS_RULE_LIST VARIANT NULL  COMMENT 'json formated data quality issue associated with the record.

initial to record unmatched reference values from source, e.g.,  iMDM and CDB, and OCM default %ref_id = 0 (unknown).',
	SRC_SYS_DATA_SECUR_RULE_LIST VARIANT NULL  COMMENT 'data access/security rule list, a json array',
	CREAT_SYS_REF_ID INTEGER NULL  COMMENT 'reference for source application.',
	CREAT_SYS_REF_CD STRING(50) NULL  COMMENT 'reference for source application.',
	CREAT_SYS_REF_DSPL STRING(100) NULL  COMMENT 'reference for source application.',
	CREAT_SYS_REF_DESC STRING(500) NULL  COMMENT 'reference for source application.',
	CHG_SYS_REF_ID INTEGER NULL  COMMENT 'reference for update application.',
	CHG_SYS_REF_CD STRING(50) NULL  COMMENT 'reference for update application.',
	CHG_SYS_REF_DSPL STRING(100) NULL  COMMENT 'reference for update application.',
	CHG_SYS_REF_DESC STRING(500) NULL  COMMENT 'reference for update application.',
	INDV_SRC_ID STRING(30) NULL  COMMENT 'Unique identifier for a Member within the clinical toolbox.',
	INDV_KEY_TYP_REF_ID INTEGER NULL  COMMENT 'Enterprise Member Identifier Type',
	INDV_KEY_TYP_REF_CD STRING(50) NULL  COMMENT 'Enterprise Member Identifier Type',
	INDV_KEY_TYP_REF_DSPL STRING(100) NULL  COMMENT 'Enterprise Member Identifier Type',
	INDV_KEY_TYP_REF_DESC STRING(500) NULL  COMMENT 'Enterprise Member Identifier Type',
	INDV_KEY_VAL STRING(100) NULL  COMMENT 'Actual Value for the Enterprise Individual Identifier Key Type',
	ORIG_SYS_REF_ID INTEGER NULL  COMMENT 'reference of system that feeds the member record to the enterprise eligibility system (CDB), which consolidates eligibility from multiple (originating or intermediate, e.g., CES) systems. Examples are

CO - Cosmos
CS - CES
GR - Golden Rule
OP - Optum
PR - Prime
UB - UBH

etc...

True originating system is captured under elig_sys_typ_id
',
	ORIG_SYS_REF_CD STRING(50) NULL  COMMENT 'reference of system that feeds the member record to the enterprise eligibility system (CDB), which consolidates eligibility from multiple (originating or intermediate, e.g., CES) systems. Examples are

CO - Cosmos
CS - CES
GR - Golden Rule
OP - Optum
PR - Prime
UB - UBH

etc...

True originating system is captured under elig_sys_typ_id',
	ORIG_SYS_REF_DSPL STRING(100) NULL  COMMENT 'reference of system that feeds the member record to the enterprise eligibility system (CDB), which consolidates eligibility from multiple (originating or intermediate, e.g., CES) systems. Examples are

CO - Cosmos
CS - CES
GR - Golden Rule
OP - Optum
PR - Prime
UB - UBH

etc...

True originating system is captured under elig_sys_typ_id',
	ORIG_SYS_REF_DESC STRING(500) NULL  COMMENT 'reference of system that feeds the member record to the enterprise eligibility system (CDB), which consolidates eligibility from multiple (originating or intermediate, e.g., CES) systems. Examples are

CO - Cosmos
CS - CES
GR - Golden Rule
OP - Optum
PR - Prime
UB - UBH

etc...

True originating system is captured under elig_sys_typ_id',
	CLIN_TRIAL_SEQ_NBR NUMBER(5) NULL  COMMENT 'system generated sequence number to uinquely identify a member clinical trial participation instance',
	TRIAL_NM VARCHAR2(1000) NULL  COMMENT 'Clinical trial name',
	STRT_DT DATE NULL  COMMENT 'start date',
	END_DT DATE NULL  COMMENT 'end date',
	CLIN_TRIAL_PHSE_TYP_REF_ID INTEGER NULL  COMMENT 'unique identifier of clinial trial phase, e.g. phase I, phase II, etc...',
	CLIN_TRIAL_PHSE_TYP_REF_CD STRING(50) NULL  COMMENT 'unique identifier of clinial trial phase, e.g. phase I, phase II, etc...',
	CLIN_TRIAL_PHSE_TYP_REF_DSPL STRING(100) NULL  COMMENT 'unique identifier of clinial trial phase, e.g. phase I, phase II, etc...',
	CLIN_TRIAL_PHSE_TYP_REF_DESC STRING(500) NULL  COMMENT 'unique identifier of clinial trial phase, e.g. phase I, phase II, etc...',
	DW_SRC_REC_STS_CD STRING(50) NOT NULL  COMMENT 'Status of the record sent down from the source system, allows for soft deletes',
	DW_DATA_SECUR_RULE_LIST VARIANT NULL  COMMENT 'list of Snowflake security tags associated with the record',
	DW_DATA_QLTY_ISS_LIST VARIANT NULL  COMMENT 'list of any data issues concerning the record',
	DW_DATA_SECURE_GROUP_ID NUMBER(38) NULL  COMMENT 'list of Snowflake security group id that associated with security tags associated with the record',
	EVNT_DTTM TIMESTAMP NULL  COMMENT 'Metadata Record: Kafka Stream Event Date time - Using to identify the ''latest'' transaction',
	DW_CREAT_DTTM TIMESTAMP NOT NULL  COMMENT 'timestamp when record was created in the data warehouse',
	DW_CHG_DTTM TIMESTAMP NULL  COMMENT 'timestamp when the record was last updated in the data warehouse',
	SRC_REC_GUID STRING(50) NULL  COMMENT 'Source Record GUID',
	SRC_REC_OFST NUMBER(18) NULL  COMMENT 'Metadata Record: Kafka offset - This column is used for compaction logic (choosing the most recent record) from the ETL Layer ONLY.
It has no business value and should NOT be present in the Foundation Layer (Remove this column from the Foundation Layer DDL).

The kafka offset is a simple integer number that is used by Kafka to maintain the current position within a given partition  and topic.
',
	SRC_REC_PARTN NUMBER(18) NULL  COMMENT 'Metadata Record: Kafka Partition - This column is used for compaction logic (choosing the most recent record) from the ETL Layer ONLY.
It has no business value and should NOT be present in the Foundation Layer (Remove this column from the Foundation Layer DDL).

In Apache Kafka, partitions are the main method of concurrency for topics. A topic, a dedicated location for events or messages, will be broken into multiple partitions among one or more Kafka brokers.'
)
COMMENT = 'Clinical trials participated by member';

ALTER TABLE DW_MBR_CLIN_TRIAL
	ADD CONSTRAINT DW_MBR_CLIN_TRIAL_PK PRIMARY KEY (DW_MBR_CLIN_TRIAL_REC_ID);