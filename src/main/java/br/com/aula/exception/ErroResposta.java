package br.com.aula.exception;

public record ErroResposta(
		int status,
		String erro,
		String mensagem) {

}
