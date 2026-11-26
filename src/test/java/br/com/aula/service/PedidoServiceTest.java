package br.com.aula.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.aula.DTO.ItemPedidoEntradaDto;
import br.com.aula.DTO.PedidoEntradaDto;
import br.com.aula.DTO.PedidoSaidaDto;
import br.com.aula.exception.LivroNaoEncontradoException;
import br.com.aula.model.LivroModel;
import br.com.aula.model.PedidoModel;
import br.com.aula.repository.LivroRepository;
import br.com.aula.repository.PedidoRepository;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

	@Mock
	private PedidoRepository pedidoRepositorio;

	@Mock
	private LivroRepository livroRepositorio;

	@InjectMocks
	private PedidoService pedidoService;

	@Test
	void deveCadastrarPedidoComSucesso() {
		LivroModel livro = new LivroModel();
		livro.setId(1L);
		livro.setTitulo("Java");

		PedidoEntradaDto entrada = new PedidoEntradaDto(
				"Maria",
				List.of(new ItemPedidoEntradaDto(1L, 2)));

		when(livroRepositorio.findById(1L)).thenReturn(Optional.of(livro));
		when(pedidoRepositorio.save(any(PedidoModel.class))).thenAnswer(invocacao -> {
			PedidoModel pedido = invocacao.getArgument(0);
			pedido.setId(5L);
			return pedido;
		});

		PedidoSaidaDto resultado = pedidoService.Cadastrar(entrada);

		assertEquals(5L, resultado.id());
		assertEquals("Maria", resultado.nomeCliente());
		assertEquals(1, resultado.itens().size());
		assertEquals("Java", resultado.itens().get(0).titulo());
		assertEquals(2, resultado.itens().get(0).quantidade());
	}

	@Test
	void deveLancarExcecaoQuandoLivroDoPedidoNaoEncontrado() {
		PedidoEntradaDto entrada = new PedidoEntradaDto(
				"Maria",
				List.of(new ItemPedidoEntradaDto(99L, 1)));

		when(livroRepositorio.findById(99L)).thenReturn(Optional.empty());

		assertThrows(
				LivroNaoEncontradoException.class,
				() -> pedidoService.Cadastrar(entrada)
		);

		verify(pedidoRepositorio, never()).save(any(PedidoModel.class));
	}

}
