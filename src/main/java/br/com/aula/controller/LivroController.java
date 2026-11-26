package br.com.aula.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.aula.DTO.LivroEntradaDto;
import br.com.aula.DTO.LivroSaidaDto;
import br.com.aula.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

	private final LivroService livroService;

	public LivroController(LivroService service) {
		this.livroService = service;
	}

	@Operation(
			summary = "Listar livros cadastrados",
			description = "Retorna uma lista com os dados dos livros cadastrados. "
					+ "Se o parâmetro autorId for informado, retorna apenas os livros desse autor."
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "200",
				description = "Livros encontrados"
				),
		@ApiResponse(
				responseCode = "404",
				description = "Autor não encontrado (quando autorId é informado)"
				)
	})
	@GetMapping
	public ResponseEntity<List<LivroSaidaDto>> getLivros(
			@RequestParam(required = false) Long autorId) {
		List<LivroSaidaDto> livros = (autorId == null)
				? this.livroService.ListarLivros()
				: this.livroService.ListarLivrosPorAutor(autorId);

		return ResponseEntity.ok(livros);
	}

	@Operation(
			summary = "Consultar livro por identificador",
			description = "Retorna os dados do livro solicitado."
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "200",
				description = "Livro encontrado"
				),
		@ApiResponse(
				responseCode = "404",
				description = "Livro não encontrado"
				)
	})
	@GetMapping("/{id}")
	public ResponseEntity<LivroSaidaDto> getLivroById(@PathVariable Long id) {
		LivroSaidaDto livro = this.livroService.ConsultarLivro(id);

		return ResponseEntity.ok(livro);
	}

	@Operation(
			summary = "Cadastrar um novo livro",
			description = "Registra os dados de um novo livro no sistema."
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "201",
				description = "Cadastrado com sucesso"
				),
		@ApiResponse(
				responseCode = "400",
				description = "Dados inválidos"
				),
		@ApiResponse(
				responseCode = "404",
				description = "Autor não encontrado"
				)
	})
	@PostMapping
	public ResponseEntity<LivroSaidaDto> cadastrar(
			@Valid @RequestBody LivroEntradaDto dto) {
		LivroSaidaDto livro = livroService.Cadastrar(dto);
		URI location = URI.create("/api/livros/" + livro.id());

		return ResponseEntity.created(location).body(livro);
	}

}
