package br.com.aula.DTO;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record PedidoEntradaDto(
		@NotBlank String nomeCliente,
		@NotEmpty @Valid List<ItemPedidoEntradaDto> itens) {

}
