package br.com.aula.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(LivroNaoEncontradoException.class)
	public ResponseEntity<ErroResposta> tratarLivroNaoEncontrado(LivroNaoEncontradoException ex) {
		return this.montarResposta(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(AutorNaoEncontradoException.class)
	public ResponseEntity<ErroResposta> tratarAutorNaoEncontrado(AutorNaoEncontradoException ex) {
		return this.montarResposta(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(PedidoNaoEncontradoException.class)
	public ResponseEntity<ErroResposta> tratarPedidoNaoEncontrado(PedidoNaoEncontradoException ex) {
		return this.montarResposta(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(LivroJaCadastradoException.class)
	public ResponseEntity<ErroResposta> tratarLivroJaCadastrado(LivroJaCadastradoException ex) {
		return this.montarResposta(HttpStatus.CONFLICT, ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErroResposta> tratarDadosInvalidos(MethodArgumentNotValidException ex) {
		// Mensagem genérica: não expomos detalhes internos ao consumidor da API
		return this.montarResposta(HttpStatus.BAD_REQUEST, "Dados inválidos. Verifique os campos informados.");
	}

	private ResponseEntity<ErroResposta> montarResposta(HttpStatus status, String mensagem) {
		ErroResposta corpo = new ErroResposta(status.value(), status.getReasonPhrase(), mensagem);

		return ResponseEntity.status(status).body(corpo);
	}

}
