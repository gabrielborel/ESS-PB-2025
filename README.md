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
   │
   │    ┌──────────┬──────────┐
   │    │  Zipkin  │  Loki    │
   │    │  :9411   │  :3100   │
   │    └────┬─────┴────┬─────┘
   │         │          │
   │         └──────────┴─── Grafana :3001
   │                         (monitoring)
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
- **Zipkin**: Distributed tracing para rastreamento de requisições
- **Loki**: Agregação de logs centralizados
- **Grafana**: Visualização de logs e métricas
- **Books Service**: Microsserviço de gerenciamento de livros (PostgreSQL)
- **Reviews Service**: Microsserviço de avaliações (MongoDB)
- **Frontend**: Interface React

## Stack

**Infraestrutura**
- Spring Cloud Gateway
- Consul (Service Discovery)
- RabbitMQ (Message Broker)
- Zipkin (Distributed Tracing)
- Loki + Grafana (Log Aggregation & Monitoring)
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

### Pré-requisitos

Antes de executar o projeto, é necessário instalar o plugin do Docker para envio de logs ao Loki:

```bash
docker plugin install grafana/loki-docker-driver:latest --alias loki --grant-all-permissions
```

**Nota:** Este plugin precisa ser instalado **uma única vez em cada máquina** que for executar o projeto. Ele não é instalado automaticamente pelo docker-compose.

### Via Docker Compose (Recomendado)

Na raiz do projeto:
```bash
docker-compose up -d
```

Isso sobe:
- **Consul** (Service Discovery) na porta 8500
- **RabbitMQ** (Message Broker) na porta 5672 (+ UI na porta 15672)
- **Zipkin** (Distributed Tracing) na porta 9411
- **Loki** (Log Aggregation) na porta 3100
- **Grafana** (Monitoring Dashboard) na porta 3001
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
- Zipkin UI: `http://localhost:9411`
- Grafana: `http://localhost:3001` (admin/admin)

Para parar tudo:
```bash
docker-compose down
```

Para rebuild:
```bash
docker-compose up -d --build
```

### Desenvolvimento Local

1. **Suba as dependências (Consul, RabbitMQ, Zipkin, Loki, Grafana, PostgreSQL, MongoDB)**:
```bash
docker-compose up -d consul rabbitmq zipkin loki grafana postgres mongodb
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

## Monitoramento

- **Zipkin**: `http://localhost:9411` - Distributed tracing
- **Grafana**: `http://localhost:3001` (admin/admin) - Dashboard de logs
- **Consul**: `http://localhost:8500` - Service discovery
- **RabbitMQ**: `http://localhost:15672` (admin/admin) - Filas e mensagens

## Implantação em Kubernetes

O sistema pode ser implantado em Kubernetes usando **Kind (Kubernetes in Docker)** para desenvolvimento local.

### Pré-requisitos

```bash
# Instalar Kind (macOS)
brew install kind
```

### Deploy

```bash
cd k8s
./deploy.sh
```

O script cria o cluster Kind, builda as imagens Docker, carrega no cluster e faz o deploy de todos os serviços.

### Cleanup

```bash
cd k8s
./cleanup.sh
```

### URLs de Acesso

Com Kind, os serviços ficam acessíveis em:
- **Frontend**: http://localhost:3000
- **API Gateway**: http://localhost:8080
- **Grafana**: http://localhost:3001 (admin/admin)
- **Zipkin**: http://localhost:9411
- **Consul**: http://localhost:8500
- **RabbitMQ**: http://localhost:15672 (admin/admin)

## Troubleshooting
