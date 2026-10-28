package br.com.projetoinfra.apiclientes.service;

import br.com.projetoinfra.apiclientes.exception.EmailJaCadastradoException;
import br.com.projetoinfra.apiclientes.exception.RecursoNaoEncontradoException;
import br.com.projetoinfra.apiclientes.model.Cliente;
import br.com.projetoinfra.apiclientes.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Cliente criar(Cliente cliente) {
        if (repository.existsByEmailIgnoreCase(cliente.getEmail())) {
            throw new EmailJaCadastradoException("Já existe cliente cadastrado com esse e-mail.");
        }
        cliente.setId(null);
        return repository.save(cliente);
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente com ID " + id + " não encontrado."));
    }

    @Transactional
    public Cliente atualizar(Long id, Cliente dados) {
        Cliente existente = buscarPorId(id);

        if (repository.existsByEmailIgnoreCaseAndIdNot(dados.getEmail(), id)) {
            throw new EmailJaCadastradoException("Outro cliente já utiliza esse e-mail.");
        }

        existente.setNome(dados.getNome());
        existente.setEmail(dados.getEmail());
        existente.setTelefone(dados.getTelefone());

        return repository.save(existente);
    }

    @Transactional
    public void excluir(Long id) {
        Cliente existente = buscarPorId(id);
        repository.delete(existente);
    }
}
