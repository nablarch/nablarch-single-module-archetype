CREATE TABLE BUSINESS_DATE (
  SEGMENT_ID CHAR(2) NOT NULL,
  BIZ_DATE CHAR(8) NOT NULL
);
COMMENT ON table BUSINESS_DATE is '業務日付1';
COMMENT ON column BUSINESS_DATE.SEGMENT_ID is 'セグメントID';
COMMENT ON column BUSINESS_DATE.BIZ_DATE is '業務日付';
CREATE TABLE CODE_NAME (
  CODE_ID CHAR(8) NOT NULL,
  CODE_VALUE VARCHAR(2) NOT NULL,
  LANG CHAR(2) NOT NULL,
  SORT_ORDER SMALLINT NOT NULL,
  CODE_NAME VARCHAR(50) NOT NULL,
  SHORT_NAME VARCHAR(50),
  OPTION01 VARCHAR(50),
  OPTION02 VARCHAR(50),
  OPTION03 VARCHAR(50),
  OPTION04 VARCHAR(50),
  OPTION05 VARCHAR(50),
  OPTION06 VARCHAR(50),
  OPTION07 VARCHAR(50),
  OPTION08 VARCHAR(50),
  OPTION09 VARCHAR(50),
  OPTION10 VARCHAR(50)
);
COMMENT ON table CODE_NAME is 'コード名称';
COMMENT ON column CODE_NAME.CODE_ID is 'コードID';
COMMENT ON column CODE_NAME.CODE_VALUE is 'コード値';
COMMENT ON column CODE_NAME.LANG is '言語';
COMMENT ON column CODE_NAME.SORT_ORDER is 'ソート順';
COMMENT ON column CODE_NAME.CODE_NAME is '名称';
COMMENT ON column CODE_NAME.SHORT_NAME is 'コード略称';
COMMENT ON column CODE_NAME.OPTION01 is 'オプション名称01';
COMMENT ON column CODE_NAME.OPTION02 is 'オプション名称02';
COMMENT ON column CODE_NAME.OPTION03 is 'オプション名称03';
COMMENT ON column CODE_NAME.OPTION04 is 'オプション名称04';
COMMENT ON column CODE_NAME.OPTION05 is 'オプション名称05';
COMMENT ON column CODE_NAME.OPTION06 is 'オプション名称06';
COMMENT ON column CODE_NAME.OPTION07 is 'オプション名称07';
COMMENT ON column CODE_NAME.OPTION08 is 'オプション名称08';
COMMENT ON column CODE_NAME.OPTION09 is 'オプション名称09';
COMMENT ON column CODE_NAME.OPTION10 is 'オプション名称10';
CREATE TABLE CODE_PATTERN (
  CODE_ID CHAR(8) NOT NULL,
  CODE_VALUE VARCHAR(2) NOT NULL,
  PATTERN01 CHAR(1) NOT NULL,
  PATTERN02 CHAR(1),
  PATTERN03 CHAR(1),
  PATTERN04 CHAR(1),
  PATTERN05 CHAR(1),
  PATTERN06 CHAR(1),
  PATTERN07 CHAR(1),
  PATTERN08 CHAR(1),
  PATTERN09 CHAR(1),
  PATTERN10 CHAR(1),
  PATTERN11 CHAR(1),
  PATTERN12 CHAR(1),
  PATTERN13 CHAR(1),
  PATTERN14 CHAR(1),
  PATTERN15 CHAR(1),
  PATTERN16 CHAR(1),
  PATTERN17 CHAR(1),
  PATTERN18 CHAR(1),
  PATTERN19 CHAR(1),
  PATTERN20 CHAR(1)
);
COMMENT ON table CODE_PATTERN is 'コードパターン';
COMMENT ON column CODE_PATTERN.CODE_ID is 'コードID';
COMMENT ON column CODE_PATTERN.CODE_VALUE is 'コード値';
COMMENT ON column CODE_PATTERN.PATTERN01 is 'パターン01';
COMMENT ON column CODE_PATTERN.PATTERN02 is 'パターン02';
COMMENT ON column CODE_PATTERN.PATTERN03 is 'パターン03';
COMMENT ON column CODE_PATTERN.PATTERN04 is 'パターン04';
COMMENT ON column CODE_PATTERN.PATTERN05 is 'パターン05';
COMMENT ON column CODE_PATTERN.PATTERN06 is 'パターン06';
COMMENT ON column CODE_PATTERN.PATTERN07 is 'パターン07';
COMMENT ON column CODE_PATTERN.PATTERN08 is 'パターン08';
COMMENT ON column CODE_PATTERN.PATTERN09 is 'パターン09';
COMMENT ON column CODE_PATTERN.PATTERN10 is 'パターン10';
COMMENT ON column CODE_PATTERN.PATTERN11 is 'パターン11';
COMMENT ON column CODE_PATTERN.PATTERN12 is 'パターン12';
COMMENT ON column CODE_PATTERN.PATTERN13 is 'パターン13';
COMMENT ON column CODE_PATTERN.PATTERN14 is 'パターン14';
COMMENT ON column CODE_PATTERN.PATTERN15 is 'パターン15';
COMMENT ON column CODE_PATTERN.PATTERN16 is 'パターン16';
COMMENT ON column CODE_PATTERN.PATTERN17 is 'パターン17';
COMMENT ON column CODE_PATTERN.PATTERN18 is 'パターン18';
COMMENT ON column CODE_PATTERN.PATTERN19 is 'パターン19';
COMMENT ON column CODE_PATTERN.PATTERN20 is 'パターン20';
CREATE TABLE SAMPLE_USER (
  USER_ID VARCHAR(20) NOT NULL,
  FAMILY_NAME VARCHAR(20) NOT NULL,
  FIRST_NAME VARCHAR(20) NOT NULL
);
COMMENT ON table SAMPLE_USER is 'サンプルユーザ';
COMMENT ON column SAMPLE_USER.USER_ID is 'ユーザID';
COMMENT ON column SAMPLE_USER.FAMILY_NAME is '姓';
COMMENT ON column SAMPLE_USER.FIRST_NAME is '名';
ALTER TABLE BUSINESS_DATE ADD CONSTRAINT PK_BUSINESS_DATE1 PRIMARY KEY
(
  SEGMENT_ID
);
ALTER TABLE SAMPLE_USER ADD CONSTRAINT PK_SAMPLE_USER_INFO PRIMARY KEY
(
  USER_ID
);
ALTER TABLE CODE_PATTERN ADD CONSTRAINT SYS_C0058550 PRIMARY KEY
(
  CODE_ID,
  CODE_VALUE
);
ALTER TABLE CODE_NAME ADD CONSTRAINT SYS_C0058560 PRIMARY KEY
(
  CODE_ID,
  CODE_VALUE,
  LANG
);
ALTER TABLE CODE_NAME
ADD
FOREIGN KEY (
  CODE_ID,
  CODE_VALUE
) REFERENCES CODE_PATTERN (
  CODE_ID,
  CODE_VALUE
);
