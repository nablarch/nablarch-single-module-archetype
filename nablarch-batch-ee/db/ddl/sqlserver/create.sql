CREATE TABLE BUSINESS_DATE (
  SEGMENT_ID CHAR(2) NOT NULL ,
  BIZ_DATE CHAR(8) NOT NULL
);
CREATE TABLE CODE_NAME (
  CODE_ID CHAR(8) NOT NULL ,
  CODE_VALUE VARCHAR(2) NOT NULL ,
  LANG CHAR(2) NOT NULL ,
  SORT_ORDER SMALLINT NOT NULL ,
  CODE_NAME VARCHAR(50) NOT NULL ,
  SHORT_NAME VARCHAR(50) NULL ,
  OPTION01 VARCHAR(50) NULL ,
  OPTION02 VARCHAR(50) NULL ,
  OPTION03 VARCHAR(50) NULL ,
  OPTION04 VARCHAR(50) NULL ,
  OPTION05 VARCHAR(50) NULL ,
  OPTION06 VARCHAR(50) NULL ,
  OPTION07 VARCHAR(50) NULL ,
  OPTION08 VARCHAR(50) NULL ,
  OPTION09 VARCHAR(50) NULL ,
  OPTION10 VARCHAR(50) NULL
);
CREATE TABLE CODE_PATTERN (
  CODE_ID CHAR(8) NOT NULL ,
  CODE_VALUE VARCHAR(2) NOT NULL ,
  PATTERN01 CHAR(1) NOT NULL ,
  PATTERN02 CHAR(1) NULL ,
  PATTERN03 CHAR(1) NULL ,
  PATTERN04 CHAR(1) NULL ,
  PATTERN05 CHAR(1) NULL ,
  PATTERN06 CHAR(1) NULL ,
  PATTERN07 CHAR(1) NULL ,
  PATTERN08 CHAR(1) NULL ,
  PATTERN09 CHAR(1) NULL ,
  PATTERN10 CHAR(1) NULL ,
  PATTERN11 CHAR(1) NULL ,
  PATTERN12 CHAR(1) NULL ,
  PATTERN13 CHAR(1) NULL ,
  PATTERN14 CHAR(1) NULL ,
  PATTERN15 CHAR(1) NULL ,
  PATTERN16 CHAR(1) NULL ,
  PATTERN17 CHAR(1) NULL ,
  PATTERN18 CHAR(1) NULL ,
  PATTERN19 CHAR(1) NULL ,
  PATTERN20 CHAR(1) NULL
);
CREATE TABLE SAMPLE_USER (
  USER_ID VARCHAR(20) NOT NULL ,
  FAMILY_NAME VARCHAR(20) NOT NULL ,
  FIRST_NAME VARCHAR(20) NOT NULL
);
ALTER TABLE BUSINESS_DATE
ADD CONSTRAINT PK_BUSINESS_DATE1 PRIMARY KEY
(
  SEGMENT_ID
);
ALTER TABLE SAMPLE_USER
ADD CONSTRAINT PK_SAMPLE_USER_INFO PRIMARY KEY
(
  USER_ID
);
ALTER TABLE CODE_PATTERN
ADD CONSTRAINT SYS_C0058550 PRIMARY KEY
(
  CODE_ID,
  CODE_VALUE
);
ALTER TABLE CODE_NAME
ADD CONSTRAINT SYS_C0058560 PRIMARY KEY
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
