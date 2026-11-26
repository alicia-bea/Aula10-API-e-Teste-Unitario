package br.com.aula.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.aula.DTO.AutorSaidaDto;
import br.com.aula.DTO.LivroEntradaDto;
import br.com.aula.DTO.LivroSaidaDto;
import br.com.aula.exception.AutorNaoEncontradoException;
import br.com.aula.exception.LivroJaCadastradoException;
import br.com.aula.exception.LivroNaoEncontradoException;
import br.com.aula.model.AutorModel;
import br.com.aula.model.LivroModel;
import br.com.aula.repository.AutorRepository;
import br.com.aula.repository.LivroRepository;

@Service
public class LivroService {

	private final LivroRepository livroRepositorio;
	private final AutorRepository autorRepositorio;

	public LivroService(LivroRepository repositorio, AutorRepository autorRepositorio) {
		this.livroRepositorio = repositorio;
		this.autorRepositorio = autorRepositorio;
	}

	public LivroSaidaDto Cadastrar(LivroEntradaDto obj) {
		AutorModel autor = this.autorRepositorio.findById(obj.autorId()).orElseThrow(
				() -> new AutorNaoEncontradoException(obj.autorId()));

		if (this.livroRepositorio.existsByTituloIgnoreCase(obj.titulo())) {
			throw new LivroJaCadastradoException(obj.titulo());
		}

		LivroModel livro = new LivroModel();
		livro.setTitulo(obj.titulo());
		livro.setAno(obj.ano());
		livro.setAutor(autor);

		LivroModel itemSalvo = this.livroRepositorio.save(livro);

		return this.ConverterParaDto(itemSalvo);
	}

	public LivroSaidaDto ConsultarLivro(Long id) {
		LivroModel livro = this.livroRepositorio.findById(id).orElseThrow(
				() -> new LivroNaoEncontradoException(id));

		return this.ConverterParaDto(livro);
	}

	public List<LivroSaidaDto> ListarLivros() {
		return this.ConverterLista(this.livroRepositorio.findAll());
	}

	public List<LivroSaidaDto> ListarLivrosPorAutor(Long autorId) {
		if (!this.autorRepositorio.existsById(autorId)) {
			throw new AutorNaoEncontradoException(autorId);
		}

		return this.ConverterLista(this.livroRepositorio.findByAutorId(autorId));
	}

	private List<LivroSaidaDto> ConverterLista(List<LivroModel> itens) {
		List<LivroSaidaDto> resposta = new ArrayList<>();
		for (LivroModel item : itens) {
			resposta.add(this.ConverterParaDto(item));
		}

		return resposta;
	}

	private LivroSaidaDto ConverterParaDto(LivroModel item) {
		AutorModel autor = item.getAutor();

		return new LivroSaidaDto(
				item.getId(),
				item.getTitulo(),
				new AutorSaidaDto(autor.getId(), autor.getNome()),
				item.getAno());
	}

}
