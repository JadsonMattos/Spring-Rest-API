package one.digitalinnovation.gof.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;

/**
 * Implementação da <b>Strategy</b> {@link ClienteService}, a qual pode ser
 * injetada pelo Spring (via {@link Autowired}). Com isso, como essa classe é um
 * {@link Service}, ela será tratada como um <b>Singleton</b>.
 * 
 * @author Usuario
 *
 */
@Service
public class ClienteServiceImpl implements ClienteService{
	
	// TODO Singleton: Injetar os componentes do Spring com @Autowired.
	
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ViaCepService viaCepService;
	
	// TODO Strategy: Implementar os métodos definidos na interface.
	// TODO Facade: Absrtair integrações com subsistemas, provendo uma interface simples.
	
	@Override
	public Iterable<Cliente> buscarTodos(){
		return clienteRepository.findAll();
		
		// FIXME Buscar todos os Clientes.		
	}
	
	@Override
	public Cliente buscarPorId(Long id){
		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente.get();
		
		// FIXME Buscar Cliente por ID.
	}
	
	@Override
	public void inserir(Cliente cliente) {
		salvarClienteComCep(cliente);
	}

	@Override
	public void atualizar(Long id, Cliente cliente) {
		
		// FIXME Buscar Cliente por ID, caso exista:
		
		Optional<Cliente> clienteBd = clienteRepository.findById(id);
		if(clienteBd.isPresent()) {
			salvarClienteComCep(cliente);
			
			// FIXME Verificar se o Endereço do Cliente já existe (pelo CEP).
			// FIXME Caso não exista, integrar com o ViaCEP e persistir o retorno.
			// FIXME Alterar Cliente, vinculando o Endereço (novo ou existente).
		}
	}
	
	@Override
	public void deletar(Long id) {
			
		// FIXME Deletar cliente por ID.
			
		clienteRepository.deleteById(id);
	}
	
	private void salvarClienteComCep(Cliente cliente) {
		
		// FIXME Verificar se o Endereço do Cliente já existe (pelo CEP).
		
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			
			// FIXME Caso não exista, integrar com o ViaCEP e persistir o retorno.
			
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);
		
		// FIXME Inserir Cliente, vinculando o Endereço (novo ou existente).
		
		clienteRepository.save(cliente);
		
	}
}

