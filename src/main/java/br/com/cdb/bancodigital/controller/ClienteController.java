package br.com.cdb.bancodigital.controller;

import br.com.cdb.bancodigital.dto.ClienteDTO;
import br.com.cdb.bancodigital.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Define que essa classe é um controlador REST
@RequestMapping("/clientes") // Caminho base para todos os endpoints relacionados ao cliente
@RequiredArgsConstructor // Lombok cria automaticamente o construtor para injeção de dependências
public class ClienteController
{
    private final ClienteService clienteService;

    // Endpoint para cadastrar um novo cliente
    @PostMapping // Método POST para cadastrar cliente
    @ResponseStatus(HttpStatus.CREATED) // Retorna status 201 em caso de sucesso
    public ClienteDTO cadastrarCliente(@RequestBody ClienteDTO clienteDTO)
    {
        return clienteService.cadastrarCliente(clienteDTO); // Chama o serviço para cadastrar o cliente
    }

    // Endpoint para listar todos os clientes
    @GetMapping // Método GET para listar clientes
    public List<ClienteDTO> listarTodos()
    {
        return clienteService.listarTodos(); // Chama o serviço para listar todos os clientes
    }

    // Endpoint para buscar um cliente por ID
    @GetMapping("/{id}") // Método GET para buscar cliente por ID
    public ClienteDTO buscarPorId(@PathVariable Long id)
    {
        return clienteService.buscarPorId(id); // Chama o serviço para buscar o cliente
    }

    // Endpoint para atualizar os dados de um cliente
    @PutMapping("/{id}") // Método PUT para atualizar cliente
    public ClienteDTO atualizarCliente(@PathVariable Long id, @RequestBody ClienteDTO clienteDTO)
    {
        return clienteService.atualizarCliente(id, clienteDTO); // Chama o serviço para atualizar o cliente
    }

    // Endpoint para deletar um cliente
    @DeleteMapping("/{id}") // Método DELETE para deletar cliente
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna status 204 em caso de sucesso
    public void deletarCliente(@PathVariable Long id)
    {
        clienteService.deletarCliente(id); // Chama o serviço para deletar o cliente
    }
}
