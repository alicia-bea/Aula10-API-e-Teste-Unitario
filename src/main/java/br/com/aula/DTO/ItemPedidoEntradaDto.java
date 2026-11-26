package br.com.aula.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemPedidoEntradaDto(
		@NotNull Long livroId,
		@Min(1) int quantidade) {

}
