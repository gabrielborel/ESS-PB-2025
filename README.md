# Sistema de Gerenciamento de Livros - Arquitetura de Microsserviços

Sistema distribuído para gerenciar livros e avaliações. Implementado com Spring Boot, Spring Cloud, MongoDB, PostgreSQL e React.

## Arquitetura

```
┌─────────────┐
│   Frontend  │
│   (React)   │
└──────┬──────┘
       │
       ▼
┌──────────────┐
│ API Gateway  │◄──────┐
│    :8080     │       │
└──────┬───────┘       │
       │          ┌────┴─────┐
       │          │  Consul  │
       │          │  :8500   │
       ├──────────►          │
       │          └────┬─────┘
       │               │
   ┌───┴───┬──────────┴─────┐
   ▼       ▼                 ▼
┌─────┐ ┌──────┐      ┌──────────┐
│Books│ │Reviews│     │          │
│:8082│ │:8081 │      │          │
└──┬──┘ └───┬──┘      └──────────┘
   │        │
   ▼        ▼
┌────────┐ ┌────────┐
│Postgres│ │MongoDB │
│ :5432  │ │:27017  │
└────────┘ └────────┘
```

### Componentes

- **API Gateway (Spring Cloud Gateway)**: Roteamento inteligente e balanceamento de carga
- **Consul**: Service Discovery e Health Checking
- **Books Service**: Microsserviço de gerenciamento de livros (PostgreSQL)
- **Reviews Service**: Microsserviço de avaliações (MongoDB)
- **Frontend**: Interface React

## Stack

**Infraestrutura**
- Spring Cloud Gateway
- Consul (Service Discovery)
- Docker & Docker Compose

**Books Service**
- Java 21
- Spring Boot 3.3.5
- Spring Cloud 2023.0.3
- PostgreSQL 16
- Hibernate Envers (Auditoria)

**Reviews Service**
- Java 21
- Spring Boot 3.3.5
- Spring Cloud 2023.0.3
- MongoDB 7

**Frontend**
- React 19
- Vite
- TailwindCSS

## Como rodar

### Via Docker Compose (Recomendado)

Na raiz do projeto:
```bash
docker-compose up -d
```

Isso sobe:
- **Consul** (Service Discovery) na porta 8500
- **PostgreSQL** na porta 5432
- **MongoDB** na porta 27017
- **API Gateway** na porta 8080
- **Books Service** na porta 8082
- **Reviews Service** na porta 8081
- **Frontend** na porta 3000

**Acessos:**
- Frontend: `http://localhost:3000`
- API Gateway: `http://localhost:8080`
- Consul UI: `http://localhost:8500`

Para parar tudo:
```bash
docker-compose down
```

Para rebuild:
```bash
docker-compose up -d --build
```

### Desenvolvimento Local

1. **Suba as dependências (Consul, PostgreSQL, MongoDB)**:
```bash
docker-compose up -d consul postgres mongodb
```

2. **Rode o API Gateway**:
```bash
cd api-gateway
./mvnw spring-boot:run
```

3. **Rode o Books Service**:
```bash
cd books-management-api
./mvnw spring-boot:run
```

4. **Rode o Reviews Service**:
```bash
cd reviews-service
./mvnw spring-boot:run
```

5. **Rode o Frontend**:
```bash
cd books-management-frontend
npm install
npm run dev
```

## API

> **Nota**: Todas as requisições passam pelo API Gateway na porta 8080

### Books Service (`:8080/api/books`)
- Listagem, criação, edição e exclusão de livros
- Histórico de alterações com auditoria

### Reviews Service (`:8080/api/reviews`)
- Sistema de avaliações (1-5 estrelas)
- Estatísticas por livro (média e total de avaliações)
- Listagem de avaliações por livro

## Estrutura do Projeto

```
├── api-gateway/                    # API Gateway (Spring Cloud Gateway)
│   ├── src/main/java/.../gateway/
│   ├── src/main/resources/
│   ├── Dockerfile
│   └── pom.xml
│
├── books-management-api/           # Microsserviço de Livros
│   ├── src/main/java/.../books_management/
│   │   ├── application/            # services, DTOs, mappers
│   │   │   ├── dto/
│   │   │   ├── mapper/
│   │   │   └── service/
│   │   ├── domain/                 # entidades, repositories, exceções
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   └── exception/
│   │   ├── presentation/           # controllers, tratamento de exceções
│   │   │   ├── controller/
│   │   │   └── exception/
│   │   └── infrastructure/         # configurações, listeners, eventos
│   │       ├── config/             # (CORS, etc)
│   │       └── listener/           # (auditoria)
│   ├── src/main/resources/
│   ├── Dockerfile
│   └── pom.xml
│
├── reviews-service/                # Microsserviço de Avaliações
│   ├── src/main/java/.../reviews/
│   │   ├── application/            # services, DTOs, mappers
│   │   │   ├── dto/
│   │   │   ├── mapper/
│   │   │   └── service/
│   │   ├── domain/                 # entidades, repositories
│   │   │   ├── model/
│   │   │   └── repository/
│   │   ├── presentation/           # controllers, exception handling
│   │   │   ├── controller/
│   │   │   └── exception/
│   │   └── infrastructure/         # configurações
│   │       └── config/
│   ├── src/main/resources/
│   ├── Dockerfile
│   └── pom.xml
│
├── books-management-frontend/      # Frontend React
│   ├── src/
│   │   ├── components/             # componentes React
│   │   ├── services/               # chamadas API
│   │   └── App.jsx
│   ├── Dockerfile
│   └── vite.config.js
│
└── docker-compose.yml              # Orquestração de todos os serviços
```

## Features

### Books Service
- CRUD completo de livros
- Histórico de alterações (Hibernate Envers)
- Validação de dados
- PostgreSQL

### Reviews Service
- Sistema de avaliações (1-5 estrelas)
- Estatísticas por livro
- Validação de dados
- MongoDB

### Infraestrutura
- Service Discovery (Consul)
- API Gateway com load balancing
- Health checks automáticos
- Docker Compose

### Frontend
- Interface responsiva
- CRUD de livros
- Sistema de avaliações interativo
- Modal de confirmação

## Arquitetura e Padrões

### Microservices Architecture
- Serviços independentes e especializados
- API Gateway como ponto único de entrada
- Service Discovery automático
- Database per Service

### Padrões de Código
- **Clean Architecture**: Separação em camadas (domain, application, presentation, infrastructure)
- **Repository Pattern**: Abstração de acesso a dados
- **DTO Pattern**: Transferência de dados entre camadas

## Monitoramento

- **Consul UI**: `http://localhost:8500`
- **Spring Boot Actuator**: `/actuator/health` em cada serviço

## Troubleshooting
