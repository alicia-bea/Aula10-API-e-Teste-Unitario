package br.com.aula.DTO;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoSaidaDto(
		Long id,
		String nomeCliente,
		LocalDateTime dataPedido,
		List<ItemPedidoSaidaDto> itens) {

}
