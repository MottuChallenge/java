-- V1: create core tables for Oracle
CREATE TABLE branch (
                        id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                        name VARCHAR2(150) NOT NULL,
                        address VARCHAR2(255)
);

CREATE TABLE motorcycle (
                            id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                            plate VARCHAR2(20) NOT NULL UNIQUE,
                            model VARCHAR2(120),
                            status VARCHAR2(30),
                            branch_id RAW(16),
                            CONSTRAINT fk_branch FOREIGN KEY (branch_id) REFERENCES branch(id)
);

CREATE TABLE app_user (
                          id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                          username VARCHAR2(100) NOT NULL UNIQUE,
                          password VARCHAR2(200) NOT NULL,
                          role VARCHAR2(50) NOT NULL
);
