# MottuGrid Java

## 📋 Descrição do Projeto

O **MottuGrid Java** é uma aplicação completa desenvolvida em Java com Spring Boot para o gerenciamento inteligente de motocicletas em pátios distribuídos por filiais. O sistema oferece uma API REST robusta com interface web integrada, proporcionando controle total sobre o ciclo de vida das motocicletas desde o cadastro até a movimentação entre pátios.

### Funcionalidades Principais

- 🏢 **Gerenciamento de Filiais**: Cadastro e controle de filiais por cidade/estado
- 🏭 **Administração de Pátios**: Gestão de pátios vinculados às filiais
- 🏍️ **Controle de Motocicletas**: CRUD completo com rastreamento de localização
- 🔄 **Movimentação de Veículos**: Sistema de transferência entre pátios
- 🔐 **Sistema de Autenticação**: Login seguro com Spring Security
- 📊 **Interface Web**: Páginas interativas com Thymeleaf
- 📖 **Documentação API**: Swagger/OpenAPI integrado

## 👥 Equipe de Desenvolvimento

| Nome | RM | GitHub |
|------|-------|--------|
| Pedro Henrique dos Santos | RM559064 | - |
| Thiago Thomaz Sales Conceição | RM557992 | - |
| Vinícius de Oliveira Coutinho | RM556182 | - |

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 17+** - Linguagem de programação
- **Spring Boot 3.2.5** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Autenticação e autorização
- **Spring Cache** - Cache de dados
- **Hibernate** - ORM
- **Flyway** - Versionamento de banco

### Frontend
- **Thymeleaf** - Template engine
- **HTML5/CSS3** - Interface web
- **Bootstrap** - Framework CSS (implícito)

### Banco de Dados
- **Oracle Database** - Banco principal
- **HikariCP** - Pool de conexões

### Documentação e Testes
- **Swagger/OpenAPI 3** - Documentação da API
- **JUnit 5** - Testes unitários
- **Spring Boot Test** - Testes de integração

### Ferramentas
- **Maven** - Gerenciamento de dependências
- **Lombok** - Redução de boilerplate
- **Maven Wrapper** - Execução sem instalação do Maven

## 🗄️ Arquitetura do Banco de Dados

```sql
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│  branches   │    │    yards    │    │ motorcycles │
├─────────────┤    ├─────────────┤    ├─────────────┤
│ id (PK)     │◄──┤ branch_id   │◄──┤ yard_id     │
│ name        │    │ id (PK)     │    │ id (PK)     │
│ city        │    │ name        │    │ model       │
│ state       │    └─────────────┘    │ plate       │
│ phone       │                       │ manufacturer│
└─────────────┘                       │ year        │
                                      └─────────────┘
```

## 🚀 Como Executar

### Pré-requisitos
- Java 17 ou superior
- Acesso ao Oracle Database (FIAP)
- Git

### Passos para Execução

1. **Clone o repositório**
   ```bash
   git clone https://github.com/MottuChallenge/java.git
   cd java
   ```

2. **Configure o banco de dados**
   
   As configurações já estão definidas em `application.properties` para o ambiente FIAP:
   ```properties
   spring.datasource.url=jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL
   spring.datasource.username=rm556182
   spring.datasource.password=101003
   ```

3. **Execute as migrações do banco**
   ```bash
   ./mvnw flyway:clean flyway:migrate
   ```

4. **Inicie a aplicação**
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Acesse a aplicação**
   - **Interface Web**: http://localhost:8080
   - **API Documentation**: http://localhost:8080/swagger-ui/index.html
   - **Endpoints da API**: http://localhost:8080/api/

## 📡 Endpoints da API

### Filiais (Branches)
- `GET /api/branches` - Listar todas as filiais
- `GET /api/branches/{id}` - Buscar filial por ID
- `POST /api/branches` - Criar nova filial
- `PUT /api/branches/{id}` - Atualizar filial
- `DELETE /api/branches/{id}` - Excluir filial

### Pátios (Yards)
- `GET /api/yards` - Listar todos os pátios
- `GET /api/yards/{id}` - Buscar pátio por ID
- `POST /api/yards` - Criar novo pátio
- `PUT /api/yards/{id}` - Atualizar pátio
- `DELETE /api/yards/{id}` - Excluir pátio

### Motocicletas (Motorcycles)
- `GET /api/motorcycles` - Listar todas as motocicletas
- `GET /api/motorcycles/{id}` - Buscar motocicleta por ID
- `POST /api/motorcycles` - Cadastrar nova motocicleta
- `PUT /api/motorcycles/{id}` - Atualizar motocicleta
- `DELETE /api/motorcycles/{id}` - Excluir motocicleta
- `PATCH /api/motorcycles/{id}/move` - Mover motocicleta entre pátios

## 🌐 Interface Web

A aplicação possui uma interface web completa acessível em `http://localhost:8080` com:

- **Dashboard Principal** - Visão geral do sistema
- **Gestão de Filiais** - CRUD completo via formulários
- **Gestão de Pátios** - Administração de pátios por filial
- **Gestão de Motocicletas** - Controle completo do estoque
- **Sistema de Login** - Autenticação segura

## 📖 Documentação da API (Swagger)

Acesse `http://localhost:8080/swagger-ui/index.html` para:

- 📋 Visualizar todos os endpoints disponíveis
- 🧪 Testar requisições diretamente no navegador
- 📝 Ver exemplos de request/response
- 🔍 Explorar modelos de dados

## 🗂️ Estrutura do Projeto

```
src/main/java/br/com/mottugrid_java/
├── controller/          # Controllers REST e Web
├── domainmodel/         # Entidades JPA
├── dto/                 # Data Transfer Objects
├── infrastructure/      # Configurações de infraestrutura
├── repository/          # Repositories JPA
└── service/            # Regras de negócio

src/main/resources/
├── db/migration/       # Scripts Flyway
├── templates/          # Templates Thymeleaf
└── application.properties
```

## 🧪 Executando Testes

```bash
# Executar todos os testes
./mvnw test

# Executar testes com relatório de cobertura
./mvnw test jacoco:report
```

## 🔧 Configurações Importantes

### Cache
A aplicação utiliza cache em memória para otimizar consultas frequentes.

### Segurança
- Autenticação baseada em sessão
- Proteção CSRF habilitada
- Senhas criptografadas com BCrypt

### Banco de Dados
- Migrações versionadas com Flyway


## 📝 Licença

Este projeto foi desenvolvido para fins acadêmicos na FIAP - Faculdade de Informática e Administração Paulista.

---

**Desenvolvido pela equipe MottuGrid**




