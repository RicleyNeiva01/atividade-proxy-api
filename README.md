# API REST de Clientes com Proxy Reverso Nginx

Atividade de infraestrutura com duas VMs: o cliente acessa a VM 1 (Nginx), que repassa as requisições para a API Java + PostgreSQL na VM 2.

```text
Cliente (PC) ──> VM 1 (Nginx, porta 80) ──> VM 2 (API :8080 / PostgreSQL :5432)
```

## Integrantes

| Nome | Responsabilidade |
|---|---|
| PREENCHER | API REST e banco de dados (VM 2) |
| PREENCHER | VMs e proxy reverso Nginx (VM 1) |

## IPs das máquinas

| Máquina | Função | IP |
|---|---|---|
| VM 1 | Nginx / Proxy Reverso | PREENCHER |
| VM 2 | API Java + PostgreSQL | PREENCHER |

## Estrutura do repositório

```text
├── api/        código-fonte da API (Spring Boot)
├── database/   script SQL de criação do banco
├── nginx/      arquivo de configuração do Nginx
├── docs/       documentação das rotas da API
└── README.md
```

## Requisitos (VM 2)

- Java JDK 21
- Maven 3.9+
- PostgreSQL

## Como rodar a API (VM 2)

### 1. Criar usuário, banco e tabela

```bash
sudo -u postgres psql -f database/schema.sql
```

O script cria o usuário `api_user`, o banco `clientes_db`, a tabela `clientes` e dois registros de exemplo.

### 2. Configurar a conexão (opcional)

Por padrão a API usa `localhost:5432`, banco `clientes_db`, usuário `api_user` e senha `cliente123`. Para alterar:

```bash
export DB_URL='jdbc:postgresql://localhost:5432/clientes_db'
export DB_USERNAME='api_user'
export DB_PASSWORD='SUA_SENHA'
```

### 3. Compilar e executar

```bash
cd api
mvn clean package
java -jar target/api-clientes-1.0.0.jar
```

A API sobe na porta `8080`.

### 4. Testar

```bash
curl -i http://localhost:8080/clientes
```

## Rotas da API

| Método | Rota | Descrição |
|---|---|---|
| POST | `/clientes` | Cria cliente |
| GET | `/clientes` | Lista clientes |
| GET | `/clientes/{id}` | Busca por ID |
| PUT | `/clientes/{id}` | Atualiza cliente |
| DELETE | `/clientes/{id}` | Exclui cliente |

Detalhes e exemplos em [`docs/rotas.md`](docs/rotas.md).

## Firewall (VM 2)

Libere a porta 8080 apenas para a VM 1 e não exponha o PostgreSQL:

```bash
sudo ufw allow from IP_DA_VM1 to any port 8080 proto tcp
```

## Nginx (VM 1)

PREENCHER pelo responsável da VM 1: copiar o arquivo de `nginx/` para `/etc/nginx/sites-available/`, ativar o site em `sites-enabled/`, remover o link do `default` e recarregar o Nginx.

## Observações

- O Hibernate usa `ddl-auto=validate`: não cria nem altera tabelas.
- E-mail duplicado retorna `409 Conflict`; campos inválidos retornam `400`.
- Não versione senhas reais; use variáveis de ambiente.
