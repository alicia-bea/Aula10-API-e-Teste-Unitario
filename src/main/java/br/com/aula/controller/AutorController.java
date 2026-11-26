package br.com.aula.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aula.DTO.AutorEntradaDto;
import br.com.aula.DTO.AutorSaidaDto;
import br.com.aula.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

	private final AutorService autorService;

	public AutorController(AutorService service) {
		this.autorService = service;
	}

	@Operation(
			summary = "Listar autores cadastrados",
			description = "Retorna uma lista com os dados dos autores cadastrados."
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "200",
				description = "Autores encontrados"
				)
	})
	@GetMapping
	public ResponseEntity<List<AutorSaidaDto>> getAutores() {
		List<AutorSaidaDto> autores = this.autorService.ListarAutores();

		return ResponseEntity.ok(autores);
	}

	@Operation(
			summary = "Consultar autor por identificador",
			description = "Retorna os dados do autor solicitado."
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "200",
				description = "Autor encontrado"
				),
		@ApiResponse(
				responseCode = "404",
				description = "Autor não encontrado"
				)
	})
	@GetMapping("/{id}")
	public ResponseEntity<AutorSaidaDto> getAutorById(@PathVariable Long id) {
		AutorSaidaDto autor = this.autorService.ConsultarAutor(id);

		return ResponseEntity.ok(autor);
	}

	@Operation(
			summary = "Cadastrar um novo autor",
			description = "Registra os dados de um novo autor no sistema."
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "201",
				description = "Cadastrado com sucesso"
				),
		@ApiResponse(
				responseCode = "400",
				description = "Dados inválidos"
				)
	})
	@PostMapping
	public ResponseEntity<AutorSaidaDto> cadastrar(
			@Valid @RequestBody AutorEntradaDto dto) {
		AutorSaidaDto autor = autorService.Cadastrar(dto);
		URI location = URI.create("/api/autores/" + autor.id());

		return ResponseEntity.created(location).body(autor);
	}

}
