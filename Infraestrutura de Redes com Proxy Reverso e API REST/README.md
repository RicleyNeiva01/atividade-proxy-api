# API REST de Clientes — Java, Spring Boot e PostgreSQL

Projeto da VM 2 para a atividade de infraestrutura com duas VMs e Nginx como proxy reverso.

## Integrantes

- Nome completo: **PREENCHER**
- Nome completo: **PREENCHER**

## Arquitetura e endereços IP

Preencha os IPs reais depois de configurar a rede das máquinas:

| Máquina | Função | IP |
|---|---|---|
| VM 1 | Nginx / Proxy Reverso | `IP_DA_VM1` |
| VM 2 | API Java + PostgreSQL | `IP_DA_VM2` |

A API escuta na porta `8080`. O PostgreSQL usa a porta `5432`. No desenho esperado, o cliente acessa a VM 1 pela porta 80 e o Nginx encaminha a requisição para `http://IP_DA_VM2:8080`.

## Requisitos

- Java JDK 21
- Maven 3.9+
- PostgreSQL
- Git (opcional para clonar o repositório)

Confira as instalações:

```bash
java -version
mvn -version
psql --version
```

## 1. Instalar PostgreSQL no Ubuntu Server

```bash
sudo apt update
sudo apt install postgresql postgresql-contrib -y
sudo systemctl enable --now postgresql
sudo systemctl status postgresql
```

## 2. Criar banco, usuário e tabela

Copie o projeto para a VM 2. Execute o script como administrador do PostgreSQL:

```bash
sudo -u postgres psql -f database/schema.sql
```

O script cria o usuário `api_user`, o banco `clientes_db`, a tabela `clientes` e dois registros de exemplo. **Antes de usar em rede real, altere a senha de exemplo no script e no ambiente da aplicação.**

Verifique os dados:

```bash
sudo -u postgres psql -d clientes_db -c "SELECT * FROM clientes;"
```

> O arquivo `database/schema.sql` contém comandos do `psql` (`\connect`), então execute-o com `psql`, não colando o arquivo inteiro em uma ferramenta SQL genérica.

## 3. Configurar conexão da API

Por padrão, `src/main/resources/application.properties` usa:

- URL: `jdbc:postgresql://localhost:5432/clientes_db`
- Usuário: `api_user`
- Senha: `troque_esta_senha`

É possível sobrescrever as configurações com variáveis de ambiente:

```bash
export DB_URL='jdbc:postgresql://localhost:5432/clientes_db'
export DB_USERNAME='api_user'
export DB_PASSWORD='SUA_SENHA_FORTE'
```

Ajuste a senha no banco e na variável `DB_PASSWORD` para que sejam iguais.

## 4. Compilar e executar a API

Na pasta raiz do projeto:

```bash
mvn clean package
java -jar target/api-clientes-1.0.0.jar
```

Ou em modo de desenvolvimento:

```bash
mvn spring-boot:run
```

A API ficará disponível na porta `8080`. O endereço `0.0.0.0` permite receber conexões por interfaces de rede da VM, mas o firewall deve permitir apenas o tráfego necessário. Para a atividade, permita a porta 8080 a partir da VM 1 e evite expor o PostgreSQL à rede pública.

## 5. Testar a API

Criar cliente:

```bash
curl -i -X POST http://localhost:8080/clientes \
  -H 'Content-Type: application/json' \
  -d '{"nome":"João da Silva","email":"joao@example.com","telefone":"74999991234"}'
```

Listar:

```bash
curl -i http://localhost:8080/clientes
```

Consultar ID 1:

```bash
curl -i http://localhost:8080/clientes/1
```

Atualizar ID 1:

```bash
curl -i -X PUT http://localhost:8080/clientes/1 \
  -H 'Content-Type: application/json' \
  -d '{"nome":"João Atualizado","email":"joao.atualizado@example.com","telefone":"74988887777"}'
```

Excluir ID 1:

```bash
curl -i -X DELETE http://localhost:8080/clientes/1
```

Os IDs devem ser os que realmente existem no banco.

## 6. Testar a partir da VM 1

Na VM 1, execute:

```bash
ping IP_DA_VM2
curl -i http://IP_DA_VM2:8080/clientes
```

Se o ping funcionar, mas o `curl` não, verifique se a API está ativa, se está escutando em `0.0.0.0:8080` e se o firewall permite a porta 8080 a partir da VM 1.

## Estrutura do projeto

```text
api-java-springboot-postgres/
├── src/main/java/br/com/projetoinfra/apiclientes/
│   ├── ApiClientesApplication.java
│   ├── controller/ClienteController.java
│   ├── exception/
│   ├── model/Cliente.java
│   ├── repository/ClienteRepository.java
│   └── service/ClienteService.java
├── src/main/resources/application.properties
├── database/schema.sql
├── docs/rotas.md
├── pom.xml
└── README.md
```

## Observações

- O Hibernate usa `ddl-auto=validate`: ele valida o esquema, mas não cria nem apaga tabelas automaticamente.
- E-mails duplicados retornam `409 Conflict`.
- Campos obrigatórios e formato de e-mail são validados.
- Não armazene senhas reais no GitHub. Use variáveis de ambiente.
- A configuração do virtual host Nginx pertence à pasta `/nginx` do repositório geral e será feita pelo responsável pela VM 1.
