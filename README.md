## Projeto MottuGrid Java

## Descrição do Projeto

Este projeto é uma API REST desenvolvida em Java com Spring Boot para gerenciamento de pátios (Yards), filiais (Branches) e motocicletas (Motorcycles). Ele oferece funcionalidades de CRUD completo para as entidades, com busca, paginação, validação, tratamento de erros e integração com banco de dados Oracle.

## Alunos

- Pedro Henrique dos Santos - RM559064  
- Thiago Thomaz Sales Conceição - RM557992  
- Vinícius de Oliveira Coutinho - RM556182  

## Tecnologias Utilizadas

- Java 17+  
- Spring Boot  
- Spring Data JPA  
- Hibernate  
- Oracle Database  
- Swagger (OpenAPI) para documentação da API  

## Como Executar

1. Certifique-se de que o Oracle Database está instalado e configurado, com o schema necessário criado.  
2. Clone este repositório:  
   ```bash
   git clone https://github.com/MottuChallenge/java.git
   cd java
   
## Como Executar

3. Configure as credenciais e a URL do banco no arquivo `application.properties` ou `application.yml`.  
   Exemplo de configuração no `application.properties`:

```properties
spring.datasource.url=url_do_seu_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

4. Compile e rode a aplicação com Maven ou Gradle:  
```bash```
```./mvnw spring-boot:run```

5. A aplicação iniciará na porta padrão 8080.

## Utilizando o Swagger (Documentação da API)

Após iniciar a aplicação, acesse a interface do Swagger para visualizar e testar os endpoints da API:

http://localhost:8080/swagger-ui.html


No Swagger, você pode explorar todos os recursos da API, fazer requisições e ver as respostas diretamente pelo navegador.

## Sobre o Projeto

Este projeto foi desenvolvido para gerenciar entidades relacionadas a pátios e motocicletas, utilizando Spring Boot, JPA, e banco de dados Oracle.




