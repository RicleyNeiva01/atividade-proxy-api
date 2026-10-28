# Documentação da API de Clientes

Base local: `http://localhost:8080`  
Pela rede: `http://IP_DA_VM2:8080`  
Através do Nginx: `http://IP_DA_VM1/clientes` (depende da configuração do proxy).

## 1. Criar cliente

- Método: `POST`
- Rota: `/clientes`
- Sucesso: `201 Created`

Exemplo de corpo JSON:

```json
{
  "nome": "João da Silva",
  "email": "joao@example.com",
  "telefone": "(74) 99999-1234"
}
```

## 2. Listar clientes

- Método: `GET`
- Rota: `/clientes`
- Sucesso: `200 OK`

Retorna uma lista JSON. Se não houver clientes, retorna `[]`.

## 3. Consultar cliente

- Método: `GET`
- Rota: `/clientes/{id}`
- Exemplo: `/clientes/1`
- Sucesso: `200 OK`
- Se não existir: `404 Not Found`

## 4. Atualizar cliente

- Método: `PUT`
- Rota: `/clientes/{id}`
- Exemplo: `/clientes/1`
- Sucesso: `200 OK`

Corpo JSON:

```json
{
  "nome": "João Silva Atualizado",
  "email": "joao.novo@example.com",
  "telefone": "(74) 97777-5555"
}
```

## 5. Excluir cliente

- Método: `DELETE`
- Rota: `/clientes/{id}`
- Exemplo: `/clientes/1`
- Sucesso: `204 No Content`

## Validações e erros

- `400 Bad Request`: campos obrigatórios ausentes ou e-mail inválido.
- `404 Not Found`: cliente não encontrado.
- `409 Conflict`: e-mail já utilizado.
- O e-mail deve ser único.
- `dataCadastro` é preenchida automaticamente pelo backend.
