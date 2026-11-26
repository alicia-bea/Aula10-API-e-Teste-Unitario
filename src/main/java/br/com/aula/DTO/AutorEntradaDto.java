package br.com.aula.DTO;

import jakarta.validation.constraints.NotBlank;

public record AutorEntradaDto(
		@NotBlank String nome) {

}
