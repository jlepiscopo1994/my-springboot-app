# Vaultdemo Backend Application

This is a Spring Boot backend application that demonstrates secure configuration management using **HashiCorp Vault** with **persistent secret storage**, integrated with a **MySQL** database.

## Features

- Spring Boot 3.4.5 application with Java 21
- Securely loads sensitive configuration (database credentials, tokens) from Vault
- Uses persistent Vault storage with file backend for durable secrets
- Connects to MySQL database with credentials dynamically injected from Vault
- Implements CRUD operations for `Product` entity via REST API
- Proper layering with Controller, Service, and Repository
- Unit tests with JUnit 5 and Mockito for service and controller layers

## Prerequisites

- Java 21+
- Maven 3.8+
- MySQL 8+
- HashiCorp Vault installed and running locally (configured with persistent file storage)

## Setup

### Vault

1. Configure Vault with persistent file backend (`vault-config.hcl`):

storage "file" {
path = "./vault-data"
}

listener "tcp" {
address = "127.0.0.1:8200"
tls_disable = 1
}

disable_mlock = true
ui = true


2. Start Vault:

vault server -config=vault-config.hcl


3. Initialize and unseal Vault:

vault operator init
vault operator unseal <key1>
vault operator unseal <key2>
vault operator unseal <key3>
vault login <root_token>


4. Enable KV secrets engine at `/secret` path:

vault secrets enable -path=secret kv


5. Add application secrets:

vault kv put secret/springstudent db_username=springstudent dbname=mydatabase password=springstudent


### Database

- Create MySQL database `mydatabase`.
- Create user `(of your choice)` with appropriate privileges.
- Create table `product`:

CREATE TABLE product (
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(255) NOT NULL,
price DECIMAL(10, 2) NOT NULL
);


### Application

- Configure Vault token in `src/main/resources/secrets.properties` (ignored by git):

* message devs for secrets.properties *


- Configure `application.properties` to import Vault secrets and use Vault token:

spring.config.import=optional:classpath:secrets.properties,vault://

spring.cloud.vault.uri=http://127.0.0.1:8200
spring.cloud.vault.token=${vault.token.secret}
spring.cloud.vault.kv.backend=secret
spring.cloud.vault.kv.application-name=springstudent
spring.cloud.vault.fail-fast=true

spring.datasource.url=jdbc:mysql://localhost:3306/${dbname}
spring.datasource.username=${db_username}
spring.datasource.password=${password}

spring.jpa.hibernate.ddl-auto=none


## Running the Application

Build and run with Maven:

mvn clean package
mvn spring-boot:run


Access REST endpoints at:

- `GET /api/products` - list all products
- `GET /api/products/{id}` - get product by ID
- `POST /api/products` - create product
- `PUT /api/products/{id}` - update product
- `DELETE /api/products/{id}` - delete product

## Testing

- Unit tests use JUnit 5 and Mockito.
- Run tests with:


## Notes

- `secrets.properties` is excluded from version control for security.
- Vault must be unsealed and running before starting the app.
- Avoid using property keys that conflict with system environment variables.

## License

[MIT](LICENSE)

---

🙏 Thanks for checking out the project! Feel free to contribute or raise issues.

