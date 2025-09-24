-- V2: Insere os dados iniciais
INSERT INTO app_user (username, password, role) VALUES ('admin', '$2a$10$h9.h2pM4FzB3yLg9A1/88u5BE4OTg2k8Tj4Wp8S.j1bJ2/Fx2yv.e', 'ADMIN');
INSERT INTO app_user (username, password, role) VALUES ('user', '$2a$10$h9.h2pM4FzB3yLg9A1/88u5BE4OTg2k8Tj4Wp8S.j1bJ2/Fx2yv.e', 'USER');

INSERT INTO branches (name, city, state, phone) VALUES ('Sede São Paulo', 'São Paulo', 'SP', '11999998888');
INSERT INTO branches (name, city, state, phone) VALUES ('Filial Rio de Janeiro', 'Rio de Janeiro', 'RJ', '21988887777');
INSERT INTO branches (name, city, state, phone) VALUES ('Filial Belo Horizonte', 'Belo Horizonte', 'MG', '31977776666');