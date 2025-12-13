# Books Management

Sistema de livros e avaliações com arquitetura de microsserviços.

## Stack

- **Backend**: Java 21, Spring Boot 3.3.5, Spring Cloud Gateway
- **Frontend**: React 19, Vite, TailwindCSS
- **Databases**: PostgreSQL 16, MongoDB 7
- **Infra**: Consul, RabbitMQ, Zipkin, Loki, Grafana

## Arquitetura

```
Frontend (React) → API Gateway → Books Service (PostgreSQL)
                              → Reviews Service (MongoDB)

Consul (Service Discovery)
RabbitMQ (Events entre serviços)
Zipkin + Loki + Grafana (Observabilidade)
```

## Rodando

### Docker Compose

```bash
# Instalar plugin do Loki (só uma vez)
docker plugin install grafana/loki-docker-driver:latest --alias loki --grant-all-permissions

# Subir tudo
docker-compose up -d

# Parar
docker-compose down
```

**URLs:**

- Frontend: http://localhost:3000
- API Gateway: http://localhost:8080
- Consul: http://localhost:8500
- RabbitMQ: http://localhost:15672 (admin/admin)
- Zipkin: http://localhost:9411
- Grafana: http://localhost:3001 (admin/admin)

### Kubernetes (Minikube)

```bash
# Deploy
cd k8s && ./deploy.sh

# Cleanup
cd k8s && ./cleanup.sh
```

**URLs (NodePort):**
| Serviço | Porta |
|---------|-------|
| Frontend | 30000 |
| API Gateway | 30080 |
| Grafana | 30001 |
| Zipkin | 30411 |
| Consul | 30500 |
| RabbitMQ | 30672 |

```bash
# Pegar IP do Minikube
minikube ip
# Acessar: http://<IP>:<PORTA>
```

### Dev Local

```bash
# Dependências
docker-compose up -d consul rabbitmq zipkin loki grafana postgres mongodb

# Backend (cada um em um terminal)
cd api-gateway && ./mvnw spring-boot:run
cd books-management-api && ./mvnw spring-boot:run
cd reviews-service && ./mvnw spring-boot:run

# Frontend
cd books-management-frontend && npm install && npm run dev
```

## API

Todas as rotas passam pelo Gateway (:8080)

**Books**: `/api/books`

- GET, POST, PUT, DELETE
- Histórico com auditoria

**Reviews**: `/api/reviews`

- Avaliações 1-5 estrelas
- Estatísticas por livro

## Estrutura

```
api-gateway/              # Spring Cloud Gateway
books-management-api/     # Serviço de livros (PostgreSQL + Hibernate Envers)
reviews-service/          # Serviço de avaliações (MongoDB)
books-management-frontend/# React
k8s/                      # Manifests Kubernetes
scripts/                  # Seeds e utilitários
```

## Seeds

Scripts para popular e limpar o banco com dados fake.

```bash
cd scripts
npm install

# Popular (500 livros + 1500 reviews)
npm run seed

# Limpar tudo
npm run clear
```

Para facilitar, a URL do API Gateway nos códigos referente a seed está fixa para rodar a aplicação no Minikube. Altere conforme necessário no arquivo `scripts/seed.js` e `scripts/clear.js`.
