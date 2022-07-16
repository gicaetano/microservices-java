package io.github.iamdev.msavaliadorcredito.application;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.github.iamdev.msavaliadorcredito.domain.model.CartaoCliente;
import io.github.iamdev.msavaliadorcredito.domain.model.DadosCliente;
import io.github.iamdev.msavaliadorcredito.domain.model.SituacaoCliente;
import io.github.iamdev.msavaliadorcredito.infra.clients.CartoesResourceClient;
import io.github.iamdev.msavaliadorcredito.infra.clients.ClienteResourceClient;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {
	
	private final ClienteResourceClient clientesClient;
	private final CartoesResourceClient cartoesClient;
	
	public SituacaoCliente obtersituacaoCliente(String cpf) {
		//ObterDadosCliente - MSCLIENTES
		//ObterCartoesCliente - MSCARTOES
		
		ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosCliente(cpf);
		ResponseEntity<List<CartaoCliente>> cartoesResponse = cartoesClient.getCartoesByCliente(cpf);
		
		return SituacaoCliente
				.builder()
				.cliente(dadosClienteResponse.getBody())
				.cartoes(cartoesResponse.getBody())
				.build();
	}
	
	


}
