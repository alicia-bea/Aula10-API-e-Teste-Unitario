package br.com.aula.exception;

public class LivroJaCadastradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LivroJaCadastradoException(String titulo) {
		super("Livro já cadastrado. Título: " + titulo);
	}

}
