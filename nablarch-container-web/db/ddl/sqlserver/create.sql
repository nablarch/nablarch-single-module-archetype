CREATE TABLE CODE_NAME (
  CODE_ID CHAR(8) NOT NULL ,
  CODE_VALUE VARCHAR(2) NOT NULL ,
  LANG CHAR(2) NOT NULL ,
  SORT_ORDER INT NOT NULL ,
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
CREATE TABLE USER_SESSION (
  SESSION_ID VARCHAR(100) NOT NULL ,
  SESSION_OBJECT VARBINARY(MAX) NULL ,
  EXPIRATION_DATETIME DATETIME2 NOT NULL
);
ALTER TABLE USER_SESSION
ADD CONSTRAINT PK_USER_SESSION PRIMARY KEY
(
  SESSION_ID
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
ADD CONSTRAINT CODE_NAME_FK_01
FOREIGN KEY (
  CODE_ID,
  CODE_VALUE
) REFERENCES CODE_PATTERN (
  CODE_ID,
  CODE_VALUE
);
CREATE TABLE DOUBLE_SUBMISSION (
  TOKEN VARCHAR(36) NOT NULL ,
  CREATED_AT TIMESTAMP NULL
);
ALTER TABLE DOUBLE_SUBMISSION
ADD CONSTRAINT PK_DOUBLE_SUBMISSION PRIMARY KEY
(
  TOKEN
);
