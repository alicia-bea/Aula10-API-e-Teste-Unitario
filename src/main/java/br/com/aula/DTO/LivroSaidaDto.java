package br.com.aula.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LivroSaidaDto(
		@NotNull Long id,
		@NotBlank String titulo,
		@NotNull AutorSaidaDto autor,
		@NotNull int ano) {

}
