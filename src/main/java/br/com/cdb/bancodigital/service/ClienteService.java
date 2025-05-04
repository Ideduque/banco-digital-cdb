package br.com.cdb.bancodigital.service;

import br.com.cdb.bancodigital.dto.ClienteDTO;
import br.com.cdb.bancodigital.dto.EnderecoDTO;
import br.com.cdb.bancodigital.entity.Cliente;
import br.com.cdb.bancodigital.entity.Endereco;
import br.com.cdb.bancodigital.repository.ClienteRepository;
import br.com.cdb.bancodigital.validation.ValidadorCPF;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service // Define que essa classe é um serviço gerenciado pelo Spring
@RequiredArgsConstructor // Lombok: cria automaticamente o construtor com as dependências necessárias
public class ClienteService
{
    private final ClienteRepository clienteRepository; // Injeção do repositório para interagir com o banco

    // Método para cadastrar um novo cliente a partir de um ClienteDTO
    public ClienteDTO cadastrarCliente(ClienteDTO clienteDTO)
    {
        // Converte o ClienteDTO para a entidade Cliente
        Cliente cliente = fromDTO(clienteDTO);

        // Verifica a idade do cliente antes de cadastrar
        LocalDate nascimento = cliente.getDataNascimento();
        if (Period.between(nascimento, LocalDate.now()).getYears() < 18)
        {
            throw new RuntimeException("Cliente deve ser maior de idade.");
        }

        // Verifica se o CPF do cliente já está cadastrado
        if (clienteRepository.findByCpf(cliente.getCpf()).isPresent())
        {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        // Valida o CPF do cliente com base no algoritmo de validação
        if (!ValidadorCPF.validarCPF(cliente.getCpf()))
        {
            throw new RuntimeException("CPF inválido.");
        }

        // Salva o cliente no banco de dados
        Cliente clienteSalvo = clienteRepository.save(cliente);

        // Converte o cliente salvo para ClienteDTO e retorna
        return toDTO(clienteSalvo);
    }

    // Método para listar todos os clientes cadastrados como ClienteDTO
    public List<ClienteDTO> listarTodos()
    {
        List<Cliente> clientes = clienteRepository.findAll(); // Busca todos os clientes no banco
        return clientes.stream()
                .map(this::toDTO) // Converte cada Cliente para ClienteDTO
                .collect(Collectors.toList()); // Retorna a lista de DTOs
    }

    // Método para buscar um cliente pelo ID e retornar como ClienteDTO
    public ClienteDTO buscarPorId(Long id)
    {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        // Converte o Cliente para ClienteDTO e retorna
        return toDTO(cliente);
    }

    // Método para atualizar os dados de um cliente existente, usando ClienteDTO
    public ClienteDTO atualizarCliente(Long clienteId, ClienteDTO clienteDTO)
    {
        Cliente clienteExistente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        // Atualiza os dados do cliente com os novos dados do ClienteDTO
        clienteExistente.setNome(clienteDTO.getNome());
        clienteExistente.setEndereco(mapearEndereco(clienteDTO.getEndereco())); // Mapeia o EnderecoDTO para Endereco
        clienteExistente.setCategoria(clienteDTO.getCategoria());

        // Salva o cliente atualizado no banco
        Cliente clienteAtualizado = clienteRepository.save(clienteExistente);

        // Converte o Cliente atualizado para ClienteDTO e retorna
        return toDTO(clienteAtualizado);
    }

    // Método para deletar um cliente pelo ID
    public void deletarCliente(Long id)
    {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        // Deleta o cliente do banco de dados
        clienteRepository.delete(cliente);
    }

    // Método para converter ClienteDTO para Cliente (entidade)
    private Cliente fromDTO(ClienteDTO clienteDTO)
    {
        Cliente cliente = new Cliente();
        cliente.setCpf(clienteDTO.getCpf());
        cliente.setNome(clienteDTO.getNome());
        cliente.setDataNascimento(clienteDTO.getDataNascimento());
        cliente.setEndereco(mapearEndereco(clienteDTO.getEndereco())); // Mapeia EnderecoDTO para Endereco
        cliente.setCategoria(clienteDTO.getCategoria());
        return cliente;
    }

    // Método para converter Cliente (entidade) para ClienteDTO
    private ClienteDTO toDTO(Cliente cliente)
    {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(cliente.getId());
        clienteDTO.setCpf(cliente.getCpf());
        clienteDTO.setNome(cliente.getNome());
        clienteDTO.setDataNascimento(cliente.getDataNascimento());
        clienteDTO.setEndereco(mapearEnderecoDTO(cliente.getEndereco())); // Mapeia Endereco para EnderecoDTO
        clienteDTO.setCategoria(cliente.getCategoria());
        return clienteDTO;
    }

    // Método para mapear EnderecoDTO para Endereco (entidade)
    private Endereco mapearEndereco(EnderecoDTO dto)
    {
        Endereco endereco = new Endereco();
        endereco.setRua(dto.getRua());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());
        endereco.setCep(dto.getCep());
        return endereco;
    }

    // Método para mapear Endereco (entidade) para EnderecoDTO
    private EnderecoDTO mapearEnderecoDTO(Endereco endereco)
    {
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setRua(endereco.getRua());
        enderecoDTO.setNumero(endereco.getNumero());
        enderecoDTO.setComplemento(endereco.getComplemento());
        enderecoDTO.setCidade(endereco.getCidade());
        enderecoDTO.setEstado(endereco.getEstado());
        enderecoDTO.setCep(endereco.getCep());
        return enderecoDTO;
    }
}


