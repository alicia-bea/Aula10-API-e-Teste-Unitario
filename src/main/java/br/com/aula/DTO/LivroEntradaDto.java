package br.com.aula.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LivroEntradaDto(
		@NotBlank String titulo,
		@NotNull Long autorId,
		@NotNull int ano) {

}
