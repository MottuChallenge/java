-- V2: Insere os usuários iniciais para Spring Security com o prefixo ROLE_
-- A senha para ambos é 'admin', criptografada com BCrypt
INSERT INTO app_user (username, password, role) VALUES ('admin', '$2a$10$3RuscODutoYh6ef5.UZRn.y2iv6irNIfrDldsmJVjYrnWDq0lHrmq', 'ROLE_ADMIN');
INSERT INTO app_user (username, password, role) VALUES ('user', '$2a$10$3RuscODutoYh6ef5.UZRn.y2iv6irNIfrDldsmJVjYrnWDq0lHrmq', 'ROLE_USER');

