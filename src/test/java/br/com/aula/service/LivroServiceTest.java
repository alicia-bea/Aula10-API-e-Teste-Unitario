package br.com.aula.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.aula.DTO.LivroEntradaDto;
import br.com.aula.DTO.LivroSaidaDto;
import br.com.aula.exception.AutorNaoEncontradoException;
import br.com.aula.exception.LivroJaCadastradoException;
import br.com.aula.exception.LivroNaoEncontradoException;
import br.com.aula.model.AutorModel;
import br.com.aula.model.LivroModel;
import br.com.aula.repository.AutorRepository;
import br.com.aula.repository.LivroRepository;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

	@Mock
	private LivroRepository livroRepositorio;

	@Mock
	private AutorRepository autorRepositorio;

	@InjectMocks
	private LivroService livroService;

	private AutorModel criarAutor() {
		AutorModel autor = new AutorModel();
		autor.setId(1L);
		autor.setNome("Machado de Assis");

		return autor;
	}

	// ---------- Cadastro de livro ----------

	@Test
	void deveCadastrarLivroComSucesso() {
		LivroEntradaDto entrada = new LivroEntradaDto("Dom Casmurro", 1L, 1899);

		when(autorRepositorio.findById(1L)).thenReturn(Optional.of(criarAutor()));
		when(livroRepositorio.existsByTituloIgnoreCase("Dom Casmurro")).thenReturn(false);
		when(livroRepositorio.save(any(LivroModel.class))).thenAnswer(invocacao -> {
			LivroModel livro = invocacao.getArgument(0);
			livro.setId(10L);
			return livro;
		});

		LivroSaidaDto resultado = livroService.Cadastrar(entrada);

		assertEquals(10L, resultado.id());
		assertEquals("Dom Casmurro", resultado.titulo());
		assertEquals(1899, resultado.ano());
		assertEquals("Machado de Assis", resultado.autor().nome());
		verify(livroRepositorio).save(any(LivroModel.class));
	}

	@Test
	void deveLancarExcecaoQuandoLivroJaCadastrado() {
		LivroEntradaDto entrada = new LivroEntradaDto("Dom Casmurro", 1L, 1899);

		when(autorRepositorio.findById(1L)).thenReturn(Optional.of(criarAutor()));
		when(livroRepositorio.existsByTituloIgnoreCase("Dom Casmurro")).thenReturn(true);

		assertThrows(
				LivroJaCadastradoException.class,
				() -> livroService.Cadastrar(entrada)
		);

		verify(livroRepositorio, never()).save(any(LivroModel.class));
	}

	@Test
	void deveLancarExcecaoQuandoAutorNaoEncontradoAoCadastrar() {
		LivroEntradaDto entrada = new LivroEntradaDto("Dom Casmurro", 99L, 1899);

		when(autorRepositorio.findById(99L)).thenReturn(Optional.empty());

		assertThrows(
				AutorNaoEncontradoException.class,
				() -> livroService.Cadastrar(entrada)
		);

		verify(livroRepositorio, never()).save(any(LivroModel.class));
	}

	// ---------- Consulta de livro (exemplo da aula) ----------

	@Test
	void deveRetornarLivroQuandoEncontrado() {
		LivroModel livro = new LivroModel();
		livro.setId(1L);
		livro.setTitulo("Java");
		livro.setAutor(criarAutor());

		when(livroRepositorio.findById(1L)).thenReturn(Optional.of(livro));

		LivroSaidaDto resultado = livroService.ConsultarLivro(1L);

		assertEquals("Java", resultado.titulo());
	}

	@Test
	void deveLancarExcecaoQuandoLivroNaoEncontrado() {
		when(livroRepositorio.findById(50L)).thenReturn(Optional.empty());

		assertThrows(
				LivroNaoEncontradoException.class,
				() -> livroService.ConsultarLivro(50L)
		);
	}

}
