-- V1: Cria o schema inicial completo, com nomes de tabela no PLURAL
CREATE TABLE branches ( -- alterado
                          id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                          name VARCHAR2(255) NOT NULL,
                          city VARCHAR2(255) NOT NULL,
                          state VARCHAR2(255) NOT NULL,
                          phone VARCHAR2(255) NOT NULL
);

CREATE TABLE yards (
                       id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                       name VARCHAR2(255) NOT NULL,
                       branch_id RAW(16) NOT NULL,
                       CONSTRAINT fk_yard_branch FOREIGN KEY (branch_id) REFERENCES branches(id) -- alterado
);

CREATE TABLE motorcycles (
                             id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                             model VARCHAR2(255) NOT NULL,
                             plate VARCHAR2(255) NOT NULL UNIQUE,
                             manufacturer VARCHAR2(255) NOT NULL,
                             year NUMBER(4) NOT NULL,
                             yard_id RAW(16) NOT NULL,
                             CONSTRAINT fk_motorcycle_yard FOREIGN KEY (yard_id) REFERENCES yards(id)
);

CREATE TABLE app_user (
                          id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
                          username VARCHAR2(100) NOT NULL UNIQUE,
                          password VARCHAR2(200) NOT NULL,
                          role VARCHAR2(50) NOT NULL
);