# Sistema de Gerenciamento de Livros

CRUD básico para gerenciar livros. Backend em Java Spring Boot e frontend em React.

## Stack

**Backend**
- Java 17 
- Spring Boot 3
- PostgreSQL
- Maven

**Frontend**
- React 19
- Vite
- TailwindCSS

## Como rodar

Você tem duas opções:

### Opção 1: Tudo via Docker (Recomendado)

Na raiz do projeto:
```bash
docker-compose up -d
```

Isso sobe:
- Banco PostgreSQL na porta 5432
- API na porta 8080
- Frontend na porta 3000

Acesse: `http://localhost:3000`

Para parar tudo:
```bash
docker-compose down
```

### Opção 2: Apenas banco no Docker

Suba só o banco:
```bash
docker-compose up -d postgres
```

Rode a API:
```bash
cd books-management-api
./mvnw spring-boot:run
```

Rode o Frontend:
```bash
cd books-management-frontend
npm install
npm run dev
```

API: `http://localhost:8080`
Frontend: `http://localhost:3000`

## Endpoints

**Livros**
```
GET    /api/books       - lista todos
GET    /api/books/{id}  - busca por id
POST   /api/books       - cria novo
PUT    /api/books/{id}  - atualiza
DELETE /api/books/{id}  - deleta
```

**Histórico de atualizações do livro**
```
GET    /api/books/{id}/history  - histórico completo do livro
```

## Estrutura

```
books-management-api/
├── src/main/java/.../books_management/
│   ├── application/       - services, DTOs, mappers
│   │   ├── dto/
│   │   ├── mapper/
│   │   └── service/
│   ├── domain/            - entidades, repositories, exceções
│   │   ├── model/
│   │   ├── repository/
│   │   └── exception/
│   ├── presentation/      - controllers, tratamento de exceções
│   │   ├── controller/
│   │   └── exception/
│   └── infrastructure/    - configurações, listeners, eventos
│       ├── config/        - (CORS, etc)
│       └── listener/      - (auditoria)
├── src/main/resources/
├── Dockerfile
└── pom.xml

books-management-frontend/
├── src/
│   ├── components/        - componentes React
│   ├── services/          - chamadas API
│   └── App.jsx
├── Dockerfile
└── vite.config.js
```

## Features

- CRUD completo
- Validação de dados
- Modal de confirmação pra deletar
- Listagem em tabela
- Formulário com validação
- Tratamento de erros
- Histórico de alterações (auditoria)
- Rastreamento automático de mudanças

## Notas

- O Vite já tem proxy configurado pra API
- CORS configurado pro localhost:3000
- Banco usa `create-drop` (recria sempre que inicia)

## Problemas comuns

**Banco não conecta**: roda `docker ps` pra ver se o postgres tá up

**Erro de CORS**: verifica se tem o `CorsConfig.java`

**Porta ocupada**: muda em `application.properties` ou `vite.config.js`

**Rebuild dos containers**: `docker-compose up -d --build`
