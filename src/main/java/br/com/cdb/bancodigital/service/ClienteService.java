package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.dto.EnderecoDTO;
import br.com.cdb.bancodigital.entity.Cliente;
import br.com.cdb.bancodigital.entity.Endereco;
import br.com.cdb.bancodigital.repository.ClienteRepository;
import br.com.cdb.bancodigital.validation.ValidadorCPF;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public Cliente cadastrarCliente( Cliente cliente)
    {

        // Converte a data e verifica idade
        LocalDate nascimento = cliente.getDataNascimento();
        if (Period.between(nascimento, LocalDate.now()).getYears() < 18) {
            throw new RuntimeException("Cliente deve ser maior de idade.");
        }
        
        // Verifica se CPF já existe
        if (clienteRepository.findByCpf(cliente.getCpf()).isPresent())
        {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        // Valida CPF (lógica baseada em algoritmo brasileiro)
        if (!ValidadorCPF.validarCPF(cliente.getCpf())) {
            throw new RuntimeException("CPF inválido.");
        }

        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente atualizarCliente(Long id, Cliente novoCliente) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        cliente.setNome(novoCliente.getNome());
        cliente.setEndereco(novoCliente.getEndereco());
        cliente.setCategoria(novoCliente.getCategoria());
        return clienteRepository.save(cliente);
    }

    public void deletarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        clienteRepository.delete(cliente);
    }

    private Endereco mapearEndereco(EnderecoDTO dto) {
        Endereco e = new Endereco();
        e.setRua(dto.getRua());
        e.setNumero(dto.getNumero());
        e.setComplemento(dto.getComplemento());
        e.setCidade(dto.getCidade());
        e.setEstado(dto.getEstado());
        e.setCep(dto.getCep());
        return e;
    }
}

