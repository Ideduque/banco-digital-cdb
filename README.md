# 💳 Banco Digital API 

Esta é uma API RESTful para um sistema de **Banco Digital**, desenvolvida com **Java (Spring Boot)**, **IntelliJ IDEA** e **MySQL**.
A aplicação permite o cadastro de clientes, criação de contas, emissão de cartões, operações bancárias (como PIX, transferências, rendimentos, saque e depósito), faturas e seguros.

Esse projeto foi desenvolvido para a conclusão do Curso de Java do EDUC360 - Codigo de Base

![Badge em Desenvolvimento](http://img.shields.io/static/v1?label=STATUS&message=EM%20DESENVOLVIMENTO&color=GREEN&style=for-the-badge)


## 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- Hibernate
- Maven
- IntelliJ IDEA

## 🧱 Estrutura do Projeto

- `controller` - Camada de controle (endpoints da API)
- `service` - Regras de negócio
- `repository` - Acesso ao banco de dados
- `entity` - Entidades JPA
- `dto` - Objetos de transferência de dados
- `config` - Configurações do projeto
- `exception` - Tratamento de erros (implementando)

## 📦 Funcionalidades

- Cadastro de clientes com validação de CPF, idade mínima e endereço
- Criação de contas (corrente e poupança)
- Emissão de cartões de crédito e débito com limites e regras personalizadas
- Operações bancárias (depósitos, transferências, PIX)
- Geração e pagamento de faturas
- Emissão de apólices de seguros vinculadas aos cartões

## 📌 Regras de Negócio

- Clientes devem ter idade mínima de 18 anos
- Contas podem ser do tipo corrente ou poupança
- Cartões de crédito têm limite baseado no tipo de conta
- Transações Pix devem validar saldo e limite
- Seguros são associados a cartões de crédito

## ⚙️ Como Executar

### Pré-requisitos

- Java 17+
- MySQL instalado e rodando
- IntelliJ IDEA (ou outro IDE de sua preferência)

### Passos

1. Clone o repositório:

```bash
git clone https://github.com/seu-usuario/banco-digital-api.git
cd banco-digital-api
```
2. Configure o `application.properties:`

```bash
   spring.datasource.url=jdbc:mysql://localhost:3306/banco_digital
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   spring.jpa.hibernate.ddl-auto=update
```
   
3. Crie o banco de dados no MySQL:

```bash
   CREATE DATABASE banco_digital;
```
4. Execute a aplicação a partir da sua IDE ou pelo terminal:

```bash
    ./mvnw spring-boot:run
```

## 📬 Endpoints principais

- `POST /clientes` - Cadastrar cliente
- `POST /contas` - Criar conta
- `POST /cartoes` - Emitir cartão
- `POST /transacoes/pix` - Realizar transferência Pix
- `POST /seguros` - Criar seguro
- `GET /faturas/{id}` - Buscar fatura

> Os endpoints foram testados e validados com o **Postman**.

## 👩‍💻 Autoria

Desenvolvido por **Joseilde Duque**  
Projeto pessoal para prática de back-end com Java e Spring Boot.


   


