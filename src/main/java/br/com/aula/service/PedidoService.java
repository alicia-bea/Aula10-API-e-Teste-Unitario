package br.com.aula.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.aula.DTO.ItemPedidoEntradaDto;
import br.com.aula.DTO.ItemPedidoSaidaDto;
import br.com.aula.DTO.PedidoEntradaDto;
import br.com.aula.DTO.PedidoSaidaDto;
import br.com.aula.exception.LivroNaoEncontradoException;
import br.com.aula.exception.PedidoNaoEncontradoException;
import br.com.aula.model.ItemPedidoModel;
import br.com.aula.model.LivroModel;
import br.com.aula.model.PedidoModel;
import br.com.aula.repository.LivroRepository;
import br.com.aula.repository.PedidoRepository;

@Service
public class PedidoService {

	private final PedidoRepository pedidoRepositorio;
	private final LivroRepository livroRepositorio;

	public PedidoService(PedidoRepository repositorio, LivroRepository livroRepositorio) {
		this.pedidoRepositorio = repositorio;
		this.livroRepositorio = livroRepositorio;
	}

	@Transactional
	public PedidoSaidaDto Cadastrar(PedidoEntradaDto obj) {
		PedidoModel pedido = new PedidoModel();
		pedido.setNomeCliente(obj.nomeCliente());
		pedido.setDataPedido(LocalDateTime.now());

		for (ItemPedidoEntradaDto itemDto : obj.itens()) {
			LivroModel livro = this.livroRepositorio.findById(itemDto.livroId()).orElseThrow(
					() -> new LivroNaoEncontradoException(itemDto.livroId()));

			ItemPedidoModel item = new ItemPedidoModel();
			item.setLivro(livro);
			item.setQuantidade(itemDto.quantidade());

			pedido.adicionarItem(item);
		}

		PedidoModel itemSalvo = this.pedidoRepositorio.save(pedido);

		return this.ConverterParaDto(itemSalvo);
	}

	@Transactional(readOnly = true)
	public PedidoSaidaDto ConsultarPedido(Long id) {
		PedidoModel pedido = this.pedidoRepositorio.findById(id).orElseThrow(
				() -> new PedidoNaoEncontradoException(id));

		return this.ConverterParaDto(pedido);
	}

	@Transactional(readOnly = true)
	public List<PedidoSaidaDto> ListarPedidos() {
		List<PedidoSaidaDto> resposta = new ArrayList<>();
		List<PedidoModel> itens = this.pedidoRepositorio.findAll();
		for (PedidoModel item : itens) {
			resposta.add(this.ConverterParaDto(item));
		}

		return resposta;
	}

	private PedidoSaidaDto ConverterParaDto(PedidoModel pedido) {
		List<ItemPedidoSaidaDto> itens = new ArrayList<>();
		for (ItemPedidoModel item : pedido.getItens()) {
			itens.add(new ItemPedidoSaidaDto(
					item.getLivro().getId(),
					item.getLivro().getTitulo(),
					item.getQuantidade()));
		}

		return new PedidoSaidaDto(
				pedido.getId(),
				pedido.getNomeCliente(),
				pedido.getDataPedido(),
				itens);
	}

}
