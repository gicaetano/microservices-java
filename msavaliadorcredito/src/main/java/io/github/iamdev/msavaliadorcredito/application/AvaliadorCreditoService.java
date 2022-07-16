package io.github.iamdev.msavaliadorcredito.application;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import feign.FeignException;
import io.github.iamdev.msavaliadorcredito.application.ex.DadosClienteNotFoundException;
import io.github.iamdev.msavaliadorcredito.application.ex.ErroComunicacaoMicrosservicesException;
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
	
	public SituacaoCliente obtersituacaoCliente(String cpf) throws DadosClienteNotFoundException, 
																   ErroComunicacaoMicrosservicesException {
		
		try {
			ResponseEntity<DadosCliente> dadosClienteResponse = clientesClient.dadosCliente(cpf);
			ResponseEntity<List<CartaoCliente>> cartoesResponse = cartoesClient.getCartoesByCliente(cpf);
			
			return SituacaoCliente
					.builder()
					.cliente(dadosClienteResponse.getBody())
					.cartoes(cartoesResponse.getBody())
					.build();
		} catch (FeignException.FeignClientException e) {
			int status = e.status();
			if(HttpStatus.NOT_FOUND.value() == status) {
				throw new DadosClienteNotFoundException();
			}
			throw new ErroComunicacaoMicrosservicesException(e.getMessage(),status);
		}
		
	}
	
}
