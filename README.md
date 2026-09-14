# FIAP - Tech Challenge - DeiviMotors Service

![Java](https://img.shields.io/badge/java-21-red?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/spring%20boot-3.2.5-brightgreen?logo=spring)
![Docker](https://img.shields.io/badge/docker-compose-2496ED?logo=docker&logoColor=white)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

# SonarCloud Code Coverage

Deivi Motors Service
[Coverage](https://sonarcloud.io/project/overview?id=soat-deivi-motors-service)

Deivi Webhook Service
[Coverage](https://sonarcloud.io/project/overview?id=soat-deivi-motors-webhook)
---

## Visao geral

Repositório do serviço de Deivi Motors. Ele inclui dois aplicativos:

- **deivimotors-service**: API principal para venda de veículos (persistência em MongoDB).
- **webhook**: endpoint que simula callbacks do provedor e aciona o checkout no deivimotors-service para finalizar o pagamento (aprovando ou recusando).

A infraestrutura local usa **MongoDB** via Docker Compose.

## Componentes e arquitetura

- **deivimotors-service** (porta `8082`, base `/deivi_motors`)
    - API REST + Swagger
    - Cadastrar um veículo para venda
    - Consulta de veículo específico
    - Edita os dados do veículo
    - Listagem de veículos à venda, ordenada por preço, do mais barato para o mais caro
    - Listagem de veículos vendidos, ordenada por preço, do mais barato para o mais caro
    - Efetua a venda do veículo
    - Recebe atualização do pagamento e atualiza o status da venda.
    - Persistencia em MongoDB
- **webhook** (porta `9090`, base `/webhook`)
    - Simula callbacks de pagamento
    - Chama o checkout do deivimotors-service via HTTP (OpenFeign)

Ambos seguem a abordagem de **Ports & Adapters (Hexagonal)** com camadas de dominio isoladas.

## Fluxo principal

1. **Venda criada** com status `EM_ANDAMENTO`.
2. Cria o pagamento com status `AGUARDANDO_PAGAMENTO`.
3. O provedor de pagamento chama o **webhook** com o novo status, `PAGAMENTO_APROVADO` ou `PAGAMENTO_RECUSADO`.
4. O webhook dispara o **checkout** no deivimotors-service.
5. O deivimotors-service atualiza o status do pagamento e atualiza o status da venda e do veículo.
   1. Caso pagamento `PAGAMENTO_APROVADO`, venda = `CONCLUIDO` e veículo = `VENDIDO`.
   2. Caso pagamento `PAGAMENTO_RECUSADO`, venda = `CANCELADO`.

## Tecnologias

- Java 21, Spring Boot 3.2.5
- MongoDB
- MapStruct, Lombok
- SpringDoc OpenAPI (Swagger UI)
- Docker / Docker Compose

## Como executar tudo com Docker (apps + dependencias)

Sobe deivimotors-service, webhook, MongoDB no mesmo network do Docker.

```bash
docker compose up --build -d
```

Após subir:

- **DeiviMotors API**: `http://localhost:8082/deivi_motors`
- **Swagger (DeiviMotors)**: `http://localhost:8082/deivi_motors/swagger-ui`
- **Webhook API**: `http://localhost:9090/webhook`
- **Swagger (Webhook)**: `http://localhost:9090/webhook/swagger-ui`
- **MongoDB**: `mongodb://localhost:27017` (usuario `admin`, senha `admin`)

Health checks:

- `http://localhost:8082/deivi_motors/actuator/health`
- `http://localhost:9090/webhook/actuator/health`

## Como executar apps localmente (dependencias no Docker)

Use quando quiser debugar no IDE: as apps rodam na sua máquina e apenas MongoDB fica em containers.

1. Suba apenas as dependencias:
   ```bash
   docker compose up -d mongodb
   ```
2. DeiviMotors service:
   ```bash
   cd deivimotors-service
   ./mvnw spring-boot:run
   ```
3. Webhook:
   ```bash
   cd webhook
   ./mvnw spring-boot:run
   ```

Para Windows, use `mvnw.cmd` no modulo `deivimotors-service`.

## Endpoints principais

Base **deivimotors-service**: `http://localhost:8082/deivi_motors`

| Metodo  | Endpoint                                            | Descricao                                           |
|---------|-----------------------------------------------------|-----------------------------------------------------|
| POST    | `/deivi_motors/v1/sales`                            | Cria venda do veículo                               |
| GET     | `/deivi_motors/v1/sales/{salesId}`                  | Consulta venda pelo id                              |
| POST    | `/deivi_motors/v1/vehicles`                         | Cria veículo para venda                             |
| PUT     | `/deivi_motors/v1/vehicles/{vehicleId}`             | Atualiza o veículo existente                        |
| GET     | `/deivi_motors/v1/vehicles/{vehicleId}`             | Consulta veículo pelo id                            |
| GET     | `/deivi_motors/v1/payments/sale/{saleId}`           | Consulta pagamento pelo id da venda                 |
| POST    | `/deivi_motors/v1/payments/sale/{saleId}`           | Cadastrar um novo pagamento para venda nova         |
| POST    | `/deivi_motors/v1/payments/sale/{saleId}/checkout`  | Finalizar o checkout de um pagamento                |
| POST    | `/deivi_motors/v1/sales/customer/purchases`         | Lista de vendas do Usuario recuperado do token      |
| POST    | `/deivi_motors/v1/sales/customer/{cpf}`             | Administrativo, Lista de vendas do Usuario pelo CPF |


Status validos de pagamento: `AGUARDANDO_PAGAMENTO`, `PAGAMENTO_APROVADO`, `PAGAMENTO_RECUSADO`.


Base **webhook**: `http://localhost:9090/webhook`

| Metodo | Endpoint                         | Descricao                   |
|--------|----------------------------------|-----------------------------|
| POST   | `/webhook/v1/payments/{saleId}` | Recebe callback do provedor  |

## Configuracao

Variaveis usadas no ambiente local (via `docker-compose.yml`):

- `SPRING_PROFILES_ACTIVE=local`
- `MONGODB_URL=mongodb://admin:admin@mongodb:27017/deivimotorsDB?authSource=admin`

## Testes

```bash
# deivimotors-service
cd deivimotors-service
mvn clean verify

# webhook
cd webhook
mvn clean verify
```

## Contato

| Nome                     | Apelido Discord     | RMs    | Contato                      |
|--------------------------|---------------------|--------|------------------------------|
| Deividson Macedo Gonini  | deividsongonini5231 | 363326 | deividsongonini@gmail.com    |

---
