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

import br.com.aula.DTO.PedidoEntradaDto;
import br.com.aula.DTO.PedidoSaidaDto;
import br.com.aula.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

	private final PedidoService pedidoService;

	public PedidoController(PedidoService service) {
		this.pedidoService = service;
	}

	@Operation(
			summary = "Listar pedidos de compra",
			description = "Retorna uma lista com os pedidos de compra cadastrados."
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "200",
				description = "Pedidos encontrados"
				)
	})
	@GetMapping
	public ResponseEntity<List<PedidoSaidaDto>> getPedidos() {
		List<PedidoSaidaDto> pedidos = this.pedidoService.ListarPedidos();

		return ResponseEntity.ok(pedidos);
	}

	@Operation(
			summary = "Consultar pedido por identificador",
			description = "Retorna os dados do pedido solicitado."
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "200",
				description = "Pedido encontrado"
				),
		@ApiResponse(
				responseCode = "404",
				description = "Pedido não encontrado"
				)
	})
	@GetMapping("/{id}")
	public ResponseEntity<PedidoSaidaDto> getPedidoById(@PathVariable Long id) {
		PedidoSaidaDto pedido = this.pedidoService.ConsultarPedido(id);

		return ResponseEntity.ok(pedido);
	}

	@Operation(
			summary = "Cadastrar um novo pedido de compra",
			description = "Registra um pedido de compra com um ou mais livros."
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
				description = "Livro não encontrado"
				)
	})
	@PostMapping
	public ResponseEntity<PedidoSaidaDto> cadastrar(
			@Valid @RequestBody PedidoEntradaDto dto) {
		PedidoSaidaDto pedido = pedidoService.Cadastrar(dto);
		URI location = URI.create("/api/pedidos/" + pedido.id());

		return ResponseEntity.created(location).body(pedido);
	}

}
