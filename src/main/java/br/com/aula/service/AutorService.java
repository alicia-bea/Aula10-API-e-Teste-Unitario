package br.com.aula.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.aula.DTO.AutorEntradaDto;
import br.com.aula.DTO.AutorSaidaDto;
import br.com.aula.exception.AutorNaoEncontradoException;
import br.com.aula.model.AutorModel;
import br.com.aula.repository.AutorRepository;

@Service
public class AutorService {

	private final AutorRepository autorRepositorio;

	public AutorService(AutorRepository repositorio) {
		this.autorRepositorio = repositorio;
	}

	public AutorSaidaDto Cadastrar(AutorEntradaDto obj) {
		AutorModel autor = new AutorModel();
		autor.setNome(obj.nome());

		AutorModel itemSalvo = this.autorRepositorio.save(autor);

		return this.ConverterParaDto(itemSalvo);
	}

	public AutorSaidaDto ConsultarAutor(Long id) {
		AutorModel autor = this.autorRepositorio.findById(id).orElseThrow(
				() -> new AutorNaoEncontradoException(id));

		return this.ConverterParaDto(autor);
	}

	public List<AutorSaidaDto> ListarAutores() {
		List<AutorSaidaDto> resposta = new ArrayList<>();
		List<AutorModel> itens = this.autorRepositorio.findAll();
		for (AutorModel item : itens) {
			resposta.add(this.ConverterParaDto(item));
		}

		return resposta;
	}

	private AutorSaidaDto ConverterParaDto(AutorModel item) {
		return new AutorSaidaDto(item.getId(), item.getNome());
	}

}
