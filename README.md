# FlowOrder — Microsserviços Assíncronos com RabbitMQ

Sistema distribuído baseado em microsserviços para processamento de pedidos de forma assíncrona, utilizando mensageria com RabbitMQ.

O projeto simula um fluxo real de e-commerce, onde cada etapa é desacoplada e processada de forma independente.

---

## 🚀 Visão Geral

A aplicação segue uma arquitetura orientada a eventos (event-driven), composta por três serviços:

```text
Order Service → Payment Service → Notification Service
```

Cada serviço possui responsabilidade única e se comunica exclusivamente por filas.

---

## 🔄 Fluxo Completo

1. Um pedido é criado via API (`Order Service`)
2. O pedido é persistido no banco
3. Um evento é publicado na fila `order.created`
4. O `Payment Service` consome esse evento
5. O pagamento é processado e salvo
6. Um novo evento é publicado em `payment.created`
7. O `Notification Service` consome o evento
8. A notificação é processada e persistida

---

## 🧠 Decisões de Arquitetura

* Comunicação assíncrona para desacoplamento
* Uso de filas para resiliência e escalabilidade
* Dead Letter Queues (DLQ) para tratamento de falhas
* DTO compartilhado (`common`) para padronização de contrato
* Persistência isolada por serviço

---

## 🏗️ Stack Tecnológica

* Java 17
* Spring Boot
* RabbitMQ (CloudAMQP)
* PostgreSQL (Neon)
* Docker
* Maven
* JUnit
* Swagger (OpenAPI)

---

## 🔁 Filas e Tratamento de Falhas

| Fila              | Função                      |
| ----------------- | --------------------------- |
| `order.created`   | Evento de criação de pedido |
| `payment.created` | Evento de pagamento         |
| `boq.<service>`   | Dead Letter Queue (falhas)  |

---

## 📦 Estrutura do Projeto

```text
store-queue-project/
├── order-service/
├── payment-service/
├── notification-service/
├── common/
└── docker-compose.yml
```

---

## ⚙️ Configuração

### Variáveis de ambiente (todos os serviços)

```text
RABBITMQ_URI=
RABBITMQ_HOST=
RABBITMQ_PORT=
RABBITMQ_USERNAME=
RABBITMQ_PASSWORD=

DB_URL=
DB_USERNAME=
DB_PASSWORD=
```

---

## 🧪 Executando Localmente

### 1. Subir o RabbitMQ

```bash
docker-compose up -d
```

---

### 2. Build do projeto

```bash
mvn clean install
```

---

### 3. Executar os serviços

Execute cada serviço separadamente:

```bash
cd order-service
mvn spring-boot:run
```

Repita para `payment-service` e `notification-service`.

---

## 📡 Documentação da API

Swagger disponível em:

```text
/swagger-ui/index.html
```

### Endpoints

* `POST /orders`
* `GET /orders`
* `GET /orders/{id}`

---

## 🧪 Como Testar o Fluxo Completo

### 1. Criar um pedido

```json
POST /orders
{
  "product_name": "Product Test",
  "amount": 99.99,
  "status": "CREATED",
  "status_reason": "CREATING ORDER"
}
```

---

### 2. Validar processamento

Após o POST:

* ✔ Pedido salvo no banco (Order Service)
* ✔ Mensagem enviada para RabbitMQ
* ✔ Payment Service consome e salva pagamento
* ✔ Evento publicado em `payment.created`
* ✔ Notification Service consome e persiste notificação

---

### 3. Validar falhas (opcional)

* Forçar erro no processamento
* Verificar envio para fila `boq.<service>`

---

## 🧪 Testes

Todos os serviços possuem testes unitários focados na lógica de negócio.

```bash
mvn test
```

---

## 🔥 Funcionalidades

* Pipeline assíncrono completo
* Comunicação desacoplada via mensageria
* Tratamento de falhas com DLQ
* Persistência independente por serviço
* Estrutura modular e escalável

---

## 🚀 Possíveis Evoluções

* Observabilidade (logs centralizados + tracing)
* Autenticação e autorização (JWT)
* Estratégias avançadas de retry/backoff

---

## 📌 Conclusão

Este projeto demonstra, na prática:

* Arquitetura de microsserviços orientada a eventos
* Processamento assíncrono com RabbitMQ
* Separação clara de responsabilidades
* Integração entre serviços de forma resiliente

---

## 👨‍💻 Autor: Vinicius Vicente Pereira Rosa

Projeto desenvolvido com foco em evolução prática em backend e sistemas distribuídos.
