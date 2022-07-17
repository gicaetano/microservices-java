package io.github.iamdev.msavaliadorcredito.application;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import feign.FeignException.InternalServerError;
import io.github.iamdev.msavaliadorcredito.application.ex.DadosClienteNotFoundException;
import io.github.iamdev.msavaliadorcredito.application.ex.ErroComunicacaoMicrosservicesException;
import io.github.iamdev.msavaliadorcredito.application.ex.ErrosolicitacaoCartaoException;
import io.github.iamdev.msavaliadorcredito.domain.model.DadosAvaliacao;
import io.github.iamdev.msavaliadorcredito.domain.model.DadosSolicitacaoEmissaoCartao;
import io.github.iamdev.msavaliadorcredito.domain.model.ProtocoloSolicitacaoCartao;
import io.github.iamdev.msavaliadorcredito.domain.model.RetornoAvaliacaoCliente;
import io.github.iamdev.msavaliadorcredito.domain.model.SituacaoCliente;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("avaliacoes-credito")
@RequiredArgsConstructor
public class AvaliadorCreditoController {
	
	private final AvaliadorCreditoService avaliadorCreditoService;

	@GetMapping
	public String status() {
		return "OK";
	}
	
	@GetMapping(value = "situacao-cliente", params = "cpf")
	public ResponseEntity consultaSituacaoCliente(@RequestParam("cpf") String cpf) {
		
		try {
			SituacaoCliente situacaoCliente = avaliadorCreditoService.obtersituacaoCliente(cpf);
			return ResponseEntity.ok(situacaoCliente);
		} catch (DadosClienteNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (ErroComunicacaoMicrosservicesException e) {
			return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity realizarAvaliacao(@RequestBody DadosAvaliacao dados) {
		
		try {
			RetornoAvaliacaoCliente retornoAvaliacaoCliente = avaliadorCreditoService.realizarAvaliacao(dados.getCpf(), dados.getRenda());
			return ResponseEntity.ok(retornoAvaliacaoCliente);
		} catch (DadosClienteNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (ErroComunicacaoMicrosservicesException e) {
			return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());
		}
		
	}
	
	@PostMapping("solicitacoes-cartao")
	public ResponseEntity solicitarCartao(@RequestBody DadosSolicitacaoEmissaoCartao dados) {
		try {
			ProtocoloSolicitacaoCartao protocoloSolicitacaoCartao = avaliadorCreditoService
					.solicitarEmissaoDeCartao(dados);
			return ResponseEntity.ok(protocoloSolicitacaoCartao);
		}catch (ErrosolicitacaoCartaoException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());		
		}
	}

}
