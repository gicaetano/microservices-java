package io.github.iamdev.msavaliadorcredito.application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<SituacaoCliente> consultaSituacaoCliente(@RequestParam("cpf") String cpf) {
		SituacaoCliente situacaoCliente = avaliadorCreditoService.obtersituacaoCliente(cpf);
		return ResponseEntity.ok(situacaoCliente);
	}

}
