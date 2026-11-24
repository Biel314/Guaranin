# Guaranin - Backend

## Sobre o Projeto

O Guaranin é uma API REST desenvolvida como parte de um Trabalho de Conclusão de Curso (TCC) com foco no combate à dependência digital relacionada a plataformas de apostas online, redes sociais e outros serviços de alto potencial aditivo.

O projeto utiliza conceitos de gamificação para incentivar a formação e manutenção de hábitos saudáveis, recompensando usuários que evitam o acesso a aplicações e URLs monitoradas. A proposta utiliza elementos inspirados em jogos digitais, como mascotes virtuais, metas, progressão e acompanhamento de comportamento, visando promover mudanças positivas de hábito.

Este repositório contém o backend da aplicação, responsável pelo gerenciamento de usuários, autenticação, monitoramento, persistência de dados e regras de negócio do sistema.

---

## Tecnologias Utilizadas

### Linguagem

- Kotlin

### Backend

- Spring Boot
- Spring MVC
- Spring Data JPA
- Spring Security
- JWT (JSON Web Token)

### Banco de Dados

- MySQL

### Cache e Autenticação

- Redis
- API Key Validation

### Documentação

- Swagger / OpenAPI

### View Layer para testes internos

- Thymeleaf

---

## Arquitetura

O projeto segue uma arquitetura em camadas:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Banco de Dados
```

Além disso, utiliza:

```text
DTO
Mapper
Entity
```

para desacoplamento entre o modelo de domínio e os contratos expostos pela API.

---

## Funcionalidades Implementadas

### Autenticação

- Login utilizando e-mail e senha
- Geração de JWT
- Proteção de endpoints por token
- Controle de acesso via Spring Security
- Validação adicional utilizando API Key

### Usuários

- Cadastro
- Consulta
- Atualização
- Remoção
- Validação de e-mail
- Validação de apelido
- Validação de senha

### Mascotes Virtuais

- Cadastro de mascotes
- Consulta por usuário
- Atualização
- Remoção
- Controle de mascotes ativos
- Mecânica de morte do mascote

### Aplicativos Monitorados

- Cadastro de aplicativos monitorados
- Consulta por usuário
- Consulta por nome
- Atualização
- Remoção
- Controle através da associação Usuário ↔ Aplicativo

### URLs Monitoradas

- Cadastro de URLs monitoradas
- Normalização automática de links
- Consulta por usuário
- Atualização
- Remoção
- Controle através da associação Usuário ↔ URL

### Histórico de Uso

- Registro de acessos a aplicativos
- Registro de acessos a URLs
- Histórico de monitoramento
- Consultas paginadas
- Filtros por período

### Metas

- Cadastro de metas
- Consulta
- Atualização
- Remoção
- Associação ao usuário

---

## Modelagem

### Entidades Principais

- Usuario
- Mascote
- Meta
- AplicativoMonitorado
- URLMonitorada
- AplicativoHistorico
- URLHistorico
- HistoricoSessoesUso
- Estado
- Humor

### Entidades Associativas

- UsuarioAplicativo
- UsuarioURL

As entidades associativas são utilizadas internamente para representar os relacionamentos entre usuários e recursos monitorados.

Essas entidades não são expostas diretamente pela API, sendo manipuladas pelas camadas de serviço.

---

## Estrutura da API

Base URL:

```text
/api/v1
```

### Exemplo de Endpoints

#### Login

```http
POST /api/v1/auth/login
```

#### Usuários

```http
GET    /api/v1/usuarios
GET    /api/v1/usuarios/{usuarioId}
POST   /api/v1/usuarios
PUT    /api/v1/usuarios/{usuarioId}
DELETE /api/v1/usuarios/{usuarioId}
```

#### Mascotes

```http
GET    /api/v1/mascotes
GET    /api/v1/mascotes/{mascoteId}
POST   /api/v1/mascotes
PUT    /api/v1/{mascoteId}
DELETE /api/v1/mascotes/{mascoteId}
```

#### Aplicativos Monitorados

```http
GET    /api/v1/aplicativos-monitorados
POST   /api/v1/aplicativos-monitorados
PUT    /api/v1/aplicativos-monitorados/{aplicativoId}
DELETE /api/v1/aplicativos-monitorados/{aplicativoId}
```

#### URLs Monitoradas

```http
GET    /api/v1/urls-monitoradas
POST   /api/v1/urls-monitoradas
PUT    /api/v1/urls-monitoradas/{urlId}
DELETE /api/v1/urls-monitoradas/{urlId}
```

---

## Execução do Projeto

### Pré-requisitos

- Java 21+
- Maven 3.9+
- MySQL
- Redis (opcional, dependendo da configuração de API Key)
- Git

### Clonar o Repositório

```bash
git clone https://github.com/Biel314/Guaranin.git
cd Guaranin
```

### Compilar o Projeto

```bash
mvn clean install
```

### Executar a Aplicação

```bash
mvn spring-boot:run
```

### Executar os Testes

```bash
mvn test
```

### Gerar Artefato JAR

```bash
mvn clean package
```

O arquivo será gerado em:

```text
target/guaranin-*.jar
```

Para executar o JAR:

```bash
java -jar target/guaranin-*.jar
```

---

## Documentação da API

Após iniciar a aplicação, a documentação OpenAPI/Swagger estará disponível em:

```text
http://localhost:8080/swagger-ui.html
```

ou

```text
http://localhost:8080/swagger-ui/index.html
```

---

## Exemplo de Login

```http
POST /api/v1/auth/login
Content-Type: application/json
```

```json
{
  "email": "admin@email.com",
  "password": "@Admin123"
}
```

Resposta:

```json
{
  "token": "JWT_TOKEN"
}
```

Utilização do token:

```http
Authorization: Bearer JWT_TOKEN
```

## Objetivo Acadêmico

O sistema foi desenvolvido como parte de um Trabalho de Conclusão de Curso voltado para investigar a utilização de mecânicas de gamificação no combate a comportamentos compulsivos associados ao uso de plataformas digitais.
A proposta combina conceitos de:

- Gamificação;
- Formação de hábitos;
- Dependência digital;
- Engenharia de Software;
- Desenvolvimento Seguro;
- APIs REST;
- Arquiteturas em Camadas.

---

## Repositório

GitHub:

- https://github.com/Biel314/Guaranin

---

## Autores

### Gabriel de Oliveira Lima Silva

GitHub: https://github.com/GabrielGit10110

### Lucas Pereira de Mattos Sartorelli

GitHub: https://github.com/VonLanplace

### Gabriel de Sousa Ordonho

GitHub: https://github.com/Biel314

---

## Licença

Projeto desenvolvido para fins acadêmicos como Trabalho de Conclusão de Curso da Faculdade de Tecnologia do Estado de São Paulo (FATEC).
