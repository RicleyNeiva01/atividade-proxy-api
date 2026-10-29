# Documentação da API de Clientes

## Informações gerais

| Item | Valor |
|---|---|
| Base local | `http://localhost:8080` |
| Direto na VM 2 | `http://IP_DA_VM2:8080` |
| Através do Nginx (VM 1) | `http://IP_DA_VM1/clientes` |
| Formato | JSON (`Content-Type: application/json`) |

## Modelo: Cliente

| Campo | Tipo | Obrigatório | Regras |
|---|---|---|---|
| `id` | número | não (gerado) | Gerado pelo banco; ignorado no envio |
| `nome` | texto | sim | Até 120 caracteres |
| `email` | texto | sim | E-mail válido, até 160 caracteres, único |
| `telefone` | texto | não | Até 25 caracteres |
| `dataCadastro` | data/hora | não (gerado) | Preenchido automaticamente pelo backend |

## Resumo das rotas

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| POST | `/clientes` | Cria cliente | `201 Created` |
| GET | `/clientes` | Lista clientes | `200 OK` |
| GET | `/clientes/{id}` | Busca por ID | `200 OK` |
| PUT | `/clientes/{id}` | Atualiza cliente | `200 OK` |
| DELETE | `/clientes/{id}` | Exclui cliente | `204 No Content` |

---

## 1. Criar cliente

`POST /clientes`

**Corpo da requisição:**

```json
{
  "nome": "João da Silva",
  "email": "joao@example.com",
  "telefone": "(74) 99999-1234"
}
```

**Resposta `201 Created`:**

```json
{
  "id": 3,
  "nome": "João da Silva",
  "email": "joao@example.com",
  "telefone": "(74) 99999-1234",
  "dataCadastro": "2026-10-29T14:35:10.123456"
}
```

**Erros possíveis:** `400` (campo inválido), `409` (e-mail já cadastrado).

## 2. Listar clientes

`GET /clientes`

**Resposta `200 OK`:**

```json
[
  {
    "id": 1,
    "nome": "Ana Souza",
    "email": "ana@example.com",
    "telefone": "(74) 99999-1111",
    "dataCadastro": "2026-10-29T07:57:30.779502"
  },
  {
    "id": 2,
    "nome": "Carlos Lima",
    "email": "carlos@example.com",
    "telefone": "(74) 98888-2222",
    "dataCadastro": "2026-10-29T07:57:30.779502"
  }
]
```

Se não houver clientes, retorna `[]`.

## 3. Buscar cliente por ID

`GET /clientes/{id}`, por exemplo `/clientes/1`

**Resposta `200 OK`:**

```json
{
  "id": 1,
  "nome": "Ana Souza",
  "email": "ana@example.com",
  "telefone": "(74) 99999-1111",
  "dataCadastro": "2026-10-29T07:57:30.779502"
}
```

**Erro possível:** `404` (cliente não encontrado).

## 4. Atualizar cliente

`PUT /clientes/{id}`, por exemplo `/clientes/1`

Todos os campos são enviados (substituição completa). O `dataCadastro` original é mantido.

**Corpo da requisição:**

```json
{
  "nome": "Ana Souza Atualizada",
  "email": "ana.nova@example.com",
  "telefone": "(74) 97777-5555"
}
```

**Resposta `200 OK`:**

```json
{
  "id": 1,
  "nome": "Ana Souza Atualizada",
  "email": "ana.nova@example.com",
  "telefone": "(74) 97777-5555",
  "dataCadastro": "2026-10-29T07:57:30.779502"
}
```

**Erros possíveis:** `400`, `404`, `409` (e-mail já usado por outro cliente).

## 5. Excluir cliente

`DELETE /clientes/{id}`, por exemplo `/clientes/1`

**Resposta `204 No Content`** (sem corpo).

**Erro possível:** `404` (cliente não encontrado).

---

## Formato das respostas de erro

Todos os erros seguem o mesmo formato:

```json
{
  "dataHora": "2026-10-29T14:40:00.123456",
  "status": 404,
  "erro": "Not Found",
  "mensagem": "Cliente com ID 99 não encontrado."
}
```

| Status | Quando acontece | Exemplo de `mensagem` |
|---|---|---|
| `400 Bad Request` | Campo obrigatório ausente ou e-mail inválido | `email: Informe um e-mail válido.` |
| `404 Not Found` | ID inexistente | `Cliente com ID 99 não encontrado.` |
| `409 Conflict` | E-mail duplicado | `Já existe cliente cadastrado com esse e-mail.` |

## Exemplos com curl

```bash
# Criar
curl -i -X POST http://localhost:8080/clientes \
  -H 'Content-Type: application/json' \
  -d '{"nome":"João da Silva","email":"joao@example.com","telefone":"(74) 99999-1234"}'

# Listar
curl -i http://localhost:8080/clientes

# Buscar
curl -i http://localhost:8080/clientes/1

# Atualizar
curl -i -X PUT http://localhost:8080/clientes/1 \
  -H 'Content-Type: application/json' \
  -d '{"nome":"Ana Atualizada","email":"ana.nova@example.com","telefone":"(74) 97777-5555"}'

# Excluir
curl -i -X DELETE http://localhost:8080/clientes/1
```