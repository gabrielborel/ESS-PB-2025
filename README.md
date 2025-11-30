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
│Books│ │Reviews│     │ RabbitMQ │
│:8082│ │:8081 │      │  :5672   │
└──┬──┘ └───┬──┘      └────┬─────┘
   │        │               │
   │        └───────────────┘
   │                (events)
   ▼
┌────────┐ ┌────────┐
│Postgres│ │MongoDB │
│ :5432  │ │:27017  │
└────────┘ └────────┘
```

### Componentes

- **API Gateway (Spring Cloud Gateway)**: Roteamento inteligente e balanceamento de carga
- **Consul**: Service Discovery e Health Checking
- **RabbitMQ**: Message broker para comunicação assíncrona entre serviços
- **Books Service**: Microsserviço de gerenciamento de livros (PostgreSQL)
- **Reviews Service**: Microsserviço de avaliações (MongoDB)
- **Frontend**: Interface React

## Stack

**Infraestrutura**
- Spring Cloud Gateway
- Consul (Service Discovery)
- RabbitMQ (Message Broker)
- Docker & Docker Compose

**Books Service**
- Java 21
- Spring Boot 3.3.5
- Spring Cloud 2023.0.3
- Spring AMQP (Event Publishing)
- PostgreSQL 16
- Hibernate Envers (Auditoria)

**Reviews Service**
- Java 21
- Spring Boot 3.3.5
- Spring Cloud 2023.0.3
- Spring AMQP (Event Consuming)
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
- **RabbitMQ** (Message Broker) na porta 5672 (+ UI na porta 15672)
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
- RabbitMQ UI: `http://localhost:15672` (admin/admin)

Para parar tudo:
```bash
docker-compose down
```

Para rebuild:
```bash
docker-compose up -d --build
```

### Desenvolvimento Local

1. **Suba as dependências (Consul, RabbitMQ, PostgreSQL, MongoDB)**:
```bash
docker-compose up -d consul rabbitmq postgres mongodb
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
│   │   ├── domain/                 # entidades, repositories, exceções, eventos
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── exception/
│   │   │   └── event/              # BookCreatedEvent, BookUpdatedEvent, BookDeletedEvent
│   │   ├── presentation/           # controllers, tratamento de exceções
│   │   │   ├── controller/
│   │   │   └── exception/
│   │   └── infrastructure/         # configurações, listeners, mensageria
│   │       ├── config/             # (CORS, etc)
│   │       ├── listener/           # (auditoria)
│   │       └── messaging/          # (RabbitMQ publisher)
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
│   │   ├── domain/                 # entidades, repositories, eventos
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   └── event/              # BookCreatedEvent, BookDeletedEvent (mirror)
│   │   ├── presentation/           # controllers, exception handling
│   │   │   ├── controller/
│   │   │   └── exception/
│   │   └── infrastructure/         # configurações, mensageria
│   │       ├── config/
│   │       └── messaging/          # (RabbitMQ consumer)
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
- Publicação de eventos (criação, atualização, deleção)
- Validação de dados
- PostgreSQL

### Reviews Service
- Sistema de avaliações (1-5 estrelas)
- Estatísticas por livro
- Consumo de eventos (deleção em cascata)
- Validação de dados
- MongoDB

### Mensageria (RabbitMQ)
- **book.created.queue**: Eventos de criação de livros
- **book.updated.queue**: Eventos de atualização de livros
- **book.deleted.queue**: Eventos de deleção (trigger para deleção de reviews)
- Topic Exchange: `books.exchange`
- Comunicação assíncrona entre serviços

### Infraestrutura
- Service Discovery (Consul)
- API Gateway com load balancing
- Event-driven architecture
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
- Event-driven communication (RabbitMQ)

### Padrões de Código
- **Clean Architecture**: Separação em camadas (domain, application, presentation, infrastructure)
- **Repository Pattern**: Abstração de acesso a dados
- **DTO Pattern**: Transferência de dados entre camadas
- **Publisher-Subscriber**: Comunicação assíncrona via eventos

## Monitoramento

- **Consul UI**: `http://localhost:8500` - Status dos serviços registrados
- **RabbitMQ Management**: `http://localhost:15672` - Filas, exchanges, mensagens (admin/admin)
- **Spring Boot Actuator**: `/actuator/health` em cada serviço

## Troubleshooting
