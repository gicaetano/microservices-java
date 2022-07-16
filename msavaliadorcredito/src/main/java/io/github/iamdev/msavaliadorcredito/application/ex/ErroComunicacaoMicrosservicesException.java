package io.github.iamdev.msavaliadorcredito.application.ex;

import lombok.Getter;

public class ErroComunicacaoMicrosservicesException extends Exception {
	
	@Getter
	private Integer status;

	public ErroComunicacaoMicrosservicesException(String msg, Integer status) {
		super(msg);
		this.status = status;
	}
	
	

}
